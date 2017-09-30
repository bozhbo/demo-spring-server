package com.snail.client.web.service;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.snail.client.web.handle.GameClientHandler;
import com.snail.client.web.process.init.InitRoleProcessor;
import com.snail.client.web.process.login.LoginProcessor;
import com.snail.client.web.process.room.init.RoomCommonProcessor;
import com.snail.client.web.process.room.init.RoomJoinProcessor;
import com.snail.mina.protocol.client.RoomClient;
import com.snail.mina.protocol.code.RequestDecode;
import com.snail.mina.protocol.code.RequestEncoder;
import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.filter.MessageCodecFilter;
import com.snail.mina.protocol.filter.ProtocolCodecFilter;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.RoomFilterInfo;

public class NetService {
	
	private byte[] b = new byte[0];
	private Map<String, AtomicBoolean> map = new ConcurrentHashMap<>();
	
	public void init() {
		RoomMessageConfig.addProcessor(new LoginProcessor());
		RoomMessageConfig.addProcessor(new InitRoleProcessor());
		RoomMessageConfig.addProcessor(new RoomCommonProcessor());
		
		RoomMessageConfig.initProcessor();
	}
	
	public boolean isConnected(String serName) {
		return RoomClient.isConnected(serName);
	}
	
	public boolean sendMessage(String serverName, Message message) {
		return RoomClient.sendMessage(serverName, message);
	}
	
	public void connectGame(String ip, int port, String serverName) {
		synchronized (b) {
			AtomicBoolean newValue = new AtomicBoolean(false);
			AtomicBoolean oldValue = map.putIfAbsent(serverName, newValue);
			
			if (oldValue != null) {
				if (oldValue.get()) {
					return;
				} else {
					if (!oldValue.compareAndSet(false, true)) {
						return;
					}
				}
			}
			
			RoomClient.shutdown(serverName);
			
			List<RoomFilterInfo> list = new ArrayList<>();
			list.add(new RoomFilterInfo("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode())));
			//list.add(new RoomFilterInfo("crypto", new CryptoIoFilter()));
			list.add(new RoomFilterInfo("message", new MessageCodecFilter("Game", ByteOrder.BIG_ENDIAN)));
			
			try {
				RoomClient.connect(ip, port, "client", serverName, new GameClientHandler(), list, true, false);
			} catch (Exception e) {
				map.get(serverName).set(false);
				e.printStackTrace();
			}
		}
	}
}
