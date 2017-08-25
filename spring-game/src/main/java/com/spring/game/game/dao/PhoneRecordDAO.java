package com.snail.webgame.game.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.PhoneRecordMap;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.PhoneRecordInfo;

public class PhoneRecordDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private PhoneRecordDAO() {
	}

	private static class InternalClass {
		public final static PhoneRecordDAO instance = new PhoneRecordDAO();
	}

	public static PhoneRecordDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 加载手机绑定信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadPhoneRecord() {
		List<PhoneRecordInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectPhoneRecord");
		if (list != null) {
			for (PhoneRecordInfo info : list) {
				PhoneRecordMap.addPhoneRecordInfo(info);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_PHONE_RECORD Table success!" + list.size());
			}
		}

		return true;
	}
	
	/**
	 * 插入手机绑定账号信息
	 * 
	 * @param info
	 * @return
	 */
	public boolean insertPhoneLink(PhoneRecordInfo info) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertPhoneLink", info);
		} catch (Exception e) {
			logger.error("insertPhoneLink error!",e);
			result = false;
		}
		return result;
	}
}
