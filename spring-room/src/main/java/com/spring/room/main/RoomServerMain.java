package com.spring.room.main;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.snail.mina.protocol.client.RoomClient;
import com.snail.mina.protocol.code.RequestDecode;
import com.snail.mina.protocol.code.RequestEncoder;
import com.snail.mina.protocol.filter.MessageCodecFilter;
import com.snail.mina.protocol.filter.ProtocolCodecFilter;
import com.snail.mina.protocol.handler.ClientHandle;
import com.snail.mina.protocol.handler.ServerHandle;
import com.snail.mina.protocol.info.RoomFilterInfo;
import com.snail.mina.protocol.server.RoomServer;
import com.spring.room.io.process.handler.RoomClientSessionHandler;
import com.spring.room.io.process.handler.RoomServerSessionHandler;

public class RoomServerMain {

public static ConfigurableApplicationContext context = null;
	
	public static void start(String[] args) {
		SpringApplication springApplication = new SpringApplication(RoomServerMain.class);

		springApplication.setBannerMode(Mode.LOG);
		springApplication.setLogStartupInfo(true);

		context = springApplication.run(args);
	}

	public static void main(String[] args) {
		start(args);
		
		RoomServer server = new RoomServer();
		List<RoomFilterInfo> list = new ArrayList<>();
		list.add(new RoomFilterInfo("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode())));
		list.add(new RoomFilterInfo("message", new MessageCodecFilter("Game", ByteOrder.BIG_ENDIAN)));
		
		server.start("127.0.0.1", 7002, 4, new ServerHandle(new RoomServerSessionHandler(), Executors.newCachedThreadPool()), false, list);
		
		try {
			RoomClient.connect("127.0.0.1", 7001, "127.0.0.1", "room", "GameServer", new ClientHandle(new RoomClientSessionHandler(), Executors.newCachedThreadPool()), list, true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
