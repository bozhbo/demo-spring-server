package com.snail.webgame.game.protocal.redPoint.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.ChallengeBattleInfoMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.FightCampaignInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.MineInfoMap;
import com.snail.webgame.game.cache.RoleAddRquestMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCost;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCostItem;
import com.snail.webgame.game.common.xml.info.HeroColourXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroStarXMLUpgrade;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.FightCampaignInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.MineHelpRole;
import com.snail.webgame.game.info.MineInfo;
import com.snail.webgame.game.info.MinePrize;
import com.snail.webgame.game.info.MineRole;
import com.snail.webgame.game.info.PresentEnergyInfo;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.MineFightLog;
import com.snail.webgame.game.info.log.RoleArenaLog;
import com.snail.webgame.game.protocal.challenge.service.ChallengeService;
import com.snail.webgame.game.protocal.checkIn.service.CheckInMgtService;
import com.snail.webgame.game.protocal.checkIn.service.CheckInService;
import com.snail.webgame.game.protocal.checkIn7Day.queryList.QueryCheckIn7DayListItemRe;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemMgtService;
import com.snail.webgame.game.protocal.mine.service.MineService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.cache.CheckInPrizeXmlMap;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.FuncOpenXMLInfoMap;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.QuestProtoXmlInfoMap;
import com.snail.webgame.game.xml.cache.SnatchMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.ChapterInfo;
import com.snail.webgame.game.xml.info.CheckInPrizeXMLInfo;
import com.snail.webgame.game.xml.info.EquipRefineInfo;
import com.snail.webgame.game.xml.info.EquipStrengthenConfigInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.EquipXMLUpgrade;
import com.snail.webgame.game.xml.info.FuncOpenXMLInfo;
import com.snail.webgame.game.xml.info.MineXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;
import com.snail.webgame.game.xml.info.SnatchInfo;

public class RedPointMgtService {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	private static final byte LIGHT = 1;
	private static final byte DARK = 0;
	private static final byte DEFAULT = -1;
	/**
	 * 所有红点类型
	 */
	public static final byte[] ALL_TYPES = new byte[] {
			GameValue.RED_POINT_TYPE_ROLE_SKILL_UPGRADE,GameValue.RED_POINT_TYPE_EQUIP_UPGRADE, 
			GameValue.RED_POINT_TYPE_HERO, GameValue.RED_POINT_TYPE_ARENA_CHALLENGED,
			GameValue.RED_POINT_TYPE_ARENA_PRIZE, GameValue.RED_POINT_TYPE_EXPERIENCE_1,
			GameValue.RED_POINT_TYPE_EXPERIENCE_2, GameValue.RED_POINT_TYPE_EXPERIENCE_3,
			GameValue.RED_POINT_TYPE_EXPERIENCE_4, GameValue.RED_POINT_TYPE_CHECK_MONTH,
			GameValue.RED_POINT_TYPE_CHECK_WEEK, GameValue.RED_POINT_TYPE_LEVEL_GIFT,
			GameValue.RED_POINT_TYPE_CHEST, GameValue.RED_POINT_TYPE_MISSION_COMPLETE_1,
			GameValue.RED_POINT_TYPE_MISSION_COMPLETE_2, GameValue.RED_POINT_TYPE_MISSION_COMPLETE_3,
			GameValue.RED_POINT_TYPE_CHALLENGE_PRIZE, GameValue.RED_POINT_TYPE_FRIEND,
			GameValue.RED_POINT_STONE_COMPOSE, GameValue.RED_POINT_TYPE_ROLE_CLUB_REQUEST,
			GameValue.RED_POINT_TYPE_SOLDIER_UPGRADE, GameValue.RED_POINT_TYPE_STONE_NOTIFY, 
			GameValue.RED_POINT_TYPE_PRESENT_ENERGY, GameValue.RED_POINT_TYPE_MINE_SCENE,
			GameValue.RED_POINT_TYPE_MINE_LOG};
	
	/**
	 * 监听变动红点
	 */
	public static final byte[] LISTENING_ALL_CHANGE = new byte[] { GameValue.RED_POINT_TYPE_EQUIP_UPGRADE,
		GameValue.RED_POINT_TYPE_STONE_NOTIFY,GameValue.RED_POINT_TYPE_SOLDIER_UPGRADE, 
		GameValue.RED_POINT_TYPE_ROLE_SKILL_UPGRADE, GameValue.RED_POINT_TYPE_CHEST};

	
	/**
	 * 监听副将等级变动红点
	 */
	public static final byte[] LISTENING_OTHER_HERO_UPGRADE_TYPES = new byte[] {GameValue.RED_POINT_TYPE_HERO};
	
	/**
	 * 监听主将等级变动红点
	 */
	public static final byte[] LISTENING_MAIN_HERO_UPGRADE_TYPES = new byte[] { GameValue.RED_POINT_TYPE_HERO,
		GameValue.RED_POINT_STONE_COMPOSE};

	/**
	 * 监听金币购买
	 */
	public static final byte[] LISTENING_GOLD_BUY = new byte[] { GameValue.RED_POINT_TYPE_HERO};
	
	/**
	 * 监听领取礼包
	 */
	public static final byte[] LISTEN_CHECK_IN_TYPES = new byte[] {GameValue.RED_POINT_TYPE_HERO, 
		GameValue.RED_POINT_TYPE_CHECK_MONTH,GameValue.RED_POINT_TYPE_CHECK_WEEK,
		GameValue.RED_POINT_TYPE_LEVEL_GIFT,GameValue.RED_POINT_STONE_COMPOSE};
	
	/**
	 * 监听武将装备穿戴
	 */
	public static final byte[] LISTENING_HERO_EQUIPS_CHANGE_TYPES = new byte[] {
			GameValue.RED_POINT_TYPE_HERO };
	
	/**
	 * 监听武将技能升级
	 */
	public static final byte[] LISTENING_HERO_SKILL_UP_TYPES = new byte[] {GameValue.RED_POINT_TYPE_HERO,};

	/**
	 * 监听银子变动红点
	 */
	public static final byte[] LISTENING_MONEY_CHANGE_TYPES = new byte[] { GameValue.RED_POINT_TYPE_HERO};

	/**
	 * 监听抽卡变动
	 */
	public static final byte[] LISTENING_CHEST_CHANGE_TYPES = new byte[] {
		 GameValue.RED_POINT_TYPE_HERO, GameValue.RED_POINT_STONE_COMPOSE};
	
	/**
	 * 监听所有任务变动红点
	 */
	public static final byte[] LISTENING_MISSION_CHANGE_TYPES = new byte[] {
			GameValue.RED_POINT_TYPE_MISSION_COMPLETE_1, GameValue.RED_POINT_TYPE_MISSION_COMPLETE_2,
			GameValue.RED_POINT_TYPE_MISSION_COMPLETE_3};
	
	/**
	 * 监听0点刷新变动红点
	 */
	public static final byte[] LISTENING_ZERO_HOUR = new byte[]{
			GameValue.RED_POINT_TYPE_MISSION_COMPLETE_1, GameValue.RED_POINT_TYPE_MISSION_COMPLETE_2,
			GameValue.RED_POINT_TYPE_MISSION_COMPLETE_3, GameValue.RED_POINT_TYPE_HERO,
			GameValue.RED_POINT_TYPE_CHECK_WEEK, GameValue.RED_POINT_TYPE_CHECK_MONTH,
			GameValue.RED_POINT_TYPE_EXPERIENCE_4, GameValue.RED_POINT_TYPE_EXPERIENCE_3};
	
	/**
	 * 监听领取副本宝箱
	 */
	public static final byte[] LISTENING_CHALLENGE_PRIZE = new byte[] {GameValue.RED_POINT_TYPE_CHALLENGE_PRIZE,
		GameValue.RED_POINT_STONE_COMPOSE};
	
	/**
	 * 监听副本完成后
	 */
	public static final byte[] LISTENING_CHALLENGE_FIGHT_END = new byte[] {GameValue.RED_POINT_TYPE_CHALLENGE_PRIZE,
		GameValue.RED_POINT_TYPE_HERO,	GameValue.RED_POINT_TYPE_LEVEL_GIFT,GameValue.RED_POINT_STONE_COMPOSE};
	
	/**
	 * 监听对攻战完成后
	 */
	public static final byte[] LISTENING_ATT_FIGHT_END = new byte[] {GameValue.RED_POINT_TYPE_EXPERIENCE_3,
		GameValue.RED_POINT_TYPE_HERO,	GameValue.RED_POINT_TYPE_LEVEL_GIFT,
		GameValue.RED_POINT_STONE_COMPOSE};
	
	/**
	 * 监听防守战完成后
	 */
	public static final byte[] LISTENING_DEF_FIGHT_END = new byte[] {GameValue.RED_POINT_TYPE_EXPERIENCE_3,
		GameValue.RED_POINT_TYPE_HERO,	GameValue.RED_POINT_TYPE_LEVEL_GIFT,
		GameValue.RED_POINT_STONE_COMPOSE};
	
	/**
	 * 监听竞技场完成后
	 */
	public static final byte[] LISTENING_ARENA_FIGHT_END = new byte[] {GameValue.RED_POINT_TYPE_ARENA_PRIZE,
		GameValue.RED_POINT_TYPE_HERO, GameValue.RED_POINT_STONE_COMPOSE};
	
	/**
	 * 监听公会
	 */
	public static final byte[] LISTENING_CLUB_TYPE = new byte[]{GameValue.RED_POINT_TYPE_ROLE_CLUB_REQUEST};
	
	/**
	 * 监听任务完成后
	 */
	public static final byte[] LISTENING_QUEST_FIGHT_END = new byte[] {GameValue.RED_POINT_TYPE_HERO,	
		GameValue.RED_POINT_TYPE_LEVEL_GIFT,GameValue.RED_POINT_STONE_COMPOSE};

	
	/**
	 * 监听好友
	 */
	public static final byte[] LISTENING_FRIENDS_TYPE = new byte[]{GameValue.RED_POINT_TYPE_FRIEND, GameValue.RED_POINT_TYPE_PRESENT_ENERGY};
	
	/**
	 * 监听剑阁
	 */
	public static final byte[] LISTENING_MINE_TYPE = new byte[]{GameValue.RED_POINT_TYPE_MINE_SCENE, GameValue.RED_POINT_TYPE_MINE_LOG,
		GameValue.RED_POINT_STONE_COMPOSE};
	
	/**
	 * 检测红点改变情况  true - 变化   false -不变化
	 * @param roleId
	 * @param common
	 * @param isRes 是否推送
	 * @param types
	 * 
	 */
	public static boolean check2PopRedPoint(int roleId, String common, boolean isRes, byte... types) {
		boolean flag = false;
		if (types.length == 0) {
			return false;
		}
		//玩家是否登录
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return false;
		}
		//整合添加的红点
		List<Byte> redList = new ArrayList<Byte>();
		for(byte i : LISTENING_ALL_CHANGE) 
		{
			redList.add(i);
		}
		for(byte red : types)
		{
			if(redList.contains(red))
			{
				continue;
			}
			redList.add(red);
		}

		
		long start = System.currentTimeMillis();
		Map<Byte, Byte> redPointMap = new HashMap<Byte, Byte>();
		for (byte type : redList) {
			//检验是否有必要检验红点
			if (!validateCheckable(roleId, type, common)) {
				continue;
			}
			byte status = checkPopRedPoint(roleId, type, common);
			if (status == DEFAULT) {
				continue;
			}
			//存入缓存
			redPointMap.put(type, status);
			
		}
		//是否有改变
		flag = checkRedPoint(roleId, redPointMap);
		
		// 发送推送
		if(isRes && flag){
			pop(roleId);
		}

		
		//日志记录
		long end = System.currentTimeMillis();
		StringBuffer stringBuffer = new StringBuffer();
		for (byte type : types) {
			stringBuffer.append(type + ",");
		}
		logger.info("check2PopRedPoint end! cost : " + (end - start) + "(ms). roleId : " + roleId + " types : "
				+ stringBuffer.toString());
		
		return flag;
	}
	
	
	/**
	 * 检测红点改变情况  true - 变化   false -不变化 (任务单独检测)
	 * @param roleId
	 * @param common
	 * @param isRes 是否推送
	 * @param types
	 * 
	 */
	public static boolean checkQuestRedPoint(int roleId, String common, boolean isRes, byte... types) {
		boolean flag = false;
		if (types.length == 0) {
			return false;
		}
		//玩家是否登录
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return false;
		}
		//整合添加的红点
		List<Byte> redList = new ArrayList<Byte>();
		if(isRes){
			for(byte i : LISTENING_ALL_CHANGE) 
			{
				redList.add(i);
			}
		}
		for(byte red : types)
		{
			if(redList.contains(red))
			{
				continue;
			}
			redList.add(red);
		}

		
		long start = System.currentTimeMillis();
		Map<Byte, Byte> redPointMap = new HashMap<Byte, Byte>();
		for (byte type : redList) {
			//检验是否有必要检验红点
			if (!validateCheckable(roleId, type, common)) {
				continue;
			}
			byte status = checkPopRedPoint(roleId, type, common);
			if (status == DEFAULT) {
				continue;
			}
			//存入缓存
			redPointMap.put(type, status);
			
		}
		//是否有改变
		flag = checkRedPoint(roleId, redPointMap);
		
		// 发送推送
		if(isRes && flag){
			pop(roleId);
		}

		
		//日志记录
		long end = System.currentTimeMillis();
		StringBuffer stringBuffer = new StringBuffer();
		for (byte type : types) {
			stringBuffer.append(type + ",");
		}
		logger.info("check2PopRedPoint end! cost : " + (end - start) + "(ms). roleId : " + roleId + " types : "
				+ stringBuffer.toString());
		
		return flag;
	}

	/**
	 * 检测红点改变情况
	 * @param roleId
	 * @param type
	 * @param common
	 */
	private static byte checkPopRedPoint(int roleId, byte type, String common) {
		switch (type) {
//		case GameValue.RED_POINT_DEFAULT:
//			return LIGHT;
		case GameValue.RED_POINT_TYPE_ROLE_SKILL_UPGRADE: //技能升级
			return checkRoleSkillUpgradeRemind(roleId);
		case GameValue.RED_POINT_TYPE_HERO:		//武将
			byte i = 0;
			//装备可穿戴、合成
//			i = checkEquipComposeRemind(roleId);
//			if(i == 1){
//				return 1;
//			}
			//有武将可招募
			i = checkHeroRecruitRemind(roleId);
			if(i == 1){
				return 1;
			}
			//有技能可以升级
			i = checkHeroSkillUpgradeRemind(roleId);
			if(i == 1){
				return 1;
			}
			//武将可以觉醒时
			i = checkHeroColorUpgradeRemind(roleId);
			if(i == 1){
				return 1;
			}
			//布阵有空位
			i = checkHeroUpStar(roleId);
			if(i == 1){
				return 1;
			}
			return i;
		case GameValue.RED_POINT_TYPE_ARENA_CHALLENGED: //竞技场排名下降
			return checkArenaChallengedRemind(roleId);
		case GameValue.RED_POINT_TYPE_ARENA_PRIZE: //跨服竞技场可领取段位奖励
			return checkArenaPrizeRemind(roleId);
		case GameValue.RED_POINT_TYPE_EXPERIENCE_1: //防守活动
			return checkExperienceRemind(roleId, FightType.FIGHT_TYPE_12);
		case GameValue.RED_POINT_TYPE_EXPERIENCE_2: //攻城略地
			return checkExperienceRemind(roleId, FightType.FIGHT_TYPE_6);
		case GameValue.RED_POINT_TYPE_EXPERIENCE_3: //对攻战
			return checkExperienceRemind(roleId, FightType.FIGHT_TYPE_11);
		case GameValue.RED_POINT_TYPE_EXPERIENCE_4: //练兵场
			return checkExperienceRemind(roleId, FightType.FIGHT_TYPE_4);
		case GameValue.RED_POINT_TYPE_CHECK_MONTH: //月签到
			return checkCheckinRemind(roleId, GameValue.RED_POINT_TYPE_CHECK_MONTH);
		case GameValue.RED_POINT_TYPE_CHECK_WEEK: //开服七日签
			return checkCheckinRemind(roleId, GameValue.RED_POINT_TYPE_CHECK_WEEK);
		case GameValue.RED_POINT_TYPE_LEVEL_GIFT: //等级礼包
			return checkCheckinRemind(roleId, GameValue.RED_POINT_TYPE_LEVEL_GIFT);
		case GameValue.RED_POINT_TYPE_CHEST:	//抽奖
			return checkFreeChestRemind(roleId);
		case GameValue.RED_POINT_TYPE_MISSION_COMPLETE_1:	//任务(主线)
			return checkMissionCompleteRemind(roleId, GameValue.RED_POINT_TYPE_MISSION_COMPLETE_1);
		case GameValue.RED_POINT_TYPE_MISSION_COMPLETE_2: //任务(国家)
			return checkMissionCompleteRemind(roleId, GameValue.RED_POINT_TYPE_MISSION_COMPLETE_2);
		case GameValue.RED_POINT_TYPE_MISSION_COMPLETE_3: //任务(日常)
			return checkMissionCompleteRemind(roleId, GameValue.RED_POINT_TYPE_MISSION_COMPLETE_3);
		case GameValue.RED_POINT_TYPE_CHALLENGE_PRIZE:	//征战有宝箱可领取
			return checkChallengePrizeRemind(roleId);
		case GameValue.RED_POINT_TYPE_FRIEND:	//好友申请
			return isFriendApply(roleId);
		case GameValue.RED_POINT_STONE_COMPOSE: //历练 你争我夺（宝物可合成）
			return checkStoneComposeRemind(roleId);
		case GameValue.RED_POINT_TYPE_ROLE_CLUB_REQUEST: //公会加入申请
			return checkJoinRequestRemind(roleId);
		case GameValue.RED_POINT_TYPE_EQUIP_UPGRADE:
			return checkRedPointWhenLvUp(roleId,common);
		case GameValue.RED_POINT_TYPE_SOLDIER_UPGRADE:
			return checkRedPointWhenLvUp(roleId,common);
		case GameValue.RED_POINT_TYPE_STONE_NOTIFY:
			return checkRedPointWhenLvUp(roleId,common);
		case GameValue.RED_POINT_TYPE_PRESENT_ENERGY: //精力领取红点
			return checkPresentEnergy(roleId);
		case GameValue.RED_POINT_TYPE_MINE_SCENE:
			return checkMinePrize(roleId);
		case GameValue.RED_POINT_TYPE_MINE_LOG:
			return checkMineLog(roleId);
		default:
			break;
		}
		return DEFAULT;
	}
	
	/**
	 * 监听角色升级
	 * @param roleId
	 * @return
	 */
	private static byte checkRedPointWhenLvUp(int roleId,String common) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		if(common!=null&&common.equals("lvUp")){
			return LIGHT;
		}
		return DARK;
	}
	
		
		
	/**
	 * 监听角色公会加入
	 * @param roleId
	 * @return
	 */
	private static byte checkJoinRequestRemind(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			return DARK;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return DARK;
		}
		
		
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		if(roleClubInfo == null){
			return DARK;
		}
		
		RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleInfo.getId());
		if(memberInfo == null){
			return DARK;
		}
		
		if(memberInfo.getStatus() != RoleClubMemberInfo.CLUB_BOSS && 
				memberInfo.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT &&
				memberInfo.getStatus() != RoleClubMemberInfo.CLUB_LEADER){
			return DARK;
		}
		
		Map<Integer, RoleClubMemberInfo> map = RoleClubMemberInfoMap.getRoleClubMemberMap(roleClubInfo.getId());
		if(map == null){
			return DARK;
		}
		
		RoleClubMemberInfo info = null;
		for(Integer rId : map.keySet()){
			info = map.get(rId);
			if(info == null){
				continue;
			}
			
			if(info.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
				return LIGHT;
			}
		}
		
		
		return DARK;
	}

	/**
	 * 检查武将是否可升技能 监听：武将等级
	 * @param roleId
	 */
	private static byte checkHeroSkillUpgradeRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		//更新玩家技能点
		RoleService.timerRecoverTech(roleInfo);
		short tech = roleInfo.getTech();
		if(tech <= 0)
		{
			return DARK;
		}
		// 取得角色武将
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap == null) {
			return DEFAULT;
		}
		Iterator<Integer> heroMapIter = heroMap.keySet().iterator();
		HeroInfo heroInfo = null;
		while (heroMapIter.hasNext()) {
			heroInfo = heroMap.get(heroMapIter.next());
			if (heroInfo == null) {
				continue;
			}
			if(heroInfo.getDeployStatus() <= 1 || heroInfo.getDeployStatus() > 5)
			{
				continue;
			}
			if (0 == checkHeroSkillUpgradeRemind(roleInfo, heroInfo)) {
				continue;
			}
			return LIGHT;
		}
		return DARK;
	}
	
	/**
	 * 检查主角是否可升技能 监听：武将等级
	 * @param roleId
	 */
	private static byte checkRoleSkillUpgradeRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		// 取得角色武将
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (heroInfo == null) {
			return DEFAULT;
		}
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
		if (heroXMLInfo == null) {
			return DEFAULT;
		}
		Map<Integer, Integer> skillMap = HeroService.getSkillMap(heroInfo);
		if (skillMap == null) {
			return DEFAULT;
		}
		Iterator<Integer> iterator = heroXMLInfo.getSkillMap().keySet().iterator();
		while (iterator.hasNext()) {
			int heroSkillNo = iterator.next();
			HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(heroSkillNo);
			if (xmlSkill == null) {
				continue;
			}
			HeroSkillXMLInfo skillXml = HeroXMLInfoMap.getHeroSkillXMLInfo(xmlSkill.getSkillPos());
			if (skillXml == null) {
				continue;
			}
			Integer skillLevel = skillMap.get(heroSkillNo);
			if (skillLevel != null) {
				skillLevel = skillLevel + 1;
			} else {
				if (heroXMLInfo.getInitial() == 0) {
					if (heroInfo.getQuality() < skillXml.getOtherColorOpen()) {
						continue;
					}

				} else {
					if (heroInfo.getHeroLevel() < skillXml.getMainLvOpen()) {
						continue;
					}
				}
				skillLevel = 1;
			}
			HeroSkillXMLUpgrade skillUpXML = skillXml.getUpMap().get(skillLevel);
			if (skillUpXML == null) {
				continue;
			}
			if (heroInfo.getHeroLevel() < skillUpXML.getHeroLevel()) {
				continue;
			}
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			if (heroXMLInfo.getInitial() == 0) {
				conds.add(new MoneyCond(skillUpXML.getNeedOtherSilver()));
			} else {
				conds.add(new MoneyCond(skillUpXML.getNeedMainSilver()));
			}

			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
			if (check == 1) {
				return LIGHT;
			}

		}
		return DARK;
	}

	/**
	 * 检查特定武将是否可升技能
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
	private static byte checkHeroSkillUpgradeRemind(RoleInfo roleInfo, HeroInfo heroInfo) {
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
		if (heroXMLInfo == null) {
			return DEFAULT;
		}
		if (heroXMLInfo.getSkillMap() == null) {
			return DEFAULT;
		}
		Map<Integer, Integer> skillMap = HeroService.getSkillMap(heroInfo);
		if (skillMap == null) {
			return DEFAULT;
		}
		Iterator<Integer> iterator = heroXMLInfo.getSkillMap().keySet().iterator();
		while (iterator.hasNext()) {
			int heroSkillNo = iterator.next();
			HeroXMLSkill xmlSkill = heroXMLInfo.getSkillMap().get(heroSkillNo);
			if (xmlSkill == null) {
				continue;
			}
			HeroSkillXMLInfo skillXml = HeroXMLInfoMap.getHeroSkillXMLInfo(xmlSkill.getSkillPos());
			if (skillXml == null) {
				continue;
			}
			Integer skillLevel = skillMap.get(heroSkillNo);
			if (skillLevel != null) {
				skillLevel = skillLevel + 1;
			} else {
				if (heroXMLInfo.getInitial() == 0) {
					if (heroInfo.getQuality() < skillXml.getOtherColorOpen()) {
						continue;
					}

				} else {
					if (heroInfo.getHeroLevel() < skillXml.getMainLvOpen()) {
						continue;
					}
				}
				skillLevel = 1;
			}
			HeroSkillXMLUpgrade skillUpXML = skillXml.getUpMap().get(skillLevel);
			if (skillUpXML == null) {
				continue;
			}
			if (heroInfo.getHeroLevel() < skillUpXML.getHeroLevel()) {
				continue;
			}
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			if (heroXMLInfo.getInitial() == 0) {
				conds.add(new MoneyCond(skillUpXML.getNeedOtherSilver()));
			} else {
				conds.add(new MoneyCond(skillUpXML.getNeedMainSilver()));
			}

			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
			if (check != 1) {
				continue;
			}
			return LIGHT;
		}
		return DARK;
	}

	/**
	 * 检查人物身上是否有装备可穿戴、合成 监听：装备穿戴、材料变动、主将等级变动
	 * @param roleId
	 */
	private static byte checkEquipComposeRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		// 取得角色武将
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap == null) {
			return DEFAULT;
		}
		for(HeroInfo heroInfo : heroMap.values())
		{
			if(heroInfo.getDeployStatus() != 1)
			{
				//副将
				HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
				if(heroXMLInfo == null)
				{
					return DEFAULT;
				}
				//获得可穿戴的装备
				Map<Integer, HeroColorXMLUpCostItem> equipXMMap= new HashMap<Integer, HeroColorXMLUpCostItem>();
				HeroColorXMLUpCost colorXml =HeroXMLInfoMap.getHeroColorXMLUpCost(heroInfo.getQuality());
				if(colorXml != null)
				{
					equipXMMap = colorXml.getItemMap(heroXMLInfo.getAwakenType());
				}
				else
				{
					return DEFAULT;
				}
				if(equipXMMap!=null){
					EquipInfo equipInfo = null;
					HeroColorXMLUpCostItem item = null;
					for(int equipType : equipXMMap.keySet()){
						item = equipXMMap.get(equipType);
						equipInfo = EquipInfoMap.getHeroEquipbyType(heroInfo, equipType);
						if(equipInfo == null && item!=null ){	
							BagItemInfo bagItem = BagItemMap.getBagItembyNo(roleInfo, item.getItemNo());
							if(bagItem!=null && bagItem.getNum() >= item.getItemNum()){
								// 可以 穿戴
								return LIGHT;
							}else{
								int needNum= item.getItemNum();
								if(bagItem!=null){
									needNum = item.getItemNum()- bagItem.getNum();
								}
								// 获取合成需要的碎片
								boolean iscan = true;
								HashMap<Integer, Integer> delMap = new HashMap<Integer, Integer>();
								int getDel = ItemMgtService.getChipMap(roleInfo, item.getItemNo(), needNum, delMap);
								if (getDel == 1) {
									for (int itemNo : delMap.keySet()) {
										int itemNum = delMap.get(itemNo);
										if (!BagItemMap.checkBagItemNum(roleInfo, itemNo, itemNum)) {
											iscan = false;
											break;
										}
									}
								}else{
									iscan = false;
								}
								if(iscan){
									return LIGHT;
								}
							}
						}
					}
				}				
			}
		}
		
		return DARK;
	}

	/**
	 * 检查某人物身上是否有装备可以合成
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
	public static byte checkEquipComposeRemind(RoleInfo roleInfo, HeroInfo heroInfo) {
		HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHeroInfo == null) {
			return 0;
		}
		int mainHeroLv = mainHeroInfo.getHeroLevel();
		Map<Integer, EquipInfo> equipMap = EquipInfoMap.getHeroEquipMap(heroInfo);
		if (equipMap == null) {
			return 0;
		}
		Iterator<Integer> equipIter = equipMap.keySet().iterator();
		equip: while (equipIter.hasNext()) {
			EquipInfo equipInfo = equipMap.get(equipIter.next());
			if (equipInfo == null) {
				continue;
			}
			EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
			if (equipXMLInfo == null) {
				continue;
			}
			int composeEquipNo = equipXMLInfo.getComposeEquipNo();
			EquipXMLInfo composeEquipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(composeEquipNo);
			if (composeEquipXMLInfo == null) {
				continue;
			}
			EquipXMLInfo equipXmlInfo = EquipXMLInfoMap.getEquipXMLInfo(Integer.valueOf(composeEquipNo));
			if (equipXmlInfo == null) {
				continue;
			}
			// 判断主将等级
			if (composeEquipXMLInfo.getHeroLevel() > mainHeroLv) {
				continue;
			}
			// 判断材料是否够
			for (int itemNo : equipXMLInfo.getItemMap().keySet()) {
				int itemNum = equipXMLInfo.getItemMap().get(itemNo);
				if (!BagItemMap.checkBagItemNum(roleInfo, itemNo, itemNum)) {
					continue equip;
				}
			}
			return 1;
		}
		return 0;
	}

	/**
	 * 检查人物身上是否有装备可以强化 监听：武将等级
	 * @param roleId
	 */
	public static byte checkEquipUpgradeRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		// 取得角色武将
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap == null) {
			return DEFAULT;
		}
		Iterator<Integer> heroMapIter = heroMap.keySet().iterator();
		HeroInfo heroInfo = null;
		while (heroMapIter.hasNext()) {
			heroInfo = heroMap.get(heroMapIter.next());
			if(heroInfo == null || heroInfo.getDeployStatus() != 1){
				continue;
			}
			Map<Integer, EquipInfo> equipMap = EquipInfoMap.getHeroEquipMap(heroInfo);
			if (equipMap == null) {
				continue;
			}
			Iterator<Integer> equipIter = equipMap.keySet().iterator();
			while (equipIter.hasNext()) {
				EquipInfo equipInfo = equipMap.get(equipIter.next());
				if (equipInfo == null) {
					continue;
				}
				
				//检测装备强化材料
				if(isCanUp(roleInfo, equipInfo, heroInfo)){
					return LIGHT;
				}
				
				//检测装备精炼材料
				if(isCanRefine(roleInfo, equipInfo, heroInfo)){
					return LIGHT;
				}
				
			}
		}
		return DARK;
	}

	/**
	 * 检查某人物身上是否有装备可以强化
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
//	private static byte checkEquipUpgradeRemind(RoleInfo roleInfo, HeroInfo heroInfo) {
//		if(heroInfo.getDeployStatus() != 1){ //主将才可以强化
//			return 0;
//		}
//		
//		Map<Integer, EquipInfo> equipMap = EquipInfoMap.getHeroEquipMap(heroInfo);
//		if (equipMap == null) {
//			return 0;
//		}
//		
//		Iterator<Integer> equipIter = equipMap.keySet().iterator();
//		while (equipIter.hasNext()) {
//			EquipInfo equipInfo = equipMap.get(equipIter.next());
//			if (equipInfo == null) {
//				continue;
//			}
//			int level = equipInfo.getLevel() + 1;
//			EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo()); //判断Equip.xml是否包含该物品
//			if (equipXMLInfo == null) {
//				continue;
//			}
//			
//			EquipStrengthenConfigInfo upConfigInfo = EquipXMLInfoMap.getEquipStrengthenConfigInfo(equipXMLInfo.getQuality());
//			if(upConfigInfo == null){
//				continue;
//			}
//			
//			Map<Integer, EquipXMLUpgrade> equipXMLUpgradeMap = upConfigInfo.getEquipXMLUpgradeMap();
//			if(equipXMLUpgradeMap == null){
//				continue;
//			}
//			
//			EquipXMLUpgrade upgrade = equipXMLUpgradeMap.get(level);
//			if (upgrade == null) {
//				continue;
//			}
//			
//			if (heroInfo.getHeroLevel() < upgrade.getHeroLevel()) {
//				continue;
//			}
//			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
//			conds.add(new MoneyCond(upgrade.getExp() * GameValue.EQUIP_STRENGEH_RATE));
//			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
//			if (check != 1) {
//				continue;
//			}
//			return 1;
//		}
//		return 0;
//	}

	/**
	 * 检查是否有武将可以升星
	 * @param roleId
	 */
	private static byte checkHeroUpStar(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() != HeroInfo.DEPLOY_TYPE_MAIN) {
					HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
					if (heroXMLInfo == null) {
						continue;
					}
					int star = heroInfo.getStar();
					HeroStarXMLUpgrade currStarXML = heroXMLInfo.getStarMap().get(star);
					if (currStarXML == null) {
						continue;
					}
					int nextStar = star + 1;
					HeroStarXMLUpgrade nextStarXML = heroXMLInfo.getStarMap().get(nextStar);
					if (nextStarXML == null) {
						continue;
					}
					List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
					conds.add(new MoneyCond(nextStarXML.getNeedSilver()));
					int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
					if (check != 1) {
						continue;
					}
					if (BagItemMap.checkBagItemNum(roleInfo, heroXMLInfo.getChipNo(), nextStarXML.getChipNum())) {
						return LIGHT;
					}
				}
			}
		} else {
			return DEFAULT;
		}
		return DARK;
	}

	/**
	 * 检查是否有可招募的武将 监听：星石增减、武将获取
	 * @param roleId
	 */
	private static byte checkHeroRecruitRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		for (HeroXMLInfo heroXMLInfo : HeroXMLInfoMap.getHeroMap().values()) {
			if (heroXMLInfo.getRace() == 0) {// 去除主角英雄
				continue;
			}
			if (HeroInfoMap.getHeroInfoByNo(roleId, heroXMLInfo.getNo()) == null) {// 没有的英雄才出现在列表中
				int costSilver = 0;// 消耗的银子
				int costStarNum = heroXMLInfo.getChipNum();// 消耗的星石
//				// 消耗的银子和星石为小于初始星级的总和
//				for (HeroStarXMLUpgrade heroStarXMLUpgrade : heroXMLInfo.getStarMap().values()) {
//					if (heroStarXMLUpgrade.getNo() <= heroXMLInfo.getStar()) {
//						costSilver = costSilver + heroStarXMLUpgrade.getNeedSilver();
//						costStarNum = costStarNum + heroStarXMLUpgrade.getNeedChips();
//					}
//				}
				// 验证星石
				if (!BagItemMap.checkBagItemNum(roleInfo, heroXMLInfo.getChipNo(), costStarNum)) {
					continue;
				}
				// 验证银子
				List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
				conds.add(new MoneyCond(costSilver));
				int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
				if (check != 1) {
					continue;
				}
				return LIGHT;
			}
		}
		return DARK;
	}

	/**
	 * 检查武将能否升品质 监听：武将获取、物品变动
	 * @param roleId
	 */
	private static byte checkHeroColorUpgradeRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		// 取得角色武将
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		if (heroMap == null) {
			return DEFAULT;
		}
		Iterator<Integer> heroMapIter = heroMap.keySet().iterator();
		HeroInfo heroInfo = null;
		while (heroMapIter.hasNext()) {
			heroInfo = heroMap.get(heroMapIter.next());
			if (heroInfo == null) {
				continue;
			}
			if (0 == checkHeroColorUpgradeRemind(roleInfo, heroInfo)) {
				continue;
			}
			return LIGHT;
		}
		return DARK;
	}

	/**
	 * 检查特定武将能否升品质
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
	private static byte checkHeroColorUpgradeRemind(RoleInfo roleInfo, HeroInfo heroInfo) {
		HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
		if (heroXMLInfo == null) {
			return DEFAULT;
		}
		//主英雄不用判断
		if(heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN)
		{
			return DARK;
		}
		Map<Integer, HeroColourXMLUpgrade> upColorMap = heroXMLInfo.getColourMap();
		// 英雄是否进阶至顶级 英雄已进阶至顶级
		int currQuality = heroInfo.getQuality();
		int nextQuality = currQuality + 1;
		if (upColorMap.containsKey(currQuality) && !upColorMap.containsKey(nextQuality)) {
			return DARK;
		}
		HeroColorXMLUpCost upColorXml = HeroXMLInfoMap.getHeroColorXMLUpCost(currQuality);
		if (upColorXml == null) {
			return DARK;
		}
		HeroColorXMLUpCost nextColorXml = HeroXMLInfoMap.getHeroColorXMLUpCost(nextQuality);
		if (nextColorXml != null) {
			int limitLv = nextColorXml.getLevelLimit();
			if(heroInfo.getHeroLevel() < limitLv){
				return DARK;
			}
		}
		Map<Integer, EquipInfo> equipMap = heroInfo.getEquipMap();
		if (equipMap != null) {
			if(upColorXml.getItemMap().size() > equipMap.size()) {
				return DARK;
			} 
		} else {
			return DARK;
		}
		
		return 1;
	}

	/**
	 * 检查任务完成未领取 监听：任务完成、等级提升、任务奖励领取
	 * @param roleId
	 */
	private static byte checkMissionCompleteRemind(int roleId, byte questType) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}

		List<QuestInProgressInfo> quests = roleInfo.getQuestInfoMap().getRoleQuest();
		if (quests != null) {
			for (QuestInProgressInfo info : quests) {
				int questProtoNo = info.getQuestProtoNo();
				QuestProtoXmlInfo questProtoXmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
				if (questProtoXmlInfo == null) {
					continue;
				}
				switch (questType) {
				case GameValue.RED_POINT_TYPE_MISSION_COMPLETE_1:
					if (questProtoXmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_ACHI && 
						questProtoXmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_NORMAL && 
						questProtoXmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_BRANCH) {
						continue;
					}
					break;
				case GameValue.RED_POINT_TYPE_MISSION_COMPLETE_2:
					if (questProtoXmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_RACE) {
						continue;
					}
					break;
				case GameValue.RED_POINT_TYPE_MISSION_COMPLETE_3:
					if (questProtoXmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_DAILY && 
						questProtoXmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_RUN &&
						questProtoXmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_CARD &&
						questProtoXmlInfo.getQuestType() != QuestProtoXmlInfo.QUEST_TYPE_ACTIVE) {
						continue;
					}
					break;
				}
				// 判断任务是否完成
				if (info.getStatus() == QuestInProgressInfo.STATUS_REVEIVE) {

					// 判断是否显示，不显示，就算领取在身上也不能完成
					if (questProtoXmlInfo.isShowCheckCond()) {
						int ShowCondResult = AbstractConditionCheck.check(questProtoXmlInfo.getQuestConds(), roleInfo, null, 0, null);
						if (ShowCondResult != 1) {
							continue;
						}
					}

					// 检测完成
					int finish = AbstractConditionCheck.check(questProtoXmlInfo.getFinishConds(), roleInfo, info, 0, null);
					if(finish == 1)
					{
						return LIGHT;
					}
				}

				if (info.getStatus() == QuestInProgressInfo.STATUS_FINISH) {
					
					// 判断是否显示，不显示，就算领取在身上也不能完成
					if (questProtoXmlInfo.isShowCheckCond()) {
						int ShowCondResult = AbstractConditionCheck.check(questProtoXmlInfo.getQuestConds(), roleInfo, null, 0, null);
						if (ShowCondResult != 1) {
							continue;
						}
					}
					
					if(questProtoXmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_NORMAL)
					{
						String finishCondStr = questProtoXmlInfo.getFinishCondStr();
						if(finishCondStr.startsWith("visit"))
						{
							if(info.getTalkOrder() == 2)
							{
								return LIGHT;
							}
						}
						else
						{
							if(info.getTalkOrder() == 1)
							{
								return LIGHT;
							}
						}

					}
//					else if (questProtoXmlInfo.getQuestType() == QuestProtoXmlInfo.QUEST_TYPE_BRANCH) 
//					{
//						if(info.getTalkOrder() != 0)
//						{
//							return LIGHT;
//						}
//					}
					else
					{
						return LIGHT;
					}
				}
			}
		}
		return DARK;
	}

	/**
	 * 检查武将装备是否可镶嵌宝石(重做)
	 * @param roleId
	 */
//	private static byte checkEquipAddStoneRemind(int roleId) {
//		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
//			return DEFAULT;
//		}
//		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
//		if (roleInfo == null) {
//			return DEFAULT;
//		}
//		// 获取所有取得钻石类型
//		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
//		if(roleLoadInfo == null) {
//			return DEFAULT;
//		}
//		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
//		if (mainHero == null) {
//			return DEFAULT;
//		}
//		
//		int heroLV = mainHero.getHeroLevel();
//		//最大开启
//		int pos = 0;
//		if (heroLV >= GameValue.WEAPON_POS_1_LV) {
//			pos = 1;
//		} else if (heroLV >= GameValue.WEAPON_POS_2_LV) {
//			pos = 2;
//		} else if (heroLV >= GameValue.WEAPON_POS_3_LV) {
//			pos = 3;
//		} else if (heroLV >= GameValue.WEAPON_POS_4_LV) {
//			pos = 4;
//		} else if (heroLV >= GameValue.WEAPON_POS_5_LV) {
//			pos = 5;
//		} else if (heroLV >= GameValue.WEAPON_POS_6_LV) {
//			pos = 6;
//		}
//		
//		
//		Map<Integer, RoleWeaponInfo> weaponMap = roleLoadInfo.getRoleWeaponInfoMap();
//		
//		
//		
//		Map<Integer, Integer> stoneTypesInBag = new HashMap<Integer, Integer>();
//		Map<Integer, BagItemInfo> bagMap = BagItemMap.getBagItem(roleInfo);
//		PropXMLInfo stoneXMLInfo = null;
//		if (bagMap != null) {
//			Map<Integer, BagItemInfo> itemMap = new HashMap<Integer, BagItemInfo>();
//			itemMap.putAll(bagMap);
//			Iterator<Integer> bagIterator = itemMap.keySet().iterator();
//			BagItemInfo bagItemInfo = null;
//			while (bagIterator.hasNext()) {
//				bagItemInfo = itemMap.get(bagIterator.next());
//				if (bagItemInfo == null) {
//					continue;
//				}
//				if (bagItemInfo.getItemType() == BagItemInfo.TYPE_STONE && bagItemInfo.getNum() > 0) {
//					stoneXMLInfo = PropXMLInfoMap.getPropXMLInfo(bagItemInfo.getItemNo());
//					if (stoneXMLInfo == null) {
//						continue;
//					}
//					stoneTypesInBag.put(stoneXMLInfo.getSubType(), 1);
//				}
//			}
//		}
//		if (stoneTypesInBag.size() == 0) {
//			return DARK;
//		}
//		// 取得角色武将
//		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
//		if (heroMap == null) {
//			return DEFAULT;
//		}
//		Iterator<Integer> heroMapIter = heroMap.keySet().iterator();
//		HeroInfo heroInfo = null;
//		while (heroMapIter.hasNext()) {
//			heroInfo = heroMap.get(heroMapIter.next());
//			if (heroInfo == null) {
//				continue;
//			}
//			Map<Integer, EquipInfo> equipBaseMap = EquipInfoMap.getHeroEquipMap(heroInfo);
//			if (equipBaseMap == null) {
//				continue;
//			}
//			Map<Integer, EquipInfo> equipMap = new HashMap<Integer, EquipInfo>();
//			equipMap.putAll(equipBaseMap);
//
//			Iterator<Integer> equipIter = equipMap.keySet().iterator();
//			while (equipIter.hasNext()) {
//				EquipInfo equipInfo = equipMap.get(equipIter.next());
//				if (equipInfo == null) {
//					continue;
//				}
//				Map<Integer, Integer> stoneTypes = new HashMap<Integer, Integer>();
//				for (int i = 1; i <= GameValue.STONE_INLAY_MAX_NUM; i++) {
//					int stoneNo = equipInfo.getEquipStoneNoBySeat(i);
//					if (stoneNo != 0) {
//						stoneXMLInfo = PropXMLInfoMap.getPropXMLInfo(stoneNo);
//						if (stoneXMLInfo == null) {
//							continue;
//						}
//						stoneTypes.put(stoneXMLInfo.getSubType(), 1);
//
//					}
//				}
//				if (stoneTypes.size() >= GameValue.STONE_INLAY_MAX_NUM) {
//					return DARK;
//				}
//				stoneBagTypeLoop: for (long stoneBagType : stoneTypesInBag.keySet()) {
//					for (long stoneType : stoneTypes.keySet()) {
//						if (stoneBagType == stoneType) {
//							continue stoneBagTypeLoop;
//						}
//					}
//					return LIGHT;
//				}
//			}
//		}
//		return DARK;
//	}

	/**
	 * 检查武将装备是否可镶嵌宝石
	 * @param roleInfo
	 * @param heroInfo
	 * @return
	 */
	public static byte checkEquipAddStoneRemind(RoleInfo roleInfo, HeroInfo heroInfo) {
		Map<Integer, EquipInfo> equipBaseMap = EquipInfoMap.getHeroEquipMap(heroInfo);
		if (equipBaseMap == null) {
			return 0;
		}
		// 获取所有取得钻石类型
		Map<Integer, Integer> stoneTypesInBag = new HashMap<Integer, Integer>();
		Map<Integer, BagItemInfo> bagMap = BagItemMap.getBagItem(roleInfo);
		PropXMLInfo stoneXMLInfo = null;
		if (bagMap != null) {
			Map<Integer, BagItemInfo> itemMap = new HashMap<Integer, BagItemInfo>();
			itemMap.putAll(bagMap);
			Iterator<Integer> bagIterator = itemMap.keySet().iterator();
			BagItemInfo bagItemInfo = null;
			while (bagIterator.hasNext()) {
				bagItemInfo = itemMap.get(bagIterator.next());
				if (bagItemInfo == null) {
					continue;
				}
				if (bagItemInfo.getItemType() == BagItemInfo.TYPE_STONE && bagItemInfo.getNum() > 0) {
					stoneXMLInfo = PropXMLInfoMap.getPropXMLInfo(bagItemInfo.getItemNo());
					if (stoneXMLInfo == null) {
						continue;
					}
					stoneTypesInBag.put(stoneXMLInfo.getSubType(), 1);
				}
			}
		}
		if (stoneTypesInBag.size() == 0) {
			return 0;
		}
		Map<Integer, EquipInfo> equipMap = new HashMap<Integer, EquipInfo>();
		equipMap.putAll(equipBaseMap);
		Iterator<Integer> equipIter = equipMap.keySet().iterator();
		while (equipIter.hasNext()) {
			EquipInfo equipInfo = equipMap.get(equipIter.next());
			if (equipInfo == null) {
				continue;
			}
			Map<Integer, Integer> stoneTypes = new HashMap<Integer, Integer>();
			for (int i = 1; i <= GameValue.STONE_INLAY_MAX_NUM; i++) {
				int stoneNo = equipInfo.getEquipStoneNoBySeat(i);
				if (stoneNo != 0) {
					stoneXMLInfo = PropXMLInfoMap.getPropXMLInfo(stoneNo);
					if (stoneXMLInfo == null) {
						continue;
					}
					stoneTypes.put(stoneXMLInfo.getSubType(), 1);

				}
			}
			if (stoneTypes.size() >= GameValue.STONE_INLAY_MAX_NUM) {
				return 0;
			}
			for (int stoneBagType : stoneTypesInBag.keySet()) {
				if (stoneTypes.containsKey(stoneBagType)) {
					continue;
				}
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 检查普通商店是否刷新
	 * @param roleId
	 */
//	private static byte checkShopRefreshRemind(int roleId, int storeType) {
//		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
//			return DEFAULT;
//		}
//		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
//		if (roleInfo == null) {
//			return DEFAULT;
//		}
//		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
//		if (roleLoadInfo == null) {
//			return DEFAULT;
//		}
//		long now = System.currentTimeMillis();
//		ShopXMLInfo xmlInfo = ShopXMLInfoMap.getShopXMLInfo(StoreItemInfo.STORE_TYPE_4);
//		if (xmlInfo == null) {
//			return DEFAULT;
//		}
//		String autoRefreshTime = xmlInfo.getAutoRefreshTime();
//		long todayRefreshTime = DateUtil.getTodayHMTime(autoRefreshTime);// HH：mm
//		switch (storeType) {
//		case StoreItemInfo.STORE_TYPE_1:
//			if (roleLoadInfo.getLastCourageTime() == null
//					|| roleLoadInfo.getLastCourageTime().getTime() == (new Timestamp(0)).getTime()
//					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getLastCourageTime().getTime())
//					|| (now < todayRefreshTime && todayRefreshTime - 24 * 3600 * 1000l > roleLoadInfo
//							.getLastCourageTime().getTime())) {
//				return LIGHT;
//			}
//			return DARK;
//		case StoreItemInfo.STORE_TYPE_2:
//			if (roleLoadInfo.getLastJusticeTime() == null
//					|| roleLoadInfo.getLastJusticeTime().getTime() == (new Timestamp(0)).getTime()
//					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getLastJusticeTime().getTime())
//					|| (now < todayRefreshTime && todayRefreshTime - 24 * 3600 * 1000l > roleLoadInfo
//							.getLastJusticeTime().getTime())) {
//				return LIGHT;
//			}
//			return DARK;
//		case StoreItemInfo.STORE_TYPE_3:
//			if (roleLoadInfo.getLastDevoteTime() == null
//					|| roleLoadInfo.getLastDevoteTime().getTime() == (new Timestamp(0)).getTime()
//					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getLastDevoteTime().getTime())
//					|| (now < todayRefreshTime && todayRefreshTime - 24 * 3600 * 1000l > roleLoadInfo
//							.getLastDevoteTime().getTime())) {
//				return LIGHT;
//			}
//			return DARK;
//		case StoreItemInfo.STORE_TYPE_4:
//			String[] refreshStr = autoRefreshTime.split(",");
//			long[] refreshTimes = new long[refreshStr.length];
//			int num = 0;
//			for (int i = 0; i < refreshStr.length; i++) {
//				refreshTimes[i] = DateUtil.getTodayHMTime(refreshStr[i]);
//				if (now >= DateUtil.getTodayHMTime(refreshStr[i])) {
//					num++;
//				}
//			}
//			if (roleLoadInfo.getAutoRefreNum() == 0 // 新玩家
//					|| roleLoadInfo.getAutoRefreNum() < num // 当天刷新未满
//					|| (roleLoadInfo.getAutoRefreNum() < refreshTimes.length && !DateUtil.isSameDay(now, roleLoadInfo
//							.getLastNormalTime().getTime())) // 当天刷新未满,到第二天
//					|| (roleLoadInfo.getAutoRefreNum() == refreshTimes.length && num >= 1 && !DateUtil.isSameDay(now,
//							roleLoadInfo.getLastNormalTime().getTime())))// 当天刷新已满,到第二天
//			{
//				return LIGHT;
//			}
//			return DARK;
//		case StoreItemInfo.STORE_TYPE_5:
//			if (roleLoadInfo.getKuafuAutoTime() == null
//					|| roleLoadInfo.getKuafuAutoTime().getTime() == (new Timestamp(0)).getTime()
//					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getKuafuAutoTime().getTime())
//					|| (now < todayRefreshTime && todayRefreshTime - 24 * 3600 * 1000l > roleLoadInfo
//							.getKuafuAutoTime().getTime())) {
//				return LIGHT;
//			}
//			return DARK;
//		case StoreItemInfo.STORE_TYPE_6:
//			if (roleLoadInfo.getExploitAutoTime() == null
//					|| roleLoadInfo.getExploitAutoTime().getTime() == (new Timestamp(0)).getTime()
//					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getExploitAutoTime().getTime())
//					|| (now < todayRefreshTime && todayRefreshTime - 24 * 3600 * 1000l > roleLoadInfo
//							.getExploitAutoTime().getTime())) {
//				return LIGHT;
//			}
//			return DARK;
//		case StoreItemInfo.STORE_TYPE_7:
//			if (roleLoadInfo.getGoldShopAutoTime() == null
//					|| roleLoadInfo.getGoldShopAutoTime().getTime() == (new Timestamp(0)).getTime()
//					|| (now >= todayRefreshTime && todayRefreshTime > roleLoadInfo.getGoldShopAutoTime().getTime())
//					|| (now < todayRefreshTime && todayRefreshTime - 24 * 3600 * 1000l > roleLoadInfo
//							.getGoldShopAutoTime().getTime())) {
//				return LIGHT;
//			}
//			return DARK;
//		}
//		return DEFAULT;
//	}

	/**
	 * 监控是否竞技场被人打了
	 * @param roleId
	 */
	private static byte checkArenaChallengedRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		FightArenaInfo arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleId);
		if (arenaInfo == null) {
			return DEFAULT;
		}
		if (arenaInfo.getLogs() == null) {
			return DEFAULT;
		}
		// 获取上一次查看时间
		long arenaLogCheckTime = 0;
		if (roleInfo.getArenaLogCheckTime() == 0 && roleInfo.getLogoutTime() != null) {
			roleInfo.setArenaLogCheckTime(roleInfo.getLogoutTime().getTime());
		}
		arenaLogCheckTime = roleInfo.getArenaLogCheckTime();

		for (RoleArenaLog info : arenaInfo.getLogs()) {
			if (roleId == info.getDefendRoleId() && info.getBeginTime().getTime() >= arenaLogCheckTime) {
				return LIGHT;
			}
		}
		return DARK;
	}

	/**
	 * 监控4个历练活动次数是否有剩余
	 * @param roleId
	 */
	private static byte checkExperienceRemind(int roleId, FightType fightType) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null)
		{
			return DEFAULT;
		}
		switch (fightType) {
		case FIGHT_TYPE_4://练兵
			int dayofWeek = DateUtil.getDayofWeek();
			int num = 0;
			if(dayofWeek == 1){
				dayofWeek = 7;
			} else {
				dayofWeek--;
			}
			// 判断是否活动开启
			if(GameValue.ACTIVITY_EXP_TYPE1.contains(String.valueOf(dayofWeek))){
				num = num + roleInfo.getExpLeftTimes1();
			}
			if(GameValue.ACTIVITY_EXP_TYPE2.contains(String.valueOf(dayofWeek))){
				num = num + roleInfo.getExpLeftTimes2();
			}
			if(GameValue.ACTIVITY_EXP_TYPE3.contains(String.valueOf(dayofWeek))){
				num = num + roleInfo.getExpLeftTimes3();
			}
			if(GameValue.ACTIVITY_EXP_TYPE4.contains(String.valueOf(dayofWeek))){
				num = num + roleInfo.getExpLeftTimes4();
			}
			if(GameValue.ACTIVITY_EXP_TYPE5.contains(String.valueOf(dayofWeek))){
				num = num + roleInfo.getExpLeftTimes5();
			}
			if(GameValue.ACTIVITY_EXP_TYPE6.contains(String.valueOf(dayofWeek))){
				num = num + roleInfo.getExpLeftTimes6();
			}
			if (num > 0) {
				return LIGHT;
			}
			return DARK;
		case FIGHT_TYPE_6://攻城略地
			FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleId);
			if (info == null) {
				return DEFAULT;
			}
			if (info.getCurrResetNum() >= info.getCurrResetLimit()) {
				return DARK;
			}
			return LIGHT;
		case FIGHT_TYPE_11:	//对攻
			//没玩过对攻
			if(roleLoadInfo.getLastAttackAnotherTime()==null)
			{
				return LIGHT;
			}
			if(!DateUtil.isSameDay(roleLoadInfo.getLastAttackAnotherTime().getTime(), System.currentTimeMillis())&&
					roleLoadInfo.getAttackAnotherTime()>0){
				//隔天了并且有玩的次数就重置玩的次数
				synchronized (roleInfo) {
					Timestamp now = new Timestamp(System.currentTimeMillis());
					if(RoleDAO.getInstance().updateRoleAttackAnotherInfo(roleId, (byte)0, now)){
						roleLoadInfo.setAttackAnotherTime((byte)0);
						roleLoadInfo.setLastAttackAnotherTime(new Timestamp(System.currentTimeMillis()));
					}else{
						return DEFAULT;
					}
				}
			}
			int XLXFNum = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXFDB_NUM)+GameValue.ATTACK_ANOTHER_EVERY_DAY_TIME;
			if(roleLoadInfo.getAttackAnotherTime() < XLXFNum)
			{
				return LIGHT;
			}
			
			return DARK;
		case FIGHT_TYPE_12:	//防守
			//没玩过防守
			if(roleLoadInfo.getLastDefendTime()==null)
			{
				return LIGHT;
			}
			if(!DateUtil.isSameDay(roleLoadInfo.getLastDefendTime().getTime(), System.currentTimeMillis())&&roleLoadInfo.getDefendTime()>0)
			{
				//隔天了并且有玩的次数就重置玩的次数(防止打开界面的时候刚好跨天了)
				if(RoleDAO.getInstance().updateRoleDefendTime(roleInfo.getId(), (byte)0, roleLoadInfo.getLastDefendTime()))
				{
					roleLoadInfo.setDefendTime((byte)0);
				}
				else
				{
					return DEFAULT;
				}
			}
			int num1 = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.BLJDB_NUM);
			if(roleLoadInfo.getDefendTime() < num1)
			{
				return LIGHT;
			}
			return DARK;
		default:
			break;
		}
		return DEFAULT;
	}

	/**
	 * 检查签到及奖品领取情况
	 * @param roleId
	 * @param checkType
	 */
	private static byte checkCheckinRemind(int roleId, byte checkType) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return DEFAULT;
		}
		switch (checkType) {
		case GameValue.RED_POINT_TYPE_CHECK_MONTH:
			// 功能开启但第一次还未开界面
			if (roleLoadInfo.getLastCheckInTime() == null) {
				return LIGHT;
			}
			
			Map<Integer,Integer> checkedMap = CheckInService.generateCheckedMap(roleLoadInfo.getCheckInStr());
			if (!checkedMap.containsKey(roleLoadInfo.getCurrCheckDay())) {
				return LIGHT;
			}
			
			// 签到月末大奖
			if (roleLoadInfo.getCurrCheckDay() == 30 && checkedMap.size() == 30) {
				return LIGHT;
			}
			
			// vip签到红点
			Map<Integer,Integer> vipCheckedMap = CheckInService.generateCheckedMap(roleLoadInfo.getVipCheckInStr());
			for (int i = 1; i <= roleLoadInfo.getCurrCheckDay(); i++) {
				CheckInPrizeXMLInfo xmlInfo = CheckInPrizeXmlMap.getXmlInfo(i);
				if (xmlInfo == null) {
					continue;
				}
				
				if (roleInfo.getVipLv() < xmlInfo.getNeedVipLv()) {
					continue;
				}
				
				// 往日没签的vip奖励也不能领
				if (i < roleLoadInfo.getCurrCheckDay() && !checkedMap.containsKey(i)) {
					continue;
				}
				
				if (!vipCheckedMap.containsKey(xmlInfo.getNo())) {
					return LIGHT;
				}
			}
			
			// vip月末大奖
			if (checkedMap.size() >= 30 && !vipCheckedMap.containsKey(CheckInPrizeXmlMap.CHECK_ALL_PRIZE_NO)) {
				CheckInPrizeXMLInfo xmlInfo = CheckInPrizeXmlMap.getXmlInfo(CheckInPrizeXmlMap.CHECK_ALL_PRIZE_NO);
				if (roleInfo.getVipLv() >= xmlInfo.getNeedVipLv()) {
					return LIGHT;
				}
			}
			
			return DARK;
		case GameValue.RED_POINT_TYPE_CHECK_WEEK:
			List<QueryCheckIn7DayListItemRe> queryCheckIn7DayListItemRes = CheckInService
					.getCheckIn7DayListItem(roleInfo);
			if (queryCheckIn7DayListItemRes != null) {
				for (QueryCheckIn7DayListItemRe queryCheckIn7DayListItemRe : queryCheckIn7DayListItemRes) {
					if (queryCheckIn7DayListItemRe.getDay() <= roleLoadInfo.getCheckIn7DayMaxLoginDays()
							&& queryCheckIn7DayListItemRe.getCheckIn() == 0) {
						return LIGHT;
					}
				}
			}
			return DARK;
		case GameValue.RED_POINT_TYPE_LEVEL_GIFT:
			if (CheckInMgtService.checkIsGetLevelGift(roleInfo, roleLoadInfo)) {
				return LIGHT;
			} else {
				return DARK;
			}
		}
		return DEFAULT;
	}

	/**
	 * 检查是否有免费抽奖
	 * @param roleId
	 */
	private static byte checkFreeChestRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return DEFAULT;
		}
		int[] checkTypes = new int[] { 1, 3, 7 };
		long now = System.currentTimeMillis();
		for (int checkType : checkTypes) {
			if(checkType == 1)
			{
				byte recruitNum = 0;// 今天银子抽卡次数
				int freeTime = GameValue.RECRUIT_FREE_MONEY_TIME;// 金子抽卡免费间隔时间
				long lastTime = 0;
				if (roleLoadInfo.getLastRecruitMoneyTime() != null
						&& DateUtil.isSameDay(System.currentTimeMillis(), roleLoadInfo.getLastRecruitMoneyTime()
								.getTime())) {
					// 判断今天银子抽卡次数
					recruitNum = roleLoadInfo.getRecruitMoneyNum();
				}
				if (roleLoadInfo.getLastRecruitMoneyTime() != null) {
					lastTime = roleLoadInfo.getLastRecruitMoneyTime().getTime();
				}
				if (recruitNum < GameValue.RECRUIT_FREE_lIMIT
						&& lastTime + freeTime * 60 * 1000L <= now) {
					return LIGHT;
				}
			}
			else if(checkType == 3)
			{
				boolean isFree = false;
				int freeTime = GameValue.RECRUIT_FREE_COIN_TIME;// 金子抽卡免费间隔时间
				if (roleLoadInfo.getLastRecruitCoinTime() == null) {
					isFree = true;
				} else if (roleLoadInfo.getLastRecruitCoinTime().getTime() + freeTime * 60 * 60 * 1000L <= now) {
					isFree = true;
				}
				if (isFree) {
					return LIGHT;
				}
			}
			else if(checkType == 7)
			{
				boolean isFree = false;
				int freeTime = GameValue.RECRUIT_FREE_HERO_TIME;// 英雄抽卡免费间隔时间
				if (roleLoadInfo.getLastRecruitHeroFreeTime() == null) {
					isFree = true;
				} else if (roleLoadInfo.getLastRecruitHeroFreeTime().getTime() + freeTime * 60 * 60 * 1000L <= now) {
					isFree = true;
				}
				if (isFree) {
					return LIGHT;
				}
			}
		}
		return DARK;
	}

	/**
	 * 检测红点是否有变化
	 * @param roleId
	 * @param redPointMap
	 * @return
	 */
	private static boolean checkRedPoint(int roleId, Map<Byte, Byte> redPointMap) {
		boolean flag = false;
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return flag;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return flag;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null) {
			return flag;
		}
		
		byte[] lastRedPoint = roleLoadInfo.getRedPoint();
		if(lastRedPoint.length <= 0){
			return flag;
		}

		Set<Byte> redPointSet = redPointMap.keySet();
		for(Byte key : redPointSet){
			byte i = redPointMap.get(key);
			if(i != lastRedPoint[key - 1]){
				lastRedPoint[key - 1] = i;
				flag = true;
			}
		}
		//变化 改变缓存
		if(flag){
			roleLoadInfo.setRedPoint(lastRedPoint);
		}
		return flag;
		
	}
	

	/**
	 * 推送红点信息
	 * @param roleId
	 * @param redPointMap 若为null则全部推送
	 */
	public static void pop(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null) {
			return;
		}
		int redPoint = 0;
		byte[] redPoints = roleLoadInfo.getRedPoint();
		if(redPoints.length <= 0){
			return;
		}
		redPoint = StringToInteger(redPoints);
		//推送r
		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		if (session != null && session.isConnected()) {
			PopRedPointResp pointResp = new PopRedPointResp();
			pointResp.setResult(1);
			pointResp.setRedPoint(redPoint);
			Message message = new Message();
			GameMessageHead head = new GameMessageHead();

			head.setMsgType(Command.USER_RED_POINT_RESP);
			head.setUserID0((int) roleId);
			message.setHeader(head);
			message.setBody(pointResp);
			if (session != null && session.isConnected()) {
				session.write(message);
				// logger.info("-- push red point! type : " + type +
				// ", status : " + status);
			}
		}
	}

	/**
	 * 检测武将红点改变
	 * @param roleId
	 */
//	public static byte checkHeroRedPoint(int roleId) {
//		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
//			return 0;
//		}
//		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
//		if (roleInfo == null) {
//			return 0;
//		}
//		Map<Integer, HeroInfo> heroInfoMap = HeroInfoMap.getHeroByRoleId(roleId);
//		if (heroInfoMap == null) {
//			return 0;
//		}
//		Iterator<Integer> heroIterator = heroInfoMap.keySet().iterator();
//		HeroInfo heroInfo = null;
//		byte i = 0;
////		PopRedPointHeroResp popRedPointHeroResp = new PopRedPointHeroResp();
////		PopRedPointHeroInfoRe popRedPointHeroInfoRe = null;
//		while (heroIterator.hasNext()) {
//			heroInfo = heroInfoMap.get(heroIterator.next());
//			if (heroInfo == null) {
//				continue;
//			}
//
//			i = RedPointMgtService.checkHeroSkillUpgradeRemind(roleInfo, heroInfo);
//			if(i == 1){
//				break;
//			}
//			i = RedPointMgtService.checkHeroColorUpgradeRemind(roleInfo, heroInfo);
//			if(i == 1){
//				break;
//			}
//			i = RedPointMgtService.checkEquipUpgradeRemind(roleInfo, heroInfo);
//			if(i == 1){
//				break;
//			}
//			
////			popRedPointHeroInfoRe = new PopRedPointHeroInfoRe();
////			popRedPointHeroInfoRe.setHeroId(heroInfo.getId());
////			// popRedPointHeroInfoRe.setCanUpgradeStar(RedPointMgtService.checkHeroStarUpgradeRemind(roleInfo,
////			// heroInfo));
////			popRedPointHeroInfoRe
////					.setCanUpgradeSkill(RedPointMgtService.checkHeroSkillUpgradeRemind(roleInfo, heroInfo));
////			popRedPointHeroInfoRe
////					.setCanUpgradeColor(RedPointMgtService.checkHeroColorUpgradeRemind(roleInfo, heroInfo));
////			popRedPointHeroInfoRe.setCanUpgradeEquip(RedPointMgtService.checkEquipUpgradeRemind(roleInfo, heroInfo));
////			/*popRedPointHeroInfoRe.setCanMakeStoneOnEquip(RedPointMgtService
////					.checkEquipAddStoneRemind(roleInfo, heroInfo));
////			popRedPointHeroInfoRe.setCanComposeEquip(RedPointMgtService.checkEquipComposeRemind(roleInfo, heroInfo));*/
////
////			popRedPointHeroResp.getHeroInfoRes().add(popRedPointHeroInfoRe);
//		}
//		
//		if(i == 1){
//			return 1;
//		}
////		if (popRedPointHeroResp.getHeroInfoRes().size() > 0) {
////			popForHero(popRedPointHeroResp, roleId);
////		}
//		return 0;
//	}

	/**
	 * 推送武将红点信息
	 * @param popRedPointHeroResp
	 * @param roleId
	 */
//	private static boolean popForHero(PopRedPointHeroResp popRedPointHeroResp, int roleId) {
//		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
//			return false;
//		}
//		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
//		if (roleInfo == null) {
//			return false;
//		}
//		// 过滤重复
//		Map<Long, PopRedPointHeroInfoRe> heroRedPointCacheMap = roleInfo.getHeroRedPointCacheMap();
//		for (int i = popRedPointHeroResp.getHeroInfoRes().size() - 1; i >= 0; i--) {
//			PopRedPointHeroInfoRe pointHeroInfoRe = popRedPointHeroResp.getHeroInfoRes().get(i);
//			if (heroRedPointCacheMap.containsKey(pointHeroInfoRe.getHeroId())) {
//				PopRedPointHeroInfoRe heroInfoReCache = heroRedPointCacheMap.get(pointHeroInfoRe.getHeroId());
//				if (heroInfoReCache != null) {
//					if (/*heroInfoReCache.getCanComposeEquip() == pointHeroInfoRe.getCanComposeEquip()*/
//							/*&& heroInfoReCache.getCanMakeStoneOnEquip() == pointHeroInfoRe.getCanMakeStoneOnEquip()
//							&&*/ heroInfoReCache.getCanUpgradeColor() == pointHeroInfoRe.getCanUpgradeColor()
//							&& heroInfoReCache.getCanUpgradeEquip() == pointHeroInfoRe.getCanUpgradeEquip()
//							&& heroInfoReCache.getCanUpgradeSkill() == pointHeroInfoRe.getCanUpgradeSkill()
//							/*&& heroInfoReCache.getCanUpgradeStar() == pointHeroInfoRe.getCanUpgradeStar()*/) {
//						popRedPointHeroResp.getHeroInfoRes().remove(pointHeroInfoRe);
//						continue;
//					}
//				}
//			}
//			heroRedPointCacheMap.put(pointHeroInfoRe.getHeroId(), pointHeroInfoRe);
//		}
//		popRedPointHeroResp.setHeroInfoResSize(popRedPointHeroResp.getHeroInfoRes().size());
//		if (popRedPointHeroResp.getHeroInfoRes().size() == 0) {
//			return;
//		}
//		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
//		if (session != null && session.isConnected()) {
//			Message message = new Message();
//			GameMessageHead head = new GameMessageHead();
//
//			head.setMsgType(Command.HERO_RED_POINT_RESP);
//			head.setUserID0((int) roleId);
//			message.setHeader(head);
//			message.setBody(popRedPointHeroResp);
//			if (session != null && session.isConnected()) {
//				session.write(message);
//				// logger.info("-- push hero red point! roleId : " + roleId);
//			}
//		}
//	}

	/**
	 * 判断开启等级
	 * @param roleInfo
	 * @param type
	 * @return
	 */
	private static boolean checkCondition(RoleInfo roleInfo, byte type) {
		FuncOpenXMLInfo funcOpenXMLInfo = null;
		if (roleInfo == null) {
			return false;
		}
		switch (type) {
		case GameValue.RED_POINT_TYPE_ARENA_CHALLENGED:
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(1);
			break;
		case GameValue.RED_POINT_TYPE_ARENA_PRIZE:
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(3);
			break;
		case GameValue.RED_POINT_TYPE_EXPERIENCE_1:
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(7);
			break;
		case GameValue.RED_POINT_TYPE_EXPERIENCE_2:
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(9);
			break;
		case GameValue.RED_POINT_TYPE_EXPERIENCE_3:
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(5);
			break;
		case GameValue.RED_POINT_TYPE_EXPERIENCE_4:
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(8);
			break;
		case GameValue.RED_POINT_TYPE_CHECK_MONTH:
			// 月签到
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(14);
			break;
		case GameValue.RED_POINT_TYPE_SOLDIER_UPGRADE:
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(12);
			break;
		case GameValue.RED_POINT_TYPE_STONE_NOTIFY:
			funcOpenXMLInfo = FuncOpenXMLInfoMap.getMap().get(19);
			break;
		default :
			return true;
		}
		if (funcOpenXMLInfo == null) {
			return false;
		}
		return 1 == AbstractConditionCheck.checkCondition(roleInfo, funcOpenXMLInfo.getCheckConds());
	}

	/**
	 * 校验是否有必要检查红点
	 * @return
	 */
	private static boolean validateCheckable(int roleId, byte type, String message) {
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return false;
		}
		
		// 判断开启等级
		boolean checkLevel = checkCondition(roleInfo, type);
		if(!checkLevel) {
			return checkLevel;
		}
		return true;
		
	}
	
	/**
	 * 红点检测判断装备是否可以精炼
	 * @param roleInfo
	 * @param equipInfo
	 * @param heroInfo
	 * @return
	 */
	private static boolean isCanRefine(RoleInfo roleInfo, EquipInfo equipInfo, HeroInfo heroInfo){
		int nextRefineLv = equipInfo.getRefineLv() + 1;
		EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
		if(equipXMLInfo == null){
			return false;
		}
		
		EquipRefineInfo equipRefineInfo = equipXMLInfo.getRefineMap().get(nextRefineLv);
		if(equipRefineInfo == null){
			return false;
		}
		
		if(equipRefineInfo.getLimitLv() > heroInfo.getHeroLevel()){
			return false;
		}
		
		int consumeNum = equipRefineInfo.getConsume(); // 需要消耗的装备个数
		boolean isEnough = false; // 判断是否足够的标识
		
		if(consumeNum > 0){
			int counter = 0;
			for (Map.Entry<Integer, EquipInfo> bagEquipInfo : EquipInfoMap.getBagEquipMap(roleInfo.getId()).entrySet())
			{
				if (bagEquipInfo.getValue().getEquipNo() == equipInfo.getEquipNo() && equipInfo.getId() != bagEquipInfo.getValue().getId())
				{
					counter++;

					if (consumeNum == counter)
					{
						// 到达满足的数量后停止循环
						isEnough = true;
						break;
					}
				}
			}
			if(!isEnough){
				return false;
			}
		}
		
		int propId = equipRefineInfo.getProp();
		int itemNeedNum = equipRefineInfo.getNum(); // 需要的道具物品数量
		
		PropXMLInfo propXMLInfo = PropXMLInfoMap.getPropXMLInfo(propId);
		if (propXMLInfo == null)
		{
			return false;
		}
		
		// 判断道具物品是否存在
		if (BagItemMap.checkBagItem(roleInfo, propId) == null)
		{
			return false;
		}
		
		if (!BagItemMap.checkBagItemNum(roleInfo, propId, itemNeedNum))
		{
			return false;
		
		}
		
		return true;

	}
	
	/**
	 * 红点检测判断装备是否可以强化
	 * @param roleInfo
	 * @param equipInfo
	 * @param heroInfo
	 * @return
	 */
	private static boolean isCanUp(RoleInfo roleInfo, EquipInfo equipInfo, HeroInfo heroInfo){
		int level = equipInfo.getLevel();
		EquipXMLInfo equipXMLInfo = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo()); //判断Equip.xml是否包含该物品
		if (equipXMLInfo == null) {
			return false;
		}
		
		EquipStrengthenConfigInfo upConfigInfo = EquipXMLInfoMap.getEquipStrengthenConfigInfo(equipXMLInfo.getQuality());
		if(upConfigInfo == null){
			return false;
		}
		
		Map<Integer, EquipXMLUpgrade> equipXMLUpgradeMap = upConfigInfo.getEquipXMLUpgradeMap();
		if(equipXMLUpgradeMap == null){
			return false;
		}
		
		EquipXMLUpgrade upgrade = equipXMLUpgradeMap.get(level);
		if (upgrade == null) {
			return false;
		}
		if (heroInfo.getHeroLevel() <= upgrade.getHeroLevel()) {
			return false;
		}
		
		int needUpExp = upgrade.getExp() - equipInfo.getExp();
		
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		conds.add(new MoneyCond(needUpExp * GameValue.EQUIP_STRENGEH_RATE));
		int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (check != 1) {
			return false;
		}
//		
//		BagItemInfo bagItemInfo = BagItemMap.getBagItembyNo(roleInfo, GameValue.EQUIP_STRENGEH_ITEM);
//		// 碎片数量的判断
//		if (bagItemInfo == null || bagItemInfo.getNum() < needUpExp)
//		{
//			return false;
//		}
		
		return true;
	}
	
	/**
	 * 跨服竞技场可领取段位奖励
	 * @param roleId
	 * @return
	 */
	private static byte checkArenaPrizeRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null) {
			return DEFAULT;
		}
		if(roleInfo.getStageAward() != 0 && roleInfo.getStageAward() != roleLoadInfo.getCompetitionAward()) {		
			return LIGHT;
		}
		return DARK;
	}
	
	/**
	 * 征战有宝箱可以领取
	 * @param roleId
	 * @return
	 */
	private static byte checkChallengePrizeRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return DEFAULT;
		}
		//获取已打过副本
		Map<Byte, Map<Integer, Map<Integer, ChallengeBattleInfo>>> map = roleLoadInfo.getChallengeMap();
		for(byte chapterType : map.keySet()){
			
			Map<Integer, Map<Integer, ChallengeBattleInfo>> challengeMap = ChallengeBattleInfoMap.getByRoleId(roleId, (byte)chapterType);
			// 获取已领奖信息
			Map<Integer, List<Integer>> prizeMap = ChallengeService.getPrizeMap(roleInfo, chapterType);
			//计算每一章的总星数与已领取奖励比较
			if(challengeMap != null && challengeMap.size() > 0)
			{
				for(int chapterNo : challengeMap.keySet()) {
					if(chapterNo == 0)
					{
						continue;
					}
					Map<Integer, ChallengeBattleInfo> roleChallengeBattleInfos = 
							ChallengeBattleInfoMap.getByRoleIdAndTypeNoAndChapter(roleId, (byte)chapterType, chapterNo);
					if(roleChallengeBattleInfos != null && roleChallengeBattleInfos.size() > 0) {
						int hasGetStar = 0;//拥有的总星数
						for (ChallengeBattleInfo battleInfo : roleChallengeBattleInfos.values()) {
							hasGetStar += battleInfo.getStars();
						}
						//可领取的总宝箱数
						int prize = 0;
						ChallengeBattleXmlInfo challengeXmlInfo = ChallengeBattleXmlInfoMap.getInfoByNo(chapterType);
						if (challengeXmlInfo != null) {
							ChapterInfo chapterXmlInfo = challengeXmlInfo.getChapterInfoByNo(chapterNo);
							if(chapterXmlInfo != null) {
								if(chapterXmlInfo.getStar1() > 0 && hasGetStar >= chapterXmlInfo.getStar1()){
									prize++;
								}
								if(chapterXmlInfo.getStar2() > 0 && hasGetStar >= chapterXmlInfo.getStar2()){
									prize++;
								}
								if(chapterXmlInfo.getStar3() > 0 && hasGetStar >= chapterXmlInfo.getStar3()){
									prize++;
								}
							}
						}

						//本章已领取宝箱数量
						List<Integer> prizeNos = prizeMap.get(chapterNo);
						if(prizeNos != null && prizeNos.size() > 0){
							if(prize > prizeNos.size()){
								return LIGHT;
							}
						}
						if(prizeNos != null && prizeNos.size() == 0)
						{
							if(prize > 0)
							{
								return LIGHT;
							}
						}
						if(prizeNos == null)
						{
							if(prize > 0)
							{
								return LIGHT;
							}
						}
					}
				}
			}
		}
		
		return DARK;
		
	}
	
	/**
	 * 历练 你争我夺 有宝物可以合成
	 * @param roleId
	 * @return
	 */
	private static byte checkStoneComposeRemind(int roleId) {
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return DEFAULT;
		}
		Map<Integer, List<SnatchInfo>> map = SnatchMap.getPropNoMap();
		if(map == null || map.size() == 0){
			return DEFAULT;
		}
		for(List<SnatchInfo> snatchInfos : map.values()) {
			boolean flag = true;
			for(SnatchInfo snatchInfo : snatchInfos) {
				if (!BagItemMap.checkBagItemNum(roleInfo, snatchInfo.getPatchNo(), 1)) 
				{
					flag = false;
					break;
				}
			}
			if(flag){
				return LIGHT;
			}
		}
		return DARK;
		
	}
	
	/**
	 * 有好友申请
	 * @param roleId
	 * @return
	 */
	private static byte isFriendApply(int roleId){
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return DEFAULT;
		}
		Set<Integer> set = RoleAddRquestMap.getAddRequestRoleIdSet(roleInfo.getId());
		if(set != null && set.size() > 0)
		{
			return LIGHT;
		}
		
		return DARK;
	}
	
	/**
	 * 是否有好友精力可以领取
	 * @param roleId
	 * @return
	 */
	private static byte checkPresentEnergy(int roleId){

		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return DEFAULT;
		}
		
		Map<Integer, PresentEnergyInfo> map = roleLoadInfo.getPresentEnergyMap();
		if(map != null && map.size() > 0)
		{
			return LIGHT;
		}
		
		return DARK;
	
	}
	
	/**
	 * 	剑阁是否有矿可领取
	 * @param roleId
	 * @return
	 */
	private static byte checkMinePrize(int roleId){
		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return DEFAULT;
		}
		Map<Integer, MinePrize> prizeMap = MineInfoMap.getMinePrizeMap(roleId);
		if(prizeMap != null && prizeMap.size() > 0){
			return LIGHT;
		}
		
		Map<Integer, MineInfo> map = MineInfoMap.getMineMap();
		for (MineInfo mineInfo : map.values()) {
			synchronized (mineInfo) {
				MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineInfo.getMineNo());
				if (xmlInfo == null) {
					continue;
				}
				long now = System.currentTimeMillis();
				MineHelpRole helpRole = null;
				for (MineRole mineRole : mineInfo.getRoles().values()) {
					if (mineRole.getRoleId() == roleId) {
						long startTime = mineRole.getCreateTime().getTime();
						long endTime = mineRole.getCurrEndTime();
						if (endTime <= now && mineRole.getStatus() == MineRole.NOT_GET_PRIZE
								&& MineService.getOut(xmlInfo, startTime, endTime) > 0) {
							return LIGHT;
						}
					} else {
						helpRole = mineRole.getHelpRoles(roleId);
						if (helpRole != null) {
							long startTime = helpRole.getCreateTime().getTime();
							long endTime = mineRole.getCurrEndTime();
							if (endTime <= now && helpRole.getStatus() == MineRole.NOT_GET_PRIZE
									&& MineService.getOut(xmlInfo, startTime, endTime) > 0) {
								return LIGHT;
							}
						}
					}
				}
			}
		}
		return DARK;
	}
	
	/**
	 * 	剑阁是否有防守日志添加
	 * @param roleId
	 * @return
	 */
	private static byte checkMineLog(int roleId){

		if (!RoleLoginMap.isExitRoleInfo(roleId)) {
			return DEFAULT;
		}
		RoleInfo roleInfo = RoleLoginMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return DEFAULT;
		}
		//获取角色防守记录
		List<MineFightLog> logList = roleInfo.getMineLogs();
		if(logList != null && logList.size() > 0){
			for(MineFightLog mineLog : logList){
				if(mineLog != null)
				{
					if(roleInfo.getLastLookLogTime() == null || mineLog.getTime().getTime() > roleInfo.getLastLookLogTime().getTime())
					{
						return LIGHT;
					}
				}
			}
		}
		return DARK;
	
	}
	
	/**
	 * 把二进制字符串转换成int
	 * @param redPoint
	 * @return
	 */
	private static int StringToInteger(byte[]  redPoint) {
		StringBuffer sb = new StringBuffer();
		for(byte a : redPoint) {
			sb.append(a);
		}
		//System.out.println("sb = "+sb);
		sb.reverse();
		sb.insert(0, 1); //二进制第一位补1 避免之前的第一位是0
		//System.out.println("sb = "+sb);
		int b = Integer.valueOf(sb.toString(), 2);
		
		return b;
		
	}
	
}
