package com.snail.webgame.game.protocal.equip.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.equip.equipStone.EquipStoneRe;
import com.snail.webgame.game.protocal.equip.heroQuery.HeroEquipDetailRe;
import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.equip.query.EquipInfoRe;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropAttriAddXmlMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.EquipEffectConfigInfo;
import com.snail.webgame.game.xml.info.EquipExtraInfo;
import com.snail.webgame.game.xml.info.EquipRefineInfo;
import com.snail.webgame.game.xml.info.EquipStrengthenInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.PropAttriAddXml;
import com.snail.webgame.game.xml.info.PropXMLInfo;

public class EquipService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 初始化装备背包属性
	 * @param roleInfo
	 */
	public static void loginInitBagEquipProperty(RoleInfo roleInfo) {
		Map<Integer, EquipInfo> equipInfoMap = EquipInfoMap.getBagEquipMap(roleInfo.getId());
		if (equipInfoMap == null) {
			return;
		}

		for (EquipInfo equipInfo : equipInfoMap.values()) {
			refeshEquipProperty(equipInfo);
		}
	}

	/**
	 * 初始化英雄装备属性
	 * @param heroInfo
	 */
	public static void loginInitHeroEquipProperty(HeroInfo heroInfo) {
		Map<Integer, EquipInfo> equipInfoMap = EquipInfoMap.getHeroEquipMap(heroInfo);
		if (equipInfoMap == null) {
			return;
		}

		for (EquipInfo equipInfo : equipInfoMap.values()) {
			refeshEquipProperty(equipInfo);
		}
	}

	/**
	 * 刷新装备属性
	 * @param heroInfo
	 * @param type
	 */
	public static void refeshEquipProperty(EquipInfo equipInfo) {
		EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
		if (equipXMLInfo == null) {
			// logger.info("EquipInfo EquipNo:" + equipInfo.getEquipNo() + " not exit");
			return;
		}

		// 计算总属性
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		list.add(EquipService.getEquipBaseProperty(equipXMLInfo, equipInfo.getLevel(), equipInfo.getRefineLv(), equipInfo.getEnchantLv(), null));

		// 刷新装备战斗力
		int fightValue = HeroService.recalEquipFightValue(equipInfo,HeroProService.generateHeroTotalProtperty(list));
		if (fightValue != equipInfo.getFightValue()) {
			equipInfo.setFightValue(fightValue);
		}
	}

	/**
	 * 获取装备基础,品质,强化,精炼属性加成
	 * @param equipInfo
	 * @param equipXMLInfo
	 * @return
	 */
	public static HeroPropertyInfo getEquipBaseProperty(EquipXMLInfo equipXMLInfo, int equipLevel, int refineLevel,int enchantLevel,
			Map<HeroProType, Double> rate) {
		HeroPropertyInfo pro = (HeroPropertyInfo) equipXMLInfo.clone();
		if (equipLevel > 0) {

			// 装备品质加成（配置文件配好的Equip里的Character与Type决定）
			equipQuailtyAdd(pro, equipXMLInfo, equipLevel);

			// 装备强化等级加成
			upgradePlusCheck(pro, rate, equipXMLInfo, equipLevel);
		}

		// 装备额外属性加成
		addEquipExtraVal(pro, rate, equipXMLInfo);

		// 装备精炼等级加成
		refinePlus(pro, rate, equipXMLInfo, refineLevel);
		
		equipEnchantAdd(pro, equipXMLInfo, enchantLevel);
		return pro;
	}

	/**
	 * 获取宝石镶嵌属性
	 * @param equipInfo
	 * @return
	 */
	public static HeroPropertyInfo getEquipGemProperty(EquipInfo equipInfo) {
		// 宝石信息<seat,stoneNo>
		Map<Integer, Integer> stoneMap = equipInfo.getStoneMap();
		return getEquipGemProperty(stoneMap);
	}

	/**
	 * 获取宝石加成属性
	 * @param stoneMap
	 * @return
	 */
	public static HeroPropertyInfo getEquipGemProperty(Map<Integer, Integer> stoneMap) {
		List<HeroPropertyInfo> list = new ArrayList<HeroPropertyInfo>();
		if (stoneMap == null) {
			return null;
		}

		for (int stoneNo : stoneMap.values()) {
			list.add(getStoneProperty(stoneNo));
		}
		return HeroProService.generateHeroTotalProtperty(list);
	}

	/**
	 * 获取宝石加成属性
	 * @param stoneNo
	 * @return
	 */
	public static HeroPropertyInfo getStoneProperty(int stoneNo) {
		HeroPropertyInfo info = new HeroPropertyInfo();
		PropXMLInfo propXmlinfo = PropXMLInfoMap.getPropXMLInfo(stoneNo);

		if (propXmlinfo == null) {
			return info;
		}
		PropAttriAddXml addXMl = PropAttriAddXmlMap.getXmlInfo(Integer.valueOf(propXmlinfo.getUseParam()));
		if (addXMl != null && addXMl.getProType() != null) {
			info.addValue(addXMl.getProType(), addXMl.getAddNum());
		}
		return info;
	}

	/**
	 * 返回英雄装备
	 * @param equipInfo
	 * @return
	 */
	public static List<HeroEquipDetailRe> getHeroEquip(HeroInfo heroInfo, Map<Integer, EquipInfo> equipInfoMap) {
		List<HeroEquipDetailRe> list = new ArrayList<HeroEquipDetailRe>();

		if (equipInfoMap != null && equipInfoMap.size() > 0) {
			for (EquipInfo info : equipInfoMap.values()) {
				HeroEquipDetailRe heroEquip = new HeroEquipDetailRe();
				heroEquip.setEquipId((int) info.getId());
				EquipInfoRe equipInfoRe = getEquipInfoRe((int) heroInfo.getId(), info);
				heroEquip.setEquipInfo(equipInfoRe);
				list.add(heroEquip);
			}
		}
		return list;
	}

	public static List<EquipDetailRe> getBagEquipList(RoleInfo roleInfo, String idStr) {
		List<EquipDetailRe> list = new ArrayList<EquipDetailRe>();
		if (idStr != null && idStr.trim().length() > 0) {
			String[] ids = idStr.split(",");// 获取的装备id
			for (String id : ids) {
				int equipId = Integer.valueOf(id.trim());
				EquipInfo equipInfo = EquipInfoMap.getBagEquip(roleInfo.getId(), equipId);
				if (equipInfo == null) {
					continue;
				}
				list.add(getEquipDetailRe(0, equipInfo, 1));
			}
		} else {
			Map<Integer, EquipInfo> equipMap = EquipInfoMap.getBagEquipMap(roleInfo.getId());
			if (equipMap != null) {
				for (EquipInfo equipInfo : equipMap.values()) {
					list.add(getEquipDetailRe(0, equipInfo, 1));
				}
			}
		}
		return list;
	}

	public static List<EquipDetailRe> getHeroEquipList(HeroInfo heroInfo, String idStr) {
		List<EquipDetailRe> list = new ArrayList<EquipDetailRe>();
		if (idStr != null && idStr.trim().length() > 0) {
			String[] ids = idStr.split(",");// 获取的英雄id
			for (String id : ids) {
				int equipId = Integer.valueOf(id.trim());
				EquipInfo equipInfo = EquipInfoMap.getHeroEquip(heroInfo, equipId);
				if (equipInfo == null) {
					continue;
				}
				list.add(getEquipDetailRe((int) heroInfo.getId(), equipInfo, 1));
			}
		} else {
			Map<Integer, EquipInfo> heroMap = EquipInfoMap.getHeroEquipMap(heroInfo);
			if (heroMap != null) {
				for (EquipInfo equipInfo : heroMap.values()) {
					list.add(getEquipDetailRe((int) heroInfo.getId(), equipInfo, 1));
				}
			}
		}
		return list;
	}

	public static EquipDetailRe getEquipDetailRe(int heroId, EquipInfo info, int num) {
		EquipDetailRe re = new EquipDetailRe();
		re.setEquipId((int) info.getId());
		re.setEquipNum((short) num);
		re.setEquipInfo(getEquipInfoRe(heroId, info));
		return re;
	}

	public static EquipInfoRe getEquipInfoRe(int heroId, EquipInfo info) {
		EquipInfoRe re = new EquipInfoRe();
		re.setEquipId((int) info.getId());
		re.setHeroId(heroId);
		re.setEquipNo(info.getEquipNo());
		re.setEquipType((byte) info.getEquipType());
		re.setLevel((byte) info.getLevel());
		re.setExp(info.getExp());
		re.setFightValue(info.getFightValue());
		re.setRefineLv(info.getRefineLv());
		re.setEnchantLv(info.getEnchantLv());
		re.setEnchantExp(info.getEnchantExp());
		
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
	 * 装备额外属性加成
	 * @return
	 */
	private static void addEquipExtraVal(HeroPropertyInfo pro, Map<HeroProType, Double> rate, EquipXMLInfo equipXMLInfo) {
		if (pro == null) {
			return;
		}

		List<EquipExtraInfo> list = equipXMLInfo.getExtraList();
		if (list != null && list.size() > 0) {
			for (EquipExtraInfo info : list) {
				if (info.getRefineType() == null) {
					continue;
				}
				if (info.isAddRate()) {
					if (rate != null) {
						Double val = rate.get(info.getRefineType());
						if (val == null) {
							rate.put(info.getRefineType(), (double) info.getEffect());
						} else {
							rate.put(info.getRefineType(), val + info.getEffect());
						}
					}
				} else {
					pro.addValue(info.getRefineType(), info.getEffect());
				}
			}
		}
	}

	/**
	 * 装备强化等级加成
	 * @return
	 */
	private static void upgradePlusCheck(HeroPropertyInfo pro, Map<HeroProType, Double> rate,
			EquipXMLInfo equipXMLInfo, int equipLevel) {
		if (pro == null) {
			return;
		}

		if (equipLevel <= 0) {
			return;
		}

		List<EquipStrengthenInfo> list = equipXMLInfo.getStrengthenList();
		if (list != null && list.size() > 0) {
			for (EquipStrengthenInfo info : list) {
				if (equipLevel >= info.getLevel()) {
					if (info.getRefineType() == null) {
						continue;
					}
					if (info.isAddRate()) {
						if (rate != null) {
							Double val = rate.get(info.getRefineType());
							if (val == null) {
								rate.put(info.getRefineType(), Double.valueOf(info.getEffect()));
							} else {
								rate.put(info.getRefineType(), val + Double.valueOf(info.getEffect()));
							}
						}
					} else {
						pro.addValue(info.getRefineType(), info.getEffect());
					}
				}
			}
		}
	}

	/**
	 * 装备品质加成（配置文件配好的Equip里的Character与Type决定）
	 * @param pro
	 * @param equipXMLInfo
	 * @param equipLevel
	 */
	private static void equipQuailtyAdd(HeroPropertyInfo pro, EquipXMLInfo equipXMLInfo, int equipLevel) {
		if (equipLevel <= 0) {
			return;
		}

		Map<Integer, EquipEffectConfigInfo> equipEffectConfigInfoMap = EquipXMLInfoMap
				.getEquipEffectConfigInfoMap(equipXMLInfo.getCharacter());

		if (equipEffectConfigInfoMap == null) {
			logger.info("EquipInfo EquipId:" + equipXMLInfo.getNo() + " level:" + equipLevel
					+ "EquipEffectConfigInfo Character " + equipXMLInfo.getCharacter() + " not exit");
			return;
		}

		EquipEffectConfigInfo equipEffectConfigInfo = equipEffectConfigInfoMap.get(equipXMLInfo.getEquipType());
		if (equipEffectConfigInfo == null) {
			logger.info("EquipInfo EquipId:" + equipXMLInfo.getNo() + " level:" + equipLevel
					+ "EquipEffectConfigInfo Type " + equipXMLInfo.getEquipType() + " not exit");
			return;
		}

		if (pro.getAttack() > 0) { // 大于0则强化增加属性
			pro.setAttack(pro.getAttack() + equipEffectConfigInfo.getAttack() * equipLevel);
		}

		if (pro.getMagicAttack() > 0) { // 大于0则强化增加属性
			pro.setMagicAttack(pro.getMagicAttack() + equipEffectConfigInfo.getMagicAttack() * equipLevel);
		}

		if (pro.getHp() > 0) { // 大于0则强化增加属性
			pro.setHp(pro.getHp() + equipEffectConfigInfo.getHp() * equipLevel);
		}

		if (pro.getAttackDef() > 0) { // 大于0则强化增加属性
			pro.setAttackDef(pro.getAttackDef() + equipEffectConfigInfo.getAttackDef() * equipLevel);
		}

		if (pro.getMagicDef() > 0) { // 大于0则强化增加属性
			pro.setMagicDef(pro.getMagicDef() + equipEffectConfigInfo.getMagicDef() * equipLevel);
		}

		if (pro.getAd() > 0) { // 大于0则强化增加属性
			pro.setAd(pro.getAd() + equipEffectConfigInfo.getAd() * equipLevel);
		}
	}

	/**
	 * 精炼等级加成
	 * @param pro
	 * @param equipXMLInfo
	 * @param equipLevel
	 */
	private static void refinePlus(HeroPropertyInfo pro, Map<HeroProType, Double> rate, EquipXMLInfo equipXMLInfo,
			int refineLevel) {
		if (refineLevel <= 0) {
			return;
		}
		Map<Integer, EquipRefineInfo> refineMap = equipXMLInfo.getRefineMap();

		EquipRefineInfo refineInfo = null;
		for (int i = 1; i <= refineLevel; i++) {
			refineInfo = refineMap.get(i);
			if (refineInfo == null) {
				continue;
			}

			pro.setHp(pro.getHp() + refineInfo.getHp());
			pro.setAd(pro.getAd() + refineInfo.getAd());
			pro.setAttack(pro.getAttack() + refineInfo.getAttack());
			pro.setMagicAttack(pro.getMagicAttack() + refineInfo.getMagicAttack());
			pro.setAttackDef(pro.getAttackDef() + refineInfo.getAttackDef());
			pro.setMagicDef(pro.getMagicDef() + refineInfo.getMagicDef());

			if (refineInfo.getRefineType() != null) {
				if (refineInfo.isAddRate()) {
					if (rate != null) {
						Double val = rate.get(refineInfo.getRefineType());
						if (val == null) {
							rate.put(refineInfo.getRefineType(), Double.valueOf(refineInfo.getEffect()));
						} else {
							rate.put(refineInfo.getRefineType(), val + Double.valueOf(refineInfo.getEffect()));
						}
					}
				} else {
					pro.addValue(refineInfo.getRefineType(), refineInfo.getEffect());
				}
			}
		}
	}

	/**
	 * 装备附魔加成
	 * @param pro
	 * @param equipXMLInfo
	 * @param enchantLevel
	 */
	private static void equipEnchantAdd(HeroPropertyInfo pro,EquipXMLInfo equipXMLInfo, int enchantLevel){
		if (enchantLevel <= 0) {
			return;
		}
		Map<HeroProType, Float> proMap = equipXMLInfo.getEnchantMap().get(enchantLevel);
		if(proMap != null){
			for(HeroProType proType:proMap.keySet()){
				pro.addValue(proType, proMap.get(proType));
			}
		}
	}
}
