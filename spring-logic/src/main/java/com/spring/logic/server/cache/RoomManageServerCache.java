package com.spring.logic.server.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 房间管理缓存，用于保存每个房间服务器管理的房间，用于掉线后角色退出房间
 * 
 * @author zhoubo
 *
 */
public class RoomManageServerCache {

	private static Map<Integer, Set<Integer>> map = new ConcurrentHashMap<>();
	
	public static void addRoomId(int serverId, int roomId) {
		if (!map.containsKey(serverId)) {
			map.putIfAbsent(serverId, Collections.synchronizedSet(new HashSet<>()));
		} else {
			map.get(serverId).add(roomId);
		}
	}
	
	public static void removeRoomId(int serverId, int roomId) {
		if (map.containsKey(serverId)) {
			map.get(serverId).remove(roomId);
		}
	}
	
	public static Set<Integer> getAllRooms(int serverId) {
		return map.get(serverId);
	}
}
