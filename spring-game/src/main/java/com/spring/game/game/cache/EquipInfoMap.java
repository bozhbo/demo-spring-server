package com.snail.webgame.game.cache;

import java.util.List;
import java.util.Map;

import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.info.EquipXMLInfo;

public class EquipInfoMap {

	public static void addRequireEquipList(List<Map<String, Object>> equipList) {
		if (equipList != null) {
			EquipInfo info = null;
			for (Map<String, Object> map : equipList) {
				int roleId = Integer.valueOf(map.get("N_ROLE_ID") + "");
				int heroId = Integer.valueOf(map.get("N_HERO_ID") + "");
				info = new EquipInfo();
				info.setId(Integer.valueOf(map.get("N_ID") + ""));
				info.setEquipNo(Integer.valueOf(map.get("N_EQUIP_NO") + ""));
				if (map.get("N_EQUIP_TYPE") != null) {
					info.setEquipType(Integer.valueOf(map.get("N_EQUIP_TYPE") + ""));
				}
				info.setLevel(Short.valueOf(map.get("N_EQUIP_LEVEL") + ""));
				// equipExp
				info.setRefineLv(Short.valueOf(map.get("N_EQUIP_REFINE_LEVEL") + ""));
				info.setExp(Integer.valueOf(map.get("N_EQUIP_EXP") + ""));
				
				info.setEnchantLv(Short.valueOf(map.get("N_ENCHANT_LEVEL") + ""));
				info.setEnchantExp(Integer.valueOf(map.get("N_ENCHANT_EXP") + ""));
				addRequireEquipInfo(roleId, heroId, info);
			}
		}
	}

	public static void addRequireEquipInfo(int roleId, int heroId, EquipInfo equipInfo) {
		if (heroId == 0) { // 为装备在英雄身上则添加在玩家装备背包中
			addBagEquipInfo(roleId, equipInfo);
		} else { // 装备在英雄身上
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo != null) {
				addHeroEquipInfo(heroInfo, equipInfo);
			}
		}
	}

	public static void addBagEquipInfo(int roleId, EquipInfo equipInfo) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		Map<Integer, EquipInfo> equipMap = roleLoadInfo.getBagEquipMap();
		equipMap.put(equipInfo.getId(), equipInfo);
	}

	public static void addHeroEquipInfo(HeroInfo heroInfo, EquipInfo equipInfo) {
		heroInfo.getEquipMap().put(equipInfo.getId(), equipInfo);
	}

	/**
	 * 获得玩家装备背包
	 * @param roleId
	 * @return
	 */
	public static Map<Integer, EquipInfo> getBagEquipMap(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return null;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		return roleLoadInfo.getBagEquipMap();
	}

	/**
	 * 获得背包中的一件装备
	 * @param roleId
	 * @param equipId
	 * @return
	 */
	public static EquipInfo getBagEquip(int roleId, int equipId) {
		Map<Integer, EquipInfo> equipMap = getBagEquipMap(roleId);
		if (equipMap != null) {
			return equipMap.get(equipId);
		}
		return null;
	}
	
	public static boolean checkBagEquip(int roleId, int equipNo) {
		Map<Integer, EquipInfo> equipMap = getBagEquipMap(roleId);
		if (equipMap != null) {
			for(EquipInfo equip : equipMap.values()){
				if(equip.getEquipNo() == equipNo){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取背包中未装备且指定装备类型战斗力最高的装备
	 * @param roleId
	 * @param equipType
	 * @return
	 */
	public static EquipInfo getBestBagTypeEquip(int roleId, byte equipType) {
		Map<Integer, EquipInfo> equipMap = getBagEquipMap(roleId);
		int highFightValue = 0;
		EquipInfo equipInfo = null;
		if (equipMap == null || equipMap.size() == 0) {
			return null;
		}
		for (EquipInfo equipInfo1 : equipMap.values()) {
			EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo1.getEquipNo());
			if (equipXMLInfo == null) {
				break;
			}
			if (equipXMLInfo.getEquipType() == equipType && equipInfo1.getFightValue() > highFightValue) {
				equipInfo = equipInfo1;
				highFightValue = equipInfo1.getFightValue();
			}
		}
		return equipInfo;
	}

	/**
	 * 获得英雄身上的所有装备
	 * @param heroId
	 * @return
	 */
	public static Map<Integer, EquipInfo> getHeroEquipMap(HeroInfo heroInfo) {
		if (heroInfo != null) {
			return heroInfo.getEquipMap();
		}

		return null;
	}

	/**
	 * 获得英雄身上的一件装备
	 * @param heroId
	 * @param equipId
	 * @return
	 */
	public static EquipInfo getHeroEquip(HeroInfo heroInfo, int equipId) {
		if (heroInfo != null) {
			return heroInfo.getEquipMap().get(equipId);
		}

		return null;
	}

	/**
	 * 获取英雄身上的装备
	 * @param heroId
	 * @param equipType
	 * @return
	 */
	public static EquipInfo getHeroEquipbyType(HeroInfo heroInfo, int equipType) {
		if (heroInfo != null) {
			Map<Integer, EquipInfo> equipMap = heroInfo.getEquipMap();
			if (equipMap != null) {
				for (EquipInfo info : equipMap.values()) {
					if (info.getEquipType() == equipType) {
						return info;
					}
				}
			}
		}

		return null;
	}

	/**
	 * 删除背包中的装备
	 * @param roleId
	 * @param equipId
	 */
	public static void removeBagEquip(int roleId, int equipId) {
		Map<Integer, EquipInfo> equips = getBagEquipMap(roleId);
		if (equips != null) {
			equips.remove(equipId);
		}
	}

	/**
	 * 删除武将上的装备
	 * @param roleId
	 * @param equipId
	 */
	public static void removeHeroEquip(HeroInfo heroInfo, int equipId) {
		if (heroInfo != null) {
			heroInfo.getEquipMap().remove(equipId);
		}
	}

	/**
	 * 获得玩家神兵包
	 * @param roleId
	 * @return
	 */
	public static Map<Integer, RoleWeaponInfo> getBagWeaponMap(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return null;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			roleLoadInfo = new RoleLoadInfo();
		}
		return roleLoadInfo.getRoleWeaponInfoMap();
	}

	/**
	 * 获得背包中的一件神兵
	 * @param roleId
	 * @param equipId
	 * @return
	 */
	public static RoleWeaponInfo getBagWeapon(int roleId, int weapId) {
		Map<Integer, RoleWeaponInfo> weaponMap = getBagWeaponMap(roleId);
		if (weaponMap != null) {
			return weaponMap.get(weapId);
		}
		return null;
	}
}
