package com.snail.webgame.game.dao;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.FightGemInfoMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.FightGemInfo;

/**
 * 宝石活动
 * @author zenggang
 */
public class FightGemDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private FightGemDAO() {
	};

	private static class InternalClass {
		public final static FightGemDAO instance = new FightGemDAO();
	}

	public static FightGemDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 获取所有记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadAllFightGemInfo() {
		List<FightGemInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectGemAll");
		if (list != null) {
			for (FightGemInfo info : list) {
				FightGemInfoMap.addFightGemInfo(info);
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("Load GAME_FIGHT_GEM Table success!");
		}
		return true;
	}

	/**
	 * 加载角色宝石活动数据
	 * @param roleId
	 * @return
	 */
	public boolean loadFightGemInfobyRoleId(long roleId) {
		FightGemInfo info = (FightGemInfo) getSqlMapClient(DbConstants.GAME_DB).query("selectGembyRoleId", roleId);
		if (info != null) {
			FightGemInfoMap.addFightGemInfo(info);
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_FIGHT_GEM Table success! roleId=" + roleId);
			}
		}

		return true;
	}

	/**
	 * 插入记录
	 * @param info
	 * @return
	 */
	public boolean insertFightGemInfo(FightGemInfo info) {
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertGem", info);
		} catch (Exception e) {
			logger.error("FightGemDAO.insertFightGemInfo error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新重置次数
	 * @param gemId
	 * @param fightNum
	 * @param resetNum
	 * @param lastResetTime
	 * @param lastFightResult
	 * @param lastFightBattleNo
	 * @return
	 */
	public boolean updateResetNum(int gemId, int fightNum, int resetNum, Timestamp lastResetTime, int lastFightResult,
			int lastFightBattleNo) {
		FightGemInfo to = new FightGemInfo();
		to.setId(gemId);
		to.setFightNum(fightNum);
		to.setResetNum(resetNum);
		to.setLastResetTime(lastResetTime);
		to.setLastFightResult(lastFightResult);
		to.setLastFightBattleNo(lastFightBattleNo);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateGemResetNum", to);
		} catch (Exception e) {
			logger.error("FightGemDAO.updateResetNum error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * gm更新重置
	 * @param gemId
	 * @param fightNum
	 * @param lastFightResult
	 * @param lastFightBattleNo
	 * @return
	 */
	public boolean updateGmResetGem(int gemId, int fightNum, int lastFightResult, int lastFightBattleNo) {
		FightGemInfo to = new FightGemInfo();
		to.setId(gemId);
		to.setFightNum(fightNum);
		to.setLastFightResult(lastFightResult);
		to.setLastFightBattleNo(lastFightBattleNo);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateGmResetGem", to);
		} catch (Exception e) {
			logger.error("FightGemDAO.updateGmResetGem error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新宝石活动战斗次数
	 * @param gemId
	 * @param fightNum
	 * @return
	 */
	public boolean updateFightNum(int gemId, int fightNum) {
		FightGemInfo to = new FightGemInfo();
		to.setId(gemId);
		to.setFightNum(fightNum);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateGemFightNum", to);
		} catch (Exception e) {
			logger.error("FightGemDAO.updateFightNum error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新宝石活动战斗结果
	 * @param gemId
	 * @param lastFightResult
	 * @param lastFightBattleNo
	 * @return
	 */
	public boolean updateFightResult(int gemId, int lastFightResult, int lastFightBattleNo) {
		FightGemInfo to = new FightGemInfo();
		to.setId(gemId);
		to.setLastFightResult(lastFightResult);
		to.setLastFightBattleNo(lastFightBattleNo);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateGemFightResult", to);
		} catch (Exception e) {
			logger.error("FightGemDAO.updateFightResult",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新宝石活动战斗结果
	 * @param gemId
	 * @param lastFightResult
	 * @param lastFightBattleNo
	 * @param maxFightBattleNo
	 * @return
	 */
	public boolean updateFightResult(int gemId, int lastFightResult, int lastFightBattleNo, int maxFightBattleNo) {
		FightGemInfo to = new FightGemInfo();
		to.setId(gemId);
		to.setLastFightResult(lastFightResult);
		to.setLastFightBattleNo(lastFightBattleNo);
		to.setMaxFightBattleNo(maxFightBattleNo);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateGemFightResult1", to);
		} catch (Exception e) {
			logger.error("FightGemDAO.updateFightResult error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新宝石活动战斗结果
	 * @param gemId
	 * @param lastFightResult
	 * @param lastFightBattleNo
	 * @param maxFightBattleNo
	 * @return
	 */
	public boolean updateFightFault(int gemId, int lastFightResult, int lastFightBattleNo, int fightNum) {
		FightGemInfo to = new FightGemInfo();
		to.setId(gemId);
		to.setLastFightResult(lastFightResult);
		to.setLastFightBattleNo(lastFightBattleNo);
		to.setFightNum(fightNum);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateFightFault", to);
		} catch (Exception e) {
			logger.error("FightGemDAO.updateFightFault error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新关卡奖励记录
	 * @param gemId
	 * @param prizeBattleNos
	 * @return
	 */
	public boolean updatePrizeBattleNos(int gemId, List<Integer> prizeBattleNos) {
		FightGemInfo to = new FightGemInfo();
		to.setId(gemId);
		to.setPrizeBattleNos(prizeBattleNos);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateGemPrizeBattleNos", to);
		} catch (Exception e) {
			logger.error("FightGemDAO.updatePrizeBattleNos error!!",e);
			result = false;
		}
		return result;
	}
}
