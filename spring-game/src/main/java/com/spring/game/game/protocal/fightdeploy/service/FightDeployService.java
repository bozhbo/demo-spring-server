package com.snail.webgame.game.protocal.fightdeploy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.info.FightDeployInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fightdeploy.query.FightDeployRe;
import com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;

public class FightDeployService {

	/**
	 * 判断布阵位置是否开启
	 * @param roleInfo
	 * @param position
	 * @return
	 */
	public static boolean checkDeployPosOpen(RoleInfo roleInfo, int position) {
		if (position == HeroInfo.DEPLOY_TYPE_COMM) {
			return true;
		} else if (position == 12 || position == 13) {
			RoleLoadInfo loadInfo = roleInfo.getRoleLoadInfo();
			if (loadInfo != null && position == 12) {
				return loadInfo.getDeployPosOpen().get(0) > 0;
			}
			if (loadInfo != null && position == 13) {
				return loadInfo.getDeployPosOpen().get(1) > 0;
			}
		} else {
			int mainHeroLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
			Integer openLv = GameValue.FIGHT_DEPLOY_FUN_OPEN_LV.get((byte)position);
			if (openLv == null || mainHeroLv >= openLv) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取玩家布阵
	 * @param roleId
	 * @return
	 */
	public static List<FightDeployInfo> getRoleFightDeployBy(int roleId) {
		List<FightDeployInfo> list = new ArrayList<FightDeployInfo>();
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM) {
					list.add(new FightDeployInfo(roleId, (int) heroInfo.getId(), (byte) 0, (byte) heroInfo
							.getDeployStatus()));
				}
			}
		}

		return list;
	}

	public static List<FightDeployRe> getFightDeployList(RoleInfo roleInfo, byte deployType) {
		List<FightDeployRe> list = new ArrayList<FightDeployRe>();
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
		for (HeroInfo heroInfo : heroMap.values()) {
			if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM) {
				list.add(new FightDeployRe(heroInfo.getId(), heroInfo.getDeployStatus()));
			}
		}
		return list;
	}

	public static List<FightDeployDetailRe> getFightDeployDetailList(RoleInfo roleInfo) {
		List<FightDeployDetailRe> list = new ArrayList<FightDeployDetailRe>();
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM) {
					list.add(getFightDeployDetailRe(heroInfo, heroInfo.getDeployStatus()));
				}
			}
		}
		return list;
	}

	/**
	 * 获取玩家布阵(不包含羁绊英雄)
	 * @param roleInfo
	 * @return
	 */
	public static List<FightDeployDetailRe> getFightDeployDetailTrueList(RoleInfo roleInfo) {
		List<FightDeployDetailRe> list = new ArrayList<FightDeployDetailRe>();
		Map<Integer, HeroInfo> heroMap = roleInfo.getHeroMap();
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() > HeroInfo.DEPLOY_TYPE_COMM
						&& heroInfo.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT) {
					list.add(getFightDeployDetailRe(heroInfo, heroInfo.getDeployStatus()));
				}
			}
		}
		return list;
	}

	private static FightDeployDetailRe getFightDeployDetailRe(HeroInfo heroInfo, byte deployPos) {
		FightDeployDetailRe re = new FightDeployDetailRe();
		re.setHeroNo(heroInfo.getHeroNo());

		re.setDeployPos(deployPos);
		re.setHeroLevel(heroInfo.getHeroLevel());
		re.setIntimacyLevel(heroInfo.getIntimacyLevel());
		re.setStar(heroInfo.getStar());
		re.setQuality(heroInfo.getQuality());
		re.setFightValue(heroInfo.getFightValue());
		return re;
	}

	public static FightDeployDetailRe getFightDeployDetailRe(RoleInfo roleInfo, Map<Byte, HeroRecord> map, HeroRecord record, double rate) {
		FightDeployDetailRe re = new FightDeployDetailRe();
		re.setHeroNo(record.getHeroNo());
		re.setDeployPos(record.getDeployStatus());
		re.setHeroLevel(record.getHeroLevel());
		re.setIntimacyLevel(record.getIntimacyLevel());
		re.setStar(record.getStar());
		re.setQuality(record.getQuality());
		if(record.getFightValue() == 0) {
			int fightValue = HeroRecordService.getHeroRecordFightValue(map, record, rate);
			record.setFightValue(fightValue);			
		}
		re.setFightValue(record.getFightValue());
		
		return re;
	}
}
