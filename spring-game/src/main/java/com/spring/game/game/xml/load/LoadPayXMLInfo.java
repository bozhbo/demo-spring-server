package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.PayXMLInfoMap;
import com.snail.webgame.game.xml.info.PayXMLInfo;

public class LoadPayXMLInfo {
	
	/**
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			String pid = rootEle.attributeValue("ProductID").trim();
			int payType = Integer.valueOf(rootEle.attributeValue("PayType").trim());
			int moneyCost = Integer.valueOf(rootEle.attributeValue("MoneyCost").trim());
			
			String name = rootEle.attributeValue("Name").trim();
			
			PayXMLInfo info = new PayXMLInfo();
			info.setNo(no);
			info.setPid(pid);
			info.setName(name);
			info.setPayType(payType);
			info.setMoneyCost(moneyCost);
			
			String giftRewardStr = rootEle.attributeValue("GiftReward");
			if (giftRewardStr != null && giftRewardStr.length() > 0) {
				int giftReward = Integer.valueOf(giftRewardStr);
				
				info.setGiftReward(giftReward);
			}
			
			String firstRewardStr = rootEle.attributeValue("FirstReward");
			if (firstRewardStr != null && firstRewardStr.length() > 0) {
				int firstReward = Integer.valueOf(firstRewardStr);
				
				info.setFirstReward(firstReward);
			}
			
			String effectDay = rootEle.attributeValue("EffectDay");
			if (effectDay != null && effectDay.length() > 0) {
				info.setEffectDay(Integer.valueOf(effectDay));
			}
			
			PayXMLInfoMap.addPayXMLInfo(info);
		}

	}
}
