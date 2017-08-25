package com.snail.webgame.game.cache;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RoleAddRquestMap {
	//好友请求缓存
	private static ConcurrentHashMap<Integer, Set<Integer>> map = new ConcurrentHashMap<Integer, Set<Integer>>();
	
	public static void addRequestRoleId(int roleId, int relRoleId){
		if(!map.containsKey(roleId)){
			Set<Integer> set = new HashSet<Integer>();
			map.put(roleId, set);
		}
		
		map.get(roleId).add(relRoleId);
	}
	
	public static Set<Integer> getAddRequestRoleIdSet(int roleId){
		return map.get(roleId) == null ? new HashSet<Integer>() : map.get(roleId);
	}
	
	public static void removeRequestRoleId(int roleId, int relRoleId){
		if(map.get(roleId) != null){
			map.get(roleId).remove(relRoleId);
		}
	}
	
	public static Set<Integer> getRequestKeySet(){
		return map.keySet();
	}
	
	public static void removeAddRequestRoleIdSet(int roleId){
		map.remove(roleId);
	}
	
}
