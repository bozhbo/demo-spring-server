package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.WorldBossXMLInfoMap;
import com.snail.webgame.game.xml.info.WorldBossPrizeInfo;

public class LoadWorldBossPrizeXML {

	/**
	 * 加载WorldBossPrize.xml
	 * @param xmlName
	 * @param e
	 */
	public static void loadXml(Element e,boolean modify) 
	{
	
		int no = Integer.parseInt(e.attributeValue("No"));
		int minPlace = Integer.parseInt(e.attributeValue("MinPlace"));
		int maxPlace = Integer.parseInt(e.attributeValue("MaxPlace"));
		String prize = e.attributeValue("PlaceDropNo");
		
		WorldBossPrizeInfo info = new WorldBossPrizeInfo();
		if(no != 0)
		{
			info.setNo(no);
			info.setMinPlace(minPlace);
			info.setMaxPlace(maxPlace);
			info.setPlaceDropNo(prize);
			
		}
		
		WorldBossXMLInfoMap.addWorldBossPrizeXMLInfo(info);
	}

}
