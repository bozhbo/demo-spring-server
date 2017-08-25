package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.ShopXMLInfo;
import com.snail.webgame.game.xml.info.ShopXMLInfo.ShopRefresh;

public class ShopXMLInfoMap {

	// <no,ShopXMLInfo>
	private static Map<Integer, ShopXMLInfo> map = new HashMap<Integer, ShopXMLInfo>();

	public static void addShopXMLInfo(ShopXMLInfo info) {
		if (map.containsKey(info.getNo())) {
			throw new RuntimeException("Load Shop.xml error! no: " + info.getNo() + " repeat");
		}
		map.put(info.getNo(), info);
	}

	public static Map<Integer, ShopXMLInfo> getMap() {
		return map;
	}

	public static ShopXMLInfo getShopXMLInfo(int no) {
		return map.get(no);
	}

	public static int getMaxRefreshNum(int no) {
		int max = 0;
		ShopXMLInfo info = map.get(no);
		if (info != null) {
			Map<Integer, ShopRefresh> rfMap = info.getShopRefresh();
			if (rfMap != null) {
				for (Integer num : rfMap.keySet()) {
					if (num > max) {
						max = num;
					}
				}
			}

		}

		return max;
	}

	public static ShopRefresh getShopRefresh(int no, int buyNum) {
		ShopXMLInfo info = map.get(no);
		if (info != null) {
			Map<Integer, ShopRefresh> rfMap = info.getShopRefresh();
			if (rfMap != null) {
				return rfMap.get(buyNum);
			}
		}

		return null;
	}

}
