package com.snail.webgame.game.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.protocal.arena.service.ArenaService;

public class FightArenaDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static class InternalClass {
		public final static FightArenaDAO instance = new FightArenaDAO();
	}

	public static FightArenaDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 分页加载竞技场数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadAllFightArena() {
		int pageSize = 5000;// 单页数目
		long size = getSqlMapClient(DbConstants.GAME_DB).query("selectFightArenaCount");
		List<FightArenaInfo> list = null;
		if (size > 0) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			int totalPage = (int) ((size + pageSize - 1) / pageSize);// 总页数
			for (int i = 1; i <= totalPage; i++) {
				int begin = (i - 1) * pageSize;
				map.put("begin", begin);
				map.put("pageSize", pageSize);
				list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectFightArenabyPage", map);
				if (list != null && list.size() > 0) {
					for (FightArenaInfo info : list) {
						FightArenaInfoMap.addFightArenaInfo(info);
					}
					list.clear();
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_FIGHT_ARENA Table success!");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("Start Init GAME_FIGHT_ARENA Table...");
			}
			long now = System.currentTimeMillis();
			list = ArenaService.getRandoms();
			if (list != null && list.size() > 0) {
				if (insertFightArena(list)) {
					for (FightArenaInfo info : list) {
						FightArenaInfoMap.addFightArenaInfo(info);
					}
					list.clear();
					if (logger.isInfoEnabled()) {
						long costTime = System.currentTimeMillis() - now;
						logger.info("Init GAME_FIGHT_ARENA Table finish!cost Time:" + costTime);
					}
				} else {
					if (logger.isErrorEnabled()) {
						logger.error("Insert Init GAME_FIGHT_ARENA Table failed!");
					}
					return false;
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error("Init GAME_FIGHT_ARENA Table failed!");
				}
				return false;
			}
			if (logger.isInfoEnabled()) {
				logger.info("Init GAME_FIGHT_ARENA Table success!");
			}
		}
		return true;
	}

	/**
	 * 插入新纪录
	 * @param info
	 * @return
	 */
	public boolean insertFightArena(FightArenaInfo info) {
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertFightArena", info);
		} catch (Exception e) {
			logger.error("FightArenaDAO.insertFightArena error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 插入新纪录
	 * @param info
	 * @return
	 */
	public boolean insertFightArena(List<FightArenaInfo> list) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client!= null){
				for (FightArenaInfo info : list) {
					client.insert("insertFightArena", info);
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("FightArenaDAO.insertFightArena error!", e);
			}
			if(client!= null){
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 更新竞技场排名信息
	 * @param id
	 * @param rolePlace
	 * @param roleMaxPlace
	 * @param defendId
	 * @param defendPlace
	 * @param defendMaxPlace
	 * @return
	 */
	public boolean updateFightArenaPlace(int id, int rolePlace, int roleMaxPlace, int defendId, int defendPlace,
			int defendMaxPlace) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client!= null){
				FightArenaInfo to = new FightArenaInfo();
				to.setId(id);
				to.setPlace(rolePlace);
				to.setMaxPlace(roleMaxPlace);

				client.update("updateFightArenaPlace", to);

				to.setId(defendId);
				to.setPlace(defendPlace);
				to.setMaxPlace(defendMaxPlace);
				client.update("updateFightArenaPlace", to);

				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("FightArenaDAO.updateFightArenaPlace error!!", e);
			}
			if(client!= null){
				client.rollback();
			}
			return false;
		}

	}
}