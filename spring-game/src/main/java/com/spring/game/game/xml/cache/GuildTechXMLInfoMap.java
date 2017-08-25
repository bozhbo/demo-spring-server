package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.GuildTechXMLInfo;

public class GuildTechXMLInfoMap {
	//key - buildType value - map<key - lv , value - info>;
	private static Map<Integer, Map<Integer, GuildTechXMLInfo>> map = new HashMap<Integer, Map<Integer, GuildTechXMLInfo>>();

	public static void addGuildTechXMLInfo(GuildTechXMLInfo info){
		Map<Integer, GuildTechXMLInfo> buildTypeMap = map.get(info.getBuildType());
		if(buildTypeMap == null){
			buildTypeMap = new HashMap<Integer, GuildTechXMLInfo>();
			map.put(info.getBuildType(), buildTypeMap);
		}
		
		buildTypeMap.put(info.getLv(), info);
		
		
	}

	public static Map<Integer, GuildTechXMLInfo> getGuildTechXMLInfobuildTypeMap(int buildType){
		return map.get(buildType);
	}

	public static GuildTechXMLInfo getGuildTechXMLInfo(int buildType, int lv){
		if(map.get(buildType) != null){
			return map.get(buildType).get(lv);
			
		}
		return null;
	}
	
	/**
	 * 获取XML的No
	 * @param buildType
	 * @param lv
	 * @return
	 */
	public static int getXmkNoByBuildTypeAndLevel(int buildType, int lv){
		if(map.get(buildType) != null){
			GuildTechXMLInfo info =  map.get(buildType).get(lv);
			if(info != null){
				return info.getNo();
			}
			
		}
		
		return 0;
	}
}
