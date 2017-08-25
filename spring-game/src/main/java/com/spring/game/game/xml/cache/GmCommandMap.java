package com.snail.webgame.game.xml.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.xml.info.GmCommandInfo;

public class GmCommandMap {

	private static ConcurrentHashMap<String, GmCommandInfo> map = new ConcurrentHashMap<String, GmCommandInfo>();

	public static void addCmdInfo(GmCommandInfo cmdInfo) {
		map.put(cmdInfo.getCmd(), cmdInfo);
	}

	public static GmCommandInfo getCmdInfo(String cmd) {
		return map.get(cmd);
	}

	public static Set<String> getKeySet() {
		return map.keySet();
	}

}
