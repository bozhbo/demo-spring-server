package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.CheckInPrizeXMLInfo;

public class CheckInPrizeXmlMap {
	
	public static int CHECK_REMEDY_COST = 50;// 补签花费
	public static int CHECK_ALL_PRIZE_NO = 31;// 所有签完之后的大奖编号
	
	public static HashMap<Integer,CheckInPrizeXMLInfo> map = new HashMap<Integer,CheckInPrizeXMLInfo>();
	
	public static void addXmlInfo(CheckInPrizeXMLInfo xmlInfo) {
		if (xmlInfo.getNo() == 32) {
			try {
				// 补签花费
				CHECK_REMEDY_COST = Integer.valueOf(xmlInfo.getBagNoStr());
			} catch (Exception e) {
				throw new RuntimeException("--------------[ checkInPrize.xml no = 32 error ]--------------------");
			}
			
			return;
		}
		
		map.put(xmlInfo.getNo(), xmlInfo);
	}
	
	public static CheckInPrizeXMLInfo getXmlInfo(int no) {
		return map.get(no);
	}

	public static HashMap<Integer, CheckInPrizeXMLInfo> getMap() {
		return map;
	}

}
