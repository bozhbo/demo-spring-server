package com.spring.logic.room.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;

public class RoomCahce {

	private static Map<Integer, RoomInfo> allRoomMap = new ConcurrentHashMap<Integer, RoomInfo>();
	private static Map<RoomTypeEnum, Map<Integer, RoomInfo>> playingRoomMap = new ConcurrentHashMap<RoomTypeEnum, Map<Integer, RoomInfo>>();
	private static Map<Integer, RoomInfo> emptyRoomMap = new ConcurrentHashMap<Integer, RoomInfo>();

	public static void init() {
		playingRoomMap.put(RoomTypeEnum.ROOM_TYPE_NEW, new ConcurrentHashMap<Integer, RoomInfo>());
		playingRoomMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL1, new ConcurrentHashMap<Integer, RoomInfo>());
		playingRoomMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL2, new ConcurrentHashMap<Integer, RoomInfo>());
		playingRoomMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL3, new ConcurrentHashMap<Integer, RoomInfo>());
		playingRoomMap.put(RoomTypeEnum.ROOM_TYPE_LEVEL4, new ConcurrentHashMap<Integer, RoomInfo>());
	}

	public static Map<Integer, RoomInfo> getAllRoomMap() {
		return allRoomMap;
	}

	public static Map<RoomTypeEnum, Map<Integer, RoomInfo>> getPlayingRoomMap() {
		return playingRoomMap;
	}

	public static Map<Integer, RoomInfo> getEmptyRoomMap() {
		return emptyRoomMap;
	}
	
	
}
