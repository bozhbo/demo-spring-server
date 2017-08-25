package com.snail.webgame.game.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.HeroInfo;

public class SkillDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private SkillDAO() {
	}

	private static class InternalClass {
		public final static SkillDAO instance = new SkillDAO();
	}

	public static SkillDAO getInstance() {
		return InternalClass.instance;
	}

	//private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public boolean addOrUpdateHeroSkill(int heroId,String skillStr) {
		HeroInfo heroInfo = new HeroInfo();
		heroInfo.setId(heroId);
		heroInfo.setSkillStr(skillStr);

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("addOrUpdateHeroSkill", heroInfo);
		} catch (Exception e) {
			logger.error("addOrUpdateHeroSkill error!",e);
			result = false;
		}
		return result;
	}

}
