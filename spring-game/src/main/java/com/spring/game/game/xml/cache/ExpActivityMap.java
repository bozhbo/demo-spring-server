package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.ExpActivity;

/**
 * 经验活动配置文件缓存
 * @author xiasd
 */
public class ExpActivityMap {

	private static Map<Integer, ExpActivity> map = new HashMap<Integer, ExpActivity>();

	public static void addExpActivity(ExpActivity expActivity) {
		map.put(expActivity.getLevel(), expActivity);
	}

	public static ExpActivity getExpActivity(int level) {
		return map.get(level);
	}

	public static Map<Integer, ExpActivity> getMap() {
		return map;
	}
}
