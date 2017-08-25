package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.GuildUpgradeXmlInfoMap;
import com.snail.webgame.game.xml.info.GuildUpgradeXmlInfo;

public class LoadGuildUpgradeXml {

	public static void load(Element element,boolean modify) {
		if (element == null) {
			throw new RuntimeException("Load GuildUpgrade.xml error! no data!");
		}
		
		GuildUpgradeXmlInfo info = new GuildUpgradeXmlInfo();
		info.setNo(Integer.parseInt(element.attributeValue("No")));
		info.setConstructionPoint(Integer.parseInt(element.attributeValue("ConstructionPoint")));
		info.setMembers(Integer.parseInt(element.attributeValue("Members")));
		info.setVicePresident(Integer.parseInt(element.attributeValue("VicePresident")));
		info.setOffical(Integer.parseInt(element.attributeValue("Offical")));
		
		if(GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(info.getNo()) != null && !modify){
			throw new RuntimeException("Load GuildUpgrade.xml error! there is no = " + info.getNo() + " repeat!");
		}
		
		GuildUpgradeXmlInfoMap.addGuildUpgradeXmlInfo(info);
		
	}
	
}
