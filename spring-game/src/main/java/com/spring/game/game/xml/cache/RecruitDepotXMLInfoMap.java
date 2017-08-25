package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.xml.info.RecruitDepotXMLInfo;
import com.snail.webgame.game.xml.info.RecruitItemXMLInfo;

public class RecruitDepotXMLInfoMap {

	// <no,RecruitDepotXMLInfo>
	private static HashMap<Integer, RecruitDepotXMLInfo> map = new HashMap<Integer, RecruitDepotXMLInfo>();
	private static Map<Integer, String> broadcastMap = new HashMap<Integer, String>();// 用于抽到特定物品的广播用
	
	//金子抽卡特定次数奖励
	private static Map<Integer,String> goldRecruitPrizeMap = new HashMap<Integer,String>();
	
	//武将抽卡特定次数奖励
	private static Map<Integer,String> heroRecruitPrizeMap = new HashMap<Integer,String>();

	public static void addRecruitDepotXMLInfo(RecruitDepotXMLInfo info,boolean modify) {
		if (map.containsKey(info.getNo()) && !modify) {
			throw new RuntimeException("Load RecruitDepot.xml error! no: "
					+ info.getNo() + " repeat");
		}
		map.put(info.getNo(), info);
	}

	public static RecruitDepotXMLInfo getRecruitDepotXMLInfo(int no) {
		return map.get(no);
	}

	public static List<RecruitItemXMLInfo> getItems(String noStr) {
		if (noStr != null && noStr.length() > 0) {
			List<RecruitItemXMLInfo> items = new ArrayList<RecruitItemXMLInfo>();
			String[] strs = noStr.split(",");
			try {
				for (String str : strs) {
					int depotNo = Integer.parseInt(str);
					RecruitDepotXMLInfo depot = getRecruitDepotXMLInfo(depotNo);
					if (depot != null) {
						items.addAll(depot.getItems());
					}
				}
			} catch (Exception e) {
				return null;
			}
			return items;
		}
		return null;
	}

	public static HashMap<Integer, RecruitDepotXMLInfo> getMap() {
		return map;
	}

	public static void addBroadcast(int no, String name) {
		broadcastMap.put(no, name);
	}

	public static Map<Integer, String> getBroadcastMap() {
		return broadcastMap;
	}

	public static Map<Integer, String> getGoldRecruitPrizeMap() {
		return goldRecruitPrizeMap;
	}

	public static void setGoldRecruitPrizeMap(
			Map<Integer, String> goldRecruitPrizeMap) {
		RecruitDepotXMLInfoMap.goldRecruitPrizeMap = goldRecruitPrizeMap;
	}

	public static Map<Integer, String> getHeroRecruitPrizeMap() {
		return heroRecruitPrizeMap;
	}

	public static void setHeroRecruitPrizeMap(
			Map<Integer, String> heroRecruitPrizeMap) {
		RecruitDepotXMLInfoMap.heroRecruitPrizeMap = heroRecruitPrizeMap;
	}

	
}