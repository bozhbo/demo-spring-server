package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.StageMoneyXMLInfoMap;
import com.snail.webgame.game.xml.info.StageMoneyXMLInfo;

public class LoadStageMoneyXML {

	public static void load(String xmlName, Element rootEle,boolean modify) {
		if (rootEle != null) {
			byte id = Byte.valueOf(rootEle.attributeValue("No").trim());
			int minRanking = Integer.valueOf(rootEle.attributeValue("MinRanking").trim());
			int maxRanking = Integer.valueOf(rootEle.attributeValue("MaxRanking").trim());
			int money = Integer.valueOf(rootEle.attributeValue("kuafumoney").trim());

			StageMoneyXMLInfo info = new StageMoneyXMLInfo();
			info.setId(id);
			info.setMinRanking(minRanking);
			info.setMaxRanking(maxRanking);
			info.setMoney(money);
			
			StageMoneyXMLInfoMap.addStageMoneyXMLInfo(info);
		}
	}
}
