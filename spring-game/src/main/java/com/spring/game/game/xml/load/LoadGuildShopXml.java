package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.GuildConstructionXmlInfoMap;
import com.snail.webgame.game.xml.info.GuildShopXmlInfo;

public class LoadGuildShopXml {

	public static void load(Element element,boolean modify) {
		if (element == null) {
			throw new RuntimeException("Load GuildShop.xml error! no data!");
		}
		
		GuildShopXmlInfo info = new GuildShopXmlInfo();
		info.setNo(Integer.parseInt(element.attributeValue("No")));
		info.setLv(Integer.parseInt(element.attributeValue("GuildLv")));

		if (GuildConstructionXmlInfoMap.getGuildShopXmlInfo(info.getNo()) != null && !modify) {
			throw new RuntimeException("Load GuildShop.xml error! there is no = " + info.getNo() + " repeat!");
		}
		
		
		GuildConstructionXmlInfoMap.addGuildShopXmlInfo(info);
		
	}
	
}
