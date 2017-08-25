package com.snail.webgame.game.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.GameSettingInfo;

public class GameSettingDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private GameSettingDAO() {
	}

	private static class InternalClass {
		public final static GameSettingDAO instance = new GameSettingDAO();
	}

	public static GameSettingDAO getInstance() {
		return InternalClass.instance;
	}

	@SuppressWarnings("unchecked")
	public boolean loadGameSetting() {
		try {
			List<GameSettingInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectGameSetting");

			for (GameSettingInfo gameSettingInfo : list) {
				GameSettingMap.addValue(gameSettingInfo);
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Load GAME_SETTING Table success!");
		}

		return checkAndInsert(GameSettingKey.SERVER_START_TIME);
	}

	public boolean checkAndInsert(GameSettingKey key) {
		if (GameSettingMap.getValue(key) == null) {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Calendar c = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			end.setTime(GameValue.COMPETITION_END_TIME);
			c.set(Calendar.HOUR_OF_DAY, end.get(Calendar.HOUR_OF_DAY) + 1);
			c.set(Calendar.MINUTE, end.get(Calendar.MINUTE));

			GameSettingInfo info = new GameSettingInfo();
			info.setKey(key.getValue());
			info.setValue(df.format(c.getTime()));

			if (insertGameSetting(info)) {
				GameSettingMap.addValue(info);
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean insertGameSetting(GameSettingInfo info) {
		return getSqlMapClient(DbConstants.GAME_DB).insert("insertGameSetting", info);
	}

	public boolean updateGameSettingValue(GameSettingKey key, String value) {
		GameSettingInfo to = new GameSettingInfo();
		to.setKey(key.getValue());
		to.setValue(value);
		return getSqlMapClient(DbConstants.GAME_DB).insert("updateGameSettingValue", to);
	}

	public boolean updateGameSettingComment(GameSettingKey key, String comment) {
		GameSettingInfo to = new GameSettingInfo();
		to.setKey(key.getValue());
		to.setComment(comment);
		return getSqlMapClient(DbConstants.GAME_DB).insert("updateGameSettingComment", to);
	}
}
