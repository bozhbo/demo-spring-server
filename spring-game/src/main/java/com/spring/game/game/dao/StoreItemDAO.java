package com.snail.webgame.game.dao;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.StoreItemInfo;

public class StoreItemDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private StoreItemDAO() {
	}

	private static class InternalClass {
		public final static StoreItemDAO instance = new StoreItemDAO();
	}

	public static StoreItemDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 刷新商店商品
	 * 
	 * @param roleId
	 * @param storeType
	 * @param list
	 * @return
	 */
	public boolean refreshRoleStoreItem(int roleId, int storeType, List<StoreItemInfo> list) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH, false);
			if(client != null){
				StoreItemInfo to = new StoreItemInfo();
				to.setRoleId(roleId);
				to.setStoreType(storeType);
				client.delete("deleteRoleStoreItemByType", to);
				for (StoreItemInfo info : list) {
					client.insert("insertStoreItem", info);
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
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新商店商品状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateStoreItemStatus(int id, int buyNum) {
		StoreItemInfo to = new StoreItemInfo();
		to.setId(id);
		to.setBuyNum(buyNum);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateStoreItemBuyNum", to);
		} catch (Exception e) {
			logger.error("updateStoreItemStatus error!",e);
			result = false;
		}
		return result;
	}
}
