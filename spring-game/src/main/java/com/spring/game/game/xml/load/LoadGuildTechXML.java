package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.GuildTechXMLInfoMap;
import com.snail.webgame.game.xml.info.GuildTechXMLInfo;

public class LoadGuildTechXML {
	public static void load(Element element,boolean modify) {
		if (element == null) {
			throw new RuntimeException("Load GuildTech.xml error! no data!");
		}
		
		GuildTechXMLInfo info = new GuildTechXMLInfo();
		info.setNo(Integer.parseInt(element.attributeValue("No")));
		info.setLv(Integer.parseInt(element.attributeValue("Lv")));
		info.setBuildType(Integer.parseInt(element.attributeValue("BuildType")));
		info.setNeedType(Integer.parseInt(element.attributeValue("NeedType")));
		info.setNeedLv(Integer.parseInt(element.attributeValue("NeedLv")));
		info.setOffical(Integer.parseInt(element.attributeValue("Offical")));
		String cost = element.attributeValue("Cost");
		
		info.setAddType(Integer.parseInt(element.attributeValue("AddType")));
		info.setAddNum(Integer.parseInt(element.attributeValue("AddNum")));
		
		if(cost != null){
			info.setConditions(AbstractConditionCheck.generateConds("GuildTech.xml", cost));
		}
		
		if(info.getBuildType() == 2){
			//公会扩容
			String[] strs = cost.split(";");
			for(String s : strs){
				if(s.split("-").length != 2){
					System.err.println("BuildType : " + info.getBuildType() + " No : " + info.getNo() + " Cost : " + cost);
					continue;
				}
				info.setCost(Integer.parseInt(s.split("-")[1]));
			}
		}
		
		if (GuildTechXMLInfoMap.getGuildTechXMLInfo(info.getBuildType(), info.getLv()) != null && !modify) {
			throw new RuntimeException("Load GuildTech.xml error! there is BuildType = " + info.getBuildType() + " LV " + info.getLv() + " repeat!");
		}
		
		GuildTechXMLInfoMap.addGuildTechXMLInfo(info);
	
	}
}
