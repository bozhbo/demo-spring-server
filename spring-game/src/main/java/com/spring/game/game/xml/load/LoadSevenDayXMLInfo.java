package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.SevenDayXMLInfoMap;
import com.snail.webgame.game.xml.info.SevenDayXMLInfo;

public class LoadSevenDayXMLInfo {
	
	/**
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int day = Integer.valueOf(rootEle.attributeValue("Day").trim());
			
			List<?> list = rootEle.elements();
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Element subEle = (Element) list.get(i);
					
					int subNo = Integer.valueOf(subEle.attributeValue("No").trim());
					int type = Integer.valueOf(subEle.attributeValue("Type").trim());
					int gold = Integer.valueOf(subEle.attributeValue("Gold").trim());
					String prizeNo = subEle.attributeValue("PropBag").trim();
					
					SevenDayXMLInfo xmlInfo = new SevenDayXMLInfo();
					xmlInfo.setDay(day);
					xmlInfo.setSubNo(subNo);
					xmlInfo.setType(type);
					xmlInfo.setNeedGold(gold);
					xmlInfo.setPrizeNo(prizeNo);
					
					SevenDayXMLInfoMap.addSevenDayXMLInfo(xmlInfo);
					
					// 记录已经使用的bagNO
					PropBagXMLMap.addUsedBagNo(prizeNo);
				}
			}
			
		}

	}
}
