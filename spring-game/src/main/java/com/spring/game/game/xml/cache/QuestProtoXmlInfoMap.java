package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;

public class QuestProtoXmlInfoMap {

	private static Map<Integer, QuestProtoXmlInfo> questProtoMap = new HashMap<Integer, QuestProtoXmlInfo>();
	
	private static Map<Integer, QuestProtoXmlInfo> oneKeyMap = new HashMap<Integer, QuestProtoXmlInfo>();
	
	public static void addQuestProtoXmlInfo(QuestProtoXmlInfo xmlInfo,boolean modify) {
		if (questProtoMap.containsKey(xmlInfo.getQuestProtoNo()) && !modify) {
			throw new RuntimeException("Load Task.xml error! no: " + xmlInfo.getQuestProtoNo() + " repeat");
		}
		questProtoMap.put(xmlInfo.getQuestProtoNo(), xmlInfo);
		
		// 是否可以被一键秒
		if (xmlInfo.getNeedCost() > 0) {
			oneKeyMap.put(xmlInfo.getQuestProtoNo(), xmlInfo);
		}
	}

	public static QuestProtoXmlInfo questXml(int questProtoNo) {
		return questProtoMap.get(questProtoNo);
	}

	public static Map<Integer, QuestProtoXmlInfo> getQuestProtoMap() {
		return questProtoMap;
	}
	
	public static Map<Integer, QuestProtoXmlInfo> getOneKeyMap() {
		return oneKeyMap;
	}

	/**
	 * 检测是否是跑环任务
	 * 
	 * @param questProtoNo
	 * @return
	 */
	public static boolean checkIsRunQuest(int questProtoNo) {
		QuestProtoXmlInfo xmlInfo = questXml(questProtoNo);
		if (xmlInfo != null && xmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_RUN) {
			return true;
		}
		return false;
	}
	
	public static int fetchQuestTypeByNo(int questNo) {
		int type = 0;
		if (questProtoMap.get(questNo) != null) {
			type = questProtoMap.get(questNo).getQuestType();
		}
		
		return type;
	}
	
}
