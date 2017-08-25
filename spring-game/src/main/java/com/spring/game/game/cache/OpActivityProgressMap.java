package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.info.OpActivityProgressInfo;

public class OpActivityProgressMap {
	
	private Map<Integer, List<OpActivityProgressInfo>> opActProsMap = new HashMap<Integer, List<OpActivityProgressInfo>>();

	// <actId, <rewardNo, info>
	private Map<Integer, Map<Integer, OpActivityProgressInfo>> opActProMapById = new HashMap<Integer, Map<Integer, OpActivityProgressInfo>>();
	
	public void addOpActivityProgressInfo(OpActivityProgressInfo opActivityProgressInfo) {
		List<OpActivityProgressInfo> list = opActProsMap.get(opActivityProgressInfo.getActId());
		if (list == null) {
			list = new ArrayList<OpActivityProgressInfo>();
			opActProsMap.put(opActivityProgressInfo.getActId(), list);
		}
		
		list.add(opActivityProgressInfo);
		
		
		Map<Integer, OpActivityProgressInfo> map = opActProMapById.get(opActivityProgressInfo.getActId());
		if (map == null) {
			map = new HashMap<Integer, OpActivityProgressInfo>();
			opActProMapById.put(opActivityProgressInfo.getActId(), map);
		}
		
		map.put(opActivityProgressInfo.getRewardNo(), opActivityProgressInfo);
		
	}
	
	public OpActivityProgressInfo fetchOpActProInfo(int actId, int rewardNo) {
		Map<Integer, OpActivityProgressInfo> map = opActProMapById.get(actId);
		if (map != null) {
			return map.get(rewardNo);
		}
		
		return null;
	}
	
	public List<OpActivityProgressInfo> fetchOpActProInfoByActId(int actId) {
		return opActProsMap.get(actId);
	}
	
	/**
	 * 根据活动id清除活动信息
	 * 
	 * @param actId
	 */
	public void clearOpActByActId(int actId) {
		opActProsMap.remove(actId);
		opActProMapById.remove(actId);
	}
	
}
