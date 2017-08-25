package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.DefendXMLInfo;

/**
 * 防守玩法配置信息
 * @author wanglinhui
 */
public class DefendXMLInfoMap {

	private static HashMap<Integer, DefendXMLInfo> map = new HashMap<Integer, DefendXMLInfo>();

	public static void addDefendXMLInfo(DefendXMLInfo info) {
		map.put(info.getNo(), info);
	}

	public static DefendXMLInfo getDefendXMLInfo(int key) {
		return map.get(key);
	}

	public static HashMap<Integer, DefendXMLInfo> getMap() {
		return map;
	}
}
