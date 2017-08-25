package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.PlayXMLInfo;

public class PlayXMLInfoMap {

	private static HashMap<Integer, PlayXMLInfo> map = new HashMap<Integer, PlayXMLInfo>();

	public static void addPlayXMLInfo(PlayXMLInfo xmlInfo) {
		map.put(xmlInfo.getNo(), xmlInfo);
	}

	public static PlayXMLInfo getPlayXMLInfo(int no) {
		return map.get(no);
	}

}
