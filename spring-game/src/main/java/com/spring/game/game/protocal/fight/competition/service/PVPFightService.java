package com.snail.webgame.game.protocal.fight.competition.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.soldier.service.SoldierService;
import com.snail.webgame.game.pvp.competition.request.SkillInfoVo;
import com.snail.webgame.game.pvp.competition.request.WarriorVo;

public class PVPFightService {

	/**
	 * 获取pvp 战斗数据
	 * @param fightType
	 * @param roleInfo
	 * @return
	 */
	public static List<WarriorVo> getFightWarriorVoList(FightType fightType, RoleInfo roleInfo) {		
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightType);
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightType);		
		List<WarriorVo> result = null;
		switch (fightType) {
		case FIGHT_TYPE_15:
			// 只取主武将
			result = new ArrayList<WarriorVo>();
			result.add(getWarriorVo(getFightWarriorVoList(roleInfo, mainRate, otherRate), HeroInfo.DEPLOY_TYPE_MAIN));
			break;
		default:
			result = getFightWarriorVoList(roleInfo, mainRate, otherRate);
			break;
		}
		return result;
	}

	/**
	 * 获取pvp 战斗数据
	 * @param 战斗类型 1.跨服 2.地图 3.组队对攻
	 * @param roleInfo
	 * @return
	 */
	private static List<WarriorVo> getFightWarriorVoList(RoleInfo roleInfo, Map<HeroProType, Double> mainRate,
			Map<HeroProType, Double> otherRate) {
		List<WarriorVo> result = new ArrayList<WarriorVo>();
		List<HeroInfo> heroInfoList = HeroInfoMap.getFightDeployHero(roleInfo.getId());
		if (heroInfoList != null) {
			WarriorVo vo = null;
			for (HeroInfo heroInfo : heroInfoList) {
				vo = getWarriorVo(roleInfo, heroInfo, mainRate, otherRate);
				if (vo != null) {
					result.add(vo);
				}
			}
			// 1号副将,2号副将的第一技能给主将
			changeArmyFightDataSkill(result);
		}
		return result;
	}

	/**
	 * 获取vo
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
	private static WarriorVo getWarriorVo(RoleInfo roleInfo, HeroInfo heroInfo, Map<HeroProType, Double> mainRate,
			Map<HeroProType, Double> otherRate) {
		byte deployStatus = heroInfo.getDeployStatus();
		int group = FightService.getFightArmyGroup(deployStatus);
		if (group == 0) { // 主英雄
			return null;
		}
		HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
		if (heroXmlInfo == null) {
			return null;
		}
		WarriorVo vo = WarriorVo.transformFromHero(heroInfo, heroXmlInfo.getHeroType(), group, mainRate, otherRate);
		if (vo == null) {
			return null;
		}
		int soldierLevel = SoldierService.getSoldierLevel(roleInfo, (byte) heroXmlInfo.getHeroType());
		vo.setEmilitLevel(soldierLevel);
		List<SkillInfoVo> heroSkillList = new ArrayList<SkillInfoVo>();
		Map<Integer, Integer> skillMap = HeroService.getSkillMap(heroInfo);
		if (deployStatus == HeroInfo.DEPLOY_TYPE_MAIN) {
			if (skillMap != null) {
				for (Integer skillNo : skillMap.keySet()) {
					int skillLevel = skillMap.get(skillNo);
					HeroXMLSkill xmlSkill = heroXmlInfo.getSkillMap().get(skillNo);
					if (xmlSkill != null) {
						heroSkillList.add(new SkillInfoVo(heroInfo.getId(), skillNo, skillLevel, (byte) xmlSkill
								.getSkillPos(), xmlSkill.getAi(), xmlSkill.getAiOrder()));
					}
				}
			}
			// 主武将添加士兵技能
			FightService.addSoldierSkill(roleInfo, heroInfo, heroXmlInfo, heroSkillList);
			// 主武将添加神兵套装技能
			FightService.addMagicSkill(roleInfo, heroSkillList, heroInfo);
		} else {
			boolean canControl = true;
			vo.setSkillControl(canControl == true ? (byte) 1 : (byte) 0);
			if (skillMap != null) {
				for (Integer skillNo : skillMap.keySet()) {
					int skillLevel = skillMap.get(skillNo);
					HeroXMLSkill xmlSkill = heroXmlInfo.getSkillMap().get(skillNo);
					int position = xmlSkill != null ? xmlSkill.getSkillPos() : 0;
					int ai = xmlSkill != null ? xmlSkill.getAi() : 0;
					byte aiOrder = xmlSkill != null ? xmlSkill.getAiOrder() : 0;
					if (canControl == true) {
						if (position == 1) { // 1号位技能
							SkillInfoVo skillVo = null;
							if (deployStatus == 2 || deployStatus == 4) {
								skillVo = new SkillInfoVo(heroInfo.getId(), skillNo, skillLevel, (byte) 5, ai, aiOrder);
							} else if (deployStatus == 3 || deployStatus == 5) {
								skillVo = new SkillInfoVo(heroInfo.getId(), skillNo, skillLevel, (byte) 6, ai, aiOrder);
							}
							heroSkillList.add(skillVo);
						} else {
							heroSkillList.add(new SkillInfoVo(heroInfo.getId(), skillNo, skillLevel, (byte) position, ai, aiOrder));
						}
					} else {
						heroSkillList.add(new SkillInfoVo(heroInfo.getId(), skillNo, skillLevel, (byte) position, ai, aiOrder));
					}
				}
			}
			// 添加士兵技能
			FightService.addSoldierSkill(roleInfo, heroInfo, heroXmlInfo, heroSkillList);
		}
		vo.setSkillCount((byte) heroSkillList.size());
		vo.setSkillList(heroSkillList);
		return vo;
	}

	/**
	 * 1号副将,2号副将的第一技能给主将
	 * @param sideDataList
	 */
	public static void changeArmyFightDataSkill(List<WarriorVo> armyList) {
		Map<Byte, SkillInfoVo> addSkill = new HashMap<Byte, SkillInfoVo>();
		WarriorVo armyData = null;
		SkillInfoVo skillDataAdd = null;
		for (byte deployStatus = 2; deployStatus <= 5; deployStatus++) {
			armyData = getWarriorVo(armyList, deployStatus);
			if (armyData == null) {
				continue;
			}
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(armyData.getHeroNo());
			if (heroXMLInfo == null) {
				continue;
			}
			// 武将1号位技能
			skillDataAdd = getSkillInfoVo(heroXMLInfo, armyData.getSkillList(), 1);
			if (skillDataAdd != null) {
				addSkill.put(deployStatus, skillDataAdd);
				// 副将去除1号位技能
				armyData.getSkillList().remove(skillDataAdd);
				armyData.setSkillCount((byte) armyData.getSkillList().size());
			}
		}
		WarriorVo armyMainData = getWarriorVo(armyList, HeroInfo.DEPLOY_TYPE_MAIN);
		if (armyMainData == null) {
			return;
		}
		for (byte deployStatus : addSkill.keySet()) {
			skillDataAdd = addSkill.get(deployStatus);
			if (skillDataAdd == null || skillDataAdd.getLevel() <= 0) {
				continue;
			}
			// 主将添加副将1号位技能
			switch (deployStatus) {
			case 2:
			case 4:
				armyMainData.getSkillList().add(
						new SkillInfoVo(skillDataAdd.getHeroId(), skillDataAdd.getSkillNo(), skillDataAdd.getLevel(),
								(byte) 5, skillDataAdd.getAi(), (byte) 5));

				break;
			case 3:
			case 5:
				armyMainData.getSkillList().add(
						new SkillInfoVo(skillDataAdd.getHeroId(), skillDataAdd.getSkillNo(), skillDataAdd.getLevel(),
								(byte) 6, skillDataAdd.getAi(), (byte) 6));
				break;
			default:
				break;
			}
		}
		armyMainData.setSkillCount((byte) armyMainData.getSkillList().size());

	}

	private static WarriorVo getWarriorVo(List<WarriorVo> armyList, byte deployStatus) {
		for (WarriorVo armyData : armyList) {
			if (armyData != null && armyData.getDeployStatus() == deployStatus) {
				return armyData;
			}
		}
		return null;
	}

	private static SkillInfoVo getSkillInfoVo(HeroXMLInfo heroXMLInfo, List<SkillInfoVo> skillList, int skillPos) {
		for (SkillInfoVo skillData : skillList) {
			HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(skillData.getSkillNo());
			HeroXMLSkill heroSkillXml = HeroXMLInfoMap.getHeroSkillXML(heroXMLInfo.getNo(), skillData.getSkillNo());
			if (heroSkillXml != null && xmlSkill != null && xmlSkill.getSkillPos() == skillPos) {
				return skillData;
			}
		}
		return null;
	}
}
