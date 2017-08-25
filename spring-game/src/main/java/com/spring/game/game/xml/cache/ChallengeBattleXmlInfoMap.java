package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.ChapterInfo;


public class ChallengeBattleXmlInfoMap {

	//副本缓存
	private static Map<Byte, ChallengeBattleXmlInfo> mapByNo = new HashMap<Byte, ChallengeBattleXmlInfo>();

	//副本等级缓存
	private static Map<Integer, ArrayList<BattleDetail>> battleLevel = new HashMap<Integer, ArrayList<BattleDetail>>();
	
	public static Map<Byte, ChallengeBattleXmlInfo> getMapByNo() {
		return mapByNo;
	}

	public static void addChapterchallengeXmlInfo(ChallengeBattleXmlInfo info) {
		mapByNo.put(info.getNo(), info);
	}

	public static ChallengeBattleXmlInfo getInfoByNo(byte no) {
		return mapByNo.get(no);
	}

	public static List<BattleDetail> getTotals() {
		List<BattleDetail> result = new ArrayList<BattleDetail>();
		for (ChallengeBattleXmlInfo xmlInfo : mapByNo.values()) {
			if (xmlInfo != null) {
				Map<Integer, ChapterInfo> chapterXml = xmlInfo.getChallengeBattlesInfoMap();
				if (chapterXml != null) {
					for (ChapterInfo chapter : chapterXml.values()) {
						if (chapter != null) {
							Map<Integer, BattleDetail> battleXml = chapter.getBattleDetailMap();
							if (battleXml != null) {
								for (BattleDetail detail : battleXml.values()) {
									result.add(detail);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取副本详细信息
	 * @param battleType
	 * @param battleChapterNo
	 * @param battleNo
	 * @return
	 */
	public static BattleDetail getBattleDetail(byte chapterType,int chapterNo,int battleNo)
	{
		if(mapByNo.containsKey(chapterType))
		{
			ChallengeBattleXmlInfo battleInfoXMLMap = mapByNo.get(chapterType);
			if(battleInfoXMLMap == null)
			{
				return null;
			}
			
			Map<Integer, ChapterInfo> challengeBattlesInfoMap = battleInfoXMLMap.getChallengeBattlesInfoMap();
			if(challengeBattlesInfoMap == null || !challengeBattlesInfoMap.containsKey(chapterNo))
			{
				return null;
			}
			
			ChapterInfo chapterInfo = challengeBattlesInfoMap.get(chapterNo);
			
			if(chapterInfo == null)
			{
				return null;
			}
			
			Map<Integer, BattleDetail> battleDetailMap = chapterInfo.getBattleDetailMap();
			if(battleDetailMap == null || !battleDetailMap.containsKey(battleNo))
			{
				return null;
			}
			
			return battleDetailMap.get(battleNo);
		}
		
		return null;
	}
	
	
	public static Map<Integer, BattleDetail> getBattleChapter(byte chapterType,int chapterNo)
	{
		if(mapByNo.containsKey(chapterType))
		{
			ChallengeBattleXmlInfo battleInfoXMLMap = mapByNo.get(chapterType);
			if(battleInfoXMLMap == null)
			{
				return null;
			}
			
			Map<Integer, ChapterInfo> challengeBattlesInfoMap = battleInfoXMLMap.getChallengeBattlesInfoMap();
			if(challengeBattlesInfoMap == null || !challengeBattlesInfoMap.containsKey(chapterNo))
			{
				return null;
			}
			
			ChapterInfo chapterInfo = challengeBattlesInfoMap.get(chapterNo);
			if(chapterInfo == null)
			{
				return null;
			}
			
			return  chapterInfo.getBattleDetailMap();
		}
		
		return null;
	}
	
	/**
	 * 添加等级副本
	 * @param info
	 */
	public static void addBattleLevel(BattleDetail info) {
		int level = info.getUnLockLv();
		ArrayList<BattleDetail> battleList = null;
		if(battleLevel.containsKey(level)){
			battleList = battleLevel.get(level);
			battleList.add(info);
		} else {
			battleList = new ArrayList<BattleDetail>();
			battleList.add(info);
			battleLevel.put(level, battleList);
		}
	}
	
	
	/**
	 * 获取等级副本
	 * @param level
	 * @return
	 */
	public static ArrayList<BattleDetail> getBattleLevel(int level){
		if(battleLevel != null && battleLevel.size() > 0 && battleLevel.containsKey(level)){
			return battleLevel.get(level);
		}
		return null;
	}
	
}
