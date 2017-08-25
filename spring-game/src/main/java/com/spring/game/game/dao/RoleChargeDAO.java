package com.snail.webgame.game.dao;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.RoleChargeMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RoleChargeInfo;

public class RoleChargeDAO extends SqlMapDaoSupport {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleChargeDAO() {
	}

	private static class InternalClass {
		public final static RoleChargeDAO instance = new RoleChargeDAO();
	}

	public static RoleChargeDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 加载角色充值信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadAllRoleCharge() {
		List<RoleChargeInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectRoleCharge");
		if (list != null) {
			for (RoleChargeInfo info : list) {
				RoleChargeMap.addRoleChargeInfo(info);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_ROLE_CHARGE Table success!" + list.size());
			}
		}

		return true;
	}
	
	/**
	 * 插入角色充值订单号记录
	 * 
	 * @param info
	 */
	public boolean insertRoleChargeInfo(RoleChargeInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertRoleChargeInfo", info);
		} catch (Exception e) {
			logger.error("RoleChargeDAO.insertRoleChargeInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 移除角色充值订单号记录
	 * 
	 * @param info
	 * @return
	 */
	public boolean removeRoleChargeInfo(RoleChargeInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("removeRoleChargeInfo", info);
		} catch (Exception e) {
			logger.error("RoleChargeDAO.removeRoleChargeInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 清理过期订单
	 * 
	 * @param infos
	 * @return
	 */
	public boolean clearRoleChargeInfo(List<RoleChargeInfo> infos) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				if (infos != null) {
					for (RoleChargeInfo info : infos) {
						client.delete("removeRoleChargeInfo", info);
					}
				}

				client.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("RoleChargeDAO.clearRoleChargeInfo error!", e);
			}
			if (client != null) {
				client.rollback();
			}
			return false;
		}
	}

}
