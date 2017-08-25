package com.snail.webgame.game.protocal.shizhuang.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.typehandler.IntegerMapTypeHandler;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.shizhuang.getReward.GetRewardReq;
import com.snail.webgame.game.protocal.shizhuang.getReward.GetRewardResp;
import com.snail.webgame.game.protocal.shizhuang.lock.ShizhuangLockReq;
import com.snail.webgame.game.protocal.shizhuang.lock.ShizhuangLockResp;
import com.snail.webgame.game.protocal.shizhuang.updatePlan.UpdateShowPlanReq;
import com.snail.webgame.game.protocal.shizhuang.updatePlan.UpdateShowPlanResp;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;

/**
 * 时装逻辑处理类
 * @author nijy
 */
public class ShizhuangMgtService {

	/**
	 * 锁定/解锁 时装属性
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ShizhuangLockResp lockShizhuang(int roleId, ShizhuangLockReq req) {
		ShizhuangLockResp resp = new ShizhuangLockResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_2);
				return resp;
			}
			String equipIds = req.getEquipIds();
			if (equipIds == null) {
				resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_3);
				return resp;
			}
			String equipIdStrs[] = equipIds.split(",");
			if (equipIdStrs == null || equipIdStrs.length <= 0) {
				resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_3);
				return resp;
			}

			int equipId = 0;
			EquipInfo equipInfo = null;
			EquipXMLInfo equipXMLInfo = null;
			Integer oldEquipId = null;
			Map<Integer, Integer> allLock = new HashMap<Integer, Integer>();
			allLock.putAll(roleInfo.getLockShizhuang());
			int lockType = req.getLockType();
			switch (lockType) {
			case 1:// 上锁
				boolean change = false;
				for (String equipIdStr : equipIdStrs) {
					equipId = NumberUtils.toInt(equipIdStr);
					equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
					if (equipInfo == null) {
						equipInfo = EquipInfoMap.getBagEquip(roleId, equipId);
					}
					if (equipInfo == null) {
						resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_3);
						return resp;
					}
					equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
					if (equipXMLInfo == null || (equipXMLInfo.getEquipType() != 9 && equipXMLInfo.getEquipType() != 10)) {
						resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_4);
						return resp;
					}
					oldEquipId = allLock.get(equipXMLInfo.getEquipType());
					if (oldEquipId == null || oldEquipId != equipId) {
						allLock.put(equipXMLInfo.getEquipType(), equipId);
						if (!change) {
							change = true;
						}
					}
				}
				if (change && RoleDAO.getInstance().updateLockShizhuang(roleInfo.getId(), allLock)) {
					roleInfo.setLockShizhuang(allLock);
					ShizhuangService.refreshRoleLockShizhuang(roleInfo);
					HeroService.refeshHeroProperty(roleInfo, heroInfo);
				}

				break;
			case 2:// 解锁
				for (String equipIdStr : equipIdStrs) {
					equipId = NumberUtils.toInt(equipIdStr);
					equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
					if (equipInfo == null) {
						equipInfo = EquipInfoMap.getBagEquip(roleId, equipId);
					}
					if (equipInfo == null) {
						resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_5);
						return resp;
					}
					equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
					if (equipXMLInfo == null || (equipXMLInfo.getEquipType() != 9 && equipXMLInfo.getEquipType() != 10)) {
						resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_6);
						return resp;
					}
					oldEquipId = allLock.get(equipXMLInfo.getEquipType());
					if (oldEquipId == null || oldEquipId != equipId) {
						resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_7);
						return resp;
					}
					allLock.remove(equipXMLInfo.getEquipType());
				}
				if (RoleDAO.getInstance().updateLockShizhuang(roleInfo.getId(), allLock)) {
					roleInfo.setLockShizhuang(allLock);
					ShizhuangService.refreshRoleLockShizhuang(roleInfo);
					HeroService.refeshHeroProperty(roleInfo, heroInfo);
				}

				break;
			default:
				resp.setResult(ErrorCode.ROLE_SZ_LOCL_ERROR_8);
				return resp;
			}
			resp.setResult(1);
			resp.setLockShizhuang(IntegerMapTypeHandler.getString(allLock));
			resp.setFightValue(heroInfo.getFightValue());
			return resp;
		}
	}

	/**
	 * 领取时装套装奖励
	 */
	public GetRewardResp getShizhuangReward(int roleId, GetRewardReq req) {
		GetRewardResp resp = new GetRewardResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_SZ_GETREWARD_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_SZ_GETREWARD_ERROR_2);
				return resp;
			}
			int num = req.getNum();
			if (!GameValue.SHIZHUANG_REWARD_MAP.containsKey(num)) {
				resp.setResult(ErrorCode.ROLE_SZ_GETREWARD_ERROR_3);
				return resp;
			}

			// 检测是否已领取
			if (roleLoadInfo.getHaveReward().contains(num)) {
				resp.setResult(ErrorCode.ROLE_SZ_GETREWARD_ERROR_4);
				return resp;
			}

			// 检测是否够条件
			int all = ShizhuangService.shizhuangTypeCondition(roleInfo);
			if (all < num) {
				resp.setResult(ErrorCode.ROLE_SZ_GETREWARD_ERROR_5);
				return resp;
			}
			List<Integer> haveReward = new ArrayList<Integer>();
			haveReward.addAll(roleLoadInfo.getHaveReward());
			haveReward.add(num);

			if (RoleDAO.getInstance().updateHaveReward(roleInfo.getId(), haveReward)) {
				roleLoadInfo.setHaveReward(haveReward);
			} else {
				resp.setResult(ErrorCode.ROLE_SZ_GETREWARD_ERROR_6);
				return resp;
			}
			// 领取
			List<DropInfo> addList = new ArrayList<DropInfo>();
			List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLList(GameValue.SHIZHUANG_REWARD_MAP.get(num));

			if (list.size() > 0) {
				ItemService.getDropXMLInfo(roleInfo, list, addList);
			}
			List<BattlePrize> dropList = new ArrayList<BattlePrize>();
			int result = ItemService.addPrize(ActionType.action481.getType(), roleInfo, addList, null, null, null,
					null, null, null, dropList, null, null, true);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			if (dropList != null && dropList.size() > 0) {
				resp.setPrizeNum(dropList.size());
				resp.setPrize(dropList);
			}
			resp.setResult(1);
			resp.setNum(num);
			return resp;
		}
	}

	/**
	 * 修改玩家显示时装/套装方案
	 * @param roleid
	 * @param req
	 */
	public UpdateShowPlanResp updateShopPlan(int roleId, UpdateShowPlanReq req) {
		UpdateShowPlanResp resp = new UpdateShowPlanResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_SZ_UPDATE_PLAN_ERROR_1);
			return resp;
		}
		int planId = req.getPlanId();// 1-显示套装 0-显示时装
		if (planId != 0 && planId != 1) {
			resp.setResult(ErrorCode.ROLE_SZ_UPDATE_PLAN_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			if (planId == roleInfo.getIsShowShizhuang()) {
				resp.setResult(ErrorCode.ROLE_SZ_UPDATE_PLAN_ERROR_3);
				return resp;
			}
			if (RoleDAO.getInstance().updateShopPlan(roleInfo.getId(), planId)) {
				roleInfo.setIsShowShizhuang(planId);
			}

			resp.setResult(1);
			resp.setPlanId(planId);
			return resp;
		}
	}
}
