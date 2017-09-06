package com.spring.logic.server.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.server.info.RoomServerInfo;

public class RoomServerCache {

	public static Map<Integer, RoomServerInfo> map = new ConcurrentHashMap<>();
	
	public static void addRoomServerInfo(RoomServerInfo roomServerInfo) {
		map.put(roomServerInfo.getRoomServerId(), roomServerInfo);
	}
	
	public static RoomServerInfo getRoomServerInfo(int roomServerId) {
		return map.get(roomServerId);
	}
	
	public static void removeRoomServerInfo(int roomServerId) {
		map.remove(roomServerId);
	}
	
	public static Set<Entry<Integer, RoomServerInfo>> getSet() {
		return map.entrySet();
	}
}
