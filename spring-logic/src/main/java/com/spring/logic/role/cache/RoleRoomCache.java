package com.spring.logic.role.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.role.info.RoomRoleInfo;

public class RoleRoomCache {

	private static Map<Integer, RoomRoleInfo> map = new ConcurrentHashMap<>();
	
	public static boolean addRoomRoleInfo(RoomRoleInfo roomRoleInfo) {
		return map.putIfAbsent(roomRoleInfo.getRoleId(), roomRoleInfo) == null;
	}
	
	public static RoomRoleInfo getRoomRoleInfo(int roleId) {
		return map.get(roleId);
	}
	
	public static boolean removeRoomRoleInfo(int roleId) {
		return map.remove(roleId) != null;
	}
}
