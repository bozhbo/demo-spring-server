package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.info.Team3V3XMLInfo;

public class LoadTeam3V3XML {

	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			if (rootEle.getName().equals("Property")) {
				Team3V3XMLInfo.setDrawBag(rootEle.attributeValue("DrawBag").trim());
				Team3V3XMLInfo.setFailureBag(rootEle.attributeValue("FailureBag").trim());
				Team3V3XMLInfo.setVictoryBag(rootEle.attributeValue("VictoryBag").trim());
				Team3V3XMLInfo.setCardBag(rootEle.attributeValue("CardBag").trim());
			}
		}
	}
}
