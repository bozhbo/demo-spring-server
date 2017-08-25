package com.snail.webgame.game.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.ride.service.RideService;

public class RideDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static class InternalClass {
		public final static RideDAO instance = new RideDAO();
	}

	public static RideDAO getInstance() {
		return InternalClass.instance;
	}
	
	@SuppressWarnings("unchecked")
	public boolean seleteRideOfBattle() {
		List<RideInfo> rideList = getSqlMapClient(DbConstants.GAME_DB).queryList("seleteRideOfBattle");
		if (rideList != null && !rideList.isEmpty()) {
			RoleInfo roleInfo = null;
			for (RideInfo info : rideList) {
				roleInfo = RoleInfoMap.getRoleInfo(info.getRoleId());
				if (roleInfo == null) {
					continue;
				}
				
				roleInfo.setRideInfo(info);
				RideService.recalRideFightVal(info);
			}
		}
		
		return true;
	}
	
	/**
	 * 插入坐骑信息
	 * 
	 * @param info
	 */
	public boolean insertRideInfo(RideInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertRideInfo", info);
		} catch (Exception e) {
			logger.error("RideDAO.insertRideInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新坐骑信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean updateRideInfo(RideInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRideInfo", info);
		} catch (Exception e) {
			logger.error("RideDAO.updateRideInfo error!",e);
			result = false;
		}
		return result;
	}
}
