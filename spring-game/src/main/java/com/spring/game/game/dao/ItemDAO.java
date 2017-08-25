package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.snail.webgame.game.cache.SnatchPatchMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.xml.cache.SnatchMap;

public class ItemDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ItemDAO() {
	}

	private static class InternalClass {
		public final static ItemDAO instance = new ItemDAO();
	}

	public static ItemDAO getInstance() {
		return InternalClass.instance;
	}
	

	/**
	 * 查询角色道具
	 * 
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BagItemInfo> loadRoleBagItem(int roleId) {
		List<BagItemInfo> bagItemlist = getSqlMapClient(DbConstants.GAME_DB).queryList("selectRoleBagItem", roleId);
		return bagItemlist;
	}

	/**
	 * 添加物品（不适用于装备）
	 * 
	 * @param upAddBagItem
	 * @return
	 */
	public boolean addItemBatch(List<BagItemInfo> insertBagItem,
			Map<Integer, Integer> updateMum) {
		SqlMapClient client = null;

		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH,
					false);
			if(client != null){
				for (BagItemInfo info : insertBagItem) {
					client.insert("insertBagItem", info);
				}
				BagItemInfo to = null;
				for (int itemId : updateMum.keySet()) {
					to = new BagItemInfo();
					to.setId(itemId);
					to.setNum(updateMum.get(itemId));
					if (to.getNum() > 0) {
						client.update("updateBagItemNum", to);
					} else {
						client.delete("deleteBagItem", to);
					}
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
				logger.error("ItemDAO.addItemBatch error!", e);
			}
			return false;
		}
	}

	/**
	 * 物品消耗
	 * 
	 * @param itemId
	 * @param itemNum
	 * @return
	 */
	public boolean updateBagItem(Map<Integer, Integer> map) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH,
					false);
			BagItemInfo to = null;
			if(client != null){
				for (int itemId : map.keySet()) {
					int itemNum = map.get(itemId);
					to = new BagItemInfo();
					to.setId(itemId);
					to.setNum(itemNum);
					if (itemNum > 0) {
						client.update("updateBagItemNum", to);
					} else {
						client.delete("deleteBagItem", to);
					}
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
				logger.error("execute update error:" + e.getMessage(), e);
			}
			return false;
		}
	}

	/**
	 * 装备升级
	 * 
	 * @param itemId
	 * @return
	 */
	public boolean updateBagItemLevel(int itemId, int level) {
		BagItemInfo to = new BagItemInfo();
		to.setId(itemId);
		to.setLevel(level);

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update(
					"updateBagItemLevel", to);
		} catch (Exception e) {
			logger.error("ItemDAO.updateBagItemLevel error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 装备附魔
	 * 
	 * @param itemId
	 * @param star
	 * @param exp
	 * @return
	 */
	public boolean updateBagItemStar(long itemId, int star, int exp) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", itemId);
		param.put("star", star);
		param.put("exp", exp);

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update(
					"updateBagItemStar", param);
		} catch (Exception e) {
			logger.error("ItemDAO.updateBagItemStar error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 加载夺宝活动石头信息
	 * 
	 * @return
	 */
	public boolean loadSnatchRivalInfo() {
		logger.info("loadSnatchRivalInfo start");
		
		boolean result = true;
		int count = 0;
		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			conn = session.getConnection();
			
			psmt = conn.prepareStatement("SELECT count(*) as ALL_COUNT FROM GAME_BAG_ITEM");
			
			rs = psmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("ALL_COUNT");
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
				logger.error("loadSnatchRivalInfo error!!",e);
				e.printStackTrace();
				return false;
			}
		}

		System.out.println("GAME_BAG_ITEM Num="+ count);
		int every = 5000000;
		int i = 0;
		while (true) {
			if (i * every >= count) {
				break;
			}

			result = loadBagItemByRange1((i * every), every);

			if (!result) {
				return result;
			}

			i++;
		}

		return result;
	}

	public boolean loadBagItemByRange1(int start, int limit) {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.gc();

		System.out.println("freeMemory = "+ (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(ExecutorType.SIMPLE, false);
		Connection conn = session.getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		long time1 = System.currentTimeMillis();
		try {
			psmt = conn.prepareStatement("SELECT N_ROLE_ID,N_NO FROM GAME_BAG_ITEM LIMIT ?,?");
			psmt.setInt(1, start);
			psmt.setInt(2, limit);
			psmt.setFetchSize(500000);
			
			rs = psmt.executeQuery();
			
		
			long time2 = System.currentTimeMillis();
			
			int i = 0;
			while (rs.next()) 
			{
				int itemNo = rs.getInt("N_NO");
				int roleId = rs.getInt("N_ROLE_ID");
				
				if (SnatchMap.get(itemNo) != null) {
					SnatchPatchMap.addRivalInfo(itemNo, roleId);
					i++;
				}
			}
			long time3 = System.currentTimeMillis();
			
			System.out.println("----1="+(time2-time1)+",---2="+(time3-time2));
	
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_BAG_ITEM Table success!" + i);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
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
				logger.error("ItemDAO.loadBagItemByRange1 error!",e);
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 查询角色装备
	 * @param roleId
	 * @return
	 */
	public static List<BagItemInfo> getBagItem(int roleId)
	{
		List<BagItemInfo> bagItemList = new ArrayList<BagItemInfo>();
		bagItemList = ItemDAO.getInstance().loadRoleBagItem(roleId);
		return bagItemList;
	}
	
	
}
