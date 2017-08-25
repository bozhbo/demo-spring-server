package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.xml.info.GoldBuyXMLInfo;
import com.snail.webgame.game.xml.info.GoldBuyXMLRand;
import com.snail.webgame.game.xml.info.GoldBuyXMLRandItem;

public class GoldBuyXMLInfoMap {

	//金币购买
	// <no,GoldBuyXMLRand>
	private static HashMap<Integer, GoldBuyXMLInfo> map = new HashMap<Integer, GoldBuyXMLInfo>();

	// <no,GoldBuyXMLRand>
	private static HashMap<Integer, GoldBuyXMLRand> randMap = new HashMap<Integer, GoldBuyXMLRand>();

	public static void addGoldBuyXMLInfo(GoldBuyXMLInfo info) {
		map.put(info.getNo(), info);
	}

	public static GoldBuyXMLInfo getGoldBuyXMLInfo(int no) {
		return map.get(no);
	}
	
	public static void addGoldBuyXMLRand(GoldBuyXMLRand info) {
		randMap.put(info.getNo(), info);
	}

	public static GoldBuyXMLRand getGoldBuyXMLRand(int mulRandNo) {
		return randMap.get(mulRandNo);
	}

	/**
	 * 获取倍数
	 * @param mulRandNo
	 * @return
	 */
	public static float getMul(int mulRandNo) {
		GoldBuyXMLRand randXML = randMap.get(mulRandNo);
		if (randXML != null) {
			Map<Float, GoldBuyXMLRandItem> mulMap = randXML.getMulMap();
			if (mulMap != null) {
				int min = 0;
				int max = 0;
				for (GoldBuyXMLRandItem item : mulMap.values()) {
					if (min > item.getMinRand()) {
						min = item.getMinRand();
					}
					if (max < item.getMaxRand()) {
						max = item.getMaxRand();
					}
				}
				int rand = RandomUtil.getRandom(min, max);
				for (GoldBuyXMLRandItem item : mulMap.values()) {
					if (rand >= item.getMinRand() && rand <= item.getMaxRand()) {
						return item.getMul();
					}
				}
			}
		}

		return 1;
	}
}
