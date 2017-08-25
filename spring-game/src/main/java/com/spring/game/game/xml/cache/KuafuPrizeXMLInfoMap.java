package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.KuafuXMLPrize;

public class KuafuPrizeXMLInfoMap {
	private static HashMap<Integer, KuafuXMLPrize> prizeMap = new HashMap<Integer, KuafuXMLPrize>();
	public static void addKuafuXMLPrize(KuafuXMLPrize xmlInfo) {
		prizeMap.put(xmlInfo.getNo(), xmlInfo);
	}
	public static KuafuXMLPrize getKuafuXMLbyPlace(int place) {
		for (int no : prizeMap.keySet()) {
			KuafuXMLPrize xmlInfo = prizeMap.get(no);
			if (xmlInfo != null && xmlInfo.getMinPlace() <= place) {
				// 排名区间 0-表示无限大
				if (xmlInfo.getMaxPlace() == 0 || xmlInfo.getMaxPlace() >= place) {
					return xmlInfo;
				}
			}
		}
		return null;
	}

	public static KuafuXMLPrize getKuafuXMLPrize(int no) {
		return prizeMap.get(no);
	}

	public static HashMap<Integer, KuafuXMLPrize> getPrizeMap() {
		return prizeMap;
	}
}
