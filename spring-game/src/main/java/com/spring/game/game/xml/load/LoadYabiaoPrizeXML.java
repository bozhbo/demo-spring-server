package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.YabiaoPrizeXMLMap;
import com.snail.webgame.game.xml.info.YabiaoPrizeXMLInfo;

public class LoadYabiaoPrizeXML {

	/**
	 * YaBiaoPrize.xml
	 * 
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int biaocheType = Integer.parseInt(rootEle.attributeValue("No").trim());
			int minRand = Integer.parseInt(rootEle.attributeValue("minRand").trim());
			int maxRand = Integer.parseInt(rootEle.attributeValue("maxRand").trim());
			float speed = Float.valueOf(rootEle.attributeValue("Speed").trim());
			int baseMoney = Integer.parseInt(rootEle.attributeValue("BaseMoney").trim());
			int addMoney = Integer.parseInt(rootEle.attributeValue("AddMoney").trim());

			YabiaoPrizeXMLInfo xmlInfo = new YabiaoPrizeXMLInfo();
			xmlInfo.setBiaocheType(biaocheType);
			xmlInfo.setMinRand(minRand);
			xmlInfo.setMaxRand(maxRand);
			xmlInfo.setSpeed(speed);
			xmlInfo.setBaseMoney(baseMoney);
			xmlInfo.setAddMoney(addMoney);
			
			YabiaoPrizeXMLMap.addXml(xmlInfo);
		}
	}
}
