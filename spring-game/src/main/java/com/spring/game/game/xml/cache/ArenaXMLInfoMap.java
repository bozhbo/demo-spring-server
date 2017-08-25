package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.NPCXmlInfo;
import com.snail.webgame.game.common.xml.info.NPCXmlLoader;
import com.snail.webgame.game.configdb.ConfigXmlService;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.xml.info.ArenaXMLBuy;
import com.snail.webgame.game.xml.info.ArenaXMLHisPrize;
import com.snail.webgame.game.xml.info.ArenaXMLPrize;

public class ArenaXMLInfoMap {

	private static HashMap<Integer, ArenaXMLPrize> prizeMap = new HashMap<Integer, ArenaXMLPrize>();
	private static HashMap<Integer, ArenaXMLHisPrize> hisPrizeMap = new HashMap<Integer, ArenaXMLHisPrize>();
	private static HashMap<Integer, ArenaXMLBuy> buyMap = new HashMap<Integer, ArenaXMLBuy>();

	public static void addArenaXMLPrize(ArenaXMLPrize xmlInfo) {
		prizeMap.put(xmlInfo.getNo(), xmlInfo);
	}

	public static void addArenaXMLHisPrize(ArenaXMLHisPrize xmlInfo) {
		hisPrizeMap.put(xmlInfo.getNo(), xmlInfo);
	}

	public static void addArenaXMLBuy(ArenaXMLBuy xmlInfo) {
		buyMap.put(xmlInfo.getNo(), xmlInfo);
	}

	public static ArenaXMLHisPrize getArenaXMLHisPrize(int no) {
		return hisPrizeMap.get(no);
	}

	public static int getHisPrizeMaxPlace() {
		int maxPlace = 0;
		for (int no : hisPrizeMap.keySet()) {
			ArenaXMLHisPrize xmlInfo = hisPrizeMap.get(no);
			if (xmlInfo != null && xmlInfo.getMaxPlace() > maxPlace) {
				maxPlace = xmlInfo.getMaxPlace();
			}
		}
		return maxPlace;
	}

	public static ArenaXMLHisPrize getArenaXMLHisPrizebyPlace(int place) {
		for (int no : hisPrizeMap.keySet()) {
			ArenaXMLHisPrize xmlInfo = hisPrizeMap.get(no);
			if (xmlInfo != null && xmlInfo.getMinPlace() <= place) {
				// 排名区间 0-表示无限大
				if (xmlInfo.getMaxPlace() == 0 || xmlInfo.getMaxPlace() >= place) {
					return xmlInfo;
				}
			}
		}
		return null;
	}

	public static ArenaXMLBuy getArenaXMLBuy(int no) {
		return buyMap.get(no);
	}

	public static ArenaXMLPrize getArenaXMLbyPlace(int place) {
		for (int no : prizeMap.keySet()) {
			ArenaXMLPrize xmlInfo = prizeMap.get(no);
			if (xmlInfo != null && xmlInfo.getMinPlace() <= place) {
				// 排名区间 0-表示无限大
				if (xmlInfo.getMaxPlace() == 0 || xmlInfo.getMaxPlace() >= place) {
					return xmlInfo;
				}
			}
		}
		return null;
	}

	public static ArenaXMLPrize getArenaXMLPrize(int no) {
		return prizeMap.get(no);
	}

	public static HashMap<Integer, ArenaXMLPrize> getPrizeMap() {
		return prizeMap;
	}

	public static HashMap<Integer, ArenaXMLHisPrize> getHisPrizeMap() {
		return hisPrizeMap;
	}

	public static int getMaxBuyNum() {
		int max = 1;
		for (int no : buyMap.keySet()) {
			if (no > max) {
				max = no;
			}
		}
		return max;
	}

	/**
	 * 历史最高排名提升奖励
	 * @param beforeMaxPlace
	 * @param currMaxPlace
	 * @return
	 */
	public static double getHisGoldPrize(int beforeMaxPlace, int currMaxPlace) {
		int begin = currMaxPlace;
		int end = beforeMaxPlace;
		if (begin < 0 || end < begin) {
			return 0;
		}
		double result = 0;
		for (ArenaXMLHisPrize xmlInfo : hisPrizeMap.values()) {
			int min = xmlInfo.getMinPlace();
			int max = xmlInfo.getMaxPlace();
			if (min >= end || max < begin) {
				continue;
			}
			if (min == max) {
				result += xmlInfo.getGoldParam();
			} else {
				if (begin > min) {
					min = begin;
				}
				if (end < max) {
					max = end;
				}
				result += (max - min) * xmlInfo.getGoldParam();
			}
		}
		return result;
	}

	/**
	 * 获取竞技场机器数据属性
	 * @param heroNo
	 * @param place
	 * @return
	 */
	public static HeroPropertyInfo getHeroPro(int heroNo, int place, Map<HeroProType, Double> rate) {
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
		if (heroXMLInfo == null) {
			return null;
		}
		ArenaXMLHisPrize prize = getArenaXMLHisPrizebyPlace(place);
		if (prize == null) {
			return null;
		}
		Integer npcNo = prize.getNpcNo((byte)heroXMLInfo.getHeroType());
		if (npcNo == null) {
			return null;
		}		
		NPCXmlInfo npcXMl = NPCXmlLoader.getNpc(npcNo);
		if (npcXMl != null) {
			HeroPropertyInfo info = NPCXmlLoader.getPropertyInfo(npcXMl);
			if (info != null) {
				return HeroProService.generateHeroTotalProtperty(info, rate);
			}
		}
		return null;
	}
}
