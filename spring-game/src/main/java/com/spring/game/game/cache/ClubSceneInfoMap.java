package com.snail.webgame.game.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.info.ClubSceneInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;

public class ClubSceneInfoMap {
	private static final AtomicInteger id = new AtomicInteger(1);
	private static Map<Integer, ClubSceneInfo> map = new ConcurrentHashMap<Integer, ClubSceneInfo>();//key clubId
	
	public static void addSceneInfo(RoleClubMemberInfo info){
		if(info == null){
			return;
		}
		
		ClubSceneInfo clubSceneInfo  = map.get(info.getClubId());
		
		if(clubSceneInfo == null){
			clubSceneInfo = new ClubSceneInfo();
			map.put(info.getClubId(), clubSceneInfo);
		}
		
		Map<Integer, Set<Integer>> sceneMap = clubSceneInfo.getClubSceneMap();
		
		if(info.getSceneId() > 0){
			//还在原来的场景中
			Set<Integer> set = sceneMap.get(info.getSceneId());
			
			if(set.contains(info.getRoleId())){
				return;
			}
		}
		
		for(Integer sceneId : sceneMap.keySet()){
			Set<Integer> set = sceneMap.get(sceneId);
			if(set == null){
				continue;
			}
			
			if(set.size() < GameValue.SCENE_ROLE_NUM){
				set.add(info.getRoleId());
				info.setSceneId(sceneId);
				return;
			}
			
		}
		
		Set<Integer> set = Collections.synchronizedSet(new HashSet<Integer>());

		
		set.add(info.getRoleId());
		
		int sceneId = id.getAndIncrement();
		
		sceneMap.put(sceneId, set);
		
		info.setSceneId(sceneId);
	}
	
	public static Set<Integer> getSceneRoleSet(int clubId, int sceneId){
		ClubSceneInfo clubSceneInfo  = map.get(clubId);
		if(clubSceneInfo == null){
			return new HashSet<Integer>();
		}
		
		Set<Integer> set = clubSceneInfo.getClubSceneMap().get(sceneId);
		
		return set == null ? new HashSet<Integer>() : set;
		
	}
	
	public static void cleanUpScene(int clubId){
		if(map.containsKey(clubId)){
			map.remove(clubId);
		}
	}
	
	public static ClubSceneInfo getClubSceneInfoByClubId(int clubId){
		return map.get(clubId);
	}
	
}
