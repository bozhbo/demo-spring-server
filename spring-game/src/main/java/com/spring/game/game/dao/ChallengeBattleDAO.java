package com.snail.webgame.game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.SqlMapClientFactory;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.TeamChallengeRecord;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.ChallengeUpdateInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.TeamChallengeRecordSub;

public class ChallengeBattleDAO extends SqlMapDaoSupport {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ChallengeBattleDAO() {

	}

	private static class InternalClass {
		public final static ChallengeBattleDAO instance = new ChallengeBattleDAO();
	}

	public static ChallengeBattleDAO getInstance() {
		return InternalClass.instance;
	}

	/**
	 * 添加新的副本信息
	 * 
	 * @param challengeBattleInfo
	 * @return
	 */
	public boolean insertChallengeBattleInfo(ChallengeBattleInfo info) {
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).insert("insertChallengeBattleInfo", info);
		} catch (Exception e) {
			logger.error("ChallengeBattleDAO.insertChallengeBattleInfo error!!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新战斗结果
	 * 
	 * @param star
	 * @param bestPassTime
	 * @param id
	 * @return
	 */
	public boolean updateChallengeBattleInfo(String star, int id) {
		ChallengeBattleInfo to = new ChallengeBattleInfo();
		to.setId(id);
		to.setStar(star);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateChallengeBattleInfo", to);
		} catch (Exception e) {
			logger.error("ChallengeBattleDAO.updateChallengeBattleInfo error!!",e);
			result = false;
		}
		return result;

	}

	/**
	 * 删除副本信息
	 * 
	 * @param itemId
	 * @param itemNum
	 * @return
	 */
	public boolean deleteChallenge(int roleId) {
		ChallengeBattleInfo to = new ChallengeBattleInfo();
		to.setRoleId(roleId);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).delete("deleteChallengeBattle", to);
		} catch (Exception e) {
			logger.error("ChallengeBattleDAO.deleteChallenge error!!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新副本可攻击次数,上次战斗时间
	 * 
	 * @param id
	 * @param refreshTime
	 * @param fightNum
	 * @return
	 */
	public boolean updateChallengeAttackNum(int id, int canFightNum, Timestamp fightTime) {

		ChallengeBattleInfo info = new ChallengeBattleInfo();
		info.setId(id);
		info.setCanFightNum(canFightNum);
		info.setFightTime(fightTime);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateChallengeNum", info);
		} catch (Exception e) {
			logger.error("ChallengeBattleDAO.updateChallengeAttackNum error!!",e);
			result = false;
		}
		return result;
	}
	
	
	/**
	 * 更新副本可攻击次数,上次战斗时间
	 * 
	 * @param id
	 * @param refreshTime
	 * @param fightNum
	 * @return
	 */
	public boolean updateChallengeAttackNumBatch(List<ChallengeUpdateInfo> updateList) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client == null)
			{
				return false;
			}
			for (ChallengeUpdateInfo info : updateList) {
				client.update("updateChallengeNum1", info);
			}
			client.commit();
			return true;
		} catch (Exception e) {
			client.rollback();
			logger.error("ChallengeBattleDAO.updateChallengeAttackNumBatch error!!",e);
			return false;
		}
	}
	

	/**
	 * 更新副本可攻击次数
	 * 
	 * @param id
	 * @param refreshTime
	 * @param fightNum
	 * @return
	 */
	public boolean updateChallengeTimes(int id, int canFightNum) {

		ChallengeBattleInfo info = new ChallengeBattleInfo();
		info.setId(id);
		info.setCanFightNum(canFightNum);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateChallengeTimes", info);
		} catch (Exception e) {
			logger.error("ChallengeBattleDAO.updateChallengeTimes error!!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新副本购买次数
	 * 
	 * @param id
	 * @param refreshTime
	 * @param fightNum
	 * @return
	 */
	public boolean updateGoldBuy(int id, int goldNum) {

		ChallengeBattleInfo info = new ChallengeBattleInfo();
		info.setId(id);
		info.setGoldNum(goldNum);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateGoldBuy", info);
		} catch (Exception e) {
			logger.error("ChallengeBattleDAO.updateGoldBuy error!!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 插入副本荣誉榜
	 * 
	 * @return
	 */
	public boolean insertTeamChallengeRecord(int duplicateId, int roleId1, int roleId2, int roleId3, long firstTime, long costTime, int fightValue1, int fightValue2, int fightValue3, int lv1, int lv2, int lv3) {
		boolean result;
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("duplicateId", duplicateId);
		to.put("firstRoleId1", roleId1);
		to.put("firstRoleId2", roleId2);
		to.put("firstRoleId3", roleId3);
		to.put("firstTime", firstTime);
		to.put("quickTime", costTime);
		to.put("fightValue1", fightValue1);
		to.put("fightValue2", fightValue2);
		to.put("fightValue3", fightValue3);
		to.put("lv1", lv1);
		to.put("lv2", lv2);
		to.put("lv3", lv3);
		
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("insertTeamChallengeRecord", to);
		} catch (Exception e) {
			logger.error("ChallengeBattleDAO.insertTeamChallengeRecord error!!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 插入副本荣誉榜
	 * 
	 * @param id
	 * @param refreshTime
	 * @param fightNum
	 * @return
	 */
	public boolean updateTeamChallengeRecord(int duplicateId, int roleId1, int roleId2, int roleId3, long costTime, int fightValue1, int fightValue2, int fightValue3, int lv1, int lv2, int lv3) {
		boolean result;
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("duplicateId", duplicateId);
		to.put("quickRoleId1", roleId1);
		to.put("quickRoleId2", roleId2);
		to.put("quickRoleId3", roleId3);
		to.put("quickTime", costTime);
		to.put("fightValue1", fightValue1);
		to.put("fightValue2", fightValue2);
		to.put("fightValue3", fightValue3);
		to.put("lv1", lv1);
		to.put("lv2", lv2);
		to.put("lv3", lv3);
		
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateTeamChallengeRecord", to);
		} catch (Exception e) {
			logger.error("ChallengeBattleDAO.updateTeamChallengeRecord error!!",e);
			result = false;
		}
		return result;
	}
	
	public static boolean getTeamChallengeRecord() {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		SqlSession session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB).openSession(
				ExecutorType.SIMPLE, true);
		
		try {
			conn = session.getConnection();
			psmt = conn.prepareStatement("select N_DUPLICATE_ID, N_FIRST_ROLE_ID1, N_FIRST_ROLE_ID2, N_FIRST_ROLE_ID3, N_FIRST_TIME, " +
					"N_QUICK_ROLE_ID1, N_QUICK_ROLE_ID2, N_QUICK_ROLE_ID3," +
					"N_FIRST_FIGHT_VALUE1, N_FIRST_FIGHT_VALUE2, N_FIRST_FIGHT_VALUE3," +
					"N_FIRST_LV1, N_FIRST_LV2, N_FIRST_LV3," +
					"N_QUICK_FIGHT_VALUE1, N_QUICK_FIGHT_VALUE2, N_QUICK_FIGHT_VALUE3," +
					"N_QUICK_LV1, N_QUICK_LV2, N_QUICK_LV3," +
					
					" N_QUICK_TIME from GAME_TEAM_CHALLENGE_BATTLE");
			
			rs = psmt.executeQuery();

			while (rs.next()){
				TeamChallengeRecordSub firstSub;
				TeamChallengeRecordSub quickSub;
				List<TeamChallengeRecordSub> firstList = new ArrayList<TeamChallengeRecordSub>();
				List<TeamChallengeRecordSub> quickList = new ArrayList<TeamChallengeRecordSub>();
				HeroInfo firstHeroInfoSub;
				RoleInfo firstRoleInfoSub;
				HeroInfo quickHeroInfoSub;
				RoleInfo quickRoleInfoSub;
				
				for(int i = 1; i <= 3; i++){
					firstRoleInfoSub = RoleInfoMap.getRoleInfo(rs.getInt("N_FIRST_ROLE_ID" + i));
				
					if(firstRoleInfoSub != null){
						firstSub = new TeamChallengeRecordSub();
						firstSub.setFirstTime(rs.getLong("N_FIRST_TIME"));
						firstHeroInfoSub = HeroInfoMap.getMainHeroInfo(firstRoleInfoSub);
						if(firstHeroInfoSub != null)
						{
							firstSub.setHeroId(firstHeroInfoSub.getHeroNo());
						}
						firstSub.setHeroLevel(rs.getInt("N_FIRST_LV" + i));
						firstSub.setFightValue(rs.getInt("N_FIRST_FIGHT_VALUE" + i));
						firstSub.setName(firstRoleInfoSub.getRoleName());
						firstSub.setRoleId(firstRoleInfoSub.getId());
						firstList.add(firstSub);
					}

					quickRoleInfoSub = RoleInfoMap.getRoleInfo(rs.getInt("N_QUICK_ROLE_ID" + i));

					if(quickRoleInfoSub != null){
						quickSub = new TeamChallengeRecordSub();
						quickSub.setQuickTime(rs.getLong("N_QUICK_TIME"));
						quickHeroInfoSub = HeroInfoMap.getMainHeroInfo(quickRoleInfoSub);
						if(quickHeroInfoSub != null)
						{
							quickSub.setHeroId(quickHeroInfoSub.getHeroNo());
						}
						quickSub.setHeroLevel(rs.getInt("N_QUICK_LV" + i));
						quickSub.setFightValue(rs.getInt("N_QUICK_FIGHT_VALUE" + i));
						quickSub.setName(quickRoleInfoSub.getRoleName());
						quickSub.setRoleId(quickRoleInfoSub.getId());
						quickList.add(quickSub);
					}
				}
				
				if(TeamChallengeRecord.getFirstKill() == null){
					Map<Integer, List<TeamChallengeRecordSub>> firstKill = new HashMap<Integer, List<TeamChallengeRecordSub>>();// 首殺
					Map<Integer, List<TeamChallengeRecordSub>> quickKill = new HashMap<Integer, List<TeamChallengeRecordSub>>();// 速殺
					firstKill.put(rs.getInt("N_DUPLICATE_ID"), firstList);
					quickKill.put(rs.getInt("N_DUPLICATE_ID"), quickList);
					TeamChallengeRecord.setFirstKill(firstKill);
					TeamChallengeRecord.setQuickKill(quickKill);
				} else {
					TeamChallengeRecord.getFirstKill().put(rs.getInt("N_DUPLICATE_ID"), firstList);
					TeamChallengeRecord.getQuickKill().put(rs.getInt("N_DUPLICATE_ID"), quickList);
				}
			}
			return true;
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
				if (conn != null) {
					conn.close();
				}
				session.close();
			} catch (SQLException e) {
				if(logger.isErrorEnabled()){
					logger.info(e.getMessage());
				}
			}
		}
	}
	
}
