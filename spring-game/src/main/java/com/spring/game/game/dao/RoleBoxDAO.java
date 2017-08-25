package com.snail.webgame.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RoleBoxRecordInfo;
import com.snail.webgame.game.info.RoleInfo;

public class RoleBoxDAO extends SqlMapDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleBoxDAO() {
	}

	private static class InternalClass {
		public final static RoleBoxDAO instance = new RoleBoxDAO();
	}

	public static RoleBoxDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 加载角色礼包购买信息
	 * 
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadRoleBoxByRoleId(RoleInfo roleInfo) {
		List<RoleBoxRecordInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectRoleBoxByRoleId", roleInfo.getId());
		if (list != null) {
			Map<Integer, RoleBoxRecordInfo> map = new HashMap<Integer, RoleBoxRecordInfo>();
			roleInfo.setRoleBoxMap(map);
			for (RoleBoxRecordInfo info : list) {
				map.put(info.getBoxId(), info);
			}
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_ROLE_BOX_RECORD Table success!");
			}
		}
		return true;
	}
	
	/**
	 * 插入玩家礼包购买信息
	 * 
	 * @param info
	 */
	public boolean insertRoleBoxInfo(RoleBoxRecordInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertRoleBoxInfo", info);
		} catch (Exception e) {
			logger.error("RoleBoxDAO.insertRoleBoxInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新玩家礼包购买信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean updateRoleBoxInfo(RoleBoxRecordInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleBoxInfo", info);
		} catch (Exception e) {
			logger.error("RoleBoxDAO.updateRoleBoxInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 移除玩家礼包购买信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean removeRoleBoxInfo(RoleBoxRecordInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("removeRoleBoxInfo", info);
		} catch (Exception e) {
			logger.error("RoleBoxDAO.removeRoleBoxInfo error!",e);
			result = false;
		}
		return result;
	}
	
}