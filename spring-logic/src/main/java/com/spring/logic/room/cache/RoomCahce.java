package com.spring.logic.room.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;

/**
 * 房间缓存, 用于管理房间的创建和回收
 * 此缓存由RoomService负责操作
 * 
 * @author Administrator
 *
 */
public class RoomCahce {

	/**
	 * 全部房间
	 */
	private static Map<Integer, RoomInfo> allRoomMap = new ConcurrentHashMap<Integer, RoomInfo>();
	
	/**
	 * 使用中房间
	 */
	private static Map<RoomTypeEnum, Map<Integer, RoomInfo>> playingRoomMap = new ConcurrentHashMap<RoomTypeEnum, Map<Integer, RoomInfo>>();
	
	/**
	 * 使用中房间(用于随机)
	 */
	private static Map<RoomTypeEnum, List<RoomInfo>> playingRoomListMap = new ConcurrentHashMap<RoomTypeEnum, List<RoomInfo>>();
	
	/**
	 * 回收后房间
	 */
	private static Map<Integer, RoomInfo> emptyRoomMap = new ConcurrentHashMap<Integer, RoomInfo>();
	
	/**
	 * 各类型房间人数
	 */
	private static Map<RoomTypeEnum, AtomicInteger> playingRoleMap = new ConcurrentHashMap<RoomTypeEnum, AtomicInteger>();
	
	/**
	 * 房间人数比 1=每人一个房间 0.2=每五人一个房间
	 */
	private static double ROOM_ROLE_RATE = 0.35;

	public static void init() {
		RoomTypeEnum[] enums = RoomTypeEnum.values();
		
		for (RoomTypeEnum roomTypeEnum : enums) {
			playingRoomMap.put(roomTypeEnum, new ConcurrentHashMap<Integer, RoomInfo>());
			playingRoomListMap.put(roomTypeEnum, new ArrayList<>());
			playingRoleMap.put(roomTypeEnum, new AtomicInteger(0));
		}
	}

	public static Map<Integer, RoomInfo> getAllRoomMap() {
		return allRoomMap;
	}

	public static Map<RoomTypeEnum, Map<Integer, RoomInfo>> getPlayingRoomMap() {
		return playingRoomMap;
	}
	
	public static Map<RoomTypeEnum, List<RoomInfo>> getPlayingRoomListMap() {
		return playingRoomListMap;
	}
	
	public static Map<Integer, RoomInfo> getEmptyRoomMap() {
		return emptyRoomMap;
	}
	
	public static void incrementRole(RoomTypeEnum roomTypeEnum) {
		playingRoleMap.get(roomTypeEnum).getAndIncrement();
	}
	
	public static void decrementRole(RoomTypeEnum roomTypeEnum) {
		playingRoleMap.get(roomTypeEnum).getAndDecrement();
	}
	
	public static int getPlayingRooms() {
		return allRoomMap.size() - emptyRoomMap.size();
	}
	
	public static int getPlayingRoomsByType(RoomTypeEnum roomTypeEnum) {
		return playingRoomMap.get(roomTypeEnum).size();
	}
	
	public static boolean needCreateRoom(RoomTypeEnum roomTypeEnum) {
		int rooms = getPlayingRoomsByType(roomTypeEnum);
		
		if (rooms <= 10) {
			return true;
		}
		
		int roles = playingRoleMap.get(roomTypeEnum).get();
		
		if (rooms * 1.0 / roles < ROOM_ROLE_RATE) {
			return true;
		}
		
		return false;
	}
}
