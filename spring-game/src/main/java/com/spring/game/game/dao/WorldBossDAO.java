package com.snail.webgame.game.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.WorldBossMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.BossInfo;


/**
 * 世界BOSS 
 * @author zhangyq
 *
 */
public class WorldBossDAO extends SqlMapDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private WorldBossDAO() {
	}

	private static class InternalClass {
		public final static WorldBossDAO instance = new WorldBossDAO();
	}

	public static WorldBossDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 查询世界boss
	 * 
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadWorldBoss() {
		List<BossInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectWorldBoss");
		if (list != null) {
			for (BossInfo info : list) {
				WorldBossMap.addWordList(info);
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("Load GAME_FIGHT_GEM Table success!");
		}
		return true;
	}

	/**
	 * 添加世界BOSS
	 * @param info
	 * @return
	 */
	public boolean insertWorldBoss(BossInfo info) {
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertWorldBossInfo", info);
		} catch (Exception e) {
			logger.error("insertWorldBoss error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新世界boss
	 * @return
	 */
	public boolean updateWorldBoss(int type, int level, int no, long hp, long currHp, float rate) {
		BossInfo to = new BossInfo();
		to.setBossType(type);
		to.setBosslevel(level);
		to.setBossNo(no);
		to.setAllHP(hp);
		to.setCurrHP(currHp);
		to.setRate(rate);
		
		try {
			getSqlMapClient(DbConstants.GAME_DB).update("updateWorldBoss", to);
			return true;
		} catch (Exception e) {
			logger.error("updateWorldBoss error!",e);
			return false;
		}
	}
	
	/**
	 * 更新世界boss HP
	 * @return
	 */
	public boolean updateWorldBossHP(int type, long hp) {
		BossInfo to = new BossInfo();
		to.setBossType(type);
		to.setCurrHP(hp);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateWorldBossbyHp", to);
		} catch (Exception e) {
			logger.error("updateWorldBossHP error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 删除世界boss
	 * 
	 * @param itemId
	 * @param itemNum
	 * @return
	 */
	public boolean deleteWorldBoss(int type) {
		BossInfo to = new BossInfo();
		to.setBossType(type);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("deleteRoleWeaponInfo", to);
		} catch (Exception e) {
			logger.error("deleteWorldBoss error!",e);
			result = false;
		}
		return result;
	}
}
