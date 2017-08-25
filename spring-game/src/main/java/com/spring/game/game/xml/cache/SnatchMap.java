package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.xml.info.SnatchInfo;

/**
 * 夺宝活动配置
 */
public class SnatchMap {

	// <propNo,list>
	private static Map<Integer, List<SnatchInfo>> propNoMap = new HashMap<Integer, List<SnatchInfo>>();
	// <patchNo,info>
	private static Map<Integer, SnatchInfo> patchNoMap = new HashMap<Integer, SnatchInfo>();

	public static void addItem(SnatchInfo snatchInfo) {
		if (!propNoMap.containsKey(snatchInfo.getPropNo())) {
			propNoMap.put(snatchInfo.getPropNo(), new ArrayList<SnatchInfo>());
		}
		List<SnatchInfo> patchList = propNoMap.get(snatchInfo.getPropNo());
		patchList.add(snatchInfo);
		patchNoMap.put(snatchInfo.getPatchNo(), snatchInfo);
	}

	public static Map<Integer, List<SnatchInfo>> getPropNoMap() {
		return propNoMap;
	}

	public static SnatchInfo get(int patchNo) {
		if (patchNoMap.containsKey(patchNo)) {
			return patchNoMap.get(patchNo);
		}
		return null;
	}

	public static List<SnatchInfo> getByPropNo(int propNo) {
		if (propNoMap.containsKey(propNo)) {
			return propNoMap.get(propNo);
		}
		return null;
	}
}
