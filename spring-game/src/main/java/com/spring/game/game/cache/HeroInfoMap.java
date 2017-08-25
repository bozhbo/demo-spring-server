package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 英雄对象缓存类
 */
public class HeroInfoMap {

	/**
	 * 添加英雄缓存
	 * @param heroInfo
	 */
	public static void addHeroInfo(HeroInfo heroInfo, boolean isLoadFromDB) {
		int roleId = heroInfo.getRoleId();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			return;
		}
		
		if (roleInfo.getHeroMap() == null) {
			roleInfo.setHeroMap(new ConcurrentHashMap<Integer, HeroInfo>());
		}
		
		if (isLoadFromDB && roleInfo.getHeroMap().size() > 5) {
			// 启动只加载战斗力最高的五名
			return;
		}
		
		roleInfo.getHeroMap().put(heroInfo.getId(), heroInfo);
		if(heroInfo.getDeployStatus()!=1){
			roleInfo.setCommHeroNum(roleInfo.getCommHeroNum()+1);
		}
			// 给新武将添加默认装备,只有前四个副将给装备
//		if (roleInfo.getHeroMap().size() <= 5 && heroInfo.getDeployStatus()!=HeroInfo.DEPLOY_TYPE_MAIN && !isLoadFromDB) {
//			EquipService.addNewHeroEquips(heroInfo, isLoadFromDB, roleInfo);
//			HeroService.refeshHeroProperty(roleInfo, heroInfo, HeroInfo.PRO_TYPE_EQUIP);
//		}
	}
	
	/**
	 * 玩家上线加载武将
	 * @param heroInfo
	 */
	public static void roleLoginAddHero(HeroInfo heroInfo)
	{
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(heroInfo.getRoleId());
		
		if (roleInfo == null) {
			return;
		}
		
		if (roleInfo.getHeroMap() == null) {
			roleInfo.setHeroMap(new ConcurrentHashMap<Integer, HeroInfo>());
		}
		if (heroInfo.getDeployStatus() > GameValue.FIGHT_ARMY_LIMIT) {
			Map<Byte, Integer> jbHeroNoMap = roleInfo.getJbHeroNoMap();
			if (jbHeroNoMap == null) {
				jbHeroNoMap = new HashMap<Byte, Integer>();
			}
			jbHeroNoMap.put(heroInfo.getDeployStatus(), heroInfo.getHeroNo());
		}
		
		roleInfo.getHeroMap().put(heroInfo.getId(), heroInfo);
	}

	/**
	 * 根据roleId和heroId获取英雄
	 * @param roleId
	 * @param heroId
	 * @return
	 */
	public static HeroInfo getHeroInfo(int roleId, int heroId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			return null;
		}
		
		if (roleInfo.getHeroMap() == null) {
			return null;
		}

		return roleInfo.getHeroMap().get(heroId);
	}

	/**
	 * 移除英雄
	 * @param roleId
	 * @param heroId
	 */
	public static void removeHero(int roleId, int heroId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			return;
		}
		
		if (roleInfo.getHeroMap() == null) {
			return;
		}

		roleInfo.getHeroMap().remove(heroId);
	}

	/**
	 * 根据roleId 获取英雄
	 * @param roleId
	 * @return
	 */
	public static Map<Integer, HeroInfo> getHeroByRoleId(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			return null;
		}
		
		return roleInfo.getHeroMap();
	}

	/**
	 * 根据roleId和 英雄xml编号获取英雄
	 * @param roleId
	 * @param heroNo
	 * @return
	 */
	public static HeroInfo getHeroInfoByNo(int roleId, int heroNo) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			return null;
		}
		
		if (roleInfo.getHeroMap() == null) {
			return null;
		}

		for (HeroInfo heroInfo : roleInfo.getHeroMap().values()) {
			if (heroInfo.getHeroNo() == heroNo) {
				return heroInfo;
			}
		}
		return null;
	}

	/**
	 * 获取主武将
	 * @param roleId
	 * @return
	 */
	public static HeroInfo getMainHeroInfo(RoleInfo roleInfo) {
		
		if (roleInfo.getHeroMap() == null) {
			return null;
		}

		for (HeroInfo heroInfo : roleInfo.getHeroMap().values()) {
			if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
				return heroInfo;
			}
		}

		return null;
	}

	/**
	 * 获取默认上阵羁绊英雄
	 * @param roleId
	 * @return
	 */
	public static List<HeroInfo> getFightDeployHero(int roleId) {
		List<HeroInfo> list = new ArrayList<HeroInfo>();
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM) {
					list.add(heroInfo);
				}
			}
		}
		return list;
	}
		
	/**
	 * 获取当前上阵武将总战斗力
	 * @param roleId
	 * @return
	 */
	public static int getFightDeployHeroFightValue(int roleId){
		int fightValue = 0;
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if(heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM 
						&& heroInfo.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT){
					fightValue += heroInfo.getFightValue();
				}
			}
		}
		return fightValue;
	}
	
	/**
	 * 获取羁绊武将No
	 * @param roleId
	 * @return
	 */
	public static List<Integer> getRelationHeroNos(int roleId) {
		List<Integer> list = new ArrayList<Integer>();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);		
		if (roleInfo == null) {
			return list;
		}
		list.addAll(roleInfo.getJbHeroNoMap().values());
		
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() > HeroInfo.DEPLOY_TYPE_MAIN
						&& !list.contains(heroInfo.getHeroNo())) {
					list.add(heroInfo.getHeroNo());
				}
			}
		}
		return list;
	}

	/**
	 * 获取指定位置的上阵武将
	 * @param roleId
	 * @return
	 */
	public static HeroInfo getFightDeployHero(int roleId, byte deployStatus) {
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() == deployStatus) {
					return heroInfo;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据品质获取副将数量
	 * 
	 * @param roleId
	 * @param quality
	 * @return
	 */
	public static int getHeroNumByQuality(int roleId, int quality) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			return 0;
		}
		
		if (roleInfo.getHeroMap() == null) {
			return 0;
		}

		int quaHeroNum = 0;
		for (HeroInfo heroInfo : roleInfo.getHeroMap().values()) {
			if (heroInfo.getQuality() >= quality && heroInfo.getDeployStatus() != 1) {
				quaHeroNum++;
			}
		}

		return quaHeroNum;
	}

	/**
	 * 根据等级获取副将数量
	 * 
	 * @param roleId
	 * @param lv
	 * @return
	 */
	public static int getHeroNumByLv(int roleId, int lv) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			return 0;
		}
		
		if (roleInfo.getHeroMap() == null) {
			return 0;
		}

		int heroLvNum = 0;
		for (HeroInfo heroInfo : roleInfo.getHeroMap().values()) {
			if (heroInfo.getHeroLevel() >= lv && heroInfo.getDeployStatus() != 1) {
				heroLvNum++;
			}
		}

		return heroLvNum;
	}

	/**
	 * 获取主武将等级
	 * @param roleId
	 */
	public static int getMainHeroLv(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			return 0;
		}
		
		if (roleInfo.getHeroMap() == null) {
			return 0;
		}

		for (HeroInfo heroInfo : roleInfo.getHeroMap().values()) {
			if (heroInfo.getDeployStatus() == 1) {
				return heroInfo.getHeroLevel();
			}
		}
		return 0;
	}

}
