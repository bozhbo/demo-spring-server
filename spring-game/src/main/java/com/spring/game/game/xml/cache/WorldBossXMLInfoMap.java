package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.WorldBossPrizeInfo;
import com.snail.webgame.game.xml.info.WorldBossXMLInfo;

public class WorldBossXMLInfoMap {

	//世界boss
	// <week, WorldBossXMLInfo>
	private static HashMap<String, WorldBossXMLInfo> map = new HashMap<String, WorldBossXMLInfo>();
	//世界boss奖励
	private static HashMap<Integer, WorldBossPrizeInfo> prizeMap = new HashMap<Integer, WorldBossPrizeInfo>();

	public static void addWorldBossXMLInfo(WorldBossXMLInfo info) {
		map.put(info.getWeek(), info);
	}

	public static WorldBossXMLInfo getGoldBuyXMLInfo(int no) {
		String weekStr = no+"";
		for(String week : map.keySet())
		{
			if(week.contains(weekStr))
			{
				return map.get(week);
			}
		}
		return null;
	}
	
	public static void addWorldBossPrizeXMLInfo(WorldBossPrizeInfo info) {
		prizeMap.put(info.getNo(), info);
	}
	
	public static Map<Integer, WorldBossPrizeInfo> getWorldBossPrizeXMLInfo() {
		return prizeMap;
	}
	
	public static WorldBossPrizeInfo getWorldBossPrizeXMLInfo(int no) {
		return prizeMap.get(no);
	}
	
	public static String getPrize(int rank)
	{
		String prize = "";
		if(rank > 0)
		{
			for(WorldBossPrizeInfo info : prizeMap.values())
			{
				if(rank >= info.getMinPlace() && rank <= info.getMaxPlace())
				{
					prize = info.getPlaceDropNo();
					break;
				}
			}
		}
		return prize;
	}
}
