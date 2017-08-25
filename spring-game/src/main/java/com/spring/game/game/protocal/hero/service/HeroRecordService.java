package com.snail.webgame.game.protocal.hero.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.EquipRecord;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.RideRecord;
import com.snail.webgame.game.common.WeaponRecord;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroColourXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroProXMLAdd;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroImageInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.club.service.ClubService;
import com.snail.webgame.game.protocal.equip.equipStone.EquipStoneRe;
import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.equip.query.EquipInfoRe;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.hero.query.HeroDetailRe;
import com.snail.webgame.game.protocal.hero.query.HeroInfoRe;
import com.snail.webgame.game.protocal.hero.query.HeroSkillRe;
import com.snail.webgame.game.protocal.soldier.service.SoldierService;
import com.snail.webgame.game.protocal.weapon.WeaponService;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.info.EquipXMLInfo;

public class HeroRecordService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 武将转换为镜像
	 * @param roleInfo
	 * @param heroInfo
	 * @param deployPos
	 * @return
	 */
	public static HeroRecord getDeployHeroRecord(HeroInfo heroInfo, byte deployPos) {
		if (deployPos <= 0 || deployPos > GameValue.FIGHT_ARMY_LIMIT) {
			logger.info("heroInfo deployStatus error = :" + deployPos);
			return null;
		}
		return getHeroRecord(heroInfo, deployPos);
	}

	/**
	 * 武将转换为镜像
	 * @param roleInfo
	 * @param heroInfo
	 * @param deployPos
	 * @return
	 */
	public static HeroRecord getHeroRecord(HeroInfo heroInfo, byte deployPos) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(heroInfo.getRoleId());
		if (roleInfo == null) {
			logger.info("heroInfo roleId:" + heroInfo.getRoleId() + " not exit");
			return null;
		}

		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
		if (heroXMLInfo == null) {
			logger.info("heroInfo heroNo:" + heroInfo.getHeroNo() + " not exit");
			return null;
		}

		HeroRecord record = new HeroRecord();
		record.setId(heroInfo.getId());
		record.setHeroNo(heroInfo.getHeroNo());
		record.setDeployStatus(deployPos);
		record.setSoldierLevel(SoldierService.getSoldierLevel(roleInfo, (byte) heroXMLInfo.getHeroType()));
		record.setHeroLevel(heroInfo.getHeroLevel());
		record.setIntimacyLevel(heroInfo.getIntimacyLevel());
		record.setQuality(heroInfo.getQuality());
		record.setStar(heroInfo.getStar());

		record.setEquipMap(getHeroEquipRecord(heroInfo));
		record.setSkillMap(getHeroSkillRecord(heroInfo));

		if (deployPos == HeroInfo.DEPLOY_TYPE_MAIN) {
			record.setWeaponList(getWeaponList(roleInfo));
			record.getJbHeroNoList().addAll(roleInfo.getJbHeroNoMap().values());
			record.setSoldierMap(SoldierService.getSoldierMap(roleInfo));
			record.setShizhuangMap(getShizhuangEquipRecord(roleInfo));
			record.setRideRecord(getRideRecord(roleInfo));
			record.setChenhaoNo(TitleService.getNowTitle(roleInfo));
			record.setClubTechMap(ClubService.clubTechPlusStr2Map(roleInfo));
		}

		return record;
	}

	public static HeroRecord getDeployHeroRecord(HeroImageInfo imageInfo, byte deployPos) {
		if (deployPos <= 0 || deployPos > GameValue.FIGHT_ARMY_LIMIT) {
			logger.info("HeroImageInfo deployStatus error = :" + deployPos);
			return null;
		}		
		return getHeroRecord(imageInfo, deployPos);
	}
	
	public static HeroRecord getHeroRecord(HeroImageInfo imageInfo, byte deployPos) {
		HeroRecord record = new HeroRecord();
		record.setId(imageInfo.getHeroId());
		record.setHeroNo(imageInfo.getHeroNo());
		record.setDeployStatus(deployPos);
		record.setSoldierLevel(imageInfo.getSoldierLevel());
		record.setHeroLevel(imageInfo.getLevel());
		record.setIntimacyLevel(imageInfo.getIntimacyLevel());
		record.setQuality(imageInfo.getQuality());
		record.setStar(imageInfo.getStar());

		record.setEquipMap(getHeroEquipRecord(imageInfo));
		record.setSkillMap(imageInfo.getSkillMap());

		record.setCutHp(imageInfo.getCutHp());
		record.setHeroStatus(imageInfo.getHeroStatus());

		return record;
	}

	/**
	 * 装备转换为镜像
	 * @param heroInfo
	 * @return
	 */
	public static Map<Integer, EquipRecord> getHeroEquipRecord(HeroImageInfo imageInfo) {
		Map<Integer, EquipRecord> result = new HashMap<Integer, EquipRecord>();
		Map<Integer, Integer> equips = imageInfo.getEquipMap();
		if (equips != null && equips.size() > 0) {
			EquipRecord equip = null;
			for (int equipType : equips.keySet()) {
				int equipNo = equips.get(equipType);
				equip = new EquipRecord();
				equip.setEquipNo(equipNo);
				equip.setEquipType(equipType);
				result.put(equip.getEquipType(), equip);
			}
		}
		return result;
	}

	/**
	 * 镜像转化
	 * @param heroRecord
	 * @return
	 */
	public static HeroDetailRe getHeroDetailRe(RoleInfo roleInfo, Map<Byte, HeroRecord> map, HeroRecord heroRecord) {
		HeroDetailRe re = new HeroDetailRe();
		re.setHeroId(heroRecord.getId());
		re.setHeroInfo(getHeroInfoRe(roleInfo, map, heroRecord));

		List<HeroSkillRe> skillList = new ArrayList<HeroSkillRe>();
		Map<Integer, Integer> skillMap = heroRecord.getSkillMap();
		if (skillMap != null) {
			for (int skillNo : skillMap.keySet()) {
				skillList.add(HeroService.getHeroSkillRe(skillNo, skillMap.get(skillNo)));
			}
		}
		re.setSkillCount(skillList.size());
		re.setSkillList(skillList);

		List<EquipDetailRe> equipList = getEquipList(heroRecord);
		re.setEquipList(equipList);
		re.setEquipCount(equipList.size());

		return re;
	}

	private static HeroInfoRe getHeroInfoRe(RoleInfo roleInfo, Map<Byte, HeroRecord> map, HeroRecord heroRecord) {
		HeroInfoRe re = new HeroInfoRe();
		re.setHeroId((int) heroRecord.getId());
		re.setHeroNo(heroRecord.getHeroNo());
		re.setDeployStatus(heroRecord.getDeployStatus());

		re.setHeroLevel((short) heroRecord.getHeroLevel());
		re.setHeroExp(0);

		re.setIntimacyLevel((short) heroRecord.getIntimacyLevel());
		re.setIntimacyValue(0);

		re.setQuality((byte) heroRecord.getQuality());
		if (heroRecord.getFightValue() == 0) {
			int fightValue = getHeroRecordFightValue(map, heroRecord, 1.0);
			heroRecord.setFightValue(fightValue);
		}
		re.setFightValue(heroRecord.getFightValue());
		return re;
	}

	private static List<EquipDetailRe> getEquipList(HeroRecord heroRecord) {
		List<EquipDetailRe> equipList = new ArrayList<EquipDetailRe>();
		EquipDetailRe re = null;
		for (EquipRecord equipRecord : heroRecord.getEquipMap().values()) {
			re = new EquipDetailRe();
			re.setEquipId((int) equipRecord.getId());
			re.setEquipNum((short) 1);
			re.setEquipInfo(getEquipInfoRe(heroRecord.getId(), equipRecord));
			equipList.add(re);
		}

		return equipList;
	}

	private static EquipInfoRe getEquipInfoRe(int heroId, EquipRecord info) {
		EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(info.getEquipNo());
		if (equipXMLInfo == null) {
			logger.info("EquipRecord EquipNo:" + info.getEquipNo() + " not exist");
			return null;
		}

		EquipInfoRe re = new EquipInfoRe();
		re.setEquipId((int) info.getId());
		re.setHeroId(heroId);
		re.setEquipNo(info.getEquipNo());
		re.setEquipType((byte) info.getEquipType());
		re.setLevel((byte) info.getEquipLevel());
		re.setExp(0);
		re.setRefineLv(info.getRefineLv());
		re.setEnchantLv(info.getEnchantLv());
		re.setEnchantExp(0);

		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		list.add(EquipService.getEquipBaseProperty(equipXMLInfo, info.getEquipLevel(), info.getRefineLv(),
				info.getEnchantLv(), null));
		list.add(EquipService.getEquipGemProperty(info.getStoneMap()));
		HeroPropertyInfo total = HeroProService.generateHeroTotalProtperty(list);
		re.setFightValue(HeroService.recalEquipRecordFightValue(info, total));

		// 宝石信息<seat,stoneNo>
		Map<Integer, Integer> stoneMap = info.getStoneMap();
		if (stoneMap != null && stoneMap.size() > 0) {
			List<EquipStoneRe> stoneList = new ArrayList<EquipStoneRe>();
			for (int seat : stoneMap.keySet()) {
				EquipStoneRe stoneRe = new EquipStoneRe();
				stoneRe.setSeat((byte) seat);
				stoneRe.setStoneNo(stoneMap.get(seat));
				stoneList.add(stoneRe);
			}

			re.setStoneNum((byte) stoneList.size());
			re.setStoneList(stoneList);
		}
		return re;
	}

	/**
	 * 装备转换为镜像
	 * @param heroInfo
	 * @return
	 */
	public static Map<Integer, EquipRecord> getHeroEquipRecord(HeroInfo heroInfo) {
		Map<Integer, EquipRecord> result = new HashMap<Integer, EquipRecord>();
		Map<Integer, EquipInfo> equipMap = EquipInfoMap.getHeroEquipMap(heroInfo);
		if (equipMap != null) {
			for (EquipInfo equipInfo : equipMap.values()) {
				result.put(equipInfo.getEquipType(), getEquipRecord(equipInfo));
			}
		}
		return result;
	}

	/**
	 * 获取背包锁定时装 <equipType,EquipRecord>
	 * @param roleInfo
	 * @return
	 */
	public static Map<Integer, EquipRecord> getShizhuangEquipRecord(RoleInfo roleInfo) {
		Map<Integer, EquipRecord> result = new HashMap<Integer, EquipRecord>();
		Map<Integer, EquipInfo> lockMap = roleInfo.getLockShizhuangMap();
		if (lockMap != null) {
			for (EquipInfo equipInfo : lockMap.values()) {
				result.put(equipInfo.getEquipType(), getEquipRecord(equipInfo));			
			}
		}
		return result;
	}

	/**
	 * 获取坐骑镜像
	 * @param roleInfo
	 * @return
	 */
	public static RideRecord getRideRecord(RoleInfo roleInfo) {
		RideInfo rideInfo = roleInfo.getRideInfo();
		if (rideInfo != null) {
			RideRecord record = new RideRecord();
			record.setRideNo(rideInfo.getRideNo());
			record.setRideLv(rideInfo.getRideLv());
			record.setQuality(rideInfo.getQuality());
			return record;
		}
		return null;
	}

	/**
	 * 装备转换为镜像
	 * @param equipInfo
	 * @return
	 */
	public static EquipRecord getEquipRecord(EquipInfo equipInfo) {
		EquipRecord equip = new EquipRecord();
		equip.setId(equipInfo.getId());
		equip.setEquipNo(equipInfo.getEquipNo());
		equip.setEquipType(equipInfo.getEquipType());
		equip.setEquipLevel(equipInfo.getLevel());
		equip.setRefineLv(equipInfo.getRefineLv());
		equip.setStoneMap(equipInfo.getStoneMap());
		equip.setEnchantLv(equipInfo.getEnchantLv());
		return equip;
	}

	/**
	 * 技能转换为镜像
	 * @param heroInfo
	 * @return
	 */
	private static Map<Integer, Integer> getHeroSkillRecord(HeroInfo heroInfo) {
		return HeroService.getSkillMap(heroInfo);
	}

	/**
	 * 获取神兵镜像
	 * @return
	 */
	private static List<WeaponRecord> getWeaponList(RoleInfo roleInfo) {
		List<WeaponRecord> weaponList = new ArrayList<WeaponRecord>();
		Map<Integer, RoleWeaponInfo> weaponMap = roleInfo.getRoleWeaponInfoPositionMap();
		if (weaponMap != null) {
			WeaponRecord record = null;
			for (RoleWeaponInfo info : weaponMap.values()) {
				record = new WeaponRecord();
				record.setWeaponNo(info.getWeaponNo());
				record.setLevel(info.getLevel());
				record.setPosition(info.getPosition());
				weaponList.add(record);
			}
		}

		return weaponList;
	}

	/**
	 * 计算HeroRecord TotalPro
	 * @param roleInfo
	 * @param map
	 * @param record
	 * @param rate
	 * @return
	 */
	public static HeroPropertyInfo recalHeroRecordTotalPro(Map<Byte, HeroRecord> map, HeroRecord record, double rate) {
		return recalHeroRecordTotalPro(map, record, HeroProService.getProRate(rate), HeroProService.getProRate(rate));
	}

	/**
	 * 计算HeroRecord TotalPro
	 * @param roleInfo
	 * @param map
	 * @param record
	 * @param rate 加成参数
	 * @return
	 */
	public static HeroPropertyInfo recalHeroRecordTotalPro(Map<Byte, HeroRecord> map, HeroRecord record,
			Map<HeroProType, Double> mainRate, Map<HeroProType, Double> otherRate) {
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(record.getHeroNo());
		if (heroXMLInfo == null) {
			logger.info("HeroRecord HeroNo:" + record.getHeroNo() + " not exist");
			return null;
		}
		int mainHeroLv = 0;
		Map<Integer, EquipRecord> shizhuangMap = null;
		HeroRecord mainHeroRecord = map.get(HeroInfo.DEPLOY_TYPE_MAIN);
		if (mainHeroRecord != null) {
			mainHeroLv = mainHeroRecord.getHeroLevel();
			shizhuangMap = mainHeroRecord.getShizhuangMap();
		}

		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		// 武将基本属性
		list.addAll(HeroProService.getHeroBaseProperty(heroXMLInfo, record.getHeroLevel(), record.getQuality(),
				record.getIntimacyLevel(), record.getStar()));
		// 武将装备属性
		Map<HeroProType, Double> equipRate = new HashMap<HeroProType, Double>();
		list.addAll(recalHeroRecordEquipPro(record, heroXMLInfo, equipRate, shizhuangMap));

		byte heroType = (byte) heroXMLInfo.getHeroType();
		int soldierLevel = record.getSoldierLevel();
		// 兵种基本属性(到了开启兵法等级才生效)
		if (mainHeroLv >= GameValue.SOLDIER_UPGRADE_OPEN_LEVEL) {
			list.add(HeroProService.getSoldierBaseProtperty(heroType, soldierLevel));
		}

		if (record.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
			// 主武将兵法激活(到了开启兵法等级才生效)
			if (mainHeroLv >= GameValue.SOLDIER_UPGRADE_OPEN_LEVEL) {
				list.add(HeroProService.getSoldierActProtperty(record.getSoldierMap()));
			}
			// 主武将神兵属性
			Object[] weaponproarr = WeaponService.getWeaponHeroPropertyInfo(record);
			if (weaponproarr != null) {
				list.add((HeroPropertyInfo) weaponproarr[0]);
			}
			
			list.add(HeroProService.getRoleTitlePlusProperty(record.getChenhaoNo()));
			list.add(HeroProService.getClubTechPlusProperty(record.getClubTechMap()));
			list.add(HeroProService.getRidePlusProperty(record.getRideRecord()));
			
			HeroPropertyInfo total = HeroProService.generateHeroTotalProtperty(list);
			if (equipRate != null) {
				for (HeroProType proType : equipRate.keySet()) {
					total.addRate(proType, (Double) equipRate.get(proType) / 100.0);
				}
			}
			return HeroProService.generateHeroTotalProtperty(total, mainRate);
		}
		HeroPropertyInfo total = HeroProService.generateHeroTotalProtperty(list);
		if (record.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM
				&& record.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN
				&& record.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT) {
			calHeroRelationShipPropAdd(map, record, total);
		}
		return HeroProService.generateHeroTotalProtperty(total, otherRate);
	}

	/**
	 * 计算镜像战斗力.血量
	 * @param map
	 * @param record
	 * @param rate
	 * @return
	 */
	public static int getHeroRecordFightValue(Map<Byte, HeroRecord> map, HeroRecord record, double rate) {
		HeroPropertyInfo total = recalHeroRecordTotalPro(map, record, rate);
		if (total != null) {
			record.setTotalHp(total.getHp());
			if (record.getFightValue() == 0) {
				int fightValue = HeroService.recalHeroRecordFightValue(record, total, record.getSkillMap());
				record.setFightValue(fightValue);
			}
			return record.getFightValue();
		}
		return 0;
	}

	/**
	 * 计算武将羁绊属性加成
	 * @param map
	 * @param record
	 * @param total
	 */
	public static void calHeroRelationShipPropAdd(Map<Byte, HeroRecord> map, HeroRecord record, HeroPropertyInfo total) {
		List<Integer> reHeroIdList = new ArrayList<Integer>();
		for (HeroRecord heroRecord : map.values()) {
			if (heroRecord.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
				reHeroIdList.addAll(heroRecord.getJbHeroNoList());
			}

			if (heroRecord.getDeployStatus() > HeroInfo.DEPLOY_TYPE_MAIN) {
				if (!reHeroIdList.contains(heroRecord.getHeroNo())) {
					reHeroIdList.add(heroRecord.getHeroNo());
				}
			}
		}
		if (reHeroIdList == null || reHeroIdList.size() == 0) {
			return;
		}
		HeroProService.calHeroRelationShipPropAdd(record.getHeroNo(), total, reHeroIdList);
	}

	/**
	 * 计算HeroRecord 装备属性
	 * @param record
	 * @return
	 */
	private static List<HeroPropertyInfo> recalHeroRecordEquipPro(HeroRecord record, HeroXMLInfo heroXMLInfo,
			Map<HeroProType, Double> equipRate, Map<Integer, EquipRecord> shizhuangMap) {
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		if (record.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
			// 主武将装备属性
			Map<Integer, EquipRecord> equipMap = record.getEquipMap();
			if (equipMap != null) {
				int equipNo = 0;
				int level = 0;
				int refineLv = 0;
				int enchantLv = 0;
				EquipXMLInfo equipXMLInfo = null;
				EquipRecord shizhuang = null;
				for (EquipRecord equipRecord : equipMap.values()) {
					equipNo = equipRecord.getEquipNo();
					level = equipRecord.getEquipLevel();
					refineLv = equipRecord.getRefineLv();
					enchantLv = equipRecord.getEnchantLv();
					// 时装锁定特殊处理
					if (shizhuangMap != null && (equipRecord.getEquipType() == 9 || equipRecord.getEquipType() == 10)) {
						shizhuang = shizhuangMap.get(equipRecord.getEquipType());
						if (shizhuang != null && shizhuang.getId() != equipRecord.getId()) {
							equipNo = shizhuang.getEquipNo();
							level = shizhuang.getEquipLevel();
							refineLv = shizhuang.getRefineLv();
							enchantLv = shizhuang.getEnchantLv();
						}
					}
					equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipNo);
					if (equipXMLInfo == null) {
						logger.info("EquipRecord EquipNo:" + equipNo + " not exist");
						continue;
					}

					list.add(EquipService.getEquipBaseProperty(equipXMLInfo, level, refineLv, enchantLv, equipRate));
					list.add(EquipService.getEquipGemProperty(equipRecord.getStoneMap()));
				}

				list.add(HeroProService.getEquipSuitPlusProperty(equipMap, equipRate));
			}
		} else {
			// 其他武将激活属性
			HeroProXMLAdd proAdd = null;
			HeroPropertyInfo pro = new HeroPropertyInfo();
			HeroColourXMLUpgrade colourXml = heroXMLInfo.getColourMap().get(record.getQuality());
			if (colourXml != null && colourXml.getCostEquipMap() != null) {
				for (int equipType : colourXml.getCostEquipMap().keySet()) {
					if (record.getEquipMap().containsKey(equipType)) {
						proAdd = colourXml.getCostEquipMap().get(equipType);
						if (proAdd != null) {
							pro.addValue(proAdd.getHeroPro(), proAdd.getAddVal());
						}
					}
				}
			} else {
				logger.info("HeroRecord HeroNo:" + heroXMLInfo.getNo() + " Quality :" + record.getQuality()
						+ " not exist");
			}
			list.add(pro);
		}
		return list;
	}

}
