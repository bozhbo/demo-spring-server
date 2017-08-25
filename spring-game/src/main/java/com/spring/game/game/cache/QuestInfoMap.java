package com.snail.webgame.game.cache;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.xml.cache.QuestProtoXmlInfoMap;
import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;

/**
 * 任务
 * 
 * @author nijp
 * 
 */
public class QuestInfoMap {

	// <questProtoNo >//进行中的任务
	private Map<Integer, QuestInProgressInfo> questMap = new HashMap<Integer, QuestInProgressInfo>();

	// <questProtoNo >已领取奖励的任务
	private Map<Integer, QuestInProgressInfo> finishQuestMap = new HashMap<Integer, QuestInProgressInfo>();
	
	private QuestInProgressInfo runQuestInfo;// 当前正在进行的跑环任务
	
	/**
	 * 已开启的跑环任务
	 */
	private Map<Integer, QuestProtoXmlInfo> runQuestMap = new HashMap<Integer, QuestProtoXmlInfo>();
	
	/**
	 * 添加已开启的跑环任务
	 * 
	 * @param xmlInfo
	 */
	public void addRunQuestXmlInfo(QuestProtoXmlInfo xmlInfo) {
		if (!runQuestMap.containsKey(xmlInfo.getQuestProtoNo())) {
			runQuestMap.put(xmlInfo.getQuestProtoNo(), xmlInfo);
		}
	}
	
	/**
	 * 检测跑环任务是否开启
	 * 
	 * @return
	 */
	public boolean checkRunQusetIsOpen() {
		if (!runQuestMap.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取下个跑环任务
	 * 
	 * @return
	 */
	public QuestProtoXmlInfo fetchNextRunQuest(int curRunQuestNo) {
		if (runQuestMap.size() == 1) {
			for (int no : runQuestMap.keySet()) {
				return runQuestMap.get(no);
			}
		}
		
		List<Integer> tempList = new ArrayList<Integer>(runQuestMap.keySet());
		tempList.remove((Integer) curRunQuestNo);	
		
		int newRunQuestNo = tempList.get(new Random().nextInt(tempList.size()));
		return runQuestMap.get(newRunQuestNo);
	}
	
	/**
	 * 领取任务奖励
	 * 
	 * @param info
	 */
	public void questClear(QuestInProgressInfo info, int questType, boolean isCache) {
		info.setStatus(QuestInProgressInfo.STATUS_CLEAR);

		int questProtoNo = info.getQuestProtoNo();

		// 会员、活跃度任务永久显示
		if (questType == QuestProtoXmlInfo.QUEST_TYPE_CARD || questType == QuestProtoXmlInfo.QUEST_TYPE_ACTIVE) {
			return;
		}
		
		questMap.remove(questProtoNo);
		
		if (isCache) {
			finishQuestMap.put(questProtoNo, info);
		}
	}

	/**
	 * 添加记录
	 * 
	 * @param info
	 */
	public void addQuestInProgressInfo(QuestInProgressInfo info) {
		int questType = QuestProtoXmlInfoMap.fetchQuestTypeByNo(info.getQuestProtoNo());
		if (questType != QuestProtoXmlInfo.QUEST_TYPE_CARD 
				&& questType != QuestProtoXmlInfo.QUEST_TYPE_ACTIVE && info.getStatus() == QuestInProgressInfo.STATUS_CLEAR) {
			// 任务已经结束
			finishQuestMap.put(info.getQuestProtoNo(), info);

		} else {
			questMap.put(info.getQuestProtoNo(), info);
		}
		
		if (QuestProtoXmlInfoMap.checkIsRunQuest(info.getQuestProtoNo())) {
			setRunQuestInfo(info);
		}
	}

	/**
	 * 移除日常任务(从已完成任务列表，转移到进行中列表)
	 * 
	 * @param roleId
	 * @param questInfo
	 */
	public void removeDailyQuest(QuestInProgressInfo questInfo) {

		int questProtoNo = questInfo.getQuestProtoNo();
		
		if (finishQuestMap.containsKey(questProtoNo)) {
			finishQuestMap.remove(questProtoNo);
		}
		
		questInfo.setStatus(QuestInProgressInfo.STATUS_REVEIVE);
		questInfo.setValue1(0);
		questInfo.setValue2(0);
		questInfo.setValue3(0);
		questInfo.setQuestGetTime(new Timestamp(System.currentTimeMillis()));

		questMap.put(questProtoNo, questInfo);
	}

	/**
	 * 获取玩家任务
	 * 
	 * @param questProtoNo
	 * @return
	 */
	public QuestInProgressInfo getQuestInProgressInfo(int questProtoNo) {
		return questMap.get(questProtoNo);
	}

	/**
	 * 获取玩家任务
	 * 
	 * @return
	 */
	public List<QuestInProgressInfo> getRoleQuest() {
		return new ArrayList<QuestInProgressInfo>(questMap.values());
	}

	/**
	 * 获取玩家任务(进行中)
	 * 
	 * @param questType任务类型
	 * @return
	 */
	public List<QuestInProgressInfo> getRoleInProgressQuestByType(int questType) {
		List<QuestInProgressInfo> list = new ArrayList<QuestInProgressInfo>();
		for (QuestInProgressInfo info : questMap.values()) {
			int questProtoNo = info.getQuestProtoNo();
			QuestProtoXmlInfo xmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
			if (xmlInfo != null && xmlInfo.getQuestType() == questType) {
				list.add(info);
			}
		}
		return list;
	}

	/**
	 * 获取玩家任务(已经完成)
	 * 
	 * @param questType任务类型
	 * @return
	 */
	public List<QuestInProgressInfo> getRoleFinishQuestByType(int questType) {
		List<QuestInProgressInfo> list = new ArrayList<QuestInProgressInfo>();
		for (QuestInProgressInfo info : finishQuestMap.values()) {
			int questProtoNo = info.getQuestProtoNo();
			QuestProtoXmlInfo xmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
			if (xmlInfo != null && xmlInfo.getQuestType() == questType) {
				list.add(info);
			}
		}
		return list;
	}

	/**
	 * 检测已领取奖励的任务
	 * @param roleId
	 * @param questProtoNo
	 * @return
	 */
	public boolean checkQuestIsFinish(int questProtoNo) {
		if (finishQuestMap.containsKey(questProtoNo)) {
			return true;
		}
		return false;
	}

	public QuestInProgressInfo getRunQuestInfo() {
		return runQuestInfo;
	}

	public void setRunQuestInfo(QuestInProgressInfo runQuestInfo) {
		this.runQuestInfo = runQuestInfo;
	}

}
