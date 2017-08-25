package com.snail.webgame.game.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.ToolBoxMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.ToolBoxInfo;

public class ToolBoxDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ToolBoxDAO() {
	}

	private static class InternalClass {
		public final static ToolBoxDAO instance = new ToolBoxDAO();
	}

	public static ToolBoxDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 加载运营礼包信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadToolBox() {
		List<ToolBoxInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectToolBoxInfo");
		if (list != null) {
			for (ToolBoxInfo info : list) {
				ToolBoxMap.addToolBoxInfo(info);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_TOOL_BOX Table success!" + list.size());
			}
		}

		return true;
	}
	
	/**
	 * 插入运营礼包信息
	 * 
	 * @param info
	 */
	public boolean insertToolBoxInfo(ToolBoxInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertToolBoxInfo", info);
		} catch (Exception e) {
			logger.error("insertToolBoxInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新运营礼包信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean updateToolBoxInfo(ToolBoxInfo info) {
		boolean result = false;
		try {
			if (info.getBoxType() == ToolBoxInfo.TYPE_BOX_CHARGE) {
				result = getSqlMapClient(DbConstants.GAME_DB).update("updateToolBoxInfo1", info);
			} else {
				result = getSqlMapClient(DbConstants.GAME_DB).update("updateToolBoxInfo2", info);
			}
		} catch (Exception e) {
			logger.error("updateToolBoxInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 移除运营礼包信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean removeToolBoxInfo(ToolBoxInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("removeToolBoxInfo", info);
		} catch (Exception e) {
			logger.error("removeToolBoxInfo error!",e);
			result = false;
		}
		return result;
	}
	
}
