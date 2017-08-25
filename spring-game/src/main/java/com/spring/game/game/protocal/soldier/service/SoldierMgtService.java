package com.snail.webgame.game.protocal.soldier.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.SoldierXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.SoldierXMLInfo;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.soldier.upgrade.UpgradeSoldierReq;
import com.snail.webgame.game.protocal.soldier.upgrade.UpgradeSoldierResp;

public class SoldierMgtService {
	/**
	 * 士兵升级
	 * 
	 * @param roleId
	 * @param req
	 * @return
	 */
	public UpgradeSoldierResp upgradeSoldier(int roleId, UpgradeSoldierReq req) {
		UpgradeSoldierResp resp = new UpgradeSoldierResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_1);
			return resp;
		}

		byte soldierType = req.getSoldierType();
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_2);
				return resp;
			}
			int mainHeroLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
			// 判断开启等级
			if (mainHeroLv < GameValue.SOLDIER_UPGRADE_OPEN_LEVEL) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_10);
				return resp;
			}
			String[] soldierInfos = SoldierService.getSoldierInfos(roleInfo);
			if (soldierInfos == null) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_2);
				return resp;
			}
			// 验证类型是否存在
			if (soldierType > soldierInfos.length) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_3);
				return resp;
			}

			// 当前士兵等级
			int soldierCurrentLevel = Integer
					.parseInt(soldierInfos[soldierType - 1]);
			if (soldierCurrentLevel >= mainHeroLv) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_9);
				return resp;
			}

			SoldierXMLInfo soldierXMLInfo = SoldierXMLInfoMap
					.getSoldierXMLInfo(soldierType, soldierCurrentLevel + 1);
			if (soldierXMLInfo == null) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_8);
				return resp;
			}

			soldierXMLInfo = SoldierXMLInfoMap.getSoldierXMLInfo(soldierType,
					soldierCurrentLevel);
			if (soldierXMLInfo == null) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_4);
				return resp;
			}

			// 可以免费升级
			if (VipXMLInfoMap.getVipVal(roleInfo.getVipLv(),
					VipType.SOLDIER_FREE_UP_NUM) > 0) {
				if (!DateUtil.isSameDay(System.currentTimeMillis(),
						roleLoadInfo.getSoldierFreeUpTime().getTime())) {
					// 第二天更新免费升级次数
					roleLoadInfo.setSoldierFreeUpNum((short) 0);
				}
			}

			// 验证资源
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			if (roleLoadInfo.getSoldierFreeUpNum() >= VipXMLInfoMap.getVipVal(
					roleInfo.getVipLv(), VipType.SOLDIER_FREE_UP_NUM)) {
				conds.add(new MoneyCond(soldierXMLInfo.getMoney()));
				int check = AbstractConditionCheck.checkCondition(roleInfo,
						conds);
				;
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}
			}

			if (!BagItemMap.checkBagItemNum(roleInfo,
					soldierXMLInfo.getPropNo(), soldierXMLInfo.getPropNum())) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_5);
				return resp;
			}

			if (roleLoadInfo.getSoldierFreeUpNum() >= VipXMLInfoMap.getVipVal(
					roleInfo.getVipLv(), VipType.SOLDIER_FREE_UP_NUM)) {
				// 扣除资源
				if (!RoleService.subRoleResource(
						ActionType.action57.getType(), roleInfo, conds , null)) {
					resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_6);
					return resp;
				}

				resp.setSourceType((byte) ConditionType.TYPE_MONEY.getType());
				resp.setSoruceNum(-soldierXMLInfo.getMoney());
			} else {
				// 免费
				if (!RoleDAO.getInstance().updateSoldierFreeUpNum(
						roleInfo.getId(),
						roleLoadInfo.getSoldierFreeUpNum() + 1)) {
					resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_11);
					return resp;
				}

				roleLoadInfo.setSoldierFreeUpNum((short) (roleLoadInfo
						.getSoldierFreeUpNum() + 1));
				roleLoadInfo.setSoldierFreeUpTime(new Timestamp(System
						.currentTimeMillis()));
			}

			Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
			itemMap.put(soldierXMLInfo.getPropNo(), soldierXMLInfo.getPropNum());

			int result = ItemService.bagItemDel(
					ActionType.action57.getType(), roleInfo, itemMap);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			int randomNum = RandomUtil.getRandom(1, 100); // 随机概率

			boolean isUp = false; // 是否升级

			int upRate = 0; // 增加的升级概率

			if (req.getCoin() > 0) {
				int cost = req.getCoin();
				upRate = cost / GameValue.BUY_SOLDIER_UP_PROBABILITY;

				if ((soldierXMLInfo.getChance() + upRate) > 100) {
					// 最大概率不能超过100
					upRate = 100 - soldierXMLInfo.getChance();
					cost = upRate * GameValue.BUY_SOLDIER_UP_PROBABILITY;
				}
				conds.clear(); // 之前银子的 扣除过了 去除掉 只关注与概率的金子
				conds.add(new CoinCond(cost));
				int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
				
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}

				// 扣除资源
				if (!RoleService.subRoleResource(ActionType.action58.getType(), roleInfo, conds , null)) {
					resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_6);
					return resp;
				}

				resp.setCostCoin(cost);

			}

			if (randomNum > 0 && randomNum <= (soldierXMLInfo.getChance() + upRate)) {
				// 升级成功
				isUp = true;

			}

			Map<Byte, Byte> upInfoMap = SoldierService.getSoldierUpCounterInfoMap(roleLoadInfo);

			if (isUp) {
				// 升级
				if (upInfoMap.containsKey(soldierType)) {
					// 去除counter
					upInfoMap.remove(soldierType);
				}
			} else {
				// 没有升级
				if (!upInfoMap.containsKey(soldierType)) {
					upInfoMap.put(soldierType, (byte)0);
				}
				
				byte counter = upInfoMap.get(soldierType);
				if (counter + 1  >= soldierXMLInfo.getLimit()) {
					// 本次达到最大升级次数升级
					isUp = true;
					upInfoMap.remove(soldierType);
				} else {
					upInfoMap.put(soldierType, (byte) (counter + 1));

				}
				
			}

			if (isUp) {
				// 升级兵种
				soldierInfos[soldierType - 1] = String.valueOf(++soldierCurrentLevel);
				StringBuffer newSoldierInfos = new StringBuffer();
				for (String oneSoldierLevel : soldierInfos) {
					if (newSoldierInfos.length() > 0) {
						newSoldierInfos.append(",");
					}
					newSoldierInfos.append(oneSoldierLevel);
				}

				if (!RoleDAO.getInstance().updateSolderInfo(roleInfo.getId(), newSoldierInfos.toString())) {
					resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_7);
					return resp;
				}
				roleInfo.setSoldierInfo(newSoldierInfos.toString());

				// 刷新武将属性
				Map<Integer, HeroInfo> heroInfoMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
				if (heroInfoMap != null) {
					Iterator<Integer> keyIterator = heroInfoMap.keySet().iterator();
					HeroInfo heroInfo = null;
					HeroXMLInfo heroXMLInfo = null;
					StringBuffer refreshHeroIds = new StringBuffer("");
					while (keyIterator.hasNext()) {
						heroInfo = heroInfoMap.get(keyIterator.next());
						if (heroInfo == null) {
							continue;
						}

						heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
						if (heroXMLInfo == null) {
							continue;
						}

						if (heroXMLInfo.getHeroType() == soldierType
								|| heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
							HeroService.refeshHeroProperty(roleInfo, heroInfo);
							if (refreshHeroIds.length() > 0) {
								refreshHeroIds.append(",");
							}
							refreshHeroIds.append(heroInfo.getId());
						}
					}

					if (refreshHeroIds.length() > 0) {
						SceneService.sendRoleRefreshMsg(roleInfo.getId(),
								SceneService.REFESH_TYPE_HERO,
								refreshHeroIds.toString());
					}
				}
			}

			String upInfo = SoldierService.getSoldierUpCounterInfo(upInfoMap);
			if (!RoleDAO.getInstance().updateSolderUpCounterInfo(
					roleInfo.getId(), upInfo)) {
				resp.setResult(ErrorCode.SOLDIER_UPGRADE_ERROR_7);
				return resp;
			}

			roleLoadInfo.setSoldierUpCounterInfo(upInfo);

			// 红点检测
			boolean isRed = RedPointMgtService.check2PopRedPoint(
					roleInfo.getId(), null, false,
					RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);
			if (isRed) {
				RedPointMgtService.pop(roleInfo.getId());
			}

			resp.setLevel(SoldierService.getSoldierLevel(roleInfo,
					req.getSoldierType()));
			resp.setSoldierType(req.getSoldierType());

			int afterLv = soldierCurrentLevel;
			byte up = 0;
			if (isUp) {
				afterLv++;
				up = 1;
			}

			GameLogService.insertSoliderUpLog(roleInfo, soldierType, up, soldierCurrentLevel, afterLv,soldierXMLInfo.getMoney(),soldierXMLInfo.getPropNo(), soldierXMLInfo.getPropNum());
				
		}
		resp.setResult(1);
		return resp;
	}
}
