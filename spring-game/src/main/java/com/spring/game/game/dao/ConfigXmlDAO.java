package com.snail.webgame.game.dao;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.xml.ConfigXmlInfo;

/**
 * 游戏XML配置数据变更
 * 
 * @author leiqiang
 * @date Oct 8, 2010
 */
public class ConfigXmlDAO extends SqlMapDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ConfigXmlDAO() {
	}

	private static class InternalClass {
		public final static ConfigXmlDAO instance = new ConfigXmlDAO();
	}

	public static ConfigXmlDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 测试数据库连接
	 * 
	 * @return
	 */
	public boolean testdbConnect() {
		long value = getSqlMapClient(DbConstants.GAME_DB).query("testdbConnect");
		return value == 1;
	}

	/**
	 * 根据配置文件名称查询数据库中保存的配置文件
	 * 
	 * @param xmlName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ConfigXmlInfo> getXmlByName(String xmlName) {
		ConfigXmlInfo to = new ConfigXmlInfo();
		to.setXmlName(xmlName);
		return getSqlMapClient(DbConstants.GAME_DB).queryList("selectByXmlName", to);
	}

	/**
	 * 根据提供的记录id批量更新删除标识
	 * 
	 * @param idStrList
	 * @return
	 */
	public boolean batchUpdateDelFlag(List<String> idStrList) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH,false);
			ConfigXmlInfo to = null;
			for (String idStr : idStrList) {
				to = new ConfigXmlInfo();
				to.setDelFlag(1);
				to.setXmlLogId(idStr);
				client.update("updateConfigDelFlag", to);
			}
			client.commit();
			return true;
		} catch (Exception e) {
			client.rollback();
			return false;
		}
	}

	/**
	 * 修改XML配置文件
	 * 
	 * @param idStr
	 * @param xmlStr
	 * @return -1 表示执行错误<br>
	 *         0 表示没有执行任何记录<br>
	 *         1 执行成功
	 */
	public int modifyConfigDAO(String idStr, String xmlStr) {
		ConfigXmlInfo to = new ConfigXmlInfo();
		to.setModXmlContent(xmlStr);
		to.setXmlLogId(idStr);

		long count = (Long) getSqlMapClient(DbConstants.GAME_DB).query(
				"selectCountbyXmlLogId", idStr);
		if (count > 0) {
			boolean result = getSqlMapClient(DbConstants.GAME_DB).update(
					"updateConfigmodContent", to);
			if (result) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 插入新节点信息记录
	 * 
	 * @param xmlName
	 * @param idStr
	 * @param name
	 * @param xmlStr
	 * @return
	 */
	public boolean insertConfigDAO(String xmlName, String idStr, String name, String xmlStr) {
		ConfigXmlInfo to = new ConfigXmlInfo();
		to.setXmlName(xmlName);
		to.setXmlLogId(idStr);
		to.setXmlLogName(name);
		to.setXmlContent(xmlStr);
		to.setModXmlContent(xmlStr);
		to.setDelFlag(0);
		
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertConfigXml",to);
		} catch (Exception e) {
			logger.error("ConfigXmlDAO.insertConfigDAO error!!",e);
			result = false;
		}
		return result;
	}

}
