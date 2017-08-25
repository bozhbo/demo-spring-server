package com.snail.webgame.game.protocal.hero.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.EquipRecord;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.RideRecord;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.SoldierXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.SoldierXMLInfoMap.HideSoldierXmlAdd;
import com.snail.webgame.game.common.xml.info.HeroColourXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroLevelXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroProXMLAdd;
import com.snail.webgame.game.common.xml.info.HeroStarXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.SoldierXMLInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.club.service.ClubService;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.ride.service.RideService;
import com.snail.webgame.game.protocal.soldier.service.SoldierService;
import com.snail.webgame.game.protocal.weapon.WeaponService;
import com.snail.webgame.game.xml.cache.ChenghaoXMLInfoMap;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.GuildTechXMLInfoMap;
import com.snail.webgame.game.xml.cache.HeroRelationShipXMLInfoMap;
import com.snail.webgame.game.xml.cache.RideXMLInfoMap;
import com.snail.webgame.game.xml.info.ChenghaoXMLInfo;
import com.snail.webgame.game.xml.info.EquipSuitConfigInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.GuildTechXMLInfo;
import com.snail.webgame.game.xml.info.HeroRelationShipXMLInfo;
import com.snail.webgame.game.xml.info.HeroRelationShipXMLInfo.RelationShipXMLInfo;
import com.snail.webgame.game.xml.info.RideQuaXMLInfo;
import com.snail.webgame.game.xml.info.RideXMLInfo;
import com.snail.webgame.game.xml.info.TitleValue;

/**
 * 计算武将属性
 * @author zenggang
 */
public class HeroProService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 获取武将属性
	 * @param heroInfo
	 * @param rate
	 * @return
	 */
	public static HeroPropertyInfo getHeroTotalProperty(HeroInfo heroInfo, double rate) {
		return getHeroTotalProperty(heroInfo, getProRate(rate), getProRate(rate));
	}

	/**
	 * 获取武将属性
	 * @param heroInfo
	 * @param rateMap
	 * @return
	 */
	public static HeroPropertyInfo getHeroTotalProperty(HeroInfo heroInfo, Map<HeroProType, Double> mainRate,
			Map<HeroProType, Double> otherRate) {
		RoleInfo roleInfo = null;
		Map<Integer, EquipInfo> lockShizhuang = null;
		int mainHeroLv = 0;
		if (heroInfo.getRoleId() != 0) {
			roleInfo = RoleInfoMap.getRoleInfo(heroInfo.getRoleId());
			if (roleInfo == null) {
				logger.info("heroInfo roleId:" + heroInfo.getRoleId() + " not exit");
				return null;
			}
			mainHeroLv = HeroInfoMap.getMainHeroLv(heroInfo.getRoleId());
			lockShizhuang = roleInfo.getLockShizhuangMap();
		}

		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
		if (heroXMLInfo == null) {
			logger.info("heroInfo heroNo:" + heroInfo.getHeroNo() + " not exit");
			return null;
		}
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		// 武将基本属性
		list.addAll(getHeroBaseProperty(heroXMLInfo, heroInfo.getHeroLevel(), heroInfo.getQuality(),
				heroInfo.getIntimacyLevel(), heroInfo.getStar()));

		// 武将装备属性
		Map<HeroProType, Double> equipRate = new HashMap<HeroProType, Double>();
		list.addAll(getEquipAddProperty(heroInfo, heroXMLInfo, equipRate, lockShizhuang));

		byte heroType = (byte) heroXMLInfo.getHeroType();
		int soldierLevel = 0;
		if (roleInfo != null) {
			soldierLevel = SoldierService.getSoldierLevel(roleInfo, heroType);

			// 兵种基本属性(到了开启兵法等级才生效)
			if (mainHeroLv >= GameValue.SOLDIER_UPGRADE_OPEN_LEVEL) {
				list.add(getSoldierBaseProtperty(heroType, soldierLevel));
			}
		}

		if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
			// 主武将兵法激活(到了开启兵法等级才生效)
			if (mainHeroLv >= GameValue.SOLDIER_UPGRADE_OPEN_LEVEL) {
				list.add(getSoldierActProtperty(SoldierService.getSoldierMap(roleInfo)));
			}

			// 主武将神兵属性
			if (roleInfo != null) {
				Object[] weaponproarr = WeaponService.getWeaponHeroPropertyInfo(roleInfo);
				if (weaponproarr != null) {
					list.add((HeroPropertyInfo) weaponproarr[0]);
				}
			}

			list.add(getClubTechPlusProperty(roleInfo)); // 公会科技属性加成
			list.add(getRoleTitlePlusProperty(roleInfo)); // 角色称号属性加成
			list.add(getRidePlusProperty(roleInfo));

			HeroPropertyInfo total = generateHeroTotalProtperty(list);
			if (equipRate != null) {
				for (HeroProType proType : equipRate.keySet()) {
					total.addRate(proType, (Double) equipRate.get(proType) / 100.0);
				}
			}
			return generateHeroTotalProtperty(total, mainRate);
		}

		if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM
				&& heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN
				&& heroInfo.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT) {
			HeroPropertyInfo total = generateHeroTotalProtperty(list);
			calHeroRelationShipPropAdd(heroInfo, total);
			return generateHeroTotalProtperty(total, otherRate);
		}

		return generateHeroTotalProtperty(list, otherRate);
	}

	/**
	 * 获取武将的裸属性
	 * @param roleInfo
	 * @param heroInfo
	 * @param heroXMLInfo
	 * @return
	 */
	public static HeroPropertyInfo getNudeProperty(RoleInfo roleInfo, HeroInfo heroInfo, HeroXMLInfo heroXMLInfo) {

		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		list.add(getHeroLevelBaseProperty(heroXMLInfo, heroInfo.getHeroLevel(), heroInfo.getStar()));
		if (heroXMLInfo.getInitial() == 0) {
			// 升级突破属性加成
			HashMap<Integer, HeroLevelXMLUpgrade> otherLvMap = HeroXMLInfoMap.getOtherLvMap();
			HeroLevelXMLUpgrade upgrade = null;
			for (int lv = 1; lv <= heroInfo.getHeroLevel(); lv++) {
				upgrade = otherLvMap.get(lv);
				if (upgrade != null) {
					list.add(upgrade);
				}
			}
		}
		return generateHeroTotalProtperty(list);
	}

	/**
	 * 装备附加属性信息
	 * @param heroInfo
	 * @return
	 */
	public static List<HeroPropertyInfo> getEquipAddProperty(HeroInfo heroInfo, HeroXMLInfo heroXMLInfo,
			Map<HeroProType, Double> equipRate, Map<Integer, EquipInfo> lockShizhuang) {
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();

		if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
			// 主武将装备可以增加套装及基础属性

			// 装备套装属性
			list.add(getEquipSuitPlusProperty(heroInfo, equipRate));
			Map<Integer, EquipInfo> equipMap = heroInfo.getEquipMap();

			if (equipMap != null) {
				int equipNo = 0;
				int level = 0;
				int refineLv = 0;
				int enchantLv = 0;
				EquipXMLInfo equipXMLInfo = null;
				EquipInfo shizhuang = null;
				for (EquipInfo equipInfo : equipMap.values()) {
					equipNo = equipInfo.getEquipNo();
					level = equipInfo.getLevel();
					refineLv = equipInfo.getRefineLv();
					enchantLv = equipInfo.getEnchantLv();
					// 时装锁定特殊处理
					if (lockShizhuang != null && (equipInfo.getEquipType() == 9 || equipInfo.getEquipType() == 10)) {
						shizhuang = lockShizhuang.get(equipInfo.getEquipType());
						if (shizhuang != null && shizhuang.getId() != equipInfo.getId()) {
							equipNo = shizhuang.getEquipNo();
							level = shizhuang.getLevel();
							refineLv = shizhuang.getRefineLv();
							enchantLv = shizhuang.getEnchantLv();
						}
					}
					equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipNo);
					if (equipXMLInfo == null) {
						logger.info("EquipInfo EquipNo:" + equipNo + " not exist");
						continue;
					}
					// 装备基础,品质,强化,精炼属性加成
					list.add(EquipService.getEquipBaseProperty(equipXMLInfo, level, refineLv, enchantLv, equipRate));
				}
			}

		} else {
			// 副将装备没有套装及基础属性，只有装备部位激活属性
			HeroPropertyInfo pro = new HeroPropertyInfo();
			HeroColourXMLUpgrade colourXml = heroXMLInfo.getColourMap().get(heroInfo.getQuality());
			HeroProXMLAdd proAdd = null;
			if (colourXml != null) {
				for (int equipType : colourXml.getCostEquipMap().keySet()) {
					if (EquipInfoMap.getHeroEquipbyType(heroInfo, equipType) != null) {
						proAdd = colourXml.getCostEquipMap().get(equipType);
						if (proAdd != null) {
							pro.addValue(proAdd.getHeroPro(), proAdd.getAddVal());
						}
					}
				}
				list.add(pro);
			} else {
				logger.info("HeroInfo HeroNo:" + heroXMLInfo.getNo() + " Quality :" + heroInfo.getQuality()
						+ " not exist");
			}

		}
		return list;
	}

	/**
	 * 获取武将基本属性
	 * @param heroXMLInfo
	 * @param heroLevel
	 * @param quality
	 * @param intimacyLevel
	 * @return
	 */
	public static List<HeroPropertyInfo> getHeroBaseProperty(HeroXMLInfo heroXMLInfo, int heroLevel, int quality,
			int intimacyLevel, int heroStar) {
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		list.add(getHeroLevelBaseProperty(heroXMLInfo, heroLevel, heroStar));
		if (heroXMLInfo.getInitial() == 0) {
			// 升级突破属性加成
			HashMap<Integer, HeroLevelXMLUpgrade> otherLvMap = HeroXMLInfoMap.getOtherLvMap();
			HeroLevelXMLUpgrade upgrade = null;
			for (int lv = 1; lv <= heroLevel; lv++) {
				upgrade = otherLvMap.get(lv);
				if (upgrade != null) {
					list.add(upgrade);
				}
			}

			// 觉醒激活属性加成
			HeroPropertyInfo pro = new HeroPropertyInfo();
			Map<Integer, HeroColourXMLUpgrade> colourMap = heroXMLInfo.getColourMap();
			HeroColourXMLUpgrade colourXml = null;
			for (int colour = 1; colour <= quality; colour++) {
				colourXml = colourMap.get(colour);
				if (colourXml != null) {
					pro.addValue(colourXml.getHeroPro(), colourXml.getAddVal());
					if (quality != colour) {
						for (HeroProXMLAdd proAdd : colourXml.getCostEquipMap().values()) {
							pro.addValue(proAdd.getHeroPro(), proAdd.getAddVal());
						}
					}
				}
			}
			list.add(pro);

			// 亲密度属性加成
			HeroPropertyInfo intimacyPro = HeroXMLInfoMap.getHeroIntimacyPro(heroXMLInfo.getCloseType(), intimacyLevel);
			if (intimacyPro != null) {
				list.add(intimacyPro);
			}
		}

		return list;
	}

	/**
	 * 获取武将等级成长
	 * @param heroXMLInfo
	 * @param heroLevel
	 * @return
	 */
	private static HeroPropertyInfo getHeroLevelBaseProperty(HeroXMLInfo heroXMLInfo, int heroLevel, int heroStar) {
		HeroPropertyInfo pro = (HeroPropertyInfo) heroXMLInfo.clone();
		Map<HeroProType, Double> proRateMap = new HashMap<HeroProType, Double>();
		proRateMap.putAll(heroXMLInfo.getProRateMap());
		if (heroXMLInfo.getInitial() == 0 && heroStar > 0) {
			// 0:非初始武将
			HeroStarXMLUpgrade starXML = null;
			for (int i = 0; i <= heroStar; i++) {
				starXML = heroXMLInfo.getStarMap().get(i);
				if (starXML != null) {
					pro.addValue(HeroProType.I_force, starXML.getForce());
					pro.addValue(HeroProType.I_wit, starXML.getWit());
					pro.addValue(HeroProType.I_troops, starXML.getTroops());

					for (HeroProType proType : starXML.getProRateMap().keySet()) {
						double add = starXML.getProRateMap().get(proType);
						Double oldValue = proRateMap.get(proType);
						if (oldValue == null) {
							proRateMap.put(proType, add);
						} else {
							proRateMap.put(proType, add + oldValue);
						}
					}
				}
			}
		}

		for (HeroProType proType : proRateMap.keySet()) {
			double rate = proRateMap.get(proType);
			float init = ((Number) heroXMLInfo.getValue(proType)).floatValue();
			pro.addValue(proType, calSingleLevelEffect(init, rate, heroLevel));
		}
		int ad = pro.getForce() * 5;// 1武力=5普攻
		pro.setAd(ad);

		int magicAttack = pro.getWit() * 5;// 1智力=5技能强度
		pro.setMagicAttack(magicAttack);

		int hp = pro.getTroops() * 100;// 1统帅=100生命值
		pro.setHp(hp);
		return pro;
	}

	/**
	 * 计算武将等级对单条属性效果
	 * @param baseVal 初始属性值
	 * @param baseRate 初始属性成长率
	 * @param heroLv 武将等级
	 * @return
	 */
	private static double calSingleLevelEffect(float baseVal, double baseRate, int heroLevel) {
		// 初始属性*成长率*（当前等级-1）
		return baseVal * baseRate * (heroLevel - 1);
	}

	/**
	 * 计算list总和
	 * @param list
	 * @param rate
	 * @return
	 */
	public static HeroPropertyInfo generateHeroTotalProtperty(List<HeroPropertyInfo> list) {
		HeroPropertyInfo total = new HeroPropertyInfo();
		for (HeroPropertyInfo pro : list) {
			if (pro == null) {
				continue;
			}

			for (HeroProType proType : HeroProType.values()) {
				total.addValue(proType, pro.getValue(proType));
			}
		}
		return total;
	}

	/**
	 * 计算list总和
	 * @param list
	 * @param rate
	 * @return
	 */
	public static HeroPropertyInfo generateHeroTotalProtperty(List<HeroPropertyInfo> list, Map<HeroProType, Double> rate) {
		HeroPropertyInfo total = HeroProService.generateHeroTotalProtperty(list);
		return generateHeroTotalProtperty(total, rate);
	}

	/**
	 * 计算list总和
	 * @param list
	 * @param rate
	 * @return
	 */
	public static HeroPropertyInfo generateHeroTotalProtperty(HeroPropertyInfo total, Map<HeroProType, Double> rate) {
		if (rate != null) {
			for (HeroProType proType : rate.keySet()) {
				total.mulRate(proType, rate.get(proType));
			}
		}
		return total;
	}

	/**
	 * 获取加成属性的加乘比
	 * @param proType
	 * @param rate
	 * @return
	 */
	public static Map<HeroProType, Double> getProRate(double rate) {
		Map<HeroProType, Double> result = new HashMap<HeroProType, Double>();
		for (HeroProType proType : HeroProType.values()) {
			switch (proType) {
			case I_hp:
			case I_attack:
			case I_magicAttack:
			case I_attackDef:
			case I_magicDef:
			case I_ad:
				result.put(proType, rate);
				break;
			default:
				break;
			}
		}

		return result;
	}

	/**
	 * 计算武将羁绊属性加成
	 * @param heroPropertyInfo
	 */
	public static void calHeroRelationShipPropAdd(HeroInfo heroInfo, HeroPropertyInfo heroPropertyInfo) {
		List<Integer> relationHeroIdList = HeroInfoMap.getRelationHeroNos(heroInfo.getRoleId());// 羁绊武将列表
		if (relationHeroIdList == null || relationHeroIdList.size() == 0) {
			return;
		}
		calHeroRelationShipPropAdd(heroInfo.getHeroNo(), heroPropertyInfo, relationHeroIdList);
	}

	/**
	 * 计算武将羁绊属性加成
	 * @param heroNo 武将编号
	 * @param total 武将属性
	 * @param relationHeroIdList 羁绊武将列表
	 */
	public static void calHeroRelationShipPropAdd(int heroNo, HeroPropertyInfo total, List<Integer> relationHeroIdList) {
		if (relationHeroIdList == null || relationHeroIdList.size() == 0) {
			return;
		}
		HashMap<HeroProType, Double> valMap = new HashMap<HeroProType, Double>();
		HashMap<HeroProType, Double> rateMap = new HashMap<HeroProType, Double>();
		HeroRelationShipXMLInfo heroRelationShipXML = HeroRelationShipXMLInfoMap.getHeroRelationShipXMLInfo(heroNo);
		if (heroRelationShipXML != null) {
			List<RelationShipXMLInfo> relationShipList = heroRelationShipXML.getRelationShipList();
			for (RelationShipXMLInfo shipXML : relationShipList) {
				// 判断羁绊是否激活
				List<Integer> relationHeroNos = shipXML.getHeroNo();
				boolean activate = true;
				for (int relationHeroNo : relationHeroNos) {
					if (!relationHeroIdList.contains(relationHeroNo)) {
						activate = false;
						break;
					}
				}
				if (activate) {// 激活则获得属性百分比加成
					HeroProType heroProType = shipXML.getBuffType();
					if(shipXML.isAddRate()){
						if (rateMap.containsKey(heroProType)) {
							rateMap.put(heroProType,rateMap.get(heroProType) + shipXML.getBuffAdd());
						} else {
							rateMap.put(heroProType, shipXML.getBuffAdd());
						}
					} else {
						if (valMap.containsKey(heroProType)) {
							valMap.put(heroProType,valMap.get(heroProType) + shipXML.getBuffAdd());
						} else {
							valMap.put(heroProType, shipXML.getBuffAdd());
						}
					}
				}
			}
		}
		for (HeroProType proType : valMap.keySet()) {
			total.addValue(proType, valMap.get(proType));
		}
		for (HeroProType proType : rateMap.keySet()) {
			total.addRate(proType, rateMap.get(proType));
		}
	}

	/**
	 * 套装加成
	 * @param heroInfo
	 * @return
	 */
	private static HeroPropertyInfo getEquipSuitPlusProperty(HeroInfo heroInfo, Map<HeroProType, Double> equipRate) {
		Map<Integer, EquipRecord> equipMap = HeroRecordService.getHeroEquipRecord(heroInfo);
		return getEquipSuitPlusProperty(equipMap, equipRate);
	}

	/**
	 * 套装加成
	 * @param equipMap
	 * @return
	 */
	public static HeroPropertyInfo getEquipSuitPlusProperty(Map<Integer, EquipRecord> equipMap,
			Map<HeroProType, Double> equipRate) {
		HeroPropertyInfo pro = new HeroPropertyInfo();
		// 套装<套装ID,套装数量>
		Map<Integer, Integer> counterMap = new HashMap<Integer, Integer>();
		EquipXMLInfo equipXMLInfo = null;
		for (EquipRecord equipRecord : equipMap.values()) {
			equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipRecord.getEquipNo());
			if (equipXMLInfo == null) {
				continue;
			}
			if (counterMap.containsKey((equipXMLInfo.getSuitId()))) { // 计算套装
				counterMap.put(equipXMLInfo.getSuitId(), counterMap.get(equipXMLInfo.getSuitId()) + 1);
			} else {
				counterMap.put(equipXMLInfo.getSuitId(), 1);
			}
		}
		if (counterMap.size() > 0) {
			for (int suitId : counterMap.keySet()) {
				int suitNum = counterMap.get(suitId);
				List<EquipSuitConfigInfo> suitList = EquipXMLInfoMap.getEquipSuitConfigInfo(suitId);
				if (suitList == null) {
					continue;
				}
				for (EquipSuitConfigInfo suitInfo : suitList) {
					if (suitNum >= suitInfo.getNum()) {
						if (suitInfo.getType() == null) {
							continue;
						}
						if (suitInfo.isAddRate()) {
							if (equipRate != null) {
								Double val = equipRate.get(suitInfo.getType());
								if (val == null) {
									equipRate.put(suitInfo.getType(), Double.valueOf(suitInfo.getEffect()));
								} else {
									equipRate.put(suitInfo.getType(), val + Double.valueOf(suitInfo.getEffect()));
								}
							}
						} else {
							pro.addValue(suitInfo.getType(), suitInfo.getEffect());
						}
					}
				}
			}
		}
		return pro;
	}

	/**
	 * 获取兵种基本属性
	 * @param soldierType
	 * @param soldierLevel
	 * @return
	 */
	public static HeroPropertyInfo getSoldierBaseProtperty(byte soldierType, int soldierLevel) {
		SoldierXMLInfo soldierXMLInfo = SoldierXMLInfoMap.getSoldierXMLInfo(soldierType, soldierLevel);
		if (soldierXMLInfo != null) {
			HeroPropertyInfo pro = new HeroPropertyInfo();
			pro.setHp(soldierXMLInfo.getHp());
			pro.setAttack(soldierXMLInfo.getAttack());
			pro.setMagicAttack(soldierXMLInfo.getMagicAttack());
			pro.setAttackDef(soldierXMLInfo.getAttackDef());
			pro.setMagicDef(soldierXMLInfo.getMagicDef());
			pro.setAd(soldierXMLInfo.getAd());
			return pro;
		}
		return null;
	}

	/**
	 * 获取兵法激活属性
	 * @param soldierType
	 * @param soldierLevel
	 * @return
	 */
	public static HeroPropertyInfo getSoldierActProtperty(Map<Byte, Integer> soldierMap) {
		if (soldierMap != null) {
			HeroPropertyInfo pro = new HeroPropertyInfo();
			for (byte soldierType : soldierMap.keySet()) {
				int soldierLevel = soldierMap.get(soldierType);
				if (soldierLevel > 0) {
					Map<Integer, HideSoldierXmlAdd> hideAddMap = SoldierXMLInfoMap.getHideSoldierXmlMap(soldierType);
					if (hideAddMap != null) {
						for (HideSoldierXmlAdd xmlInfo : hideAddMap.values()) {
							if (soldierLevel >= xmlInfo.getLv()) {
								pro.addValue(xmlInfo.getHideType(), xmlInfo.getEffect());
							}
						}
					}
				}
			}
			return pro;
		}
		return null;
	}

	/**
	 * 公会科技 属性加成
	 * @param roleInfo
	 * @return
	 */
	private static HeroPropertyInfo getClubTechPlusProperty(RoleInfo roleInfo) {
		return getClubTechPlusProperty(ClubService.clubTechPlusStr2Map(roleInfo));
	}

	/**
	 * 公会科技 属性加成
	 * @param roleInfo
	 * @return
	 */
	public static HeroPropertyInfo getClubTechPlusProperty(Map<Integer, Integer> clubTechMap) {
		if (clubTechMap == null || clubTechMap.size() <= 0) {
			return null;
		}
		HeroPropertyInfo pro = new HeroPropertyInfo();
		Map<Integer, GuildTechXMLInfo> xmlMap = null;
		GuildTechXMLInfo xmlInfo = null;
		for (Integer buildType : clubTechMap.keySet()) {
			xmlMap = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(buildType);
			if (xmlMap == null || xmlMap.size() <= 0) {
				continue;
			}
			xmlInfo = xmlMap.get(clubTechMap.get(buildType));
			if (xmlInfo == null) {
				continue;
			}
			pro.addValue(HeroProType.getHeroProType(xmlInfo.getAddType()), xmlInfo.getAddNum());
		}
		return pro;
	}

	/**
	 * 坐骑 属性加成
	 * @param roleInfo
	 * @return
	 */
	public static HeroPropertyInfo getRidePlusProperty(RoleInfo roleInfo) {
		if (roleInfo == null) {
			return null;
		}
		RideInfo rideInfo = roleInfo.getRideInfo();
		if (rideInfo == null) {
			return null;
		}
		// 计算总属性
		return getRidePlusProperty(rideInfo.getRideNo(), rideInfo.getRideLv(), rideInfo.getQuality());
	}

	/**
	 * 坐骑 属性加成
	 * @param roleInfo
	 * @return
	 */
	public static HeroPropertyInfo getRidePlusProperty(RideRecord rideRecord) {
		if (rideRecord == null) {
			return null;
		}
		// 计算总属性
		return getRidePlusProperty(rideRecord.getRideNo(), rideRecord.getRideLv(), rideRecord.getQuality());
	}

	/**
	 * 坐骑 属性加成
	 * @param roleInfo
	 * @return
	 */
	private static HeroPropertyInfo getRidePlusProperty(int rideNo, int rideLv, int quality) {
		RideXMLInfo rideXMLInfo = RideXMLInfoMap.fetchRideXMLInfo(rideNo);
		RideQuaXMLInfo rideQuaXMLInfo = RideXMLInfoMap.fetchRideQuaXMLInfo(rideNo);
		if (rideXMLInfo == null || rideQuaXMLInfo == null) {
			return null;
		}
		// 计算总属性
		return RideService.getRideBaseProperty(rideXMLInfo, rideQuaXMLInfo, rideLv, quality);
	}

	/**
	 * 角色称号属性加成
	 * @param roleInfo
	 * @return
	 */
	private static HeroPropertyInfo getRoleTitlePlusProperty(RoleInfo roleInfo) {
		if (roleInfo == null) {
			return null;
		}
		int chenhaoNo = TitleService.getNowTitle(roleInfo);
		return getRoleTitlePlusProperty(chenhaoNo);
	}
	
	/**
	 * 角色称号属性加成
	 * @param roleInfo
	 * @return
	 */
	public static HeroPropertyInfo getRoleTitlePlusProperty(int chenhaoNo) {
		HeroPropertyInfo pro = new HeroPropertyInfo();
		ChenghaoXMLInfo xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByNo(chenhaoNo);
		if (xmlInfo != null) {
			List<TitleValue> list = xmlInfo.getTitleValueList();
			if (list != null && list.size() > 0) {
				for (TitleValue tv : list) {
					pro.addValue(HeroProType.getHeroProType(tv.getAddType()), tv.getAddNum());
				}
			}
		}
		return pro;
	}
}
