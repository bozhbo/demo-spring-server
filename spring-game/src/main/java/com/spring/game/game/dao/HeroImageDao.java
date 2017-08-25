package com.snail.webgame.game.dao;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.HeroImageInfo;

public class HeroImageDao extends SqlMapDaoSupport {
	//private static final Logger logger = LoggerFactory.getLogger("logs");

	private HeroImageDao() {

	}

	public static HeroImageDao getInstance() {
		return Inner.instance;

	}

	private static class Inner {
		private static final HeroImageDao instance = new HeroImageDao();
	}

	/**
	 * 插入镜像
	 * @param to
	 * @return
	 */
	public boolean insertHeroImageInfo(HeroImageInfo to) {
		return getSqlMapClient(DbConstants.GAME_DB).insert("insertHeroImageInfo", to);
	}

	/**
	 * 删除镜像
	 * @param to
	 * @return
	 */
	public boolean deleteHeroImageInfobyId(int imageId) {
		return getSqlMapClient(DbConstants.GAME_DB).delete("deleteHeroImageInfobyId", imageId);
	}
}
