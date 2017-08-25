package com.snail.webgame.game.protocal.quest.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.cache.QuestInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.GameSettingDAO;
import com.snail.webgame.game.dao.QuestDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.GameSettingInfo;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.xml.cache.QuestProtoXmlInfoMap;
import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;

public class QuestService {

	private static QuestDAO questDAO = new QuestDAO();

	/**
	 * 检测玩家任务
	 * @param roleInfo
	 * @param roleAction 玩家行为 （默认为-0）
	 * @param obj	（默认填null）
	 * @param isRefresh 推送客户端
	 * @param isRedPoint 是否检测推送红点
	 */
	public static boolean checkQuest(RoleInfo roleInfo, int roleAction, Object obj, boolean isRefresh, boolean isRedPoint) {
		checkQuest(roleInfo, isRefresh, roleAction, obj);
		//红点检测
		boolean redFlag = RedPointMgtService.checkQuestRedPoint(roleInfo.getId(), null, isRedPoint,
				RedPointMgtService.LISTENING_MISSION_CHANGE_TYPES);
		return redFlag;
	}
	
	/**
	 * 检测玩家行为对用户任务的影响 （检测并推送红点）
	 * @param roleInfo
	 * @param roleAction
	 * @param obj
	 */
//	public static void dealRoleActionforQuest(RoleInfo roleInfo, int roleAction, Object obj) {
//		checkQuest(roleInfo, true, roleAction, obj);
//	}

	/**
	 * 检测玩家任务（红点不检测）
	 * 
	 * @param roleInfo
	 * @param isRefresh 推送客户端
	 */
	private static void checkQuest(RoleInfo roleInfo, boolean isRefresh, int roleAction, Object obj) {
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return;
		}
		
		QuestInfoMap questInfoMap = roleInfo.getQuestInfoMap();
		
		String noStr = "";// 记录任务状态发生改变的任务
		// 移除过期的每日任务
		noStr = removeOldDailyQuest(roleLoadInfo, questInfoMap, ActionType.action182.getType());

		// 检测玩家可领取任务
		Map<Integer, QuestProtoXmlInfo> map = QuestProtoXmlInfoMap.getQuestProtoMap();
		if (map == null) {
			return;
		}

		// 任务状态值发生变化的任务
		List<QuestInProgressInfo> finishQuest = new ArrayList<QuestInProgressInfo>();
		List<QuestInProgressInfo> addNewQuest = new ArrayList<QuestInProgressInfo>();

		StringBuilder strBuilder = new StringBuilder();
		for (QuestProtoXmlInfo xmlInfo : map.values()) {
			int questProtoNo = xmlInfo.getQuestProtoNo();
			// 检测已领取奖励的任务
			if (questInfoMap.checkQuestIsFinish(questProtoNo)) {
				continue;
			}
			// 任务进行中
			QuestInProgressInfo info = questInfoMap.getQuestInProgressInfo(questProtoNo);
			if (info == null) {
				// 判断任务是否可领取
				int result = AbstractConditionCheck.check(xmlInfo.getQuestConds(), roleInfo, null, roleAction, obj);
				if (result != 1) {
					continue;
				}
				
				if (xmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_RUN) {
					// 判断是否有新的跑环任务
					questInfoMap.addRunQuestXmlInfo(xmlInfo);
					
					// 跑环任务另外处理
					continue;
				}
				
				info = new QuestInProgressInfo(roleInfo.getId(), questProtoNo);
				// 新增任务
				strBuilder.append(questProtoNo);
				strBuilder.append(',');
				addNewQuest.add(info);
			}
			// 判断任务是否完成
			if (info.getStatus() == QuestInProgressInfo.STATUS_REVEIVE) {

				// 判断是否显示，不显示，就算领取在身上也不能完成
				if (xmlInfo.isShowCheckCond()) {
					int ShowCondResult = AbstractConditionCheck.check(xmlInfo.getQuestConds(), roleInfo, null, roleAction, obj);
					if (ShowCondResult != 1) {
						strBuilder.append(questProtoNo);
						strBuilder.append(',');
						continue;
					}
				}

				// 检测完成
				AbstractConditionCheck.check(xmlInfo.getFinishConds(), roleInfo, info, roleAction, obj);
				// 更新任务中间值
				if (info.isVersionChg()) {
					// 任务完成条件值改变，或任务已经完成
					if (strBuilder.indexOf(questProtoNo + "") == -1) {
						strBuilder.append(questProtoNo);
						strBuilder.append(',');
					}
					if (!addNewQuest.contains(info)) {
						// 午餐晚餐任务只修改缓存，不更新数据库
						if (info.getQuestProtoNo() != GameValue.LUNCH_QUEST_PROTO＿NO && info.getQuestProtoNo() != GameValue.NIGHT_QUEST_PROTO＿NO) {
							finishQuest.add(info);
						}
					}
					
					info.setVersionChg(false);
				}
			}
			
			// 会员任务完成后推送删除
			if (xmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_CARD) {
				// 判断是否显示，不显示，就算领取在身上也不能完成
				if (xmlInfo.isShowCheckCond()) {
					int ShowCondResult = AbstractConditionCheck.check(xmlInfo.getQuestConds(), roleInfo, null, roleAction, obj);
					if (ShowCondResult != 1) {
						if (strBuilder.indexOf(questProtoNo + "") == -1) {
							strBuilder.append(questProtoNo);
							strBuilder.append(',');
						}
						continue;
					}
				}
			}
			
			// 等级段的显示任务判断
			if (xmlInfo.isRoleLvRangCond()) {
				int ShowCondResult = AbstractConditionCheck.check(xmlInfo.getQuestConds(), roleInfo, null, roleAction, obj);
				if (ShowCondResult != 1) {
					if (strBuilder.indexOf(questProtoNo + "") == -1) {
						strBuilder.append(questProtoNo);
						strBuilder.append(',');
					}
					continue;
				}
			}
			
			// 判断体力任务领取时间是否过期
			if ((info.getQuestProtoNo() == GameValue.LUNCH_QUEST_PROTO＿NO || info.getQuestProtoNo() == GameValue.NIGHT_QUEST_PROTO＿NO) 
					&& info.getStatus() == QuestInProgressInfo.STATUS_FINISH) {
				// 检测是否能完成
				int rt = AbstractConditionCheck.checkCondition(roleInfo, xmlInfo.getFinishConds());
				if (rt != 1) {
					// 领取时间已过期 
					// 午餐晚餐任务只修改缓存，不更新数据库
					info.setStatus(QuestInProgressInfo.STATUS_REVEIVE);
					
					if (strBuilder.indexOf(questProtoNo + "") == -1) {
						strBuilder.append(questProtoNo);
						strBuilder.append(',');
					}
				}
			}
			
		}
		
		// 检测跑环任务
		if (questInfoMap.checkRunQusetIsOpen()) {
			// 跑环任务已开启
			if (questInfoMap.getRunQuestInfo() == null && roleLoadInfo.getTodayRunNum() < GameValue.GAME_QUEST_RUN_MAX_NUM) {
				int lastRunQuestProtoNo = 0;
				if (roleAction == ActionType.action181.getType()) {
					lastRunQuestProtoNo = (Integer) obj;
				}
				QuestProtoXmlInfo runQuestXmlInfo = questInfoMap.fetchNextRunQuest(lastRunQuestProtoNo);
				 
				QuestInProgressInfo runQuestInfo = new QuestInProgressInfo(roleInfo.getId(), runQuestXmlInfo.getQuestProtoNo());
				
				// 新增任务
				strBuilder.append(runQuestXmlInfo.getQuestProtoNo());
				strBuilder.append(',');
				addNewQuest.add(runQuestInfo);
				
				// 更新领取跑环任务时间
				roleLoadInfo.setLastRunTime(new Timestamp(System.currentTimeMillis()));
				
				// 更新数据库
				RoleDAO.getInstance().updateRoleRunQuestInfo(roleLoadInfo);
			}
		}
		
		
		if (addNewQuest.size() > 0) {// 新增任务
			if (questDAO.dealQuestInfo(addNewQuest, null)) {
				for (QuestInProgressInfo questInfo : addNewQuest) {
					questInfoMap.addQuestInProgressInfo(questInfo);
				}
				// 日志
				GameLogService.insertTaskLog(roleInfo, addNewQuest, 0);
			}
		}
		
		if (finishQuest.size() > 0) {
			// 修改任务中间值及任务状态
			questDAO.dealQuestInfo(null, finishQuest);
		}
		
		noStr += strBuilder.toString();

		if (noStr.length() > 0 && isRefresh) {
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_QUEST, noStr);
		}
	}

	/**
	 * 移除过期的每日任务
	 * 
	 * @param roleLoadInfo
	 * @param questInfoMap
	 * @param roleAction
	 * @return
	 */
	private static String removeOldDailyQuest(RoleLoadInfo roleLoadInfo, QuestInfoMap questInfoMap, int roleAction) {

		StringBuilder noStrBuilder = new StringBuilder();// 记录任务状态发生改变的任务
		// 过时任务，修改数据库中的状态
		List<QuestInProgressInfo> dels = new ArrayList<QuestInProgressInfo>();

		// 清理过时日常活动任务
		long nowTime = System.currentTimeMillis();

		List<QuestInProgressInfo> list = new ArrayList<QuestInProgressInfo>();
		// (已经完成)日常任务
		List<QuestInProgressInfo> finishs = questInfoMap.getRoleFinishQuestByType(QuestProtoXmlInfo.QUEST_TYPE_DAILY);
		if (finishs != null) {
			list.addAll(finishs);
		}
		// (进行中)日常任务
		List<QuestInProgressInfo> inPro = questInfoMap.getRoleInProgressQuestByType(QuestProtoXmlInfo.QUEST_TYPE_DAILY);
		if (inPro != null) {
			list.addAll(inPro);
		}
		
		// (已经完成)国家任务
		List<QuestInProgressInfo> raceFinishs = questInfoMap.getRoleFinishQuestByType(QuestProtoXmlInfo.QUEST_TYPE_RACE);
		if (raceFinishs != null) {
			list.addAll(raceFinishs);
		}
		// (进行中)国家任务
		List<QuestInProgressInfo> raceInPro = questInfoMap.getRoleInProgressQuestByType(QuestProtoXmlInfo.QUEST_TYPE_RACE);
		if (raceInPro != null) {
			list.addAll(raceInPro);
		}
		
		// 会员任务
		List<QuestInProgressInfo> cardInPro = questInfoMap.getRoleInProgressQuestByType(QuestProtoXmlInfo.QUEST_TYPE_CARD);
		if (cardInPro != null) {
			list.addAll(cardInPro);
		}
		
		// 活跃度任务
		List<QuestInProgressInfo> activeInPro = questInfoMap.getRoleInProgressQuestByType(QuestProtoXmlInfo.QUEST_TYPE_ACTIVE);
		if (activeInPro != null) {
			list.addAll(activeInPro);
		}

		if (list != null) {

			for (QuestInProgressInfo info : list) {
				// 任务模板id
				int questProtoNo = info.getQuestProtoNo();
				// 任务xml
				QuestProtoXmlInfo xmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
				if (xmlInfo == null) {
					continue;
				}
				if (xmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_DAILY 
						&& xmlInfo.getQuestType() !=  QuestProtoXmlInfo.QUEST_TYPE_RACE
						&& xmlInfo.getQuestType() !=  QuestProtoXmlInfo.QUEST_TYPE_CARD
						&& xmlInfo.getQuestType() !=  QuestProtoXmlInfo.QUEST_TYPE_ACTIVE) {
					continue;
				}

				if (!DateUtil.isSameDay(nowTime, info.getCurrQuestGetTime())) {
					// 每日任务判断领取时间
					dels.add(info);
					noStrBuilder.append(questProtoNo);
					noStrBuilder.append(',');
				}
			}

			if (dels.size() > 0) {
				// 修改数据库
				if (questDAO.refeshDailyQuest(dels)) {
					for (QuestInProgressInfo removeQuest : dels) {
						questInfoMap.removeDailyQuest(removeQuest);
					}
				}
			}
		}
		
		// 检测跑环任务
		if (roleLoadInfo.getLastRunTime() != null && !DateUtil.isSameDay(nowTime, roleLoadInfo.getLastRunTime().getTime())) {
			roleLoadInfo.setLastRunTime(new Timestamp(nowTime));
			roleLoadInfo.setTodayRunNum(0);
			
			// 更新数据库
			RoleDAO.getInstance().updateRoleRunQuestInfo(roleLoadInfo);
			
			// 跑环信息改变,记录跑环任务编号
			if (questInfoMap.getRunQuestInfo() != null) {
				noStrBuilder.append(questInfoMap.getRunQuestInfo().getQuestProtoNo());
				noStrBuilder.append(',');
			}
		}
		
		// 检测每日活跃度是否需要重置
		if (roleLoadInfo.getLastActiveChgTime() != null && !DateUtil.isSameDay(nowTime, roleLoadInfo.getLastActiveChgTime().getTime())) {
			// 此处只改变缓存,不更新数据库(等真正变化活跃度的一刻才操作库)
			roleLoadInfo.setLastActiveChgTime(new Timestamp(nowTime));
			roleLoadInfo.setTodayActive(0);
			
			SceneService.sendRoleRefreshMsg(roleLoadInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
		}

		return noStrBuilder.toString();
	}
	
	/**
	 * 每日任务午餐晚餐的变化处理 
	 * 
	 * @param questProtoNo
	 * @param checkFlag true-任务开始 false-任务结束
	 */
	public static void dealSpQuestChg(int questProtoNo, boolean checkFlag) {
		Set<Entry<Integer, RoleInfo>> roleIds = RoleInfoMap.getRoleInfoEntrySet();
		if (roleIds != null && roleIds.size() > 0) {
			int i = 0;
			for (Entry<Integer, RoleInfo> entry : roleIds) {
				RoleInfo roleInfo = entry.getValue();
				if (roleInfo != null && roleInfo.getLoginStatus() == 1) {
					synchronized (roleInfo) {
						// 玩家的体力任务
						QuestInProgressInfo questInProgressInfo = roleInfo.getQuestInfoMap().getQuestInProgressInfo(questProtoNo);
						if (questInProgressInfo == null || questInProgressInfo.getStatus() == QuestInProgressInfo.STATUS_CLEAR) {
							// 已领取,不需要改变
							continue;
						}
						
						if (checkFlag) {
							// 未完成置为完成
							if (questInProgressInfo.getStatus() == QuestInProgressInfo.STATUS_REVEIVE) {
								// 体力任务只改变缓存,不刷数据库
								questInProgressInfo.setStatus(QuestInProgressInfo.STATUS_FINISH);
							}
						} else {
							// 完成置为未完成
							if (questInProgressInfo.getStatus() == QuestInProgressInfo.STATUS_FINISH) {
								// 体力任务只改变缓存,不刷数据库
								questInProgressInfo.setStatus(QuestInProgressInfo.STATUS_REVEIVE);
							}
						}
						i++;
						
						SceneService.sendRoleRefreshMsg(questInProgressInfo.getRoleId(), SceneService.REFESH_TYPE_QUEST, questInProgressInfo.getQuestProtoNo() + "");
					}
				}
				if(i > 300)
				{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					i=0;
				}
			}
		}
	}

	/**
	 * 配置内新增任务后需特殊处理(检测是否有玩家已全部通关任务)
	 */
	public static void checkIsPassTask() {
		// 该方法只执行一遍
		boolean isExec = false;
		
		GameSettingInfo gameSetInfo = GameSettingMap.getValue(GameSettingKey.TASK_PASS_FLAG);
		if (gameSetInfo != null && "1".equals(gameSetInfo.getValue())) {
			if (GameSettingDAO.getInstance().updateGameSettingValue(GameSettingKey.TASK_PASS_FLAG, "2")) {
				gameSetInfo.setValue("2");
			}
			
			isExec = true;
		}
		
		if (isExec) {
			List<QuestInProgressInfo> insertList = new ArrayList<QuestInProgressInfo>();
			for (RoleInfo roleInfo : RoleInfoMap.getMap().values()) {
				QuestInfoMap questInfoMap = roleInfo.getQuestInfoMap();
				if (questInfoMap == null) {
					continue;
				}
				
				// 判断主线是否已打通
				List<QuestInProgressInfo> mainInfos = questInfoMap.getRoleInProgressQuestByType(QuestProtoXmlInfo.QUEST_TYPE_NORMAL);
				if (mainInfos.size() >= 1) {
					continue;
				}
				
				QuestInProgressInfo info = new QuestInProgressInfo(roleInfo.getId(), 5084);
				questInfoMap.addQuestInProgressInfo(info);
				insertList.add(info);
				
				// 主线已通,判断下支线是否也已打通 (主线不通的情况下不用判断支线)
				List<QuestInProgressInfo> branchInfos = questInfoMap.getRoleInProgressQuestByType(QuestProtoXmlInfo.QUEST_TYPE_BRANCH);
				if (branchInfos.size() >= 1) {
					continue;
				}
				
				QuestInProgressInfo branchInfo = new QuestInProgressInfo(roleInfo.getId(), 2041);
				questInfoMap.addQuestInProgressInfo(branchInfo);
				insertList.add(branchInfo);
			}
			
			if (insertList.size() > 0) {
				QuestDAO.getInstance().dealQuestInfo(insertList, null);
			}
		}
		
	}
	
}
