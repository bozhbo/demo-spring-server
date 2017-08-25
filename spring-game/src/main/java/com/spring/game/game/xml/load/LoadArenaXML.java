package com.snail.webgame.game.xml.load;

import org.dom4j.Element;

import com.snail.webgame.game.common.xml.info.NPCXmlInfo;
import com.snail.webgame.game.common.xml.info.NPCXmlLoader;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.ArenaXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.ArenaXMLBuy;
import com.snail.webgame.game.xml.info.ArenaXMLHisPrize;
import com.snail.webgame.game.xml.info.ArenaXMLPrize;

public class LoadArenaXML {

	public static boolean loadPrize(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.parseInt(rootEle.attributeValue("No").trim());
			int minPlace = Integer.parseInt(rootEle.attributeValue("MinPlace").trim());
			int maxPlace = Integer.parseInt(rootEle.attributeValue("MaxPlace").trim());
			String placeDropNo = rootEle.attributeValue("PlaceDropNo").trim();
			ArenaXMLPrize info = new ArenaXMLPrize();
			info.setNo(no);
			info.setMinPlace(minPlace);
			info.setMaxPlace(maxPlace);
			info.setPlaceDropNoStr(placeDropNo);

			// 记录已经使用的bagNO
			PropBagXMLMap.addUsedBagNo(placeDropNo);

			if (ArenaXMLInfoMap.getArenaXMLPrize(no) != null && !modify) {
				throw new RuntimeException("Load ArenaPrize.xml error! no: " + no + " repeat");
			}
			ArenaXMLInfoMap.addArenaXMLPrize(info);
			return true;
		}
		return false;
	}

	public static boolean loadHisPrize(Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.parseInt(rootEle.attributeValue("No").trim());
			int minPlace = Integer.parseInt(rootEle.attributeValue("MinPlace").trim());
			int maxPlace = Integer.parseInt(rootEle.attributeValue("MaxPlace").trim());
			float goldParam = Float.parseFloat(rootEle.attributeValue("Gold").trim());
			ArenaXMLHisPrize info = new ArenaXMLHisPrize();
			info.setNo(no);
			info.setMinPlace(minPlace);
			info.setMaxPlace(maxPlace);
			info.setGoldParam(goldParam);
			info.setHeroNum(Integer.parseInt(rootEle.attributeValue("NpcNum").trim()));

			String npcNotr = rootEle.attributeValue("Npc").trim();
			if (npcNotr == null) {
				throw new RuntimeException("Load ArenaHisPrize.xml error! no: " + no + " Npc is null");
			}
			String[] npcNos = npcNotr.split(",");
			NPCXmlInfo xmlInfo = null;
			for (String npcNo : npcNos) {
				xmlInfo = NPCXmlLoader.getNpc(Integer.parseInt(npcNo));
				if (xmlInfo == null || xmlInfo.getSoldierType() == null) {
					throw new RuntimeException("Load ArenaHisPrize.xml error! no: " + no + " Npc :" + npcNo + " error");
				}
				byte type = xmlInfo.getSoldierType().getValue();
				if (type < 1 || type > 5) {
					throw new RuntimeException("Load ArenaHisPrize.xml error! no: " + no + " Npc :" + npcNo + " error");
				}
				if (info.getNpcNos().containsKey(type)) {
					throw new RuntimeException("Load ArenaHisPrize.xml error! no: " + no + " Npc repeat");
				}
				info.getNpcNos().put(type, Integer.parseInt(npcNo));
			}
			if (info.getNpcNos().size() != 5) {
				throw new RuntimeException("Load ArenaHisPrize.xml error! no: " + no + " Npc is null");
			}

			if (ArenaXMLInfoMap.getArenaXMLHisPrize(no) != null && !modify) {
				throw new RuntimeException("Load ArenaHisPrize.xml error! no: " + no + " repeat");
			}
			ArenaXMLInfoMap.addArenaXMLHisPrize(info);
			return true;
		}
		return false;
	}

	public static boolean loadBuy(String xmlName, Element rootEle,boolean modify) {
		if (rootEle != null) {
			int no = Integer.parseInt(rootEle.attributeValue("No").trim());
			String conditions = rootEle.attributeValue("Condition").trim();
			int fightNum = Integer.parseInt(rootEle.attributeValue("FightNum").trim());
			ArenaXMLBuy xmlInfo = new ArenaXMLBuy();
			xmlInfo.setNo(no);
			xmlInfo.setConditions(AbstractConditionCheck.generateConds(xmlName, conditions));
			xmlInfo.setFightNum(fightNum);

			if (ArenaXMLInfoMap.getArenaXMLBuy(no) != null && !modify) {
				throw new RuntimeException("Load ArenaBuy.xml error! no: " + no + " repeat");
			}
			ArenaXMLInfoMap.addArenaXMLBuy(xmlInfo);
			return true;
		}
		return false;
	}
}
