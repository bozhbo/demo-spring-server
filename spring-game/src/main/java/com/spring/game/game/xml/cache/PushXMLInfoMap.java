package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.PushXMLInfo;

public class PushXMLInfoMap {
	private static Map<Integer, PushXMLInfo> map = new HashMap<Integer, PushXMLInfo>();

	public static void addPushXMLInfo(PushXMLInfo xmlInfo) {
		map.put(xmlInfo.getNo(), xmlInfo);
	}

	public static PushXMLInfo fetchPushXMLInfo(int no) {
		return map.get(no);
	}

	public static Map<Integer, PushXMLInfo> getMap() {
		return map;
	}
}
