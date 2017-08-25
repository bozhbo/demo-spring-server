package com.snail.webgame.game.protocal.hero.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.EquipRecord;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.WeaponRecord;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroLevelXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.GameSettingDAO;
import com.snail.webgame.game.dao.HeroDAO;
import com.snail.webgame.game.dao.ItemDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.SkillDAO;
import com.snail.webgame.game.dao.typehandler.IntegerMapTypeHandler;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.GameSettingInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.hero.query.HeroDetailRe;
import com.snail.webgame.game.protocal.hero.query.HeroInfoRe;
import com.snail.webgame.game.protocal.hero.query.HeroSkillRe;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.soldier.service.SoldierService;
import com.snail.webgame.game.protocal.weapon.WeaponService;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.WeaponXmlInfoMap;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.WeaponXmlInfo;

/**
 * 英雄服务类
 * @author wangxf
 * @date 2013-3-20
 */
public class HeroService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 初始化英雄属性
	 * @param roleInfo
	 */
	public static void loginInitHeroProperty(RoleInfo roleInfo) {
		int roleId = roleInfo.getId();
		Map<Integer, HeroInfo> heroInfoMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroInfoMap == null) {
			return;
		}

		for (HeroInfo heroInfo : heroInfoMap.values()) {
			// xml
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXMLInfo == null) {
				continue;
			}
			// 初始化英雄装备属性
			EquipService.loginInitHeroEquipProperty(heroInfo);
			// 刷新英雄属性 战斗力
			int fightValue = recalHeroFightValue(heroInfo, HeroProService.getHeroTotalProperty(heroInfo, 1.0),
					HeroService.getSkillMap(heroInfo));
			if (fightValue != heroInfo.getFightValue()) {
				heroInfo.setFightValue(fightValue);
			}
		}
		int currFightValue = HeroInfoMap.getFightDeployHeroFightValue(roleInfo.getId());
		if (roleInfo.getFightValue() != currFightValue) {
			roleInfo.setFightValue(currFightValue);
		}
	}

	/**
	 * 刷新角色羁绊列表
	 * @param roleInfo
	 */
	public static void recalRoleJbHeroNos(RoleInfo roleInfo) {
		Map<Byte, Integer> jbHeroNoMap = new HashMap<Byte, Integer>();
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() > GameValue.FIGHT_ARMY_LIMIT) {
					jbHeroNoMap.put(heroInfo.getDeployStatus(), heroInfo.getHeroNo());
				}
			}
			// 刷新角色战斗力
			int currFightValue = HeroInfoMap.getFightDeployHeroFightValue(roleInfo.getId());
			if (roleInfo.getFightValue() != currFightValue) {
				roleInfo.setFightValue(currFightValue);
			}
		}
		roleInfo.getJbHeroNoMap().clear();
		roleInfo.getJbHeroNoMap().putAll(jbHeroNoMap);
	}

	/**
	 * 初始化新英雄
	 * @param roleId
	 * @param heroXMLInfo
	 * @param deployStatus
	 * @return
	 */
	public static HeroInfo initNewHeroInfo(RoleInfo roleInfo, HeroXMLInfo heroXMLInfo, byte deployStatus) {
		HeroInfo heroInfo = new HeroInfo();
		heroInfo.setRoleId(roleInfo.getId());
		heroInfo.setHeroNo(heroXMLInfo.getNo());
		heroInfo.setDeployStatus(deployStatus);

		heroInfo.setHeroLevel(1);
		heroInfo.setHeroExp(0);
		heroInfo.setQuality(heroXMLInfo.getQuality());
		if (heroInfo.getQuality() <= 0) {
			heroInfo.setQuality(1);
		}
		heroInfo.setStar(heroXMLInfo.getStar());
		if (heroInfo.getStar() <= 0) {
			heroInfo.setStar(1);
		}
		heroInfo.setIntimacyLevel(0);
		heroInfo.setIntimacyValue(0);

		// 刷新英雄属性 战斗力
		initHeroSkill(heroInfo, heroXMLInfo);

		// 战斗力赋值
		int fightValue = recalHeroFightValue(heroInfo, HeroProService.getHeroTotalProperty(heroInfo, 1.0),
				HeroService.getSkillMap(heroInfo));
		if (fightValue != heroInfo.getFightValue()) {
			heroInfo.setFightValue(fightValue);
		}
		return heroInfo;
	}

	/**
	 * 英雄技能开启初始化
	 * @param heroInfo
	 * @param heroXMLInfo
	 * @return 1-有变动 0-无变动 other-无变动有问题
	 */
	private static int initHeroSkill(HeroInfo heroInfo, HeroXMLInfo heroXMLInfo) {
		boolean ischange = false;
		Map<Integer, HeroXMLSkill> skillMap = heroXMLInfo.getSkillMap();
		Map<Integer, Integer> heroSkillMap = HeroService.getSkillMap(heroInfo);
		if (heroSkillMap == null) {
			heroSkillMap = new HashMap<Integer, Integer>();
		}
		if (skillMap != null && skillMap.size() > 0) {
			for (HeroXMLSkill xmlSkill : skillMap.values()) {
				int skillNo = xmlSkill.getSkillNo();
				int skillPos = xmlSkill.getSkillPos();
				HeroSkillXMLInfo skillXml = HeroXMLInfoMap.getHeroSkillXMLInfo(skillPos);
				if (skillXml == null) {
					return ErrorCode.INIT_SKILL_ERROR_2;
				}
				if (heroSkillMap.get(skillNo) != null) {
					continue;
				}

				if (heroXMLInfo.getInitial() == 0) {
					if (heroInfo.getQuality() < skillXml.getOtherColorOpen()) {
						continue;
					}

				} else {
					if (heroInfo.getHeroLevel() < skillXml.getMainLvOpen()) {
						continue;
					}
				}
				HeroSkillXMLUpgrade up = skillXml.getUpMap().get(1);
				if (up != null) {
					if (heroInfo.getHeroLevel() < up.getHeroLevel()) {
						continue;
					}
					heroSkillMap.put(skillNo, 1);
					if (!ischange) {
						ischange = true;
					}
				}
			}
		}

		if (ischange) {
			String skillStr = IntegerMapTypeHandler.getString(heroSkillMap);
			if (heroInfo.getId() > 0) {
				if (SkillDAO.getInstance().addOrUpdateHeroSkill(heroInfo.getId(), skillStr)) {
					heroInfo.setSkillStr(skillStr);
				} else {
					return ErrorCode.INIT_SKILL_ERROR_1;
				}
			} else {
				heroInfo.setSkillStr(skillStr);
			}
			// 有变动
			return 1;
		} else {
			// 无变动
			return 0;
		}
	}

	/**
	 * 刷新英雄属性 战斗力
	 * @param heroInfo
	 * @param type
	 */
	public static void refeshHeroProperty(RoleInfo roleInfo, HeroInfo... heroInfos) {
		if (heroInfos.length == 0) {
			return;
		}
		boolean rolefightChange = false;
		for (HeroInfo heroInfo : heroInfos) {
			if (heroInfo == null) {
				continue;
			}
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXMLInfo == null) {
				logger.info("heroInfo heroNo:" + heroInfo.getHeroNo() + " not exit");
				return;
			}
			// 刷新英雄战斗力
			int fightValue = recalHeroFightValue(heroInfo, HeroProService.getHeroTotalProperty(heroInfo, 1.0),
					HeroService.getSkillMap(heroInfo));
			if (fightValue != heroInfo.getFightValue()) {
				heroInfo.setFightValue(fightValue);
				if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM
						&& heroInfo.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT) {
					if (!rolefightChange) {
						rolefightChange = true;
					}
				}
			}
		}
		if (rolefightChange) {
			// 刷新角色战斗力
			int currFightValue = HeroInfoMap.getFightDeployHeroFightValue(roleInfo.getId());
			if (roleInfo.getFightValue() != currFightValue) {
				roleInfo.setFightValue(currFightValue);
			}
		}
	}

	/**
	 * 计算英雄战斗力
	 * @param heroInfo
	 */
	public static int recalHeroRecordFightValue(HeroRecord heroRecord, HeroPropertyInfo total,
			Map<Integer, Integer> skillMap) {
		int fightValue = recalHeroFightValue(total, skillMap);
		if (heroRecord != null && heroRecord.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
			EquipXMLInfo xmlInfo = null;
			for (EquipRecord equip : heroRecord.getEquipMap().values()) {
				xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(equip.getEquipNo());
				if (xmlInfo != null) {
					fightValue += Math.pow(xmlInfo.getCharacter(), 2) * 10;
				}
			}
			WeaponXmlInfo weaponXml = null;
			for (WeaponRecord weaponRecord : heroRecord.getWeaponList()) {
				weaponXml = WeaponXmlInfoMap.getWeaponXmlInfoByNo(weaponRecord.getWeaponNo());
				if (weaponXml != null && weaponXml.getWeaponType() == 5) {
					fightValue += weaponRecord.getLevel() * 1000;
				}
			}
		}
		return fightValue;
	}

	/**
	 * 计算英雄战斗力
	 * @param heroInfo
	 * @param total
	 * @param skillMap
	 * @return
	 */
	public static int recalHeroFightValue(HeroInfo heroInfo, HeroPropertyInfo total, Map<Integer, Integer> skillMap) {
		int fightValue = recalHeroFightValue(total, skillMap);
		if (heroInfo != null && heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
			EquipXMLInfo xmlInfo = null;
			for (EquipInfo equip : heroInfo.getEquipMap().values()) {
				xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(equip.getEquipNo());
				if (xmlInfo != null) {
					fightValue += Math.pow(xmlInfo.getCharacter(), 2) * 10;
				}
			}
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(heroInfo.getRoleId());
			if(roleInfo!=null){			
				WeaponXmlInfo weaponXml = null;
				for(RoleWeaponInfo weaponRecord :roleInfo.getRoleWeaponInfoPositionMap().values() ){
					weaponXml = WeaponXmlInfoMap.getWeaponXmlInfoByNo(weaponRecord.getWeaponNo());
					if (weaponXml != null && weaponXml.getWeaponType() == 5) {
						fightValue += weaponRecord.getLevel()*1000;
					}
				}	
			}	
		}
		return fightValue;
	}

	/**
	 * 计算装备战斗力
	 * @param equip
	 * @param total
	 * @return
	 */
	public static int recalEquipRecordFightValue(EquipRecord equip, HeroPropertyInfo total) {
		int fightValue = recalHeroFightValue(total, null);
		if (equip != null) {
			EquipXMLInfo xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(equip.getEquipNo());
			if (xmlInfo != null) {
				fightValue += Math.pow(xmlInfo.getCharacter(), 2) * 10;
			}
		}
		return fightValue;
	}
	
	/**
	 * 计算装备战斗力
	 * @param equip
	 * @param total
	 * @return
	 */
	public static int recalEquipFightValue(EquipInfo equip, HeroPropertyInfo total) {
		int fightValue = recalHeroFightValue(total, null);
		if (equip != null) {
			EquipXMLInfo xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(equip.getEquipNo());
			if (xmlInfo != null) {
				fightValue += Math.pow(xmlInfo.getCharacter(), 2) * 10;
			}
		}
		return fightValue;
	}
	
	/**
	 * 获取副将总战斗力（去除兵法，神兵）
	 * @param heroInfo
	 * @param rateMap
	 * @return
	 */
	public static int getOtherHeroFightValue(RoleInfo roleInfo) {
		
		int fightValue = 0;
		if(roleInfo != null){
			Map<Integer, HeroInfo> heroInfoMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
			if(heroInfoMap != null){
				for(HeroInfo heroInfo : heroInfoMap.values()){
					List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
					if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM
							&& heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN
							&& heroInfo.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT) {
						
						HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
						if (heroXMLInfo == null) {
							logger.info("heroInfo heroNo:" + heroInfo.getHeroNo() + " not exit");
							return 0;
						}

						// 武将基本属性
						list.addAll(HeroProService.getHeroBaseProperty(heroXMLInfo, heroInfo.getHeroLevel(), heroInfo.getQuality(),
								heroInfo.getIntimacyLevel(), heroInfo.getStar()));

						// 武将装备属性
						Map<HeroProType, Double> equipRate = new HashMap<HeroProType, Double>();
						list.addAll(HeroProService.getEquipAddProperty(heroInfo, heroXMLInfo, equipRate, null));
						
						HeroPropertyInfo total = HeroProService.generateHeroTotalProtperty(list, HeroProService.getProRate(1.0));
						
						Map<Integer, Integer> skillMap = getSkillMap(heroInfo);
						
						fightValue += recalHeroFightValue(total, skillMap);
						
					}
				}
			}
		}
		return fightValue;
	}
	
	/**
	 * 获取主将装备战斗力
	 * @param heroInfo
	 * @param rateMap
	 * @return
	 */
	public static int getHeroEquipFightValue(RoleInfo roleInfo) {
		
		int fightValue = 0;
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		if(roleInfo != null){
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if(heroInfo != null){
				
				HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
				if (heroXMLInfo == null) {
					logger.info("heroInfo heroNo:" + heroInfo.getHeroNo() + " not exit");
					return 0;
				}
				Map<HeroProType, Double> equipRate = new HashMap<HeroProType, Double>();
				list.addAll(HeroProService.getEquipAddProperty(heroInfo, heroXMLInfo, equipRate ,null));

				HeroPropertyInfo total = HeroProService.generateHeroTotalProtperty(list, HeroProService.getProRate(1.0));
				fightValue += recalHeroFightValue(total, null);
				
				Map<Integer, EquipInfo> equipMap = heroInfo.getEquipMap();
				if(equipMap != null && equipMap.size() > 0){
					EquipXMLInfo xmlInfo = null;
					for (EquipInfo equip : heroInfo.getEquipMap().values()) {
						xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(equip.getEquipNo());
						if (xmlInfo != null) {
							fightValue += Math.pow(xmlInfo.getCharacter(), 2) * 10;
						}
					}
				}
			}
		}
		return fightValue;
	}
	
	/**
	 * 获取兵法战斗力
	 * @param heroInfo
	 * @param rateMap
	 * @return
	 */
	public static int getSoldierFightValue(RoleInfo roleInfo) {

		int fightValue = 0;
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		int mainHeroLv = 0;
		if (roleInfo != null) {
			Map<Integer, HeroInfo> heroInfoMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
			mainHeroLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
			// 主武将兵法激活(到了开启兵法等级才生效)
			if(mainHeroLv >= GameValue.SOLDIER_UPGRADE_OPEN_LEVEL){
				list.add(HeroProService.getSoldierActProtperty(SoldierService.getSoldierMap(roleInfo)));
			}
			if(heroInfoMap != null){
				for(HeroInfo heroInfo : heroInfoMap.values()){
					if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM
							&& heroInfo.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT){
						HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
						if (heroXMLInfo == null) {
							logger.info("heroInfo heroNo:" + heroInfo.getHeroNo() + " not exit");
							return 0;
						}
						byte heroType = (byte) heroXMLInfo.getHeroType();
						int soldierLevel = SoldierService.getSoldierLevel(roleInfo, heroType);


						if (mainHeroLv >= GameValue.SOLDIER_UPGRADE_OPEN_LEVEL) {
							// 兵种基本属性(到了开启兵法等级才生效)
							list.add(HeroProService.getSoldierBaseProtperty(heroType, soldierLevel));

						}
					}
				}
			}
			
			
			HeroPropertyInfo total = HeroProService.generateHeroTotalProtperty(list, HeroProService.getProRate(1.0));
			
			fightValue += recalHeroFightValue(total, null);
		}

		return fightValue;
	}
	
	/**
	 * 获取神兵战斗力
	 * @param heroInfo
	 * @param rateMap
	 * @return
	 */
	public static int getMagicFightValue(RoleInfo roleInfo) {
		
		int fightValue = 0;
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		if (roleInfo != null) {
			Object[] weaponproarr = WeaponService.getWeaponHeroPropertyInfo(roleInfo);
			if (weaponproarr != null) {
				list.add((HeroPropertyInfo) weaponproarr[0]);
			}
		}
		HeroPropertyInfo total = HeroProService.generateHeroTotalProtperty(list, HeroProService.getProRate(1.0));
		fightValue = recalHeroFightValue(total, null);	
		WeaponXmlInfo weaponXml = null;
		for(RoleWeaponInfo weaponRecord :roleInfo.getRoleWeaponInfoPositionMap().values() ){
			weaponXml = WeaponXmlInfoMap.getWeaponXmlInfoByNo(weaponRecord.getWeaponNo());
			if (weaponXml != null && weaponXml.getWeaponType() == 5) {
				fightValue += weaponRecord.getLevel()*1000;
			}
		}	

		return fightValue;
	}
	

	/**
	 * 计算英雄战斗力
	 * @param heroInfo
	 */
	public static int recalHeroFightValue(HeroPropertyInfo total, Map<Integer, Integer> skillMap) {
		// 计算英雄战斗力
		// 生命值/30+士兵生命/30+法术攻击力+普攻+物理防御+法术防御+
		// （暴击+抗暴+技能暴击+技能抗暴+物理穿透+法术穿透+命中+闪避）/2+技能等级*10
		// +暴伤加成值/10+暴伤减免值/10+破兵值/10
		int fightValue = (int) (total.getHp() / 30.0
				+ total.getSoldierHp() / 30.0
				+ total.getRideHp() / 30.0
				+ total.getMagicAttack()
				+ total.getAd()
				+ total.getAttackDef()
				+ total.getMagicDef()
				+ (total.getCrit() + total.getCritAvo() + total.getSkillCrit() + total.getSkillCritAvo()
				+ total.getIgnorAttackAvo() + total.getIgnorMagicAvo() + total.getHit() + total.getMiss()) / 2.0
				+ total.getCritMore() / 10.0 + total.getCritLess() / 10.0 + total.getBreakSoldierDef() / 10.0)
				+ total.getReduceDamage() + (int) (total.getImmunityDamage() * 10000);
		int totalskillLevel = 0;
		if (skillMap != null) {
			for (int skillLevel : skillMap.values()) {
				totalskillLevel += skillLevel;
			}
		}
		fightValue += totalskillLevel * 10;
		return fightValue;
	}

	public static List<HeroDetailRe> getHeroDetailList(RoleInfo roleInfo, String idStr) {
		List<HeroDetailRe> list = new ArrayList<HeroDetailRe>();
		if (idStr != null && idStr.trim().length() > 0) {
			String[] ids = idStr.split(",");// 获取的英雄id
			for (String id : ids) {
				int heroId = Integer.valueOf(id.trim());
				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroId);
				if (heroInfo == null) {
					continue;
				}
				list.add(getHeroDetailRe(heroInfo));
			}
		} else {
			Map<Integer, HeroInfo> heroInfoMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
			if (heroInfoMap != null) {
				for (HeroInfo heroInfo : heroInfoMap.values()) {
					list.add(getHeroDetailRe(heroInfo));
				}
			}
		}
		return list;
	}

	public static HeroDetailRe getHeroDetailRe(HeroInfo heroInfo) {
		HeroDetailRe re = new HeroDetailRe();
		re.setHeroId((int) heroInfo.getId());
		re.setHeroInfo(getHeroInfoRe(heroInfo));

		List<HeroSkillRe> skillList = getHeroSkillList(heroInfo, "");
		re.setSkillCount(skillList.size());
		re.setSkillList(skillList);

		List<EquipDetailRe> equipList = EquipService.getHeroEquipList(heroInfo, "");
		re.setEquipCount(equipList.size());
		re.setEquipList(equipList);

		return re;
	}

	public static HeroInfoRe getHeroInfoRe(HeroInfo heroInfo) {
		HeroInfoRe re = new HeroInfoRe();
		re.setHeroId((int) heroInfo.getId());
		re.setHeroNo(heroInfo.getHeroNo());
		re.setDeployStatus(heroInfo.getDeployStatus());

		re.setHeroLevel((short) heroInfo.getHeroLevel());
		re.setHeroExp(heroInfo.getHeroExp());

		re.setIntimacyLevel((short) heroInfo.getIntimacyLevel());
		re.setIntimacyValue(heroInfo.getIntimacyValue());

		re.setQuality((byte) heroInfo.getQuality());
		re.setStar((byte) heroInfo.getStar());
		re.setFightValue(heroInfo.getFightValue());
		return re;
	}

	public static List<HeroSkillRe> getHeroSkillList(HeroInfo heroInfo, String idStr) {
		List<HeroSkillRe> list = new ArrayList<HeroSkillRe>();
		Map<Integer, Integer> skillMap = HeroService.getSkillMap(heroInfo);
		if (idStr != null && idStr.trim().length() > 0) {
			String[] ids = idStr.split(",");// 获取的英雄id
			for (String id : ids) {
				int skillNo = Integer.valueOf(id.trim());
				Integer skillLevel = skillMap.get(skillNo);
				if (skillLevel == null) {
					continue;
				}
				list.add(getHeroSkillRe(skillNo, skillLevel));
			}
		} else {
			if (skillMap != null) {
				for (int skillNo : skillMap.keySet()) {
					list.add(getHeroSkillRe(skillNo, skillMap.get(skillNo)));
				}
			}
		}
		return list;
	}

	public static HeroSkillRe getHeroSkillRe(int skillNo, int skillLevel) {
		HeroSkillRe re = new HeroSkillRe();
		re.setSkillNo(skillNo);
		re.setSkillLevel(skillLevel);
		return re;
	}

	/**
	 * 英雄增加经验
	 * @param heroInfo
	 * @param addExp
	 * @return
	 */
	public static int[] addHeroExp(RoleInfo roleInfo, HeroInfo heroInfo, int addExp) {
		int heroLevel = heroInfo.getHeroLevel();
		int heroExp = heroInfo.getHeroExp();
		int costExp = 0;
		if (addExp <= 0) {
			return new int[] { heroLevel, heroExp, costExp };
		}
		HashMap<Integer, HeroLevelXMLUpgrade> lvMap = null;
		int maxLv = 0;// 当前最高等级
		int pzMaxLv = 0;// 配置最高等级
		if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
			lvMap = HeroXMLInfoMap.getMainLvMap();
			pzMaxLv = HeroXMLInfoMap.getMaxMainLv();
			maxLv = pzMaxLv;
		} else {
			lvMap = HeroXMLInfoMap.getOtherLvMap();
			pzMaxLv = HeroXMLInfoMap.getMaxOtherLv();
			// 等级不能超出主武将等级
			maxLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
			if (maxLv > pzMaxLv) {
				maxLv = pzMaxLv;
			}
		}
		// 达到限制等级且达到经验上限就不添加
		if (lvMap == null || heroLevel >= pzMaxLv) {
			// lvMap == null xml有验证
			// 达到最高等级
			return null;
		}

		int afterLevel = heroLevel;// 升级后等级
		int afterExp = heroExp + addExp;// 升级后角色经验
		HeroLevelXMLUpgrade upgrade = null;
		while (afterLevel <= maxLv) {
			upgrade = lvMap.get(afterLevel + 1);
			if (upgrade != null) {
				if (upgrade.getExp() <= afterExp) {
					if (afterLevel == maxLv) {
						if (maxLv < pzMaxLv) {
							afterExp = upgrade.getExp();
							costExp += upgrade.getExp();
						} else {
							afterExp = 0;
						}
						break;
					} else {
						afterExp -= upgrade.getExp();
						costExp += upgrade.getExp();
					}
				} else {
					costExp += afterExp;
					break;
				}
			}
			if (afterLevel == maxLv) {
				break;
			}
			afterLevel++;
		}
		costExp -= heroExp;
		if (costExp <= 0) {
			costExp = 0;
		}
		return new int[] { afterLevel, afterExp, costExp };
	}

	/**
	 * 武将加经验（不推送武将变化）
	 * @param action
	 * @param roleInfo
	 * @param heroInfo
	 * @param addExp
	 * @param delMap 消耗的道具（推送道具变化）
	 * @return
	 */
	public static int heroExpAdd(int action, RoleInfo roleInfo, HeroInfo heroInfo, int addExp,
			Map<Integer, Integer> delMap) {
		if (addExp <= 0) {
			return ErrorCode.ADD_ERP_ERROR_1;
		}
		int beforeLevel = heroInfo.getHeroLevel();
		int beforeExp = heroInfo.getHeroExp();
		int[] after = addHeroExp(roleInfo, heroInfo, addExp);
		if (after == null) {
			return ErrorCode.ADD_ERP_ERROR_2;
		}
		if (delMap != null && delMap.size() > 0) {
			int delresult = ItemService.bagItemDel(action, roleInfo, delMap);
			if (delresult != 1) {
				return delresult;
			}
		}

		int afterLevel = after[0];
		int afterExp = after[1];
		// int costExp = after[2];
		if (beforeLevel != afterLevel || afterExp != beforeExp) {
			if (HeroDAO.getInstance().updateHeroLv(heroInfo.getId(), afterLevel, afterExp)) {
				heroInfo.setHeroLevel(afterLevel);
				heroInfo.setHeroExp(afterExp);
				if (beforeLevel != afterLevel) {
					// 升级了则按照HeroUp.xml中的Tili属性的数值赠送体力
					RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
					if (roleLoadInfo == null) {
						return ErrorCode.BAG_ERROR_6;
					}
					HashMap<Integer, HeroLevelXMLUpgrade> heroLvMap = HeroXMLInfoMap.getMainLvMap();
					if (heroLvMap == null) {
						return ErrorCode.HERO_UP_ERROR_6;
					}
					HeroLevelXMLUpgrade upgrade = heroLvMap.get(afterLevel);
					if (upgrade == null) {
						return ErrorCode.HERO_UP_ERROR_7;
					}
					RoleService.addRoleRoleResource(ActionType.action448.getType(), roleInfo, ConditionType.TYPE_SP,
							upgrade.getTili(),null);
					String spStr = roleInfo.getSp() + "," + roleInfo.getLastRecoverSPTime().getTime() + ","
							+ roleLoadInfo.getTodayBuySpNum() + "," + 0;
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_SP, spStr);

					// 升级刷新战斗力
					HeroService.refeshHeroProperty(roleInfo, heroInfo);

					if (heroInfo.getDeployStatus() == 1) {
						// 检测角色升级对活动影响
						OpActivityService.dealOpActProInfoCheck(roleInfo, ActionType.action448.getType(), null, true);
					}
					
					//升级后 角色世界聊天数量发送变化
					if(beforeLevel < GameValue.WORLD_CHAT_MAX_LEVEL){
						//升级前低于满级才推送 超过 则限制数据不会变化，只在登录时候推送一次
						RoleService.sendWorldChatLimit2MailServer(roleInfo, 0);
					}
				}
			} else {
				return ErrorCode.BAG_ERROR_6;
			}
			if (beforeLevel != afterLevel) {
				GameLogService.insertHeroUpLog(roleInfo, heroInfo.getHeroNo(), action, 1, beforeLevel, afterLevel);
			}
		}
		return 1;
	}

	/**
	 * 获取技能Map<skillNo,skillLevel>
	 * @param heroInfo
	 * @return
	 */
	public static Map<Integer, Integer> getSkillMap(HeroInfo heroInfo) {
		return getSkillMap(heroInfo.getSkillStr());
	}
	
	/**
	 * 获取技能Map<skillNo,skillLevel>
	 * @param heroInfo
	 * @return
	 */
	public static Map<Integer, Integer> getSkillMap(String allSkillStr) {
		try {
			return IntegerMapTypeHandler.getString(allSkillStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 升级或新增技能
	 * @param heroInfo
	 * @param skillNo
	 * @param skillLv
	 */
	public static String addOrUpdateSkill(HeroInfo heroInfo, int skillNo, int skillLv) {
		Map<Integer, Integer> skillMap = getSkillMap(heroInfo);
		if (skillMap == null) {
			skillMap = new HashMap<Integer, Integer>();
		}
		skillMap.put(skillNo, skillLv);
		return IntegerMapTypeHandler.getString(skillMap);
	}

	/**
	 * 获取技能等级
	 * @param heroInfo
	 * @param skillNo
	 * @return
	 */
	public static int getSkillLv(HeroInfo heroInfo, int skillNo) {
		Map<Integer, Integer> skillMap = getSkillMap(heroInfo);
		if (skillMap != null && skillMap.containsKey(skillNo)) {
			return skillMap.get(skillNo);
		}
		return 0;
	}

	/**
	 * 武将添加亲密度
	 * @param roleInfo
	 * @param heroInfo
	 * @param add
	 * @return
	 */
	public static int[] addHeroIntimacy(RoleInfo roleInfo, HeroInfo heroInfo, int add) {
		int intimacyLevel = heroInfo.getIntimacyLevel();
		int intimacyValue = heroInfo.getIntimacyValue();
		int cost = 0;
		if (add <= 0) {
			return new int[] { intimacyLevel, intimacyValue, cost };
		}
		Map<Integer, Integer> lvMap = HeroXMLInfoMap.getIntimacyCostMap();
		int pzMaxLv = HeroXMLInfoMap.getMaxIntimacy();// 配置最高等级
		int maxLv = heroInfo.getHeroLevel();// 当前最高等级
		// 亲密度等级不能超出武将等级
		if (maxLv > pzMaxLv) {
			maxLv = pzMaxLv;
		}
		// 达到限制等级就不添加
		if (lvMap == null || intimacyLevel >= maxLv) {
			// lvMap == null xml有验证
			// 达到最高等级
			return null;
		}
		int afterLevel = intimacyLevel;// 升级后等级
		int after = intimacyValue + add;// 升级后角色经验
		Integer costValue = null;
		while (afterLevel <= maxLv) {
			costValue = lvMap.get(afterLevel + 1);
			if (costValue != null) {
				if (costValue <= after) {
					if (afterLevel == maxLv) {
						if (maxLv < pzMaxLv) {
							after = costValue;
							cost += costValue;
						} else {
							after = 0;
						}
						break;
					} else {
						after -= costValue;
						cost += costValue;
					}
				} else {
					cost += after;
					break;
				}
			}
			if (afterLevel == maxLv) {
				break;
			}
			afterLevel++;
		}
		cost -= intimacyValue;
		if (cost <= 0) {
			cost = 0;
		}
		return new int[] { afterLevel, after, cost };
	}

	/**
	 * 武将加亲密度（不推送武将变化）
	 * @param action
	 * @param roleInfo
	 * @param heroInfo
	 * @param add
	 * @param delMap 消耗的道具（推送道具变化）
	 * @return
	 */
	public static int heroIntimacyAdd(int action, RoleInfo roleInfo, HeroInfo heroInfo, int add,
			Map<Integer, Integer> delMap) {
		if (add <= 0) {
			return ErrorCode.ADD_ERP_ERROR_1;
		}
		int beforeLevel = heroInfo.getIntimacyLevel();
		int beforeValue = heroInfo.getIntimacyValue();
		int[] after = addHeroIntimacy(roleInfo, heroInfo, add);
		if (after == null) {
			return ErrorCode.ADD_ERP_ERROR_2;
		}
		if (delMap != null && delMap.size() > 0) {
			int delresult = ItemService.bagItemDel(action, roleInfo, delMap);
			if (delresult != 1) {
				return delresult;
			}
		}
		int afterLevel = after[0];
		int afterValue = after[1];
		if (beforeLevel != afterLevel || afterValue != beforeValue) {
			if (HeroDAO.getInstance().updateHeroIntimacy(heroInfo.getId(), afterLevel, afterValue)) {
				heroInfo.setIntimacyLevel(afterLevel);
				heroInfo.setIntimacyValue(afterValue);
				if (beforeLevel != afterLevel) {
					HeroService.refeshHeroProperty(roleInfo, heroInfo);
				}
			} else {
				return ErrorCode.BAG_ERROR_6;
			}
			if (beforeLevel != afterLevel) {
				GameLogService.insertHeroUpLog(roleInfo, heroInfo.getHeroNo(), action, 4, beforeLevel, afterLevel);
			}
		}
		return 1;
	}
	
	/**
	 * 外网已有4、5星武将的武将都退回3星，返还升星用的武将碎片和银两。
	 * @param roleInfo
	 */
	public static void heroStarDownDeal(Map<Integer, Integer> starHeroIds, Map<Integer, Map<String, Integer>> itemMap) {
		GameSettingInfo setting = GameSettingMap.getValue(GameSettingKey.HERO_STAR_DOWN);
		if (setting == null) {
			GameSettingDAO.getInstance().checkAndInsert(GameSettingKey.HERO_STAR_DOWN);
		} else {
			return;
		}
		RoleInfo roleInfo = null;
		if (starHeroIds != null && starHeroIds.size() > 0) {
			if (HeroDAO.getInstance().updateHeroStarDown(starHeroIds)) {
				HeroInfo heroInfo = null;
				for (int heroId : starHeroIds.keySet()) {
					int roleId = starHeroIds.get(heroId);
					heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
					if (heroInfo != null) {
						heroInfo.setStar(3);
					}
				}
			}
		}

		if (itemMap != null && itemMap.size() > 0) {
			BagItemInfo itemInfo = null;
			for (int roleId : itemMap.keySet()) {
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				if (roleInfo != null) {

					Map<String, Integer> item = itemMap.get(roleId);
					if (item != null && item.size() > 0) {
						int addMoney = 0;
						List<BagItemInfo> insertBagItem = new ArrayList<BagItemInfo>();
						Map<Integer, Integer> updateMum = new HashMap<Integer, Integer>();

						for (String itemNo : item.keySet()) {
							int itemNum = item.get(itemNo);
							if (itemNo.equals(ConditionType.TYPE_MONEY.getName())) {
								addMoney += itemNum;
							} else {
								int chipNo = NumberUtils.toInt(itemNo);
								itemInfo = RoleDAO.getInstance().getBagItemInfo(chipNo, roleInfo.getId());
								if (itemInfo != null && itemInfo.getNum() > 0) {
									updateMum.put(itemInfo.getId(), itemNum + itemInfo.getNum());
								} else {
									insertBagItem.add(new BagItemInfo(roleId, BagItemInfo.getItemType(itemNo), chipNo,
											itemNum, 0, 0));
								}
							}
						}
						if (addMoney > 0) {
							RoleService.addRoleRoleResource(ActionType.action501.getType(), roleInfo,
									ConditionType.TYPE_MONEY, addMoney,null);
						}
						ItemDAO.getInstance().addItemBatch(insertBagItem, updateMum);
					}
				}
			}
		}
		
		logger.info("GameInit hero star down successed");
	}
}
