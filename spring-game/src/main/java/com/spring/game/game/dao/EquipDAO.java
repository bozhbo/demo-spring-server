package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;

public class EquipDAO extends SqlMapDaoSupport {

	private static class InternalClass {
		public final static EquipDAO instance = new EquipDAO();
	}

	public static EquipDAO getInstance() {
		return InternalClass.instance;
	}

	private EquipDAO() {

	}

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public boolean loadEquipInfoByRange() {
		logger.info("loadEquipInfoByRange start");
		
		System.gc();
		
		try {
			TimeUnit.SECONDS.sleep(GameValue.GAME_LOAD_GAP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.gc();
		
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		Connection conn = session.getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		int count = 0;
		
		try {
			psmt = conn.prepareStatement("SELECT count(*) as ALL_COUNT FROM GAME_EQUIP_INFO");
			
			rs = psmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("ALL_COUNT");
			}
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_EQUIP_INFO count succ!,count="+count);
			}
		} catch (SQLException e) {
			logger.error("EquipDAO.loadEquipInfoByRange error!!",e);
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				
				if (psmt != null) {
					psmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				
				session.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		boolean result = true;
		int every = 6000000;
		
		int i = 0;
		
		while (true) {
			if (i * every >= count) {
				break;
			}
			
			result = loadEquipInfoByRange1((i * every), every);
			
			if (!result) {
				return result;
			}
			
			i++;
		}
		
		return result;
	}
	
	public boolean loadEquipInfoByRange1(int start, int limit) {
		System.gc();
		
		try {
			TimeUnit.SECONDS.sleep(GameValue.GAME_LOAD_GAP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.gc();
		
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = session.getConnection();
			psmt = conn.prepareStatement("SELECT N_ID,N_ROLE_ID,N_HERO_ID,"
					+ "N_EQUIP_NO,N_EQUIP_TYPE,N_EQUIP_LEVEL,N_EQUIP_EXP,N_EQUIP_REFINE_LEVEL,N_ENCHANT_LEVEL,N_ENCHANT_EXP"
					+ " FROM GAME_EQUIP_INFO LIMIT ?,?");
			
			psmt.setInt(1, start);
			psmt.setInt(2, limit);
			psmt.setFetchSize(500000);
			
			rs = psmt.executeQuery();
			
			int equipId = 0;
			int roleId = 0;
			int heroId = 0;
			
			int index = 0;

			while (rs.next()) {
				equipId = (int)rs.getLong("N_ID");
				roleId = rs.getInt("N_ROLE_ID");
				heroId = rs.getInt("N_HERO_ID");
				
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
				
				if (roleInfo != null) {
					HeroInfo heroInfo = roleInfo.getHeroMap().get(heroId);
					EquipInfo equipInfo = null;
					if (heroInfo != null) {
						equipInfo = new EquipInfo();
						equipInfo.setId(equipId);
						equipInfo.setEquipNo(rs.getInt("N_EQUIP_NO"));
						equipInfo.setEquipType(rs.getInt("N_EQUIP_TYPE"));
						equipInfo.setLevel(rs.getShort("N_EQUIP_LEVEL"));
						equipInfo.setExp(rs.getInt("N_EQUIP_EXP"));
						equipInfo.setRefineLv(rs.getShort("N_EQUIP_REFINE_LEVEL"));
						equipInfo.setEnchantLv(rs.getShort("N_ENCHANT_LEVEL"));
						equipInfo.setEnchantExp(rs.getInt("N_ENCHANT_EXP"));
						
						heroInfo.getEquipMap().put(equipInfo.getId(), equipInfo);
						index++;
					}
					
					if(roleInfo.getLockShizhuang().values().contains(equipId)){
						if(equipInfo == null){
							equipInfo = new EquipInfo();
							equipInfo.setId(equipId);
							equipInfo.setEquipNo(rs.getInt("N_EQUIP_NO"));
							equipInfo.setEquipType(rs.getInt("N_EQUIP_TYPE"));
							equipInfo.setLevel(rs.getShort("N_EQUIP_LEVEL"));
							equipInfo.setExp(rs.getInt("N_EQUIP_EXP"));
							equipInfo.setRefineLv(rs.getShort("N_EQUIP_REFINE_LEVEL"));
							equipInfo.setEnchantLv(rs.getShort("N_ENCHANT_LEVEL"));
							equipInfo.setEnchantExp(rs.getInt("N_ENCHANT_EXP"));
							index++;
						}						
						roleInfo.getLockShizhuangMap().put(equipInfo.getEquipType(), equipInfo);
					}
				}
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_EQUIP_INFO Table success! " + index);
			}
		} catch (SQLException e) {
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_EQUIP_INFO Table failed!", e);
			}
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				
				if (psmt != null) {
					psmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				if(session != null){
					session.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}

	public boolean addEquip(long roleId, long heroId, List<EquipInfo> upAddBagItem) {
		if (upAddBagItem.size() <= 0) {
			return true;
		}
		SqlSession session = null;
		Map<String, Object> to = new HashMap<String, Object>();
		to.put("roleId", roleId);
		to.put("heroId", heroId);
		try {
			session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
			if(session != null){
				for (EquipInfo info : upAddBagItem) {
					to.put("equipNo", info.getEquipNo());
					to.put("equipType", info.getEquipType());
					to.put("level", info.getLevel());
					if (session.insert("insertEquipInfo", to) != 1) {
						throw new Exception();
					}
					info.setId(((Long) to.get("id")).intValue());
				}
				session.commit();
				session.close();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if(session != null){
				session.rollback();
				session.close();
			}
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
			return false;
		}
	}

	public boolean deleteEquip(List<Integer> equipIds) {
		if (equipIds == null || equipIds.size() <= 0) {
			return true;
		}
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client != null){
				for (long id : equipIds) {
					client.delete("deleteEquip", id);
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
			
			if (logger.isErrorEnabled()) {
				logger.error("EquipDAO.addEquip error", e);
			}
			return false;
		}
	}

	/**
	 * 删除装备
	 * @param itemId
	 * @param itemNum
	 * @return
	 */
	public boolean deleteEquip(long equipId) {
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("deleteEquip", equipId);
		} catch (Exception e) {
			logger.error("EquipDAO.deleteEquip error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 将多件装备穿到同一个武将身上
	 * @param HeroId
	 * @param equipMap <EquipId, heroId>
	 * @return
	 */
	public boolean wearEquips(HashMap<Integer, Integer> equipMap) {
		SqlMapClient sqlMapClient = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH, false);
		try {
			for(int equipId : equipMap.keySet()){
				int heroId = equipMap.get(equipId);
				Map<String, Object> to = new HashMap<String, Object>();

				to.put("id", equipId);
				to.put("heroId", heroId);
				sqlMapClient.update("updateEquipHeroID", to);
			}
			sqlMapClient.commit();
		} catch (Exception e) {
			logger.error("EquipDAO.wearEquips error!!!",e);
			sqlMapClient.rollback();
			return false;
		}
		return true;
	}

	/**
	 * 更新装备强化等级
	 * @param equipId
	 * @param level
	 * @return
	 */
	public boolean updateEquipLevel(int equipId, short level, int exp) {
		EquipInfo to = new EquipInfo();
		to.setId(equipId);
		to.setLevel(level);
		to.setExp(exp);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateEquipLevel", to);
		} catch (Exception e) {
			logger.error("EquipDAO.updateEquipLevel error",e);
			result = false;
		}
		return result;

	}

	/**
	 * 更新装备
	 * @param equipId
	 * @param level
	 * @return
	 */
	public boolean updateEquipNo(int equipId, int equipNo) {
		EquipInfo to = new EquipInfo();
		to.setId(equipId);
		to.setEquipNo(equipNo);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateEquipNo", to);
		} catch (Exception e) {
			logger.error("EquipDAO.updateEquipNo error!",e);
			result = false;
		}
		return result;

	}
	
	/**
	 * 更新装备的石头
	 * @param equipId
	 * @param level
	 * @return
	 */
	public boolean updateEquipStones(long equipId, String stones) {
		Map<String, Object> to = new HashMap<String, Object>();
		to.put("id", equipId);
		to.put("stones", stones);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateEquipStones", to);
		} catch (Exception e) {
			logger.error("EquipDAO.updateEquipStones error!!",e);
			result = false;
		}
		return result;

	}
	
	/**
	 * 熔炼功能 删除熔炼的装备
	 * @param delList
	 * @return
	 */
	public boolean batchEquipResolve(List<Integer> delList){
		if(delList == null || delList.size() <= 0){
			return true;
		}
		
		boolean result;
		try {
			result = getSqlMapClient("GAME_DB").deleteBatch("batchDelEquip", delList);
		} catch (Exception e) {
			logger.error("EquipDAO.batchEquipResolve error!!",e);
			result = false;
		}
		
		if(result){
			if(logger.isInfoEnabled()){
				logger.info("batchEquipResolve method successful");
			}
		}else{
			if(logger.isInfoEnabled()){
				logger.info("batchEquipResolve method failure");
			}
		}
		
		return result;
	}
	
	/**
	 * 更新装备精炼等级
	 * @param equipId
	 * @param refineLevel
	 * @return
	 */
	public boolean updateEquipRefineLevel(int equipId, int refineLevel){
		EquipInfo to = new EquipInfo();
		to.setId(equipId);
		to.setRefineLv((short)refineLevel);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateEquipRefineLevel", to);
		} catch (Exception e) {
			logger.error("EquipDAO.updateEquipRefineLevel error!!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 批量更新
	 * @param equipIds
	 * @return
	 */
	public boolean batchUpdateEquipLevel(List<Integer> equipIds){
		if (equipIds == null || equipIds.size() <= 0) {
			return true;
		}
		SqlMapClient client = null;
		EquipInfo to = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH, false);
			if(client!= null){
				for (int id : equipIds) {
					to = new EquipInfo();
					to.setId(id);
					to.setLevel((short)0);
					to.setExp(0);
					to.setRefineLv((short)0);
					to.setEnchantLv((short)0);
					to.setEnchantExp(0);
					
					client.update("updateEquipInfo", to);
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if(client!= null){
				client.rollback();
			}
			if (logger.isErrorEnabled()) {
				logger.error("EquipDAO.batchUpdateEquipLevel error", e);
			}
			return false;
		}
	}
	
	/**
	 * 设置附魔等级
	 * @param equipId
	 * @param enchantLv
	 * @param enchantExp
	 * @return
	 */
	public boolean updateEquipEnchant(int equipId, short enchantLv,int enchantExp){
		EquipInfo to = new EquipInfo();
		to.setId(equipId);
		to.setEnchantLv(enchantLv);
		to.setEnchantExp(enchantExp);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateEquipEnchant", to);
	}
}