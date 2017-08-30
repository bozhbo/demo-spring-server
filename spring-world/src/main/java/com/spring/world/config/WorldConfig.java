package com.spring.world.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.world.thread.WorldRoomChooseThread;

public class WorldConfig {

	private static Map<RoomTypeEnum, WorldRoomChooseThread> roomChooseThreadMap = new ConcurrentHashMap<>();
	
	public static void init() {
		RoomTypeEnum[] enums = RoomTypeEnum.values();
		
		for (RoomTypeEnum roomTypeEnum : enums) {
			roomChooseThreadMap.put(roomTypeEnum, new WorldRoomChooseThread(roomTypeEnum));
			roomChooseThreadMap.get(roomTypeEnum).start();
		}
	}
}
