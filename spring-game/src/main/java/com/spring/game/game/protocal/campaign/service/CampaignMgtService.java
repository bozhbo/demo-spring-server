package com.snail.webgame.game.protocal.campaign.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;

import com.snail.webgame.game.cache.FightCampaignInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.fightdata.ArmyFightingInfo;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.ClubHireHeroDao;
import com.snail.webgame.game.dao.FightCampaignDAO;
import com.snail.webgame.game.info.FightCampaignBattle;
import com.snail.webgame.game.info.FightCampaignHero;
import com.snail.webgame.game.info.FightCampaignInfo;
import com.snail.webgame.game.info.HeroImageInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.HireHeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.RoleCampLog;
import com.snail.webgame.game.protocal.campaign.buy.BuyCampaignResp;
import com.snail.webgame.game.protocal.campaign.prize.GetCampaignPrizeReq;
import com.snail.webgame.game.protocal.campaign.prize.GetCampaignPrizeResp;
import com.snail.webgame.game.protocal.campaign.query.CampaignBattleRe;
import com.snail.webgame.game.protocal.campaign.query.CampaignHeroRe;
import com.snail.webgame.game.protocal.campaign.query.QueryCampaignHeroResp;
import com.snail.webgame.game.protocal.campaign.query.QueryCampaignResp;
import com.snail.webgame.game.protocal.campaign.revice.ReviceCampaignResp;
import com.snail.webgame.game.protocal.campaign.sweep.SweepCampaignResp;
import com.snail.webgame.game.protocal.club.hire.service.HireHeroService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;
import com.snail.webgame.game.protocal.fightdeploy.service.FightDeployService;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.CampaignXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.CampaignXMLBattle;
import com.snail.webgame.game.xml.info.CampaignXMLInfo;
import com.snail.webgame.game.xml.info.DropXMLInfo;

public class CampaignMgtService {

	private FightCampaignDAO fightCampaignDAO = FightCampaignDAO.getInstance();

	/**
	 * 查询宝物活动信息信息
	 * @param roleId
	 * @return
	 */
	public QueryCampaignResp queryCampaign(int roleId) {
		QueryCampaignResp resp = new QueryCampaignResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CAMPAIGN_QUERY_ERROR_1);
			return resp;
		}

		synchronized (roleInfo) {
			FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleId);
			if (info == null) {			
				info = CampaignService.initFightCampaignInfo(roleInfo);
				if (info == null) {
					resp.setResult(ErrorCode.CAMPAIGN_QUERY_ERROR_2);
					return resp;
				}

				if (fightCampaignDAO.insertFightCampaignInfo(info)) {
					FightCampaignInfoMap.addFightCampaignInfo(info);
				} else {
					resp.setResult(ErrorCode.CAMPAIGN_QUERY_ERROR_3);
					return resp;
				}
			}

			setCampaignResp(resp, roleInfo, info);
			return resp;
		}
	}

	/**
	 * 
	 * @param resp
	 * @param info
	 */
	private void setCampaignResp(QueryCampaignResp resp,RoleInfo roleInfo, FightCampaignInfo info) {
		resp.setResult(1);
		resp.setReviceNum((byte) info.getReviceNum());
		resp.setResetNum(info.getCurrResetNum());
		resp.setLastFightBattleNo(info.getCurrLastFightBattleNo());
		resp.setHisFightBattleNo(info.getCurrHisFightBattleNo());

		List<CampaignHeroRe> list = CampaignService.getCampaignHeros(roleInfo, info, "");
		resp.setList(list);
		resp.setCount(list.size());

		List<CampaignBattleRe> battleList = CampaignService.getCampaignBattles(info, "");
		resp.setBattleList(battleList);
		resp.setBattleCount(battleList.size());
		
		String hireHeroIds = "";
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo != null){
			Map<Integer, HeroImageInfo> val = roleLoadInfo.getHeroImageMapbyFightType(FightType.FIGHT_TYPE_6);
			if(val != null){
				for(HeroImageInfo imageInfo : val.values()){
					if(hireHeroIds.length() <= 0){
						hireHeroIds += imageInfo.getHeroId();
					} else {
						hireHeroIds += ("," + imageInfo.getHeroId());
					}					
				}
			}	
		}
		resp.setHireHeroIds(hireHeroIds);
	}

	/**
	 * 重置宝物活动
	 * @param roleId
	 * @return
	 */
	public QueryCampaignResp resetCampaign(int roleId) {
		QueryCampaignResp resp = new QueryCampaignResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CAMPAIGN_RESET_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleId);
			if (info == null) {
				resp.setResult(ErrorCode.CAMPAIGN_RESET_ERROR_2);
				return resp;
			}
			int resetNum = info.getCurrResetNum();
			int resetLimit = info.getCurrResetLimit();
			if (resetNum >= resetLimit) {
				resp.setResult(ErrorCode.CAMPAIGN_RESET_ERROR_3);
				return resp;
			}

			CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.CAMPAIGN_RESET_ERROR_7);
				return resp;
			}
			// 检测是否有未领奖的宝箱
			FightCampaignBattle battle = null;
			for (CampaignXMLBattle battleXMLInfo : xmlInfo.getBattles().values()) {
				if (battleXMLInfo.getCaseDropBag() == null || battleXMLInfo.getCaseDropBag().length() <= 0) {
					// 通关的宝箱奖励，为空则该关卡通关无奖励
					continue;
				}
				int battleNo = battleXMLInfo.getNo();
				battle = info.getBattleMap().get(battleNo);
				// 0-未领取 1-已领取
				if (battle != null && battle.getIsGetPrize() == 0) {
					// 1-胜 2-败
					if ((info.getLastFightResult() == 1 && battleNo <= info.getLastFightBattleNo())
							|| (info.getLastFightResult() == 2 && battleNo < info.getLastFightBattleNo())) {
						resp.setResult(ErrorCode.CAMPAIGN_RESET_ERROR_8);
						return resp;
					}
				}
			}

			int result = CampaignService.resetFightCampaignInfo(roleInfo, info);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			setCampaignResp(resp, roleInfo, info);
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action281.getType(), "");
			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleId, null, true, GameValue.RED_POINT_TYPE_EXPERIENCE_2);
			return resp;
		}
	}

	/**
	 * 购买重置次数
	 * @param roleId
	 * @return
	 */
	public BuyCampaignResp buyCampaign(int roleId) {
		BuyCampaignResp resp = new BuyCampaignResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CAMPAIGN_BUY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleId);
			if (info == null) {
				resp.setResult(ErrorCode.CAMPAIGN_BUY_ERROR_2);
				return resp;
			}

			// TODO 购买重置次数
			resp.setResult(1);
			resp.setResetLimit(info.getCurrResetLimit());
			resp.setBuyNum(info.getCurrBuyNum());
			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleId, null, true, GameValue.RED_POINT_TYPE_EXPERIENCE_2);
			return resp;
		}
	}

	/**
	 * 领取关卡奖励
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetCampaignPrizeResp getCampaignPrize(int roleId, GetCampaignPrizeReq req) {
		GetCampaignPrizeResp resp = new GetCampaignPrizeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleId);
			if (info == null) {
				resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_2);
				return resp;
			}
			int battleNo = req.getBattleNo();
			CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_3);
				return resp;
			}
			CampaignXMLBattle battleXMLInfo = xmlInfo.getBattles().get(battleNo);
			if (battleXMLInfo == null) {
				resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_4);
				return resp;
			}
			String dropBag = battleXMLInfo.getCaseDropBag();
			if (dropBag == null || dropBag.length() <= 0) {
				resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_5);
				return resp;
			}
			FightCampaignBattle battle = info.getBattleMap().get(battleNo);
			if (battle == null) {
				resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_6);
				return resp;
			}
			if (battle.getIsGetPrize() == FightCampaignBattle.GET_PRIZE_STATUS_1) {
				resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_7);
				return resp;
			}
			if (info.getCurrLastFightBattleNo() != -1 && info.getCurrLastFightBattleNo() <= battleNo) {
				resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_8);
				return resp;
			}

			if (fightCampaignDAO.updateCampaignBattlePrize(battle.getId(), FightCampaignBattle.GET_PRIZE_STATUS_1)) {
				battle.setIsGetPrize(FightCampaignBattle.GET_PRIZE_STATUS_1);
			} else {
				resp.setResult(ErrorCode.CAMPAIGN_PRIZE_ERROR_9);
				return resp;
			}

			List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(dropBag);
			List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
			if (list != null) {

				int result = ItemService.addPrizeForPropBag(ActionType.action305.getType(), roleInfo, list, null,
						getPropList, null, null, null, false);

				if (result != 1) {
					resp.setResult(result);
					return resp;
				}
			}

			resp.setResult(1);
			resp.setBattleNo(battleNo);
			resp.setIsGetPrize(battle.getIsGetPrize());
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action286.getType(), req.getBattleNo() + "");

			if (getPropList != null && getPropList.size() > 0) {
				resp.setPrizeNum(getPropList.size());
				resp.setPrize(getPropList);
			}

			// 任务
			boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action182.getType(), null, true,
					false);
			boolean isRedPointMonth = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
					GameValue.RED_POINT_STONE_COMPOSE);
			// 红点推送
			if (isRedPointQuest || isRedPointMonth) {
				RedPointMgtService.pop(roleInfo.getId());
			}

			return resp;
		}
	}

	/**
	 * 复活主武将继续战斗
	 * @param roleId
	 * @return
	 */
	public ReviceCampaignResp reviceCampaign(int roleId) {
		ReviceCampaignResp resp = new ReviceCampaignResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null) {
				resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_2);
				return resp;
			}

			FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleId);
			if (info == null) {
				resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_3);
				return resp;
			}
			if (info.getReviceNum() >= roleInfo.getCampaignReviceLimit()) {
				resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_4);
				return resp;
			}

			FightCampaignHero mainCampHero = info.getHeroMap().get(mainHero.getId());
			if (mainCampHero == null) {
				resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_5);
				return resp;
			}
			if (mainCampHero.getHeroStatus() != FightCampaignHero.HERO_STATUS_0) {
				resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_6);
				return resp;
			}

			// 扣除消耗
			int needCoin = GameValue.CAMPAIGN_REVICE_COST_GOLD;
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new CoinCond(needCoin));

			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}

			if (RoleService.subRoleResource(ActionType.action306.getType(), roleInfo, conds, null)) {
				resp.setSourceType((byte) ConditionType.TYPE_COIN.getType());
				resp.setSourceChange((int) -needCoin);
			} else {
				resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_7);
				return resp;
			}
			// 更新复活次数
			int reviceNum = info.getReviceNum() + 1;
			if (fightCampaignDAO.updateCampaignReviceNum(info.getId(), reviceNum)) {
				info.setReviceNum(reviceNum);
			} else {
				resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_8);
				return resp;
			}

			// 更新主武将状态血量
			if (fightCampaignDAO.updateCampaignHeroStatus(mainCampHero.getId(), FightCampaignHero.HERO_STATUS_1, 0)) {
				mainCampHero.setHeroStatus(FightCampaignHero.HERO_STATUS_1);
				mainCampHero.setCutHp(0);
			} else {
				resp.setResult(ErrorCode.CAMPAIGN_REVICE_ERROR_9);
				return resp;
			}

			resp.setResult(1);
			resp.setReviceNum((byte) reviceNum);
			return resp;
		}
	}

	/**
	 * 宝物活动开始处理
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int dealFightStart(RoleInfo roleInfo, FightInfo fightInfo) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return ErrorCode.CAMPAIGN_FIGHT_START_ERROR_1;
		}
		FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleInfo.getId());
		if (info == null) {
			return ErrorCode.CAMPAIGN_FIGHT_START_ERROR_1;
		}
		CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
		if (xmlInfo == null) {
			return ErrorCode.CAMPAIGN_FIGHT_START_ERROR_2;
		}

		// 检测布阵信息
		Map<Integer, FightCampaignHero> changes = new HashMap<Integer, FightCampaignHero>();
		// <heroId,info>
		Map<Integer, HeroImageInfo> imageChanges = new HashMap<Integer, HeroImageInfo>();

		int check = checkFightStartReq(roleInfo, info, fightInfo, xmlInfo, changes,imageChanges);
		if (check != 1) {
			return check;
		}
		Map<Integer, FightCampaignHero> insertOrUpdate = new HashMap<Integer, FightCampaignHero>();
		Map<Integer, Integer> delIds = new HashMap<Integer, Integer>();
		CampaignService.getFightCampaignHeroUpdate(info, changes, insertOrUpdate, delIds);
		
		Map<Integer, HeroImageInfo> imageInsertOrUpdate = new HashMap<Integer, HeroImageInfo>();
		CampaignService.getFightCampaignHeroImageUpdate(roleLoadInfo, imageChanges, imageInsertOrUpdate);
		

		// 更新当前自己的布阵信息
		if (FightCampaignDAO.getInstance().updateFightCampaignHero(insertOrUpdate, delIds,imageInsertOrUpdate)) {
			String heroIdStr = "";

			for (int heroId : delIds.keySet()) {
				heroIdStr += heroId + ",";
				info.getHeroMap().remove(heroId);
			}
			for (FightCampaignHero hero : insertOrUpdate.values()) {
				heroIdStr += hero.getHeroId() + ",";
				info.getHeroMap().put(hero.getHeroId(), hero);
			}
			
			for(HeroImageInfo image:imageInsertOrUpdate.values()){
				heroIdStr += image.getHeroId() + ",";
				roleLoadInfo.addHeroImageInfo(image);
			}

			if (heroIdStr.length() > 0) {
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CAMPAIGN_HERO, heroIdStr);
			}
		} else {
			return ErrorCode.CAMPAIGN_FIGHT_START_ERROR_3;
		}

		FightSideData attackSide = getAttackFightSideData(roleInfo, info, fightInfo);
		if (attackSide == null) {
			return ErrorCode.CAMPAIGN_FIGHT_START_ERROR_4;
		}

		FightSideData defendSide = getDefendFightSideData(roleInfo, info, fightInfo, xmlInfo);
		if (defendSide == null) {
			return ErrorCode.CAMPAIGN_FIGHT_START_ERROR_5;
		}

		fightInfo.getFightDataList().add(attackSide);
		fightInfo.getFightDataList().add(defendSide);
		fightInfo.setCheckFlag((byte) 1);

		return 1;
	}

	/**
	 * 验证请求信息
	 * @param roleInfo
	 * @param deployType
	 * @param list
	 * @return
	 */
	private static int checkFightStartReq(RoleInfo roleInfo, FightCampaignInfo info, FightInfo fightInfo,
			CampaignXMLInfo xmlInfo, Map<Integer, FightCampaignHero> changes, Map<Integer, HeroImageInfo> imageChanges) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return ErrorCode.CAMPAIGN_FIGHT_START_ERROR_1;
		}
		int currFightBattleNo = NumberUtils.toInt(fightInfo.getDefendStr());
		if (xmlInfo.getBattles().get(currFightBattleNo) == null) {
			return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_1;
		}
		FightCampaignBattle battle = info.getBattleMap().get(currFightBattleNo);
		if (battle == null) {
			return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_2;
		}
		int lastFightResult = info.getLastFightResult();// 1-胜 2-败
		int lastFightBattleNo = info.getLastFightBattleNo();
		if (lastFightResult == 0) {
			if (xmlInfo.getFristBattleNo() != currFightBattleNo) {
				return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_3;
			}
		} else if (lastFightResult == 1) {
			if (lastFightBattleNo + 1 != currFightBattleNo) {
				return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_4;
			}
		} else if (lastFightResult == 2) {
			if (lastFightBattleNo != currFightBattleNo) {
				return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_5;
			}
		}
		List<StartFightPosInfo> list = fightInfo.getChgPosInfos();
		if (list == null || list.size() <= 0) {
			return ErrorCode.CAMPAIGN_POS_ERROR_1;
		}
		boolean havMainHero = false;// 验证是否有主武将
		Set<Integer> heroIds = new HashSet<Integer>();// 验证英雄id
		Set<Byte> deployPoss = new HashSet<Byte>();// 验证布阵位置
		Set<Integer> heroNos = new HashSet<Integer>();// 验证布阵武将编号
		HeroInfo heroInfo = null;
		RoleInfo hireRole = null;
		HeroInfo hireHero = null;	
		HireHeroInfo hireHeroInfo = null;
		HeroImageInfo imageInfo = null;
		int costMoney = 0;
		int hireheroDeployNum = 0;
		for (StartFightPosInfo re : list) {
			if(hireheroDeployNum > 1){
				// 玩家每次战斗仅能上场1个佣兵。
				return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_7;
			}
			byte deployPos = re.getDeployPos();
			int roleId = re.getRoleId();
			int heroId = re.getHeroId();
			if (deployPos <= 0) {
				return ErrorCode.CAMPAIGN_POS_ERROR_2;
			}
			if (deployPoss.contains(deployPos)) {
				// 判断上阵位置是否重复
				return ErrorCode.CAMPAIGN_POS_ERROR_3;
			} else {
				deployPoss.add(deployPos);// 记录heroId
			}
			if(roleId == roleInfo.getId()){
				heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
				if (heroInfo == null) {
					return ErrorCode.CAMPAIGN_POS_ERROR_4;
				}
				if (deployPos == HeroInfo.DEPLOY_TYPE_MAIN) {
					if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
						havMainHero = true;
					} else {
						// 主英雄判断
						return ErrorCode.CAMPAIGN_POS_ERROR_5;
					}
				}
			}
			if (heroIds.contains(heroId)) {
				// 判断heroId是否重复
				return ErrorCode.CAMPAIGN_POS_ERROR_6;
			} else {
				heroIds.add(heroId);// 记录heroId
			}

			if (!FightDeployService.checkDeployPosOpen(roleInfo, deployPos)) {
				return ErrorCode.CAMPAIGN_POS_ERROR_10;
			}
			if(roleId == roleInfo.getId()){
				if (heroNos.contains(heroInfo.getHeroNo())) {
					// 每次战斗仅能上阵一个同名武将
					return ErrorCode.CAMPAIGN_POS_ERROR_6;
				} else {
					heroNos.add(heroInfo.getHeroNo());// 记录heroId
				}
				FightCampaignHero cahero = info.getHeroMap().get(heroId);
				if (cahero == null) {
					cahero = new FightCampaignHero();
					cahero.setRoleId(info.getRoleId());
					cahero.setHeroId(re.getHeroId());
					cahero.setDeployPos(deployPos);
					cahero.setHeroStatus(FightCampaignHero.HERO_STATUS_1);
					cahero.setCutHp(0);
					changes.put(re.getHeroId(), cahero);
				} else {
					if (cahero.getHeroStatus() == FightCampaignHero.HERO_STATUS_0) {
						return ErrorCode.CAMPAIGN_POS_ERROR_7;
					}
					FightCampaignHero campaignHero = (FightCampaignHero) cahero.clone();
					campaignHero.setDeployPos(deployPos);
					changes.put(re.getHeroId(), campaignHero);
				}
			} else {		
				imageInfo = roleLoadInfo.getHeroImageInfo(fightInfo.getFightType(), heroId);
				if(imageInfo == null){
					hireRole = RoleInfoMap.getRoleInfo(roleId);
					if (hireRole == null) {
						return ErrorCode.CAMPAIGN_POS_ERROR_4;
					}
					hireHeroInfo = hireRole.getHireHeroInfo(heroId);
					if(hireHeroInfo == null){
						return ErrorCode.CAMPAIGN_POS_ERROR_4;
					}
					hireHero = HeroInfoMap.getHeroInfo(roleId, heroId);
					if (hireHero == null) {
						return ErrorCode.CAMPAIGN_POS_ERROR_4;
					}
					int check = HireHeroService.copyHeroPropCheck(fightInfo.getFightType(), roleInfo, hireHero);
					if(check != 1){
						return check;
					}		
					imageInfo = HireHeroService.getHeroImageInfo(roleInfo.getId(), hireHero, deployPos, fightInfo.getFightType());
					if(imageInfo!=null){
						imageChanges.put(heroId, imageInfo);
					}
					// 支付银子获得佣兵
					costMoney += HireHeroService.getHireHeroPrize(hireHero);
				} else {
					if (imageInfo.getHeroStatus() == FightCampaignHero.HERO_STATUS_0) {
						return ErrorCode.CAMPAIGN_POS_ERROR_7;
					}
					HeroImageInfo campaignImage = (HeroImageInfo) imageInfo.clone();
					campaignImage.setDeployStatus(deployPos);
					imageChanges.put(heroId, campaignImage);
				}
				hireheroDeployNum++;
				if (heroNos.contains(imageInfo.getHeroNo())) {
					//  每次战斗仅能上阵一个同名武将
					return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_10;
				} else {
					heroNos.add(imageInfo.getHeroNo());// 记录heroId
				}
			}
		}
		if (!havMainHero) {
			// 没有主武将
			return ErrorCode.CAMPAIGN_POS_ERROR_8;
		}
		if(costMoney > 0){
			// 支付银子获得佣兵
			if(roleInfo.getMoney() >= costMoney){			
				if(RoleService.subRoleRoleResource(ActionType.action502.getType(), roleInfo, ConditionType.TYPE_MONEY,
						(long)costMoney, null)){
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
					
					if(hireHero != null && hireRole != null)
					{
						GameLogService.insertPlayActionLog(roleInfo, ActionType.action502.getType(), 0, hireRole.getRoleName()+":"+hireHero.getHeroNo()+":"+costMoney);
					}
				} else {
					return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_9;
				}						
			} else {
				return ErrorCode.CHECK_CAMPAIGN_FIGHT_START_ERROR_8;
			}
			if(hireHeroInfo != null){
				synchronized(hireHeroInfo){
					// 更新佣兵获得总的银子
					int sum = (int) (hireHeroInfo.getHireMoneySum() + costMoney);
					if(ClubHireHeroDao.getInstance().updateHireHeroInfoByRoleId(hireHeroInfo.getRoleId(), hireHeroInfo.getHeroId(), sum)){
						hireHeroInfo.setHireMoneySum(sum);
						HireHeroService.notifyClient4AddHireMoney(fightInfo.getFightType(), hireHeroInfo);
					}
				}
			}
		}
		return 1;
	}

	/**
	 * 获取攻击方战斗数据
	 * @param info
	 * @return
	 */
	private static FightSideData getAttackFightSideData(RoleInfo roleInfo, FightCampaignInfo info, FightInfo fightInfo) {
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightInfo.getFightType());
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightInfo.getFightType());

		FightSideData sideDate = new FightSideData();
		sideDate.setSideId(FightType.FIGHT_SIDE_0);
		sideDate.setSideRoleId(roleInfo.getId());
		sideDate.setSideName(roleInfo.getRoleName());
		sideDate.setArmyInfos(new ArrayList<FightArmyDataInfo>());

		Map<Byte, HeroRecord> recordMap = CampaignService.getAttackHeroRecordMap(roleInfo, info);
		if (recordMap == null) {
			return null;
		}
		FightArmyDataInfo armyData = null;
		for (HeroRecord record : recordMap.values()) {
			armyData = FightService.getFightArmyDatabyHeroRecord(recordMap, record, sideDate.getSideId(), mainRate,
					otherRate);
			if (armyData == null) {
				continue;
			}
			sideDate.getArmyInfos().add(armyData);
		}
		sideDate.setFightArmyNum(sideDate.getArmyInfos().size());
		if (sideDate.getArmyInfos().size() <= 0) {
			return null;
		}
		return sideDate;
	}

	/**
	 * 获取防守方战斗数据
	 * @param fightArena
	 * @return
	 */
	private static FightSideData getDefendFightSideData(RoleInfo roleInfo, FightCampaignInfo info, FightInfo fightInfo,
			CampaignXMLInfo xmlInfo) {
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightInfo.getFightType());
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightInfo.getFightType());

		FightSideData sideDate = new FightSideData();
		sideDate.setSideId(FightType.FIGHT_SIDE_1);
		sideDate.setSideRoleId(info.getRoleId());

		int currFightBattleNo = NumberUtils.toInt(fightInfo.getDefendStr());
		CampaignXMLBattle xmlBattle = xmlInfo.getBattles().get(currFightBattleNo);
		if (xmlBattle == null) {
			return null;
		}
		FightCampaignBattle battle = info.getBattleMap().get(currFightBattleNo);
		if (battle == null) {
			return null;
		}
		sideDate.setSideName(battle.getDefendRoleName());
		sideDate.setArmyInfos(new ArrayList<FightArmyDataInfo>());
		Map<Byte, HeroRecord> rocordMap = battle.getFightDeployMap();
		if (rocordMap == null) {
			return null;
		}
		float rate = xmlBattle.getAve();
		// 不是本人镜像
		if (battle.getDefendRoleId() != info.getRoleId()) {
			rate = 1;
		}
		mainRate = FightService.mulRate(mainRate, rate);
		otherRate = FightService.mulRate(otherRate, rate);

		FightArmyDataInfo armyData = null;
		boolean isHavingHero = false; // 是否有存活武将
		for (HeroRecord record : rocordMap.values()) {
			if (!isHavingHero && record.getHeroStatus() == 1) {
				isHavingHero = true;
			}
			armyData = FightService.getFightArmyDatabyHeroRecord(rocordMap, record, sideDate.getSideId(), mainRate,
					otherRate);
			if (armyData == null) {
				continue;
			}
			sideDate.getArmyInfos().add(armyData);
		}
		if (!isHavingHero && rocordMap.containsKey(HeroInfo.DEPLOY_TYPE_MAIN)) {
			// 双方一起死时 复活主武将保存一点血
			HeroRecord mainRecord = rocordMap.get(HeroInfo.DEPLOY_TYPE_MAIN);
			if (mainRecord != null) {
				mainRecord.setHeroStatus((byte) 1);
				armyData = FightService.getFightArmyDatabyHeroRecord(rocordMap, mainRecord, sideDate.getSideId(),
						mainRate, otherRate);
				if (armyData != null) {
					if (armyData.getCurrHp() <= 0) {
						armyData.setCurrHp(1);
					}
					sideDate.getArmyInfos().add(armyData);
				}
				mainRecord.setHeroStatus((byte) 0);
			}
		}
		sideDate.setFightArmyNum(sideDate.getArmyInfos().size());
		if (sideDate.getArmyInfos().size() <= 0) {
			return null;
		}
		return sideDate;
	}

	/**
	 * 宝物活动结果处理
	 * @param fightResult // 1-胜 2-败
	 * @param roleInfo
	 * @param fightInfo
	 * @param getResourceNum 资源变动
	 * @param getPrizeMum 道具变动
	 * @param addEquipIds 装备变动（掉装备）
	 * @return
	 */
	public static int dealFightEnd(int fightResult, RoleInfo roleInfo, FightInfo fightInfo,
			List<BattlePrize> prizeList, List<BattlePrize> fpPrizeList) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return ErrorCode.CAMPAIGN_FIGHT_START_ERROR_1;
		}
		CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
		if (xmlInfo == null) {
			return ErrorCode.CAMPAIGN_FIGHT_END_ERROR_1;
		}
		int currFightBattleNo = NumberUtils.toInt(fightInfo.getStartRespDefendStr());
		CampaignXMLBattle battleXMLInfo = xmlInfo.getBattles().get(currFightBattleNo);
		if (battleXMLInfo == null) {
			return ErrorCode.CAMPAIGN_FIGHT_END_ERROR_2;
		}
		FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleInfo.getId());
		if (info == null) {
			return ErrorCode.CAMPAIGN_FIGHT_END_ERROR_3;
		}
		FightCampaignBattle battle = info.getBattleMap().get(currFightBattleNo);
		if (battle == null) {
			return ErrorCode.CAMPAIGN_FIGHT_END_ERROR_4;
		}
		Map<Byte, HeroRecord> records = battle.getFightDeployMap();
		if (records == null) {
			return ErrorCode.CAMPAIGN_FIGHT_END_ERROR_5;
		}
		Map<Byte, HeroRecord> deployMap = new HashMap<Byte, HeroRecord>(records);
		Map<Integer, FightCampaignHero> insertOrUpdate = new HashMap<Integer, FightCampaignHero>();
		Map<Integer, HeroImageInfo> imageUpdate = new HashMap<Integer, HeroImageInfo>();

		int check = checkFightEndReq(roleLoadInfo,fightInfo, info, deployMap, insertOrUpdate, imageUpdate);
		if (check != 1) {
			return check;
		}

		// 攻城略地每场战斗胜利后，恢复所有未上阵并未死亡的武将的生命
		if (fightResult == 1) {
			Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightInfo.getFightType());
			Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightInfo.getFightType());
			HeroInfo heroInfo = null;
			HeroPropertyInfo pro = null;
			FightCampaignHero caHero = null;
			for (FightCampaignHero campaignhero : info.getHeroMap().values()) {
				if (campaignhero.getDeployPos() == HeroInfo.DEPLOY_TYPE_COMM
						&& campaignhero.getHeroStatus() == FightCampaignHero.HERO_STATUS_1
						&& campaignhero.getCutHp() != 0) {
					heroInfo = HeroInfoMap.getHeroInfo(campaignhero.getRoleId(), campaignhero.getHeroId());
					if (heroInfo == null) {
						return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_5;
					}
					pro = HeroProService.getHeroTotalProperty(heroInfo, mainRate, otherRate);
					if (pro == null) {
						return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_5;
					}
					int totalHp = pro.getHp();
					caHero = (FightCampaignHero) campaignhero.clone();
					if (caHero.getCutHp() > totalHp) {
						caHero.setCutHp(totalHp);
					}
					caHero.setCutHp(caHero.getCutHp() - (int) (totalHp * GameValue.CAMPAIGN_HP_BACK_RATE));
					if (caHero.getCutHp() < 0) {
						caHero.setCutHp(0);
					}
					insertOrUpdate.put(campaignhero.getHeroId(), caHero);
				}
			}
		}

		if (FightCampaignDAO.getInstance().updateFightCampaignFightResult(info.getId(), currFightBattleNo, fightResult,
				battle.getId(), deployMap, insertOrUpdate, null ,imageUpdate)) {
			info.setLastFightBattleNo(currFightBattleNo);
			info.setLastFightResult(fightResult);

			battle.setFightDeployMap(deployMap);

			String heroIdStr = "";
			for (FightCampaignHero hero : insertOrUpdate.values()) {
				heroIdStr += hero.getHeroId() + ",";
				info.getHeroMap().put(hero.getHeroId(), hero);
			}
			for (HeroImageInfo hero : imageUpdate.values()) {
				heroIdStr += hero.getHeroId() + ",";
				roleLoadInfo.addHeroImageInfo(hero);
			}
			if (heroIdStr.length() > 0) {
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CAMPAIGN_HERO, heroIdStr);
			}
		} else {
			return ErrorCode.CAMPAIGN_FIGHT_END_ERROR_6;
		}
		if (fightResult == 1) {
			int action = ActionType.action304.getType();
			int mainHeroLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
			List<DropXMLInfo> prizesList = PropBagXMLMap.getPropBagXMLList(battleXMLInfo.getDropBag());
			List<DropInfo> addList = new ArrayList<DropInfo>();
			ItemService.getDropXMLInfo(roleInfo, prizesList, addList);
			addList.add(new DropInfo(ConditionType.TYPE_MONEY.getName(), mainHeroLv*battleXMLInfo.getLvMoney()));
			
			String cardBag = battleXMLInfo.getCardBag();

			int check1 = ItemService.addPrize(action, roleInfo, addList, cardBag, null, null, null, null, null, prizeList, fpPrizeList, null, true);
			if (check1 != 1) {
				return check1;
			}
		}
		// 通知下关关卡编号变动
		fightInfo.setEndRespDefendStr(info.getCurrLastFightBattleNo() + "");

		// 任务检测
		boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action304.getType(), null, true, false);
		boolean isRedPointMonth = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
				GameValue.RED_POINT_STONE_COMPOSE);
		// 红点推送
		if (isRedPointQuest || isRedPointMonth) {
			RedPointMgtService.pop(roleInfo.getId());
		}

		String dropItem = "";
		List<BattlePrize> totalPrizeList = new ArrayList<BattlePrize>();
		totalPrizeList.addAll(prizeList);
		if (fpPrizeList.size() > 0) {
			totalPrizeList.add(fpPrizeList.get(0));
		}
		for (BattlePrize prize : totalPrizeList) {
			if (dropItem.length() == 0) {
				dropItem = prize.getNo() + "-" + prize.getNum();
			} else {
				dropItem = dropItem + "," + prize.getNo() + "-" + prize.getNum();
			}
		}
		RoleCampLog campLog = new RoleCampLog();
		campLog.setRoleId(roleInfo.getId());
		campLog.setAccount(roleInfo.getAccount());
		campLog.setRoleName(roleInfo.getRoleName());
		campLog.setCampNo(currFightBattleNo);
		campLog.setBattleResult(fightResult);
		campLog.setStartTime(new Timestamp(fightInfo.getFightTime()));
		campLog.setEndTime(new Timestamp(System.currentTimeMillis()));
		campLog.setPrize(dropItem);
		GameLogService.insertCampLog(campLog);

		return 1;
	}

	/**
	 * 验证请求信息
	 * @param fightInfo
	 * @param info
	 * @param clone
	 * @param insertOrUpdate
	 * @return
	 */
	private static int checkFightEndReq(RoleLoadInfo roleLoadInfo,FightInfo fightInfo, FightCampaignInfo info, Map<Byte, HeroRecord> records,
			Map<Integer, FightCampaignHero> insertOrUpdate,Map<Integer, HeroImageInfo> imageUpdate) {

		if (fightInfo.getFightDataList() == null || fightInfo.getFightDataList().size() == 0) {
			return ErrorCode.CHECK_CAMPAIGN_FIGHT_END_ERROR_1;
		}

		FightSideData attackSide = fightInfo.getFightDataList().get(0);
		if (attackSide == null) {
			return ErrorCode.CHECK_CAMPAIGN_FIGHT_END_ERROR_2;
		}
		List<FightArmyDataInfo> attackArmyDatas = attackSide.getArmyInfos();
		if (attackArmyDatas == null) {
			return ErrorCode.CHECK_CAMPAIGN_FIGHT_END_ERROR_3;
		}

		FightSideData defendSide = fightInfo.getFightDataList().get(1);
		if (defendSide == null) {
			return ErrorCode.CHECK_CAMPAIGN_FIGHT_END_ERROR_4;
		}
		List<FightArmyDataInfo> defendArmyDatas = defendSide.getArmyInfos();
		if (defendArmyDatas == null) {
			return ErrorCode.CHECK_CAMPAIGN_FIGHT_END_ERROR_5;
		}

		List<ArmyFightingInfo> armyFightingInfos = fightInfo.getArmyFightingInfos();
		if (armyFightingInfos == null) {
			return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_1;
		}

		for (FightArmyDataInfo data : attackArmyDatas) {
			if (getArmyFightingInfo(armyFightingInfos, data.getId()) == null) {
				return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_2;
			}
		}
		for (FightArmyDataInfo data : defendArmyDatas) {
			if (getArmyFightingInfo(armyFightingInfos, data.getId()) == null) {
				return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_3;
			}
		}

		FightCampaignHero caHero = null;
		HeroImageInfo caImage = null;
		for (ArmyFightingInfo army : armyFightingInfos) {
			if (army.getSideId() == FightType.FIGHT_SIDE_0) {
				FightArmyDataInfo armyData = getFightArmyDataInfo(attackArmyDatas, army.getId());
				if (armyData == null) {
					return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_5;
				}
				int totalHp = armyData.getHp();
				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(info.getRoleId(), (int) army.getId());
				if (heroInfo == null) {
					HeroImageInfo imageInfo = roleLoadInfo.getHeroImageInfo(fightInfo.getFightType(), (int) army.getId());
					if(imageInfo == null){			
						return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_4;
					}
					byte heroStatus = (byte)imageInfo.getHeroStatus();
					int cutHp = imageInfo.getCutHp();
					if (army.getCurrentHp() == 0) {
						heroStatus = FightCampaignHero.HERO_STATUS_0;
						cutHp = totalHp;
					} else {
						heroStatus = FightCampaignHero.HERO_STATUS_1;
						cutHp = totalHp - (int) army.getCurrentHp();
						if (cutHp < 0) {
							cutHp = 0;
						}
					}
					if (heroStatus != imageInfo.getHeroStatus() || cutHp != imageInfo.getCutHp()) {
						caImage = (HeroImageInfo) imageInfo.clone();
						caImage.setHeroStatus(heroStatus);
						caImage.setCutHp(cutHp);
						imageUpdate.put(caImage.getHeroId(), caImage);
					}
					
				} else {
					// 角色武将
					FightCampaignHero hero = info.getHeroMap().get(heroInfo.getId());
					if (hero == null) {
						return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_6;
					}
					byte heroStatus = hero.getHeroStatus();
					int cutHp = hero.getCutHp();
					if (army.getCurrentHp() == 0) {
						heroStatus = FightCampaignHero.HERO_STATUS_0;
						cutHp = totalHp;
					} else {
						heroStatus = FightCampaignHero.HERO_STATUS_1;
						cutHp = totalHp - (int) army.getCurrentHp();
						if (cutHp < 0) {
							cutHp = 0;
						}
					}
					if (heroStatus != hero.getHeroStatus() || cutHp != hero.getCutHp()) {
						caHero = (FightCampaignHero) hero.clone();
						caHero.setHeroStatus(heroStatus);
						caHero.setCutHp(cutHp);
						insertOrUpdate.put(heroInfo.getId(), caHero);
					}
				}

			} else if (army.getSideId() == FightType.FIGHT_SIDE_1) {
				// 守方
				FightArmyDataInfo armyData = getFightArmyDataInfo(defendArmyDatas, army.getId());
				if (armyData == null) {
					return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_7;
				}
				int totalHp = armyData.getHp();
				HeroRecord record = getHeroRecord(records, army.getId());
				if (record == null) {
					return ErrorCode.CAMPAIGN_FIGHT_RESULT_ERROR_8;
				}
				if (army.getCurrentHp() == 0) {
					record.setHeroStatus(FightCampaignHero.HERO_STATUS_0);
					record.setCutHp(totalHp);
				} else {
					int cutHp = totalHp - (int) army.getCurrentHp();
					record.setHeroStatus(FightCampaignHero.HERO_STATUS_1);
					record.setCutHp(cutHp < 0 ? 0 : cutHp);
				}
			}
		}
		return 1;
	}

	private static ArmyFightingInfo getArmyFightingInfo(List<ArmyFightingInfo> armyFightingInfos, long id) {
		for (ArmyFightingInfo info : armyFightingInfos) {
			if (info.getId() == id) {
				return info;
			}
		}
		return null;
	}

	private static FightArmyDataInfo getFightArmyDataInfo(List<FightArmyDataInfo> armyDatas, long id) {
		for (FightArmyDataInfo info : armyDatas) {
			if (info.getId() == id) {
				return info;
			}
		}
		return null;
	}

	private static HeroRecord getHeroRecord(Map<Byte, HeroRecord> record, long id) {
		for (HeroRecord info : record.values()) {
			if (info.getId() == id) {
				return info;
			}
		}
		return null;
	}

	/**
	 * 刷新查询战死残血和上阵的英雄信息
	 * @param roleId
	 * @param heroIdStr
	 * @return
	 */
	public QueryCampaignHeroResp refreshCampaignHero(int roleId, String heroIdStr) {
		QueryCampaignHeroResp resp = new QueryCampaignHeroResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CAMPAIGN_HERO_REFRESH_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleId);
			if (info == null) {
				resp.setResult(ErrorCode.CAMPAIGN_HERO_REFRESH_ERROR_2);
				return resp;
			}
			resp.setResult(1);
			resp.setHeroIdStr(heroIdStr);

			List<CampaignHeroRe> list = CampaignService.getCampaignHeros(roleInfo, info, heroIdStr);
			resp.setList(list);
			resp.setCount(list.size());

			int battleNo = info.getLastFightBattleNo();
			if (info.getLastFightResult() == 0) {
				CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
				if (info != null) {
					battleNo = xmlInfo.getFristBattleNo();
				}
			}

			FightCampaignBattle battle = info.getBattleMap().get(battleNo);
			if (battle == null) {
				resp.setResult(ErrorCode.CAMPAIGN_HERO_REFRESH_ERROR_3);
				return resp;
			}

			resp.setBattleInfo(CampaignService.getCampaignBattleRe(roleInfo, battle));

			String hireHeroIds = "";
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo != null){
				Map<Integer, HeroImageInfo> val = roleLoadInfo.getHeroImageMapbyFightType(FightType.FIGHT_TYPE_6);
				if(val != null){
					for(HeroImageInfo imageInfo : val.values()){
						if(hireHeroIds.length() <= 0){
							hireHeroIds += imageInfo.getHeroId();
						} else {
							hireHeroIds += ("," + imageInfo.getHeroId());
						}					
					}
				}	
			}
			resp.setHireHeroIds(hireHeroIds);
			return resp;
		}
	}

	/**
	 * 攻城略地VIP等级奖励加成
	 * @param vipLv
	 * @return
	 */
	public static double CampaignVipPlus(int vipLv) {
		int num = VipXMLInfoMap.getVipVal(vipLv, VipType.CAMPAIGN_PLUS);
		double rate = (double) num / (double) 100;

		return rate;
	}

	/**
	 * 扫荡功能
	 * @param roleId
	 * @return
	 */
	public SweepCampaignResp sweepCampaign(int roleId) {
		SweepCampaignResp resp = new SweepCampaignResp();
		QueryCampaignResp resp1 = new QueryCampaignResp();
		resp.setCampaign(resp1);
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.CAMPAIGN_SWEEP_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleId);
			if (info == null) {
				resp.setResult(ErrorCode.CAMPAIGN_SWEEP_ERROR_3);
				return resp;
			}
			int resetNum = info.getCurrResetNum();
			if (resetNum < 2) {
				resp.setResult(ErrorCode.CAMPAIGN_SWEEP_ERROR_4);
				return resp;
			}
			int lastFightBattleNo = info.getLastFightBattleNo();
			int lastFightResult = info.getLastFightResult();
			int hisFightBattleNo = info.getHisFightBattleNo();
			int hisFightResult = info.getHisFightResult();
			if (lastFightBattleNo >= hisFightBattleNo) {
				resp.setResult(ErrorCode.CAMPAIGN_SWEEP_ERROR_7);
				return resp;
			}

			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null) {
				resp.setResult(ErrorCode.CAMPAIGN_SWEEP_ERROR_2);
				return resp;
			}
			FightCampaignHero campaignHero = info.getHeroMap().get(mainHero.getId());
			if (campaignHero == null) {
				resp.setResult(ErrorCode.CAMPAIGN_SWEEP_ERROR_5);
				return resp;
			}
			if (campaignHero.getHeroStatus() == 0) {
				resp.setResult(ErrorCode.CAMPAIGN_SWEEP_ERROR_6);
				return resp;
			}

			int reviceNum = roleInfo.getCampaignReviceLimit();
			int currLastFightBattleNo = hisFightBattleNo;
			int currLastFightResult = hisFightResult;

			// 武将变动
			Map<Integer, FightCampaignHero> insertOrUpdateHeros = new HashMap<Integer, FightCampaignHero>();
			// 武将将会全部阵亡
			FightCampaignHero hero = null;
			for (int heroId : HeroInfoMap.getHeroByRoleId(roleId).keySet()) {
				hero = new FightCampaignHero();
				hero.setRoleId(roleId);
				hero.setHeroId(heroId);
				hero.setHeroStatus((byte) 0);
				hero.setCutHp(0);
				campaignHero = info.getHeroMap().get(mainHero.getId());
				if (campaignHero != null) {
					if (campaignHero.getHeroStatus() == 0) {
						continue;
					}
					hero.setId(campaignHero.getId());
				}
				insertOrUpdateHeros.put(heroId, hero);
			}

			// 更新变动
			if (FightCampaignDAO.getInstance().updateFightCampaignbySweep(info.getId(), reviceNum,
					currLastFightBattleNo, currLastFightResult, insertOrUpdateHeros, null)) {
				info.setReviceNum(reviceNum);
				info.setLastFightBattleNo(currLastFightBattleNo);
				info.setLastFightResult(currLastFightResult);
				for (FightCampaignHero campaignHeroInfo : insertOrUpdateHeros.values()) {
					info.getHeroMap().put(campaignHeroInfo.getHeroId(), campaignHeroInfo);
				}
			} else {
				resp.setResult(ErrorCode.CAMPAIGN_SWEEP_ERROR_8);
				return resp;
			}

			List<BattlePrize> prize = new ArrayList<BattlePrize>();
			// 扫荡功能掉落
			int result[] = getPrize(roleInfo, lastFightBattleNo, lastFightResult, hisFightBattleNo, hisFightResult,
					prize);
			if (result[0] != 1) {
				resp.setResult(result[0]);
				return resp;
			}

			resp.setResult(1);
			setCampaignResp(resp1, roleInfo, info);
			resp.setPrize(prize);
			resp.setPrizeNum(prize.size());
			resp.setSweepNum(result[1]);
			return resp;
		}
	}

	/**
	 * 扫荡功能掉落
	 * @param fristBattleNo
	 * @param fristFightResult
	 * @param lastBattleNo
	 * @param lastFightResult
	 * @param prize
	 * @return
	 */
	private int[] getPrize(RoleInfo roleInfo, int startBattleNo, int startFightResult, int endBattleNo,
			int endFightResult, List<BattlePrize> prize) {
		int sweepNum = 0;
		CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
		if (xmlInfo == null) {
			return new int[] { ErrorCode.CAMPAIGN_FIGHT_END_ERROR_1, sweepNum };
		}
		int start = startBattleNo;
		if (startFightResult == 0) {
			start = xmlInfo.getFristBattleNo();
		} else if (startFightResult == 1) {
			if (startBattleNo == xmlInfo.getLastBattleNo()) {
				return new int[] { 1, sweepNum };// 全部通关
			} else if (xmlInfo.getBattles().containsKey(startBattleNo + 1)) {
				start = startBattleNo + 1;
			}
		}

		int end = endBattleNo;
		if (endFightResult == 0) {
			return new int[] { 1, sweepNum };// 一关未通
		} else if (endFightResult == 2) {
			if (endBattleNo == xmlInfo.getFristBattleNo()) {
				return new int[] { 1, sweepNum };// 一关未通
			} else if (xmlInfo.getBattles().containsKey(endBattleNo - 1)) {
				end = endBattleNo - 1;
			}
		}
		if (start > end) {
			return new int[] { ErrorCode.CAMPAIGN_SWEEP_ERROR_7, sweepNum };
		}
		List<DropInfo> addList = new ArrayList<DropInfo>();
		CampaignXMLBattle battleXMLInfo = null;
		List<DropXMLInfo> prizesList = null;
		int mainHeroLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
		for (int i = start; i <= end; i++) {
			battleXMLInfo = xmlInfo.getBattles().get(i);
			if (battleXMLInfo != null) {
				byte sweep = (byte) (sweepNum + 1);
				if (i > 100) {
					sweep = Byte.parseByte(String.valueOf(i).substring(String.valueOf(i).length() - 2));
				}
				prizesList = PropBagXMLMap.getPropBagXMLList(battleXMLInfo.getDropBag());
				ItemService.getDropXMLInfo(roleInfo, prizesList, addList, sweep);
				prizesList = PropBagXMLMap.getPropBagXMLList(battleXMLInfo.getCardBag());
				ItemService.getDropXMLInfo(roleInfo, prizesList, addList, sweep);				
				addList.add(new DropInfo(ConditionType.TYPE_MONEY.getName(), mainHeroLv*battleXMLInfo.getLvMoney()));
				sweepNum++;
			}
		}

		return new int[] {
				ItemService.addPrize(ActionType.action307.getType(), roleInfo, addList, null, null, null, null, null,
						null, prize, null, null, true), sweepNum };
	}
}
