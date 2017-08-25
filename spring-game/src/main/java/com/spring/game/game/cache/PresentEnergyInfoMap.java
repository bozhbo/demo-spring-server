package com.snail.webgame.game.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.PresentEnergyInfo;

public class PresentEnergyInfoMap {
	//key roleId value - key - info.getId()[数据库主键Id] value - info
	private static Map<Integer, Map<Integer, PresentEnergyInfo>> map = new ConcurrentHashMap<Integer, Map<Integer, PresentEnergyInfo>>();
	
	public static void addPresentEnergyInfo(int roleId, PresentEnergyInfo info){
		if(map.get(roleId) == null){
			Map<Integer, PresentEnergyInfo> tmp = new ConcurrentHashMap<Integer, PresentEnergyInfo>();
			map.put(roleId, tmp);
		}
		
		map.get(roleId).put(info.getId(), info);
	}
	
	public static Map<Integer, PresentEnergyInfo> getPresentEnergyInfoMap(int roleId){
		return map.get(roleId);
	}
	
	public static void removePresentEnergyInfo(int roleId, int id){
		if(map.get(roleId) != null){
			map.get(roleId).remove(id);
		}
	}
	
	public static Set<Integer> getPresentEnergyInfoKeySet(){
		return map.keySet();
	}
	
	
}
