package com.snail.webgame.game.dao;

import java.sql.Timestamp;
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
import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.ChallengeBattleInfoMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.FightCampaignInfoMap;
import com.snail.webgame.game.cache.FightGemInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.StoreItemInfoMap;
import com.snail.webgame.game.cache.WorldBossMap;
import com.snail.webgame.game.common.RoleLogoutInfo;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.FightCampaignBattle;
import com.snail.webgame.game.info.FightCampaignHero;
import com.snail.webgame.game.info.FightCampaignInfo;
import com.snail.webgame.game.info.FightGemInfo;
import com.snail.webgame.game.info.HeroImageInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.PresentEnergyInfo;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleBoxRecordInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.info.StoreItemInfo;
import com.snail.webgame.game.protocal.ride.service.RideService;
import com.snail.webgame.game.protocal.scene.info.RolePoint;
import com.snail.webgame.game.protocal.snatch.getRivalList.RivalListRe;

public class RoleDAO extends SqlMapDaoSupport {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleDAO() {
	}

	private static class InternalClass {
		public final static RoleDAO instance = new RoleDAO();
	}

	public static RoleDAO getInstance() {
		return InternalClass.instance;
	}
	
	/**
	 * 加载角色
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean loadAllRole() {
		List<RoleInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectAllRole");
		if (list != null && list.size() > 0) {
			for (RoleInfo info : list) {
				RoleInfoMap.addRoleInfo(info);
				WorldBossMap.addbossList(info,null,0,0,0);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_ROLE Table success!" + list.size());
			}
		}

		return true;
	}

	/**
	 * 角色上线加载相关信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean roleLoadOtherInfo(RoleInfo roleInfo,SqlMapClient client) {

		if (roleInfo.getRoleLoadInfo() != null) {
			return true;
		}
		if (client != null) {
			// 玩家上线加载角色其它信息
			RoleLoadInfo loadInfo = (RoleLoadInfo) client.query("selectRoleOtherInfo", roleInfo.getId());
			if (loadInfo != null) {
				roleInfo.setRoleLoadInfo(loadInfo);
				RolePoint rolePoint = roleInfo.getRolePoint();
				if (rolePoint == null) {
					
					//清除脏数据：
					String teamChallengeStr = loadInfo.getTeamChallengeStr();
					if(teamChallengeStr != null && teamChallengeStr.length() > 0)
					{
						String tempChallengeStr = "";
						
						String[] teamChallengeStrs = teamChallengeStr.split(",");
						for(int i = 0 ; i < teamChallengeStrs.length ; i++)
						{
							if(tempChallengeStr.indexOf(teamChallengeStrs[i]) == -1)
							{
								tempChallengeStr = tempChallengeStr+","+teamChallengeStrs[i];
							}
						}
						
						loadInfo.setTeamChallengeStr(tempChallengeStr);
					}
					
					rolePoint = new RolePoint();
					rolePoint.setRoleId(roleInfo.getId());
					rolePoint.setNo(loadInfo.getSceneNo());
					rolePoint.setPointX(loadInfo.getPointX());
					rolePoint.setPointY(loadInfo.getPointY());
					rolePoint.setPointZ(loadInfo.getPointZ());

					roleInfo.setRolePoint(rolePoint);
				}

				if (logger.isInfoEnabled()) {
					logger.info("role login Load GAME_ROLE Table success!");
				}
			} else {
				if (logger.isErrorEnabled()) {
					logger.error("Load GAME_ROLE Table failed! roleId=" + roleInfo.getId());
				}
				return false;
			}
			// 玩家上线加载所有背包物品
			List<BagItemInfo> bagItemlist = client.queryList("selectRoleBagItem", roleInfo.getId());
			if (bagItemlist != null) {
				for (BagItemInfo info : bagItemlist) {
					BagItemMap.addBagItem(roleInfo, info);
				}
				if (logger.isInfoEnabled()) {
					logger.info("role login Load GAME_BAG_ITEM Table success!");
				}
				bagItemlist.clear();
			}

			// 玩家上线加载所有武将
			List<HeroInfo> herolist = client.queryList("selectRoleHero", roleInfo.getId());
			if (herolist != null) {
				for (HeroInfo info : herolist) {
					HeroInfoMap.roleLoginAddHero(info);
				}
				if (logger.isInfoEnabled()) {
					logger.info("role login Load GAME_HERO Table success!");
				}

				herolist.clear();
			}

			// 角色上线加载角色所有装备
			List<Map<String, Object>> equipList = client.queryList("selectRoleEquipByRange", roleInfo.getId());
			if (equipList != null) {
				EquipInfoMap.addRequireEquipList(equipList);
				if (logger.isInfoEnabled()) {
					logger.info("role login Load GAME_ROLE_EQUIP Table success!");
				}
				equipList.clear();
			}

			// 上线加载商店物品
			List<StoreItemInfo> storeList = client.queryList("selectRoleStoreItem", roleInfo.getId());
			if (storeList != null) {
				for (StoreItemInfo info : storeList) {
					StoreItemInfoMap.addStoreItemInfo(loadInfo, info);
				}

				if (logger.isInfoEnabled()) {
					logger.info("role longin Load GAME_ROLE_STORE Table success!");
				}
				storeList.clear();
			}

			// 加载副本战斗信息
			List<ChallengeBattleInfo> challengeBattleList = client.queryList("selectChallengeBattleInfo",
					roleInfo.getId());
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			
			if (challengeBattleList != null) {
				for (ChallengeBattleInfo info : challengeBattleList) {
					ChallengeBattleInfoMap.addInfo(info);
					if (info.getStar() != null && info.getStar().length() > 0) {
						String[] star = info.getStar().trim().split(",");
						info.setStars(star.length);
						roleLoadInfo.addBattle(info.getBattleId());
					}
				}
				if (logger.isInfoEnabled()) {
					logger.info("Load GAME_CHALLENGE_BATTLE Table success!");
				}
				challengeBattleList.clear();
			}

//			// 加载角色任务
//			List<QuestInProgressInfo> list = getSqlMapClient(DbConstants.GAME_DB).queryList("selectQuestbyRoleId", roleInfo.getId());
//			if (list != null && roleInfo.getRoleLoadInfo() != null) {
//				QuestInfoMap questInfoMap = new QuestInfoMap();
//				roleInfo.getRoleLoadInfo().setQuestInfoMap(questInfoMap);
//				for (QuestInProgressInfo info : list) {
//					questInfoMap.addQuestInProgressInfo(info);
//				}
//				if (logger.isInfoEnabled()) {
//					logger.info("Load GAME_QUEST_INFO Table success!");
//				}
//			}
			
			List<RoleBoxRecordInfo> roleBoxList = client.queryList("selectRoleBoxByRoleId", roleInfo.getId());
			if (roleBoxList != null) {
				Map<Integer, RoleBoxRecordInfo> map = new HashMap<Integer, RoleBoxRecordInfo>();
				roleInfo.setRoleBoxMap(map);
				for (RoleBoxRecordInfo info : roleBoxList) {
					map.put(info.getBoxId(), info);
				}
				if (logger.isInfoEnabled()) {
					logger.info("Load GAME_ROLE_BOX_RECORD Table success!");
				}
			}

			// 加载角色宝石活动数据
			FightGemInfo gemInfo = (FightGemInfo) client.query("selectGembyRoleId", roleInfo.getId());
			if (gemInfo != null) {
				FightGemInfoMap.addFightGemInfo(gemInfo);
				if (logger.isInfoEnabled()) {
					logger.info("Load GAME_FIGHT_GEM Table success! roleId=" + roleInfo.getId());
				}
			}

			// 加载角色宝物活动数据
			FightCampaignInfo info = (FightCampaignInfo) client.query("selectCampaignbyRoleId", roleInfo.getId());
			if (info != null) {
				if (info.getHeroMap() == null) {
					info.setHeroMap(new HashMap<Integer, FightCampaignHero>());
				}
				List<FightCampaignHero> heroList = client.queryList("selectCampaignHerobyRoleId", roleInfo.getId());
				if (heroList != null) {
					for (FightCampaignHero hero : heroList) {
						info.getHeroMap().put(hero.getHeroId(), hero);
					}
				}

				if (info.getBattleMap() == null) {
					info.setBattleMap(new HashMap<Integer, FightCampaignBattle>());
				}
				List<FightCampaignBattle> battleList = client.queryList("selectCampaignBattlebyRoleId",
						roleInfo.getId());
				if (battleList != null) {
					for (FightCampaignBattle battle : battleList) {
						info.getBattleMap().put(battle.getBattleNo(), battle);
					}
				}
				FightCampaignInfoMap.addFightCampaignInfo(info);
				if (logger.isInfoEnabled()) {
					logger.info("Load GAME_FIGHT_CAMPAIGN Table success! roleId=" + roleInfo.getId());
					logger.info("Load GAME_FIGHT_CAMPAIGN_HERO Table success! roleId=" + roleInfo.getId());
					logger.info("Load GAME_FIGHT_CAMPAIGN_BATTLE Table success! roleId=" + roleInfo.getId());
				}
			}
			
			// 加载坐骑信息
			List<RideInfo> rideList = client.queryList("selectRidebyRoleId", roleInfo.getId());
			if (rideList != null) {
				for (RideInfo rideInfo : rideList) {
					roleLoadInfo.getRoleRideMap().put(rideInfo.getId(), rideInfo);
					RideService.recalRideFightVal(rideInfo);
				}

				if (logger.isInfoEnabled()) {
					logger.info("role longin Load GAME_RIDE_INFO Table success!");
				}
				rideList.clear();
			}
			
			//加载好友列表
//			List<RoleRelationInfo> relationList = getSqlMapClient(DbConstants.GAME_DB).queryList("selectRoleRelationInfoById", roleInfo.getId());
//			if(relationList != null && list.size() > 0){
//				if(roleInfo.getRoleLoadInfo() == null) return false;
//				for(RoleRelationInfo rInfo : relationList){
//					if(rInfo.getStatus() == 0){
//						//不是好友 是好友请求
//						roleInfo.getRoleLoadInfo().getFriendRequestIdSet().add(rInfo.getFriendId());
//					}else if(rInfo.getStatus() == 1){
//						//好友
//						roleInfo.getRoleLoadInfo().getFriendIdSet().add(rInfo.getFriendId());
//						
//					}else if(rInfo.getStatus() == 2){
//						//黑名单
//						roleInfo.getRoleLoadInfo().getBlackFriendIdSet().add(rInfo.getFriendId());
//						
//					}
//				}
//				
//				if (logger.isInfoEnabled()) {
//					logger.info("Load GAME_ROLE_RELATION_INFO Table success! roleId=" + roleInfo.getId());
//				}
//			}
			
			//清理脏数据
			PresentEnergyDao.getInstence().cleanupPresentEnergyByRoleId(roleInfo.getId(),client);
			
			//加载可获得的好友赠送精力
			List<PresentEnergyInfo> presentList = client.queryList("selectPresentEnergyById", roleInfo.getId());
			if(presentList != null && presentList.size() > 0){
				for(PresentEnergyInfo pInfo : presentList){
					if(pInfo.getRoleId() == roleInfo.getId())
					{
						//可领取赠送精力
						roleLoadInfo.getPresentEnergyMap().put(pInfo.getId(), pInfo);
					}
					else if(pInfo.getRelRoleId() == roleInfo.getId())
					{
						//记录自己的精力赠送记录
						long time = 0;
						if(roleLoadInfo.getRecordPresentTimeMap().containsKey(pInfo.getRoleId()))
						{
							time = roleLoadInfo.getRecordPresentTimeMap().get(pInfo.getRoleId());
						}
						
						if(time == 0 || pInfo.getPresentDate().getTime() > time)
						{
							roleLoadInfo.getRecordPresentTimeMap().put(pInfo.getRoleId(), pInfo.getPresentDate().getTime());
						}
					}
				}
				
				if (logger.isInfoEnabled()) {
					logger.info("Load GAME_PRESENT_ENERGY_INFO Table success! roleId=" + roleInfo.getId());
				}
			}
			
			//从公会的全局缓存加载好友公会信息(申请的可以多条，加入的只有一条)
			if(roleInfo.getClubId() <= 0){
				//说明角色没有加入的公会 
				//查找申请过的公会
				roleLoadInfo.setRoleClubMemberInfoSet(RoleClubMemberInfoMap.getRoleRequestClubIdSet(roleInfo.getId()));
			}
			
			List<HeroImageInfo> heroImageInfoList = client.queryList("selectAllHeroImageInfoByRoleId", roleInfo.getId());
			if(heroImageInfoList != null && heroImageInfoList.size() >0){
				for(HeroImageInfo heroImageInfo : heroImageInfoList){
					if(heroImageInfo == null){
						continue;
					}
					
					if(roleLoadInfo != null){
						roleLoadInfo.addHeroImageInfo(heroImageInfo);
					}
				}
			}
			
			
		} else {
			return false;
		}

		return true;
	}

	/**
	 * 添加角色信息
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
	public boolean insertRoleInfo(RoleInfo roleInfo, HeroInfo heroInfo, List<BagItemInfo> addBagItem,
			List<EquipInfo> addBagEquips) {
		SqlSession session = null;
		try {
			session = SqlMapClientFactory.getSessionFactory(DbConstants.GAME_DB)
					.openSession(ExecutorType.SIMPLE, false);
			
			if(session != null){
				if (session.insert("insertRole", roleInfo) != 1) {
					throw new Exception("insertRole");
				}

				heroInfo.setRoleId(roleInfo.getId());
				if (session.insert("insertHero", heroInfo) != 1) {
					throw new Exception();
				}

				Map<String, Object> to = new HashMap<String, Object>();
				to.put("roleId", roleInfo.getId());
				to.put("heroId", 0);
				for (EquipInfo equipInfo : addBagEquips) {
					to.put("equipNo", equipInfo.getEquipNo());
					to.put("equipType", equipInfo.getEquipType());
					to.put("level", equipInfo.getLevel());
					if (session.insert("insertEquipInfo", to) != 1) {
						throw new Exception();
					}
					equipInfo.setId(((Long) to.get("id")).intValue());
				}

				for (BagItemInfo info : addBagItem) {
					info.setRoleId(roleInfo.getId());
					if (session.insert("insertBagItem", info) != 1) {
						throw new Exception();
					}
				}

				session.commit();
				session.close();
				return true;
			}else{
				return false;
			}
			
		} catch (Exception e) {
			if(session != null){
				session.rollback();
				session.close();
			}
			if (logger.isErrorEnabled()) {
				logger.error("insertRoleInfo error!", e);
			}
			return false;
		}
	}

	/**
	 * 角色登录状态修改
	 * @param roleId
	 * @param loginTime
	 * @param ip
	 */
	public void updateRoleLogin(int roleId,String loginIp, String mac, String packageName, int clientType) {

		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setId(roleId);
		roleInfo.setLoginIp(loginIp);
		roleInfo.setClientType(clientType);
		roleInfo.setMac(mac);
		roleInfo.setPackageName(packageName);
		roleInfo.setLoginTime(new Timestamp(System.currentTimeMillis()));

		try {
			getSqlMapClient(DbConstants.GAME_DB).update("updateRoleLogin", roleInfo);
		} catch (Exception e) {
			logger.error("updateRoleLogin error!",e);
		}
	}

	/**
	 * 更新角色资源信息
	 * @param action
	 * @param roleId
	 * @param type
	 * @param value
	 * @param time
	 * @return
	 */
	public boolean updateRoleResource(int action, long roleId, ConditionType type, long value, Timestamp time,String param1) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		switch (type) {
		case TYPE_MONEY:
		case TYPE_COURAGE:
		case TYPE_JUSTICE:
//		case TYPE_DEVOTE:
		case TYPE_KUAFU_MONEY:
		case TYPE_TEAM_MONEY:
		case TYPE_HIS_EXPLOIT:
		case TYPE_EQUIP:
		case TYPE_PVP_3_MONEY:
		case TYPE_STAR_MONEY:
			to.put(type.getName(), value);
			break;
		case TYPE_COIN:
			to.put(type.getName(), value);
			if (param1 != null && !"".equals(param1)) {
				to.put(ConditionType.TYPE_TOTAL_COIN.getName(), Long.valueOf(param1));
			}
			break;
		case TYPE_EXPLOIT:
			to.put(type.getName(), value);
			if(param1 != null && !"".equals(param1))
			{
				to.put(ConditionType.TYPE_HIS_EXPLOIT.getName(), Long.valueOf(param1));
			}
			break;
		case TYPE_SP:
			to.put(type.getName(), value);
			if (time != null) {
				to.put("lastRecoverSPTime", time);
			}
			break;
		case TYPE_ENERGY:
			to.put(type.getName(), value);
			if (time != null) {
				to.put("lastRecoverEnergyTime", time);
			}
			break;
		case TYPE_TECH:
			to.put(type.getName(), value);
			if (time != null) {
				to.put("lastRecoverTechTime", time);
			}
			break;
		case TYPE_CLUB_CONTRIBUTION:
			to.put(type.getName(), value);
			if (param1 != null && !"".equals(param1)) {
				try{
					to.put(ConditionType.TYPE_CLUB_CONTRIBUTION_SUM.getName(), Long.valueOf(param1));
				}catch (Exception e) {
					logger.error("", e);
				}
			}
			break;
		default:
			logger.error("there are no resoure update");
			return false;
		}

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleReource", to);
		} catch (Exception e) {
			logger.error("updateRoleResource error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新今日抽卡次数
	 * @param roleId
	 * @param todayPlunderNum
	 * @return
	 */
	public boolean updateRoleRecruitMoney(int roleId, byte recruitNum, Timestamp lastRecruitTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("recruitMoneyNum", recruitNum);
		to.put("lastRecruitMoneyTime", lastRecruitTime);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleRecruitMoney", to);
		} catch (Exception e) {
			logger.error("updateRoleRecruitMoney error!",e);
			result = false;
		}
		return result;
	}
	/**
	 * 更新十连抽抽卡状态
	 * @param roleId
	 * @param tenRecruitMoneyStats 银子十连抽次数
	 * @param tenRecruitCoinStats 金子十连抽次数
	 * @param oneRecruitCoinOpTimes 金子十连抽次数
	 * @return
	 */
	public boolean updateTenRecruit(int roleId, int tenRecruitMoneyStats, int tenRecruitCoinStats) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("tenRecruitMoneyStats", tenRecruitMoneyStats);
		to.put("tenRecruitCoinStatus", tenRecruitCoinStats);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateTenRecruitMoneyStatus", to);
		} catch (Exception e) {
			logger.error("updateTenRecruit error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新今日抽卡次数
	 * @param roleId
	 * @param todayPlunderNum
	 * @return
	 */
	public boolean updateRoleRecruitCoin(int roleId, Timestamp lastRecruitTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("lastRecruitCoinTime", lastRecruitTime);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleRecruitCoin", to);
		} catch (Exception e) {
			logger.error("updateRoleRecruitCoin error!",e);
			result = false;
		}
		return result;
	}

	 /**
     * 玩家下线，更新下线时间
     * @param outList
     * @param isShoutDown
     */
	public void updateLoginOut(List<RoleLogoutInfo> outList)
	{
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH,false);
			if(client != null){
				for(RoleLogoutInfo info : outList)
				{
					client.update("updateLoginOutTime", info);
				}
				client.commit();
			}
		} catch (Exception e) {
			if(client != null){
				client.rollback();
			}
			logger.error("updateLoginOut error!",e);
		}
	}

	/**
	 * 更新上次恢复SP时间
	 * @param roleId
	 * @param todayPlunderNum
	 * @return
	 */
	public boolean updateRoleLastRecSpTime(int roleId, long lastRecoverSpTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("lastRecoverSPTime", new Timestamp(lastRecoverSpTime));
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRecoveSP", to);
		} catch (Exception e) {
			logger.error("updateRoleLastRecSpTime error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新上次恢复精力时间
	 * @param roleId
	 * @param todayPlunderNum
	 * @return
	 */
	public boolean updateRoleLastRecEnergyTime(int roleId, long lastRecoverEnergyTime) {
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setId(roleId);
		roleInfo.setLastRecoverEnergyTime(new Timestamp(lastRecoverEnergyTime));
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRecoveEnergy", roleInfo);
		} catch (Exception e) {
			logger.error("updateRoleLastRecEnergyTime error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新上次技能点变化时间
	 * @param roleId
	 * @param lastRecoverTechTime
	 * @return
	 */
	public boolean updateRoleLastRecTechTime(int roleId, long lastRecoverTechTime) {
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setId(roleId);
		roleInfo.setLastRecoverTechTime(new Timestamp(lastRecoverTechTime));
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRecoveTech", roleInfo);
	}

	/**
	 * 修改角色GM权限
	 * @param roleId
	 * @param gmRight
	 * @return
	 */
	public boolean updateRoleGMRight(long roleId, int gmRight) {
		SqlMapClient client = getSqlMapClient(DbConstants.GAME_DB);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", roleId);
		param.put("gmRight", gmRight);
		
		boolean result = false;
		try {
			if(client != null){
				result = client.update("updateRoleGMRight", param);
			}
		} catch (Exception e) {
			logger.error("updateRoleGMRight error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 修改角色签到信息
	 * @param roleLoadInfo
	 * @return
	 */
	public boolean updateRoleCheckIn(RoleLoadInfo roleLoadInfo) {

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleCheckIn", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateRoleCheckIn error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 修改角色7日签到信息
	 * @param roleId
	 * @param days
	 * @param maxDays
	 * @param currentDays
	 * @param timeMilles
	 * @return
	 */
	public boolean role7CheckIn(int roleId, String days, byte maxDays, byte currentDays, long timeMilles) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("checkIn7DayNum",days);
		to.put("checkIn7DayMaxLoginDays",maxDays);
		to.put("checkIn7DayCurrentLoginDays",currentDays);
		to.put("checkIn7DayTime",new Timestamp(timeMilles));
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRole7CheckIn", to);
		} catch (Exception e) {
			logger.error("role7CheckIn error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新角色禁言信息
	 * @param roleId
	 * @param punishStatus
	 * @param punishTime
	 * @return
	 */
	public boolean updateRolePublish(int roleId, byte punishStatus, long punishTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("punishStatus",punishStatus);
		to.put("punishTime",new Timestamp(punishTime));

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRolePublish", to);
		} catch (Exception e) {
			logger.error("updateRolePublish error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新角色状态信息
	 * @param roleId
	 * @param roleStatus
	 * @param roleStatusTime
	 * @return
	 */
	public boolean updateRoleStatus(int roleId, int roleStatus, long roleStatusTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("roleStatus",(byte) roleStatus);
		to.put("roleStatusTime",new Timestamp(roleStatusTime));
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleStatus", to);
		} catch (Exception e) {
			logger.error("updateRoleStatus error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新商店最后更新时间
	 * @param roleId
	 * @param shopType
	 * @param RefreshNum
	 * @param lastRefreshTime
	 * @return
	 */
	public boolean updateLastRefreshTime(long roleId, int storeType, int refreshNum, Timestamp lastRefreshTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		switch (storeType) {
		case StoreItemInfo.STORE_TYPE_1:
			// 竞技场商店
			to.put("lastCourageTime", lastRefreshTime);
			break;
		case StoreItemInfo.STORE_TYPE_2:
			// 征战四方商店
			to.put("lastJusticeTime", lastRefreshTime);
			break;
		case StoreItemInfo.STORE_TYPE_3:
			// 工会商店
			to.put("lastDevoteTime", lastRefreshTime);
			break;
		case StoreItemInfo.STORE_TYPE_4:
			// 普通商店
			to.put("lastNormalTime", lastRefreshTime);
			to.put("autoRefreNum", refreshNum);
			break;
		case StoreItemInfo.STORE_TYPE_5:
			// 跨服商店
			to.put("kuafuAutoTime", lastRefreshTime);
			break;
		case StoreItemInfo.STORE_TYPE_6:
			// 战功商店
			to.put("exploitAutoTime", lastRefreshTime);
			break;
		case StoreItemInfo.STORE_TYPE_7:
			// 黑市商店
			to.put("goldShopAutoTime", lastRefreshTime);
			break;
		case StoreItemInfo.STORE_TYPE_8:
			// 装备商店
			to.put("equipShopAutoTime", lastRefreshTime);
			to.put("autoRefreEquipShopNum", refreshNum);
			break;
		case StoreItemInfo.STORE_TYPE_9:
			// 异域商店
			to.put("turkShopAutoTime", lastRefreshTime);
			break;
		case StoreItemInfo.STORE_TYPE_10:
			// 组队PVP商店
			to.put("pvp3ShopAutoTime", lastRefreshTime);
			to.put("pvp3ShopAutoNum", refreshNum);
			break;
		case StoreItemInfo.STORE_TYPE_11:
			// 星石商店
			to.put("starShopAutoTime", lastRefreshTime);
			to.put("autoRefreStarShopNum", refreshNum);
			break;
		case StoreItemInfo.STORE_TYPE_12:
			// 组队副本
			to.put("teamShopAutoTime", lastRefreshTime);
			to.put("teamShopAutoNum", refreshNum);
			break;
		default:
			logger.error("there are no storeType update");
			return false;
		}
		
		boolean result = false;
		try {
			result = getSqlMapClient("GAME_DB").update("updateLastRefreshTime", to);
		} catch (Exception e) {
			logger.error("updateLastRefreshTime error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新购买次数
	 * @param roleId
	 * @param resource_type
	 * @param buyNum
	 * @param buyTime
	 * @return
	 */
	public boolean updateRoleResourceBuyNum(long roleId, ConditionType resource_type, int buyNum, Timestamp buyTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		switch (resource_type) {
		case TYPE_MONEY:
			to.put("buyMoneyNum", buyNum);
			to.put("lastBuyMoneyTime", buyTime);
			break;
		case TYPE_SP:
			to.put("buySpNum", buyNum);
			to.put("lastBuySpTime", buyTime);
			break;
		case TYPE_COURAGE:
			to.put("buyCourageNum", buyNum);
			to.put("lastBuyCourageTime", buyTime);
			break;
		case TYPE_JUSTICE:
			to.put("buyJusticeNum", buyNum);
			to.put("lastBuyJusticeTime", buyTime);
			break;
//		case TYPE_DEVOTE:
//			to.put("buyDevoteNum", buyNum);
//			to.put("lastBuyDevoteTime", buyTime);
//			break;
		case TYPE_NORMAL_SHOP_FRE:
			to.put("buyNormalNum", buyNum);
			to.put("lastBuyNormalTime", buyTime);
			break;
		case TYPE_KUAFU_SHOP_FRE:
			to.put("buyKuafuNum", buyNum);
			to.put("lastBuyKuafuTime", buyTime);
			break;
		case TYPE_EXPLOIT_SHOP_FRE:
			to.put("buyExploitNum", buyNum);
			to.put("lastBuyExploitTime", buyTime);
			break;
		case TYPE_GOLD_SHOP_REF:
			to.put("buyGoldShopNum", buyNum);
			to.put("lastBuyGoldShopTime", buyTime);
			break;
		case TYPE_ENERGY:
			to.put("buyEnergyNum", buyNum);
			to.put("lastBuyEnergyTime", buyTime);
			break;
		case TYPE_EQUIP:
			to.put("buyEquipShopNum", buyNum);
			to.put("lastBuyEquipTime", buyTime);
			break;
		case TYPE_TECH:
			to.put("buyTechNum", buyNum);
			to.put("lastBuyTechTime", buyTime);
			break;
		case TYPE_PVP_3_SHOP_REF:
			to.put("buyPvp3ShopNum", buyNum);
			to.put("lastBuyPvp3ShopTime", buyTime);
			break;
		case TYPE_TEAM_SHOP_FRE:
			to.put("teamShopBuyNum", buyNum);
			to.put("teamShopLastBuyTime", buyTime);
			break;
		case TYPE_TURK_SHOP_REF:
			to.put("buyTurkShopNum", buyNum);
			to.put("lastBuyTurkShopTime", buyTime);
			break;
		case TYPE_CLUB_CONTRIBUTION:
			to.put("buyDevoteNum", buyNum);
			to.put("lastBuyDevoteTime", buyTime);
			break;
		case TYPE_STAR_MONEY:
			to.put("buyStarShopNum", buyNum);
			to.put("lastBuyStarTime", buyTime);
			break;
		default:
			logger.error("there are no resoure buy num update");
			return false;
		}

		boolean result = false;
		try {
			result = getSqlMapClient("GAME_DB").update("updateRoleResourceBuyNum", to);
		} catch (Exception e) {
			logger.error("updateRoleResourceBuyNum error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 修改 角色引导信息
	 * @param roleId
	 * @param guideNo
	 * @return
	 */
	public boolean updateGuideData(int roleId, String guideInfo) {
		// 最大节点长度
		String[] guides = guideInfo.split(",");
		if (guides.length > UserGuideNode.MAX_NODE + 1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i <= UserGuideNode.MAX_NODE; i++) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(guides[i]);
			}
			guideInfo = sb.toString();
		}
		
		RoleLoadInfo roleLoadInfo = new RoleLoadInfo();
		roleLoadInfo.setId(roleId);
		roleLoadInfo.setGuideInfo(guideInfo);
		boolean result = false;
		try {
			result =getSqlMapClient(DbConstants.GAME_DB).update("updateGuideData", roleLoadInfo);
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.info("updateGuideData error!",e);
			}
		}
		return result;
	}

	/**
	 * 更新玩家场景中坐标
	 * @param rolePoint
	 * @return
	 */
	public boolean updateSceneAIPoint(RolePoint rolePoint) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateSceneAIPoint", rolePoint);
		} catch (Exception e) {
			logger.error("updateSceneAIPoint error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新角色名
	 * @param roleId
	 * @param roleName
	 * @param times
	 * @return
	 */
	public boolean updateRoleName(int roleId, String roleName, byte times) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("roleName",roleName);
		to.put("changeRoleNameTimes",times);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleName", to);
		} catch (Exception e) {
			logger.error("updateRoleName error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 同布异步竞技场排行榜名字
	 * @param arenaId
	 * @param roleName
	 * @return
	 */
	public boolean updateArenaRoleName(int arenaId, String roleName) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", arenaId);
		to.put("roleName",roleName);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateArenaRoleName", to);
		} catch (Exception e) {
			logger.error("updateArenaRoleName error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新角色功能开启字符串
	 * @param roleId
	 * @param newFuncOpenStr
	 * @return
	 */
	public boolean updateRoleFuncOpenStr(int roleId, String newFuncOpenStr,SqlMapClient client) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("funcOpenStr",newFuncOpenStr);
		boolean result = false;
		try {
			if(client == null)
			{
				client = getSqlMapClient(DbConstants.GAME_DB);
			}
			result =client.update("updateRoleFuncOpenStr", to);
		} catch (Exception e) {
			logger.error("updateRoleFuncOpenStr error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新等级礼包字符串
	 * 
	 * @param roleId
	 * @param newLevelGiftNoStr
	 * @return
	 */
	public boolean updateLevelGiftNo(int roleId, String newLevelGiftNoStr) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("drawLevelGiftStr",newLevelGiftNoStr);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateLevelGiftNo", to);
		} catch (Exception e) {
			logger.error("updateLevelGiftNo error!",e);
			result = false;
		}
		return result;
	}
	/**
	 * 更新角色信息（极效专用）
	 * @param params
	 * @return
	 */
	public boolean updateRoleInfoForTool(int roleId, String roleName, byte roleRace, long money, long totalCoin, long coin, short sp,
			long courage, long justice, int kuafuMoney, int exploit, int rankShow, int isAdvert, int equip, int starMoney) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		to.put("roleName",roleName);
		to.put("roleRace",roleRace);
		to.put("money",money);
		to.put("totalCoin",totalCoin);
		to.put("coin",coin);
		to.put("sp",sp);
		to.put("courage",courage);
		to.put("justice",justice);
		to.put("kuafuMoney",kuafuMoney);
		to.put("exploit",exploit);
		to.put("rankShow", rankShow);
		to.put("isAdvert", isAdvert);
		to.put("equip", equip);
		to.put("starMoney", starMoney);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleInfoForTool", to);
		} catch (Exception e) {
			logger.error("updateRoleInfoForTool error!",e);
			result = false;
		}
		return result;
	}
	/**
	 * 更新士兵信息
	 * @param roleId
	 * @param 
	 * @return
	 */
	public boolean updateSolderInfo(int roleId, String soldierInfo) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("soldierInfo", soldierInfo);
		to.put("id",roleId);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateSolderInfo", to);
		} catch (Exception e) {
			logger.error("updateSolderInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新副本领奖信息 - 普通
	 * @param roleId
	 * @param 
	 * @return
	 */
	public boolean updateChallengePrize(int roleId, String prize) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("challengePrize", prize);
		to.put("id",roleId);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateChallengePrize", to);
		} catch (Exception e) {
			logger.error("updateChallengePrize error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新副本领奖信息 - 精英
	 * @param roleId
	 * @param 
	 * @return
	 */
	public boolean updateChallengePrize2(int roleId, String prize) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("challengePrize2", prize);
		to.put("id",roleId);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateChallengePrize2", to);
		} catch (Exception e) {
			logger.error("updateChallengePrize2 error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新副本开启信息
	 * @param roleId
	 * @param 
	 * @return
	 */
	public boolean updateChallengeOpen(int roleId, int open) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("challengeOpen", open);
		to.put("id",roleId);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateChallengeOpen", to);
		} catch (Exception e) {
			logger.error("updateChallengeOpen error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新安全模式结束时间
	 * @param roleId
	 * @param endTimeMillis
	 * @return
	 */
	public boolean updateSafeModeEndTime(int roleId, long endTimeMillis){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("safeModeEndTime", endTimeMillis);
		to.put("id",roleId);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateSafeModeEndTime", to);
		} catch (Exception e) {
			logger.error("updateSafeModeEndTime error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 获取已有特定道具的用户
	 * @param storeNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RivalListRe> getRivalList(String account, int stoneNo, int minLevel, int maxLevel){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("account", account);
		to.put("stoneNo", stoneNo);
		to.put("minLevel", minLevel);
		to.put("maxLevel", maxLevel);
		to.put("safeModeEndTime", System.currentTimeMillis());

		return getSqlMapClient(DbConstants.GAME_DB).queryList("getRivalList", to);
	}
	
	/**
	 * 获取单个物品信息
	 * @param stoneNo
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BagItemInfo getBagItemInfo(int stoneNo, int roleId){
		List<BagItemInfo> bagItemlist = getSqlMapClient(DbConstants.GAME_DB).queryList("selectRoleBagItem", roleId);
		if (bagItemlist != null) {
			for (BagItemInfo info : bagItemlist) {
				if(info.getItemNo() == stoneNo){
					return info;
				}
			}
		}
		return null;
	}
	
	/**
	 * 更新夺宝标志
	 * @param roleId
	 * @param snatchFlag
	 * @return
	 */
	public boolean updateSnatchFlag(int roleId, byte snatchFlag){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("snatchFlag", snatchFlag);
		to.put("id",roleId);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateSnatchFlag", to);
		} catch (Exception e) {
			logger.error("updateSnatchFlag error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色防守玩法信息
	 * @param roleId
	 * @param defendTime
	 * @param lastDefendTime
	 * @return
	 */
	public boolean updateRoleDefendTime(int roleId,byte defendTime,Timestamp lastDefendTime){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("defendTime", defendTime);
		to.put("id",roleId);
		to.put("lastDefendTime", lastDefendTime);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleDefendTime", to);
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("updateRoleDefendTime error!",e);
			}
		}
		return result;
	}
	
	/**
	 * 更新角色对攻战玩法信息
	 * @param roleId
	 * @param attackAnotherTime
	 * @param lastAttackAnotherTime
	 * @return
	 */
	public boolean updateRoleAttackAnotherInfo(int roleId, byte attackAnotherTime, Timestamp lastAttackAnotherTime){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("attackAnotherTime", attackAnotherTime);
		to.put("id",roleId);
		to.put("lastAttackAnotherTime", lastAttackAnotherTime);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleAttackAnotherInfo", to);
		} catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.error("updateRoleAttackAnotherInfo error!",e);
			}
		}
		return result;
	}
	
	/**
	 * 通过角色账号查询该账号的所有角色
	 * @param account
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RoleInfo> queryRoleListByAccount(String account){
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("account", account);
		return getSqlMapClient(DbConstants.GAME_DB).queryList("queryRoleListByAccount", map);
	}
	/**
	 * 更新士兵信息
	 * @param roleId
	 * @param 
	 * @return
	 */
	public boolean updateSolderUpCounterInfo(int roleId, String solderUpCounterInfo) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("soldierUpCounterInfo", solderUpCounterInfo);
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateSolderUpCounterInfo", to);
		} catch (Exception e) {
			logger.error("updateSolderUpCounterInfo error!",e);
			result = false;
		}
		return result;
	}
		
	/**
	 * 更新布阵位置开启
	 * @param roleId
	 * @param deployPosOpen
	 * @return
	 */
	public boolean updateDeployPosOpen(int roleId,List<Integer> deployPosOpen){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("deployPosOpen", deployPosOpen);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateDeployPosOpen", to);
	}
	
	/**
	 * 批处理重置每日获取精力的次数
	 * @return
	 */
	public boolean updateGetPresentEnergyTimes(int roleId, long time){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("getPresentEnergyTimes", (byte)0);
		to.put("queryEnergyInfoTime", new Timestamp(time));
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateGetPresentEnergyTimes", to);
			
			if(result){
				if(logger.isInfoEnabled()){
					logger.info("updateGetPresentEnergyTimes success");
				}
				
			}else{
				if(logger.isInfoEnabled()){
					logger.info("updateGetPresentEnergyTimes failure");
				}
			}
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("updateGetPresentEnergyTimes error!", e);
			}
		
		}
		
		return result;
	}
	
	/**
	 * 更新角色领取赠送精力次数
	 * @return
	 */
	public boolean updateGetPresentEnergyTimesById(int roleId, int times){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("getPresentEnergyTimes", (byte)times);
		try {
			getSqlMapClient(DbConstants.GAME_DB).update("updateGetPresentEnergyTimesById", to);
			return true;
		} catch (Exception e) {
			
			logger.error("updateGetPresentEnergyTimesById error!",e);
			return false;
		}
		
	}
		
	/**
	 * 更新抢夺次数信息
	 * @param roleId
	 * @param snatchTimes
	 * @return
	 */
	public boolean updateSnatchTimes(int roleId,Map<Integer, Integer> snatchTimes,int energy,Timestamp lastRecoverTime){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("snatchTimes", snatchTimes);
		to.put("energy", energy);
		to.put("lastRecoverTime", lastRecoverTime);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateSnatchTimes", to);
	}
	
	/**
	 * 更新角色对攻战次数
	 * 
	 * @param roleLoadInfo	角色缓存信息
	 * @return	boolean true-成功 false-失败
	 */
	public boolean updateRoleMutualCounts(RoleLoadInfo roleLoadInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleMutualCount", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateRoleMutualCounts error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色组队副本次数
	 * 
	 * @param roleLoadInfo	角色缓存信息
	 * @return	boolean true-成功 false-失败
	 */
	public boolean updateRoleTeamChallengeTimesAndLastTime(int roleId,Map<Integer, Integer> teamChallengeTimes, long lastFightTime,String teamChallengeStr) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("teamChallengeTimes", teamChallengeTimes);
		to.put("lastFightTime", lastFightTime);
		to.put("teamChallengeStr", teamChallengeStr);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleTeamChallengeTimesAndLastFightTime", to);
	}
	
	/**
	 * 更新竞技场3V3次数
	 * 
	 * @param roleLoadInfo	角色缓存信息
	 * @return	boolean true-成功 false-失败
	 */
	public boolean updateRoleTeam3V3TimesAndLastTime(int roleId, byte times, long lastFightTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("times", times);
		to.put("lastFightTime", lastFightTime);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleTeam3V3TimesAndLastFightTime", to);
	}
	
	/**
	 * 更新角色积分
	 * 
	 * @param roleInfo	角色信息
	 * @return	boolean true-成功 false-失败
	 */
	public boolean updateRoleScoreValue(RoleInfo roleInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleScoreValue", roleInfo);
		} catch (Exception e) {
			logger.error("updateRoleScoreValue error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色跑环信息
	 * 
	 * @param roleLoadInfo
	 * @return
	 */
	public boolean updateRoleRunQuestInfo(RoleLoadInfo roleLoadInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleRunQuestInfo", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateRoleRunQuestInfo error!", e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新首充奖励标记和手机绑定标记
	 * 
	 * @param roleLoadInfo
	 */
	public boolean updateFirstChargeAndPhoneState(RoleLoadInfo roleLoadInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateFirstChargeAndPhoneState", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateFirstChargeAndPhoneState error!",e);
			result = false;
		}
		return result;		
	}
	
	
	/**
	 * 更新苹果五星评论标识
	 * 
	 * @param roleId
	 */
	public boolean updateAppCommentState(int roleId) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("appCommentState", 1);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateAppCommentState", to);
		} catch (Exception e) {
			logger.error("updateFirstChargeAndPhoneState error!",e);
			result = false;
		}
		return result;		
	}

	/**
	 * 更新微信标记信息
	 * 
	 * @param roleLoadInfo
	 */
	public boolean updateWeiXinInfo(RoleLoadInfo roleLoadInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateWeiXinInfo", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateWeiXinInfo error!", e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新在线礼包时间
	 * 
	 * @param roleLoadInfo
	 */
	public boolean updateRoleOnlineTime(RoleLoadInfo roleLoadInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleOnlineTime", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateRoleOnlineTime error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 下线更新所有玩家在线礼包时间
	 * 
	 * @param roleLoadInfo
	 */
	public void batchUupdateRoleOnlineTime(List<RoleLoadInfo> roleLoadList) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH,false);
			if(client != null){
				for(RoleLoadInfo info : roleLoadList)
				{
					client.update("updateRoleOnlineTime", info);
				}
				client.commit();
			}
		} catch (Exception e) {
			if(client != null){
				client.rollback();
			}
			logger.error("batchUupdateRoleOnlineTime error!",e);
		}
	}

	/**
	 * 更新vip等级礼包记录
	 * 
	 * @param roleId
	 * @param newVipAwardStr
	 * @return
	 */
	public boolean updateVipAwardStr(int roleId, String newVipAwardStr) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("drawVipGiftStr", newVipAwardStr);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateVipAwardStr", to);
		} catch (Exception e) {
			logger.error("updateVipAwardStr error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色总充值的钱
	 * 
	 * @param roleId
	 * @param totalChargeMoney
	 * @param totalCoin
	 * @return
	 */
	public boolean updateRoleTotalCharge(int roleId, long totalChargeMoney) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("totalCharge", totalChargeMoney);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleTotalCharge", to);
		} catch (Exception e) {
			logger.error("updateRoleTotalCharge error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色第一次购买金币买一送一
	 * 
	 * @param roleId
	 * @param totalChargeMoney
	 * @param totalCoin
	 * @return
	 */
	public boolean updateRoleFirstChargeRewardStr(int roleId, String firstChargeRewardStr) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("firstChargeSaleNoStr", firstChargeRewardStr);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleFirstChargeRewardStr", to);
		} catch (Exception e) {
			logger.error("updateRoleFirstChargeRewardStr error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色会员卡信息
	 * 
	 * @param roleInfo
	 * @return
	 */
	public boolean updateRoleTimeCardInfo(RoleInfo roleInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleTimeCardInfo", roleInfo);
		} catch (Exception e) {
			logger.error("updateRoleTimeCardInfo error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色公会贡献值
	 * @param roleInfo
	 * @return
	 */
	public boolean updateRoleClubContribution(int roleId, int clubContribution){
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setId(roleId);
		roleInfo.setClubContribution(clubContribution);
		roleInfo.setClubContributionSum(roleInfo.getClubContributionSum() + clubContribution);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleClubContribution", roleInfo);
		} catch (Exception e) {
			logger.error("updateRoleClubContribution error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色攻打世界boss时间,总血量
	 * 
	 * @param roleLoadInfo
	 * @return
	 */
	public boolean updateFightWorldBoss(Timestamp lastTime, long hp, long bestHp,int id,long thisBossBest) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",id);
			to.put("lastWorldBossFightTime", lastTime);
			to.put("fightWorldBossHp", hp);
			to.put("bestFightBossHp", bestHp);
			to.put("thisBossBest", thisBossBest);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateFightWorldBoss", to);
		} catch (Exception e) {
			logger.error("updateFightWorldBoss",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色攻打世界boss历史最高伤害
	 * 
	 * @param roleLoadInfo
	 * @return
	 */
	public boolean updateBestFightWorldBoss(int roleId, int hp) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("bestFightBossHp", hp);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateBestFightWorldBoss", to);
	}
	/**
	 * 更新角色本日是否攻打世界boss
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateIsJoinWorldBoss(int roleId,byte state) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("isJoinBoss", state);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateIsJoinWorldBoss", to);
	}
	/**
	 * 删除世界boss
	 * 
	 * @param roleLoadInfoz
	 * @return
	 */
	public boolean clearFightWorldBoss() {
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("fightWorldBossHp",0);
			to.put("thisBossBest", 0);
			getSqlMapClient(DbConstants.GAME_DB).update("clearFightWorldBoss", to);
			return true;
		} catch (Exception e) {
			logger.error("clearFightWorldBoss error!",e);
			return false;
		}
		
	}
	/**
	 * 商店 type 1-购买黑市商店 2-刷新黑市商店 3-购买异域商店 4-刷新异域商店 5-VIP购买黑市永久使用权限
	 * 6-VIP购买异域商城永久使用权限
	 * @param roleLoadInfo
	 * @return
	 */
	public boolean shop(int roleId, int num, int type,Timestamp refreshTime) {
		boolean result = false;
		try {
			RoleLoadInfo to = new RoleLoadInfo();
			to.setId(roleId);

			if (type == 1) {
				to.setIsBuyGoldShop(num);
				result = getSqlMapClient(DbConstants.GAME_DB).update("buyGoldShop", to);
			} else if (type == 2) {
				to.setIsRefreshGoldShop(num);
				to.setRefreshGoldShopTime(refreshTime);
				result = getSqlMapClient(DbConstants.GAME_DB).update("refreshGoldShop", to);
			} else if (type == 3) {
				to.setIsBuyTurkShop(num);
				result = getSqlMapClient(DbConstants.GAME_DB).update("buyTurkShop", to);
			} else if (type == 4) {
				to.setIsRefreshTurkShop(num);
				to.setRefreshTurkShopTime(refreshTime);
				result = getSqlMapClient(DbConstants.GAME_DB).update("refreshTurkShop", to);
			} else if (type == 5) {
				to.setIsBuyGoldShopForVip(num);
				result = getSqlMapClient(DbConstants.GAME_DB).update("buyBlackShopForVip", to);
			} else if (type == 6) {
				to.setIsBuyTurkShopForVip(num);
				result = getSqlMapClient(DbConstants.GAME_DB).update("buyTrukShopForVip", to);
			}

		} catch (Exception e) {
			logger.error("shop error!",e);
			result = false;
		}
		return result;
	}
	/**
	 * 设置VIP等级
	 * @param roleId
	 * @param snatchTimes
	 * @return
	 */
	public boolean updateRoleVipLv(int roleId,int vipLv){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("vipLv", vipLv);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleVipLv", to);
	}
	
	/**
	 * 更新角色每天免费升级兵法次数
	 * @param roleId
	 * @param snatchTimes
	 * @return
	 */
	public boolean updateSoldierFreeUpNum(int roleId,int freeNum){
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("soldierFreeUpNum", (short) freeNum);
		to.put("soldierFreeUpTime", new Timestamp(System.currentTimeMillis()));
		return getSqlMapClient(DbConstants.GAME_DB).update("updateSoldierFreeUp", to);
	}
	
	/**
	 * 更新7日活动领取奖励字符串
	 * 
	 * @param roleId
	 * @param sevenRewardStr
	 * @return
	 */
	public boolean updateSevenRewardStr(int roleId, String sevenRewardStr) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("sevenDayAwardStr", sevenRewardStr);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateSevenRewardStr", to);
		} catch (Exception e) {
			logger.error("updateSevenRewardStr error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新7日活动登录天数
	 * 
	 * @param roleLoadInfo
	 * @return
	 */
	public boolean updateSevenDayLogin(RoleLoadInfo roleLoadInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateSevenDayLogin", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateSevenDayLogin error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新精彩活动领取奖励记录
	 * 
	 * @param roleId
	 * @param wonderRewardStr
	 * @return
	 */
	public boolean updateWonderRewardStr(int roleId, String wonderRewardStr) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("wonderAwardStr", wonderRewardStr);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateWonderRewardStr", to);
		} catch (Exception e) {
			logger.error("updateWonderRewardStr error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新投资计划标记
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateWonderPlanFlag(int roleId) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("isBuyPlan", 1);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateWonderPlanFlag", to);
		} catch (Exception e) {
			logger.error("updateWonderPlanFlag error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新押镖num，镖车类型，这镖车被截数量
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateYabiaoNum_biaocheType_ThisBiaocheTypeJiebiaoNum(int roleId, byte biaocheType, byte thisbiaocheTypeJieBiaoNum, byte todayYabiaoNum) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("biaocheType", biaocheType);
			to.put("thisbiaocheTypeJieBiaoNum", thisbiaocheTypeJieBiaoNum);
			to.put("todayYabiaoNum", todayYabiaoNum);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateYabiaoNum_biaocheType_ThisBiaocheTypeJiebiaoNum", to);
		} catch (Exception e) {
			logger.error("updateYabiaoNum_biaocheType_ThisBiaocheTypeJiebiaoNum error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新刷新押镖num，镖车类型，这镖车被截数量
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateRefBiaocheNum_biaocheType_ThisBiaocheTypeJiebiaoNum(int roleId, byte refBiaocheNum, byte biaocheType, byte thisbiaocheTypeJieBiaoNum) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("biaocheType", biaocheType);
			to.put("thisbiaocheTypeJieBiaoNum", thisbiaocheTypeJieBiaoNum);
			to.put("refBiaoCheNum", refBiaocheNum);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRefBiaocheNum_biaocheType_ThisBiaocheTypeJiebiaoNum", to);
		} catch (Exception e) {
			logger.error("updateRefBiaocheNum_biaocheType_ThisBiaocheTypeJiebiaoNum error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新刷新押镖num，今天截标数量，今天押镖数量
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean resetAllBiaocheNum(int roleId) 
	{
		try{
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("id", roleId+"");
			map.put("time", new Timestamp(System.currentTimeMillis())+"");
			getSqlMapClient(DbConstants.GAME_DB).update("resetAllBiaocheNum",map);
		}catch(Exception e){
			logger.error("resetAllBiaocheNum error!",e);
			return false;
		}
		return true;
	}
	
	/**
	 * 更新刷新押镖num
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateRefBiaoCheNum(int roleId, byte refBiaoCheNum) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("refBiaoCheNum", refBiaoCheNum);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRefBiaoCheNum", to);
		} catch (Exception e) {
			logger.error("updateRefBiaoCheNum error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新镖车类型
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateBiaocheType(int roleId, byte biaocheType) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("biaocheType", biaocheType);
			
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateBiaocheType", to);
		} catch (Exception e) {
			logger.error("updateBiaocheType error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新这镖车被截数量
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateThisBiaocheJiebiaoNum(int roleId, byte thisbiaocheTypeJieBiaoNum) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("thisbiaocheTypeJieBiaoNum", thisbiaocheTypeJieBiaoNum);
			
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateThisBiaocheJiebiaoNum", to);
		} catch (Exception e) {
			logger.error("updateThisBiaocheJiebiaoNum error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新押镖num，镖车类型，这镖车被截数量
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateTodayYabiaoNum(int roleId, byte todayYabiaoNum) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("todayYabiaoNum", todayYabiaoNum);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateTodayYabiaoNum", to);
		} catch (Exception e) {
			logger.error("updateTodayYabiaoNum error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新今天截标次数
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateTodayJiebiaoNum(int roleId, byte todayJiebiaoNum) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("todayJiebiaoNum", todayJiebiaoNum);
			
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateTodayJiebiaoNum", to);
		} catch (Exception e) {
			logger.error("updateTodayJiebiaoNum error!" ,e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新今天护镖次数
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateHubiaoNum(int roleId, byte hubiaoNum) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("hubiaoNum", hubiaoNum);
			
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateHubiaoNum", to);
		} catch (Exception e) {
			logger.error("updateHubiaoNum error!",e);
			result = false;
		}
		return result;
	}

	/**
	 * 更新vip特权购买信息
	 * 
	 * @param roleLoadInfo
	 */
	public boolean updateVipBuyAward(RoleLoadInfo roleLoadInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateVipBuyAward", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateVipBuyAward error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新vipExp
	 * 
	 * @param roleLoadInfo
	 */
	public boolean updateVipExp(int roleId, int vipExp) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("vipExp", vipExp);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateVipExp", to);
		} catch (Exception e) {
			logger.error("updateVipExp error!",e);
			result = false;
		}
		return result;
	}
	
	public boolean updateOneRecruitCoinOpTimes(int roleId, int times){
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("oneRecruitCoinOpTimes", times);
			
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateOneRecruitCoinOpTimes", to);
		} catch (Exception e) {
			logger.error("updateOneRecruitCoinOpTimes error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新武将单抽次数
	 * @param roleId
	 * @param times
	 * @return
	 */
	public boolean updateOneRecruitHeroNum(int roleId, int times){
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("id",roleId);
			to.put("oneRecruitHeroNum", times);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateOneRecruitHeroNum", to);
		} catch (Exception e) {
			logger.error("updateOneRecruitHeroNum error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新武将十连抽次数
	 * @param roleId
	 * @param tenRecruitMoneyStats 武将十连抽次数
	 * @param oneRecruitHeroNum 武将单抽次数
	 * @return
	 */
	public boolean updateTenRecruitHeroNum(int roleId, int tenRecruitHeroNum) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("tenRecruitHeroNum", tenRecruitHeroNum);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateTenRecruitHeroNum", to);
		} catch (Exception e) {
			logger.error("updateTenRecruitHeroNum error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新武将单抽免费时间
	 * @param roleId
	 * @param todayPlunderNum
	 * @return
	 */
	public boolean updateOneRecruitHeroFreeTime(int roleId, Timestamp lastRecruitTime) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id",roleId);
		to.put("lastRecruitHeroFreeTime", lastRecruitTime);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateHeroRecruitFreeTime", to);
		} catch (Exception e) {
			logger.error("updateOneRecruitHeroFreeTime error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 加载神兵信息
	 * @param roleInfo
	 */
	@SuppressWarnings("unchecked")
	public void WeaponInfo(RoleInfo roleInfo)
	{
		List<RoleWeaponInfo> roleWeaponInfoList = getSqlMapClient(DbConstants.GAME_DB)
				.queryList("selectRoleWeaponInfoByRoleId", roleInfo.getId());

 		if (roleWeaponInfoList != null && roleInfo.getRoleLoadInfo() != null) {
			Map<Integer, RoleWeaponInfo> roleWeaponInfoMap = new HashMap<Integer, RoleWeaponInfo>();

			for (RoleWeaponInfo info : roleWeaponInfoList) {
				roleWeaponInfoMap.put(info.getId(), info);
			}

			roleWeaponInfoMap.putAll(roleInfo.getRoleWeaponInfoPositionMap());
			roleInfo.getRoleLoadInfo().setRoleWeaponInfoMap(roleWeaponInfoMap);
			if (logger.isInfoEnabled()) {
				logger.info("Load GAME_WEAPON_INFO Table success! roleId=" + roleInfo.getId());
			}
		}
		roleInfo.setWeaponFlag(1);
	}
	
	/**
	 * 更新战斗力
	 * @param fightValueMap
	 * @return
	 */
	public boolean updateRoleFightValue(Map<Integer, Integer> fightValueMap) {
		SqlMapClient client = null;
		try {
			client = getSqlMapClient(DbConstants.GAME_DB, ExecutorType.BATCH, false);
			if (client == null) {
				return false;
			}
			for (int roleId : fightValueMap.keySet()) {
				HashMap<String, Object> to = new HashMap<String, Object>();
				to.put("id",roleId);
				to.put("fightValue", fightValueMap.get(roleId));
				client.update("updateRoleFightValue", to);
			}
			client.commit();
			return true;
		} catch (Exception e) {
			if(client != null)
			{
				client.rollback();
			}
			logger.error("updateRoleFightValue error!",e);
			return false;
		}
	}
	
	/**
	 * 更新玩家推送信息
	 * 
	 * @param roleInfo
	 * @return
	 */
	public boolean updateRolePush(RoleInfo roleInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRolePush", roleInfo);
		} catch (Exception e) {
			logger.error("updateRolePush error!",e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新抢夺次数
	 * @param roleId
	 * @param mineNum
	 * @param lastMineTime
	 * @return
	 */
	public boolean updateRoleMineNum(int roleId,int mineNum,Timestamp lastMineTime){
		RoleLoadInfo to=new RoleLoadInfo();
		to.setId(roleId);
		to.setMineNum(mineNum);
		to.setLastMineTime(lastMineTime);		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleMineNum", to);
	}
	
	/**
	 * 更新抢夺购买次数
	 * @param roleId
	 * @param mineNum
	 * @param lastMineTime
	 * @return
	 */
	public boolean updateRoleMinebuyNum(int roleId,int buyMine,Timestamp lastBuyMineTime){
		RoleLoadInfo to=new RoleLoadInfo();
		to.setId(roleId);
		to.setBuyMine(buyMine);
		to.setLastBuyMineTime(lastBuyMineTime);		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleMinebuyNum", to);
	}
	
	/**
	 * 更新角色公会建设时间
	 * @param roleId
	 * @param buildTime
	 * @return
	 */
	public boolean updateRoleClubBuildTime(int roleId, long buildTime, long totalBuild){
		RoleInfo to = new RoleInfo();
		to.setId(roleId);
		to.setClubBuildTime(new Timestamp(buildTime));
		to.setTotalBuild(totalBuild);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleClubBuildTime", to);
		
		
	}
	/**
	 * 更新角色可膜拜次数
	 * @param roleId
	 * @param worshipCount
	 * @return
	 */
	public boolean updateRoleWorshipCount(int roleId,int worshipCount){
		HashMap<String,Integer> to = new HashMap<String, Integer>();
		to.put("id", roleId);
		to.put("worshipCount",worshipCount);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleWorshipCount", to);
	}
	
	/**
	 * 批量更新角色可膜拜次数
	 * @param roleId
	 * @param worshipCount
	 * @return
	 */
	public boolean updateRoleWorshipCountBatch(List<Integer> list,int worshipCount){
		SqlMapClient client = null;
		try
		{
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH,false);
			if(client == null)
			{
				return false;
			}
			HashMap<String,Integer> to = new HashMap<String, Integer>();
			for(int roleId : list){
				to.put("id", roleId);
				to.put("worshipCount", worshipCount);
				to.put("worldChatCount", 0); //世界聊天消息条数 每日清空
				client.update("updateRoleWorshipCount", to);
			}
			client.commit();
		} catch (Exception e)
		{
			if(client != null)
			{
				client.rollback();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 更新角色被膜拜次数
	 * @param roleId
	 * @param worshipCount
	 * @return
	 */
	public boolean updateRoleBeWorshipNum(int roleId,int beWorshipNum){
		HashMap<String,Integer> to = new HashMap<String, Integer>();
		to.put("id", roleId);
		to.put("beWorshipNum",beWorshipNum);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleBeWorshipNum", to);
	}
	
	/**
	 * 更新角色星石币数量
	 * @param roleId
	 * @param starMoney
	 * @return
	 */
	public boolean updateRoleStarMoney(int roleId, int starMoney){
		RoleLoadInfo to = new RoleLoadInfo();
		to.setId(roleId);
		to.setStarMoney(starMoney);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleStarMoney", to);
		
	}
	
	/**
	 * 世界聊天数量清零
	 * @param list
	 * @return
	 */
	public boolean batchUpdateRoleWorldChatCount(List<Integer> list){
		SqlMapClient client = null;
		try
		{
			client = getSqlMapClient(DbConstants.GAME_DB,ExecutorType.BATCH,false);
			if(client == null)
			{
				return false;
			}
			HashMap<String,Integer> to = new HashMap<String, Integer>();
			for(int roleId : list){
				to.put("id", roleId);
				to.put("worldChatCount", 0); //世界聊天消息条数 每日清空
				client.update("updateRoleWorldChatCount", to);
			}
			client.commit();
		} catch (Exception e)
		{
			if(client != null)
			{
				client.rollback();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 更新公会科技 角色相关属性
	 * @param roleId
	 * @param starMoney
	 * @return
	 */
	public boolean updateRoleClubTechPlusInfo(int roleId, String clubTechPlusInfo){
		RoleInfo to = new RoleInfo();
		to.setId(roleId);
		to.setClubTechPlusInfo(clubTechPlusInfo);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleClubTechPlusInfo", to);
		
	}
	
	/**
	 * 更新通关npc
	 * @param roleId
	 * @param passMapNpcNos
	 * @return
	 */
	public boolean updateRolePassMapNpcNos(int roleId, List<Integer> passMapNpcNos){
		RoleLoadInfo to = new RoleLoadInfo();
		to.setId(roleId);
		to.setPassMapNpcNos(passMapNpcNos);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRolePassMapNpcNos", to);
	}
	
	/**
	 * 更新玩家当前称号
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateNowAppellation(int roleId, String nowAppellation) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("nowAppellation", nowAppellation);
			to.put("id", roleId);
			result = getSqlMapClient(DbConstants.GAME_DB).update(
					"updateNowAppellation", to);
		} catch (Exception e) {
			logger.error("updateNowAppellation error!", e);
			result = false;
		}
		return result;
	}
	/**
	 * 更新玩家所有称号
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateAllAppellation(int roleId, String allAppellation) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("allAppellation", allAppellation);
			to.put("id", roleId);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateAllAppellation", to);
		} catch (Exception e) {
			logger.error("updateAllAppellation error!", e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新玩家练兵场扫荡信息
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateAllExpAction(int roleId, String allExpActionMax) {
		boolean result = false;
		try {
			HashMap<String, Object> to = new HashMap<String, Object>();
			to.put("allExpActionMax",allExpActionMax);
			to.put("id", roleId);
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateAllExpAction", to);
		} catch (Exception e) {
			logger.error("updateAllExpAction error!" ,e);
			result = false;
		}
		return result;
	}
	/**
	 * 更新玩家时装显示方案
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateShopPlan(int roleId, int planId) {	
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("isShowShizhuang",planId);
		to.put("id", roleId);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateShopPlan", to);
	}
	/**
	 * 更新玩家时装领取奖励状态
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateHaveReward(int roleId, List<Integer> rewards) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("haveReward",rewards);
		to.put("id", roleId);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateHaveReward", to);
	}
	/**
	 * 更新玩家时装锁定属性ID
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateLockShizhuang(int roleId, Map<Integer,Integer> lock) {
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("lockShizhuang",lock);
		to.put("id", roleId);
		return getSqlMapClient(DbConstants.GAME_DB).update("updateLockShizhuang", to);
	}
	
	/**
	 * 更新公会科技 角色相关属性
	 * @param roleId
	 * @param starMoney
	 * @return
	 */
	public boolean updateRoleIsCanAddFlag(int roleId, int isCanAddFlag){
		RoleInfo to = new RoleInfo();
		to.setId(roleId);
		to.setIsCanAddFlag(isCanAddFlag);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleIsCanAddFlag", to);
		
	}

	/**
	 * 更新玩家活跃度
	 * 
	 * @param roleLoadInfo
	 */
	public boolean updateRoleActiveVal(RoleLoadInfo roleLoadInfo) {
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleActiveVal", roleLoadInfo);
		} catch (Exception e) {
			logger.error("updateRoleActiveVal error!", e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色所雇佣的所以英雄Id和时间
	 * @param roleId
	 * @param hireInfos
	 * @return
	 */
	public boolean updateRoleHireInfos(int roleId, String hireInfos){
		RoleLoadInfo to = new RoleLoadInfo();
		to.setId(roleId);
		to.setHireInfos(hireInfos);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleHireInfos", to);
		} catch (Exception e) {
			logger.error("updateRoleHireInfos error!", e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色称号信息
	 * @param roleId
	 * @param nowAppellation
	 * @param allAppellation
	 * @return
	 */
	public boolean updateRoleTitleInfo(int roleId, String nowAppellation, String allAppellation){
		RoleLoadInfo to = new RoleLoadInfo();
		to.setId(roleId);
		to.setNowAppellation(nowAppellation);
		to.setAllAppellation(allAppellation);
		
		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleTitleInfo", to);
		} catch (Exception e) {
			logger.error("updateRoleTitleInfo error!", e);
			result = false;
		}
		return result;
	}
	
	/**
	 * 更新角色被膜拜次数
	 * @param roleId
	 * @param worshipCount
	 * @return
	 */
	public boolean updateTodayPlusWorshipNum(int roleId,int todayPlus){
		HashMap<String,Integer> to = new HashMap<String, Integer>();
		to.put("id", roleId);
		to.put("plusWorshipNum",todayPlus);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateSuperRPlus", to);
	}
	
	/**
	 * 更新角色被膜拜次数
	 * @param roleId
	 * @param worshipCount
	 * @return
	 */
	public boolean updateRoleStarMoney4GM(int roleId,int starMoney){
		HashMap<String,Integer> to = new HashMap<String, Integer>();
		to.put("id", roleId);
		to.put("starMoney",starMoney);
		
		return getSqlMapClient(DbConstants.GAME_DB).update("updateRoleStarMoney4GM", to);
	}
	
	/**
	 * 更新角色资源信息
	 * @param action
	 * @param roleId
	 * @param type
	 * @param value
	 * @param time
	 * @return
	 */
	public boolean updateRoleResource4GM(int action, long roleId, ConditionType type, long value) {
		if(value <= 0){
			value = 0;
		}
		HashMap<String, Object> to = new HashMap<String, Object>();
		to.put("id", roleId);
		switch (type) {
		case TYPE_MONEY:
		case TYPE_COURAGE:
		case TYPE_JUSTICE:
		case TYPE_KUAFU_MONEY:
		case TYPE_TEAM_MONEY:
		case TYPE_HIS_EXPLOIT:
		case TYPE_EQUIP:
		case TYPE_PVP_3_MONEY:
		case TYPE_STAR_MONEY:
		case TYPE_COIN:
		case TYPE_EXPLOIT:
		case TYPE_ENERGY:
		case TYPE_SP:
		case TYPE_TECH:
		case TYPE_CLUB_CONTRIBUTION:
			to.put(type.getName(), value);
			break;
		default:
			logger.error("there are no resoure update");
			return false;
		}

		boolean result = false;
		try {
			result = getSqlMapClient(DbConstants.GAME_DB).update("updateRoleReource4GM", to);
		} catch (Exception e) {
			logger.error("updateRoleReource4GM error!",e);
			result = false;
		}
		return result;
	}
	
}
