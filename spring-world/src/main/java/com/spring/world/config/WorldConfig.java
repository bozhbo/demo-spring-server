package com.spring.world.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.logic.room.cache.RoomCahce;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.service.RoomService;
import com.spring.world.room.service.RoomClientService;
import com.spring.world.thread.WorldRoomChooseThread;
import com.spring.world.thread.WorldRoomThread;

public class WorldConfig {

	private static WorldRoomThread worldRoomThread;
	private static Map<RoomTypeEnum, WorldRoomChooseThread> roomChooseThreadMap = new ConcurrentHashMap<>();
	
	public static void init() {
		RoomCahce.init();
		
		worldRoomThread = new WorldRoomThread();
		worldRoomThread.setRoomService(GlobalBeanFactory.getBeanByName(RoomService.class));
		worldRoomThread.setRoomClientService(GlobalBeanFactory.getBeanByName(RoomClientService.class));
		worldRoomThread.start();
		
		RoomTypeEnum[] enums = RoomTypeEnum.values();
		
		for (RoomTypeEnum roomTypeEnum : enums) {
			WorldRoomChooseThread worldRoomChooseThread = new WorldRoomChooseThread(roomTypeEnum);
			worldRoomChooseThread.setRoomService(GlobalBeanFactory.getBeanByName(RoomService.class));
			worldRoomChooseThread.setRoomClientService(GlobalBeanFactory.getBeanByName(RoomClientService.class));
			roomChooseThreadMap.put(roomTypeEnum, worldRoomChooseThread);
			roomChooseThreadMap.get(roomTypeEnum).start();
		}
	}
	
	public static WorldRoomChooseThread getWorldRoomChooseThread(RoomTypeEnum roomTypeEnum) {
		return roomChooseThreadMap.get(roomTypeEnum);
	}
}
