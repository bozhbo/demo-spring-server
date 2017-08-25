package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.xml.cache.PlayXMLInfoMap;
import com.snail.webgame.game.xml.info.PlayXMLBattle;
import com.snail.webgame.game.xml.info.PlayXMLInfo;

public class LoadPlayXML {

	public static void loadPlay(Element rootEle,boolean modify) {
		if (rootEle == null) {
			throw new RuntimeException("Load Play.xml error! no data!");
		}

		PlayXMLInfo xmlInfo = new PlayXMLInfo();
		xmlInfo.setNo(Integer.parseInt(rootEle.attributeValue("No")));
		xmlInfo.setResetTimes(Integer.parseInt(rootEle.attributeValue("ResetTimes")));
		xmlInfo.setChallengeTimes(Integer.parseInt(rootEle.attributeValue("ChallengeTimes")));

		List<?> battleList = rootEle.elements("Battle");
		loadBattles(xmlInfo, battleList);

		if (PlayXMLInfoMap.getPlayXMLInfo(xmlInfo.getNo()) != null && !modify) {
			throw new RuntimeException("Load Play.xml error! there is no = " + xmlInfo.getNo() + " repeat!");
		}
		PlayXMLInfoMap.addPlayXMLInfo(xmlInfo);
	}

	private static void loadBattles(PlayXMLInfo xmlInfo, List<?> battleList) {
		if (battleList != null && battleList.size() > 0) {
			Element e = null;
			for (int i = 0; i < battleList.size(); i++) {
				e = (Element) battleList.get(i);
				if (e != null) {
					PlayXMLBattle battle = new PlayXMLBattle();
					battle.setNo(Integer.parseInt(e.attributeValue("No")));
					String BattleCondition = e.attributeValue("BattleCondition");
					if (BattleCondition != null && BattleCondition.length() > 0) {
						battle.setBattleCondition(Integer.parseInt(BattleCondition));
					}

					battle.setDropBag(e.attributeValue("Bag"));
					battle.setCaseDropBag(e.attributeValue("Case"));

					List<?> points = e.elements("Point");
					loadBattlePoints(battle, points);

					if (xmlInfo.getBattles().containsKey(battle.getNo())) {
						throw new RuntimeException("Load Play.xml Battle error!  " + "there is PlayNo = "
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

	private static void loadBattlePoints(PlayXMLBattle battle, List<?> points) {
		if (points != null && points.size() > 0) {
			Element e = null;
			for (int i = 0; i < points.size(); i++) {
				e = (Element) points.get(i);
				if (e != null) {
					battle.getGwNos().add(e.attributeValue("NPC"));
				}
			}
		}
	}

}
