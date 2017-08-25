package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.LevelGiftXMLInfo;

public class LevelGiftXMLInfoMap {
	private static Map<Integer, LevelGiftXMLInfo> map = new HashMap<Integer, LevelGiftXMLInfo>();

	public static void addLevelGiftXMLInfo(LevelGiftXMLInfo xmlInfo,boolean modify) {
		if (map.containsKey(xmlInfo.getNo()) && !modify) {
			throw new RuntimeException("Load LevelGift.xml error! no: " + xmlInfo.getNo() + " repeat");
		}
		map.put(xmlInfo.getNo(), xmlInfo);
	}
	
	/**
	 * 根据编号获取LevelGiftXMLInfo
	 * 
	 * @param no
	 * @return
	 */
	public static LevelGiftXMLInfo fetchXMLInfoByNo(int no) {
		return map.get(no);
	}

	public static Map<Integer, LevelGiftXMLInfo> getMap() {
		return map;
	}

	public static void setMap(Map<Integer, LevelGiftXMLInfo> map) {
		LevelGiftXMLInfoMap.map = map;
	}

}
