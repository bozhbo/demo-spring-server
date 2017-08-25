package com.snail.webgame.game.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.RoleChargeInfo;

public class RoleChargeMap {

	/**
	 * key：orderId 
	 */
	private static Map<String, RoleChargeInfo> map = new ConcurrentHashMap<String, RoleChargeInfo>();
	
	public static boolean addRoleChargeInfo(RoleChargeInfo info) {
		if (map.containsKey(info.getOrderStr())) {
			map.put(info.getOrderStr(), info);// info里的seqId可能有改动
			return false;
		}
		
		map.put(info.getOrderStr(), info);
		return true;
	}

	public static void removeRoleChargeInfo(String orderIdStr) {
		map.remove(orderIdStr);
	}
	
	public static RoleChargeInfo fetchRoleChargeInfo(String orderIdStr) {
		if (orderIdStr == null) {
			return null;
		}
		return map.get(orderIdStr);
	}
	
	public static RoleChargeInfo fetchRoleChargeInfoBySeqId(long seqId) {
		RoleChargeInfo roleChargeInfo;
		
		for(Entry<String, RoleChargeInfo> entry : map.entrySet()){
			roleChargeInfo = entry.getValue();
			
			if(roleChargeInfo != null && roleChargeInfo.getSeqId() == seqId){
				return roleChargeInfo;
			}
		}
		return null;
	}

	public static Map<String, RoleChargeInfo> getMap() {
		return map;
	}
}
