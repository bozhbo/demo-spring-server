package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.cache.MineInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.GameSettingInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;

public class HeroDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private HeroDAO() {
	}

	private static class InternalClass {
		public final static HeroDAO instance = new HeroDAO();
	}

	public static HeroDAO getInstance() {
		return InternalClass.instance;
	}

	public boolean loadAllHeroInfoByRange(Map<Integer, Integer> starHeroIds, Map<Integer, Map<String, Integer>> itemMap) {
		System.gc();

		try {
			TimeUnit.SECONDS.sleep(GameValue.GAME_LOAD_GAP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.gc();

		System.out.println("freeMemory = " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));

		// 获取矿上阵武将
		Set<Integer> heroIds = MineInfoMap.getMineHeros();		
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(
				ExecutorType.SIMPLE, false);
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			conn = session.getConnection();
			psmt = conn.prepareStatement("SELECT N_ID,N_ROLE_ID,N_NO,N_DEPLOY_STATUS,"
					+ " N_HERO_LEVEL,N_HERO_EXP,N_INTIMACY_LEVEL,N_INTIMACY_VALUE," + " N_QUALITY,N_STAR,S_SKILL_STR"
					+ " FROM GAME_HERO_INFO");
			psmt.setFetchSize(100000);
			rs = psmt.executeQuery();
			int index = 0;
			while (rs.next()) {
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(rs.getInt("N_ROLE_ID"));

				if (roleInfo != null) {
					if (roleInfo.getHeroMap() == null) {
						roleInfo.setHeroMap(new ConcurrentHashMap<Integer, HeroInfo>());
					}
					byte b = (byte) rs.getInt("N_DEPLOY_STATUS");
					int no = rs.getInt("N_NO");
					int id = rs.getInt("N_ID");
					int star = rs.getInt("N_STAR");
					if ((b > 0 && b <= GameValue.FIGHT_ARMY_LIMIT) || heroIds.contains(id)
							|| roleInfo.getHireHeroMap().containsKey(id)) {
						HeroInfo heroInfo = new HeroInfo();
						heroInfo.setId(id);
						heroInfo.setRoleId(rs.getInt("N_ROLE_ID"));
						heroInfo.setHeroNo(no);
						heroInfo.setDeployStatus(b);
						heroInfo.setHeroLevel(rs.getInt("N_HERO_LEVEL"));
						heroInfo.setHeroExp(rs.getInt("N_HERO_EXP"));
						heroInfo.setIntimacyLevel(rs.getInt("N_INTIMACY_LEVEL"));
						heroInfo.setIntimacyValue(rs.getInt("N_INTIMACY_VALUE"));

						heroInfo.setQuality(rs.getInt("N_QUALITY"));
						heroInfo.setStar(star);
						heroInfo.setSkillStr(rs.getString("S_SKILL_STR"));

						roleInfo.getHeroMap().put(heroInfo.getId(), heroInfo);

						index++;
					}
					if (b != 1) {
						roleInfo.setCommHeroNum(roleInfo.getCommHeroNum() + 1);
					}
					if (b > GameValue.FIGHT_ARMY_LIMIT) {
						roleInfo.getJbHeroNoMap().put(b, no);
					}
					if (b != 1 && star > 3 && star <= 5 && starHeroIds != null && itemMap != null) {
						GameSettingInfo setting = GameSettingMap.getValue(GameSettingKey.HERO_STAR_DOWN);
						if (setting != null) {
							continue;
						}

						HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(no);
						if (heroXMLInfo == null) {
							continue;
						}
						String chipNo = String.valueOf(heroXMLInfo.getChipNo());
						int addChipNum = (star == 4 ? 120 : 300);
						int addMoney = (star == 4 ? 200000 : 700000);

						Map<String, Integer> roleMap = itemMap.get(roleInfo.getId());
						if (roleMap == null) {
							roleMap = new HashMap<String, Integer>();
							itemMap.put(roleInfo.getId(), roleMap);
						}
						Integer oldChipNum = roleMap.get(chipNo);
						if (oldChipNum == null) {
							roleMap.put(chipNo, addChipNum);
						} else {
							roleMap.put(chipNo, addChipNum + oldChipNum);
						}
						Integer oldMoney = roleMap.get(ConditionType.TYPE_MONEY.getName());
						if (oldMoney == null) {
							roleMap.put(ConditionType.TYPE_MONEY.getName(), addMoney);
						} else {
							roleMap.put(ConditionType.TYPE_MONEY.getName(), addMoney + oldMoney);
						}
						starHeroIds.put(id, roleInfo.getId());
					}
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_HERO_INFO Table success!" + index);
			}
		} catch (SQLException e) {
			if (logger.isInfoEnabled()) {
				logger.error("HeroDAO.loadAllHeroInfoByRange error!", e);
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

				if (conn != null) {
					conn.close();
				}

				session.close();
			} catch (SQLException e) {
				if (logger.isInfoEnabled()) {
					logger.error("HeroDAO.loadAllHeroInfoByRange finally error!", e);
				}

				return false;
			}
		}

		return true;
	}

	/**
	 * 添加武将
	 * @param heros
	 * @return
	 */
	public boolean insertHeros(List<HeroInfo> heros) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				for (HeroInfo heroInfo : heros) {
					client.insert("insertHero", heroInfo);
				}
				client.commit();
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			if (client != null) {
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 更新英雄等级
	 * @param id
	 * @param newLv
	 * @return
	 */
	public boolean updateHeroLv(int id, int heroLevel, int heroExp) {
		HeroInfo heroInfo = new HeroInfo();
		heroInfo.setId(id);
		heroInfo.setHeroLevel(heroLevel);
		heroInfo.setHeroExp(heroExp);

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateHeroLv", heroInfo);
		} catch (Exception e) {
			logger.error("HeroDAO.updateHeroLv error!!", e);
			result = false;
		}
		return result;
	}

	/**
	 * 武将品质升级
	 * @param id
	 * @param quality
	 * @return
	 */
	public boolean updateHeroQuality(int id, int quality) {
		HeroInfo heroInfo = new HeroInfo();
		heroInfo.setId(id);
		heroInfo.setQuality(quality);

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateHeroQuality", heroInfo);
		} catch (Exception e) {
			logger.error("HeroDAO.updateHeroQuality error!!", e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新布阵
	 * @param upMap <heroId,deployPos>
	 * @return
	 */
	public boolean updateHeroDeployStatus(Map<Integer, Byte> upMap) {

		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				for (int heroId : upMap.keySet()) {
					byte deployPos = upMap.get(heroId);
					HeroInfo to = new HeroInfo();
					to.setId(heroId);
					to.setDeployStatus(deployPos);
					client.update("updateHeroDeployStatus", to);
				}
				client.commit();
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			logger.error("HeroDAO.updateHeroDeployStatus error!!", e);
			if (client != null) {
				client.rollback();
			}
			return false;
		}

	}

	/**
	 * 更新单个武将的上阵状态
	 * @param heroId
	 * @param position
	 * @param sqlMapClient
	 * @return
	 */
	public boolean updateOneHeroDeployStatus(int heroId, byte position, SqlMapClient sqlMapClient) {

		boolean result = false;
		if (sqlMapClient == null) {
			sqlMapClient = getSqlMapClient(DbConstants.GAME_DB);
		}
		HeroInfo heroInfo = new HeroInfo();
		heroInfo.setId(heroId);
		heroInfo.setDeployStatus(position);
		try {
			result = sqlMapClient.update("updateHeroDeployStatus", heroInfo);
		} catch (Exception e) {
			logger.error("updateOneHeroDeployStatus error!!", e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新武将亲密度等级
	 * @param heroId
	 * @param intimacyLevel
	 * @param intimacyValue
	 * @return
	 */
	public boolean updateHeroIntimacy(int heroId, int intimacyLevel, int intimacyValue) {
		HeroInfo heroInfo = new HeroInfo();
		heroInfo.setId(heroId);
		heroInfo.setIntimacyLevel(intimacyLevel);
		heroInfo.setIntimacyValue(intimacyValue);

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateHeroIntimacy", heroInfo);
		} catch (Exception e) {
			logger.error("updateHeroIntimacy error!!", e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新武将星级
	 * @param heroId
	 * @param star
	 * @return
	 */
	public boolean updateHeroStar(int heroId, int star) {
		HeroInfo heroInfo = new HeroInfo();
		heroInfo.setId(heroId);
		heroInfo.setStar(star);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateHeroStar", heroInfo);
	}

	public boolean updateHeroStarDown(Map<Integer, Integer> starMap) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client != null) {
				for (int heroId : starMap.keySet()) {
					HeroInfo to = new HeroInfo();
					to.setId(heroId);
					to.setStar(3);
					client.update("updateHeroStar", to);
				}
				client.commit();
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			logger.error("HeroDAO.updateHeroStar error!!", e);
			if (client != null) {
				client.rollback();
			}
			return false;
		}

	}

}
