package com.spring.room.main;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.snail.mina.protocol.client.RoomClient;
import com.snail.mina.protocol.code.RequestDecode;
import com.snail.mina.protocol.code.RequestEncoder;
import com.snail.mina.protocol.filter.MessageCodecFilter;
import com.snail.mina.protocol.filter.ProtocolCodecFilter;
import com.snail.mina.protocol.handler.ClientHandle;
import com.snail.mina.protocol.handler.ServerHandle;
import com.snail.mina.protocol.info.RoomFilterInfo;
import com.snail.mina.protocol.server.RoomServer;
import com.spring.common.ServerName;
import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.logic.gf.GoldFlowerConfig;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.io.process.RoomIoControl;
import com.spring.room.io.process.handler.RoomClientSessionHandler;
import com.spring.room.io.process.handler.RoomServerSessionHandler;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.spring")
public class RoomServerMain {

	public static void start(String[] args) {
		SpringApplication springApplication = new SpringApplication(RoomServerMain.class);

		springApplication.setBannerMode(Mode.LOG);
		springApplication.setLogStartupInfo(true);

		GlobalBeanFactory.context = springApplication.run(args);
	}

	public static void main(String[] args) {
		start(args);

		RoomServerConfig.init();
		GoldFlowerConfig.init();
		
		RoomIoControl roomIoControl = GlobalBeanFactory.getBeanByName("RoomIoControl", RoomIoControl.class);
		roomIoControl.init();

		RoomServer server = new RoomServer();
		List<RoomFilterInfo> list = new ArrayList<>();
		list.add(new RoomFilterInfo("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode())));
		list.add(new RoomFilterInfo("message", new MessageCodecFilter("Game", ByteOrder.BIG_ENDIAN)));

		server.start(RoomServerConfig.ROOM_SERVER_IP, RoomServerConfig.ROOM_SERVER_PORT, 4,
				new ServerHandle(new RoomServerSessionHandler(), Executors.newCachedThreadPool()), false, list);

		try {
			RoomClient.connect(RoomServerConfig.WORLD_SERVER_IP, RoomServerConfig.WORLD_SERVER_PORT,
					RoomServerConfig.ROOM_SERVER_IP, ServerName.ROOM_SERVER_NAME + "-" + RoomServerConfig.ROOM_SERVER_ID, ServerName.GAME_SERVER_NAME,
					new ClientHandle(new RoomClientSessionHandler(), Executors.newCachedThreadPool()), list, true,
					true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Bean("RoomIoControl")
	public RoomIoControl getRoomIoControl() {
		RoomIoControl roomIoControl = new RoomIoControl();
		return roomIoControl;
	}

}
