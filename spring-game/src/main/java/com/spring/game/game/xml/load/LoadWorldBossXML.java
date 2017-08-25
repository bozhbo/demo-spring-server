package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.xml.cache.WorldBossXMLInfoMap;
import com.snail.webgame.game.xml.info.WorldBossXMLInfo;

public class LoadWorldBossXML {

	/**
	 * 加载WorldBoss.xml
	 * @param xmlName
	 * @param e
	 */
	public static void loadXml(Element e,boolean modify) 
	{
	
		int no = Integer.parseInt(e.attributeValue("No"));
		int mapCityNo = Integer.parseInt(e.attributeValue("MapCityNpcNo"));
		String beginTime =  e.attributeValue("BeginTime");
		String endTime = e.attributeValue("EndTime");
		String week = e.attributeValue("ActivityTime");
		String npcNo = e.attributeValue("Npc");
		String npcHp = e.attributeValue("Hp");
		String rate = e.attributeValue("Ratio");
		String beginTime2 = e.attributeValue("BeginTime2");
		String endTime2 = e.attributeValue("EndTime2");
		WorldBossXMLInfo info = new WorldBossXMLInfo();
		if(no != 0)
		{
			info.setNo(no);
			info.setMapCityNo(mapCityNo);
			info.setBeginTime(beginTime);
			GameValue.WORLD_BOSS_BEGIN1 = beginTime;
			info.setEndTime(endTime);
			GameValue.WORLD_BOSS_END1 = endTime;
			info.setWeek(week);
			info.setNPCNo(Integer.parseInt(npcNo));
			info.setHp(Long.parseLong(npcHp));
			info.setRate(Float.parseFloat(rate));
			info.setBeginTime2(beginTime2);
			GameValue.WORLD_BOSS_BEGIN2 = beginTime2;
			info.setEndTime2(endTime2);
			GameValue.WORLD_BOSS_END2 = endTime2;
			
		}
		
		WorldBossXMLInfoMap.addWorldBossXMLInfo(info);
	}

}
