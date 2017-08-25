package com.snail.webgame.game.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.ToolOpActMap;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.ToolOpActivityRewardInfo;

public class ToolOpActRewardDAO extends SqlMapDaoSupport {

	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ToolOpActRewardDAO() {
	}

	private static class InternalClass {
		public final static ToolOpActRewardDAO instance = new ToolOpActRewardDAO();
	}

	public static ToolOpActRewardDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 加载运营时限活动奖励信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadToolOpActRewardInfo() {
		List<ToolOpActivityRewardInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectToolOpActRewardInfo");
		if (list != null) {
			for (ToolOpActivityRewardInfo info : list) {
				ToolOpActMap.addToolOpActivityRewardInfo(info);
				
				String condStr = info.getRewardCond();
				int goalVal = info.getGoalVal();
				if (condStr != null && !"".equals(condStr)) {
					if (info.isSpecCond()) {
						condStr = condStr + AbstractConditionCheck.SPLIT_2 + goalVal + AbstractConditionCheck.SPLIT_2 + info.getDateStr();
					} else {
						condStr = condStr + AbstractConditionCheck.SPLIT_2 + goalVal;
					}
					
					try {
						info.getRewardConds().addAll(AbstractConditionCheck.generateConds("", condStr));
					} catch (Exception e) {
						logger.error("loadToolOpActRewardInfo error!",e);
					}
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_TOOL_OPACT_REWARD Table success!" + list.size());
			}
		}

		return true;
	}
	
	/**
	 * 插入运营时限活动奖励信息
	 * 
	 * @param info
	 */
	public boolean insertToolOpActRewardInfo(ToolOpActivityRewardInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertToolOpActRewardInfo", info);
		} catch (Exception e) {
			logger.error("insertToolOpActRewardInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 修改运营时限活动奖励信息
	 * 
	 * @param info
	 */
	public boolean updateToolOpActRewardInfo(ToolOpActivityRewardInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("updateToolOpActRewardInfo", info);
		} catch (Exception e) {
			logger.error("updateToolOpActRewardInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 移除旧的奖励信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean deleteToolOpActRewardInfo(ToolOpActivityRewardInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("deleteToolOpActRewardInfo", info);
		} catch (Exception e) {
			logger.error("deleteToolOpActRewardInfo error!",e);
			result = false;
		}
		return result;
	}

}
