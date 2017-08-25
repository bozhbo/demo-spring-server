package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.GuildConstructionXmlInfo;
import com.snail.webgame.game.xml.info.GuildShopXmlInfo;

public class GuildConstructionXmlInfoMap {
	private static Map<Integer, GuildConstructionXmlInfo> map = new HashMap<Integer, GuildConstructionXmlInfo>();
	private static Map<Integer, GuildShopXmlInfo> clubShopMap = new HashMap<Integer, GuildShopXmlInfo>();

	public static void addGuildConstructionXmlInfo(GuildConstructionXmlInfo info) {
		map.put(info.getNo(), info);
	}

	public static GuildConstructionXmlInfo getGuildConstructionXmlInfo(int no) {
		return map.get(no);
	}
	
	public static void addGuildShopXmlInfo(GuildShopXmlInfo info){
		clubShopMap.put(info.getNo(), info);
	}
	
	public static GuildShopXmlInfo getGuildShopXmlInfo(int no){
		return clubShopMap.get(no);
	}
}
