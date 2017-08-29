package com.spring.logic.role.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.role.info.RoleInfo;

public class RoleCache {

	private static Map<Integer, RoleInfo> map = new ConcurrentHashMap<>();
	
	public static void addRoleInfo(RoleInfo roleInfo) {
		map.put(roleInfo.getRoleId(), roleInfo);
	}
	
	public static RoleInfo getRoleInfo(int roleId) {
		return map.get(roleId);
	}
	
}
