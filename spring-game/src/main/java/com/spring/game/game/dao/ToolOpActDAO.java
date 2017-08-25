package com.snail.webgame.game.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.ToolOpActMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.ToolOpActivityInfo;
import com.snail.webgame.game.xml.info.RecruitItemXMLInfo;

public class ToolOpActDAO extends SqlMapDaoSupport {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ToolOpActDAO() {
	}

	private static class InternalClass {
		public final static ToolOpActDAO instance = new ToolOpActDAO();
	}

	public static ToolOpActDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 加载运营时限活动信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadToolOpActInfo() {
		List<ToolOpActivityInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectToolOpActInfo");
		if (list != null) {
			long now = System.currentTimeMillis();
			for (ToolOpActivityInfo info : list) {
				ToolOpActMap.addToolOpActivityInfo(info, now);
				
				// 加载限时武将缓存
				if (info.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_3) {
					
					if (info.getLotHeroStr() != null) {
						String[] tempArr = info.getLotHeroStr().split(":");
						info.setActHeroNo(Integer.valueOf(tempArr[0]));
						
						RecruitItemXMLInfo xmlInfo = new RecruitItemXMLInfo();
						xmlInfo.setItemNo(tempArr[0]);
						xmlInfo.setNum(1);
						xmlInfo.setRand(Integer.valueOf(tempArr[1]));
						
						info.getList().add(xmlInfo);
					}
					
					if (info.getLotRewardStr() != null) {
						String[] tempArr = info.getLotRewardStr().split(";");
						for (String str : tempArr) {
							String[] subArr = str.split(":");
							
							// 2:36000004:10;2:36000005:10
							RecruitItemXMLInfo xmlInfo = new RecruitItemXMLInfo();
							xmlInfo.setItemNo(subArr[1]);
							xmlInfo.setNum(Integer.valueOf(subArr[0]));
							xmlInfo.setRand(Integer.valueOf(subArr[2]));
							
							info.getList().add(xmlInfo);
						}
					}
					
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_TOOL_OPACT Table success!" + list.size());
			}
		}

		return true;
	}
	
	/**
	 * 插入运营时限活动信息
	 * 
	 * @param info
	 */
	public boolean insertToolOpActInfo(ToolOpActivityInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertToolOpActInfo", info);
		} catch (Exception e) {
			logger.error("insertToolOpActInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新运营时限活动信息
	 * 
	 * @param info
	 */
	public boolean updateToolOpActInfo(ToolOpActivityInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateToolOpActInfo", info);
		} catch (Exception e) {
			logger.error("updateToolOpActInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 移除运营时限活动信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean removeToolOpActInfo(ToolOpActivityInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("removeToolOpActInfo", info);
		} catch (Exception e) {
			logger.error("removeToolOpActInfo error!",e);
			result = false;
		}
		return result;
	}
}
