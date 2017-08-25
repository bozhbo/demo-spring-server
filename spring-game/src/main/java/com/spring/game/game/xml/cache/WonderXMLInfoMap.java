package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.xml.info.WonderXMLInfo;

public class WonderXMLInfoMap {
	private static Map<Integer, WonderXMLInfo> map = new HashMap<Integer, WonderXMLInfo>();
	private static Map<Integer, List<WonderXMLInfo>> mapByType = new HashMap<Integer, List<WonderXMLInfo>>();

	public static void addWonderXMLInfo(WonderXMLInfo xmlInfo) {
		map.put(xmlInfo.getNo(), xmlInfo);
		
		List<WonderXMLInfo> list = mapByType.get(xmlInfo.getWonderType());
		if (list == null) {
			list = new ArrayList<WonderXMLInfo>();
			mapByType.put(xmlInfo.getWonderType(), list);
		}
		list.add(xmlInfo);
	}

	public static WonderXMLInfo fetchWonderXMLInfo(int no) {
		return map.get(no);
	}
	
	public static List<WonderXMLInfo> fetchWondersByType(int type) {
		return mapByType.get(type);
	}

	public static Map<Integer, WonderXMLInfo> getMap() {
		return map;
	}
	
}
