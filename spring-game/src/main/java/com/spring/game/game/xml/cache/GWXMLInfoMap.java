package com.snail.webgame.game.xml.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.xml.info.GWXMLInfo;

public class GWXMLInfoMap {

	private static ConcurrentHashMap<String, GWXMLInfo> map = new ConcurrentHashMap<String, GWXMLInfo>();

	public static ConcurrentHashMap<String, GWXMLInfo> getMap() {
		return map;
	}

	public static void addNPCGWXMLInfo(GWXMLInfo GWXML) {
		map.put(GWXML.getNo(), GWXML);
	}

	public static GWXMLInfo getNPCGWXMLInfo(String gwNo) {
		return map.get(gwNo);
	}

}
