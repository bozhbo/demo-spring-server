package com.snail.webgame.game.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.ToolBoxInfo;

public class ToolBoxMap {
	
	/**
	 * 用于金币礼包全服更新
	 * key：金币礼包的guid
	 */
	private static Map<Integer, ToolBoxInfo> guidMap = new ConcurrentHashMap<Integer, ToolBoxInfo>();

	private static Map<Integer, Map<Integer, ToolBoxInfo>> boxMapByType = new ConcurrentHashMap<Integer, Map<Integer, ToolBoxInfo>>();
	
	private static Map<Integer, ToolBoxInfo> boxMapById = new ConcurrentHashMap<Integer, ToolBoxInfo>();
	
	public static void addToolBoxInfo(ToolBoxInfo info) {
		Map<Integer, ToolBoxInfo> map = boxMapByType.get(info.getBoxType());
		if (map == null) {
			map = new ConcurrentHashMap<Integer, ToolBoxInfo>();
			boxMapByType.put(info.getBoxType(), map);
		}
		
		if (info.getBoxType() == ToolBoxInfo.TYPE_BOX_CHARGE) {
			map.put(info.getChargeNo(), info);
		} else {
			map.put(info.getId(), info);
		}
		
		boxMapById.put(info.getId(), info);
		
		// 只针对金币礼包
		if (info.getBoxType() == ToolBoxInfo.TYPE_BOX_GOLD) {
			guidMap.put(info.getGuid(), info);
		}
	}
	
	public static ToolBoxInfo fetchBoxInfoById(int type, int id) {
		if (boxMapByType.get(type) == null) {
			return null;
		}
		
		return boxMapByType.get(type).get(id);
	}
	
	public static ToolBoxInfo fetchBoxByGuId(int guid) {
		return guidMap.get(guid);
	}
	
	public static Collection<ToolBoxInfo> fetchBoxInfo(int boxType) {
		if (boxMapByType.get(boxType) == null) {
			return null;
		}
		
		return boxMapByType.get(boxType).values();
	}
	
	public static ToolBoxInfo fetchToolBoxInfoById(int boxId) {
		return boxMapById.get(boxId);
	}

	public static Map<Integer, ToolBoxInfo> getBoxMapById() {
		return boxMapById;
	}
	
}
