package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.MoneyActivity;

/**
 * 金币活动配置文件缓存
 * @author xiasd
 */
public class MoneyActivityMap {

	private static Map<Integer, MoneyActivity> map = new HashMap<Integer, MoneyActivity>();

	public static void addMoneyActivity(MoneyActivity moneyActivity) {
		map.put(moneyActivity.getLevel(), moneyActivity);
	}

	public static MoneyActivity getMoneyActivity(int level) {
		return map.get(level);
	}

	public static Map<Integer, MoneyActivity> getMap() {
		return map;
	}
}
