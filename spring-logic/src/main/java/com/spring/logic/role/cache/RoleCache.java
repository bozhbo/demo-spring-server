package com.spring.logic.role.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.logic.role.info.RoleInfo;

public class RoleCache {

	private static Map<String, RoleInfo> accountMap = new ConcurrentHashMap<>();
	
	private static Map<Integer, RoleInfo> map = new ConcurrentHashMap<>();
	
	public static void addRoleInfo(RoleInfo roleInfo) {
		accountMap.put(roleInfo.getAccount(), roleInfo);
		map.put(roleInfo.getRoleId(), roleInfo);
	}
	
	public static RoleInfo getRoleInfo(int roleId) {
		return map.get(roleId);
	}
	
	public static RoleInfo getRoleInfo(String account) {
		return accountMap.get(account);
	}
	
}
