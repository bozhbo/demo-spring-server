package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.condtion.conds.RoleLvCond;
import com.snail.webgame.game.condtion.conds.TotalChargeCond;
import com.snail.webgame.game.condtion.conds.TotalCostCond;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.WonderXMLInfoMap;
import com.snail.webgame.game.xml.info.WonderXMLInfo;

public class LoadWonderXMLInfo {

	/**
	 * @param rootEle
	 * @return
	 */
	public static void load(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int type = Integer.valueOf(rootEle.attributeValue("Type").trim());
			int goal = Integer.valueOf(rootEle.attributeValue("Goal").trim());
			String prizeNo = rootEle.attributeValue("PrizeNo").trim();

			WonderXMLInfo info = new WonderXMLInfo();
			info.setNo(no);
			info.setWonderType(type);
			info.setGoal(goal);
			info.setPrizeNo(prizeNo);
			
			if (info.getWonderType() == WonderXMLInfo.WONDER_TYPE_CHARGE) {
				info.getConds().add(new TotalChargeCond(goal));
			} else if (info.getWonderType() == WonderXMLInfo.WONDER_TYPE_COST) {
				info.getConds().add(new TotalCostCond(goal));
			} else if (info.getWonderType() == WonderXMLInfo.WONDER_TYPE_PLAN) {
				info.getConds().add(new RoleLvCond(goal));
			}

			WonderXMLInfoMap.addWonderXMLInfo(info);

			// 记录已经使用的bagNO
			PropBagXMLMap.addUsedBagNo(prizeNo);
		}

	}
}
