package com.snail.webgame.game.protocal.guide.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.ChallengeBattleInfoMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.UserGuideNode;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.dao.ChallengeBattleDAO;
import com.snail.webgame.game.dao.EquipDAO;
import com.snail.webgame.game.dao.QuestDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.SkillDAO;
import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.QuestProtoXmlInfoMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;

/**
 * 新手引导节点 赋值处理
 * @author zenggang
 *
 */
public class GuideNodeService {

	/**
	 * 设置新手引导节点
	 * @param roleInfo
	 * @param node
	 * @return
	 */
	public int setGuideNode(RoleInfo roleInfo, int node) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return ErrorCode.SYSTEM_ERROR;
		}

		int val = GuideMgtService.getGuideIndexNum(roleLoadInfo, node);
		if (val == 0) {
			for (int i = 0; i <= node; i++) {
				val = GuideMgtService.getGuideIndexNum(roleLoadInfo, i);
				if (val == 0) {
					int result = dealGuideNode(roleInfo, i);
					if (result == 0) {
						continue;
					}
					if (result != 1) {
						return result;
					}
				}
			}
		}

		return 1;
	}

	/**
	 * 处理新手引导节点
	 * @param roleInfo
	 * @param node
	 * @return
	 */
	private int dealGuideNode(RoleInfo roleInfo, int node) {
		int result = 0;
		switch (node) {
		case UserGuideNode.GAME_GUIDE_FIRST_FIGHT:
			result = dealPoint4(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_WEAR_EQUIPE_1:
			result = dealPoint5(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_TASK_1:
			result = dealPoint6(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_SECOND_FIGHT:
			result = dealPoint8(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_LEARN_SEDOND_SKILL:
			result = dealPoint9(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_TASK_2:
			result = dealPoint10(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_THIRD_FIGHT:
			result = dealPoint12(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_NORMAL_LOTTERY:
			result = dealPoint13(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_UPGRADE_HERO:
			result = dealPoint14(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_HERO_WEAR_EQUIPE_1:
			result = dealPoint15(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_HERO_WEAR_EQUIPE_2:
			result = dealPoint16(roleInfo);
			break;
		case UserGuideNode.GAME_GUIDE_HERO_WEAR_EQUIPE_3:
			result = dealPoint17(roleInfo);
			break;
		default:
			break;
		}

		if (result == 1) {
			if (!updateGuideIndexNum(roleInfo, node, 1)) {
				return ErrorCode.SQL_DB_ERROR;
			}
		}

		return 1;
	}

	/**
	 * 更新节点值
	 * @param roleInfo
	 * @param index
	 * @param status
	 * @return
	 */
	private boolean updateGuideIndexNum(RoleInfo roleInfo, int index, int status) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return false;
		}
		String guideInfoStr = roleLoadInfo.getGuideInfo();
		String[] guideInfo = new String[] { "0" };
		if (guideInfoStr != null) {
			guideInfo = guideInfoStr.split(",");
		}
		for (int i = guideInfo.length; i <= index; i++) {
			if ("".equals(guideInfoStr)) {
				guideInfoStr = "0";
			} else {
				guideInfoStr = guideInfoStr + ",0";
			}
		}
		guideInfo = guideInfoStr.split(",");
		guideInfo[index] = status + "";
		StringBuffer guideInfoUpdate = new StringBuffer();
		for (String oneGuideInfo : guideInfo) {
			if ("1".equals(oneGuideInfo)) {
				guideInfoUpdate.append("1,");
			} else {
				guideInfoUpdate.append("0,");
			}
		}
		guideInfoStr = guideInfoUpdate.substring(0, guideInfoUpdate.length() - 1);
		if (RoleDAO.getInstance().updateGuideData(roleLoadInfo.getId(), guideInfoStr)) {
			roleLoadInfo.setGuideInfo(guideInfoStr);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 处理新手引导节点 第一场战斗
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint4(RoleInfo roleInfo) {
		byte chapterType = 1;
		int chapterNo = 0;
		int battleNo = 63010001;
		return dealFightStart(roleInfo, chapterType, chapterNo, battleNo);
	}

	/**
	 * 处理新手引导节点 主角第一次穿装备
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint5(RoleInfo roleInfo) {
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHero == null) {
			return ErrorCode.SYSTEM_ERROR;
		}
		Map<Integer, EquipInfo> equipMap = mainHero.getEquipMap();
		if (equipMap != null && equipMap.size() <= 0) {
			EquipInfo equipInfo = EquipInfoMap.getBestBagTypeEquip(roleInfo.getId(), (byte) 1);
			if (equipInfo != null) {
				List<EquipInfo> upAddBagItem = new ArrayList<EquipInfo>();
				upAddBagItem.add(equipInfo);
				if (EquipDAO.getInstance().addEquip(roleInfo.getId(), mainHero.getId(), upAddBagItem)) {
					EquipInfoMap.addHeroEquipInfo(mainHero, equipInfo);
				}
			}
		}

		return 1;
	}

	/**
	 * 处理新手引导节点 第一次领任务
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint6(RoleInfo roleInfo) {
		int taskNo = 5000;// 主线任务
		return dealTaskFinish(roleInfo, taskNo);
	}

	/**
	 * 处理新手引导节点 第二场战斗
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint8(RoleInfo roleInfo) {
		byte chapterType = 1;
		int chapterNo = 1;
		int battleNo = 63010101;
		return dealFightStart(roleInfo, chapterType, chapterNo, battleNo);
	}

	/**
	 * 处理新手引导节点 学第二个技能
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint9(RoleInfo roleInfo) {
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHero == null) {
			return ErrorCode.SYSTEM_ERROR;
		}
		return dealHeroSkill(roleInfo, mainHero, 2);
	}

	/**
	 * 处理新手引导节点 第二次领任务
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint10(RoleInfo roleInfo) {

		return 1;
	}

	/**
	 * 处理新手引导节点 第三场战斗
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint12(RoleInfo roleInfo) {
		byte chapterType = 1;
		int chapterNo = 1;
		int battleNo = 63010102;
		return dealFightStart(roleInfo, chapterType, chapterNo, battleNo);
	}

	/**
	 * 处理新手引导节点 普通抽卡
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint13(RoleInfo roleInfo) {

		return 1;
	}

	/**
	 * 处理新手引导节点 升级武将
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint14(RoleInfo roleInfo) {

		return 1;
	}

	/**
	 * 处理新手引导节点 副将穿第一个装备
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint15(RoleInfo roleInfo) {

		return 1;
	}

	/**
	 * 处理新手引导节点 副将穿第二个装备
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint16(RoleInfo roleInfo) {

		return 1;
	}

	/**
	 * 处理新手引导节点 副将穿第三个装备
	 * @param roleInfo
	 * @return
	 */
	private int dealPoint17(RoleInfo roleInfo) {

		return 1;
	}

	/**
	 * 处理新手引导节点 战斗
	 * @param roleInfo
	 * @param chapterType
	 * @param chapterNo
	 * @param battleNo
	 * @return
	 */
	private int dealFightStart(RoleInfo roleInfo, byte chapterType, int chapterNo, int battleNo) {
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHero == null) {
			return ErrorCode.SYSTEM_ERROR;
		}

		BattleDetail battleDetail = ChallengeBattleXmlInfoMap.getBattleDetail(chapterType, chapterNo, battleNo);
		if (battleDetail == null) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}

		ChallengeBattleInfo challengeBattleInfo = ChallengeBattleInfoMap.getBattleInfo(roleInfo.getId(), chapterType,
				chapterNo, battleNo);
		if (challengeBattleInfo == null) {
			challengeBattleInfo = new ChallengeBattleInfo(roleInfo.getId(), chapterType, chapterNo, battleNo);
			challengeBattleInfo.setStar("1,2,3");
			challengeBattleInfo.setStars(3);

			if (ChallengeBattleDAO.getInstance().insertChallengeBattleInfo(challengeBattleInfo)) {

				ChallengeBattleInfoMap.addInfo(challengeBattleInfo);
				if (battleDetail.getBattleNum() > -1) {
					// 该章副本未挑战过
					int newBattleNum = battleDetail.getBattleNum() - 1;
					// 记录战斗信息
					if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(),
							newBattleNum, new Timestamp(System.currentTimeMillis()))) {
						challengeBattleInfo.setCanFightNum(newBattleNum);
						challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
					}
				}
				// 记录已通关的副本
				roleInfo.getRoleLoadInfo().addBattle(challengeBattleInfo.getBattleId());
				ChallengeBattleInfoMap.addInfo(challengeBattleInfo);
			}
		} else {
			// 更新星级
			if (ChallengeBattleDAO.getInstance().updateChallengeBattleInfo("1,2,3", challengeBattleInfo.getId())) {
				challengeBattleInfo.setStar("1,2,3");
				challengeBattleInfo.setStars(3);
			}
		}

		if (battleDetail.getBattleNum() > -1) {
			int nextBattleNum = challengeBattleInfo.getCanFightNum() - 1;
			// 胜利后,更新次数及战斗时间
			if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(), nextBattleNum,
					new Timestamp(System.currentTimeMillis()))) {
				challengeBattleInfo.setCanFightNum(nextBattleNum);
				challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
			}
		} else if (battleDetail.getBattleInterTime() > -1) {
			if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(),
					battleDetail.getBattleNum(), new Timestamp(System.currentTimeMillis()))) {
				challengeBattleInfo.setCanFightNum(battleDetail.getBattleNum());
				challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
			}
		}
		List<DropInfo> mustDrops = new ArrayList<DropInfo>();
		int addExp = 0;
		int level = mainHero.getHeroLevel();
		int pzMaxLv = HeroXMLInfoMap.getMaxMainLv();
		if (level < pzMaxLv) {
			if (battleDetail.getChapterType() == 1) {
				addExp = level * GameValue.EXP_VALUE + GameValue.EXP_ADD;
			} else if (battleDetail.getChapterType() == 2) {
				addExp = level * GameValue.EXP_VALUE_1 + GameValue.EXP_ADD_1;
			}
		}
		if (addExp > 0) {
			mustDrops.add(new DropInfo(ConditionType.TYPE_EXP.getName(), addExp));
		}
		mustDrops.add(new DropInfo(battleDetail.getItemNo(), 1));

		List<DropXMLInfo> prizeXmls = PropBagXMLMap.getPropBagXMLList(battleDetail.getBag());
		ItemService.addPrizeForPropBag(ActionType.action6.getType(), roleInfo, prizeXmls, battleDetail.getCardBag(),
				null, null, mustDrops, null, true);

		return 1;
	}

	/**
	 * 处理新手引导节点 学技能
	 * @param roleInfo
	 * @param heroInfo
	 * @param skillPos
	 * @return
	 */
	private int dealHeroSkill(RoleInfo roleInfo, HeroInfo heroInfo, int skillPos) {
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
		if (heroXMLInfo == null) {
			return ErrorCode.SYSTEM_ERROR;
		}
		HeroXMLSkill heroSkill = null;
		for (HeroXMLSkill xmlSkill : heroXMLInfo.getSkillMap().values()) {
			if (xmlSkill.getSkillPos() == skillPos) {
				heroSkill = xmlSkill;
			}
		}
		if (heroSkill == null) {
			return ErrorCode.SYSTEM_ERROR;
		}
		int skillNo = heroSkill.getSkillNo();
		HeroSkillXMLInfo skillXml = HeroXMLInfoMap.getHeroSkillXMLInfo(skillPos);
		if (skillXml == null) {
			return ErrorCode.SYSTEM_ERROR;
		}
		int currSkillLevel = HeroService.getSkillLv(heroInfo, skillNo);
		if (currSkillLevel >= 1) {
			return ErrorCode.SYSTEM_ERROR;
		}
		String skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, 1);
		if (SkillDAO.getInstance().addOrUpdateHeroSkill(heroInfo.getId(), skillStr)) {
			heroInfo.setSkillStr(skillStr);
		} else {
			return ErrorCode.SQL_DB_ERROR;
		}
		return 1;
	}

	/**
	 * 处理新手引导节点 领任务
	 * @param roleInfo
	 * @param taskNo
	 * @return
	 */
	private int dealTaskFinish(RoleInfo roleInfo, int taskNo) {
		QuestProtoXmlInfo xmlInfo = QuestProtoXmlInfoMap.questXml(taskNo);
		if (xmlInfo == null) {
			return ErrorCode.SYSTEM_ERROR;
		}
		QuestInProgressInfo quest = roleInfo.getQuestInfoMap().getQuestInProgressInfo(xmlInfo.getQuestProtoNo());
		if (quest == null) {
			return ErrorCode.SYSTEM_ERROR;
		}
		if (quest.getStatus() == QuestInProgressInfo.STATUS_FINISH) {
			if (QuestDAO.getInstance().updateQuestInProgressInfo(quest.getId(), QuestInProgressInfo.STATUS_CLEAR)) {
				// 修改任务状态
				roleInfo.getQuestInfoMap().questClear(quest, xmlInfo.getQuestType(), true);

				String prizeNo = xmlInfo.getPrizeNo();
				List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
				List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
				ItemService.addPrizeForPropBag(ActionType.action6.getType(), roleInfo, drops, null, getPropList, null,
						null, null, false);
			}
		}

		return 1;
	}
}
