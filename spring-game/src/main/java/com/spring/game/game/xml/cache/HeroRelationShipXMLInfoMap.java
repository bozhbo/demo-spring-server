package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.HeroRelationShipXMLInfo;

public class HeroRelationShipXMLInfoMap {
	
	private static HashMap<Integer, HeroRelationShipXMLInfo> map = new HashMap<Integer, HeroRelationShipXMLInfo>();
	
	public static void addHeroRelationShipXMLInfo(HeroRelationShipXMLInfo info){
		
		map.put(info.getHeroNo(), info);
	}
	
	public static HeroRelationShipXMLInfo getHeroRelationShipXMLInfo(int heroNo){
		
		return map.get(heroNo);
	}

}
