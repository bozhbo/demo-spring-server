package com.snail.webgame.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;

public class WeaponDao extends SqlMapDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private WeaponDao() {
	}

	private static class InternalClass {
		public final static WeaponDao instance = new WeaponDao();
	}

	public static WeaponDao getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 装备或者卸下神兵
	 * 
	 * @param seqId
	 * @param isInPosition
	 * @return
	 */
	public boolean equipOrOffWeapon(int seqId, int position) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("position", position);
		map.put("id", seqId);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("equipOrOffWeapon", map);
		} catch (Exception e) {
			logger.error("equipOrOffWeapon error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 插入神兵
	 * 
	 * @param seqId
	 * @param isInPosition
	 * @return
	 */
	public boolean insertRoleWeaponInfo(int seqId, RoleWeaponInfo roleWeaponInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertRoleWeaponInfo", roleWeaponInfo);
		} catch (Exception e) {
			logger.error("insertRoleWeaponInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 插入神兵
	 * @param list
	 * @return
	 */
	public boolean insertRoleWeaponList(List<RoleWeaponInfo> list) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client != null){
				for (RoleWeaponInfo info : list) {
					client.insert("insertRoleWeaponInfo", info);
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("insertRoleWeaponList error!", e);
			}
			if(client != null){
				client.rollback();
			}
			return false;
		}
	}
	
	/**
	 * 删除神兵
	 * 
	 * @param weaponIds
	 * @return
	 */
	public boolean deleteBatchRoleWeaponInfo(String[] weaponIds) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client != null){
				for (String id : weaponIds) {
					client.delete("deleteRoleWeaponInfo", id);
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("deleteBatchRoleWeaponInfo error!", e);
			}
			if(client != null){
				client.rollback();
			}
			return false;
		}
	}
	
	/**
	 * 查询角色阵型上的神兵
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean loadRoleWeaponInfoInPos() {
		logger.info("loadRoleWeaponInfoInPos start");
		
		SqlSession session = null;
		long size = 0;
		int pageSize = 6000000;// 单页数目
		try {
			session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession();
			if(session != null){
				size = (Long)session.selectOne("selectloadRoleWeaponInfoInPosCount");
				session.close();
			}
		} catch (Exception e) {
			if(session != null){
				session.close();
			}
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_WEAPON_INFO count failed!", e);
			}
			return false;
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("Load GAME_WEAPON_INFO count succ!,count="+size);
		}
		
		if (size > 0) {
			int totalPage = (int) ((size + pageSize - 1) / pageSize);// 总页数
			for (int i = 1; i <= totalPage; i++) {
				loadWeapInfoByRange1((i - 1) * pageSize,pageSize);
			}
		}
		
		return true;
		
	}
	
	
	public boolean loadWeapInfoByRange1(int start, int limit) {
		System.gc();
		
		try {
			TimeUnit.SECONDS.sleep(GameValue.GAME_LOAD_GAP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.gc();
		
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		
		try {
			if(session != null){
				HashMap<String, Integer> to = new HashMap<String, Integer>();
				to.put("begin", start);
				to.put("pageSize", limit);
				session.select("loadRoleWeaponInfoInPos", to, new ResultHandler() {
					@Override
					public void handleResult(ResultContext context) {
						RoleWeaponInfo info = (RoleWeaponInfo) context.getResultObject();
						RoleInfo roleInfo = RoleInfoMap.getRoleInfo(info.getRoleId());
						if (roleInfo == null) {
							return;
						}
						roleInfo.getRoleWeaponInfoPositionMap().put(info.getId(), info);
					}
				});
				
				if (logger.isInfoEnabled()) {
					logger.info("Load GAME_WEAPON_INFO Table sucess!,count="+limit);
				}
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_WEAPON_INFO Table failed!", e);
			}
			return false;
		} finally {
			try {
				if(session != null){
					session.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 装备或者卸下神兵
	 * 
	 * @param seqId
	 * @param afterExp 
	 * @param isInPosition
	 * @return
	 */
	public boolean upgradeRoleWeaponInfoById(int seqId, int level, int afterExp) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("id", seqId);
		map.put("level", level);
		map.put("exp", afterExp);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("upgradeRoleWeaponInfoById", map);
		} catch (Exception e) {
			logger.error("upgradeRoleWeaponInfoById error!",e);
			result = false;
		}
		return result;
	}
}
