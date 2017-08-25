package com.snail.webgame.game.protocal.fightdeploy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.HeroDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.fightdeploy.buy.BuyDeployPosReq;
import com.snail.webgame.game.protocal.fightdeploy.buy.BuyDeployPosResp;
import com.snail.webgame.game.protocal.fightdeploy.change.DeployChangeJBRe;
import com.snail.webgame.game.protocal.fightdeploy.change.DeployChangeReq;
import com.snail.webgame.game.protocal.fightdeploy.change.DeployChangeResp;
import com.snail.webgame.game.protocal.fightdeploy.query.FightDeployRe;
import com.snail.webgame.game.protocal.fightdeploy.query.QueryFightDeployReq;
import com.snail.webgame.game.protocal.fightdeploy.query.QueryFightDeployResp;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;

public class FightDeployMgtService {

	/**
	 * 查询布阵信息
	 * @param roleId
	 * @param req
	 * @return
	 */
	public QueryFightDeployResp queryFightDeploy(int roleId, QueryFightDeployReq req) {
		QueryFightDeployResp resp = new QueryFightDeployResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.DEPLOY_QUERY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			byte deployType = req.getDeployType();// 1-普通布阵 -2-竞技场防守阵营
			List<FightDeployRe> list = FightDeployService.getFightDeployList(roleInfo, deployType);
			resp.setDeployType(deployType);
			resp.setCount(list.size());
			resp.setList(list);
			resp.setResult(1);
			return resp;
		}
	}

	/**
	 * 单个上下阵
	 * @param roleId
	 * @param req
	 * @return
	 */
	public DeployChangeResp deployChange(int roleId, DeployChangeReq req) {
		DeployChangeResp resp = new DeployChangeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			byte changeType = req.getChangeType();// 1-上阵 0-下阵
			int heroId = req.getHeroId();// 英雄ID
			byte position = req.getPosition();// 目标位置
			// 判断英雄是否存在
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_2);
				return resp;
			}
			HeroInfo changeHeroInfo = null;
			byte changePosition = 0;// 位置
			switch (changeType) {// 上阵
			case 1:
				if (position == HeroInfo.DEPLOY_TYPE_COMM) {
					resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_3);
					return resp;
				}
				if (!FightDeployService.checkDeployPosOpen(roleInfo, position)) {
					resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_8);
					return resp;
				}
				changeHeroInfo = HeroInfoMap.getFightDeployHero(roleId, position);
				if (changeHeroInfo != null) {
					changePosition = heroInfo.getDeployStatus();
					if (changePosition != HeroInfo.DEPLOY_TYPE_COMM) {
						if (!FightDeployService.checkDeployPosOpen(roleInfo, changePosition)) {
							resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_9);
							return resp;
						}
					}
				}				
				break;
			case 0:// 下阵
				if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_COMM) {
					resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_4);
					return resp;
				}
				if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
					resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_5);
					return resp;
				}
				position = HeroInfo.DEPLOY_TYPE_COMM;
				break;
			default:
				resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_6);
				return resp;
			}
			Map<Integer, Byte> upMap = new HashMap<Integer, Byte>();
			upMap.put(heroId, position);
			if (changeHeroInfo != null) {
				upMap.put(changeHeroInfo.getId(), changePosition);
			}

			if (HeroDAO.getInstance().updateHeroDeployStatus(upMap)) {
				heroInfo.setDeployStatus(position);
				if (changeHeroInfo != null) {
					changeHeroInfo.setDeployStatus(changePosition);
				}
				// 刷新角色羁绊列表
				HeroService.recalRoleJbHeroNos(roleInfo);
				HeroService.refeshHeroProperty(roleInfo, heroInfo, changeHeroInfo);
			} else {
				resp.setResult(ErrorCode.DEPLOY_CHANGE_ERROR_7);
				return resp;
			}

			// 日志
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action85.getType(), changeType+","+heroId+","+position);
			// 武将上下阵红点监听
			RedPointMgtService.check2PopRedPoint(roleId, null, true, GameValue.RED_POINT_TYPE_HERO);
			
			if(changeType == 1 ){
				// 上阵判断新手引导是否要更新
				int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.DEPLOY_NODES);
				if(ck != 1){
					resp.setResult(ck);
					return resp;
				}
			}
			
			resp.setResult(1);
			resp.setHeroId(heroId);
			resp.setPosition(heroInfo.getDeployStatus());
			resp.setFightValue(heroInfo.getFightValue());
			if (changeHeroInfo != null) {
				resp.setChangeHeroId(changeHeroInfo.getId());
				resp.setChangeHeroPosition(changeHeroInfo.getDeployStatus());
				resp.setChangeHeroFightValue(changeHeroInfo.getFightValue());
			}
			List<DeployChangeJBRe> list = getDeployChangeJBList(roleInfo, heroInfo, changeHeroInfo);
			resp.setCount(list.size());
			resp.setList(list);
			return resp;
		}
	}

	/**
	 * 获取羁绊引起上阵武将的战斗力变化
	 * @param heroInfo
	 * @param changeHero
	 * @return
	 */
	private List<DeployChangeJBRe> getDeployChangeJBList(RoleInfo roleInfo, HeroInfo heroInfo, HeroInfo changeHero) {
		List<DeployChangeJBRe> list = new ArrayList<DeployChangeJBRe>();
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
		if (heroMap != null) {
			DeployChangeJBRe re = null;
			for (HeroInfo hero : heroMap.values()) {
				if (heroInfo != null && heroInfo.getId() == hero.getId()) {
					continue;
				}
				if (changeHero != null && changeHero.getId() == hero.getId()) {
					continue;
				}
				if (hero.getDeployStatus() > HeroInfo.DEPLOY_TYPE_MAIN
						&& hero.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT) {
					int oldfightValue = hero.getFightValue();
					HeroService.refeshHeroProperty(roleInfo, hero);
					if (hero.getFightValue() != oldfightValue) {
						re = new DeployChangeJBRe();
						re.setHeroId(hero.getId());
						re.setFightValue(hero.getFightValue());
						list.add(re);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 购买上阵位置
	 * @param roleId
	 * @param req
	 * @return
	 */
	public BuyDeployPosResp buyDeployPos(int roleId, BuyDeployPosReq req) {
		BuyDeployPosResp resp = new BuyDeployPosResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			byte pos = req.getPosition();
			int costGold = 0;
			List<Integer> deployPosOpen = new ArrayList<Integer>();
			deployPosOpen.addAll(roleInfo.getRoleLoadInfo().getDeployPosOpen());
			switch (pos) {
			case 12:
				Integer vipVal = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.JB_1);
				if (vipVal != null && vipVal == 0) {
					resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_7);
					return resp;
				}

				if (FightDeployService.checkDeployPosOpen(roleInfo, pos)) {
					resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_3);
					return resp;
				}
				costGold = GameValue.GAME_DEPLOY_POS_OPEN_GOLD_COST[0];
				deployPosOpen.set(0, 1);
				break;
			case 13:
				Integer vipVal2 = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.JB_2);
				if (vipVal2 != null && vipVal2 == 0) {
					resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_7);
					return resp;
				}
				if (FightDeployService.checkDeployPosOpen(roleInfo, pos)) {
					resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_3);
					return resp;
				}
				if (!FightDeployService.checkDeployPosOpen(roleInfo, pos - 1)) {
					resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_4);
					return resp;
				}
				costGold = GameValue.GAME_DEPLOY_POS_OPEN_GOLD_COST[1];
				deployPosOpen.set(1, 1);
				break;
			default:
				resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_2);
				return resp;
			}
			if (costGold > 0) {
				List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
				conds.add(new CoinCond(costGold));
				int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}
				if (RoleService.subRoleResource(ActionType.action91.getType(), roleInfo, conds , null)) {
					String updateSourceStr = RoleService.returnResourceChange(conds);
					if (updateSourceStr != null) {
						String[] sourceStr = updateSourceStr.split(",");
						if (sourceStr != null && sourceStr.length > 1) {
							resp.setSourceType(Byte.valueOf(sourceStr[0]));
							resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
						}
					}
				} else {
					resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_5);
					return resp;
				}
			}

			if (RoleDAO.getInstance().updateDeployPosOpen(roleId, deployPosOpen)) {
				roleInfo.getRoleLoadInfo().setDeployPosOpen(deployPosOpen);
			} else {
				resp.setResult(ErrorCode.DEPLOY_POS_BUY_ERROR_6);
				return resp;
			}

			// 日志
			String value = pos+","+costGold;
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action91.getType(), value);
			// 武将上下阵红点监听
			RedPointMgtService.check2PopRedPoint(roleId, null, true, GameValue.RED_POINT_TYPE_HERO);

			resp.setResult(1);
			resp.setDeployPosOpenStr(roleInfo.getRoleLoadInfo().getDeployPosOpenStr());
			return resp;
		}
	}
}
