package com.snail.webgame.game.init;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.FightCompetitionRankList;
import com.snail.webgame.game.cache.FightMutualRankList;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.configdb.ConfigXmlService;
import com.snail.webgame.game.configdb.ConfigXmlVerify;
import com.snail.webgame.game.core.GameLogConfigService;
import com.snail.webgame.game.dao.ChallengeBattleDAO;
import com.snail.webgame.game.dao.ClubHireHeroDao;
import com.snail.webgame.game.dao.EquipDAO;
import com.snail.webgame.game.dao.FightArenaDAO;
import com.snail.webgame.game.dao.GameLogDAO;
import com.snail.webgame.game.dao.GameSettingDAO;
import com.snail.webgame.game.dao.HeroDAO;
import com.snail.webgame.game.dao.ItemDAO;
import com.snail.webgame.game.dao.MineDAO;
import com.snail.webgame.game.dao.OpActivityProgressDAO;
import com.snail.webgame.game.dao.PhoneRecordDAO;
import com.snail.webgame.game.dao.PresentEnergyDao;
import com.snail.webgame.game.dao.QuestDAO;
import com.snail.webgame.game.dao.RideDAO;
import com.snail.webgame.game.dao.RoleChargeDAO;
import com.snail.webgame.game.dao.RoleClubEventDao;
import com.snail.webgame.game.dao.RoleClubInfoDao;
import com.snail.webgame.game.dao.RoleClubMemberDao;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.RoleRelationDao;
import com.snail.webgame.game.dao.ToolBoxDAO;
import com.snail.webgame.game.dao.ToolDAO;
import com.snail.webgame.game.dao.ToolOpActDAO;
import com.snail.webgame.game.dao.ToolOpActRewardDAO;
import com.snail.webgame.game.dao.VoiceDao;
import com.snail.webgame.game.dao.WeaponDao;
import com.snail.webgame.game.dao.WorldBossDAO;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.gm.annotation.CommandMappingHandlerAdapter;
import com.snail.webgame.game.protocal.gm.service.CommandService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.rank.service.RankService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;

public class GameInit {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private static final GameInit instance = new GameInit();

	public static GameInit instance() {
		return instance;
	}

	public boolean init() {

		// 初始化条件检测
		AbstractConditionCheck.init();

		// 初始化GM 命令对应方法
		CommandMappingHandlerAdapter.init(CommandService.class);
		
		//检测是否要新建日志表
		if(!GameLogDAO.getInstance().checkThisWeekLog())
		{
			System.out.println("[SYSTEM] Start server create new log error#######");
			return false;
		}

		return initData();
	}

	/**
	 * 载入初始化数据
	 * @return
	 */
	private boolean initData() {
		boolean init = false;
		ExecutorService service = null;
		
		// <heroId,roleId>
		Map<Integer,Integer> starHeroIds = new HashMap<Integer, Integer>();
		// <roleId,<itemNo,itemNum>>
		Map<Integer,Map<String,Integer>> itemMap = new HashMap<Integer,Map<String,Integer>>();
		
		try {
			init = ConfigXmlService.initAllConfigXMLData()// 加载xml
					&& ConfigXmlVerify.verifyAllConfigXMLData()// 验证xml
					&& GameSettingDAO.getInstance().loadGameSetting()//
					&& RoleDAO.getInstance().loadAllRole()//
					&& MineDAO.getInstance().loadAllMines() //
					&& ClubHireHeroDao.getInstance().loadAllClubHireHero()
					&& HeroDAO.getInstance().loadAllHeroInfoByRange(starHeroIds, itemMap)//
					//&& EquipDAO.getInstance().loadEquipInfoByRange()//
					//&& StoreItemDAO.getInstance().loadAllStoreItem()//
					//&& FightDeployDAO.getInstance().loadAllFightDeployInfo()//
					&& FightArenaDAO.getInstance().loadAllFightArena()//
					//&& QuestDAO.getInstance().loadQuestList()
					//&& HeroRecruitDAO.getInstance().loadHeroRecruit()
					//&& FightGemDAO.getInstance().loadAllFightGemInfo()//
					//&& FightCampaignDAO.getInstance().loadAllFightCampaignInfo()//
					//&& FightCampaignDAO.getInstance().loadAllFightCampaignHero()//
					//&& FightCampaignDAO.getInstance().loadAllFightCampaignBattle()//					
					&& ToolDAO.getInstance().loadToolProgramInfo()//
					//&& WeaponDao.getInstance().loadRoleWeaponInfoInPos()//
					//&& ItemDAO.getInstance().loadSnatchRivalInfo();
					&& PresentEnergyDao.getInstence().cleanupPresentEnergy()
					&& WorldBossDAO.getInstance().loadWorldBoss()//世界boss==================
					&& RoleChargeDAO.getInstance().loadAllRoleCharge()
					&& PhoneRecordDAO.getInstance().loadPhoneRecord()
					&& ToolBoxDAO.getInstance().loadToolBox()
					&& ToolOpActDAO.getInstance().loadToolOpActInfo()
					&& ToolOpActRewardDAO.getInstance().loadToolOpActRewardInfo()
					&& OpActivityProgressDAO.getInstance().loadRoleOpActProInfo()
//					&& RoleClubEventDao.getInstance().cleanupClubEvent()
					&& RoleClubInfoDao.getInstance().loadAllRoleClubInfo()
					&& RoleClubMemberDao.getInstance().loadAllRoleClubMember()
					&& RoleClubEventDao.getInstance().loadAllClubEvent()
					&& QuestDAO.getInstance().loadQuestList()
					&& VoiceDao.getInstance().loadVoiceRoom()
					&& EquipDAO.getInstance().loadEquipInfoByRange()
					&& WeaponDao.getInstance().loadRoleWeaponInfoInPos()
					&& ItemDAO.getInstance().loadSnatchRivalInfo()
					&& RoleRelationDao.getInstance().loadAllRoleRelation()
					&& RideDAO.getInstance().seleteRideOfBattle()
					&& ChallengeBattleDAO.getTeamChallengeRecord();
					
			if (!init) {
				return init;
			}
			
			service = Executors.newFixedThreadPool(4);
			
			/*FutureTask<Boolean> loadEquipFuture = new FutureTask<Boolean>(new Callable<Boolean>() {
				public Boolean call() {
					return EquipDAO.getInstance().loadEquipInfoByRange();
				}
			});
			
			FutureTask<Boolean> loadWeaponFuture = new FutureTask<Boolean>(new Callable<Boolean>() {
				public Boolean call() {
					return WeaponDao.getInstance().loadRoleWeaponInfoInPos();
				}
			});
			
			FutureTask<Boolean> loadItemFuture = new FutureTask<Boolean>(new Callable<Boolean>() {
				public Boolean call() {
					return ItemDAO.getInstance().loadSnatchRivalInfo();
				}
			});

			FutureTask<Boolean> loadAllRoleRelationFuture = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return RoleRelationDao.getInstance().loadAllRoleRelation();
				}
			});
			
			service.execute(loadEquipFuture);
			service.execute(loadWeaponFuture);
			service.execute(loadItemFuture);
			service.execute(loadAllRoleRelationFuture);
			
			boolean result1 = loadEquipFuture.get();
			boolean result2 = loadWeaponFuture.get();
			boolean result3 = loadItemFuture.get();
			boolean result4 = loadAllRoleRelationFuture.get();
			
			if (!result1 || !result2 || !result3 || !result4) {
				logger.error("game init error,result1="+result1+",result2="+result2+",result3="+result3+",result4="+result4);
				return false;
			}*/
			
			logger.info("GameInit Load data from db successed");
			
			HeroService.heroStarDownDeal(starHeroIds, itemMap);

			
			//服务器启动计算所有玩家战力
			RoleService.calAllRoleFightValue(service);			
			logger.info("GameInit calAllRoleFightValue successed");
						
			//世界boss伤害个人排行赋值
			//WorldBossMap.sortRoleWorldBoss();
			
			// 执行全局竞技场排序
			FightCompetitionRankList.initSort();
			FightMutualRankList.initSort();
			
			//维护日志库的GAME_CONFIG与GAME_CONFIG_Type
			GameLogConfigService.insertGameConfig();
			
			//加载排行榜称号
			try {
				RankService.rank();
				TitleService.getNewRanks();
				TitleService.getNewRanks4WorldBoss();
			} catch(Exception e) {
				if(logger.isInfoEnabled()){
					logger.info("rank exception: "+e.getMessage());
				}
			}

			return true;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("game init Data error!", e);
			}
		} finally {
			if (service != null) {
				service.shutdown();
			}
		}
		
		return false;
	}
}
