package com.snail.webgame.game.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 计费物品
 */
public class ToolItemMap {

	private static ConcurrentHashMap<Integer, String> toolItemMap = new ConcurrentHashMap<Integer, String>();

	/**
	 * 添加计费物品
	 */
	public static void addToolItem(int toolItemNo, String itemNo) {
		toolItemMap.put(toolItemNo, itemNo);
	}

	/**
	 * 获取计费物品
	 * @param toolItemNo
	 * @return
	 */
	public static String getToolItem(int toolItemNo) {
		if(toolItemMap.containsKey(toolItemNo)){
			return toolItemMap.get(toolItemNo);
		}
		return null;
	}
}
