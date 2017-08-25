package com.snail.webgame.game.info;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClubSceneInfo {
	private int clubId;
	private Map<Integer, Set<Integer>> clubSceneMap; //key sceneId, value HashSet<Integer>(roleId)
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	public Map<Integer, Set<Integer>> getClubSceneMap() {
		if(clubSceneMap == null){
			clubSceneMap = new ConcurrentHashMap<Integer, Set<Integer>>();
		}
		
		return clubSceneMap;
	}
	public void setClubSceneMap(Map<Integer, Set<Integer>> clubSceneMap) {
		this.clubSceneMap = clubSceneMap;
	}
	
}
