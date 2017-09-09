package com.spring.room.config;

import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.logic.room.RoomConfig;
import com.spring.logic.util.LogicUtil;
import com.spring.room.control.service.RoomWorldService;
import com.spring.room.thread.RoomThread;

public class RoomServerConfig {

	private static RoomThread roomThread;
	
	public static String WORLD_SERVER_IP;
	
	public static int WORLD_SERVER_PORT;
	
	public static String ROOM_SERVER_IP;
	
	public static int ROOM_SERVER_PORT;
	
	public static int ROOM_SERVER_ID = 1;
	
	public static void init() {
		roomThread = new RoomThread(1);
		roomThread.setRoomWorldService(GlobalBeanFactory.getBeanByName(RoomWorldService.class));
		roomThread.start();
		
		WORLD_SERVER_IP = GlobalBeanFactory.getEnvironmentProperty("world.server.ip", "127.0.0.1");
		WORLD_SERVER_PORT = Integer.parseInt(GlobalBeanFactory.getEnvironmentProperty("world.server.port", "7001"));
		ROOM_SERVER_IP = GlobalBeanFactory.getEnvironmentProperty("room.server.ip", "127.0.0.1");
		ROOM_SERVER_PORT = Integer.parseInt(GlobalBeanFactory.getEnvironmentProperty("room.server.port", "5001"));
		ROOM_SERVER_ID = Integer.parseInt(GlobalBeanFactory.getEnvironmentProperty("room.server.id", RoomConfig.getRandom(Integer.MAX_VALUE) + ""));
	
		LogicUtil.initJson();
	}

	public static RoomThread getRoomThread() {
		return roomThread;
	}
	
	
}
