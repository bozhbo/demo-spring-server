package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.info.SubTeamDuigongXMLInfo;
import com.snail.webgame.game.xml.info.TeamDuigongXMLInfo;

public class LoadTeamDuiGongXML {

	public static void load(String xmlName, Element rootEle,boolean modify) {
		if (rootEle != null) {
			if (rootEle.getName().equals("Property")) {
				SubTeamDuigongXMLInfo subTeamDuigongXMLInfo = new SubTeamDuigongXMLInfo();
				subTeamDuigongXMLInfo.setId(Integer.valueOf(rootEle.attributeValue("No").trim()));
				subTeamDuigongXMLInfo.setMaxLevel(Integer.valueOf(rootEle.attributeValue("MaxLv").trim()));
				subTeamDuigongXMLInfo.setMinLevel(Integer.valueOf(rootEle.attributeValue("MinLv").trim()));
				subTeamDuigongXMLInfo.setDrawBag(rootEle.attributeValue("DrawBag").trim());
				subTeamDuigongXMLInfo.setFailureBag(rootEle.attributeValue("FailureBag").trim());
				subTeamDuigongXMLInfo.setVictoryBag(rootEle.attributeValue("VictoryBag").trim());
				subTeamDuigongXMLInfo.setCardBag(rootEle.attributeValue("CardBag").trim());
			
				TeamDuigongXMLInfo.map.put(subTeamDuigongXMLInfo.getId(), subTeamDuigongXMLInfo);
			}
		}
	}
}
