package com.snail.webgame.game.protocal.fight.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.ChallengeBattleInfoMap;
import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.common.fightdata.DropBagInfo;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.fightdata.HeroSkillDataInfo;
import com.snail.webgame.game.common.fightdata.ServerFightEndReq;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.NPCXmlInfo;
import com.snail.webgame.game.common.xml.info.NPCXmlLoader;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.EnergyCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.ChallengeBattleDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.FightDeployInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.StoreItemInfo;
import com.snail.webgame.game.info.log.ChallengeLog;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.arena.service.ArenaMgtService;
import com.snail.webgame.game.protocal.challenge.service.ChallengeService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.startFight.IntoFightResp;
import com.snail.webgame.game.protocal.fightdeploy.service.FightDeployService;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.protocal.store.service.StoreService;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.cache.GWXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.SceneXmlInfoMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetailPoint;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.ChapterInfo;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXMLNPC;

/**
 * 副本战斗处理
 * @author zenggang
 */
public class CreateFightInfoService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 创建副本战斗数据
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int createChallengeFightInfo(RoleInfo roleInfo, FightInfo fightInfo) {
		if(GameValue.GAME_CHALLENGE_OPEN != 1){
			 return ErrorCode.CHALLENGE_ERROR_9;
		}
		String[] defendStrs = fightInfo.getDefendStr().split(",");
		if (defendStrs.length != 3) {
			return ErrorCode.CHALLENGE_ERROR_1;
		}
		byte challengeTypeNo = NumberUtils.toByte(defendStrs[0]);
		int chapterNo = NumberUtils.toInt(defendStrs[1]);
		int battleId = NumberUtils.toInt(defendStrs[2]);

		if (chapterNo < 0 || challengeTypeNo < 0 || battleId < 0) {
			return ErrorCode.CHALLENGE_ERROR_2;
		}

		// 副本xml信息
		ChallengeBattleXmlInfo challengeXmlInfo = ChallengeBattleXmlInfoMap.getInfoByNo(challengeTypeNo);
		if (challengeXmlInfo == null) {
			return ErrorCode.CHALLENGE_ERROR_3;
		}

		// 副本章节信息
		ChapterInfo chapterXmlInfo = challengeXmlInfo.getChapterInfoByNo(chapterNo);
		if (chapterXmlInfo == null) {
			return ErrorCode.CHALLENGE_ERROR_4;
		}
		
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHero == null) {
			return ErrorCode.HERO_UP_ERROR_8;
		}

		// 战场信息
		BattleDetail battleDetail = chapterXmlInfo.getBattleDetail(battleId);
		if (battleDetail == null) {
			return ErrorCode.CHALLENGE_ERROR_5;
		}
		
		if(battleDetail.getUnLockLv() > mainHero.getHeroLevel()){
			return ErrorCode.CHALLENGE_ERROR_8;
		}
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		if(roleInfo.getRoleLoadInfo().getChallengeOpen() != 1){
			if (battleDetail.getConds() != null) {
				conds.addAll(battleDetail.getConds());
			}
		}
		if (battleDetail.getConds2() != null) {
			conds.addAll(battleDetail.getConds2());
		}

		// 判断是否可以打
		int condCheck = AbstractConditionCheck.checkCondition(roleInfo,conds);
		if (condCheck != 1) {
			return condCheck;
		}

		// 判断副本时间,次数限制
		ChallengeBattleInfo info = ChallengeBattleInfoMap.getBattleInfo(roleInfo.getId(), (byte)challengeTypeNo, chapterNo,
				battleId);
		int timeNumCheck = ChallengeService.checkChallengeTimeNum(roleInfo.getId(), info, battleDetail);
		if (timeNumCheck != 1) {
			return timeNumCheck;
		}

		fightInfo.setFightId(FightIdGen.getSequenceId());
		fightInfo.setFightRoleId(roleInfo.getId());
		fightInfo.setEndTimeType(battleDetail.getEndTimeType());
		
		// 暂定为10级以后(出新手),剧情副本第三章开启异常记录,英雄副本第二章开启检测
		if(battleDetail != null 
				&& battleDetail.getKillNpc() > 0 && mainHero.getHeroLevel() > 10 
				&& ((challengeTypeNo == 1 && chapterNo >2) || (challengeTypeNo == 2 && chapterNo > 1)))
		{
			fightInfo.setCheckFlag((byte) 1);
			generateBossProp(fightInfo,battleDetail.getKillNpc());
		}
		
		
		
		//计算副本掉落
		HashMap<Integer, BattleDetailPoint> pointMap = battleDetail.getPointsMap();
		Map<Integer, DropBagInfo> dropBagMap = FightService.fightDrop(pointMap);
		if(dropBagMap != null && dropBagMap.size() > 0)
		{
			fightInfo.setDropMap(dropBagMap);
		}
		
		return 1;
	}

	/**
	 * 获取进攻方战斗数据
	 * @param fightArena
	 * @return
	 */
	public static FightSideData getAttFightSideData(RoleInfo roleInfo, FightType fightType) {
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightType);
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightType);
		
		FightSideData sideDate = new FightSideData();

		sideDate.setSideId(FightType.FIGHT_SIDE_0);
		sideDate.setSideRoleId(roleInfo.getId());
		sideDate.setSideName(roleInfo.getRoleName());

		List<FightArmyDataInfo> armyInfos = new ArrayList<FightArmyDataInfo>();
		List<FightArmyDataInfo> addArmyInfos = sideDate.getAddArmyInfos();

		List<FightDeployInfo> fightDeployList = FightDeployService.getRoleFightDeployBy(roleInfo.getId());
		if (fightDeployList != null) {
			for (FightDeployInfo info : fightDeployList) {
				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(info.getRoleId(), info.getHeroId());
				if (heroInfo == null) {
					continue;
				}
				if (fightType == FightType.FIGHT_TYPE_11 || fightType == FightType.FIGHT_TYPE_12
						|| fightType == FightType.FIGHT_TYPE_10 || fightType == FightType.FIGHT_TYPE_13) {
					double baseAddRate = 0;
					if(fightType == FightType.FIGHT_TYPE_11){
						baseAddRate = GameValue.FIGHT_TYPE_11_BASE_ADD_RATE;;
					}
					
					FightArmyDataInfo armyData = FightService.getFightArmyDatabyHeroInfo(roleInfo, heroInfo,
							info.getDeployPos(), sideDate.getSideId(), mainRate, otherRate, (byte) 2, baseAddRate);
					if (armyData == null) {
						continue;
					}
					armyInfos.add(armyData);
					FightArmyDataInfo addArmyData = FightService.getFightArmyDatabyHeroInfo(roleInfo, heroInfo,
							info.getDeployPos(), sideDate.getSideId(), mainRate, otherRate, (byte) 3, baseAddRate);
					if (addArmyData == null) {
						continue;
					}
					addArmyInfos.add(addArmyData);
				} else {
					FightArmyDataInfo armyData = FightService.getFightArmyDatabyHeroInfo(roleInfo, heroInfo,
							info.getDeployPos(), sideDate.getSideId(), mainRate, otherRate, (byte) 1);
					if (armyData == null) {
						continue;
					}
					armyInfos.add(armyData);
				}
			}
		}
		if (armyInfos.size() <= 0) {
			return null;
		}

		if (armyInfos != null && armyInfos.size() > 0) {
			sideDate.setArmyInfos(armyInfos);
		}
		sideDate.setFightArmyNum(sideDate.getArmyInfos().size());
		sideDate.setFightAddArmyNum(addArmyInfos.size());

		return sideDate;
	}

	/**
	 * 副本战斗结束处理
	 * @param roleInfo
	 * @param fightEndReq 战后信息
	 * @param fightInfo 战斗信息
	 * @param star 星级
	 * @param getResourceNum 资源变动
	 * @param getPrizeMum 道具变动
	 * @param addEquipIds 装备变动（掉装备）
	 * @return
	 */
	public static int opChallengeBattleEnd(RoleInfo roleInfo, ServerFightEndReq fightEndReq, FightInfo fightInfo,
			String star,List<BattlePrize> prizeList, List<BattlePrize> fbPrizeList, List<Integer> refreshShop) {
		// 普通副本
		int action = ActionType.action103.getType();
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return ErrorCode.ROLE_LOAD_ERROR_3;
		}
		
		int newStar = 0;
		int winSide = fightEndReq.getWinSide();// 获胜方
		for (FightSideData fightSideData : fightInfo.getFightDataList()) {
			int sideId = fightSideData.getSideId();
			if (sideId == FightType.FIGHT_SIDE_1) {// 守方（npc）
				// 主线副本战斗，不需要结算npc
				continue;
			}

			int roleId = roleInfo.getId();
			int sideRoleId = (int) fightSideData.getSideRoleId();
			if (roleId != sideRoleId) {
				// 请求的，不是玩家自己的战斗信息
				return ErrorCode.CHALLENGE_ERROR_7;
			}
			String common = "";
			boolean win = false;
			// 副本战斗记录处理
			String pamarater = fightInfo.getStartRespDefendStr();
			if (pamarater == null) {
				continue;
			}
			//获取副本信息
			String[] challengeChapters = pamarater.split(",");
			if (challengeChapters.length != 3) {
				continue;
			}
			byte chapterType = Byte.valueOf(challengeChapters[0]);
			int chapterNo = Integer.valueOf(challengeChapters[1]);
			int battleNo = Integer.valueOf(challengeChapters[2]);
			BattleDetail battleDetail = ChallengeBattleXmlInfoMap.getBattleDetail(chapterType, chapterNo, battleNo);
			ChallengeBattleInfo challengeBattleInfo = ChallengeBattleInfoMap.getBattleInfo(roleId, (byte)chapterType, chapterNo, battleNo);
			if (battleDetail == null) {
				continue;
			}
			if(battleDetail.getChapterType() == ChallengeBattleXmlInfo.TYPE_NO_2){
				// 英雄副本
				action = ActionType.action107.getType();
			}
			
			if (winSide == sideId) {
				// 获胜方
				win = true;
				List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
				//扣除体力
				if (battleDetail.getConds2() != null) {
					conds.addAll(battleDetail.getConds2());
				}
				// 扣除消耗
				if (!RoleService.subRoleResource(action, roleInfo, conds ,null)) {
					return ErrorCode.CHALLENGE_ERROR_6;
				}
				
				//处理经验掉落
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if (heroInfo == null) {
					return ErrorCode.ADD_ERP_ERROR_1;
				}
				//奖励之前等级
				int level = heroInfo.getHeroLevel();
				int addExp = 0;
				int pzMaxLv = HeroXMLInfoMap.getMaxMainLv();
				if(level < pzMaxLv)
				{
					if(battleDetail.getChapterType() == 1){
						addExp = level*GameValue.EXP_VALUE + GameValue.EXP_ADD;
					} else if(battleDetail.getChapterType() == 2) {
						addExp = level*GameValue.EXP_VALUE_1 + GameValue.EXP_ADD_1;
					}
				}
				if(addExp > 0)
				{	
					int addExpResult = ItemService.expAdd(action, roleInfo, addExp, null, null);
					if (addExpResult != 1) 
					{
						return addExpResult;
					}
					prizeList.add(new BattlePrize(ConditionType.TYPE_EXP.getName(), addExp, (byte) 2, (byte)0));
					if(level < heroInfo.getHeroLevel())
					{
						common = "lvUp";
					}
				}
				
				//计算副本胜利获得奖励
				List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLList(battleDetail.getBag());
				List<DropInfo> addList = new ArrayList<DropInfo>();
				//解析胜利奖励
				if(list != null && list.size() > 0)
				{
					ItemService.getDropXMLInfo(roleInfo, list,addList);
				}
				
				//副本掉落
				List<DropBagInfo> dropBagList = fightEndReq.getDropList();
				List<DropInfo> dropInfoList = new ArrayList<DropInfo>();

				//验证掉落
				if(dropBagList != null && dropBagList.size() > 0){
					if(checkDrop(dropBagList, fightInfo)){
						for(DropBagInfo bagInfo : dropBagList){
							dropInfoList.addAll(bagInfo.getDrop());
						}
					} else {
						return ErrorCode.CHALLENGE_ERROR_6;
					}
				}
				
				if(addList != null && addList.size() > 0)
				{
					dropInfoList.addAll(addList);
				}
				
				if(dropInfoList != null && dropInfoList.size() > 0){
					//计算翻牌
					String cardBag = "";
					if(battleDetail.getCardBag() != null && battleDetail.getCardBag().length() > 0)
					{
						cardBag = battleDetail.getCardBag();
					}
					
					//必出奖励获取计算
					String itemNo = battleDetail.getItemNo(); //必掉物品
					if(itemNo != null && itemNo.length() > 0)
					{
						int itemCount = battleDetail.getItemNo().split(",").length;
						String item = "";
						boolean flag1 = false;
						if(challengeBattleInfo != null)
						{
							List<String> dropItemNo = challengeBattleInfo.getItemByNo();
							
							if(challengeBattleInfo != null  && challengeBattleInfo.getItemByNum() == (GameValue.CHALLENGE_ITEM_BY-1))
							{
								if(dropItemNo == null) //一件都没掉
								{
									item = itemNo;
									flag1 = true;
								}
								else if(dropItemNo != null && itemCount > dropItemNo.size())
								{
									for(String a : dropItemNo)
									{
										itemNo = itemNo.replaceAll(a, "");
									}
									item = itemNo;
									flag1 = true;
								}
							}
						}
						
						//掉落必掉物品
						if(flag1)
						{
							//必掉物品所在的bag
							List<DropXMLInfo> bagList = PropBagXMLMap.getPropBagXMLList(battleDetail.getBagView());
							
							Iterator<DropInfo> iter = dropInfoList.iterator();
							while(iter.hasNext())
							{
								DropInfo dropInfo = iter.next();
								if(item.contains(dropInfo.getItemNo() + ","))
								{
									iter.remove();
								}
							}
							
							for(String a : item.split(","))
							{
								DropInfo dropInfo = new DropInfo();
								int num = 1;
								boolean isFind = false;
								for(DropXMLInfo info : bagList)
								{
									if(info.getItemNo().equals(a))
									{
										isFind = true;
										num = RandomUtil.getRandom(info.getItemMinNum(), info.getItemMaxNum());

										dropInfo.setItemNo(info.getItemNo());
										dropInfo.setItemNum(num);
										dropInfo.setItemType(ItemService.checkItemType(info.getItemNo()));

										break;
									}
								}
								if(!isFind)
								{
									dropInfo.setItemNo(a);
									dropInfo.setItemNum(num);
									dropInfo.setItemType(ItemService.checkItemType(a));
								}
								dropInfoList.add(dropInfo);
							}
						}
					}
					
					//添加掉落物品
					List<BattlePrize> dropList1 =  new ArrayList<BattlePrize>();
					int result = ItemService.addPrize(action, roleInfo, dropInfoList, cardBag,
							null, null, null, null, null, dropList1, fbPrizeList, null, true);
					if (result != 1) 
					{
						return result;
					}
					if(dropList1 != null && dropList1.size() > 0){

						for(BattlePrize prize1 : dropList1){
							boolean flag = false;
							for(BattlePrize prize2 : prizeList){
								if(prize1.getNo().equals(prize2.getNo())){
									prize2.setNum(prize2.getNum() + prize1.getNum());
									flag = true;
									break;
								}
							}
							if(!flag){
								prizeList.add(prize1);
							}
						}
					}
				}
				
				//判断是否刷新商店
				int goldShop = 0;
				int turkShop = 0;
				//判断之前先更新商店状态
				long now = System.currentTimeMillis();
				Timestamp goldTime = roleLoadInfo.getRefreshGoldShopTime();
				Timestamp turkTime = roleLoadInfo.getRefreshTurkShopTime();
				int refreshGoldShop = roleLoadInfo.getIsRefreshGoldShop();
				int refreshTurkShop = roleLoadInfo.getIsRefreshTurkShop();
				
				if(refreshGoldShop == 1)
				{
					if(goldTime != null && now > goldTime.getTime()){
						if(RoleDAO.getInstance().shop(roleId, 0, 2,null))
						{
							roleLoadInfo.setIsRefreshGoldShop(0);
							roleLoadInfo.setRefreshGoldShopTime(null);
						}
					}
				}
				if( refreshTurkShop == 1)
				{
					if(turkTime != null && now > turkTime.getTime())
					{
						if(RoleDAO.getInstance().shop(roleId, 0, 4,null))
						{
							roleLoadInfo.setIsRefreshTurkShop(0);
							roleLoadInfo.setRefreshTurkShopTime(null);
						}
					}
				}
				//开始判断
				//黑市商店
				if(roleLoadInfo.getIsBuyGoldShopForVip() == 0 && roleLoadInfo.getIsRefreshGoldShop() != 1)
				{
					if(level ==  heroInfo.getHeroLevel() //本次不升级才触发
							&& heroInfo.getHeroLevel() >= GameValue.BLACK_SHOP_OPEN_LV
							&& StoreService.refreshShop(1))
					{
						Timestamp refreshTime = new Timestamp(System.currentTimeMillis() + (60 * 60 *1000));
						StoreService.refreshStoreItem(roleInfo, StoreItemInfo.STORE_TYPE_7);
						//刷新黑市商店
						if(RoleDAO.getInstance().shop(roleId, 1, 2,refreshTime))
						{
							roleLoadInfo.setIsRefreshGoldShop(1);
							roleLoadInfo.setRefreshGoldShopTime(refreshTime);
							//通知客户端 黑市商店已刷新
							goldShop = 1;
						}
					}
				}
				//异域商店
				if(roleLoadInfo.getIsBuyTurkShopForVip() == 0 && roleLoadInfo.getIsRefreshTurkShop() != 1)
				{
					if(level ==  heroInfo.getHeroLevel() //本次不升级才触发
							&& heroInfo.getHeroLevel() >= GameValue.TURK_SHOP_OPEN_LV
							&&StoreService.refreshShop(2))
					{
						Timestamp refreshTime = new Timestamp(System.currentTimeMillis() + (60 * 60 *1000));
						StoreService.refreshStoreItem(roleInfo, StoreItemInfo.STORE_TYPE_9);
						//刷新异域商店
						if(RoleDAO.getInstance().shop(roleId, 1, 4,refreshTime))
						{
							roleLoadInfo.setIsRefreshTurkShop(1);
							roleLoadInfo.setRefreshTurkShopTime(refreshTime);
							//通知客户端 异域商店已刷新
							turkShop = 1;
						}
					}
				}
				if(goldShop == 1 && turkShop == 0)
				{
					refreshShop.add(1);
				}
				else if(goldShop == 0 && turkShop == 1)
				{
					refreshShop.add(2);
				}
				else if(goldShop == 1 && turkShop == 1)
				{
					refreshShop.add(3);
				}	
			}

			if (challengeBattleInfo == null) {

				challengeBattleInfo = new ChallengeBattleInfo(sideRoleId, (byte)chapterType, chapterNo, battleNo);
				if(star != null){
					String[] stars = star.trim().split(",");
					newStar = stars.length;
					challengeBattleInfo.setStar(star);
					challengeBattleInfo.setStars(newStar);
				} else {
					challengeBattleInfo.setStar("");
					challengeBattleInfo.setStars(0);
				}
				//第一次打这个副本判断是否获得成就称谓
				TitleService.achieveTitleCheck(GameValue.APPELLATION_TYPE_COPY, battleNo, roleInfo);
				// 刷新数据库
				if (ChallengeBattleDAO.getInstance().insertChallengeBattleInfo(challengeBattleInfo)) {

					ChallengeBattleInfoMap.addInfo(challengeBattleInfo);
					// 胜利 战斗后刷新副本信息
					if (win) {
						// 有次数限制,胜利后,次数减1
						if (battleDetail.getBattleNum() > -1) {
							// 该章副本未挑战过
							int newBattleNum = battleDetail.getBattleNum() - 1;

							//记录战斗信息
							if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(),
									newBattleNum, new Timestamp(System.currentTimeMillis()))) {
								challengeBattleInfo.setCanFightNum(newBattleNum);
								challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
							}
						}
						//记录已通关的副本
						roleLoadInfo.addBattle(challengeBattleInfo.getBattleId());
					} else {
						int canFightNum = battleDetail.getBattleNum();
						//记录战斗次数
						if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(),
								canFightNum, new Timestamp(System.currentTimeMillis()))) {
							challengeBattleInfo.setCanFightNum(canFightNum);
							challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
						}
					}
				} else {
					logger.info("insertChallengeBattleInfo error! roleId = "+ sideRoleId +" BattleId:" + challengeBattleInfo.getBattleId());
				}
				ChallengeBattleInfoMap.addInfo(challengeBattleInfo);
				
				if(winSide == sideId)
				{
					//第一次打过副本时,验证时间是否异常
					challengeBattleTimeCheck(roleInfo,fightInfo,battleDetail,newStar);
				}
				
			} else {
				//刷新最短通关时间，星级
				if(star != null && star.length() > 0){
					String lastStar = challengeBattleInfo.getStar();
					int oldStar  = challengeBattleInfo.getStars();
					String[] stars = star.trim().split(",");
					newStar = stars.length;
					
					if(lastStar != null){
						if(oldStar != 3 && !lastStar.equals(star)){
							if(newStar >= oldStar){
								newStar = newStar > oldStar ? newStar : oldStar;
								// 更新星级
								if (ChallengeBattleDAO.getInstance().updateChallengeBattleInfo(star, challengeBattleInfo.getId())) {
									challengeBattleInfo.setStar(star);
									challengeBattleInfo.setStars(newStar);
								}
							}
						}
					} else {
						// 更新星级
						if (ChallengeBattleDAO.getInstance().updateChallengeBattleInfo(star, challengeBattleInfo.getId())) {
							challengeBattleInfo.setStar(star);
							challengeBattleInfo.setStars(newStar);
						}
						
						if(winSide == sideId)
						{
							//第一次打过副本时,验证时间是否异常
							challengeBattleTimeCheck(roleInfo,fightInfo,battleDetail,newStar);
						}
					}
				}
				
				// 有次数限制,胜利后,次数减1
				if (battleDetail.getBattleNum() > -1) {
					int nextBattleNum = 0;
					nextBattleNum = challengeBattleInfo.getCanFightNum();
					// 每场战斗次数独立
					if (win) {
						nextBattleNum = challengeBattleInfo.getCanFightNum() - 1;
						// 胜利后,更新次数及战斗时间
						if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(), nextBattleNum,
								new Timestamp(System.currentTimeMillis()))) {
							challengeBattleInfo.setCanFightNum(nextBattleNum);
							challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
						}
						//更新玩家通关副本
						roleLoadInfo.addBattle(challengeBattleInfo.getBattleId());
						//称号
						TitleService.achieveTitleCheck(GameValue.APPELLATION_TYPE_COPY, battleNo, roleInfo);
					}
				} else if (battleDetail.getBattleInterTime() > -1) {
					if (win) {
						// 有CD时间限制
						if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(),
								battleDetail.getBattleNum(), new Timestamp(System.currentTimeMillis()))) {
							challengeBattleInfo.setCanFightNum(battleDetail.getBattleNum());
							challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
						}
						//称号
						TitleService.achieveTitleCheck(GameValue.APPELLATION_TYPE_COPY, battleNo, roleInfo);
					}
				}
			}
			//刷新副本信息
			ChallengeService.refreshBattles(roleInfo, challengeBattleInfo, true, 0);
			
			//检测是否掉落必掉品
			if(battleDetail.getItemNo() != null && battleDetail.getItemNo().length() > 0){
				//必掉物品计数
				challengeBattleInfo.setItemByNum(challengeBattleInfo.getItemByNum() + 1);
				if(challengeBattleInfo.getItemByNum() >= GameValue.CHALLENGE_ITEM_NUM){
					challengeBattleInfo.setItemByNum(0);
					challengeBattleInfo.getItemByNo().clear();
				}
				if(prizeList != null && prizeList.size() > 0){
					//已记录的必掉品
					List<String> itemNoList = challengeBattleInfo.getItemByNo();
					
					//已掉落必掉品，添加标记
					if(challengeBattleInfo.getItemByNum() <= GameValue.CHALLENGE_ITEM_BY){
						for(BattlePrize prize : prizeList){
							String prizeNo = prize.getNo()+",";
							if(battleDetail.getItemNo().contains(prizeNo) && !itemNoList.contains(prizeNo)){
								itemNoList.add(prizeNo);
							}
						}
					}
				}
			}		
			
			// 任务触发
			boolean isRedPointQuest = false;
			if (action == ActionType.action103.getType() || (win && action == ActionType.action107.getType())) {
				isRedPointQuest = QuestService.checkQuest(roleInfo,ActionType.action103.getType(), battleDetail, true, false);
			}
			
			if (win) {
				boolean isRedPoint = RedPointMgtService.check2PopRedPoint(roleId, common, false, RedPointMgtService.LISTENING_CHALLENGE_FIGHT_END);
				//红点推送
				if(isRedPointQuest || isRedPoint){
					RedPointMgtService.pop(roleInfo.getId());
				}
				
				// 检测关卡变化对活动的影响
				OpActivityService.dealOpActProInfoCheck(roleInfo,  ActionType.action103.getType(), battleDetail, true);
				
				//判断新手引导是否要更新
				int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.FIGHT_NODES);
				if(ck != 1){
					return ck;
				}
			}
		}
		//副本日志记录
		ChallengeLog log = new ChallengeLog();
		log.setRoleId(roleInfo.getId());
		log.setRoleName(roleInfo.getRoleName());
		log.setAccount(roleInfo.getAccount());
		log.setAction(action);
		log.setChallengeNO(fightInfo.getDefendStr());
		log.setTime(new Timestamp(System.currentTimeMillis()));
		log.setStar(newStar);
		log.setStartTime(new Timestamp(fightInfo.getFightTime()));
		
		GameLogService.insertChallengeLog(log);
		
		return 1;
	}

	/**
	 * 大地图攻击NPC(真实的NPC数据)
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int createMapNPCFight(RoleInfo roleInfo, FightInfo fightInfo) {
		MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo((int) roleInfo.getId());
		if (pointInfo == null) {
			return ErrorCode.MAP_NPC_FIGHT_ERROR_1;
		}
		String[] defendStr = fightInfo.getDefendStr().split(",");
		if (defendStr.length < 2) {
			return ErrorCode.MAP_NPC_FIGHT_ERROR_2;
		}
		int mapNpcNo = NumberUtils.toInt(defendStr[0]);
		int battleType = NumberUtils.toInt(defendStr[1]);	
		MapCityXMLNPC mapNpc = SceneXmlInfoMap.getMapCityXMLNPC(mapNpcNo);
		if (mapNpc == null) {
			return ErrorCode.MAP_NPC_FIGHT_ERROR_3;
		}
		if (mapNpc.getBattleType() != battleType && mapNpc.getBattleType() != 9 && mapNpc.getBattleType() != 10) {
			return ErrorCode.MAP_NPC_FIGHT_ERROR_4;
		}

		if (mapNpc.getLevel() > HeroInfoMap.getMainHeroLv(roleInfo.getId())) {
			return ErrorCode.MAP_NPC_FIGHT_ERROR_5;
		}
		// 扣除精力
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		conds.add(new EnergyCond(mapNpc.getCostEng()));
		int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (check != 1) {
			return check;
		}
		String gwNo = mapNpc.getGw();
		if (gwNo == null || gwNo.length() <= 0 || GWXMLInfoMap.getNPCGWXMLInfo(gwNo) == null) {
			return ErrorCode.MAP_NPC_FIGHT_ERROR_4;
		}

		fightInfo.setStartRespDefendStr(gwNo + "," + battleType);

		pointInfo.setStatus((byte) 1);
		// 大地图上战斗，广播给可见自己的其它人
		SceneService1.brocastRolePointStatus(pointInfo, roleInfo);
		return 1;
	}

	/**
	 * 大地图攻击NPC(玩家镜像)
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int createMapNPC1Fight(RoleInfo roleInfo, FightInfo fightInfo) {
		MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleInfo.getId());
		if (pointInfo == null) {
			return ErrorCode.MAP_PVP_FIGHT_ERROR_9;
		}
		String[] defendStr = fightInfo.getDefendStr().split(",");
		if (defendStr.length < 2) {
			return ErrorCode.MAP_NPC_FIGHT_ERRROR_1;
		}
		int mapNpcNo = NumberUtils.toInt(defendStr[0]);
		int battleType = NumberUtils.toInt(defendStr[1]);		
		// 野外古墓与流寇需要消耗精力
		MapCityXMLNPC mapNpc = SceneXmlInfoMap.getMapCityXMLNPC(mapNpcNo);
		if (mapNpc == null) {
			return ErrorCode.MAP_NPC_FIGHT_ERROR_3;
		}
		if (mapNpc.getBattleType() != battleType && battleType != 2 && battleType != 8) {
			return ErrorCode.MAP_NPC_FIGHT_ERROR_4;
		}
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		conds.add(new EnergyCond(mapNpc.getCostEng()));
		int condCheck = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (condCheck != 1) {
			return condCheck;
		}

		int place = 1;
		FightArenaInfo arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleInfo.getId());
		if (arenaInfo != null) {
			place = arenaInfo.getPlace();
		}
		FightArenaInfo fightArena = null;
		int index = 0;
		while (fightArena == null && index < 10) {
			// 取自己竞技场名次向下1-100名内随机一个玩家的数据,无限循环,查找10次
			int randomPlace = new Random().nextInt(100) + 1;
			fightArena = FightArenaInfoMap.getRolePlaceArenaInfo(place + randomPlace);
			if (fightArena != null) {
				break;
			}
			index++;
		}

		FightSideData defendSide = null;
		if (fightArena == null) {
			// 如果没有数据，取自己当前镜像
			List<HeroInfo> heros = HeroInfoMap.getFightDeployHero(roleInfo.getId());
			if (heros != null) {
				defendSide = new FightSideData();
				for (HeroInfo heroInfo : heros) {
					if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_COMM
							|| heroInfo.getDeployStatus() > GameValue.FIGHT_ARMY_LIMIT) {
						continue;
					}

					FightArmyDataInfo armyData = FightService.getFightArmyDatabyHeroInfo(roleInfo, heroInfo,
							heroInfo.getDeployStatus(), 1, HeroProService.getProRate(GameValue.MAP_PVE_NPC_EQUIP_RATE),
							HeroProService.getProRate(GameValue.MAP_PVE_NPC_EQUIP_RATE), (byte) 1);
					if (armyData == null) {
						continue;
					}
					// 打自己镜像时,随机生成其它id,防止战斗过程产生其它问题(敌我双方CD同步了。。。。)
					armyData.setId(armyData.getId() + 1);
					if (armyData.getHeroSkills() != null) {
						for (HeroSkillDataInfo info : armyData.getHeroSkills()) {
							info.setHeroId(armyData.getId());
						}
					}
					defendSide.getArmyInfos().add(armyData);
				}
				defendSide.setFightArmyNum(defendSide.getArmyInfos().size());
			}
		} else {
			defendSide = ArenaMgtService.getDefendFightSideData(fightArena,
					HeroProService.getProRate(GameValue.MAP_PVE_NPC_EQUIP_RATE),
					HeroProService.getProRate(GameValue.MAP_PVE_NPC_EQUIP_RATE));
		}

		if (defendSide == null) {
			return ErrorCode.MAP_PVP_FIGHT_ERROR_8;
		}
		defendSide.setSideName(mapNpc.getGwName());
		defendSide.setSideId(FightType.FIGHT_SIDE_1);
		defendSide.setSideRoleId(0);

		StringBuilder sb = new StringBuilder();
		sb.append(fightInfo.getDefendStr()).append(",").append(fightArena == null ? 0 : fightArena.getRoleId());
		fightInfo.setStartRespDefendStr(sb.toString());
		fightInfo.getFightDataList().add(defendSide);
		pointInfo.setStatus((byte) 1);

		// 大地图上战斗，广播给可见自己的其它人
		SceneService1.brocastRolePointStatus(pointInfo, roleInfo);

		return 1;
	}
	
	/**
	 * 掉落检测
	 * @param dropBagList
	 * @param fightInfo
	 * @return
	 */
	public static boolean checkDrop(List<DropBagInfo> dropBagList, FightInfo fightInfo){
		Map<Integer, DropBagInfo> dropMap = fightInfo.getDropMap();
		for(DropBagInfo dropBagInfo : dropBagList){
			int no = dropBagInfo.getNo();
			DropBagInfo bagInfo = dropMap.get(no);
			if(bagInfo != null){
				List<DropInfo> dropList = dropBagInfo.getDrop();
				List<DropInfo> dropList2 = bagInfo.getDrop();
				for(DropInfo dropInfo : dropList){
					int count = 0;
					String itemNo = dropInfo.getItemNo();
					for(int i=0;i<dropList2.size();i++){

						DropInfo dropInfo2 = dropList2.get(i);
						if(dropInfo2.getItemNo().equals(itemNo)){
							if(dropInfo2.getItemNum() == dropInfo.getItemNum()){
								count = 1;
								break;
							}
						}
					}
					if(count != 1)
					{
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 第一次打过副本时,验证时间是否异常
	 * @param roleInfo
	 * @param fightInfo
	 * @param battleDetail
	 * @param star
	 */
	public static void challengeBattleTimeCheck(RoleInfo roleInfo,FightInfo fightInfo,BattleDetail battleDetail,int star)
	{
		long shortFightTime = battleDetail.getMinTime() * 1000;
		
		// 战斗时间小于推荐战力战斗时间的一半,可能异常,日志记录
		long fightTime = System.currentTimeMillis() - fightInfo.getFightTime();
		if(fightTime * 2  < shortFightTime)
		{
			String comment = "fightTime="+fightTime+",shortTime="+shortFightTime;
			GameLogService.insertChallengeUnusualLog(roleInfo.getId(), roleInfo.getRoleName(), roleInfo.getAccount(), 
					fightInfo.getDefendStr(), fightInfo.getFightTime(), System.currentTimeMillis(), 3,comment);
		}
	}
	
	
	/**
	 * 构造副本BOSS属性
	 * @param fightInfo
	 */
	public static void generateBossProp(FightInfo fightInfo,int npcNo)
	{
		NPCXmlInfo npcXml = NPCXmlLoader.getNpc(npcNo);
		if(npcXml == null)
		{
			return;
		}
		
		/*PowerXmlInfo powXml = PowerXmlLoader.getPower(npcXml.getPower());
		if(powXml == null)
		{
			return;
		}*/
		
		FightArmyDataInfo bossArmyDataInfo = new FightArmyDataInfo();
		bossArmyDataInfo.setHeroNo(npcXml.getNo());
		bossArmyDataInfo.setHp(npcXml.getHp());
		bossArmyDataInfo.setAd(npcXml.getAd());
		bossArmyDataInfo.setAttack(npcXml.getAttack());
		bossArmyDataInfo.setAttackDef(npcXml.getAttackDef());
		bossArmyDataInfo.setMagicAttack(npcXml.getMagicAttack());
		bossArmyDataInfo.setMagicDef(npcXml.getMagicDef());
		
		
		fightInfo.setBossArmyDataInfo(bossArmyDataInfo);
	}
	
	/**
	 * 构造BOSS属性
	 * @param fightInfo
	 */
	public static void generateWorldBossProp(FightInfo fightInfo,long hp)
	{
		FightArmyDataInfo bossArmyDataInfo = new FightArmyDataInfo();
		bossArmyDataInfo.setBossHp(hp);
		
		
		fightInfo.setBossArmyDataInfo(bossArmyDataInfo);
	}
	
	/**
	 * 检查双方是否有黄月英开启流马技能
	 * @param fightReq
	 * @return
	 */
	public static boolean checkHeroInArmy(IntoFightResp fightReq)
	{
		List<FightSideData> sizeList = fightReq.getSizeList();
		if(sizeList != null && sizeList.size() >0)
		{
			for(FightSideData sideData : sizeList)
			{
				if(sideData == null)
				{
					continue;
				}
				
				List<FightArmyDataInfo> armyInfos = sideData.getArmyInfos();
				for(FightArmyDataInfo armyInfo : armyInfos)
				{
					if(armyInfo != null && armyInfo.getHeroNo() == 35000022)
					{
						List<HeroSkillDataInfo> heroSkills = armyInfo.getHeroSkills();
						if(heroSkills != null && heroSkills.size() > 0)
						{
							for(HeroSkillDataInfo skillData : heroSkills)
							{
								if(skillData != null && skillData.getSkillNo() == 53000222)
								{
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
}
