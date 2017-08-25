package com.snail.webgame.game.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.RoleClubInfo;

public class RoleClubInfoMap {
	private static Map<Integer, RoleClubInfo> clubIdMap = new ConcurrentHashMap<Integer, RoleClubInfo>(); //key -clubId
	private static Set<String> nameSet = new HashSet<String>(); //存放公会名字 用于创建公会时重名判断
	
	
	public static void addRoleClubInfo(RoleClubInfo info){
		clubIdMap.put(info.getId(), info);
		nameSet.add(info.getClubName().trim().toUpperCase());
	}
	
	public static RoleClubInfo getRoleClubInfoByClubId(int clubId){
		return clubIdMap.get(clubId);
	}
	
	public static Set<Integer> getRoleClubInfoClubIdKeySet(){
		return clubIdMap.keySet();
	}
	
	public static void removeRoleIdMapByClubId(int clubId){
		clubIdMap.remove(clubId);
	}
	
	public static Set<String> getClubNameSet(){
		return nameSet;
	}
	
	public static String getClubName(int clubId){
		return clubIdMap.get(clubId) == null ? "" : clubIdMap.get(clubId).getClubName();
	}
	
	public static void removeClubName(String clubName){
		nameSet.remove(clubName);
	}

	public static Map<Integer, RoleClubInfo> getAllClub()
	{
		return clubIdMap;
	}
}
