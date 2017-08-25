package com.snail.webgame.game.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RoleBlackMap {
	//黑名单缓存
	private static Map<Integer, Set<Integer>> map = new ConcurrentHashMap<Integer, Set<Integer>>();
	
	public static void addBlackRoleId(int roleId, int relRoleId){
		if(!map.containsKey(roleId)){
			Set<Integer> set = new HashSet<Integer>();
			map.put(roleId, set);
		}
		
		map.get(roleId).add(relRoleId);
	}
	
	public static Set<Integer> getBlackRoleIdSet(int roleId){
		return map.get(roleId) == null ? new HashSet<Integer>() : map.get(roleId);
	}
	
	public static void removeBlackRoleId(int roleId, int relRoleId){
		if(map.get(roleId) != null){
			map.get(roleId).remove(relRoleId);
		}
	}
	
	public static Set<Integer> getBlackRoleKeySet(){
		return map.keySet();
	}
}
