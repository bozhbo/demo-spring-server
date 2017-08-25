package com.snail.webgame.game.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.ToolOpActivityInfo;
import com.snail.webgame.game.info.ToolOpActivityRewardInfo;

public class ToolOpActMap {
	
	/**
	 * 用于判断活动是新增还是修改 
	 */
	private static Map<String, ToolOpActivityInfo> guidMap = new ConcurrentHashMap<String, ToolOpActivityInfo>();
	
	/**
	 * 未开始和进行中的活动
	 */
	private static Map<Integer, ToolOpActivityInfo> opActMap = new ConcurrentHashMap<Integer, ToolOpActivityInfo>();
	
	/**
	 * 过期的活动
	 */
	private static Map<Integer, ToolOpActivityInfo> outTimeMap = new ConcurrentHashMap<Integer, ToolOpActivityInfo>();
	
	/**
	 * 所有活动的奖励
	 */
	private static Map<Integer, Map<Integer, ToolOpActivityRewardInfo>> opActRewardMapById = new ConcurrentHashMap<Integer, Map<Integer, ToolOpActivityRewardInfo>>();
	
	public static void addToolOpActivityInfo(ToolOpActivityInfo toolOpActivityInfo, long now) {
		guidMap.put(toolOpActivityInfo.getActNo(), toolOpActivityInfo);
		
		if (toolOpActivityInfo.getActState() == 0 || now >= toolOpActivityInfo.getEndTime().getTime()) {
			outTimeMap.put(toolOpActivityInfo.getId(), toolOpActivityInfo);
			
			if (toolOpActivityInfo.getActState() == 1) {
				toolOpActivityInfo.setActState(0);
			}
			return;
		}
		opActMap.put(toolOpActivityInfo.getId(), toolOpActivityInfo);
	}
	
	public static void removeToolOpActivityInfo(ToolOpActivityInfo toolOpActInfo) {
		opActMap.remove(toolOpActInfo.getId());
		outTimeMap.remove(toolOpActInfo.getId());
	}
	
	public static void addToolOpActivityRewardInfo(ToolOpActivityRewardInfo toolOpActivityRewardInfo) {
		Map<Integer, ToolOpActivityRewardInfo> map = opActRewardMapById.get(toolOpActivityRewardInfo.getOpActId());
		if (map == null) {
			map = new ConcurrentHashMap<Integer, ToolOpActivityRewardInfo>();
			opActRewardMapById.put(toolOpActivityRewardInfo.getOpActId(), map);
		}
		
		map.put(toolOpActivityRewardInfo.getRewardNo(), toolOpActivityRewardInfo);
	}
	
	public static void clearRewardOfToolOpAct(int opActId) {
		opActRewardMapById.remove(opActId);
	}
	
	/**
	 * 运营时限活动过期
	 * 
	 * @param toolOpActInfo
	 */
	public static void opActIsOutTime(ToolOpActivityInfo toolOpActInfo) {
		opActMap.remove(toolOpActInfo.getId());
		
		outTimeMap.put(toolOpActInfo.getId(), toolOpActInfo);
		
		// 只改变缓存
		toolOpActInfo.setActState(0);
	}

	public static Map<Integer, ToolOpActivityInfo> getOpActMap() {
		return opActMap;
	}
	
	public static ToolOpActivityInfo fetchToolOpActivityInfo(int actId) {
		return opActMap.get(actId);
	}
	
	public static ToolOpActivityInfo fetchOutTimeToolOpAct(int actId) {
		return outTimeMap.get(actId);
	}
	
	public static Map<Integer, ToolOpActivityRewardInfo> fetchRewardsByActId(int actId) {
		return opActRewardMapById.get(actId);
	}
	
	public static ToolOpActivityRewardInfo fetchToolOpActRewardInfo(int actId, int rewardNo) {
		if (opActRewardMapById.get(actId) == null) {
			return null;
		}
		
		return opActRewardMapById.get(actId).get(rewardNo);
	}
	
	public static ToolOpActivityInfo fetchInfoByGuid(String actNo) {
		return guidMap.get(actNo);
	}

}
