package com.snail.webgame.game.protocal.goldBuy.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.goldBuy.goldBuy.GoldBuyRe;
import com.snail.webgame.game.protocal.goldBuy.goldBuy.GoldBuyReq;
import com.snail.webgame.game.protocal.goldBuy.goldBuy.GoldBuyResp;
import com.snail.webgame.game.protocal.goldBuy.query.QueryGoldBuyReq;
import com.snail.webgame.game.protocal.goldBuy.query.QueryGoldBuyResp;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.GoldBuyXMLInfoMap;
import com.snail.webgame.game.xml.info.GoldBuyXMLInfo;
import com.snail.webgame.game.xml.info.GoldBuyXMLPro;

public class GoldBuyMgtService {

	/**
	 * 查询金币购买
	 * @param roleId
	 * @param req
	 * @return
	 */
	public QueryGoldBuyResp queryGoldBuy(int roleId, QueryGoldBuyReq req) {
		QueryGoldBuyResp resp = new QueryGoldBuyResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_13);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_LOAD_ERROR_3);
				return resp;
			}
			byte buyType = req.getBuyType();
			GoldBuyXMLInfo xmlInfo = GoldBuyXMLInfoMap.getGoldBuyXMLInfo(buyType);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.GOLD_BUY_ERROR_1);
				return resp;
			}
			if (xmlInfo.getPros() == null) {
				resp.setResult(ErrorCode.GOLD_BUY_ERROR_1);
				return resp;
			}
			int buyNum = 0;
			int maxBuyNum = getMaxBuyNum(roleInfo ,buyType);
			switch (buyType) {
			// 银币购买
			case GoldBuyXMLInfo.TYPE_MONEY_BUY:
				buyNum = roleLoadInfo.getTodayBuyMoneyNum();
				break;
			// 体力购买
			case GoldBuyXMLInfo.TYPE_SP_BUY:
				buyNum = roleLoadInfo.getTodayBuySpNum();
				maxBuyNum = GameValue.ROLE_SP_BUY_NUM;
				break;
			case GoldBuyXMLInfo.TYPE_TECH_BUY:
				buyNum = roleLoadInfo.getTodayBuyTechNum();
				break;
			// 精力购买
			case GoldBuyXMLInfo.TYPE_ENERGY_BUY:
				buyNum = roleLoadInfo.getTodayBuyEnergyNum();
				maxBuyNum = GameValue.ROLE_ENERGY_BUY_NUM;
				break;
			default:
				resp.setResult(ErrorCode.GOLD_BUY_TYPE_ERROR_1);
				return resp;
			}

			int costGold = 0;
			int gain = 0;
			GoldBuyXMLPro pro = null;
			if (buyNum < xmlInfo.getMaxBuyNum()) {
				pro = xmlInfo.getPros().get(buyNum + 1);
			} else {
				pro = xmlInfo.getMaxBuyXMLPro();
			}
			if (pro != null) {
				costGold = pro.getGold();
				gain = getGain(roleInfo, xmlInfo, pro.getGain());
			}

			int repeatNum = GameValue.ROLE_GOLD_BUY_REPEAT;
			if (repeatNum + buyNum > maxBuyNum && maxBuyNum != -1) {
				repeatNum = maxBuyNum - buyNum;
				if (repeatNum < 0) {
					repeatNum = 0;
				}
			}
			int costGoldRepeat = 0;// 连续使用消耗金子
			int gainRepeat = 0;// 连续使用获取值(公式计算)
			if (repeatNum > 0) {
				GoldBuyXMLPro pro1 = null;
				for (int currBuyNum = buyNum + 1; currBuyNum <= buyNum + repeatNum; currBuyNum++) {
					if (currBuyNum < xmlInfo.getMaxBuyNum()) {
						pro1 = xmlInfo.getPros().get(currBuyNum);
					} else {
						pro1 = xmlInfo.getMaxBuyXMLPro();
					}
					if (pro1 != null) {
						costGoldRepeat += pro1.getGold();
						gainRepeat += getGain(roleInfo, xmlInfo, pro1.getGain());
					}
				}
			}

			resp.setResult(1);
			resp.setBuyType(buyType);
			resp.setBuyNum((byte) buyNum);
			resp.setMaxBuyNum((byte) maxBuyNum);

			resp.setCostGold(costGold);
			resp.setGain(gain);

			resp.setRepeatNum(repeatNum);
			resp.setCostGoldRepeat(costGoldRepeat);
			resp.setGainRepeat(gainRepeat);
			return resp;
		}
	}

	/**
	 * 获得值
	 * @param roleInfo
	 * @param xmlInfo
	 * @param baseGain
	 * @return
	 */
	private int getGain(RoleInfo roleInfo, GoldBuyXMLInfo xmlInfo, int baseGain) {
		// TODO 等级影响计算公式（待调整）
		if (baseGain <= 0) {
			return 0;
		}
		// 1受等级影响 2不受等级影响
		if (xmlInfo.getType() == 1) {
			return baseGain * (1 + HeroInfoMap.getMainHeroLv(roleInfo.getId()) / 100);
		} else {
			return baseGain;
		}
	}

	/**
	 * 获取上限
	 * @param roleInfo
	 * @param buyType
	 * @return -1 无上限
	 */
	private int getMaxBuyNum(RoleInfo roleInfo, int buyType) {
		int result = 0;
		GoldBuyXMLInfo xmlInfo = GoldBuyXMLInfoMap.getGoldBuyXMLInfo(buyType);
		if (xmlInfo != null && xmlInfo.getPros() != null) {
			GoldBuyXMLPro pro = null;
			for (int i = xmlInfo.getMaxBuyNum(); i > 0; i--) {
				pro = xmlInfo.getPros().get(i);
				if (pro != null) {
					int check = AbstractConditionCheck.checkCondition(roleInfo, pro.getConditions());
					if (check == 1) {
						result = i;
						break;
					}
				}
			}
			// 0:固定次数 1:无限次数，超过最后一次后价格不递增，按照最后一次的价格来
			if (result == xmlInfo.getMaxBuyNum() && xmlInfo.getFixed() == 1) {
				result = -1;
			}
		}
		return result;
	}

	/**
	 * 购买
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GoldBuyResp goldBuy(int roleId, GoldBuyReq req) {
		GoldBuyResp resp = new GoldBuyResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
			return resp;
		}
		synchronized (roleInfo) {
			byte buyType = req.getBuyType();
			byte repeat = req.getRepeat();// 0-单次 1-连续
			GoldBuyXMLInfo xmlInfo = GoldBuyXMLInfoMap.getGoldBuyXMLInfo(buyType);
			if (xmlInfo == null || repeat < 0 || repeat > 1) {
				resp.setResult(ErrorCode.GOLD_BUY_ERROR_2);
				return resp;
			}
			int before = 0;
			int buyNum = 0;
			int action = 0;
			ConditionType resource_type = null;
			int maxBuyNum = getMaxBuyNum(roleInfo ,buyType);
			switch (buyType) {
			// 购买银子
			case GoldBuyXMLInfo.TYPE_MONEY_BUY:
				buyNum = roleLoadInfo.getTodayBuyMoneyNum();
				action = ActionType.action10.getType();
				resource_type = ConditionType.TYPE_MONEY;
				before = (int) roleInfo.getMoney();
				if (roleInfo.getMoney() >= GameValue.MONEY_MAX) {
					resp.setResult(ErrorCode.BUY_MONEY_ERROR_1);
					return resp;
				}
				break;
			// 购买体力
			case GoldBuyXMLInfo.TYPE_SP_BUY:
				if (GameValue.BUY_ENY_SP_OPEN != 1) {
					resp.setResult(ErrorCode.GOLD_BUY_TYPE_ERROR_5);
					return resp;
				}
				buyNum = roleLoadInfo.getTodayBuySpNum();
				action = ActionType.action13.getType();
				resource_type = ConditionType.TYPE_SP;
				before = roleInfo.getSp();
				// 因为现在体力不是实时计算的，所以购买体力的一刻一定需要把玩家之前时间回复的先加上
				RoleService.timerRecoverSp(roleInfo);
				if (roleInfo.getSp() >= GameValue.ROLE_SP_MAX_1) {
					resp.setResult(ErrorCode.BUY_SP_ERROR_1);
					return resp;
				}
				break;
			// 购买精力
			case GoldBuyXMLInfo.TYPE_ENERGY_BUY:
				if (GameValue.BUY_ENY_SP_OPEN != 1) {
					resp.setResult(ErrorCode.GOLD_BUY_TYPE_ERROR_5);
					return resp;
				}
				buyNum = roleLoadInfo.getTodayBuyEnergyNum();
				action = ActionType.action23.getType();
				resource_type = ConditionType.TYPE_ENERGY;
				before = roleInfo.getEnergy();
				// 因为现在体力不是实时计算的，所以购买体力的一刻一定需要把玩家之前时间回复的先加上
				RoleService.timerRecoverEnergy(roleInfo);
				if (roleInfo.getEnergy() >= GameValue.ROLE_ENERGY_MAX_1) {
					resp.setResult(ErrorCode.BUY_ENERGY_ERROR_1);
					return resp;
				}
				break;
			case GoldBuyXMLInfo.TYPE_TECH_BUY:
				buyNum = roleLoadInfo.getTodayBuyTechNum();
				action = ActionType.action26.getType();
				resource_type = ConditionType.TYPE_TECH;
				before = roleInfo.getTech();
				// 因为现在体力不是实时计算的，所以购买体力的一刻一定需要把玩家之前时间回复的先加上
				RoleService.timerRecoverTech(roleInfo);
				if (roleInfo.getTech() >= GameValue.ROLE_TECH_MAX) {
					resp.setResult(ErrorCode.BUY_TECH_ERROR_1);
					return resp;
				}
				break;
			default:
				resp.setResult(ErrorCode.GOLD_BUY_TYPE_ERROR_2);
				return resp;
			}
			GoldBuyXMLPro pro = null;
			
			int lastBuyNum = buyNum + 1;
			
			if (maxBuyNum != -1 && buyNum >= maxBuyNum) {
				if (lastBuyNum > xmlInfo.getMaxBuyNum()) {
					resp.setResult(ErrorCode.GOLD_BUY_TYPE_ERROR_4);
					return resp;
				} else {
					//errorCode 提示
					pro = xmlInfo.getPros().get(buyNum + 1);
					if (pro != null) {
						int check = AbstractConditionCheck.checkCondition(roleInfo, pro.getConditions());
						if (check != 1) {
							resp.setResult(check);
							return resp;
						}
					}
				}
			}
			
			if (repeat != 0) {
				lastBuyNum = buyNum + GameValue.ROLE_GOLD_BUY_REPEAT;
				if (lastBuyNum > maxBuyNum && maxBuyNum != -1) {
					lastBuyNum = maxBuyNum;
				}
			}
			List<GoldBuyRe> list = new ArrayList<GoldBuyRe>();
			GoldBuyRe re = null;
			long totalCost = 0;
			long totalAdd = 0;
			for (int currBuyNum = buyNum + 1; currBuyNum <= lastBuyNum; currBuyNum++) {
				if (currBuyNum < xmlInfo.getMaxBuyNum()) {
					pro = xmlInfo.getPros().get(currBuyNum);
				} else {
					pro = xmlInfo.getMaxBuyXMLPro();
				}
				if (pro == null) {
					resp.setResult(ErrorCode.XML_PARAM_ERROR);
					return resp;
				}
				int check = AbstractConditionCheck.checkCondition(roleInfo, pro.getConditions());
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}
				int gold = pro.getGold();
				totalCost += gold;
				int gain = getGain(roleInfo, xmlInfo, pro.getGain());
				float mul = GoldBuyXMLInfoMap.getMul(pro.getMulRandNo());
				long add = (long) (gain * mul);
				totalAdd += add;
				re = new GoldBuyRe();
				re.setCostGold(gold);
				re.setGain(gain);
				re.setMul(mul);
				list.add(re);
			}
			if (totalCost > roleInfo.getCoin()) {
				resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
				return resp;
			}

			// 扣除消耗
			if (RoleService.subRoleRoleResource(action, roleInfo, ConditionType.TYPE_COIN, totalCost ,null)) {
				// 购买银子时,后面有刷新角色信息,里面含有金子与银子
				if (buyType == GoldBuyXMLInfo.TYPE_SP_BUY || buyType == GoldBuyXMLInfo.TYPE_ENERGY_BUY
						|| buyType == GoldBuyXMLInfo.TYPE_TECH_BUY) {
					resp.setSourceType((byte) ConditionType.TYPE_COIN.getType());
					resp.setSourceChange((int) -totalCost);
				}
			} else {
				resp.setResult(ErrorCode.SUB_RESOURCE_ERROR_1);
				return resp;
			}

			// 更新购买次数
			Timestamp buyTime = new Timestamp(System.currentTimeMillis());
			int result = RoleService.updateRoleResourceBuyNum(roleInfo, resource_type, lastBuyNum, buyTime);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_GOLD_BUY, buyType);
			// 添加获取
			int after = 0;
			if (RoleService.addRoleRoleResource(action, roleInfo, resource_type, totalAdd,null)) {
				switch (resource_type) {
				case TYPE_SP:
					after = roleInfo.getSp();
					// 推送体力变化
					String spStr = roleInfo.getSp() + "," + roleInfo.getLastRecoverSPTime().getTime() + ","
							+ roleLoadInfo.getTodayBuySpNum() + "," + 0;
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_SP, spStr);
					break;
				case TYPE_TECH:
					after = roleInfo.getTech();
					String techStr = roleInfo.getTech() + "," + roleInfo.getLastRecoverTechTime().getTime() + ","
							+ roleLoadInfo.getTodayBuyTechNum() + "," + 0;
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_TECH, techStr);
					break;
				case TYPE_ENERGY:
					after = roleInfo.getEnergy();
					String energyStr = roleInfo.getEnergy() + "," + roleInfo.getLastRecoverEnergyTime().getTime() + ","
							+ roleLoadInfo.getTodayBuyEnergyNum() + "," + 0;
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_ENERGY, energyStr);
					break;
				case TYPE_MONEY:
					after = (int) roleInfo.getMoney();
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
					break;
				default:
					break;
				}
			} else {
				resp.setResult(ErrorCode.GOLD_BUY_ERROR_4);
				return resp;
			}
			
			//红点检测
			RedPointMgtService.check2PopRedPoint(roleId, null, true, RedPointMgtService.LISTENING_GOLD_BUY);
			
			resp.setResult(1);
			resp.setBuyType(buyType);
			resp.setList(list);
			resp.setCount(list.size());
			//String res = lastBuyNum + "-" + totalAdd;
			GameLogService.insertGoldBuyLog(roleInfo, action, resource_type, before, after, (int) totalCost, buyNum,
					lastBuyNum);
			
			if (buyType == GoldBuyXMLInfo.TYPE_SP_BUY || buyType == GoldBuyXMLInfo.TYPE_MONEY_BUY) {
				QuestService.checkQuest(roleInfo, action, lastBuyNum - buyNum, true, true);
			}
			
			return resp;
		}
	}
}
