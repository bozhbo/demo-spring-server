package com.snail.webgame.game.protocal.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.EquipRecord;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.WeaponRecord;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.WeaponDao;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.weapon.equip.EquipWeaponReq;
import com.snail.webgame.game.protocal.weapon.equip.EquipWeaponResp;
import com.snail.webgame.game.protocal.weapon.query.QueryRoleWeaponInfoResp;
import com.snail.webgame.game.protocal.weapon.query.RoleWeaponInfoRe;
import com.snail.webgame.game.protocal.weapon.upgrade.UpgradeWeaponReq;
import com.snail.webgame.game.protocal.weapon.upgrade.UpgradeWeaponResp;
import com.snail.webgame.game.xml.cache.WeaponExpXmlInfoMap;
import com.snail.webgame.game.xml.cache.WeaponSuitXmlInfoMap;
import com.snail.webgame.game.xml.cache.WeaponXmlInfoMap;
import com.snail.webgame.game.xml.info.WeaponExpXmlInfo;
import com.snail.webgame.game.xml.info.WeaponLv;
import com.snail.webgame.game.xml.info.WeaponRefine;
import com.snail.webgame.game.xml.info.WeaponSpecial;
import com.snail.webgame.game.xml.info.WeaponSuitNum;
import com.snail.webgame.game.xml.info.WeaponSuitXmlInfo;
import com.snail.webgame.game.xml.info.WeaponXmlInfo;

/**
 * 神兵业务类
 * @author xiasd
 */
public class WeaponService {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 查询角色神兵信息
	 * @param roleId
	 * @return
	 */
	public static QueryRoleWeaponInfoResp getRoleWeaponInfo(int roleId) {
		QueryRoleWeaponInfoResp resp = new QueryRoleWeaponInfoResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_13);
			return resp;
		}
		synchronized (roleInfo) {
			
			//加载神兵信息
			if(roleInfo.getWeaponFlag() != 1)
			{
				RoleDAO.getInstance().WeaponInfo(roleInfo);
			}
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_LOAD_ERROR_3);
				return resp;
			}
			List<RoleWeaponInfoRe> list = new ArrayList<RoleWeaponInfoRe>();

			RoleWeaponInfoRe roleWeaponInfoRe;

			for (Entry<Integer, RoleWeaponInfo> entry : roleLoadInfo.getRoleWeaponInfoMap().entrySet()) {
				roleWeaponInfoRe = new RoleWeaponInfoRe();
				roleWeaponInfoRe.setPosition(entry.getValue().getPosition());
				roleWeaponInfoRe.setExp(entry.getValue().getExp());
				roleWeaponInfoRe.setLevel(entry.getValue().getLevel());
				roleWeaponInfoRe.setWeaponId(entry.getValue().getId());
				roleWeaponInfoRe.setWeaponNo(entry.getValue().getWeaponNo());
				list.add(roleWeaponInfoRe);
			}
			resp.setList(list);
			resp.setCount(list.size());
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 装备或卸载神兵
	 * @param roleId
	 * @return
	 */
	public static EquipWeaponResp equipWeapon(int roleId, EquipWeaponReq req) {
		EquipWeaponResp resp = new EquipWeaponResp();
		
		if(GameValue.GAME_SB_OPEN != 1){
			resp.setResult(ErrorCode.NOT_OPEN_ERROR);
			return resp;
		}
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_13);
			return resp;
		}
		synchronized (roleInfo) {
			
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null) {
				resp.setResult(ErrorCode.ROLE_LOAD_ERROR_2);
				return resp;
			}
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_LOAD_ERROR_3);
				return resp;
			}

			int weaponId = req.getWeaponId();
			RoleWeaponInfo roleWeaponInfo = roleLoadInfo.getRoleWeaponInfoMap().get(weaponId);
			// 检查是否有此装备
			if (roleWeaponInfo == null) {
				resp.setResult(ErrorCode.WEAPON_NOT_EXISIT);
				return resp;
			}
			byte equipPos = (byte) req.getEquipPosition();// 镶嵌的位置 0-卸载 else 部位
															// 1-武器、2-盔甲、3-头盔、4-护腕、5-披风、6-鞋子

			// 检查是否已经装备或未装备
			if (equipPos != 0 && roleWeaponInfo.getPosition() != 0) {
				resp.setResult(ErrorCode.WEAPON_ALREADY_EQUIPED);
				return resp;
			} else if (equipPos == 0 && roleWeaponInfo.getPosition() == 0) {
				resp.setResult(ErrorCode.WEAPON_NOT_EQUIPED);
				return resp;
			}

			// 参数非法
			if (equipPos != 0 && equipPos != 1 && equipPos != 2 && equipPos != 3 && equipPos != 4 && equipPos != 5
					&& equipPos != 6) {
				resp.setResult(ErrorCode.REQUEST_PARAM_ERROR);
				return resp;
			}

			// 检查当前位置上是否已经有神兵
			if (equipPos != 0) {// 卸载不需要判断
				WeaponXmlInfo srcWeaponXmlInfo = WeaponXmlInfoMap.getWeaponXmlInfoByNo(roleWeaponInfo.getWeaponNo());

				if (srcWeaponXmlInfo == null) {
					resp.setResult(ErrorCode.XML_PARAM_ERROR);
					return resp;
				}

				// 判断此格子是否已经开启
				int result = checkWeaponTypeIsOpen(roleInfo, srcWeaponXmlInfo);

				if (result != 1) {
					resp.setResult(result);
					return resp;
				}

				RoleWeaponInfo removeWeaponInfo = null;// 待卸载的装备

				for (Entry<Integer, RoleWeaponInfo> entry : roleInfo.getRoleWeaponInfoPositionMap().entrySet()) {
					RoleWeaponInfo info = entry.getValue();

					if (info.getPosition() == equipPos) {// 位置相同
						WeaponXmlInfo weaponXmlInfo = WeaponXmlInfoMap.getWeaponXmlInfoByNo(info.getWeaponNo());

						if (weaponXmlInfo == null) {
							resp.setResult(ErrorCode.XML_PARAM_ERROR);
							return resp;
						}

						if (weaponXmlInfo.getWeaponType() == srcWeaponXmlInfo.getWeaponType()) {// 类型相同
							// 表明同一个部位上，已经有神兵了，默认卸载上面的神兵
							if (!WeaponDao.getInstance().equipOrOffWeapon(info.getId(), 0)) {
								resp.setResult(ErrorCode.SQL_DB_ERROR);
								return resp;
							}
							info.setPosition((byte) 0);
							removeWeaponInfo = info;
						}
					} else {
						// 不是相同位置上，核心神兵装备唯一
						WeaponXmlInfo weaponXmlInfo = WeaponXmlInfoMap.getWeaponXmlInfoByNo(info.getWeaponNo());

						if (weaponXmlInfo == null) {
							resp.setResult(ErrorCode.XML_PARAM_ERROR);
							return resp;
						}
						if(weaponXmlInfo.getWeaponType() == 5 && roleWeaponInfo.getWeaponNo() == info.getWeaponNo()){
							resp.setResult(ErrorCode.WEAPON_MAIN_IS_SAME_ERROR);
							return resp;
						}
					}
				}
				if (removeWeaponInfo != null) {
					roleInfo.getRoleWeaponInfoPositionMap().remove(removeWeaponInfo.getId());
				}
			}

			// 更新数据库
			if (!WeaponDao.getInstance().equipOrOffWeapon(roleWeaponInfo.getId(), equipPos)) {
				resp.setResult(ErrorCode.SQL_DB_ERROR);
				return resp;
			}
			roleWeaponInfo.setPosition(equipPos);
			if (equipPos == 0) {
				roleInfo.getRoleWeaponInfoPositionMap().remove(roleWeaponInfo.getId());
			} else {
				roleInfo.getRoleWeaponInfoPositionMap().put(roleWeaponInfo.getId(), roleWeaponInfo);
			}
			resp.setEquipPosition(equipPos);
			resp.setWeaponId(weaponId);
			resp.setResult(1);
			
			//刷新武将信息
			HeroService.refeshHeroProperty(roleInfo, mainHero);
			resp.setHeroId(mainHero.getId());
			resp.setFightValue(mainHero.getFightValue());
		}
		return resp;
	}

	public static int checkWeaponPosIsOpen(RoleInfo roleInfo, byte equipPos) {
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHero == null) {
			return ErrorCode.HERO_UP_ERROR_8;
		}
		
		switch(equipPos){
			case 1:
				if (mainHero.getHeroLevel() < GameValue.WEAPON_POS_1_LV) {
					return ErrorCode.WEAPON_POS_NOT_OPEN;
				}
				break;
			case 2:
				if (mainHero.getHeroLevel() < GameValue.WEAPON_POS_2_LV) {
					return ErrorCode.WEAPON_POS_NOT_OPEN;
				}
				break;
			case 3:
				if (mainHero.getHeroLevel() < GameValue.WEAPON_POS_3_LV) {
					return ErrorCode.WEAPON_POS_NOT_OPEN;
				}
				break;
			case 4:
				if (mainHero.getHeroLevel() < GameValue.WEAPON_POS_4_LV) {
					return ErrorCode.WEAPON_POS_NOT_OPEN;
				}
				break;
			case 5:
				if (mainHero.getHeroLevel() < GameValue.WEAPON_POS_5_LV) {
					return ErrorCode.WEAPON_POS_NOT_OPEN;
				}
				break;
			case 6:
				if (mainHero.getHeroLevel() < GameValue.WEAPON_POS_6_LV) {
					return ErrorCode.WEAPON_POS_NOT_OPEN;
				}
				break;
			default:
				return ErrorCode.WEAPON_TYPE_EQUIPED_ERROR;
		}
		return 1;
	}
	
	/**
	 * 检查格子是否开启
	 * @param roleInfo
	 * @param srcWeaponXmlInfo
	 * @return
	 */
	private static int checkWeaponTypeIsOpen(RoleInfo roleInfo, WeaponXmlInfo srcWeaponXmlInfo) {
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);

		if (mainHero == null) {
			return ErrorCode.HERO_UP_ERROR_8;
		}
		switch (srcWeaponXmlInfo.getWeaponType()) {
		case 1:
			if (mainHero.getHeroLevel() < GameValue.WEAPON_FIELD_1_LV) {
				return ErrorCode.WEAPON_TYPE_NOT_OPEN;
			}
			break;
		case 2:
			if (mainHero.getHeroLevel() < GameValue.WEAPON_FIELD_2_LV) {
				return ErrorCode.WEAPON_TYPE_NOT_OPEN;
			}
			break;
		case 3:
			if (mainHero.getHeroLevel() < GameValue.WEAPON_FIELD_3_LV) {
				return ErrorCode.WEAPON_TYPE_NOT_OPEN;
			}
			break;
		case 4:
			if (mainHero.getHeroLevel() < GameValue.WEAPON_FIELD_4_LV) {
				return ErrorCode.WEAPON_TYPE_NOT_OPEN;
			}
			break;
		case 5:
			if (mainHero.getHeroLevel() < GameValue.WEAPON_FIELD_5_LV) {
				return ErrorCode.WEAPON_TYPE_NOT_OPEN;
			}
			break;
		case 6:
			return ErrorCode.WEAPON_EXP;
		default:
			return ErrorCode.WEAPON_TYPE_EQUIPED_ERROR;
		}

		return 1;
	}

	/**
	 * 升级神兵
	 * @param roleId
	 * @return
	 */
	public static UpgradeWeaponResp upgradeWeapon(UpgradeWeaponReq req, int roleId) {
		UpgradeWeaponResp resp = new UpgradeWeaponResp();
		
		if(GameValue.GAME_SB_OPEN != 1){
			resp.setResult(ErrorCode.NOT_OPEN_ERROR);
			return resp;
		}
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

			int weaponId = req.getWeaponId();
			RoleWeaponInfo roleWeaponInfo = roleLoadInfo.getRoleWeaponInfoMap().get(weaponId);
			// 检查是否有此神兵
			if (roleWeaponInfo == null) {
				resp.setResult(ErrorCode.WEAPON_NOT_EXISIT);
				return resp;
			}
			short beforeLevel = roleWeaponInfo.getLevel();// 升级前的等级
			short afterLevel = beforeLevel;// 升级后的等级
			int afterExp = roleWeaponInfo.getExp();

			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);

			if (mainHero == null) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_8);
				return resp;
			}
			WeaponXmlInfo weaponXmlInfo = WeaponXmlInfoMap.getWeaponXmlInfoByNo(roleWeaponInfo.getWeaponNo());

			if (weaponXmlInfo == null) {
				resp.setResult(ErrorCode.XML_PARAM_ERROR);
				return resp;
			}

			int result = 0;

			if (weaponXmlInfo.getWeaponType() == 5) {
				// 核心神兵走进阶
				result = upgradeMainWeapon(roleInfo, weaponXmlInfo, roleWeaponInfo, mainHero, resp, req);
			} else {
				// 非核心神兵走强化
				result = strongWeapon(roleInfo, afterLevel, afterExp, mainHero, resp, req);
			}

			if(resp.getLevel() > beforeLevel){
				GameLogService.insertWeaponUpLog(roleInfo, ActionType.action359.getType(), 0, beforeLevel, resp.getLevel(), roleWeaponInfo.getId(), weaponXmlInfo.getNo());
			}
			
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			// 删除消耗掉的神兵
			if (resp.getDelWeaponIds() != null && !"".equals(resp.getDelWeaponIds())) {
				String[] weaponArr = resp.getDelWeaponIds().split(",");
				// 更新数据库
				if (!WeaponDao.getInstance().deleteBatchRoleWeaponInfo(weaponArr)) {
					resp.setResult(ErrorCode.SQL_DB_ERROR);
					return resp;
				}
				// 删除缓存
				for (String id : weaponArr) {
					roleLoadInfo.getRoleWeaponInfoMap().remove(Integer.valueOf(id));
				}
			}
			// 更新数据库，等级，经验
			if (WeaponDao.getInstance().upgradeRoleWeaponInfoById(roleWeaponInfo.getId(), resp.getLevel(),
					resp.getExp())) {
				roleWeaponInfo.setLevel(resp.getLevel());
				roleWeaponInfo.setExp(resp.getExp());
			}else{
				resp.setResult(ErrorCode.SQL_DB_ERROR);
				return resp;
			}

			// 任务检测
			QuestService.checkQuest(roleInfo, ActionType.action359.getType(), null, true, true);
			
			//刷新武将信息
			HeroService.refeshHeroProperty(roleInfo, mainHero);
			resp.setHeroId(mainHero.getId());
			resp.setFightValue(mainHero.getFightValue());
			
			resp.setResult(1);
			resp.setWeaponId(weaponId);
		}
		return resp;
	}

	/**
	 * 进阶核心神兵
	 * @param weaponXmlInfo
	 * @param roleWeaponInfo
	 * @param mainHero
	 * @param resp
	 * @param req
	 */
	private static int upgradeMainWeapon(RoleInfo roleInfo, WeaponXmlInfo weaponXmlInfo, RoleWeaponInfo roleWeaponInfo,
			HeroInfo mainHero, UpgradeWeaponResp resp, UpgradeWeaponReq req) {
		WeaponRefine refine = weaponXmlInfo.getRefineMap().get(roleWeaponInfo.getLevel());

		if (refine == null || refine.getMainHeroLv() == 0) {
			return ErrorCode.WEAPON_FULL_LEVEL;
		}
		// 检查主将等级是否足够
		if (mainHero.getHeroLevel() < refine.getMainHeroLv()) {
			return ErrorCode.WEAPON_MAIN_HERO_LV_ERROR;
		}
		try {
			if(req.getWeaponIds() == null) return ErrorCode.WEAPON_MATERIAL_ERROR;
			String[] costWeaponArr = req.getWeaponIds().split(",");

			if (costWeaponArr.length != refine.getCostWeaponsNum()) {
				return ErrorCode.WEAPON_MATERIAL_ERROR;
			}

			RoleWeaponInfo tempExpWeapInfo;
			// 检查各种请客
			for (String seqId : costWeaponArr) {
				int expWeaponId = Integer.valueOf(seqId);
				tempExpWeapInfo = roleInfo.getRoleLoadInfo().getRoleWeaponInfoMap().get(expWeaponId);
				// 检查是否有此经验神兵
				if (tempExpWeapInfo == null) {
					return ErrorCode.WEAPON_NOT_EXISIT;
				}

				// 装备上的神兵不能被作为素材
				if (tempExpWeapInfo.getPosition() != 0) {
					return ErrorCode.WEAPON_IN_POS;
				}

				if (tempExpWeapInfo.getWeaponNo() != roleWeaponInfo.getWeaponNo()) {
					return ErrorCode.WEAPON_NOT_SAME_ERROR;
				}
			}
			// 升级
			resp.setLevel((short) (roleWeaponInfo.getLevel() + 1));
			resp.setDelWeaponIds(req.getWeaponIds());
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("upgradeMainWeapon error : " + e.getMessage());
			}
			return ErrorCode.WEAPON_PARAM_ERROR;
		}

		return 1;
	}

	/**
	 * 强化普通神兵
	 * @param roleInfo
	 * @param afterLevel
	 * @param afterExp
	 * @param mainHero
	 * @param resp
	 * @param req
	 * @return
	 */
	private static int strongWeapon(RoleInfo roleInfo, short afterLevel, int afterExp, HeroInfo mainHero,
			UpgradeWeaponResp resp, UpgradeWeaponReq req) {
		// 检查神兵是否已经强化到最大
		int result = checkWeaponLevel(roleInfo, afterLevel, afterExp, mainHero);

		if (result != 1) {
			return result;
		}

		try {
			String[] expWeaponStrArr = req.getWeaponIds().split(",");// 素材神兵
			RoleWeaponInfo tempExpWeapInfo;
			WeaponExpXmlInfo weaponExpXmlInfo;
			StringBuilder delWeaponIds = new StringBuilder();

			out: for (String seqId : expWeaponStrArr) {
				int expWeaponId = Integer.valueOf(seqId);
				tempExpWeapInfo = roleInfo.getRoleLoadInfo().getRoleWeaponInfoMap().get(expWeaponId);
				// 检查是否有此经验神兵
				if (tempExpWeapInfo == null) {
					return ErrorCode.WEAPON_NOT_EXISIT;
				}

				// 装备上的神兵不能被作为素材
				if (tempExpWeapInfo.getPosition() != 0) {
					return ErrorCode.WEAPON_IN_POS;
				}
				
				afterExp += getRoleWeaponAllExp(tempExpWeapInfo);
				
				weaponExpXmlInfo = WeaponExpXmlInfoMap.getWeaponXmlInfoByLevel((short) (afterLevel + 1));

				if (weaponExpXmlInfo == null || weaponExpXmlInfo.getExp() == 0) {
					return ErrorCode.WEAPON_FULL_LEVEL;
				}

				while (true) {
					if (afterExp >= weaponExpXmlInfo.getExp()) {
						if (afterLevel < mainHero.getHeroLevel()) {
							afterExp = afterExp - weaponExpXmlInfo.getExp();// 升级后多出来的经验
							afterLevel++;
							weaponExpXmlInfo = WeaponExpXmlInfoMap.getWeaponXmlInfoByLevel((short) (afterLevel + 1));

							if (weaponExpXmlInfo == null || weaponExpXmlInfo.getExp() == 0) {// 此处判断是否等于0（实际XML不可能为0），是为了afterExp必定一直减少，防止while死循环
								return ErrorCode.WEAPON_FULL_LEVEL;
							}
						} else {
							afterExp = weaponExpXmlInfo.getExp() - 1;// 当前升级经验少1，满了
							break out;
						}
					} else {
						delWeaponIds.append(expWeaponId).append(",");
						break;
					}
				}
			}
			resp.setDelWeaponIds(delWeaponIds.length() > 0 ? delWeaponIds.substring(0, delWeaponIds.length() - 1)
					: delWeaponIds.toString());
			resp.setLevel(afterLevel);
			resp.setExp(afterExp);
		} catch (Exception e) {
			return ErrorCode.XML_PARAM_ERROR;
		}

		return 1;
	}

	/**
	 * 获取神兵等级
	 * 
	 * @param info
	 * @return
	 */
	private static int getRoleWeaponAllExp(RoleWeaponInfo info){
		int exp = 0;
		WeaponXmlInfo weaponXmlInfo = WeaponXmlInfoMap.getWeaponXmlInfoByNo(info.getWeaponNo());
		// 先加上神兵的基础经验
		exp += weaponXmlInfo.getExp();
		// 再加上神兵的等级经验
		WeaponExpXmlInfo weaponExpXmlInfo;
		
		for(int i=1;i<info.getLevel();i++){
			weaponExpXmlInfo = WeaponExpXmlInfoMap.getWeaponXmlInfoByLevel((short) (i + 1));
			
			if (weaponExpXmlInfo == null) {
				return ErrorCode.XML_PARAM_ERROR;
			}
			exp += weaponExpXmlInfo.getExp();
		}
		// 再加上多出来的经验
		exp += info.getExp();
		
		return exp;
	}
	/**
	 * 检查神兵受否已经强化到最大
	 * @param roleInfo
	 * @param currentLevel
	 * @param currentExp
	 * @param mainHero
	 * @return
	 */
	private static int checkWeaponLevel(RoleInfo roleInfo, int currentLevel, int currentExp, HeroInfo mainHero) {
		// 检查神兵等级，神兵等级不能大于主武将的等级
		WeaponExpXmlInfo weaponExpXmlInfo = WeaponExpXmlInfoMap.getWeaponXmlInfoByLevel((short) (currentLevel + 1));

		if (weaponExpXmlInfo == null) {
			return ErrorCode.WEAPON_FULL_LEVEL;
		}

		// 如果达到最大等级，并且当前等级经验已满不能继续强化
		if (mainHero == null) {
			return 0;
		}
		if (currentLevel == mainHero.getHeroLevel() && currentExp == weaponExpXmlInfo.getExp() - 1) {
			return ErrorCode.WEAPON_FULL_LEVEL;
		}
		return 1;
	}

	/**
	 * 获取神兵的属性
	 * @param roleInfo
	 * @return Object[0]-HeroPropertyInfo Object[1]-ArrayList<String>技能字符串
	 */
	public static Object[] getWeaponHeroPropertyInfo(RoleInfo roleInfo) {
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHero == null) {
			return new Object[2];
		}
		
		HeroRecord record=HeroRecordService.getDeployHeroRecord(mainHero, mainHero.getDeployStatus());
		return getWeaponHeroPropertyInfo(record);
	}

	/**
	 * 获取装备加成属性
	 * @param stoneNo
	 * @return
	 */
	private static void getWeaponEquipProperty(HeroProType type, float effectNum, HeroPropertyInfo heroPropertyInfo) {
		if(type != null){
			heroPropertyInfo.addValue(type, effectNum);
		}
	}

	/**
	 * 获取神兵的属性
	 * @param heroRecord
	 * @return
	 */
	public static Object[] getWeaponHeroPropertyInfo(HeroRecord heroRecord) {	
		if (heroRecord == null || heroRecord.getWeaponList() == null || heroRecord.getWeaponList().size() <= 0) {
			return new Object[2];
		}
		HeroPropertyInfo heroPropertyInfo = new HeroPropertyInfo();
		ArrayList<String> suitSkillList = null;// 获得神兵的技能集合
		WeaponLv weaponLv;
		WeaponLv weaponUpdate;
		WeaponRefine refine;
		WeaponXmlInfo weaponXmlInfo;
		// 套装属性<装备位置，<套装编号,套装数量>>
		Map<Byte, Map<Integer, Integer>> suitMap = new HashMap<Byte, Map<Integer, Integer>>();

		for (WeaponRecord weaponRecord : heroRecord.getWeaponList()) {
			weaponXmlInfo = WeaponXmlInfoMap.getWeaponXmlInfoByNo(weaponRecord.getWeaponNo());
			if (weaponXmlInfo == null) {
				continue;
			}

			if (weaponXmlInfo.getWeaponType() == 5) {
				// 核心神兵
				refine = weaponXmlInfo.getRefineMap().get(weaponRecord.getLevel());
				// 核心神兵只提供技能
				if (suitSkillList == null) {
					suitSkillList = new ArrayList<String>();
				}
				if(refine != null){
					suitSkillList.add(refine.getSkill());
				}
			} else {
				// 普通神兵
				weaponLv = weaponXmlInfo.getWeaponLv();
				weaponUpdate = weaponXmlInfo.getUpdateWeaponLv();			
				if(weaponRecord.getLevel() >= 1 && weaponLv != null){
					// 神兵本身属性加成
					heroPropertyInfo.setAd(heroPropertyInfo.getAd() + weaponLv.getAd());
					heroPropertyInfo.setAttack(heroPropertyInfo.getAttack() + weaponLv.getAttack());
					heroPropertyInfo.setAttackDef(heroPropertyInfo.getAttackDef() + weaponLv.getAttackDef() );
					heroPropertyInfo.setHp(heroPropertyInfo.getHp() + weaponLv.getHp());
					heroPropertyInfo.setMagicAttack(heroPropertyInfo.getMagicAttack() + weaponLv.getMagicAttack());
					heroPropertyInfo.setMagicDef(heroPropertyInfo.getMagicDef() + weaponLv.getMagicDef());
				}								
				if(weaponRecord.getLevel() > 1 && weaponUpdate != null){				
					// 神兵等级属性加成
					heroPropertyInfo.setAd(heroPropertyInfo.getAd() + weaponUpdate.getAd() * weaponRecord.getLevel());
					heroPropertyInfo.setAttack(heroPropertyInfo.getAttack() + weaponUpdate.getAttack() * weaponRecord.getLevel());
					heroPropertyInfo.setAttackDef(heroPropertyInfo.getAttackDef() + weaponUpdate.getAttackDef() * weaponRecord.getLevel());
					heroPropertyInfo.setHp(heroPropertyInfo.getHp() + weaponUpdate.getHp() * weaponRecord.getLevel());
					heroPropertyInfo.setMagicAttack(heroPropertyInfo.getMagicAttack() + weaponUpdate.getMagicAttack() * weaponRecord.getLevel());
					heroPropertyInfo.setMagicDef(heroPropertyInfo.getMagicDef() + weaponUpdate.getMagicDef() * weaponRecord.getLevel());
				}

				// 神兵因装备获得的隐藏属性加成
				// 获取对应神兵部位的装备
				EquipRecord samePosEquip = heroRecord.getEquipRecordbyEquipType(weaponXmlInfo.getWeaponType());
				// 如果该神兵部位上没装备，直接遍历下一个神兵
				if (samePosEquip != null) {
					for (WeaponSpecial special : weaponXmlInfo.getSpecialList()) {
						if (special.getSpecialType() == 1) {// 1.装备强化等级
							if (samePosEquip.getEquipLevel() >= special.getSpecialNum()) {
								getWeaponEquipProperty(special.getEffectType(), special.getEffectNum(), heroPropertyInfo);
							}

						} else if (special.getSpecialType() == 2) {// 2.装备精练等级
							if (samePosEquip.getRefineLv() >= special.getSpecialNum()) {
								getWeaponEquipProperty(special.getEffectType(), special.getEffectNum(), heroPropertyInfo);
							}
						}
					}
				}
			}

			Map<Integer, Integer> posSuitMap = suitMap.get(weaponRecord.getPosition());
			if (posSuitMap == null) {
				posSuitMap = new HashMap<Integer, Integer>();
				suitMap.put(weaponRecord.getPosition(), posSuitMap);
			}
			Integer suitNum = posSuitMap.get(weaponXmlInfo.getSuit());
			if (suitNum != null) {
				suitNum = suitNum + 1;
			} else {
				suitNum = 1;
			}
			// 统计套装套数
			posSuitMap.put(weaponXmlInfo.getSuit(), suitNum);
		}

		// 套装效果遍历
		WeaponSuitXmlInfo weaponSuitXmlInfo = null;
		HeroProType proType = null;
		for (Map<Integer, Integer> posSuitMap : suitMap.values()) {
			for (int suitNo : posSuitMap.keySet()) {
				int suitNum = posSuitMap.get(suitNo);
				weaponSuitXmlInfo = WeaponSuitXmlInfoMap.getWeaponSuitXmlInfoByNo(suitNo);
				if (weaponSuitXmlInfo != null) {
					for (WeaponSuitNum suitXmlNum : weaponSuitXmlInfo.getMap().values()) {
						if (suitNum >= suitXmlNum.getNum()) {
							if (suitXmlNum.getType() != 101) {
								proType = HeroProType.getHeroProType(suitXmlNum.getType());
								getWeaponEquipProperty(proType, Integer.valueOf(suitXmlNum.getEffect()), heroPropertyInfo);
							} else {
								if (suitSkillList == null) {
									suitSkillList = new ArrayList<String>();
								}
								if (!suitSkillList.contains(suitXmlNum.getEffect())) {
									suitSkillList.add(suitXmlNum.getEffect());
								}
							}
						}
					}
				}
			}
		}
		return new Object[] { heroPropertyInfo, suitSkillList };
	}

}
