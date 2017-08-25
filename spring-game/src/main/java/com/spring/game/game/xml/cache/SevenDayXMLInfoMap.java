package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.xml.info.SevenDayXMLInfo;

public class SevenDayXMLInfoMap {
	
	private static List<SevenDayXMLInfo> allList = new ArrayList<SevenDayXMLInfo>();
	private static Map<Integer, Map<Integer, SevenDayXMLInfo>> mapByDay = new HashMap<Integer, Map<Integer, SevenDayXMLInfo>>();
	private static Map<Integer, List<SevenDayXMLInfo>> mapByType = new HashMap<Integer, List<SevenDayXMLInfo>>();

	public static void addSevenDayXMLInfo(SevenDayXMLInfo xmlInfo) {
		Map<Integer, SevenDayXMLInfo> map = mapByDay.get(xmlInfo.getDay());
		if (map == null) {
			map = new HashMap<Integer, SevenDayXMLInfo>();
			mapByDay.put(xmlInfo.getDay(), map);
		}
		map.put(xmlInfo.getSubNo(), xmlInfo);
		
		List<SevenDayXMLInfo> lists = mapByType.get(xmlInfo.getType());
		if (lists == null) {
			lists = new ArrayList<SevenDayXMLInfo>();
			mapByType.put(xmlInfo.getType(), lists);
		}
		lists.add(xmlInfo);
		
		allList.add(xmlInfo);
	}

	public static SevenDayXMLInfo fetchSevenDayXMLInfo(int day, int subNo) {
		 if (mapByDay.get(day) != null) {
			 return mapByDay.get(day).get(subNo);
		 }
		 
		 return null;
	}
	
	public static List<SevenDayXMLInfo> fetchSevenDayByType(int type) {
		return mapByType.get(type);
	}

	public static List<SevenDayXMLInfo> getAllList() {
		return allList;
	}

}
