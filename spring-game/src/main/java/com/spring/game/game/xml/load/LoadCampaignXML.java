package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.CampaignXMLInfoMap;
import com.snail.webgame.game.xml.info.CampaignXMLBattle;
import com.snail.webgame.game.xml.info.CampaignXMLInfo;

public class LoadCampaignXML {

	public static void loadCampaign(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load Campaign.xml error! no data!");
		}

		CampaignXMLInfo xmlInfo = new CampaignXMLInfo();
		xmlInfo.setNo(Integer.parseInt(rootEle.attributeValue("No")));
		//xmlInfo.setResetTimes(Integer.parseInt(rootEle.attributeValue("ResetTimes")));

		List<?> battleList = rootEle.elements("Battle");
		loadBattles(xmlInfo, battleList);

		if (CampaignXMLInfoMap.getCampaignXMLInfo(xmlInfo.getNo()) != null && !modify) {
			throw new RuntimeException("Load Campaign.xml error! there is no = " + xmlInfo.getNo() + " repeat!");
		}
		CampaignXMLInfoMap.addCampaignXMLInfo(xmlInfo);
	}

	private static void loadBattles(CampaignXMLInfo xmlInfo, List<?> battleList) {
		if (battleList != null && battleList.size() > 0) {
			Element e = null;
			for (int i = 0; i < battleList.size(); i++) {
				e = (Element) battleList.get(i);
				if (e != null) {
					CampaignXMLBattle battle = new CampaignXMLBattle();
					battle.setNo(Integer.parseInt(e.attributeValue("No")));
					battle.setName(e.attributeValue("Name").trim());
					battle.setDropBag(e.attributeValue("Bag"));
					battle.setCaseDropBag(e.attributeValue("Case"));
					battle.setCardBag(e.attributeValue("CardBag"));
					battle.setLvMoney(Integer.parseInt(e.attributeValue("LvMoney")));
					
					battle.setMin(Integer.parseInt(e.attributeValue("Min")));
					battle.setMax(Integer.parseInt(e.attributeValue("Max")));
					battle.setAve(Float.parseFloat(e.attributeValue("Ave")));

					if (xmlInfo.getBattles().containsKey(battle.getNo())) {
						throw new RuntimeException("Load Campaign.xml Battle error!  " + "there is CampaignNo = "
								+ xmlInfo.getNo() + " battleNo = " + battle.getNo() + " repeat!");
					}
					if (xmlInfo.getFristBattleNo() == 0) {
						xmlInfo.setFristBattleNo(battle.getNo());
					}
					if (xmlInfo.getLastBattleNo() < battle.getNo()) {
						xmlInfo.setLastBattleNo(battle.getNo());
					}

					xmlInfo.getBattles().put(battle.getNo(), battle);
				}
			}
		}

	}

}
