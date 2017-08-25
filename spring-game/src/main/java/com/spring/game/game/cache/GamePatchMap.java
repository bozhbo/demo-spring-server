package com.snail.webgame.game.cache;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 类介绍:游戏补丁包缓存,防止加载相同的jar包
 *
 * @author zhoubo
 * @2015年9月21日
 */
public class GamePatchMap {

	private static Map<String, String> map = new ConcurrentHashMap<String, String>();
	
	public static void addGamePatchJar(String jarName, String className) {
		map.put(jarName, className);
	}
	
	public static boolean checkJar(String jarName, String className) {
		if (map.containsKey(jarName)) {
			return false;
		}
		
		Set<Entry<String, String>> set =  map.entrySet();
		
		for (Entry<String, String> entry : set) {
			if (entry.getValue().equals(className)) {
				return false;
			}
		}
	
		return true;
	}
}
