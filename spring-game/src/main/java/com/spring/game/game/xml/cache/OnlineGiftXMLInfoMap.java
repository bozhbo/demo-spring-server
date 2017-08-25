package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.OnlineGiftXMLInfo;

public class OnlineGiftXMLInfoMap {

	private static HashMap<Integer, OnlineGiftXMLInfo> map = new HashMap<Integer, OnlineGiftXMLInfo>();

	public static void addOnlineGiftXMLInfo(OnlineGiftXMLInfo xmlInfo) {
		map.put(xmlInfo.getNo(), xmlInfo);
	}

	public static OnlineGiftXMLInfo fetchOnlineGiftXMLInfo(int no) {
		return map.get(no);
	}

	public static HashMap<Integer, OnlineGiftXMLInfo> getMap() {
		return map;
	}
	
}
