package com.snail.webgame.game.xml.load;


import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.AttackAnotherXMLInfoMap;
import com.snail.webgame.game.xml.info.AttackAnotherXMLInfo;

public class LoadAttackAnotherXML {

public static boolean load(Element rootEle,boolean modify) {
		
		if (rootEle != null) {
			int no = Integer.parseInt(rootEle.attributeValue("No").trim());
			int minLevel = Integer.parseInt(rootEle.attributeValue("MinLv").trim());
			int maxLevel = Integer.parseInt(rootEle.attributeValue("MaxLv").trim());
			String easyBag = rootEle.attributeValue("EasyBag");
			String normalBag = rootEle.attributeValue("NormalBag");
			String hardBag = rootEle.attributeValue("HardBag");
			
			AttackAnotherXMLInfo attackAnotherXMLInfo = new AttackAnotherXMLInfo();
			attackAnotherXMLInfo.setNo(no);
			attackAnotherXMLInfo.setMinLevel(minLevel);
			attackAnotherXMLInfo.setMaxLevel(maxLevel);
			attackAnotherXMLInfo.setEasyBag(easyBag);
			attackAnotherXMLInfo.setNormalBag(normalBag);
			attackAnotherXMLInfo.setHardBag(hardBag);
			
			AttackAnotherXMLInfoMap.addAttackAnotherXMLInfo(attackAnotherXMLInfo);
			return true;
		}
		return false;
	}
	
}
