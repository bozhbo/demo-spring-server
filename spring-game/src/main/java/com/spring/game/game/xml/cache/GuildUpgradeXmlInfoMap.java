package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.GuildUpgradeXmlInfo;

public class GuildUpgradeXmlInfoMap {
	private static Map<Integer, GuildUpgradeXmlInfo> map = new HashMap<Integer, GuildUpgradeXmlInfo>();

	public static void addGuildUpgradeXmlInfo(GuildUpgradeXmlInfo info) {
		map.put(info.getNo(), info);
	}

	public static GuildUpgradeXmlInfo getGuildUpgradeXmlInfo(int no) {
		return map.get(no);
	}
}
