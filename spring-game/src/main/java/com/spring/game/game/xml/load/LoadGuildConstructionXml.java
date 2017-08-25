package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.GuildConstructionXmlInfoMap;
import com.snail.webgame.game.xml.info.GuildConstructionXmlInfo;

public class LoadGuildConstructionXml {

	public static void load(Element element,boolean modify) {
		if (element == null) {
			throw new RuntimeException("Load GuildConstruction.xml error! no data!");
		}
		
		GuildConstructionXmlInfo info = new GuildConstructionXmlInfo();
		info.setNo(Integer.parseInt(element.attributeValue("No")));
		info.setConstructionPoint(Integer.parseInt(element.attributeValue("ConstructionPoint")));
		info.setCostType(Integer.parseInt(element.attributeValue("CostType")));
		info.setCostNum(Integer.parseInt(element.attributeValue("CostNo")));
		info.setClubContribution(Integer.parseInt(element.attributeValue("ClubContribution")));
		info.setVipLv(Integer.parseInt(element.attributeValue("VipLv")));
		
		if (GuildConstructionXmlInfoMap.getGuildConstructionXmlInfo(info.getNo()) != null && !modify) {
			throw new RuntimeException("Load GuildConstruction.xml error! there is no = " + info.getNo() + " repeat!");
		}
		
		GuildConstructionXmlInfoMap.addGuildConstructionXmlInfo(info);
		
	}

}
