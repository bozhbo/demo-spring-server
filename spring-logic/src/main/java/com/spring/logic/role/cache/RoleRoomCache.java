package com.spring.logic.role.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.role.info.RoomRoleInfo;

public class RoleRoomCache {

	private static Map<Integer, RoomRoleInfo> map = new ConcurrentHashMap<>();
	
	public static void addRoomRoleInfo(RoomRoleInfo roomRoleInfo) {
		map.put(roomRoleInfo.getRoleId(), roomRoleInfo);
	}
	
	public static RoomRoleInfo getRoomRoleInfo(int roleId) {
		return map.get(roleId);
	}
	
	public static void removeRoomRoleInfo(int roleId) {
		map.remove(roleId);
	}
}
