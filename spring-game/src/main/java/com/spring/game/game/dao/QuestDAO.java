package com.snail.webgame.game.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.QuestInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.quest.service.QuestService;

public class QuestDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static class InternalClass {
		public final static QuestDAO instance = new QuestDAO();
	}

	public static QuestDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 加载任务信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadQuestList() {
		List<QuestInProgressInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectAllQuest");
		if (list != null) {
			for (QuestInProgressInfo info : list) {
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(info.getRoleId());
				if (roleInfo == null) {
					continue;
				}
				
				roleInfo.getQuestInfoMap().addQuestInProgressInfo(info);
			}
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_QUEST_INFO Table success!");
			}
		}
		
		// 给已通关任务的玩家设置新配置任务
		QuestService.checkIsPassTask();
		return true;
	}

	/**
	 * 加载角色任务
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadQuestListbyRoleId(RoleInfo roleInfo) {
		List<QuestInProgressInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectQuestbyRoleId", roleInfo.getId());
		if (list != null && roleInfo.getRoleLoadInfo() != null) {
			QuestInfoMap questInfoMap = new QuestInfoMap();
			roleInfo.setQuestInfoMap(questInfoMap);
			for (QuestInProgressInfo info : list) {
				questInfoMap.addQuestInProgressInfo(info);
			}
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_QUEST_INFO Table success!");
			}
		}
		return true;
	}

	/**
	 * 处理任务信息
	 * 
	 * @param dels
	 * @param adds
	 * @param finishs
	 * @return
	 */
	public boolean dealQuestInfo(List<QuestInProgressInfo> adds, List<QuestInProgressInfo> finishs) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client != null){
				if (finishs != null) {
					for (QuestInProgressInfo info : finishs) {
						client.update("updateQuestInfo", info);
					}
				}

				if (adds != null) {
					for (QuestInProgressInfo info : adds) {
						client.insert("insertQuest", info);
					}
				}

				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("QuestDAO.dealQuestInfo error!", e);
			}
			if(client != null){
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 刷新日常任务
	 * 
	 * @param finishedDailyQuest 已完成的日常任务
	 * @return
	 */
	public boolean refeshDailyQuest(List<QuestInProgressInfo> finishedDailyQuest) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client != null){
				QuestInProgressInfo to = null;
				long now = System.currentTimeMillis();
				for (QuestInProgressInfo info : finishedDailyQuest) {
					to = new QuestInProgressInfo();
					to.setId(info.getId());
					to.setStatus(QuestInProgressInfo.STATUS_REVEIVE);
					to.setValue1(0);
					to.setValue2(0);
					to.setValue3(0);
					to.setQuestGetTime(new Timestamp(now));
					to.setTalkOrder(0);
					client.update("updateQuestInfo", to);
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("QuestDAO.refeshDailyQuest error!", e);
			}
			if(client != null){
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 更新任务
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateQuestInProgressInfo(int id, int status) {
		QuestInProgressInfo to = new QuestInProgressInfo();
		to.setId(id);
		to.setStatus(status);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateQuestStatus", to);
		} catch (Exception e) {
			logger.error("QuestDAO.updateQuestInProgressInfo error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新任务对话顺序
	 * 
	 * @param id
	 * @param order
	 * @return
	 */
	public boolean updateQuestTalkOrder(int id, int order) {
		QuestInProgressInfo to = new QuestInProgressInfo();
		to.setId(id);
		to.setTalkOrder(order);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateQuestTalkOrder", to);
		} catch (Exception e) {
			logger.error("QuestDAO.updateQuestTalkOrder error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * 删除已领奖的任务
	 * 
	 * @param info
	 */
	public boolean deleteQuest(QuestInProgressInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("deleteQuest", info);
		} catch (Exception e) {
			logger.error("QuestDAO.deleteQuest error!",e);
			result = false;
		}
		return result;		
	}

}
