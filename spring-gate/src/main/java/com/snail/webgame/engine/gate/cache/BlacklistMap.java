package com.snail.webgame.engine.gate.cache;

import java.util.HashMap;

public class BlacklistMap {

	private static HashMap<String, Long> map = new HashMap<String, Long>();

	/**
	 * 添加黑名单
	 * 
	 * @param ip
	 *            IP地址
	 * @param endTime
	 *            结束时间
	 */
	public static void addBlack(String ip, long endTime) {
		map.put(ip, endTime);
	}

	/**
	 * 判断是否在黑名单里
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isBlack(String ip) {
		if (map.containsKey(ip)) {
			long endTime = map.get(ip);
			if (System.currentTimeMillis() > endTime) {
				map.remove(ip);
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
