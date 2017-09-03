package com.spring.world.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.world.thread.WorldRoomChooseThread;
import com.spring.world.thread.WorldRoomThread;

public class WorldConfig {

	private static WorldRoomThread worldRoomThread;
	private static Map<RoomTypeEnum, WorldRoomChooseThread> roomChooseThreadMap = new ConcurrentHashMap<>();
	
	public static void init() {
		worldRoomThread = new WorldRoomThread();
		worldRoomThread.start();
		
		RoomTypeEnum[] enums = RoomTypeEnum.values();
		
		for (RoomTypeEnum roomTypeEnum : enums) {
			roomChooseThreadMap.put(roomTypeEnum, new WorldRoomChooseThread(roomTypeEnum));
			roomChooseThreadMap.get(roomTypeEnum).start();
		}
	}
	
	public static WorldRoomChooseThread getWorldRoomChooseThread(RoomTypeEnum roomTypeEnum) {
		return roomChooseThreadMap.get(roomTypeEnum);
	}
}
