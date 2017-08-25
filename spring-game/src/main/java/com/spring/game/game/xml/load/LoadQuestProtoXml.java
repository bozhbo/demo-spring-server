package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.QuestProtoXmlInfoMap;
import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;

public class LoadQuestProtoXml {

	public static void load(String xmlName, Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.valueOf(rootEle.attributeValue("No").trim());
			int type = Integer.valueOf(rootEle.attributeValue("Type").trim());
			String showConditions = rootEle.attributeValue("ShowCondition").trim();
			String finishCondition = rootEle.attributeValue("FinishCondition").trim();
			String prizeNo = rootEle.attributeValue("PrizeNo").trim();

			QuestProtoXmlInfo info = new QuestProtoXmlInfo();
			info.setQuestProtoNo(no);
			info.setQuestName(rootEle.attributeValue("Name").trim());
			info.setQuestType(type);
			info.setQuestConds(AbstractConditionCheck.generateConds(xmlName, showConditions));
			info.setFinishConds(AbstractConditionCheck.generateConds(xmlName, finishCondition));
			info.setFinishCondStr(finishCondition);
			info.setPrizeNo(prizeNo);
			if (rootEle.attributeValue("ShowNpc") != null) {
				info.setShowNpc(Integer.valueOf(rootEle.attributeValue("ShowNpc")));
			}
			
			if (rootEle.attributeValue("Cost") != null) {
				info.setNeedCost(Integer.valueOf(rootEle.attributeValue("Cost")));
			}
			
			if (rootEle.attributeValue("Huoyuedu") != null) {
				info.setActiveVal(Integer.valueOf(rootEle.attributeValue("Huoyuedu")));
			}
			
			if (rootEle.attributeValue("NeedProp") != null) {
				info.setOneKeyItemNo(Integer.valueOf(rootEle.attributeValue("NeedProp")));
			}
			
			if (rootEle.attributeValue("Neednum") != null) {
				info.setOneKeyItemNum(Integer.valueOf(rootEle.attributeValue("Neednum")));
			}

			QuestProtoXmlInfoMap.addQuestProtoXmlInfo(info,modify);

			// 记录已经使用的bagNO
			PropBagXMLMap.addUsedBagNo(prizeNo);
		}

	}

}
