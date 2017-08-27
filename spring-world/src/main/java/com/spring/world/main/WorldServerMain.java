package com.spring.world.main;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.snail.mina.protocol.code.RequestDecode;
import com.snail.mina.protocol.code.RequestEncoder;
import com.snail.mina.protocol.filter.MessageCodecFilter;
import com.snail.mina.protocol.filter.ProtocolCodecFilter;
import com.snail.mina.protocol.handler.ServerHandle;
import com.snail.mina.protocol.info.RoomFilterInfo;
import com.snail.mina.protocol.server.RoomServer;
import com.spring.world.bean.GlobalBeanFactory;
import com.spring.world.io.WorldIoControl;
import com.spring.world.io.process.handler.GameServerSessionHandler;
import com.spring.world.state.WorldStateControl;

@SpringBootApplication
public class WorldServerMain {

	public static ConfigurableApplicationContext context = null;
	
	public static void start(String[] args) {
		SpringApplication springApplication = new SpringApplication(WorldServerMain.class);

		springApplication.setBannerMode(Mode.LOG);
		springApplication.setLogStartupInfo(true);

		context = springApplication.run(args);
	}

	public static void main(String[] args) {
		start(args);
		
		WorldStateControl worldStateControl = GlobalBeanFactory.getBeanByName("WorldStateControl", WorldStateControl.class);
		worldStateControl.init();
		
		WorldIoControl worldIoControl = GlobalBeanFactory.getBeanByName("WorldIoControl", WorldIoControl.class);
		worldIoControl.init();
		
		RoomServer server = new RoomServer();
		List<RoomFilterInfo> list = new ArrayList<>();
		list.add(new RoomFilterInfo("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode())));
		list.add(new RoomFilterInfo("message", new MessageCodecFilter("Game", ByteOrder.BIG_ENDIAN)));
		
		server.start("127.0.0.1", 7001, 4, new ServerHandle(new GameServerSessionHandler(), Executors.newCachedThreadPool()), false, list);
	}
	
	@Bean("WorldStateControl")
	public WorldStateControl getWorldStateControl() {
		WorldStateControl worldStateControl = new WorldStateControl();
		return worldStateControl;
	}
	
	@Bean("WorldIoControl")
	public WorldIoControl getWorldIoControl() {
		WorldIoControl worldIoControl = new WorldIoControl();
		return worldIoControl;
	}
}
