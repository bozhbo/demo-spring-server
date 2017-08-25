package com.snail.webgame.game.xml.load;

import java.util.List;

import org.dom4j.Element;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetailPoint;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.ChapterInfo;

public class LoadChallengeBattleXML {

	/**
	 * 加载 Challenge.xml
	 * 
	 * @param e
	 */
	public static void loadChallengeBattle(String xmlName, Element e,boolean modify) {
		byte chapterType = Byte.valueOf(e.attributeValue("No").trim());

		ChallengeBattleXmlInfo xmlInfo = new ChallengeBattleXmlInfo(chapterType);

		List<?> battleElems = e.elements("Chapter");
		if (battleElems != null && battleElems.size() > 0) {

			for (int i = 0; i < battleElems.size(); i++) {
				Element battleElem = (Element) battleElems.get(i);

				int chapterNo = Integer.valueOf(battleElem.attributeValue("No").trim());
				String guildIdStr = battleElem.attributeValue("GuideId");
				int guildId = guildIdStr == null ? 0 : Integer.valueOf(guildIdStr.trim());

				// 战役信息
				ChapterInfo battlesInfo = new ChapterInfo(chapterType, chapterNo, guildId);

				String battleCondition = battleElem.attributeValue("BattleCondition");
				// 需要打赢战场的条件
				List<AbstractConditionCheck> conditions = AbstractConditionCheck.generateConds(xmlName, battleCondition);
				if (conditions != null) {
					battlesInfo.setConds(conditions);
				}
				
				String chest1 = battleElem.attributeValue("Chest1");
				if(chest1 != null){
					battlesInfo.setChest1(chest1);
				}
				String chest2 = battleElem.attributeValue("Chest2");
				if(chest2 != null){
					battlesInfo.setChest2(chest2);
				}
				String chest3 = battleElem.attributeValue("Chest3");
				if(chest3 != null){
					battlesInfo.setChest3(chest3);
				}
				
				String star1 = battleElem.attributeValue("Star1");
				if(star1 != null){
					battlesInfo.setStar1(Integer.parseInt(star1));
				}
				String star2 = battleElem.attributeValue("Star2");
				if(star2 != null){
					battlesInfo.setStar2(Integer.parseInt(star2));
				}
				String star3 = battleElem.attributeValue("Star3");
				if(star3 != null){
					battlesInfo.setStar3(Integer.parseInt(star3));
				}


				// 战役下的战场
				List<?> battleInfoElems = battleElem.elements("Battle");
				if (battleInfoElems != null && battleInfoElems.size() > 0) {
					for (int j = 0; j < battleInfoElems.size(); j++) {
						Element battleDetailElem = (Element) battleInfoElems.get(j);

						int battleNo = Integer.valueOf(battleDetailElem.attributeValue("No").trim());// 战场编号
						String thisGuildIdStr = battleDetailElem.attributeValue("GuideId");
						int thisGuildId = thisGuildIdStr == null ? 0 : Integer.valueOf(thisGuildIdStr.trim());

						String bossName = battleDetailElem.attributeValue("BossName");
						bossName = bossName == null ? "" : bossName;
						BattleDetail battleDetail = new BattleDetail(battleNo, thisGuildId, bossName);
						battleDetail.setBattleName(battleDetailElem.attributeValue("Name").trim());

						String thisBattleConditionStr = battleDetailElem.attributeValue("BattleCondition");
						if(thisBattleConditionStr != null && thisBattleConditionStr.length() > 0){
							String[] thisbattleConditions = thisBattleConditionStr.split(",");
							for(String thisBattleCondition : thisbattleConditions){
								// 需要打赢战场的条件
								List<AbstractConditionCheck> thisConditions = AbstractConditionCheck.generateConds(xmlName, thisBattleCondition);
								if (thisConditions != null) {
									battleDetail.addConds(thisConditions);
								}
							}
						}
						
						String otherCondition = battleDetailElem.attributeValue("OtherCondition");
						// 其它条件
						List<AbstractConditionCheck> otherConditions = AbstractConditionCheck.generateConds(xmlName, otherCondition);
						if (otherConditions != null) {
							battleDetail.setConds2(otherConditions);
						}
						
						String challengeTimes = battleDetailElem.attributeValue("ChallengeTimes");//每日可攻打次数
						if(challengeTimes != null){
							battleDetail.setBattleNum(Integer.parseInt(challengeTimes));
						}
						
						int endTimeType = Integer.valueOf(battleDetailElem.attributeValue("EndTimeType").trim());
						String bag = battleDetailElem.attributeValue("Bag");
						String bagView = battleDetailElem.attributeValue("BagPreview");
						String cardBag = battleDetailElem.attributeValue("CardBag");
						
						String battleTypeStr = battleDetailElem.attributeValue("BattleType");
						if(battleTypeStr != null){
							battleDetail.setBattleType(Integer.parseInt(battleTypeStr));
						}
						
						String starStr = battleDetailElem.attributeValue("Star");
						if(starStr != null){
							String[] stars = starStr.split(",");
							if(stars != null && stars.length == 3){
								battleDetail.setStarType1(Integer.parseInt(stars[0]));
								battleDetail.setStarType2(Integer.parseInt(stars[1]));
								battleDetail.setStarType3(Integer.parseInt(stars[2]));
							}

						}
						
						String endTimeStr =  battleDetailElem.attributeValue("EndTime");
						if(endTimeStr != null)
						{
							battleDetail.setEndTime(Integer.parseInt(endTimeStr.trim()));
						}
						
						String itemNoStr = battleDetailElem.attributeValue("HeroDrop");
						if(itemNoStr != null && itemNoStr.length() > 1){
							battleDetail.setItemNo(itemNoStr.trim()+",");
						}
						
						battleDetail.setEndTimeType(endTimeType);
						battleDetail.setChapterType(chapterType);
						battleDetail.setChapterNo(chapterNo);
						if(bag != null && bag.length() > 0){
							battleDetail.setBag(bag);
						}
						if(bagView != null && bagView.length() > 0){
							battleDetail.setBagView(bagView);
						}
						if(cardBag != null && cardBag.length() > 0){
							battleDetail.setCardBag(cardBag);
						}

						
						if(battleElem.attributeValue("Date") != null && !"".equalsIgnoreCase(battleElem.attributeValue("Date")))
						{
							battleDetail.setBattleWeekDay(battleElem.attributeValue("Date").trim());
						}
						if(battleElem.attributeValue("CD") != null && !"".equalsIgnoreCase(battleElem.attributeValue("CD")))
						{
							battleDetail.setBattleInterTime(Integer.valueOf(battleElem.attributeValue("CD").trim()));
						}
						if(battleElem.attributeValue("OpenTime") != null && !"".equalsIgnoreCase(battleElem.attributeValue("OpenTime")))
						{
							battleDetail.setBattleRefreshTime(Integer.valueOf(battleElem.attributeValue("OpenTime").trim()));
						}
						
						String unLockLvStr = battleDetailElem.attributeValue("UnlockLv");
						if(unLockLvStr != null){
							battleDetail.setUnLockLv(Integer.valueOf(unLockLvStr.trim()));
						}
						
						if(battleDetailElem.attributeValue("MinTime") != null && !"".equalsIgnoreCase(battleDetailElem.attributeValue("MinTime")))
						{
							battleDetail.setMinTime(Integer.valueOf(battleDetailElem.attributeValue("MinTime").trim()));
						}
						
						if(battleDetailElem.attributeValue("killNpc") != null && !"".equalsIgnoreCase(battleDetailElem.attributeValue("killNpc")))
						{
							battleDetail.setKillNpc(Integer.valueOf(battleDetailElem.attributeValue("killNpc").trim()));
						}
						
						
						// 副本中怪点
						List<?>pointsElems = battleDetailElem.elements("Points");
						if (pointsElems != null && pointsElems.size() > 0) 
						{
							for (int a = 0; a < pointsElems.size(); a++) 
							{
								Element pointsElems2 = (Element) pointsElems.get(a);
								List<?>pointsElems3 =pointsElems2.elements("Point");
								if(pointsElems3 != null && pointsElems3.size() > 0)
								{
									for(int z = 0; z < pointsElems3.size(); z++){
										Element pointElemt = (Element) pointsElems3.get(z);
										BattleDetailPoint point = new BattleDetailPoint();
										point.setPointNo(Integer.valueOf(pointElemt.attributeValue("No").trim()));
										point.setGw(pointElemt.attributeValue("NPC").trim());
										
										battleDetail.getPointsMap().put(point.getPointNo(), point);
									}
								}

							}
						}

						battlesInfo.addBattleDetail(battleDetail);
						ChallengeBattleXmlInfoMap.addBattleLevel(battleDetail);
					}
				}
				xmlInfo.addChallengeBattlesInfo(battlesInfo);
				ChallengeBattleXmlInfoMap.addChapterchallengeXmlInfo(xmlInfo);

			}
		}
	}
	
}
