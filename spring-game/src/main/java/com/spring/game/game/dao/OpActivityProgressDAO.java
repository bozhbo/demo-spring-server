package com.snail.webgame.game.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.OpActivityProgressInfo;
import com.snail.webgame.game.info.RoleInfo;

public class OpActivityProgressDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private OpActivityProgressDAO() {
	}

	private static class InternalClass {
		public final static OpActivityProgressDAO instance = new OpActivityProgressDAO();
	}

	public static OpActivityProgressDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 加载玩家时限活动信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadRoleOpActProInfo() {
		List<OpActivityProgressInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectOpActProInfo");
		if (list != null) {
			for (OpActivityProgressInfo info : list) {
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(info.getRoleId());
				if (roleInfo == null) {
					continue;
				}
				
				roleInfo.getOpActProMap().addOpActivityProgressInfo(info);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_OP_ACT_PRO Table success!" + list.size());
			}
		}

		return true;
	}
	
	/**
	 * 处理运营活动信息
	 * 
	 * @param adds
	 * @param finishs
	 * @param dels
	 * @return
	 */
	public boolean dealOpActProInfo(List<OpActivityProgressInfo> adds, List<OpActivityProgressInfo> finishs, List<OpActivityProgressInfo> dels) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client != null){
				if (finishs != null && finishs.size() > 0) {
					for (OpActivityProgressInfo info : finishs) {
						client.update("updateOpActProInfo", info);
					}
				}

				if (adds != null && adds.size() > 0) {
					for (OpActivityProgressInfo info : adds) {
						client.insert("insertOpActProInfo", info);
					}
				}
				
				if (dels != null && dels.size() > 0) {
					for (OpActivityProgressInfo info : dels) {
						client.delete("deleteOpActProInfo", info);
					}
				}

				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("OpActivityProgressDAO.dealOpActProInfo error!", e);
			}
			if(client != null){
				client.rollback();
			}
			return false;
		}
	}
	
	/**
	 * 更新运营活动领取状态
	 * 
	 * @param roleId
	 * @param 
	 * @return
	 */
	public boolean updateOpActProRewardState(int id, int rewardState) {
		OpActivityProgressInfo info = new OpActivityProgressInfo();
		info.setId(id);
		info.setRewardState(rewardState);
		info.setCheckTime(new Timestamp(System.currentTimeMillis()));
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateOpActProRewardState", info);
		} catch (Exception e) {
			logger.error("OpActivityProgressDAO.updateOpActProRewardState error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新限时武奖抽奖次数
	 * 
	 * @param roleId
	 * @param 
	 * @return
	 */
	public boolean updateOpActProVal1(int id, int val1) {
		OpActivityProgressInfo info = new OpActivityProgressInfo();
		info.setId(id);
		info.setValue1(val1);
		info.setCheckTime(new Timestamp(System.currentTimeMillis()));
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateOpActProVal1", info);
		} catch (Exception e) {
			logger.error("OpActivityProgressDAO.updateOpActProVal1 error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新累计登录天数
	 * 
	 * @param info
	 * @return
	 */
	public boolean updateOpActProVal3(OpActivityProgressInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateOpActProVal3", info);
		} catch (Exception e) {
			logger.error("OpActivityProgressDAO.updateOpActProVal3 error!",e);
			result = false;
		}
		return result;
	}
	
}
