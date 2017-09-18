package com.spring.world.main;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.snail.mina.protocol.code.RequestDecode;
import com.snail.mina.protocol.code.RequestEncoder;
import com.snail.mina.protocol.filter.MessageCodecFilter;
import com.snail.mina.protocol.filter.ProtocolCodecFilter;
import com.snail.mina.protocol.handler.ServerHandle;
import com.snail.mina.protocol.info.RoomFilterInfo;
import com.snail.mina.protocol.server.RoomServer;
import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.world.config.WorldConfig;
import com.spring.world.dao.RoleDao;
import com.spring.world.io.WorldIoControl;
import com.spring.world.io.process.handler.GameServerSessionHandler;
import com.spring.world.room.service.impl.TestDaoServiceImpl;
import com.spring.world.state.WorldStateControl;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.spring")
public class WorldServerMain {

	public static void start(String[] args) {
		SpringApplication springApplication = new SpringApplication(WorldServerMain.class);

		springApplication.setBannerMode(Mode.LOG);
		springApplication.setLogStartupInfo(true);
		
		GlobalBeanFactory.context = springApplication.run(args);
	}

	public static void main(String[] args) {
		start(args);
		
		WorldConfig.init();
		
		WorldStateControl worldStateControl = GlobalBeanFactory.getBeanByName("WorldStateControl", WorldStateControl.class);
		worldStateControl.init();
		
		WorldIoControl worldIoControl = GlobalBeanFactory.getBeanByName("WorldIoControl", WorldIoControl.class);
		worldIoControl.init();
		
		RoomServer server = new RoomServer();
		List<RoomFilterInfo> list = new ArrayList<>();
		list.add(new RoomFilterInfo("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode())));
		list.add(new RoomFilterInfo("message", new MessageCodecFilter("Game", ByteOrder.BIG_ENDIAN)));
		
		server.start(WorldConfig.WORLD_SERVER_IP, WorldConfig.WORLD_SERVER_PORT, 4, new ServerHandle(GlobalBeanFactory.getBeanByName(GameServerSessionHandler.class), Executors.newCachedThreadPool()), false, list);
	
		TestDaoServiceImpl service = GlobalBeanFactory.getBeanByName(TestDaoServiceImpl.class); 
		service.testNoTransaction();
		service.testTransaction();
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
	
	@Bean("GameServerSessionHandler")
	public GameServerSessionHandler getGameServerSessionHandler() {
		GameServerSessionHandler gameServerSessionHandler = new GameServerSessionHandler();
		return gameServerSessionHandler;
	}
	
	@Bean
	public HikariDataSource dataSource() {
		HikariDataSource ds = (HikariDataSource) DataSourceBuilder.create()
	            .type(HikariDataSource.class).build();
		
		ds.setMaximumPoolSize(100); 
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setJdbcUrl("jdbc:mysql://172.17.14.45:3306/db1?useUnicode=true&amp;zeroDateTimeBehavior=convertToNull&amp;characterEncoding=UTF-8");
		ds.setUsername("root");
		ds.setPassword("123456");
		ds.setConnectionInitSql("select 1");
		ds.setConnectionTestQuery("select 1");
		ds.setIdleTimeout(300000);
		
		Properties properties = ds.getHealthCheckProperties();
		properties.setProperty("connectivityCheckTimeoutMs", "10000");
		ds.setHealthCheckProperties(properties);
		
		return ds;
	}
}
