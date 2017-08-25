package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.RideQuaXMLInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo.UpCostInfo;

public class RideXMLInfoMap {
	
	private static Map<Integer, RideXMLInfo> map = new HashMap<Integer, RideXMLInfo>();
	private static Map<Integer, RideQuaXMLInfo> mapByQua = new HashMap<Integer, RideQuaXMLInfo>();
	
	/**
	 * 升级、进阶消耗信息
	 */
	private static Map<Integer, UpCostInfo> lvUpCostMap = new HashMap<Integer, UpCostInfo>();
	private static Map<Integer, UpCostInfo> quaUpCostMap = new HashMap<Integer, UpCostInfo>();
	
	public static void addRideXMLInfo(RideXMLInfo rideXMLInfo) {
		map.put(rideXMLInfo.getNo(), rideXMLInfo);
	}
	
	public static void addRideQuaXMLInfo(RideQuaXMLInfo rideQuaXMLInfo) {
		mapByQua.put(rideQuaXMLInfo.getNo(), rideQuaXMLInfo);
	}
	
	public static void addLvUpCostInfo(UpCostInfo costInfo) {
		lvUpCostMap.put(costInfo.getLvOrQua(), costInfo);
	}
	
	public static void addQuaUpCostInfo(UpCostInfo costInfo) {
		quaUpCostMap.put(costInfo.getLvOrQua(), costInfo);
	}
	
	/**
	 * 根据编号获得坐骑信息
	 * 
	 * @param no
	 * @return
	 */
	public static RideXMLInfo fetchRideXMLInfo(int no) {
		return map.get(no);
	}
	
	/**
	 * 根据编号获得坐骑进阶信息
	 * 
	 * @param no
	 * @return
	 */
	public static RideQuaXMLInfo fetchRideQuaXMLInfo(int no) {
		return mapByQua.get(no);
	}
	
	/**
	 * 根据等级获取消耗
	 * 
	 * @param lv
	 * @return
	 */
	public static UpCostInfo fetchLvUpCost(int lv) {
		return lvUpCostMap.get(lv);
	}
	
	/**
	 * 根据品阶获取消耗
	 * 
	 * @param qua
	 * @return
	 */
	public static UpCostInfo fetchQuaUpCost(int qua) {
		return quaUpCostMap.get(qua);
	}
}
