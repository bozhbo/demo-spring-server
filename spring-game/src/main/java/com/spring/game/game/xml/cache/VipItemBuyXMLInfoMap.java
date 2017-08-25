package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.VipItemBuyXMLInfo;

public class VipItemBuyXMLInfoMap {
	
	private static Map<Integer, VipItemBuyXMLInfo> map = new HashMap<Integer, VipItemBuyXMLInfo>();
	
	public static void addVipItemBuyXMLInfo(VipItemBuyXMLInfo info) {
		map.put(info.getNo(), info);
	}

	public static Map<Integer, VipItemBuyXMLInfo> getMap() {
		return map;
	}
	
	public static VipItemBuyXMLInfo fetchVipItemBuyXMLInfo(int no) {
		return map.get(no);
	}
}
