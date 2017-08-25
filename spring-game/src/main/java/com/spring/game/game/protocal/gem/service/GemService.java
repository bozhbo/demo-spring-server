package com.snail.webgame.game.protocal.gem.service;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.info.FightGemInfo;
import com.snail.webgame.game.xml.cache.PlayXMLInfoMap;
import com.snail.webgame.game.xml.info.PlayXMLBattle;
import com.snail.webgame.game.xml.info.PlayXMLInfo;

public class GemService {

	/**
	 * 可领取奖励的关卡
	 * @param fightGemInfo
	 * @return
	 */
	public static List<Integer> getPrizeBattleNos(FightGemInfo fightGemInfo) {
		List<Integer> result = new ArrayList<Integer>();
		PlayXMLInfo xmlInfo = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
		if (xmlInfo != null) {
			for (PlayXMLBattle battleXML : xmlInfo.getBattles().values()) {
				int battleNo = battleXML.getNo();
				String dropBag = battleXML.getCaseDropBag();
				if (dropBag == null || dropBag.length() <= 0) {
					continue;
				}
				if (fightGemInfo.getPrizeBattleNos() != null && fightGemInfo.getPrizeBattleNos().contains(battleNo)) {
					continue;
				}
				if (fightGemInfo.getLastFightResult() == 1) {
					if (fightGemInfo.getLastFightBattleNo() < battleNo) {
						continue;
					}
				} else if (fightGemInfo.getLastFightResult() == 2) {
					if (fightGemInfo.getLastFightBattleNo() <= battleNo) {
						continue;
					}
				} else {
					continue;
				}
				result.add(battleNo);
			}
		}
		return result;
	}

}
