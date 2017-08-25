package com.snail.webgame.game.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.CompetitionPersistentInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

/**
 * 
 * 类介绍:竞技场功能持久化DAO
 * 
 * @author zhoubo
 * @2014-11-28
 */
public class CompetitionDao extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private CompetitionDao() {

	}

	private static class InternalClass {
		public final static CompetitionDao instance = new CompetitionDao();
	}

	public static CompetitionDao getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 批量更新角色竞技场数据
	 * 
	 * @param list
	 *            更新集合
	 * @return boolean true-更新成功 false-更新失败
	 */
	public boolean updateRolesCompetitionValue(Map<String, Object> map) {
		SqlMapClient client = getSqlMapClient(DbConstants.GAME_DB,
				ExecutorType.BATCH, false);

		try {
			if(client != null){
				client.update("updateCompetition", map);
				client.commit();
				return true;
			}else{
				return false;
			}
			
		} catch (Exception e) {
			if(client != null){
				client.rollback();
			}
			logger.error("CompetitionDao.updateRolesCompetitionValue error", e);
			return false;
		}
	}

	/**
	 * 批量更新角色竞技场数据到初始值
	 * 
	 * @param list
	 *            更新集合
	 * @return boolean true-更新成功 false-更新失败
	 */
	public boolean updateRolesCompetitionInitValue(List<CompetitionPersistentInfo> list) {
		if (list.size() <= 0) {
			return true;
		}

		SqlMapClient client = getSqlMapClient(DbConstants.GAME_DB,
				ExecutorType.BATCH, false);

		try {
			if(client != null){
				for (CompetitionPersistentInfo competitionPersistentInfo : list) {
					client.update("updateInitCompetition", competitionPersistentInfo);
				}

				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if(client != null){
				client.rollback();
			}
			logger.error("CompetitionDao.updateRolesCompetitionInitValue error", e);
			return false;
		}
	}

	/**
	 * 更新角色段位领取结果
	 * 
	 * @param roleInfo
	 *            角色信息
	 * @return boolean true-更新成功 false-更新失败
	 */
	public boolean updateRolesCompetitionAward(RoleInfo roleInfo) {
		if (roleInfo == null) {
			return true;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return false;
		}

		SqlMapClient client = getSqlMapClient(DbConstants.GAME_DB, false);
		try {
			client.update("updateCompetitionAward", roleLoadInfo);

			client.commit();
			return true;
		} catch (Exception e) {
			client.rollback();
			logger.error("CompetitionDao.updateRolesCompetitionAward error", e);
			return false;
		}
	}
}
