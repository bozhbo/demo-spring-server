package com.spring.world.config;

import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.logic.business.service.RoleBusinessService;
import com.spring.logic.role.service.RoleRoomService;
import com.spring.logic.room.cache.RoomCahce;
import com.spring.logic.util.LogicUtil;
import com.spring.world.thread.WorldRobotThread;

public class WorldConfig {
	
	public static String WORLD_SERVER_IP;
	
	public static int WORLD_SERVER_PORT;
	
	public static int WORLD_SERVER_CPU_PROCESS;
	
	public static int ROOM_ROBOT_MIN_COUNT = 10;
	
	public static double ROOM_ROBOT_RATE_COUNT = 1;

	public static void init() {
		RoomCahce.init();
		
		LogicUtil.initJson();
		
		WORLD_SERVER_IP = GlobalBeanFactory.getEnvironmentProperty("world.server.ip", "127.0.0.1");
		WORLD_SERVER_PORT = Integer.parseInt(GlobalBeanFactory.getEnvironmentProperty("world.server.port", "7001"));
		WORLD_SERVER_CPU_PROCESS = Integer.parseInt(GlobalBeanFactory.getEnvironmentProperty("world.server.process", "4"));
	
		WorldRobotThread worldRobotThread = new WorldRobotThread();
		worldRobotThread.setRoleLogicService(GlobalBeanFactory.getBeanByName(RoleBusinessService.class));
		worldRobotThread.setRoleRoomService(GlobalBeanFactory.getBeanByName(RoleRoomService.class));
	
		worldRobotThread.start();
	}
}
