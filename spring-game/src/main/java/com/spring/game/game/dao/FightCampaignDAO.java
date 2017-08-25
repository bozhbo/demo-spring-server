package com.snail.webgame.game.dao;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.FightCampaignBattle;
import com.snail.webgame.game.info.FightCampaignHero;
import com.snail.webgame.game.info.FightCampaignInfo;
import com.snail.webgame.game.info.HeroImageInfo;

public class FightCampaignDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private FightCampaignDAO() {
	};

	private static class InternalClass {
		public final static FightCampaignDAO instance = new FightCampaignDAO();
	}

	public static FightCampaignDAO getInstance() {
		return InternalClass.instance;
	}

//	/**
//	 * 获取所有记录
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean loadAllFightCampaignInfo() {
//		List<FightCampaignInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectCampaignAll");
//		if (list != null) {
//			for (FightCampaignInfo info : list) {
//				FightCampaignInfoMap.addFightCampaignInfo(info);
//			}
//		}
//		if (logger.isInfoEnabled()) {
//			logger.info("Load GAME_FIGHT_CAMPAIGN Table success!");
//		}
//		return true;
//	}

//	/**
//	 * 获取所有记录
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean loadAllFightCampaignHero() {
//		List<FightCampaignHero> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectCampaignHeroAll");
//		if (list != null) {
//			for (FightCampaignHero hero : list) {
//				FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(hero.getRoleId());
//				if (info != null) {
//					if (info.getHeroMap() == null) {
//						info.setHeroMap(new HashMap<Integer, FightCampaignHero>());
//					}
//					info.getHeroMap().put(hero.getHeroId(), hero);
//				}
//			}
//		}
//		if (logger.isInfoEnabled()) {
//			logger.info("Load GAME_FIGHT_CAMPAIGN_HERO Table success!");
//		}
//		return true;
//	}

//	/**
//	 * 获取所有记录
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean loadAllFightCampaignBattle() {
//		List<FightCampaignBattle> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectCampaignBattleAll");
//		if (list != null) {
//			for (FightCampaignBattle battle : list) {
//				FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(battle.getRoleId());
//				if (info != null) {
//					if (info.getBattleMap() == null) {
//						info.setBattleMap(new HashMap<Integer, FightCampaignBattle>());
//					}
//					info.getBattleMap().put(battle.getBattleNo(), battle);
//				}
//			}
//		}
//		if (logger.isInfoEnabled()) {
//			logger.info("Load GAME_FIGHT_CAMPAIGN_BATTLE Table success!");
//		}
//		return true;
//	}

//	/**
//	 * 加载角色宝物活动数据
//	 * @param roleId
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean loadFightCampaignbyRoleId(long roleId) {
//		SqlMapClient client = getSqlMapClient(DbConstants.GAME_DB,false);
//		FightCampaignInfo info = (FightCampaignInfo) client.query("selectCampaignbyRoleId", roleId);
//		if (info != null) {
//			if (info.getHeroMap() == null) {
//				info.setHeroMap(new HashMap<Integer, FightCampaignHero>());
//			}
//			List<FightCampaignHero> heroList = client.queryList("selectCampaignHerobyRoleId", roleId);
//			if (heroList != null) {
//				for (FightCampaignHero hero : heroList) {
//					info.getHeroMap().put(hero.getHeroId(), hero);
//				}
//			}
//
//			if (info.getBattleMap() == null) {
//				info.setBattleMap(new HashMap<Integer, FightCampaignBattle>());
//			}
//			List<FightCampaignBattle> battleList = client.queryList("selectCampaignBattlebyRoleId", roleId);
//			if (battleList != null) {
//				for (FightCampaignBattle battle : battleList) {
//					info.getBattleMap().put(battle.getBattleNo(), battle);
//				}
//			}
//			FightCampaignInfoMap.addFightCampaignInfo(info);
//			if (logger.isInfoEnabled()) {
//				logger.info("Load GAME_FIGHT_CAMPAIGN Table success! roleId=" + roleId);
//				logger.info("Load GAME_FIGHT_CAMPAIGN_HERO Table success! roleId=" + roleId);
//				logger.info("Load GAME_FIGHT_CAMPAIGN_BATTLE Table success! roleId=" + roleId);
//			}
//		}
//		client.commit();
//		return true;
//	}

	/**
	 * 插入信息
	 * @param info
	 * @return
	 */
	public boolean insertFightCampaignInfo(FightCampaignInfo info) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH,false);
			if(client!= null){
				client.insert("insertCampaign", info);
				
				Map<Integer, FightCampaignHero> heroMap = info.getHeroMap();
				if (heroMap != null) {
					for (FightCampaignHero hero : heroMap.values()) {
						client.insert("insertCampaignHero", hero);
					}
				}

				Map<Integer, FightCampaignBattle> battleMap = info.getBattleMap();
				if (heroMap != null) {
					for (FightCampaignBattle battle : battleMap.values()) {
						client.insert("insertCampaignBattle", battle);
					}
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("FightCampaignDAO.insertFightCampaignInfo error!!", e);
			}
			if(client!= null){
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 更新阵形
	 * @param insertOrUpdate
	 * @param delIds
	 * @return
	 */
	public boolean updateFightCampaignHero(Map<Integer, FightCampaignHero> insertOrUpdate, Map<Integer, Integer> delIds,
			Map<Integer, HeroImageInfo> imageChanges) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH,false);
			if(client!= null){
				if (insertOrUpdate != null) {
					for (FightCampaignHero hero : insertOrUpdate.values()) {
						if (hero.getId() > 0) {
							client.update("updateCampaignHero", hero);
						} else {
							client.insert("insertCampaignHero", hero);
						}
					}
				}
				if (delIds != null) {
					for (int id : delIds.values()) {
						FightCampaignHero to1 = new FightCampaignHero();
						to1.setId(id);
						client.delete("deleteCampaignHero", to1);
					}
				}
				if (imageChanges != null) {
					for (HeroImageInfo image : imageChanges.values()) {
						if (image.getId() > 0) {
							client.update("updateHeroImageDeploy", image);
						} else {
							client.insert("insertHeroImageInfo", image);
						}
					}
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("FightCampaignDAO.updateFightCampaignHero error!!", e);
			}
			if(client!= null){
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 更新重置
	 * @param campaignId
	 * @param resetNum
	 * @param lastResetTime
	 * @param lastFightBattleNo
	 * @param lastFightResult
	 * @param insertOrUpdateHeros
	 * @param delHeroIds
	 * @param insertOrUpdateBattles
	 * @param delBattleIds
	 * @return
	 */
	public boolean updateFightCampaignResetNum(int campaignId, int reviceNum, int resetNum, Timestamp lastResetTime,
			int lastFightBattleNo, int lastFightResult, int hisFightBattleNo, int hisFightResult,
			Map<Integer, FightCampaignHero> insertOrUpdateHeros, Map<Integer, Integer> delHeroIds, 
			Map<Integer, FightCampaignBattle> insertOrUpdateBattles, Map<Integer, Integer> delBattleIds,
			Map<Integer, Integer> delImageIds) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH, false);
			if(client!= null){
				FightCampaignInfo to = new FightCampaignInfo();
				to.setId(campaignId);
				to.setReviceNum(reviceNum);
				to.setResetNum(resetNum);
				to.setLastResetTime(lastResetTime);

				to.setLastFightBattleNo(lastFightBattleNo);
				to.setLastFightResult(lastFightResult);
				to.setHisFightBattleNo(hisFightBattleNo);
				to.setHisFightResult(hisFightResult);

				client.update("updateCampaignResetNum", to);
				if (insertOrUpdateHeros != null) {
					for (FightCampaignHero hero : insertOrUpdateHeros.values()) {
						if (hero.getId() > 0) {
							client.update("updateCampaignHero", hero);
						} else {
							client.insert("insertCampaignHero", hero);
						}
					}
				}
				if (delHeroIds != null) {
					for (int id : delHeroIds.values()) {
						FightCampaignHero to1 = new FightCampaignHero();
						to1.setId(id);
						client.delete("deleteCampaignHero", to1);
					}
				}

				if (insertOrUpdateBattles != null) {
					for (FightCampaignBattle battle : insertOrUpdateBattles.values()) {
						if (battle.getId() > 0) {
							client.update("updateCampaignBattle", battle);
						} else {
							client.insert("insertCampaignBattle", battle);
						}
					}
				}
				if (delBattleIds != null) {
					for (int id : delBattleIds.values()) {
						FightCampaignBattle to1 = new FightCampaignBattle();
						to1.setId(id);
						client.delete("deleteCampaignBattle", to1);
					}
				}
				if (delImageIds != null) {
					for (int id : delImageIds.values()) {
						client.delete("deleteHeroImageInfobyId",id);
					}
				}
				
				client.commit();
				return true;
			}else{
				return false;
			}	
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("FightCampaignDAO.updateFightCampaignResetNum error!!", e);
			}
			if(client!= null){
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 更新重置
	 * @param campaignId
	 * @param lastFightBattleNo
	 * @param lastFightResult
	 * @param insertOrUpdateHeros
	 * @param delHeroIds
	 * @param insertOrUpdateBattles
	 * @param delBattleIds
	 * @return
	 */
	public boolean updateFightCampaignResetNumbyGm(int campaignId,int reviceNum, int lastFightBattleNo, int lastFightResult,
			int hisFightBattleNo, int hisFightResult,
			Map<Integer, FightCampaignHero> insertOrUpdateHeros, Map<Integer, Integer> delHeroIds,
			Map<Integer, FightCampaignBattle> insertOrUpdateBattles, Map<Integer, Integer> delBattleIds,
			Map<Integer, Integer> delImageIds) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH, false);
			if(client!= null){
				FightCampaignInfo to = new FightCampaignInfo();
				to.setId(campaignId);
				to.setReviceNum(reviceNum);
				to.setLastFightBattleNo(lastFightBattleNo);
				to.setLastFightResult(lastFightResult);
				to.setHisFightBattleNo(hisFightBattleNo);
				to.setHisFightResult(hisFightResult);

				client.update("updateCampaignResetbyGm", to);
				
				if (insertOrUpdateHeros != null) {
					for (FightCampaignHero hero : insertOrUpdateHeros.values()) {
						if (hero.getId() > 0) {
							client.update("updateCampaignHero", hero);
						} else {
							client.insert("insertCampaignHero", hero);
						}
					}
				}
				if (delHeroIds != null) {
					for (int id : delHeroIds.values()) {
						FightCampaignHero to1 = new FightCampaignHero();
						to1.setId(id);
						client.delete("deleteCampaignHero", to1);
					}
				}

				if (insertOrUpdateBattles != null) {
					for (FightCampaignBattle battle : insertOrUpdateBattles.values()) {
						if (battle.getId() > 0) {
							client.update("updateCampaignBattle", battle);
						} else {
							client.insert("insertCampaignBattle", battle);
						}
					}
				}
				if (delBattleIds != null) {
					for (int id : delBattleIds.values()) {
						FightCampaignBattle to1 = new FightCampaignBattle();
						to1.setId(id);
						client.delete("deleteCampaignBattle", to1);
					}
				}
				if (delImageIds != null) {
					for (int id : delImageIds.values()) {
						client.delete("deleteHeroImageInfobyId", id);
					}
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("FightCampaignDAO.updateFightCampaignResetNum error!!", e);
			}
			if(client!= null){
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 更新扫荡
	 * @param campaignId
	 * @param reviceNum
	 * @param lastFightBattleNo
	 * @param lastFightResult
	 * @param insertOrUpdateHeros
	 * @param delHeroIds
	 * @return
	 */
	public boolean updateFightCampaignbySweep(int campaignId,int reviceNum, int lastFightBattleNo, int lastFightResult,
			Map<Integer, FightCampaignHero> insertOrUpdateHeros, Map<Integer, Integer> delHeroIds){
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH, false);
			if(client!= null){
				FightCampaignInfo to = new FightCampaignInfo();
				to.setId(campaignId);
				to.setReviceNum(reviceNum);
				to.setLastFightBattleNo(lastFightBattleNo);
				to.setLastFightResult(lastFightResult);
				client.update("updateCampaignbySweep", to);
				
				if (insertOrUpdateHeros != null) {
					for (FightCampaignHero hero : insertOrUpdateHeros.values()) {
						if (hero.getId() > 0) {
							client.update("updateCampaignHero", hero);
						} else {
							client.insert("insertCampaignHero", hero);
						}
					}
				}
				if (delHeroIds != null) {
					for (int id : delHeroIds.values()) {
						FightCampaignHero to1 = new FightCampaignHero();
						to1.setId(id);
						client.delete("deleteCampaignHero", to1);
					}
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("FightCampaignDAO.updateFightCampaignbySweep error!!", e);
			}
			if(client!= null){
				client.rollback();
			}
			return false;
		}
	}
	
	
	/**
	 * 更新战斗结果
	 * @param campaignId
	 * @param lastFightBattleNo
	 * @param lastFightResult
	 * @param insertOrUpdate
	 * @param delIds
	 * @return
	 */
	public boolean updateFightCampaignFightResult(int campaignId, int lastFightBattleNo, int lastFightResult,
			int campaignBattleId, Map<Byte, HeroRecord> fightDeployMap, Map<Integer, FightCampaignHero> insertOrUpdate,
			Map<Integer, Integer> delIds , Map<Integer, HeroImageInfo> imageUpdate) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if(client!= null){
				FightCampaignInfo to = new FightCampaignInfo();
				to.setId(campaignId);
				to.setLastFightBattleNo(lastFightBattleNo);
				to.setLastFightResult(lastFightResult);

				client.update("updateCampaignFightResult", to);

				FightCampaignBattle battle = new FightCampaignBattle();
				battle.setId(campaignBattleId);
				battle.setFightDeployMap(fightDeployMap);
				client.update("updateCampaignBattleDeploy", battle);

				if (insertOrUpdate != null) {
					for (FightCampaignHero hero : insertOrUpdate.values()) {
						if (hero.getId() > 0) {
							client.update("updateCampaignHero", hero);
						} else {
							client.insert("insertCampaignHero", hero);
						}
					}
				}
				if (delIds != null) {
					for (int id : delIds.values()) {
						FightCampaignHero to1 = new FightCampaignHero();
						to1.setId(id);
						client.delete("deleteCampaignHero", to1);
					}
				}
				if (imageUpdate != null) {
					for (HeroImageInfo hero : imageUpdate.values()) {
						if (hero.getId() > 0) {
							client.update("updateHeroImageHp", hero);
						} else {
							client.insert("insertHeroImageInfo", hero);
						}
					}
				}
				client.commit();
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("FightCampaignDAO.updateFightCampaignFightResult error!!", e);
			}
			if(client!= null){
				client.rollback();
			}
			return false;
		}
	}

	/**
	 * 领取奖励
	 * @param id
	 * @param isGetPrize
	 * @return
	 */
	public boolean updateCampaignBattlePrize(int id, byte isGetPrize) {
		FightCampaignBattle to = new FightCampaignBattle();
		to.setId(id);
		to.setIsGetPrize(isGetPrize);
		
		boolean result;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateCampaignBattlePrize", to);
		} catch (Exception e) {
			logger.error("FightCampaignDAO.updateCampaignBattlePrize error!!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新复活次数
	 * @param id
	 * @param isGetPrize
	 * @return
	 */
	public boolean updateCampaignReviceNum(int id, int reviceNum) {
		FightCampaignInfo to = new FightCampaignInfo();
		to.setId(id);
		to.setReviceNum(reviceNum);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateCampaignReviceNum", to);
	}
	
	/**
	 * 更新复活次数
	 * @param id
	 * @param isGetPrize
	 * @return
	 */
	public boolean updateCampaignHeroStatus(int id, byte heroStatus,int cutHp) {
		FightCampaignHero to = new FightCampaignHero();
		to.setId(id);
		to.setHeroStatus(heroStatus);
		to.setCutHp(cutHp);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateCampaignHeroStatus", to);
	}
	
	/**
	 * 更新战斗结果
	 * @param campaignId
	 * @param lastFightBattleNo
	 * @param lastFightResult
	 * @return
	 */
	public boolean updateFightCampaignFightResultbyGm(int campaignId, int lastFightBattleNo, int lastFightResult){
		FightCampaignInfo to = new FightCampaignInfo();
		to.setId(campaignId);
		to.setLastFightBattleNo(lastFightBattleNo);
		to.setLastFightResult(lastFightResult);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateCampaignFightResult", to);
	}
}
