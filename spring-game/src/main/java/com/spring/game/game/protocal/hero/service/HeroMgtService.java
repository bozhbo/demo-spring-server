package com.snail.webgame.game.protocal.hero.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCost;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCostItem;
import com.snail.webgame.game.common.xml.info.HeroLevelXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroStarXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.condtion.conds.TechCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.EquipDAO;
import com.snail.webgame.game.dao.HeroDAO;
import com.snail.webgame.game.dao.SkillDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.hero.colorUp.HeroColorUpReq;
import com.snail.webgame.game.protocal.hero.colorUp.HeroColorUpResp;
import com.snail.webgame.game.protocal.hero.fightValue.FightValueResp;
import com.snail.webgame.game.protocal.hero.merge.MergeHeroReq;
import com.snail.webgame.game.protocal.hero.merge.MergeHeroResp;
import com.snail.webgame.game.protocal.hero.propUse.HeroPropUseReq;
import com.snail.webgame.game.protocal.hero.propUse.HeroPropUseResp;
import com.snail.webgame.game.protocal.hero.query.HeroDetailRe;
import com.snail.webgame.game.protocal.hero.query.QueryHeroReq;
import com.snail.webgame.game.protocal.hero.query.QueryHeroResp;
import com.snail.webgame.game.protocal.hero.recruitList.CanRecruitHeroComparator;
import com.snail.webgame.game.protocal.hero.recruitList.DisRecruitHeroComparator;
import com.snail.webgame.game.protocal.hero.recruitList.QueryRecruitResp;
import com.snail.webgame.game.protocal.hero.recruitList.QueryRecuitHeroRe;
import com.snail.webgame.game.protocal.hero.skillUp.HeroSkillUpReq;
import com.snail.webgame.game.protocal.hero.skillUp.HeroSkillUpResp;
import com.snail.webgame.game.protocal.hero.starUp.HeroStarUpReq;
import com.snail.webgame.game.protocal.hero.starUp.HeroStarUpResp;
import com.snail.webgame.game.protocal.hero.upgrade.HeroUpgradeReq;
import com.snail.webgame.game.protocal.hero.upgrade.HeroUpgradeResp;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.PropXMLInfo;

public class HeroMgtService {

	private HeroDAO heroDAO = HeroDAO.getInstance();
	private SkillDAO skillDAO = SkillDAO.getInstance();
	private CanRecruitHeroComparator canRecruitHeroComparator = new CanRecruitHeroComparator();
	private DisRecruitHeroComparator disRecruitHeroComparator = new DisRecruitHeroComparator();

	/**
	 * 查询武将信息
	 * @param roleId
	 * @param idStr
	 * @return
	 */
	public QueryHeroResp queryHero(int roleId, QueryHeroReq req) {
		QueryHeroResp resp = new QueryHeroResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.HERO_QUERY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			String idStr = req.getIdStr();
			List<HeroDetailRe> list = HeroService.getHeroDetailList(roleInfo, idStr);
			resp.setResult(1);
			resp.setIdStr(idStr);
			resp.setCount(list.size());
			resp.setList(list);
			return resp;
		}
	}

	/**
	 * 武将碎片合成武将
	 * @param roleId
	 * @param req
	 * @return
	 */
	public MergeHeroResp mergeHero(int roleId, MergeHeroReq req) {
		MergeHeroResp resp = new MergeHeroResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.HERO_MERGE_ERROR_1);
			return resp;
		}
		int heroNo = req.getHeroNo();
		if (heroNo <= 0) {
			resp.setResult(ErrorCode.HERO_MERGE_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
			if (heroXMLInfo == null) {
				resp.setResult(ErrorCode.HERO_MERGE_ERROR_3);
				return resp;
			}
			HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleId, heroNo);
			if (heroInfo != null) {
				resp.setResult(ErrorCode.HERO_MERGE_ERROR_4);
				return resp;
			}
			int chipNo = heroXMLInfo.getChipNo();
			int chipNum = heroXMLInfo.getChipNum();
			PropXMLInfo propXml = PropXMLInfoMap.getPropXMLInfo(chipNo);
			if (propXml == null) {
				resp.setResult(ErrorCode.HERO_MERGE_ERROR_5);
				return resp;
			}
			if (!BagItemMap.checkBagItemNum(roleInfo, chipNo, chipNum)) {
				resp.setResult(ErrorCode.HERO_MERGE_ERROR_6);
				return resp;
			}

			Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
			itemMap.put(chipNo, chipNum);
			int result = ItemService.bagItemDel(ActionType.action74.getType(), roleInfo, itemMap);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			List<HeroInfo> heros = new ArrayList<HeroInfo>();
			heroInfo = HeroService.initNewHeroInfo(roleInfo, heroXMLInfo, HeroInfo.DEPLOY_TYPE_COMM);
			heros.add(heroInfo);
			if (heroDAO.insertHeros(heros)) {
				HeroInfoMap.addHeroInfo(heroInfo, false);
			} else {
				resp.setResult(ErrorCode.HERO_MERGE_ERROR_8);
				return resp;
			}

			// 任务
			boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action74.getType(), null, true, false);
			// 红点监听武将变动
			boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
					GameValue.RED_POINT_TYPE_HERO);
			// 推送
			if (isRedQuest || isRed) {
				RedPointMgtService.pop(roleInfo.getId());
			}

			resp.setResult(1);
			resp.setHeroDetail(HeroService.getHeroDetailRe(heroInfo));
			GameLogService.insertHeroUpLog(roleInfo,heroNo, ActionType.action74.getType(), 0, 0,1);
			return resp;
		}
	}

	/**
	 * 武将升级
	 * @param roleId
	 * @param req
	 * @return
	 */
	public HeroUpgradeResp heroUpgrade(int roleId, HeroUpgradeReq req) {
		HeroUpgradeResp resp = new HeroUpgradeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.HERO_UP_ERROR_1);
			return resp;
		}
		int heroId = req.getHeroId();
		int num = req.getNum();
		if (heroId <= 0 || num <= 0) {
			resp.setResult(ErrorCode.HERO_UP_ERROR_2);
			return resp;
		}
		synchronized (roleInfo) {

			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_3);
				return resp;
			}
			if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_4);
				return resp;
			}
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXMLInfo == null) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_5);
				return resp;
			}
			int heroLevel = heroInfo.getHeroLevel();
			int currHeroLevel = heroInfo.getHeroLevel() + num;
			HashMap<Integer, HeroLevelXMLUpgrade> heroLvMap = HeroXMLInfoMap.getOtherLvMap();
			if (heroLvMap == null) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_6);
				return resp;
			}
			HeroLevelXMLUpgrade upgrade = heroLvMap.get(currHeroLevel);
			if (upgrade == null) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_7);
				return resp;
			}
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_8);
				return resp;
			}
			if (mainHero.getHeroLevel() < upgrade.getLevel()) {
				resp.setResult(ErrorCode.HERO_UP_ERROR_9);
				return resp;
			}

			int needExp = 0;
			Map<Integer, Integer> needProp = new HashMap<Integer, Integer>();
			for (int i = heroLevel + 1; i <= currHeroLevel; i++) {
				upgrade = heroLvMap.get(i);
				if (upgrade == null) {
					resp.setResult(ErrorCode.HERO_UP_ERROR_10);
					return resp;
				}
				needExp += upgrade.getExp();
				if (upgrade.getPropNo() > 0 && upgrade.getPropNum() > 0) {
					if (needProp.containsKey(upgrade.getPropNo())) {
						needProp.put(upgrade.getPropNo(), upgrade.getPropNum() + needProp.get(upgrade.getPropNo()));
					} else {
						needProp.put(upgrade.getPropNo(), upgrade.getPropNum());
					}
				}
			}
			if (needProp.size() > 0) {
				for (int propNo : needProp.keySet()) {
					if (!BagItemMap.checkBagItemNum(roleInfo, propNo, needProp.get(propNo))) {
						resp.setResult(ErrorCode.HERO_UP_ERROR_11);
						return resp;
					}
				}
			}

			int currHeroExp = heroInfo.getHeroExp();
			// if (heroInfo.getHeroExp() >= needExp) {
			// currHeroExp = heroInfo.getHeroExp() - needExp;
			// } else {
			// currHeroExp = 0;
			int costMoney = needExp;// (needExp - heroInfo.getHeroExp()) *
									// GameValue.HERO_EXP_ZH_MONEY_RATE;
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new MoneyCond(costMoney));
			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}
			if (RoleService.subRoleResource(ActionType.action72.getType(), roleInfo, conds , null)) {
				String updateSourceStr = RoleService.returnResourceChange(conds);
				if (updateSourceStr != null) {
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1) {
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}

				// SceneService.sendRoleRefreshMsg(roleInfo.getId(),
				// SceneService.REFESH_TYPE_ROLE, null);
			} else {
				resp.setResult(ErrorCode.HERO_UP_ERROR_12);
				return resp;
			}

			if (needProp.size() > 0) {
				int result = ItemService.bagItemDel(ActionType.action72.getType(), roleInfo, needProp);
				if (result != 1) {
					resp.setResult(ErrorCode.HERO_UP_ERROR_11);
					return resp;
				}
			}
			// }

			if (heroDAO.updateHeroLv(heroInfo.getId(), currHeroLevel, currHeroExp)) {
				heroInfo.setHeroLevel(currHeroLevel);
				heroInfo.setHeroExp(currHeroExp);
			} else {
				resp.setResult(ErrorCode.HERO_UP_ERROR_13);
				return resp;
			}

			// 判断新手引导是否要更新(要升到2级才检测)
			if (heroInfo.getHeroLevel() >= 2 && heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN){
				int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_UPGRADE_HERO);
				if(ck != 1){
					resp.setResult(ck);
					return resp;
				}
			}

			// 任务
			boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action72.getType(), null, true, false);
			// 红点监听武将变动
			boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
					RedPointMgtService.LISTENING_OTHER_HERO_UPGRADE_TYPES);
			// 推送红点
			if (isRedQuest || isRed) {
				RedPointMgtService.pop(roleInfo.getId());
			}

			// 刷新英雄属性 战斗力
			HeroService.refeshHeroProperty(roleInfo, heroInfo);
			resp.setResult(1);
			resp.setHeroInfo(HeroService.getHeroInfoRe(heroInfo));


			GameLogService.insertHeroUpLog(roleInfo,heroInfo.getHeroNo(), ActionType.action72.getType(), 1, heroLevel,
					currHeroLevel);
			return resp;
		}
	}

	/**
	 * 武将觉醒
	 * @param roleId
	 * @param req
	 * @return
	 */
	public HeroColorUpResp heroColorUp(int roleId, HeroColorUpReq req) {
		HeroColorUpResp resp = new HeroColorUpResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.HERO_COLOR_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			int heroId = req.getHeroId();
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.HERO_COLOR_ERROR_2);
				return resp;
			}

			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXMLInfo == null) {
				resp.setResult(ErrorCode.HERO_COLOR_ERROR_3);
				return resp;
			}
			if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
				resp.setResult(ErrorCode.HERO_COLOR_ERROR_10);
				return resp;
			}

			// 英雄是否进阶至顶级 英雄已进阶至顶级
			int currQuality = heroInfo.getQuality();
			int nextQuality = currQuality + 1;

			HeroColorXMLUpCost currColorXml = HeroXMLInfoMap.getHeroColorXMLUpCost(currQuality);
			if (currColorXml == null) {
				resp.setResult(ErrorCode.HERO_COLOR_ERROR_5);
				return resp;
			}
			HeroColorXMLUpCost nextColorXml = HeroXMLInfoMap.getHeroColorXMLUpCost(nextQuality);
			if (nextColorXml == null) {
				resp.setResult(ErrorCode.HERO_COLOR_ERROR_4);
				return resp;
			}
			if (nextColorXml.getLevelLimit() > heroInfo.getHeroLevel()) {
				resp.setResult(ErrorCode.HERO_COLOR_ERROR_9);
				return resp;
			}

			List<Integer> delEquipIds = new ArrayList<Integer>();
			Map<Integer, HeroColorXMLUpCostItem> costEquipMap = currColorXml.getItemMap(heroXMLInfo.getAwakenType());
			if (costEquipMap != null) {
				for (int equipType : costEquipMap.keySet()) {
					EquipInfo equipInfo = EquipInfoMap.getHeroEquipbyType(heroInfo, equipType);
					if (equipInfo == null) {
						resp.setResult(ErrorCode.HERO_COLOR_ERROR_6);
						return resp;
					}
					delEquipIds.add(equipInfo.getId());
				}
			}

			// 扣除装备
			if (EquipDAO.getInstance().deleteEquip(delEquipIds)) {
				for (int equipId : delEquipIds) {
					EquipInfoMap.removeHeroEquip(heroInfo, equipId);
				}
			} else {
				resp.setResult(ErrorCode.HERO_COLOR_ERROR_8);
				return resp;
			}

			if (heroDAO.updateHeroQuality(heroInfo.getId(), nextQuality)) {
				heroInfo.setQuality(nextQuality);
			} else {
				resp.setResult(ErrorCode.HERO_COLOR_ERROR_8);
				return resp;
			}

			// 刷新英雄基本信息
			HeroService.refeshHeroProperty(roleInfo, heroInfo);

			// 任务
			boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action76.getType(), null, true, false);
			// 红点监听
			boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
					RedPointMgtService.LISTENING_MONEY_CHANGE_TYPES);
			// 推送红点
			if (isRedQuest || isRed) {
				RedPointMgtService.pop(roleInfo.getId());
			}

			// 判断新手引导是否要更新
			int ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_AWAKE_HERO);
			if(ck != 1){
				resp.setResult(ck);
				return resp;
			}

			// 日志
			GameLogService.insertHeroUpLog(roleInfo,heroInfo.getHeroNo(), ActionType.action76.getType(), 3, currQuality,
					nextQuality);

			resp.setResult(1);
			resp.setHeroInfo(HeroService.getHeroInfoRe(heroInfo));

			return resp;
		}
	}

	/**
	 * 武将升星
	 * @param roleId
	 * @param req
	 * @return
	 */
	public HeroStarUpResp heroStarUp(int roleId, HeroStarUpReq req) {
		HeroStarUpResp resp = new HeroStarUpResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.HERO_STAR_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			int heroId = req.getHeroId();
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.HERO_STAR_ERROR_2);
				return resp;
			}
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXMLInfo == null) {
				resp.setResult(ErrorCode.HERO_STAR_ERROR_3);
				return resp;
			}
			int star = heroInfo.getStar();
			HeroStarXMLUpgrade currStarXML = heroXMLInfo.getStarMap().get(star);
			if (currStarXML == null) {
				resp.setResult(ErrorCode.HERO_STAR_ERROR_4);
				return resp;
			}
			int nextStar = star + 1;
			HeroStarXMLUpgrade nextStarXML = heroXMLInfo.getStarMap().get(nextStar);
			if (nextStarXML == null) {
				resp.setResult(ErrorCode.HERO_STAR_ERROR_7);
				return resp;
			}
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new MoneyCond(nextStarXML.getNeedSilver()));
			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}

			if (!BagItemMap.checkBagItemNum(roleInfo, heroXMLInfo.getChipNo(), nextStarXML.getChipNum())) {
				resp.setResult(ErrorCode.HERO_STAR_ERROR_6);
				return resp;
			}

			if (RoleService.subRoleResource(ActionType.action73.getType(), roleInfo, conds , null)) {
				String updateSourceStr = RoleService.returnResourceChange(conds);
				if (updateSourceStr != null) {
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1) {
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}
			} else {
				resp.setResult(ErrorCode.HERO_STAR_ERROR_8);
				return resp;
			}

			HashMap<Integer, Integer> delMap = new HashMap<Integer, Integer>();
			delMap.put(heroXMLInfo.getChipNo(), nextStarXML.getChipNum());
			int result = ItemService.bagItemDel(ActionType.action73.getType(), roleInfo, delMap);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			if (heroDAO.updateHeroStar(heroId, nextStar)) {
				heroInfo.setStar(nextStar);
			} else {
				resp.setResult(ErrorCode.HERO_STAR_ERROR_8);
				return resp;
			}
			// 刷新
			HeroService.refeshHeroProperty(roleInfo, heroInfo);
			GameLogService.insertHeroUpLog(roleInfo,heroInfo.getHeroNo(), ActionType.action73.getType(), 2, star,
					nextStar);
			
			QuestService.checkQuest(roleInfo, ActionType.action73.getType(), null, true, false);

			resp.setResult(1);
			resp.setHeroInfo(HeroService.getHeroInfoRe(heroInfo));
			return resp;
		}
	}

	/**
	 * 武将技能升级
	 * @param roleId
	 * @param req
	 * @return
	 */
	public HeroSkillUpResp heroSkillUp(int roleId, HeroSkillUpReq req) {
		HeroSkillUpResp resp = new HeroSkillUpResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			int heroId = req.getHeroId();
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_2);
				return resp;
			}
			int skillNo = req.getSkillNo();
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXMLInfo == null) {
				resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_3);
				return resp;
			}
			HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(skillNo);
			if (xmlSkill == null) {
				resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_4);
				return resp;
			}
			int skillPos = xmlSkill.getSkillPos();
			HeroSkillXMLInfo skillXml = HeroXMLInfoMap.getHeroSkillXMLInfo(xmlSkill.getSkillPos());
			if (skillXml == null) {
				resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_5);
				return resp;
			}
			int currSkillLevel = HeroService.getSkillLv(heroInfo, skillNo);
			if (currSkillLevel == 0) {
				if (heroXMLInfo.getInitial() == 0) {
					if (heroInfo.getQuality() < skillXml.getOtherColorOpen()) {
						resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_6);
						return resp;
					}
				} else {
					if (heroInfo.getHeroLevel() < skillXml.getMainLvOpen()) {
						resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_7);
						return resp;
					}
				}
			}
			int skillLevel = 0;
			int costMoney = 0;
			switch (req.getUpType()) {
			case 0:// 升1级
				skillLevel = currSkillLevel + 1;
				HeroSkillXMLUpgrade skillUpXML = skillXml.getUpMap().get(skillLevel);
				if (skillUpXML == null) {
					resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_8);
					return resp;
				}
				if (heroInfo.getHeroLevel() < skillUpXML.getHeroLevel()) {
					resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_9);
					return resp;
				}
				if (heroXMLInfo.getInitial() == 0) {
					costMoney = skillUpXML.getNeedOtherSilver();
				} else {
					costMoney = skillUpXML.getNeedMainSilver();
				}
				break;
			case 1:// 升max
				skillLevel = currSkillLevel;
				int tech = roleInfo.getTech();
				if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
					// 技能点自动回复
					RoleService.timerRecoverTech(roleInfo);
					tech = roleInfo.getTech();
				}
				HeroSkillXMLUpgrade upgrade = null;
				while (true) {
					if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
						if (tech <= 0) {
							break;
						}
					}
					upgrade = skillXml.getUpMap().get(skillLevel + 1);
					if (upgrade == null) {
						break;
					}
					if (upgrade.getHeroLevel() > heroInfo.getHeroLevel()) {
						break;
					}
					int add = 0;
					// 1:初始武将 0:非初始武将
					if (heroXMLInfo.getInitial() == 0) {
						add = upgrade.getNeedOtherSilver();
					} else {
						add = upgrade.getNeedMainSilver();
					}
					if (costMoney + add > roleInfo.getMoney()) {
						break;
					}
					costMoney += add;
					skillLevel++;
					if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
						tech--;
					}
				}
				if (skillLevel <= currSkillLevel) {
					resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_13);
					return resp;
				}
				break;
			default:
				resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_15);
				return resp;
			}

			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new MoneyCond(costMoney));
			if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
				conds.add(new TechCond(skillLevel - currSkillLevel));
			}
			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}

			int gameAction = ActionType.action90.getType();
			if (heroInfo.getDeployStatus() != 1) {
				gameAction = ActionType.action75.getType();
			}
			if (RoleService.subRoleResource(gameAction, roleInfo, conds , null)) {
				resp.setSourceType((byte) ConditionType.TYPE_MONEY.getType());
				resp.setSourceChange(-costMoney);
			} else {
				resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_10);
				return resp;
			}
			String skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, skillLevel);
			if (skillDAO.addOrUpdateHeroSkill(heroId, skillStr)) {
				heroInfo.setSkillStr(skillStr);
			} else {
				resp.setResult(ErrorCode.HERO_SKILL_UP_ERROR_11);
				return resp;
			}
			HeroService.refeshHeroProperty(roleInfo, heroInfo);
			
			int ck = 1;// 判断新手引导是否要更新
			if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
				if (skillPos == 2) {
					ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_LEARN_SEDOND_SKILL);
				} else if (skillPos == 3) {
					ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_LEARN_THIRD_SKILL);
				}
			} else {
				if (skillPos == 2) {
					ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_HERO_LEARN_SECOND_SKILL);
				} else if (skillPos == 3) {
					ck = GuideMgtService.dealGuideIndexNum(roleInfo, UserGuideNode.GAME_GUIDE_HERO_LEARN_THIRD_SKILL);
				}
			}
			if (ck != 1) {
				resp.setResult(ck);
				return resp;
			}


			// 任务
			boolean isRedQuest = QuestService.checkQuest(roleInfo, gameAction, null, true, false);
			// 红点监听
			boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false,
					RedPointMgtService.LISTENING_HERO_SKILL_UP_TYPES);
			// 推送红点
			if (isRedQuest || isRed) {
				RedPointMgtService.pop(roleInfo.getId());
			}
		
			resp.setResult(1);
			resp.setHeroId(heroId);
			resp.setFightValue(heroInfo.getFightValue());
			// resp.setHeroSkillPro(HeroService.getHeroPropertyRe(HeroService.getSkillAddProperty(HeroService.getSkillMap(heroInfo))));

			resp.setSkillNo(skillNo);
			resp.setSkillLevel(skillLevel);

			resp.setTech(roleInfo.getTech());
			resp.setLastRecoverTechTime(roleInfo.getLastRecoverTechTime().getTime());

			GameLogService.insertHeroSkillUpLog(roleInfo, heroInfo.getHeroNo(), skillNo, gameAction, currSkillLevel, skillLevel);
			return resp;
		}
	}

	/**
	 * 查看招募列表
	 * @param roleId
	 * @return
	 */
	public QueryRecruitResp queryRecruitList(int roleId) {

		QueryRecruitResp resp = new QueryRecruitResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_16);
			return resp;
		}
		List<QueryRecuitHeroRe> recruitHeroList = new ArrayList<QueryRecuitHeroRe>();
		List<QueryRecuitHeroRe> canRecruitHeroList = new ArrayList<QueryRecuitHeroRe>();
		List<QueryRecuitHeroRe> disRecruitHeroList = new ArrayList<QueryRecuitHeroRe>();
		for (HeroXMLInfo heroXMLInfo : HeroXMLInfoMap.getHeroMap().values()) {
			if (heroXMLInfo.getRace() == 0) {// 去除主角英雄
				continue;
			}
			if (HeroInfoMap.getHeroInfoByNo(roleId, heroXMLInfo.getNo()) == null) {// 没有的英雄才出现在列表中
				QueryRecuitHeroRe queryRecuitHeroRe = new QueryRecuitHeroRe();
				queryRecuitHeroRe.setNo(heroXMLInfo.getNo());
				// queryRecuitHeroRe.setStar(heroXMLInfo.getStar());
				int costSilver = 0;// 消耗的银子
				int costStarNum = heroXMLInfo.getChipNum();// 消耗的星石
				// 消耗的银子和星石为小于初始星级的总和
				// for (HeroStarXMLUpgrade heroStarXMLUpgrade :
				// heroXMLInfo.getStarMap().values()) {
				// if (heroStarXMLUpgrade.getNo() <= heroXMLInfo.getStar()) {
				// costSilver = costSilver + heroStarXMLUpgrade.getNeedSilver();
				// costStarNum = costStarNum +
				// heroStarXMLUpgrade.getNeedChips();
				// }
				// }
				int remianStarNum = 0;
				BagItemInfo bagItemInfo = BagItemMap.getBagItembyNo(roleInfo, heroXMLInfo.getChipNo());
				if (bagItemInfo != null) {
					remianStarNum = bagItemInfo.getNum();
				}
				queryRecuitHeroRe.setRemainNum(remianStarNum);
				queryRecuitHeroRe.setCostNum(costStarNum);
				queryRecuitHeroRe.setCostSilver(costSilver);
				queryRecuitHeroRe.setLackNum(costStarNum - remianStarNum);
				if (costStarNum <= remianStarNum) {// 可以招募
					canRecruitHeroList.add(queryRecuitHeroRe);
				} else {
					disRecruitHeroList.add(queryRecuitHeroRe);
				}
				Collections.sort(canRecruitHeroList, canRecruitHeroComparator);
				Collections.sort(disRecruitHeroList, disRecruitHeroComparator);
			}
		}
		recruitHeroList.addAll(canRecruitHeroList);
		recruitHeroList.addAll(disRecruitHeroList);
		resp.setResult(1);
		resp.setHeroListCount(recruitHeroList.size());
		resp.setRecruitHeroList(recruitHeroList);
		return resp;
	}

	/**
	 * 武将使用道具
	 * @param roleId
	 * @param req
	 * @return
	 */
	public HeroPropUseResp heroPropUse(int roleId, HeroPropUseReq req) {
		HeroPropUseResp resp = new HeroPropUseResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.HERO_PROP_USE_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			int heroId = req.getHeroId();
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(roleId, heroId);
			if (heroInfo == null) {
				resp.setResult(ErrorCode.HERO_PROP_USE_ERROR_2);
				return resp;
			}
			byte action = req.getAction();
			int gameAction = ActionType.action88.getType();
			int itemNum = req.getItemNum();
			switch (action) {
			case 0:
				if(itemNum <= 0) {
					resp.setResult(ErrorCode.HERO_PROP_USE_ERROR_3);
					return resp;
				}
				if(itemNum > GameValue.MAX_PROP_USE){
					itemNum = GameValue.MAX_PROP_USE;
				}
				int result = heroPropUseUp(gameAction, roleInfo, heroInfo, req.getItemNo(), itemNum);
				if (result != 1) {
					resp.setResult(result);
					return resp;
				}
				break;
			case 1:// 一键升级亲密度
				int result1 = heroOneKeyIntimacyUp(gameAction, roleInfo, heroInfo);
				if (result1 != 1) {
					resp.setResult(result1);
					return resp;
				}
				break;
			default:
				resp.setResult(ErrorCode.HERO_PROP_USE_ERROR_3);
				return resp;
			}

			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_TYPE_HERO);

			resp.setResult(1);
			resp.setHeroInfo(HeroService.getHeroInfoRe(heroInfo));
			return resp;
		}
	}

	/**
	 * 武将使用道具
	 * @param action
	 * @param roleInfo
	 * @param heroInfo
	 * @param itemNo
	 * @param itemNum
	 * @return
	 */
	public int heroPropUseUp(int action, RoleInfo roleInfo, HeroInfo heroInfo, int itemNo, int itemNum) {
		PropXMLInfo propXMLInfo = PropXMLInfoMap.getPropXMLInfo(itemNo);
		if (propXMLInfo == null) {
			return ErrorCode.HERO_PROP_USE_ERROR_4;
		}

		if (propXMLInfo.getSubType() != 1 && propXMLInfo.getSubType() != 8) {
			return ErrorCode.HERO_PROP_USE_ERROR_6;
		}

		if (!BagItemMap.checkBagItemNum(roleInfo, itemNo, itemNum)) {
			return ErrorCode.HERO_PROP_USE_ERROR_5;
		}
		Map<Integer, Integer> delMap = new HashMap<Integer, Integer>();
		delMap.put(itemNo, itemNum);

		int add = itemNum * NumberUtils.toInt(propXMLInfo.getUseParam());
		switch (propXMLInfo.getSubType()) {
		case 1:// 经验丹
			int result = HeroService.heroExpAdd(action, roleInfo, heroInfo, add, delMap);
			if (result != 1) {
				return result;
			}
			break;
		case 8:// 亲密度
			int result1 = HeroService.heroIntimacyAdd(action, roleInfo, heroInfo, add, delMap);
			if (result1 != 1) {
				return result1;
			}
			break;
		default:
			break;
		}
		return 1;
	}

	/**
	 * 武将一键升级亲密度
	 * @param action
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
	private int heroOneKeyIntimacyUp(int action, RoleInfo roleInfo, HeroInfo heroInfo) {
		int add = 0;
		// <itemNo,per亲密度>
		Map<Integer, Integer> itemPerMap = new HashMap<Integer, Integer>();
		// <itemNo,数量>
		Map<Integer, Integer> canCostMap = new HashMap<Integer, Integer>();
		Map<Integer, BagItemInfo> itemMap = BagItemMap.getBagItem(roleInfo);
		if (itemMap != null) {
			for (BagItemInfo item : itemMap.values()) {
				PropXMLInfo propXMLInfo = PropXMLInfoMap.getPropXMLInfo(item.getItemNo());
				if (propXMLInfo == null) {
					continue;
				}
				if (propXMLInfo.getSubType() == 8) {
					int per = NumberUtils.toInt(propXMLInfo.getUseParam());
					add += item.getNum() * per;
					if (canCostMap.containsKey(item.getItemNo())) {
						canCostMap.put(item.getItemNo(), canCostMap.get(item.getItemNo()) + item.getNum());
					} else {
						canCostMap.put(item.getItemNo(), item.getNum());
					}
					if (!itemPerMap.containsKey(item.getItemNo())) {
						itemPerMap.put(item.getItemNo(), per);
					}
				}
			}
		}
		if (add <= 0) {
			return ErrorCode.HERO_PROP_USE_ERROR_7;
		}
		int beforeLevel = heroInfo.getIntimacyLevel();
		int beforeValue = heroInfo.getIntimacyValue();
		int[] after = HeroService.addHeroIntimacy(roleInfo, heroInfo, add);
		if (after == null) {
			return ErrorCode.HERO_PROP_USE_ERROR_8;
		}
		int afterLevel = after[0];
		int afterValue = after[1];
		int costValue = after[2];
		Map<Integer, Integer> costMap = null;
		if (costValue == add) {
			costMap = new HashMap<Integer, Integer>(canCostMap);
		} else {
			costMap = new HashMap<Integer, Integer>();
			// 消耗道具从小到大
			Map<Integer, Integer> sortPerMap = CommonUtil.sortMapByValue(itemPerMap);
			int cost = 0;
			for (int itemNo : sortPerMap.keySet()) {
				int per = sortPerMap.get(itemNo);
				int itemNum = canCostMap.get(itemNo);
				if (costValue > cost) {
					int costNum = (int) Math.ceil((costValue - cost) / (per * 1.0));
					if (costNum > itemNum) {
						costNum = itemNum;
					}
					cost += costNum * per;
					costMap.put(itemNo, costNum);
				} else {
					break;
				}				
			}
		}
		int delresult = ItemService.bagItemDel(action, roleInfo, costMap);
		if (delresult != 1) {
			return delresult;
		}
		if (beforeLevel != afterLevel || afterValue != beforeValue) {
			if (HeroDAO.getInstance().updateHeroIntimacy(heroInfo.getId(), afterLevel, afterValue)) {
				heroInfo.setIntimacyLevel(afterLevel);
				heroInfo.setIntimacyValue(afterValue);
				if (beforeLevel != afterLevel) {
					HeroService.refeshHeroProperty(roleInfo, heroInfo);
				}
			} else {
				return ErrorCode.HERO_PROP_USE_ERROR_9;
			}
			GameLogService.insertHeroUpLog(roleInfo,heroInfo.getHeroNo(), ActionType.action72.getType(), 4, beforeLevel,
					afterLevel);
		}
		return 1;
	}
	
	/**
	 * 战斗力
	 * @param action
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
	public FightValueResp queryFightValue(int roleId) {

		FightValueResp resp = new FightValueResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_16);
			return resp;
		}
		
		int heroFightValue = HeroService.getOtherHeroFightValue(roleInfo);
		int equipFightValue = HeroService.getHeroEquipFightValue(roleInfo);
		int soldierFightValue = HeroService.getSoldierFightValue(roleInfo);
		int magicFightValue = HeroService.getMagicFightValue(roleInfo);
		
		
		resp.setResult(1);
		resp.setHeroFightValue(heroFightValue);
		resp.setEquipFightValue(equipFightValue);
		resp.setSoldierFightValue(soldierFightValue);
		resp.setMagicFightValue(magicFightValue);
		return resp;
	}

	
}