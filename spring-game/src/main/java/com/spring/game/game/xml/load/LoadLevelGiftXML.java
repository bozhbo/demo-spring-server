package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.LevelGiftXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.LevelGiftXMLInfo;

public class LoadLevelGiftXML {

	/**
	 * LevelGift.xml
	 * 
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int needLv = Integer.valueOf(rootEle.attributeValue("NeedLv").trim());
			String prizeNo = rootEle.attributeValue("PrizeNo").trim();

			LevelGiftXMLInfo info = new LevelGiftXMLInfo();
			info.setNo(no);
			info.setNeedLv(needLv);
			info.setPrizeNo(prizeNo);

			LevelGiftXMLInfoMap.addLevelGiftXMLInfo(info,modify);

			// 记录已经使用的bagNO
			PropBagXMLMap.addUsedBagNo(prizeNo);
		}

	}
}
