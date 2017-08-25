package com.snail.webgame.game.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RoleFriendMap {
	//好友缓存
	private static Map<Integer, Set<Integer>> map = new ConcurrentHashMap<Integer, Set<Integer>>();
	
	public static void addRoleFriendId(int roleId, int relRoleId){
		if(!map.containsKey(roleId)){
			Set<Integer> set = new HashSet<Integer>();
			map.put(roleId, set);
		}
		
		map.get(roleId).add(relRoleId);
	}
	
	public static Set<Integer> getRoleFriendIdSet(int roleId){
		return map.get(roleId) == null ? new HashSet<Integer>() : map.get(roleId);
	}
	
	public static void removeRoleFriendId(int roleId, int relRoleId){
		if(map.get(roleId) != null){
			map.get(roleId).remove(relRoleId);
		}
	}
	
	public static Set<Integer> getRoleFriendKeySet(){
		return map.keySet();
	}
}
