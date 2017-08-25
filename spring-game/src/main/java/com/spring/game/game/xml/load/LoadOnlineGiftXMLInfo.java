package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.OnlineGiftXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.OnlineGiftXMLInfo;

public class LoadOnlineGiftXMLInfo {
	
	/**
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int needOnlineTime = Integer.valueOf(rootEle.attributeValue("NeedTime").trim());
			String prizeNo = rootEle.attributeValue("PrizeNo").trim();

			OnlineGiftXMLInfo info = new OnlineGiftXMLInfo();
			info.setNo(no);
			info.setNeedOnlineTime(needOnlineTime);
			info.setPrizeNo(prizeNo);

			OnlineGiftXMLInfoMap.addOnlineGiftXMLInfo(info);
			
			// 记录已经使用的bagNO
			PropBagXMLMap.addUsedBagNo(prizeNo);
		}
	}
}
