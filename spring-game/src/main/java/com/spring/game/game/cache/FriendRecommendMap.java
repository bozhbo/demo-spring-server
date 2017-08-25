package com.snail.webgame.game.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FriendRecommendMap {
	//好友推荐缓存 用于刷新去重复
	private static Map<Integer, Set<Integer>> map = new ConcurrentHashMap<Integer, Set<Integer>>();

	public static void addFriendRecommendMap(int roleId, int reRoleId){
		if(map.get(roleId) == null){
			Set<Integer> set = Collections.synchronizedSet(new HashSet<Integer>());
			map.put(roleId, set);
		}
		
		map.get(roleId).add(reRoleId);
	}
	
	public static Set<Integer> getFriendRecommendSet(int roleId){
		return map.get(roleId) != null ? map.get(roleId) : new HashSet<Integer>();
	}
	
	public static void addFriendRecommendSet(int roleId, Set<Integer> set){
		map.put(roleId, set);
	}
	
	public static Set<Integer> getFriendRecommendKeySet(){
		return map.keySet();
	}
	
}
