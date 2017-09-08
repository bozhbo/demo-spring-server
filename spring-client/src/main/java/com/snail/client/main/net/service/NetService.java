package com.snail.client.main.net.service;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import com.snail.client.main.net.handler.GameClientHandler;
import com.snail.client.main.net.process.login.LoginProcessor;
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
	private String serverName = "GameServer";
	
	public void init() {
		RoomMessageConfig.addProcessor(new LoginProcessor());
		
		RoomMessageConfig.initProcessor();
	}

	public void checkSession() {
		if (!RoomClient.isConnected(serverName)) {
			connectGame("127.0.0.1", 8088);
		}
	}
	
	public boolean sendMessage(Message message) {
		return RoomClient.sendMessage(serverName, message);
	}
	
	public void connectGame(String ip, int port) {
		synchronized (b) {
			RoomClient.shutdown(serverName);
			
			List<RoomFilterInfo> list = new ArrayList<>();
			list.add(new RoomFilterInfo("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode())));
			//list.add(new RoomFilterInfo("crypto", new CryptoIoFilter()));
			list.add(new RoomFilterInfo("message", new MessageCodecFilter("Game", ByteOrder.BIG_ENDIAN)));
			
			try {
				RoomClient.connect(ip, port, "client", serverName, new GameClientHandler(), list, true, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
