package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.VipItemBuyXMLInfoMap;
import com.snail.webgame.game.xml.info.VipItemBuyXMLInfo;

public class LoadVipItemBuyXMLInfo {
	
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			VipItemBuyXMLInfo xmlInfo = new VipItemBuyXMLInfo();
			xmlInfo.setNo(Integer.parseInt(rootEle.attributeValue("No").trim()));
			xmlInfo.setItemId(rootEle.attributeValue("ItemId").trim());
			
			String priceRule = rootEle.attributeValue("PriceRule").trim();
			String[] tempArr = priceRule.split(",");
			for (int i = 1; i <= tempArr.length; i++) {
				xmlInfo.getPriceMap().put(i, Integer.valueOf(tempArr[i - 1]));
			}
			
			String vipRule = rootEle.attributeValue("VipRule").trim();
			tempArr = vipRule.split(",");
			
			int buyMaxNum = 0;
			for (int i = 0; i < tempArr.length; i++) {
				if (buyMaxNum < Integer.valueOf(tempArr[i])) {
					buyMaxNum = Integer.valueOf(tempArr[i]);
				}
				
				xmlInfo.getNumMap().put(i, Integer.valueOf(tempArr[i]));
			}
			
			if (xmlInfo.getPriceMap().size() < buyMaxNum) {
				throw new RuntimeException("Load ItemBuyRule.xml no:" + xmlInfo.getNo() + " PriceRule is low! ");
			}

			if (VipItemBuyXMLInfoMap.fetchVipItemBuyXMLInfo(xmlInfo.getNo()) != null && !modify) {
				throw new RuntimeException("Load ItemBuyRule.xml no:" + xmlInfo.getNo() + " repeat! ");
			}
			
			VipItemBuyXMLInfoMap.addVipItemBuyXMLInfo(xmlInfo);
		}
	}
}
