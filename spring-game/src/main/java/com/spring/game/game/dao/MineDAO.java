package com.snail.webgame.game.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.cache.MineInfoMap;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.GameSettingInfo;
import com.snail.webgame.game.info.MineHelpRole;
import com.snail.webgame.game.info.MineInfo;
import com.snail.webgame.game.info.MinePrize;
import com.snail.webgame.game.info.MineRole;
import com.snail.webgame.game.protocal.mine.service.MineService;

public class MineDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private MineDAO() {
	};

	private static class InternalClass {
		public final static MineDAO instance = new MineDAO();
	}

	public static MineDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 加载记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadAllMines() {
		long size = getSqlMapClient(DbConstants.GAME_DB).query("selectMineCount");
		if (size <= 0) {
			long now = System.currentTimeMillis();
			if (logger.isInfoEnabled()) {
				logger.info("start init GAME_MINE_INFO Table!");
			}
			MineService.serverStartInitData();
			if (logger.isInfoEnabled()) {
				long costTime = System.currentTimeMillis() - now;
				logger.info("init GAME_MINE_INFO Table success! cost Time:" + costTime);
			}
			GameSettingDAO.getInstance().checkAndInsert(GameSettingKey.MINE_PRIZE_CHANGE);
		} else {
			List<MineInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectAllMine");
			if (list != null) {
				for (MineInfo info : list) {
					MineInfoMap.addMineInfo(info);
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_MINE_INFO Table success!");
			}
			List<MineRole> list1 = getSqlMapClient(DbConstants.GAME_DB).queryList("selectAllMineRole");
			if (list != null) {
				for (MineRole info : list1) {
					MineInfoMap.addMineRole(info);
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_MINE_ROLE Table success!");
			}

			GameSettingInfo setting = GameSettingMap.getValue(GameSettingKey.MINE_PRIZE_CHANGE);
			if (setting == null) {
				if(MineService.minePrizeInitData()){
					if (logger.isInfoEnabled()) {
						logger.info("mine data deal success!");
					}
					GameSettingDAO.getInstance().checkAndInsert(GameSettingKey.MINE_PRIZE_CHANGE);
				}else{
					if (logger.isInfoEnabled()) {
						logger.info("mine data deal falult!");
					}
					return false;
				}
			} else {
				List<MinePrize> list2 = getSqlMapClient(DbConstants.GAME_DB).queryList("selectAllMinePrize");
				if (list != null) {
					for (MinePrize info : list2) {
						MineInfoMap.addMinePrize(info);
					}
				}
				if (logger.isInfoEnabled()) {
					logger.info("Load GAME_MINE_PRIZE Table success!");
				}
			}
		}
		return true;
	}

	public boolean insertMines(List<MineInfo> mines) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				for (MineInfo mineInfo : mines) {
					client.insert("insertMine", mineInfo);
				}
				client.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if (client != null) {
				client.rollback();
			}
			return false;
		}
	}

	public boolean insertMineRole(MineRole info) {
		return getSqlMapClient(DbConstants.GAME_DB).insert("insertMineRole", info);
	}

	public boolean insertMinePrizes(List<MinePrize> list) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				for (MinePrize prize : list) {
					client.insert("insertMinePrize", prize);
				}
				client.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (client != null) {
				client.rollback();
			}
			return false;
		}
	}
	
	public boolean delMines(List<Integer> ids) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				for (int mineId : ids) {
					client.delete("deleteMine", mineId);
					client.delete("deleteMineRoleByMineId", mineId);
				}
				client.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if (client != null) {
				client.rollback();
			}
			return false;
		}
	}

	public boolean updateMines(List<Integer> delIds, List<MineInfo> addMines){
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				if(delIds != null){
					for (int mineId : delIds) {
						client.delete("deleteMine", mineId);
						client.delete("deleteMineRoleByMineId", mineId);
					}
				}
				if(addMines != null){
					for (MineInfo mineInfo : addMines) {
						client.insert("insertMine", mineInfo);
					}
				}
				
				client.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if (client != null) {
				client.rollback();
			}
			return false;
		}
	}
	
	public boolean updateMines(List<Integer> delIds,List<MinePrize> addPrizes, List<MineInfo> addMines){
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				if(delIds != null){
					for (int mineId : delIds) {
						client.delete("deleteMine", mineId);
						client.delete("deleteMineRoleByMineId", mineId);
					}
				}
				if(addPrizes != null){
					for (MinePrize prize : addPrizes) {
						client.insert("insertMinePrize", prize);
					}
				}
				if(addMines != null){
					for (MineInfo mineInfo : addMines) {
						client.insert("insertMine", mineInfo);
					}
				}
				
				client.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if (client != null) {
				client.rollback();
			}
			return false;
		}
	}
	
	public boolean delMineRole(int mineRoleId) {
		return getSqlMapClient(DbConstants.GAME_DB).delete("deleteMineRole", mineRoleId);
	}

	public boolean updateMineRoleStatus(int mineRoleId, byte status) {
		MineRole to = new MineRole();
		to.setId(mineRoleId);
		to.setStatus(status);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateMineRoleStatus", to);
	}

	public boolean updateMineRoleHelp(int mineRoleId, Map<Integer, MineHelpRole> helpRoles) {
		MineRole to = new MineRole();
		to.setId(mineRoleId);
		to.setHelpRoles(helpRoles);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateMineRoleHelp", to);
	}

	public boolean updateMineRoleHero(int mineRoleId, Map<Integer, Integer> heroMap) {
		MineRole to = new MineRole();
		to.setId(mineRoleId);
		to.setHeroMap(heroMap);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateMineRoleHero", to);
	}

	public boolean updateMineRoleEndTime(int mineRoleId, Timestamp endTime, byte endStatus) {
		MineRole to = new MineRole();
		to.setId(mineRoleId);
		to.setEndTime(endTime);
		to.setEndStatus(endStatus);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateMineRoleEndTime", to);
	}

	public boolean zlMineRoles(int mineRoleId, Timestamp endTime, byte endStatus, MineRole newMineRole) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, false);
			if (client != null) {
				MineRole to = new MineRole();
				to.setId(mineRoleId);
				to.setEndTime(endTime);
				to.setEndStatus(endStatus);
				client.update("updateMineRoleEndTime", to);
				client.insert("insertMineRole", newMineRole);
				client.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if (client != null) {
				client.rollback();
			}
			return false;
		}
	}

	public boolean updateMinePrizeStatus(int id, byte status) {
		MinePrize to = new MinePrize();
		to.setId(id);
		to.setStatus(status);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateMinePrizeStatus", to);
	}

	public boolean updateMinePrizeHelp(int id, Map<Integer, MineHelpRole> helpRoles) {
		MinePrize to = new MinePrize();
		to.setId(id);
		to.setHelpRoles(helpRoles);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateMinePrizeHelp", to);
	}

	public boolean deleteMinePrize(int id) {
		return getSqlMapClient(DbConstants.GAME_DB).delete("deleteMinePrize", id);
	}
}
