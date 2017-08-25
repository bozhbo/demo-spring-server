package com.snail.webgame.game.protocal.fight.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.FightInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.ArmyFightingInfo;
import com.snail.webgame.game.common.fightdata.DropBagInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.fightdata.ServerFightEndReq;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.SkillInfoLoader;
import com.snail.webgame.game.common.xml.info.SkillXmlInfo;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.activity.service.ActivityService;
import com.snail.webgame.game.protocal.arena.service.ArenaMgtService;
import com.snail.webgame.game.protocal.attackAnother.service.AttackAnotherMgtService;
import com.snail.webgame.game.protocal.campaign.service.CampaignMgtService;
import com.snail.webgame.game.protocal.defend.service.DefendMgtService;
import com.snail.webgame.game.protocal.fight.checkFight.CheckFightReq;
import com.snail.webgame.game.protocal.fight.checkFight.CheckPropInfo;
import com.snail.webgame.game.protocal.fight.checkFight.CheckSkillInfo;
import com.snail.webgame.game.protocal.fight.fightOut.FightOutResp;
import com.snail.webgame.game.protocal.fight.fightTime.FightTimeResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.fightend.FightEndResp;
import com.snail.webgame.game.protocal.fight.startFight.IntoFightResp;
import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;
import com.snail.webgame.game.protocal.fight.startFight.StartFightReq;
import com.snail.webgame.game.protocal.fight.startFight.StartFightResp;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.mine.service.MineMgtService;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.protocal.worldBoss.service.WorldBossMgtService;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.cache.InstanceStarXMLMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.InstanceStarXMLInfo;

public class FightMgtService {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	/**
	 * 修改战斗中部队位置，并开始战斗 PVE战斗时,我方数据服务器传送,对方如果为人时也需自己传送
	 * @param roleId
	 * @param req
	 * @return
	 */
	public StartFightResp startFight(int roleId, StartFightReq req) {
		StartFightResp resp = new StartFightResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.GEM_FIGHT_END_ERROR_6);
			return resp;
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			
			if(roleLoadInfo != null && roleLoadInfo.getYabiaoFriendRoleId() > 0)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_37);
				return resp;
			}
			
			FightType fightType = FightType.getFightType(req.getFightType());// 玩法类型
			if (fightType == null) {
				resp.setResult(ErrorCode.GEM_FIGHT_END_ERROR_6);
				return resp;
			}
			String defendStr = req.getDefendStr();
			List<StartFightPosInfo> chgPosInfos = req.getChgPosInfos();
			
			//背包判断
			int itemCheck = ItemService.addItemAndEquipCheck(roleInfo);
			if(itemCheck != 1){
				resp.setResult(ErrorCode.FIGHT_ERROR_4);
				return resp;
			}

			int checkFightResult = ErrorCode.GEM_FIGHT_END_ERROR_6;
			FightInfo fightInfo = FightInfoMap.getFightInfoByRoleId(roleId);
			if(fightInfo != null){
				resp.setResult(ErrorCode.FIGHT_ERROR_6);
				return resp;
			}
					
			fightInfo = new FightInfo(fightType, roleId);
			fightInfo.setDefendStr(defendStr);
			fightInfo.setStartRespDefendStr(defendStr);
			fightInfo.setEndRespDefendStr(defendStr);
			fightInfo.setChgPosInfos(chgPosInfos);

			switch (fightType) {
			case FIGHT_TYPE_1:// 主线副本
				checkFightResult = CreateFightInfoService.createChallengeFightInfo(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_2:// 竞技场
				checkFightResult = ArenaMgtService.dealFightStart(roleInfo, fightInfo);
				break;
//			case FIGHT_TYPE_3:// 宝石活动
//				checkFightResult = GemMgtService.dealFightStart(roleInfo, fightInfo);
//				if(checkFightResult == 1){
//					GameLogService.insertPlayActionLog(roleInfo, GameAction.GAME_GEM_ACTION_4, "");
//				}else{
//					GameLogService.insertPlayActionLog(roleInfo, GameAction.GAME_GEM_ACTION_4, 2, "");
//				}
//				break;
			case FIGHT_TYPE_4:// 练兵经验活动
				checkFightResult = ActivityService.dealExpFightStart(roleInfo, fightInfo);
				break;
//			case FIGHT_TYPE_5:// 小偷银币活动
//				checkFightResult = ActivityService.dealMoneyFightStart(roleInfo, fightInfo);
//				if(checkFightResult == 1){
//					GameLogService.insertPlayActionLog(roleInfo, GameAction.GAME_MONEY_ACTIVITY_ACTION_1, "");
//				}else{
//					GameLogService.insertPlayActionLog(roleInfo, GameAction.GAME_MONEY_ACTIVITY_ACTION_1, 2, "");
//				}
//				break;
			case FIGHT_TYPE_6:// 宝物活动
				checkFightResult = CampaignMgtService.dealFightStart(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_7:// 大地图攻击NPC(真实的NPC数据)
				checkFightResult = CreateFightInfoService.createMapNPCFight(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_8:// 大地图攻击NPC(玩家镜像)
				checkFightResult = CreateFightInfoService.createMapNPC1Fight(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_10:// 防守关卡
				checkFightResult = CreateFightInfoService.createChallengeFightInfo(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_11:// 对攻战(玩家镜像)
				checkFightResult = AttackAnotherMgtService.dealStartFight(roleInfo, fightInfo, NumberUtils.toInt(defendStr));
				break;
			case FIGHT_TYPE_12:// 活动防守
				checkFightResult = DefendMgtService.dealStartFight(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_13:// 对攻关卡
				checkFightResult = CreateFightInfoService.createChallengeFightInfo(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_15:// 世界BOSS
				checkFightResult = WorldBossMgtService.dealStartFight(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_18:// 世界地图开矿
				checkFightResult = MineMgtService.dealFightStart(roleInfo, fightInfo);
				break;
			case FIGHT_TYPE_19:// 世界地图抢矿
				checkFightResult = MineMgtService.dealFightStart(roleInfo, fightInfo);
				break;
			default:
				break;
			}
			
			if (checkFightResult != 1) {
				resp.setResult(checkFightResult);
				return resp;
			}
			// sendIntoFight
			checkFightResult = sendIntoFight(roleInfo, fightInfo);
			if (checkFightResult != 1) {
				resp.setResult(checkFightResult);
				return resp;
			}
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 战斗数据赋值，发送进入战斗场景
	 * @param roleInfo
	 * @param fightInfo
	 */
	public static int sendIntoFight(RoleInfo roleInfo,FightInfo fightInfo){
		FightType fightType = fightInfo.getFightType();
		// PVE战斗时,我方数据服务器传送,对方如果为人时也需服务器传送
		// 攻击部队
		if (fightType != FightType.FIGHT_TYPE_6 && fightType != FightType.FIGHT_TYPE_4
				&& fightType != FightType.FIGHT_TYPE_18 && fightType != FightType.FIGHT_TYPE_19) {
			// 默认我方部队
			FightSideData attSide = CreateFightInfoService.getAttFightSideData(roleInfo,fightType);
			if (attSide == null) {
				return ErrorCode.GEM_FIGHT_END_ERROR_7;
			}
			fightInfo.getFightDataList().add(attSide);
		}
		
		// 偏将,副将第一技能传给主将
		FightService.changeArmyFightDataSkill(fightInfo.getFightDataList());
		
		// 士兵技能,神兵技能
		FightService.addSoldierSkillAndMagic(roleInfo,fightInfo.getFightDataList());


		// 改动战斗缓存
		FightInfoMap.addFightInfo(roleInfo, fightInfo);
		//删除原有的战斗结算缓存
		FightInfoMap.removeFightResultByRoleId(roleInfo.getId());

		IntoFightResp fightReq = new IntoFightResp();
		fightReq.setResult(1);
		fightReq.setId(fightInfo.getFightId());
		fightReq.setMapType("");
		fightReq.setFightType(fightInfo.getFightType().getValue());
		fightReq.setDefendStr(fightInfo.getStartRespDefendStr());

		List<FightSideData> sizeList = fightInfo.getFightDataList();
		fightReq.setSizeNum(sizeList.size());
		fightReq.setSizeList(sizeList);
		
		Map<Integer, DropBagInfo> dropMap = fightInfo.getDropMap();
		if(dropMap != null && dropMap.size() > 0){
			List<DropBagInfo> dropList = new ArrayList<DropBagInfo>(dropMap.values());
			
			fightReq.setDropCount(dropList.size());
			fightReq.setDropList(dropList);
		}
		
		
		//黄月英的流马暂时过率
		if(fightInfo.getCheckFlag() == 1 && CreateFightInfoService.checkHeroInArmy(fightReq))
		{
			fightReq.setCheckFlag(fightInfo.getCheckFlag());
		}
		else
		{
			fightInfo.setCheckFlag((byte) 0);
		}


		// 向客户端发送进入战斗场景
		FightService.sendIntoFight(roleInfo, fightReq);

		// 战斗如果在内城场景就移除
		SceneMgtService.AIDisapperForMoment(roleInfo.getId());
		
		//将我方技能，部队归类，方便后期检测
		CheckPVEService.setSide0DataAllSkill(fightReq,fightInfo);
		return 1;
	}
	
	/**
	 * 战斗结束处理
	 * @param roleId
	 * @param fightEndReq
	 * @return
	 */
	public FightEndResp battleEnd(int roleId, ServerFightEndReq fightEndReq) {
		FightEndResp resp = new FightEndResp();
		if (fightEndReq == null) {
			// 战斗数据传输异常
			resp.setResult(ErrorCode.FIGHT_ERROR_5);
			return resp;
		}
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.FIGHT_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			long fightId = fightEndReq.getFightId();
			FightInfo fightInfo = FightInfoMap.getFightInfoByRoleId(roleId);
			if (fightInfo == null) {
				FightEndResp resp1 = FightInfoMap.getFightResultByFightId(roleId, fightId);
				if (resp1 != null) {
					return resp1;
				}
				resp.setResult(ErrorCode.FIGHT_ERROR_10);
				return resp;
			}
			if (fightInfo.getFightTime() <= 0) {
				resp.setResult(ErrorCode.FIGHT_ERROR_11);
				return resp;
			}
			if (fightInfo.getRoleId() != roleId || fightInfo.getFightId() != fightId) {
				resp.setResult(ErrorCode.FIGHT_ERROR_12);
				return resp;
			}
			// 获得胜利方
			fightInfo.setWinSide(fightEndReq.getWinSide());
			fightInfo.setArmyFightingInfos(fightEndReq.getArmyFightingInfos());

			List<Integer> starResult = new ArrayList<Integer>();
			List<BattlePrize> prizeList = new ArrayList<BattlePrize>();
			List<BattlePrize> fpPrizeList = new ArrayList<BattlePrize>();
			List<Integer> refreshShop = new ArrayList<Integer>();			
			
			int result = fightEndHandle(resp, roleInfo, fightInfo, fightEndReq, starResult, prizeList, fpPrizeList,
					refreshShop);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			resp.setResult(1);
			resp.setFightId(fightInfo.getFightId());
			resp.setFightType((byte) fightInfo.getFightType().getValue());
			resp.setParam(fightInfo.getEndRespDefendStr());
			resp.setPrizeNum(prizeList.size());
			resp.setPrize(prizeList);
			resp.setFpPrize(fpPrizeList);
			resp.setFpPrizeNum(fpPrizeList.size());
			if (refreshShop.size() > 0) {
				resp.setRefreshShop(refreshShop.get(0) == null ? 0 : refreshShop.get(0));
			}
			// 日志
			GameLogService.insertInstanceLog(roleInfo.getId(), fightInfo);
			
			// 移除缓存
			FightInfoMap.removeFightInfoByRoleId(roleInfo.getId());
			// 添加战斗结算缓存
			FightInfoMap.addFightResult(roleInfo, resp);
			
			if(fightInfo.getCheckFlag() == 1)
			{
				logger.info("########fightEndHandle,fightId="+fightInfo.getFightId()+",checkNum1="+fightInfo.getCheckNum1()+",checkNum2="+fightInfo.getCheckNum2()+
						",checkNum3="+fightInfo.getCheckNum3()+",errorCheckNum="+fightInfo.getCheckErrorNum()+
						",fightType="+fightInfo.getFightType().getValue()+",roleName="+roleInfo.getRoleName()+",bossHurt="+fightInfo.getBossHurt());
			}
			
			
			return resp;
		}
	}

	/**
	 * 战斗结束处理
	 * @param roleInfo
	 * @param fightInfo
	 * @param fightEndReq
	 * @param prizeList
	 * @return
	 */
	private int fightEndHandle(FightEndResp resp, RoleInfo roleInfo, FightInfo fightInfo,
			ServerFightEndReq fightEndReq, List<Integer> starResult, List<BattlePrize> prizeList,
			List<BattlePrize> fpPrizeList, List<Integer> refreshShop) {
		
		// 评星处理
		if (fightInfo.getFightType() == FightType.FIGHT_TYPE_1 || fightInfo.getFightType() == FightType.FIGHT_TYPE_10
				|| fightInfo.getFightType() == FightType.FIGHT_TYPE_13) {
			if (fightEndReq.getFightTime() == 0) {
				// 战斗数据传输异常
				return ErrorCode.FIGHT_ERROR_5;
			}
			
			List<FightSideData> sizeList = fightInfo.getFightDataList();
			if (sizeList == null || sizeList.size() <= 0) {
				// 找不到对阵双方的数据
				logger.error("battle end error!,battle side info null!");
				return ErrorCode.FIGHT_ERROR_2;
			}
			// 战斗结束评星结果
			starResult = battleEndResult(fightEndReq, sizeList, fightInfo);
			if(starResult == null || starResult.size() == 0){
				logger.error("battle end error! star is null");
				return ErrorCode.FIGHT_ERROR_5;
			}
		} else {
			starResult.add(0);
			starResult.add(0);
			starResult.add(0);
			starResult.add(1);
			starResult.add(2);
			starResult.add(3);
		}
		
		//遇到BOSS值就被篡改,很有问题，战斗失败
		if(fightInfo.getCheckFlag() == 1 && fightInfo.getBossError() == 2)
		{
			if(GameValue.PVE_BOSS_ERROR_FLAG == 1)
			{
				starResult.clear();
				fightEndReq.setWinSide(FightType.FIGHT_SIDE_1);
			}
			String comment = fightInfo.getDefendStr();
			GameLogService.insertChallengeUnusualLog(roleInfo.getId(), roleInfo.getRoleName(), roleInfo.getAccount(), fightInfo.getDefendStr(), fightInfo.getFightTime(), System.currentTimeMillis(), 4,comment);
		}
		
		
		//战斗秒结算或客户端屏蔽验证
		if(fightInfo.getCheckFlag() == 1)
		{
			//查看验证失败率
			int totalCheckNum1 = fightInfo.getCheckNum1();
			int totalCheckNum2 = fightInfo.getCheckNum2();
			int totalCheckNum3 = fightInfo.getCheckNum3();
			
			int totalErrorCheckNum = fightInfo.getCheckErrorNum();
			
			if(totalCheckNum1 + totalCheckNum2 + totalCheckNum3 < 1)
			{
				if(GameValue.PVE_LI_MA_WIN_FLAG == 1)
				{
					starResult.clear();
					fightEndReq.setWinSide(FightType.FIGHT_SIDE_1);
				}
				
				String comment = totalCheckNum1+","+totalCheckNum2+","+totalCheckNum3;
				
				GameLogService.insertChallengeUnusualLog(roleInfo.getId(), roleInfo.getRoleName(), roleInfo.getAccount(), fightInfo.getDefendStr(), fightInfo.getFightTime(), System.currentTimeMillis(), 2,comment);
			}
			else
			{
				if(totalErrorCheckNum/(totalCheckNum1+totalCheckNum2+totalCheckNum3) >= 0.3f || (totalCheckNum1+totalCheckNum2+totalCheckNum3) < GameValue.CHECK_FIGHT_NUM)
				{
					if(GameValue.FIGHT_CHECK_FLAG == 1)
					{
						starResult.clear();
						fightEndReq.setWinSide(FightType.FIGHT_SIDE_1);
					}
					
					String comment = totalErrorCheckNum+","+totalCheckNum1+","+totalCheckNum2+","+totalCheckNum3;
					
					GameLogService.insertChallengeUnusualLog(roleInfo.getId(), roleInfo.getRoleName(), roleInfo.getAccount(), fightInfo.getDefendStr(), fightInfo.getFightTime(), System.currentTimeMillis(), 1,comment);
				}
			}
		}
		
		String stars = resp.addStar(starResult);
		int fightResult = getFightEndResult(roleInfo, fightInfo, fightEndReq);
		FightType fightType = fightInfo.getFightType();// 战场类型
		switch (fightType) {
		case FIGHT_TYPE_1:// 主线副本
			return CreateFightInfoService.opChallengeBattleEnd(roleInfo, fightEndReq,
					fightInfo, stars, prizeList, fpPrizeList, refreshShop);
		case FIGHT_TYPE_2:// 竞技场
			return ArenaMgtService.dealFightEnd(ActionType.action142.getType(), fightResult, roleInfo, fightInfo,
					prizeList, fpPrizeList);
			// case FIGHT_TYPE_3:// 宝石活动
			// return GemMgtService.dealFightEnd(fightResult, roleInfo,
			// fightInfo,prizeList);
		case FIGHT_TYPE_4:// 练兵经验活动
			return ActivityService.dealExpFightEnd(fightResult, roleInfo, fightInfo, fightEndReq, prizeList);
			// case FIGHT_TYPE_5:// 小偷银币活动
			// return ActivityService.dealMoneyFightEnd(fightResult, roleInfo,
			// fightInfo,prizeList);
		case FIGHT_TYPE_6:// 攻城略地
			return CampaignMgtService.dealFightEnd(fightResult, roleInfo, fightInfo, prizeList, fpPrizeList);
		case FIGHT_TYPE_7:// 大地图攻击NPC(真实的NPC数据)
			return SceneService1.mapPVEFightEndHandle(ActionType.action27.getType(), fightResult, roleInfo, fightInfo,
					prizeList);
		case FIGHT_TYPE_8:// 大地图攻击NPC(玩家镜像)
			return SceneService1.mapPVEFightEndHandle(ActionType.action29.getType(), fightResult, roleInfo, fightInfo,
					prizeList);
		case FIGHT_TYPE_10:// 关卡防守
			return CreateFightInfoService.opChallengeBattleEnd(roleInfo, fightEndReq,
					fightInfo, stars, prizeList, fpPrizeList, refreshShop);
		case FIGHT_TYPE_11:// 匹配 对攻战(玩家镜像)
			return AttackAnotherMgtService.dealFightEnd(fightResult, roleInfo, prizeList,
					NumberUtils.toInt(fightInfo.getDefendStr()), fightInfo.getFightTime());
		case FIGHT_TYPE_12:// 活动防守玩法
			return DefendMgtService.dealFightEnd(fightResult, fightEndReq.getReserve(), roleInfo, fightInfo, prizeList);
		case FIGHT_TYPE_13:// 关卡对攻战
			return CreateFightInfoService.opChallengeBattleEnd(roleInfo, fightEndReq,
					fightInfo, stars, prizeList, fpPrizeList, refreshShop);
		case FIGHT_TYPE_15:// 世界boss
			return WorldBossMgtService.worldBossEnd(roleInfo, fightEndReq, fightInfo, prizeList);
		case FIGHT_TYPE_18:// 世界地图开矿
			return MineMgtService.dealFightEnd(ActionType.action452.getType(), fightResult, roleInfo, fightInfo,
					prizeList);
		case FIGHT_TYPE_19:// 世界地图抢矿
			return MineMgtService.dealFightEnd(ActionType.action454.getType(), fightResult, roleInfo, fightInfo,
					prizeList);
		default:
			return ErrorCode.FIGHT_ERROR_3;
		}
	}
	
	/**
	 * 获取战斗结果
	 * @param roleInfo
	 * @param fightInfo
	 * @param fightEndReq
	 * @return 1-胜 2-败
	 */
	private int getFightEndResult(RoleInfo roleInfo, FightInfo fightInfo, ServerFightEndReq fightEndReq) {
		byte winSide = fightEndReq.getWinSide();
		if (fightInfo.getFightType() == FightType.FIGHT_TYPE_7) {
			// 战斗超时判断
			int fightTime = fightEndReq.getFightTime();
			long fightTimeLimit = FightType.getFightTimeLimit(fightInfo.getFightType());
			if (fightTime != 0 && fightTime >= fightTimeLimit) {
				// 战斗超时
				winSide = FightType.FIGHT_SIDE_1;
			} else {		
				List<ArmyFightingInfo> fightEndReqSide = fightEndReq.getArmyFightingInfos();
				List<FightSideData> sizeList = fightInfo.getFightDataList();
				if (fightEndReqSide != null && fightEndReqSide.size() > 0 && sizeList != null && sizeList.size() > 0) {
					long mainId = 0;
					for (FightSideData fightSideData : sizeList) {
						if (fightSideData.getSideId() == FightType.FIGHT_SIDE_0) {
							for (FightArmyDataInfo data : fightSideData.getArmyInfos()) {
								if (data.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
									mainId = data.getId();
									break;
								}
							}
						}
					}
					for (ArmyFightingInfo endSide : fightEndReqSide) {
						if (endSide.getSideId() == FightType.FIGHT_SIDE_0) { // 攻方
							if (mainId == endSide.getId()) {
								if (endSide.getCurrentHp() <= 0) {
									winSide = FightType.FIGHT_SIDE_1;
								} else {
									winSide = FightType.FIGHT_SIDE_0;
								}
								break;
							}
						}
					}
				}
			}
		}
		return winSide == (byte) FightType.FIGHT_SIDE_0 ? 1 : 2;
	}

	/**
	 * 战斗结束评级（PVE战斗）
	 * @param sizeList 进战斗前部队信息
	 */
	private List<Integer> battleEndResult(ServerFightEndReq fightEndReq, List<FightSideData> oldFightDataList,
			FightInfo fightInfo) {
		//战斗时间
		int fightTime = fightEndReq.getFightTime();
		// 战斗结束传过来的部队信息
		List<ArmyFightingInfo> fightEndReqSide = fightEndReq.getArmyFightingInfos();
		List<Integer> result = new ArrayList<Integer>();
		if (fightEndReqSide == null || fightEndReqSide.size() <= 0) {
			return result;
		}
		int endTime = 0;
		int battleType = 0;
		boolean isFull = false;
		//副本类型
		String pamarater = fightInfo.getStartRespDefendStr();
		if (pamarater == null) {
			return result;
		}
		String[] challengeChapters = pamarater.split(",");
		if (challengeChapters.length != 3) {
			return result;
		}
		//副本编号 类型，章节，关卡
		byte chapterType = Byte.valueOf(challengeChapters[0]);
		int chapterNo = Integer.valueOf(challengeChapters[1]);
		int battleNo = Integer.valueOf(challengeChapters[2]);

		BattleDetail battleDetail = ChallengeBattleXmlInfoMap.getBattleDetail(chapterType, chapterNo, battleNo);
		if (battleDetail != null) {
			endTime = battleDetail.getEndTime();
			battleType = battleDetail.getBattleType();
		}
		
		if(fightInfo.getFightType() == FightType.FIGHT_TYPE_10 || fightInfo.getFightType() == FightType.FIGHT_TYPE_13)
		{
			if(fightInfo.getWinSide() == 1)
			{
				fightEndReq.setWinSide(FightType.FIGHT_SIDE_1);// 防守方胜利
				if (battleDetail != null) {
					result.add(battleDetail.getStarType1());
					result.add(battleDetail.getStarType2());
					result.add(battleDetail.getStarType3());
				}
				
				return result;
			}
		}
		
		Map<Long, ArmyFightingInfo> fightEndSideMap1 = new HashMap<Long, ArmyFightingInfo>();//攻方
		Map<Long, ArmyFightingInfo> fightEndSideMap2 = new HashMap<Long, ArmyFightingInfo>();//守方
		
		for (ArmyFightingInfo endSide : fightEndReqSide) {
			if (endSide.getSideId() == FightType.FIGHT_SIDE_0) { // 攻方
				fightEndSideMap1.put(endSide.getId(), endSide);
			} else if (endSide.getSideId() == FightType.FIGHT_SIDE_1) { // 守方
				fightEndSideMap2.put(endSide.getId(), endSide);
			}
		}
		double allArmyLeftTotalHp = 0;// 己方主将剩余血量
		int deadOurNPCNum = 0; // 己方NPC死亡数量
		
		//double allArmyRightTotalHp = 0;//敌方剩余血量
		// 攻击方计算
		// 进战斗前部队信息
		if (oldFightDataList != null && oldFightDataList.size() > 0) {
			for (FightSideData fightSideData : oldFightDataList) {

				int sideId = fightSideData.getSideId();
				List<FightArmyDataInfo> armyList = fightSideData.getArmyInfos();
				for (FightArmyDataInfo armyInfo : armyList) {
					// hp计算
					//float hp = armyInfo.getHp();
					long id = armyInfo.getId();
					// 战斗结束信息，获取的部队信息
					if(sideId == FightType.FIGHT_SIDE_0){
						if(armyInfo.getDeployStatus() == 1){
							//己方主将
							ArmyFightingInfo endSideArmyInfo = fightEndSideMap1.get(armyInfo.getId());
							if (endSideArmyInfo != null) {
								fightEndSideMap1.remove(id);
								double leftHp = endSideArmyInfo.getCurrentHp() * 100;
								if (leftHp >= 0) {					
									allArmyLeftTotalHp += leftHp;
									if(leftHp >= 100){
										isFull = true;
									}
								}
							}
						} else {
							//己方副将
							ArmyFightingInfo endSideArmyInfo = fightEndSideMap1.get(id);
							if (endSideArmyInfo != null) {
								fightEndSideMap1.remove(id);
							}
						}
					}
				}
				// 己方NPC
				if (fightEndSideMap1.size() > 0) {
					// 护送任务
					if (battleType == 5) {
						for (ArmyFightingInfo armyInfo : fightEndSideMap1.values()) {
							if (armyInfo.getNPCType() != 0 && armyInfo.getCurrentHp() * 100 <= 0) {
								deadOurNPCNum++;
							}
						}
					}
				}
			}
		} else {
			logger.info("oldFightDataList error! roleId = "+ fightInfo.getRoleId() +" BattleId:" + fightInfo.getFightId());
		}
		//主将没血必定失败
		if(allArmyLeftTotalHp <= 0){
			if(fightInfo.getFightType() != FightType.FIGHT_TYPE_10 && fightInfo.getFightType() != FightType.FIGHT_TYPE_13)
			{
				fightEndReq.setWinSide(FightType.FIGHT_SIDE_1);// 防守方胜利
				if (battleDetail != null) {
					result.add(battleDetail.getStarType1());
					result.add(battleDetail.getStarType2());
					result.add(battleDetail.getStarType3());
				}
				return result;
			}
		}
		
		//时间计算
		if (fightTime != 0 && endTime > fightTime) {

			if (battleType != 5) {
				//非护送任务
				fightEndReq.setWinSide(FightType.FIGHT_SIDE_0);// 进攻方胜利
				//评星
				result = battleStar(fightEndReq, fightInfo, battleDetail, fightEndReqSide,isFull);
				return result;
			} else {
				 // 护送任务
				if (deadOurNPCNum <= 0) {
					fightEndReq.setWinSide(FightType.FIGHT_SIDE_0);
				} else {
					fightEndReq.setWinSide(FightType.FIGHT_SIDE_1);
					result.add(battleDetail.getStarType1());
					result.add(battleDetail.getStarType2());
					result.add(battleDetail.getStarType3());
				}
				if (fightEndReq.getWinSide() == FightType.FIGHT_SIDE_0) {
					// 评星
					result = battleStar(fightEndReq, fightInfo, battleDetail, fightEndReqSide,isFull);
					return result;
				}
			}
		} else {
			logger.info("###battleEndResult fight end ,fight time error,fightTime="+fightTime+",endTime="+endTime);
			fightEndReq.setWinSide(FightType.FIGHT_SIDE_1);// 失败
			result.add(battleDetail.getStarType1());
			result.add(battleDetail.getStarType2());
			result.add(battleDetail.getStarType3());
			
		}
		return result;
	}
	
	/**
	 * 主线副本战后评星（胜负）特殊处理
	 * @param fightInfo //战前
	 * @param fightEndSideList //战后
	 * @param type 0-普通战役 1-护送战斗
	 * @return
	 */
	public static List<Integer> battleStar(ServerFightEndReq fightEndReq, FightInfo fightInfo,BattleDetail battleDetail, 
			List<ArmyFightingInfo> fightArmyList, boolean isFull) {
		List<Integer> result = new ArrayList<Integer>();
		List<Integer> content = new ArrayList<Integer>();
		if (fightArmyList == null || fightArmyList.size() <= 0) {
			return null;
		}
		Map<Long, ArmyFightingInfo> fightEndSideMap1 = new HashMap<Long, ArmyFightingInfo>();//攻方
		Map<Long, ArmyFightingInfo> fightEndSideMap2 = new HashMap<Long, ArmyFightingInfo>();//守方
		
		for (ArmyFightingInfo endSide : fightArmyList) {
			if (endSide.getSideId() == FightType.FIGHT_SIDE_0) { // 攻方
				fightEndSideMap1.put(endSide.getId(), endSide);
			} else if (endSide.getSideId() == FightType.FIGHT_SIDE_1) { // 守方
				fightEndSideMap2.put(endSide.getId(), endSide);
			}
		}
		if (battleDetail != null) {
			//获得评星类型
			int starType1 = battleDetail.getStarType1();// 评星
			int starType2 = battleDetail.getStarType2();// 评星
			int starType3 = battleDetail.getStarType3();// 评星
			
			//根据评星类型获得对应InstanceStar.XML中的评星条件
			InstanceStarXMLInfo xmlInfo1 = InstanceStarXMLMap.getInstanceStar(starType1);
			InstanceStarXMLInfo xmlInfo2 = InstanceStarXMLMap.getInstanceStar(starType2);
			InstanceStarXMLInfo xmlInfo3 = InstanceStarXMLMap.getInstanceStar(starType3);
			if(xmlInfo1 == null || xmlInfo2 == null || xmlInfo3 == null){
				return null;
			}
			
			List<InstanceStarXMLInfo> starList = new ArrayList<InstanceStarXMLInfo>();
			starList.add(xmlInfo1);
			starList.add(xmlInfo2);
			starList.add(xmlInfo3);
			//计算评星
			for(int i=0;i<starList.size();i++){
				InstanceStarXMLInfo xmlInfo = starList.get(i);
				
				content.add(xmlInfo.getNo());
				if(xmlInfo.getType() == 1){
					//胜利
					if (fightEndReq.getWinSide() == FightType.FIGHT_SIDE_0)// 进攻方胜利
					{
						result.add(FightType.FIGHT_RESULT_WIND + i);
					}
				} else if(xmlInfo.getType() == 2) {
					//主将满血过关
					if(isFull){
						result.add(FightType.FIGHT_RESULT_WIND + i);	
					}
				} else if(xmlInfo.getType() == 3) {
					int allArmyNum = 0;// 己方部队数量
					int allAliveArmyNum = 0;// 活着己方活着的数量
					
					for (ArmyFightingInfo info : fightEndSideMap1.values()) {
							if (info.getCurrentHp() * 100 > 0) {
								// currentHp = info.getCurrentHp();
								allAliveArmyNum++;// 统计己方活着的单位
							}
							allArmyNum++;
					}
					//阵亡部队数
					int param = Integer.parseInt(xmlInfo.getParam());
					// 仅损失N支部队
					if (allArmyNum - allAliveArmyNum <= param) {
						result.add(FightType.FIGHT_RESULT_WIND + i);	
					}
				} else if(xmlInfo.getType() == 4) {
					//三杀
					int threeKill = fightEndReq.getThreeKill();
					int param = Integer.parseInt(xmlInfo.getParam());
					
					if(threeKill >= param){
						result.add(FightType.FIGHT_RESULT_WIND + i);	
					}
				} else if(xmlInfo.getType() == 5) {
					//斩杀敌人
					int killenemyNum = 0; // 杀死敌人数量
					int killNum = fightEndReq.getKillNum();
					if(battleDetail.getBattleType() == 8)
					{
						killenemyNum = killNum;
					}
					else
					{
						for (ArmyFightingInfo info : fightEndSideMap2.values()) {
							if (info.getCurrentHp() * 100 <= 0) {
								if(battleDetail.getBattleType() != 8)
								{
									killenemyNum++; // 杀死的敌人
								}

							}
						}
					}

					int param = Integer.parseInt(xmlInfo.getParam());
					if (killenemyNum > param) {
						result.add(FightType.FIGHT_RESULT_WIND + i);	
					}
				} else if(xmlInfo.getType() == 6) {
					//限时
					int param = Integer.parseInt(xmlInfo.getParam());
					int fightTime = fightEndReq.getFightTime();
					
					if (fightTime <= param) {
						result.add(FightType.FIGHT_RESULT_WIND + i);	
					}
				} else if(xmlInfo.getType() == 7) {
					//建筑物
					int type = Integer.parseInt(xmlInfo.getParam());
					int num = xmlInfo.getNum();
					int value = xmlInfo.getValue();
					int size = 0;
					
					for(ArmyFightingInfo info : fightEndSideMap1.values()){
						if(info.getNPCType() == type && info.getCurrentHp() * 100 > value ){
							size++;
						}
					}
					if(size >= num){
						result.add(FightType.FIGHT_RESULT_WIND + i);	
					}
					
				} else if(xmlInfo.getType() == 8) {
					//突袭兵
					int type = Integer.parseInt(xmlInfo.getParam());
					int num = xmlInfo.getNum();
					int value = xmlInfo.getValue();
					int size = 0;
					
					for(ArmyFightingInfo info : fightEndSideMap2.values()){
						if(info.getNPCType() == type && info.getCurrentHp() == (float)value/100){
							size++;
						}
					}
					if(size >= num){
						result.add(FightType.FIGHT_RESULT_WIND + i);	
					}
					
				}  else if(xmlInfo.getType() == 9) {
					//指定NPC
					String param = xmlInfo.getParam();
					boolean isStar = false;
					
					for(ArmyFightingInfo info : fightEndSideMap2.values()){
						if(info.getNPCno().equals(param) && info.getCurrentHp() * 100 <= 0){
							isStar = true;
							break;
						}
					}
					if(isStar){
						result.add(FightType.FIGHT_RESULT_WIND + i);	
					}
					
				}
			}
			
			content.addAll(result);
			
		}
		return content;
	}
	/**
	 * 获得倒计时时间
	 * @param roleId
	 */
	public FightTimeResp battleTime(int roleId) {
		FightTimeResp resp = new FightTimeResp();
		int time = 3;
		FightInfo fightInfo = FightInfoMap.getFightInfoByRoleId(roleId);
		if (fightInfo != null) {
			fightInfo.setFightTime(System.currentTimeMillis());
			if(fightInfo.getFightType() == FightType.FIGHT_TYPE_1){
				resp.setFightTime(1);
			} else {
				resp.setFightTime(time);
			}
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 战斗中途退出
	 * @param roleId
	 */
	public FightOutResp battleOut(int roleId) {
		FightOutResp resp = new FightOutResp();
		resp.setResult(1);
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo != null) {
			synchronized (roleInfo){
				//强制退出处理
				FightService.dealFightOut(roleInfo);
			}
		} else {
			return resp;
		}
		
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action21.getType(), "");
		return resp;
	}
	
	/**
	 * PVE战斗伤害验证(只验证BOSS)
	 * @param roleId
	 * @param req
	 */
	public void checkFight(int roleId,CheckFightReq req)
	{
		//客户端值变化
		//hurt = (hurt + 9966) * 0.4
		//ad = (ad + 3200) * 0.6
		//magicAttack = (magicAttack + 2600) * 0.8
		//attackDef = (attackDef + 500) * 0.25

		
		//我方单位属性
		//String[] side0ProStr = {"id-1","currHp-2","ad-3","magicAttack-4","attackDef-5","magicDef-6"};
		
		//对方单位属性
		//String[] side1ProStr = {"id-1","currHp-2","ad-3","magicAttack-4","attackDef-5","magicDef-6"};
		
		//String skillStr = "53100011,53100012,53100013,53100014," +
		//				  "53100023,53100022,53100021,53100024," +
		//				  "53100041,53100042,53100043,53100044," +
		//				  "53100051,53100052,53100053,53100054";
		
		//String speSkillStr = "53100054流星火雨,53100023散风刃,53100013龙吟虎啸,53100011弦月破";
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(req.getRoleId());
		if (roleInfo == null) 
		{
			logger.error("#####check fight error---1");
			return;
		}
		
		synchronized (roleInfo) 
		{
			long fightId = req.getFightId();
			//int fightType = req.getFightType();
			int reqHurt = (int) (req.getHurt()/0.4-9966);
			FightInfo fightInfo = FightInfoMap.getFightInfoByRoleId(roleId);
			
			if (fightInfo == null)
			{
				logger.error("#####check fight error---1");
				return;
			}
			
			if(fightInfo.getFightId() != fightId)
			{
				logger.error("#####check fight error---2");
				fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
				return;
			}
			 
			 List<CheckPropInfo> list0 = req.getList0();
			 List<CheckPropInfo> list1 = req.getList1();
			 
			 //检查技能CD
			 CheckPVEService.checkFightSkillCD(fightInfo);
			 
			//我方单位数据
			 long id = 0;
			 int currHp = 0;
			 int ad = 0;
			 int magicAttack = 0;
			 int attackDef = 0;
			 int magicDef = 0;
			 int moveSpeed = 0;
			 int attSpeed = 0;
			 if(list0 .size() > 0)
			 {
				 /*String str0 = checkFightListToString(list0);
				 String str1 = checkFightListToString(list1);
				 
				 if(str0.equalsIgnoreCase(fightInfo.getStr0()) || str1.equalsIgnoreCase(fightInfo.getStr1()))
				 {
					 logger.error("#####check fight error---3,this str0="+str0+",preStr0="+fightInfo.getStr0()+",this str1="+str1+",pre str1="+fightInfo.getStr1());
					 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
					 return;
				 }
				 
				 fightInfo.setStr0(str0);
				 fightInfo.setStr1(str1);*/
				 
				 for(CheckPropInfo propInfo : list0)
				 {
					 if(propInfo.getIndex() == 1)
					 {
						 id = (long) propInfo.getValue();
					 }
					 else if(propInfo.getIndex() == 2)
					 {
						 currHp = (int) propInfo.getValue();
					 }
					 else if(propInfo.getIndex() == 3)
					 {
						 ad = (int) (propInfo.getValue()/0.6-3200);
					 }
					 else if(propInfo.getIndex() == 4)
					 {
						 magicAttack = (int) (propInfo.getValue()/0.8-2600);
					 }
					 else if(propInfo.getIndex() == 5)
					 {
						 attackDef = (int) (propInfo.getValue()/0.25-500);
					 }
					 else if(propInfo.getIndex() == 6)
					 {
						 magicDef = (int) propInfo.getValue();
					 }
					 else if(propInfo.getIndex() == 7)
					 {
						 attSpeed = (int) propInfo.getValue();
					 }
					 else if(propInfo.getIndex() == 8)
					 {
						 moveSpeed = (int) propInfo.getValue();
					 }
					 else
					 {
						 logger.error("#####check fight error---4,index="+propInfo.getIndex());
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 return;
					 }
				 }
			 }
			 
			 if(req.getAttackType() == 3)
			 {
				 fightInfo.setCheckNum3(fightInfo.getCheckNum3()+1);
				 if(fightInfo.getSkillMap().containsKey(req.getSkillNo()))
				 {
					 CheckSkillInfo skillInfo = fightInfo.getSkillMap().get(req.getSkillNo());
					 long cdTime = System.currentTimeMillis() - skillInfo.getReleaseTime();
					 
					//小于技能CD可能异常
					 SkillXmlInfo skillXmlInfo = SkillInfoLoader.getSkillInfo(req.getSkillNo());
					 if(cdTime < skillXmlInfo.getCDTime() - 2000)
					 {
						 logger.error("#####check fight error---5,skillNo="+req.getSkillNo()+",cdTime="+cdTime);
						 
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 return;
					 }
					 
					 skillInfo.setReleaseTime(System.currentTimeMillis());
					 skillInfo.setStatus((byte) 1);
				 }
				 
				//释放技能 
				 CheckPVEService.pveReleaseSkill(fightInfo.getRoleId(),fightInfo,req);
				 
				 //logger.info("release skill,time="+new Timestamp(System.currentTimeMillis())+",no = "+ req.getSkillNo());
			 }
			 else
			 {
				 if(req.getAttackType() == 1)
				 {
					 fightInfo.setCheckNum1(fightInfo.getCheckNum1()+1);
				 }
				 else if(req.getAttackType() == 2)
				 {
					 fightInfo.setCheckNum2(fightInfo.getCheckNum2()+1);
				 }
				 
				 //普通攻击或技能伤害
				 for(long armyId : fightInfo.getSide0ArmyMap().keySet())
				 {
					 FightArmyDataInfo armyInfo = fightInfo.getSide0ArmyMap().get(armyId);
					 if(armyInfo == null || armyInfo.getId() != id)
					 {
						 continue;
					 }
					 
					 HeroXMLInfo heroxml = null;
					 if(armyInfo.getHeroNo() != 0)
					 {
						 heroxml = HeroXMLInfoMap.getHeroXMLInfo(armyInfo.getHeroNo());
					 }
					 
					 //普攻验证
					 if(Math.abs(ad - armyInfo.getAd()) > 400 
							 && Math.abs(ad - armyInfo.getAd())> armyInfo.getAd()*fightInfo.getF1())
					 {
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 logger.error("#####check fight error---6,roleName="+roleInfo.getRoleName() +
								 ",armyId="+armyInfo.getId()+
								 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
								 ",clientAd="+ad+",serverAd="+armyInfo.getAd());
						 
						 return;
					 }
					 
					//技攻验证
					 if(Math.abs(magicAttack - armyInfo.getMagicAttack()) > 400 
							 && Math.abs(magicAttack - armyInfo.getMagicAttack())>armyInfo.getMagicAttack()*fightInfo.getF2())
					 {
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 logger.error("#####check fight error---7,roleName="+roleInfo.getRoleName() +
								 ",armyId="+armyInfo.getId()+
								 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
								 ",clientMagicAttack="+magicAttack+",serverMagicAttack="+armyInfo.getMagicAttack());
						 
						 return;
					 }
					//普攻防御验证
					 if(Math.abs(attackDef - armyInfo.getAttackDef()) > 400 
							 && Math.abs(attackDef - armyInfo.getAttackDef())>armyInfo.getAttackDef()*fightInfo.getF3())
					 {
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 logger.error("#####check fight error---8,roleName="+roleInfo.getRoleName() +
								 ",armyId="+armyInfo.getId()+
								 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
								 ",clientAttackDef="+attackDef+",serverAttackDef="+armyInfo.getAttackDef());
						 
						 return;
					 }
					//技能防御验证
					 if(Math.abs(magicDef - armyInfo.getMagicDef()) > 400 
							 &&Math.abs(magicDef - armyInfo.getMagicDef())>armyInfo.getMagicDef()*fightInfo.getF4())
					 {
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 logger.error("#####check fight error---9,roleName="+roleInfo.getRoleName() +
								 ",armyId="+armyInfo.getId()+
								 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
								 ",clientMagicDef="+magicDef+",serverMagicDef="+armyInfo.getMagicDef());
						 
						 return;
					 }
					 
					//攻击速度验证
					 if(armyInfo.getAttackSpeed() / attSpeed > 2.5f)
					 {
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 logger.error("#####check fight error---10,roleName="+roleInfo.getRoleName() +
								 ",armyId="+armyInfo.getId()+
								 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
								 ",clientAttackSpeed="+attSpeed+",serverAttackSpeed="+armyInfo.getAttackSpeed());
						 
						 return;
					 }
					 
					//移动速度验证
					 if(Math.abs(moveSpeed - armyInfo.getMoveSpeed()) > 15 
							 &&moveSpeed / armyInfo.getMoveSpeed() > 2.0f)
					 {
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 logger.error("#####check fight error---11,roleName="+roleInfo.getRoleName() +
								 ",armyId="+armyInfo.getId()+
								 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
								 ",clientMoveSpeed="+moveSpeed+",serverMoveSpeed="+armyInfo.getMoveSpeed());
						 
						 return;
					 }
					 
					 //普通攻击
					 int hurt = 0;
					 if(req.getAttackType() == 1)
					 {
						 //攻击力*连击系数*（1.5+暴伤加成/10000）*（1+破兵值/10000）*方向系数
						 hurt = (int) (armyInfo.getAd() * heroxml.getLj4() * (1.5 + armyInfo.getCritMore()/10000)*(1+ armyInfo.getBreakSoldierDef()/10000) * 1.5);
					 }
					 else if(req.getAttackType() == 2)
					 {
						 SkillXmlInfo skillXmlInfo = SkillInfoLoader.getSkillInfo(req.getSkillNo());
						 if(skillXmlInfo == null)
						 {
							 return;
						 }
						 
						 if(fightInfo.getSkillMap().containsKey(req.getSkillNo()))
						 {
							 CheckSkillInfo skillInfo = fightInfo.getSkillMap().get(req.getSkillNo());
							 long cdTime = System.currentTimeMillis() - skillInfo.getHurtTime();
							 
							 //连击伤害的技能，判断CD时间内连击次数
							 if(cdTime < skillXmlInfo.getCDTime()/2)
							 {
								 skillInfo.setReleaseNum(skillInfo.getReleaseNum()+1);
								 
								 if(skillInfo.getReleaseNum() > 6)
								 {
									 logger.error("#####check fight error---12,skillNo="+req.getSkillNo()+",releaseNum="+skillInfo.getReleaseNum());
									 
									 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
									 skillInfo.setReleaseNum(0);
									 return;
								 }
							 }
							 skillInfo.setReleaseNum(0);
							 skillInfo.setHurtTime(System.currentTimeMillis());
							 
							 //（技能强度*（1+技能伤害比例）+技能额外造成伤害）*（1.5+暴伤加成/10000）
							 int currSkillLevel = skillInfo.getSkillLv();
							 
							 hurt = (int) ((armyInfo.getMagicAttack() *
									 (1+skillXmlInfo.getSkillParameter()+currSkillLevel*skillXmlInfo.getLevelParameter())+
									 (skillXmlInfo.getSkillConstant()+currSkillLevel*skillXmlInfo.getLevelParameter()))
									 *(1.5+armyInfo.getSkillCrit()/10000));
						 }
					 }
					 else
					 {
						 logger.error("#####check fight error---13,attackType="+req.getAttackType());
						 return;
					 }
					 
					 //伤害验证
					 if(reqHurt > 500 && hurt > 500 && reqHurt > hurt * 3)
					 {
						 fightInfo.setCheckErrorNum(fightInfo.getCheckErrorNum()+1);
						 
						 logger.error("#####check fight error---14,roleId="+roleInfo.getId()+",roleName="+roleInfo.getRoleName() +
								 ",armyId="+armyInfo.getId()+
								 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
								 ",attackType="+req.getAttackType()+",skillNo="+req.getSkillNo()+
								 ",clientHp="+currHp+",serverHp="+armyInfo.getCurrHp()+
								 ",clientHurt="+reqHurt+",serverHurt="+hurt);
						 
						 return;
					 }
				 } 
			 }
			 
			 
			 //BOSS属性
			 long id1 = 0;
			 int currHp1 = 0;
			 int ad1 = 0;
			 int magicAttack1 = 0;
			 int attackDef1 = 0;
			 int magicDef1 = 0;
			 //int moveSpeed1 = 0;
			 //int attSpeed1 = 0;
			 
			 //检测BOSS属性是否修改
			 if(list1.size() > 0 && fightInfo.getBossError() == 0)
			 {
				 for(CheckPropInfo propInfo : list1)
				 {
					 if(propInfo.getIndex() == 1)
					 {
						 id1 = (long) propInfo.getValue();
					 }
					 else if(propInfo.getIndex() == 2)
					 {
						 currHp1 = (int) propInfo.getValue();
					 }
					 else if(propInfo.getIndex() == 3)
					 {
						 ad1 = (int) (propInfo.getValue()/0.6-3200);
					 }
					 else if(propInfo.getIndex() == 4)
					 {
						 magicAttack1 = (int) (propInfo.getValue()/0.8-2600);
					 }
					 else if(propInfo.getIndex() == 5)
					 {
						 attackDef1 = (int) (propInfo.getValue()/0.25-500);
					 }
					 else if(propInfo.getIndex() == 6)
					 {
						 magicDef1 = (int) propInfo.getValue();
					 }
					 else if(propInfo.getIndex() == 7)
					 {
						 //attSpeed1 = (int) propInfo.getValue();
					 }
					 else if(propInfo.getIndex() == 8)
					 {
						 //moveSpeed1 = (int) propInfo.getValue();
					 }
					 else
					 {
						 logger.error("#####check fight error---15,index="+propInfo.getIndex());
						 return;
					 }
				 }
				 
				 FightArmyDataInfo armyInfo = null;
				 switch(fightInfo.getFightType())
				 {
					 case FIGHT_TYPE_1:
					 case FIGHT_TYPE_10:
					 case FIGHT_TYPE_13:
					 case FIGHT_TYPE_15:
						 armyInfo = fightInfo.getBossArmyDataInfo();
						 break;
					 case FIGHT_TYPE_2:
					 case FIGHT_TYPE_6:
						 armyInfo = fightInfo.getSide1ArmyMap().get(id1);
						 break;
					 default:
							break;
				 }
				 if(armyInfo == null)
				 {
					 return;
				 }
				 switch(fightInfo.getFightType())
				 {
					 case FIGHT_TYPE_1:
					 case FIGHT_TYPE_10:
					 case FIGHT_TYPE_13:
						 
						 if( (Math.abs(currHp1 - armyInfo.getHp()) > 400 && Math.abs(currHp1 - armyInfo.getHp())> armyInfo.getHp()*0.25f)
								 || (Math.abs(ad1 - armyInfo.getAd()) > 400 && Math.abs(ad1 - armyInfo.getAd())> armyInfo.getAd()*0.25f)
								 || (Math.abs(magicAttack1 - armyInfo.getMagicAttack()) > 400 && Math.abs(magicAttack1 - armyInfo.getMagicAttack())>armyInfo.getMagicAttack()*0.25f)
								 || (Math.abs(attackDef1 - armyInfo.getAttackDef()) > 400 && Math.abs(attackDef1 - armyInfo.getAttackDef())>armyInfo.getAttackDef()*0.25f)
								 || (Math.abs(magicDef1 - armyInfo.getMagicDef()) > 400 && Math.abs(magicDef1 - armyInfo.getMagicDef())>armyInfo.getMagicDef()*0.25f))
						 {
							 fightInfo.setBossError(2);
							 
							 logger.error("#####check fight error---16,roleId="+roleInfo.getId()+",roleName="+roleInfo.getRoleName() +
									 ",account="+roleInfo.getAccount()+
									 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
									 ",clientHP="+currHp1+",serverHp="+armyInfo.getHp()+
									 ",clientAd="+ad1+",serverAd="+armyInfo.getAd()+
									 ",clientMagicAttack="+magicAttack1+",serverMagicAttack="+armyInfo.getMagicAttack()+
									 ",clientAttackDef="+attackDef1+",serverAttackDef="+armyInfo.getAttackDef()+
									 ",clientMagicDef="+magicDef1+",serverMagicDef="+armyInfo.getMagicDef()+
									 ",attackType="+req.getAttackType()+",npcNo="+armyInfo.getHeroNo());
							 
							 return;
						 }
						 else
						 {
							 fightInfo.setBossError(1);
						 }
						 break;
					 case FIGHT_TYPE_15:
						 armyInfo = fightInfo.getBossArmyDataInfo();
						 
						 if(Math.abs(currHp1 - armyInfo.getBossHp())> armyInfo.getBossHp()*0.25f)
						 {
							 fightInfo.setBossError(2);
							 
							 logger.error("#####check fight error---17,roleId="+roleInfo.getId()+",roleName="+roleInfo.getRoleName() +
									 ",account="+roleInfo.getAccount()+
									 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
									 ",clientHp="+currHp1+",serverHp="+armyInfo.getBossHp());
							 
							 return;
						 }
						 else
						 {
							 fightInfo.setBossError(1);
						 }
						 break;
					 case FIGHT_TYPE_2:
					 case FIGHT_TYPE_6:
						 if( (Math.abs(currHp1 - armyInfo.getHp()) > 400 && Math.abs(currHp1 - armyInfo.getHp())> armyInfo.getHp()*0.3f)
								 || (Math.abs(ad1 - armyInfo.getAd()) > 400 && Math.abs(ad1 - armyInfo.getAd())> armyInfo.getAd()*0.3f)
								 || (Math.abs(magicAttack1 - armyInfo.getMagicAttack()) > 400 && Math.abs(magicAttack1 - armyInfo.getMagicAttack())>armyInfo.getMagicAttack()*0.3f)
								 || (Math.abs(attackDef1 - armyInfo.getAttackDef()) > 400 && Math.abs(attackDef1 - armyInfo.getAttackDef())>armyInfo.getAttackDef()*0.3f)
								 || (Math.abs(magicDef1 - armyInfo.getMagicDef()) > 400 && Math.abs(magicDef1 - armyInfo.getMagicDef())>armyInfo.getMagicDef()*0.3f))
						 {
							 fightInfo.setBossError(2);
							 
							 logger.error("#####check fight error---18,roleId="+roleInfo.getId()+",roleName="+roleInfo.getRoleName() +
									 ",account="+roleInfo.getAccount()+
									 ",defendStr="+fightInfo.getDefendStr()+",fightType="+fightInfo.getFightType().getValue()+
									 ",clientAd="+ad1+",serverAd="+armyInfo.getAd()+
									 ",clientMagicAttack="+magicAttack1+",serverMagicAttack="+armyInfo.getMagicAttack()+
									 ",clientAttackDef="+attackDef1+",serverAttackDef="+armyInfo.getAttackDef()+
									 ",clientMagicDef="+magicDef1+",serverMagicDef="+armyInfo.getMagicDef()+
									 ",attackType="+req.getAttackType()+",skillNo="+req.getSkillNo()+
									 ",clientHp="+currHp1+",serverHp="+armyInfo.getCurrHp());
							 
							 return;
						 }
						 else
						 {
							 fightInfo.setBossError(1);
						 }
					 default:
							break;
				 }
			 }
		}
	}
	
	/**
	 * 查看前后发过来的顺序是否一致
	 * @param list
	 * @return
	 */
	public String checkFightListToString(List<CheckPropInfo> list)
	{
		String str = "";
		for(CheckPropInfo info : list)
		{
			str = str+","+info.getIndex();
		}
		
		return str;
	}
}
