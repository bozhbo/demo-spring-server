package com.snail.webgame.game.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.RoleInfo;

public class RoleLoginMap {

	private static ConcurrentHashMap<Integer, RoleInfo> map = new ConcurrentHashMap<Integer, RoleInfo>();

	public static RoleInfo getRoleInfo(int roleId) {
		return map.get(roleId);
	}

	public static void addRoleInfo(int roleId, RoleInfo info) {
		map.put(roleId, info);
	}

	public static void removeRoleInfo(int roleId) {
		map.remove(roleId);
	}

	public static long getSize() {
		return map.size();
	}

	public static void clear() {
		map.clear();
	}

	public static boolean isExitRoleInfo(int roleId) {
		return map.containsKey(roleId);
	}

	public static Set<Integer> getSet() {
		return map.keySet();
	}
	
	public static ConcurrentHashMap<Integer, RoleInfo> getMap(){
		return map;
	}

}
