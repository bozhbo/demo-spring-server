package com.snail.webgame.game.protocal.gm.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.client.Client;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.ChallengeBattleInfoMap;
import com.snail.webgame.game.cache.ClubSceneInfoMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.FightCampaignInfoMap;
import com.snail.webgame.game.cache.FightGemInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameFlag;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCost;
import com.snail.webgame.game.common.xml.info.HeroColorXMLUpCostItem;
import com.snail.webgame.game.common.xml.info.HeroSkillXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.HeroXMLSkill;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.ActivityDao;
import com.snail.webgame.game.dao.ChallengeBattleDAO;
import com.snail.webgame.game.dao.EquipDAO;
import com.snail.webgame.game.dao.FightCampaignDAO;
import com.snail.webgame.game.dao.FightGemDAO;
import com.snail.webgame.game.dao.HeroDAO;
import com.snail.webgame.game.dao.QuestDAO;
import com.snail.webgame.game.dao.RoleClubEventDao;
import com.snail.webgame.game.dao.RoleClubInfoDao;
import com.snail.webgame.game.dao.RoleClubMemberDao;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.SkillDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.ClubEventInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.FightCampaignInfo;
import com.snail.webgame.game.info.FightGemInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.protocal.activity.service.ActivityService;
import com.snail.webgame.game.protocal.app.AppStoreRechargeReq;
import com.snail.webgame.game.protocal.app.AppStoreRechargeResp;
import com.snail.webgame.game.protocal.app.service.AppService;
import com.snail.webgame.game.protocal.appellation.entity.ResetTitleInfosResp;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.campaign.service.CampaignService;
import com.snail.webgame.game.protocal.challenge.service.ChallengeService;
import com.snail.webgame.game.protocal.club.entity.RoomIdMsgResp;
import com.snail.webgame.game.protocal.club.scene.entity.GetOutClubSceneResp;
import com.snail.webgame.game.protocal.club.scene.service.ClubSceneService;
import com.snail.webgame.game.protocal.club.service.ClubMgtService;
import com.snail.webgame.game.protocal.club.service.ClubService;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.funcopen.service.FuncOpenMgtService;
import com.snail.webgame.game.protocal.gm.annotation.CommandMapping;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.mail.service.MailService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.rolemgt.worldChatLimit.WorldChatLimitReq;
import com.snail.webgame.game.protocal.scene.info.RolePoint;
import com.snail.webgame.game.protocal.scene.joinScene.RoleJoinSceneResp;
import com.snail.webgame.game.protocal.scene.joinScene.SceneRolePointInfo;
import com.snail.webgame.game.protocal.scene.sceneRefre.SceneRefreService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.xml.cache.ArenaXMLInfoMap;
import com.snail.webgame.game.xml.cache.CampaignXMLInfoMap;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.cache.ChenghaoXMLInfoMap;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.cache.SceneXmlInfoMap;
import com.snail.webgame.game.xml.cache.WeaponXmlInfoMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.CampaignXMLInfo;
import com.snail.webgame.game.xml.info.ChenghaoXMLInfo;
import com.snail.webgame.game.xml.info.EquipRefineInfo;
import com.snail.webgame.game.xml.info.EquipStrengthenConfigInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo;

/**
 * 执行GM命令Service 单例模式 直接添加GM命令注解和对应的方法， 通过 @CommandMapping 匹配。 @CommandMapping
 * 默认值为方法名
 * @author caowl
 */
public class CommandService {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private static final int RESULT_SUCCESS = 1;
	private RoleDAO roleDao = RoleDAO.getInstance();
	private HeroDAO heroDAO = HeroDAO.getInstance();

	// 单例模式
	private CommandService() {
	}

	private static class CommandServiceHolder {
		final static CommandService commandService = new CommandService();
	}

	public static CommandService getInstance() {
		return CommandServiceHolder.commandService;
	}

	/**
	 * 设置角色GM权限
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("setGMRight")
	public int setGMRight(RoleInfo roleInfo, String[] args) {
		int result = 0;

		int gmRight = roleInfo.getGmRight();

		boolean hasRight = false;
		if (gmRight > 0) {
			hasRight = true;// 拥有这个权限
		}
		if (!hasRight) {
			return ErrorCode.GM_CMD_ERROR_20;
		}

		RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfoByName(args[0]);
		if (otherRoleInfo == null) {
			return ErrorCode.GM_CMD_ERROR_5;
		}

		int roleId = otherRoleInfo.getId();
		if (args.length > 0 && StringUtils.isNotBlank(args[1])) {
			byte setGmRight = Byte.valueOf(args[1]);
			if (setGmRight == 1) {
				if (roleDao.updateRoleGMRight(roleId, setGmRight)) {
					synchronized (otherRoleInfo) {
						otherRoleInfo.setGmRight(setGmRight);
					}
					result = 1;
					SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
				} else {
					result = 0;
				}
			}
		}
		return result;
	}
	
	/**
	 * 添加道具/装备
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addItem")
	public int addItem(RoleInfo roleInfo, String[] args) {
		if (args.length != 2) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		String itemNoStr = args[0];
		int itemNo = NumberUtils.toInt(args[0]);
		int num = NumberUtils.toInt(args[1]);
		if (itemNo == 0 || num == 0) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}

		if (itemNoStr.startsWith(GameValue.EQUIP_N0)) {
			EquipXMLInfo equipXmlInfo = EquipXMLInfoMap.getEquipXMLInfo(itemNo);
			if (equipXmlInfo == null) {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
		} else if (itemNoStr.startsWith(GameValue.PROP_N0)) {
			PropXMLInfo xmlInfo = PropXMLInfoMap.getPropXMLInfo(itemNo);
			if (xmlInfo == null) {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
		} else {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}

		DropInfo dropInfo = new DropInfo(itemNoStr, num);

		synchronized (roleInfo) {
			return addPropOrResouce(roleInfo, 1, dropInfo);
		}
	}

	/**
	 * GM添加道具和资源
	 * @param roleInfo
	 * @param type 1 道具 2 资源
	 * @param dropInfo
	 * @return
	 */
	private int addPropOrResouce(RoleInfo roleInfo, int type, DropInfo dropInfo) {

		List<DropInfo> tempItemList = new ArrayList<DropInfo>(1);
		tempItemList.add(dropInfo);

		int result = 1;
		if (type == 1 || type == 2) {
			result = ItemService.itemAdd(ActionType.action6.getType(), roleInfo, tempItemList, 
					null, null, null, null, true);
		}
		
		if (result == 1) {
			// 行为日志
			String actValue = dropInfo.getItemNo() + "-" + dropInfo.getItemNum();
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action6.getType(), actValue);
		}
		return result;
	}

	/**
	 * 添加银子
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addSilver")
	public int addSilver(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_MONEY);
	}

	/**
	 * 添加金子
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addGold")
	public int addGold(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_COIN);
	}

	/**
	 * 添加体力
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addSp")
	public int addSp(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_SP);
	}
	
	/**
	 * 添加精力
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addEnergy")
	public int addEnergy(RoleInfo roleInfo, String[] args) {
		int result = addResources(roleInfo, args, ConditionType.TYPE_ENERGY);
		if(result == 1){
			// 推送精力变化
			String energyStr = roleInfo.getEnergy() + "," + roleInfo.getLastRecoverEnergyTime().getTime() + "," + 
					roleInfo.getRoleLoadInfo().getTodayBuyEnergyNum() + "," + 0;
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_ENERGY, energyStr);
			return 1;
		}
		return result;
	}

	/**
	 * 添加竞技场货币勇气点
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addCourage")
	public int addCourage(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_COURAGE);
	}

	/**
	 * 添加远征货币正义点
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addJustice")
	public int addJustice(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_JUSTICE);
	}

	/**
	 * 添加战功
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addExploit")
	public int addExploit(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_EXPLOIT);
	}

	/**
	 * 添加跨服币
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addKuafuMoney")
	public int addKuafuMoney(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_KUAFU_MONEY);
	}
	
	/**
	 * 添加斩将令
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addTeamMoney")
	public int addTeamMoney(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_TEAM_MONEY);
	}
	
	/**
	 * 添加荣誉点
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addPvp3Money")
	public int addPVP3Money(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_PVP_3_MONEY);
	}

	/**
	 * 添加资源
	 * @param roleInfo
	 * @param args
	 * @param type
	 * @return
	 */
	private int addResources(RoleInfo roleInfo, String[] args, ConditionType type) {
		if (args.length != 1) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		int num = NumberUtils.toInt(args[0]);

		boolean flag = false;
		synchronized (roleInfo) {
			if(num > 0)
			{
				flag = RoleService.addRoleRoleResource(ActionType.action6.getType(), roleInfo, type, num,null);
			}
			else
			{
				flag = RoleService.subRoleRoleResource(ActionType.action6.getType(), roleInfo, type, -num ,null);
			}
		}
		if (!flag) {
			return ErrorCode.GM_CMD_ERROR_14;
		}
		SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action6.getType(), type + "-" + num);
		return 1;
	}

	/**
	 * 添加英雄
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addHero")
	public int addHero(RoleInfo roleInfo, String[] args) {
		int heroNo = 0;
		if (args.length > 0) {
			heroNo = NumberUtils.toInt(args[0]);
		}
		synchronized (roleInfo) {
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
			if (heroXmlInfo == null) {
				return ErrorCode.GM_CMD_ERROR_6;
			}
			if (heroXmlInfo.getInitial() != 0) {
				return ErrorCode.GM_CMD_ERROR_13;
			}
			HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
			if (heroInfo != null) {
				return ErrorCode.GM_CMD_ERROR_8;
			}
			List<HeroInfo> addHeroList = new ArrayList<HeroInfo>();
			heroInfo = HeroService.initNewHeroInfo(roleInfo, heroXmlInfo, HeroInfo.DEPLOY_TYPE_COMM);
			addHeroList.add(heroInfo);
			if (heroDAO.insertHeros(addHeroList)) {
				String reserve = "";
				for (HeroInfo hero : addHeroList) {
					reserve += hero.getId() + ",";
					HeroInfoMap.addHeroInfo(hero, false);
				}
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, reserve);

				// 任务
				QuestService.checkQuest(roleInfo, 0, null, true, false);
				//红点监听武将变动
				RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, GameValue.RED_POINT_TYPE_HERO);
				RedPointMgtService.pop(roleInfo.getId());
				
			} else {
				return ErrorCode.GM_CMD_ERROR_14;
			}
			return 1;
		}
	}
	
	/**
	 * 添所有英雄
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addAllHero")
	public int addAllHero(RoleInfo roleInfo,String[] args) 
	{
		//TODO
		synchronized (roleInfo) 
		{
			HashMap<Integer, HeroXMLInfo> heroMap  = HeroXMLInfoMap.getHeroMap();
			if (heroMap == null) 
			{
				return ErrorCode.GM_CMD_ERROR_6;
			}
			
			for(int heroNo : heroMap.keySet())
			{
				HeroXMLInfo heroXMLInfo = heroMap.get(heroNo);
				
				if (heroXMLInfo.getInitial() != 0) 
				{
					continue;
				}
				
				HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
				if (heroInfo != null) 
				{
					continue;
				}
				List<HeroInfo> addHeroList = new ArrayList<HeroInfo>();
				heroInfo = HeroService.initNewHeroInfo(roleInfo, heroXMLInfo, HeroInfo.DEPLOY_TYPE_COMM);
				addHeroList.add(heroInfo);
				if (heroDAO.insertHeros(addHeroList)) 
				{
					String reserve = "";
					for (HeroInfo hero : addHeroList)
					{
						reserve += hero.getId() + ",";
						HeroInfoMap.addHeroInfo(hero, false);
					}
					SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, reserve);
				}
				else
				{
					logger.error("GM add Hero error,heroNo="+heroNo);
				}
			}
		}
		return 1;
	}
	
	

	/**
	 * 修改武将等级
	 * @param roleInfo
	 * @param args [heroNo, level]
	 * @return
	 */
	@CommandMapping("setHeroLv")
	public int setHeroLv(RoleInfo roleInfo, String[] args) {
		int heroNo = 0;
		int heroLevel = 0;
		if (args.length > 1) {
			heroNo = NumberUtils.toInt(args[0]);
			heroLevel = NumberUtils.toInt(args[1]);
		}
		synchronized (roleInfo) {
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
			if (heroXmlInfo == null) {
				return ErrorCode.GM_CMD_ERROR_6;
			}
			HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_8;
			}
			if (heroXmlInfo.getInitial() != 0) {
				if (HeroXMLInfoMap.getOtherLvMap().get(heroLevel) == null) {
					return ErrorCode.GM_CMD_ERROR_11;
				}
			} else {
				if (HeroXMLInfoMap.getMainLvMap().get(heroLevel) == null) {
					return ErrorCode.GM_CMD_ERROR_11;
				}
			}

			if (heroDAO.updateHeroLv(heroInfo.getId(), heroLevel, 0)) {
				heroInfo.setHeroLevel(heroLevel);
				heroInfo.setHeroExp(0);

				HeroService.refeshHeroProperty(roleInfo, heroInfo);
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
				//红点
				RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, RedPointMgtService.LISTENING_CHALLENGE_FIGHT_END);
				
				RoleService.sendWorldChatLimit2MailServer(roleInfo, 0);
				
			} else {
				return ErrorCode.GM_CMD_ERROR_14;
			}
			return 1;
		}
	}

	/**
	 * 设置武将品质
	 * @param roleInfo
	 * @param args [heroNo, level]
	 * @return
	 */
	@CommandMapping("setHeroColor")
	public int setHeroColor(RoleInfo roleInfo, String[] args) {
		int heroNo = 0;
		int quality = 0;
		if (args.length > 1) {
			heroNo = NumberUtils.toInt(args[0]);
			quality = NumberUtils.toInt(args[1]);
		}
		synchronized (roleInfo) {
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
			if (heroXmlInfo == null) {
				return ErrorCode.GM_CMD_ERROR_6;
			}
			if (heroXmlInfo.getColourMap().get(quality) == null) {
				return ErrorCode.GM_CMD_ERROR_7;
			}
			HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_8;
			}

			if (heroDAO.updateHeroQuality(heroInfo.getId(), quality)) {
				heroInfo.setQuality(quality);
				HeroService.refeshHeroProperty(roleInfo, heroInfo);
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
			} else {
				return ErrorCode.GM_CMD_ERROR_9;
			}
			return 1;
		}
	}
	
	/**
	 * 设置武将星级
	 * @param roleInfo
	 * @param args [heroNo, level]
	 * @return
	 */
	@CommandMapping("setHeroStar")
	public int setHeroStar(RoleInfo roleInfo, String[] args) {
		int heroNo = 0;
		int star = 0;
		if (args.length > 1) {
			heroNo = NumberUtils.toInt(args[0]);
			star = NumberUtils.toInt(args[1]);
		}
		synchronized (roleInfo) {
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
			if (heroXmlInfo == null) {
				return ErrorCode.GM_CMD_ERROR_6;
			}
			if (heroXmlInfo.getStarMap().get(star) == null) {
				return ErrorCode.GM_CMD_ERROR_7;
			}
			HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_8;
			}

			if (heroDAO.updateHeroStar(heroInfo.getId(), star)) {
				heroInfo.setStar(star);
				HeroService.refeshHeroProperty(roleInfo, heroInfo);
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
			} else {
				return ErrorCode.GM_CMD_ERROR_9;
			}
			return 1;
		}
	}
	
	/**
	 * 设置主武将等级
	 * @param roleInfo
	 * @param args [heroNo, level]
	 * @return
	 */
	@CommandMapping("setMainHeroLv")
	public int setMainHeroLv(RoleInfo roleInfo, String[] args) {
		int heroLevel = 0;
		if (args.length > 0) {
			heroLevel = NumberUtils.toInt(args[0]);
		}
		synchronized (roleInfo) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_10;
			}
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXmlInfo == null) {
				return ErrorCode.GM_CMD_ERROR_6;
			}
			if (heroXmlInfo.getInitial() == 0) {
				return ErrorCode.GM_CMD_ERROR_13;
			}

			if (HeroXMLInfoMap.getMainLvMap().get(heroLevel) == null) {
				return ErrorCode.GM_CMD_ERROR_11;
			}

			if (heroDAO.updateHeroLv(heroInfo.getId(), heroLevel, 0)) {
				heroInfo.setHeroLevel(heroLevel);
				heroInfo.setHeroExp(0);

				HeroService.refeshHeroProperty(roleInfo, heroInfo);
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
				
				// 检测是否有功能开启
				FuncOpenMgtService.checkIsHasFuncOpen(roleInfo, true,null);
				
				// 升级检测等级礼包红点
				RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_TYPE_LEVEL_GIFT);
				
				RoleService.sendWorldChatLimit2MailServer(roleInfo, 0);

			} else {
				return ErrorCode.GM_CMD_ERROR_9;
			}
			return 1;
		}
	}

	/**
	 * 设置主武将品质
	 * @param roleInfo
	 * @param args [heroNo, level]
	 * @return
	 */
	@CommandMapping("setMainHeroColor")
	public int setMainHeroColor(RoleInfo roleInfo, String[] args) {
		int quality = 0;
		if (args.length > 0) {
			quality = NumberUtils.toInt(args[0]);
		}
		synchronized (roleInfo) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_21;
			}
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXmlInfo == null) {
				return ErrorCode.GM_CMD_ERROR_6;
			}
			if (heroXmlInfo.getInitial() == 0) {
				return ErrorCode.GM_CMD_ERROR_13;
			}
			if (heroXmlInfo.getColourMap().get(quality) == null) {
				return ErrorCode.GM_CMD_ERROR_7;
			}

			if (heroDAO.updateHeroQuality(heroInfo.getId(), quality)) {
				heroInfo.setQuality(quality);
				HeroService.refeshHeroProperty(roleInfo, heroInfo);
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
			} else {
				return ErrorCode.GM_CMD_ERROR_9;
			}
			return 1;
		}
	}
	
	/**
	 * 测试发邮件
	 * @param roleInfo
	 * @param args
	 * @return /gm sendMail roleName itemNo,itemNum,level,0 /gm sendMail 粱思真
	 *         36000001,1,1,0 /gm sendMail 粱思真 38200071,1,1,0 /gm sendMail 粱思真
	 *         money,100,0,0 /gm sendMail 粱思真 gold,100,0,0 /gm sendMail 粱思真
	 *         courage,100,0,0
	 */
	@CommandMapping("sendMail")
	public int sendMail(RoleInfo roleInfo, String[] args) {
		if (args.length >= 2) {

			StringBuffer buff = new StringBuffer();
			RoleInfo receRoleInfo = RoleInfoMap.getRoleInfoByName(args[0]);
			if (receRoleInfo == null) {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
			String attachmentStr = args[1];
			// 验证附件格式 itemNo,num,level,flag;itemNo,num,level,flag
			String[] attachments = StringUtils.split(attachmentStr, ';');
			if (attachments != null && attachments.length > 0) {
				for (String itemStr : attachments) {
					String[] itemArr = StringUtils.split(itemStr, ',');
					if (itemArr == null || itemArr.length != 4) {
						return ErrorCode.MAIL_SEND_ERROR_2;
					}

					if (itemArr[0].startsWith(GameValue.EQUIP_N0)) {
						buff.append("Equip_" + itemArr[0]);
						buff.append(",");
					} else if (itemArr[0].startsWith(GameValue.PROP_N0)) {
						buff.append("Prop_" + itemArr[0]);
						buff.append(",");
					} else {
						buff.append(itemArr[0]);
						buff.append(",");
					}
					buff.append(itemArr[1]);
					buff.append(",");
					buff.append(itemArr[2]);
					buff.append(",");
					buff.append(itemArr[3]);
					buff.append(",");
					buff.append(";");
				}

			}
			MailService.pushMailPrize(receRoleInfo.getId() + "", buff.toString(), "systemTest", "systemTest", "");
			return RESULT_SUCCESS;
		} else {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
	}

	/**
	 * 设置积分
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("sendInte")
	public int sendInte(RoleInfo roleInfo, String[] args) {
		try {
			if (args.length >= 1) {
				String integralStr = args[0];
				int integral = Integer.valueOf(integralStr);
				roleInfo.setCompetitionValue(integral);
				return RESULT_SUCCESS;
			} else {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
	}

	/**
	 * 设置段位
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("sendStage")
	public int sendStage(RoleInfo roleInfo, String[] args) {
		try {
			if (args.length >= 1) {
				String stageStr = args[0];
				int stage = Integer.valueOf(stageStr);
				roleInfo.setCompetitionStage((byte) stage);
				return RESULT_SUCCESS;
			} else {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
	}

	/**
	 * 清空背包
	 * @param roleInfo
	 * @param args 0-全部 1-装备 2-宝物 3-将星 4-宝石 5-消耗品 6-材料
	 * @return
	 */
	@CommandMapping("delBag")
	public int delBag(RoleInfo roleInfo, String[] args) {
		if (args.length != 1) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		int itemType = NumberUtils.toInt(args[0]);

		synchronized (roleInfo) {
			if (itemType == 0) {
				// 全部
				// 装备
				List<Integer> equipIds = new ArrayList<Integer>();
				Map<Integer, EquipInfo> equipMap = EquipInfoMap.getBagEquipMap(roleInfo.getId());
				if (equipMap != null && equipMap.size() > 0) {
					for (EquipInfo equip : equipMap.values()) {
						if (equip != null) {
							equipIds.add(equip.getId());
						}
					}
					int result = ItemService.bagEquipDel(ActionType.action6.getType(), roleInfo, equipIds);
					if (result != 1) {
						return result;
					}
				}
				// 道具
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				Map<Integer, BagItemInfo> itemMap = BagItemMap.getBagItem(roleInfo);
				if (itemMap != null && itemMap.size() > 0) {
					for (BagItemInfo itemInfo : itemMap.values()) {
						map.put(itemInfo.getItemNo(), itemInfo.getNum());
					}
					int result = ItemService.bagItemDel(ActionType.action6.getType(), roleInfo, map);
					if (result != 1) {
						return result;
					}
				}
			} else if (itemType == 1) {
				// 装备
				List<Integer> equipIds = new ArrayList<Integer>();
				Map<Integer, EquipInfo> equipMap = EquipInfoMap.getBagEquipMap(roleInfo.getId());
				if (equipMap != null && equipMap.size() > 0) {
					for (EquipInfo equip : equipMap.values()) {
						if (equip != null) {
							equipIds.add(equip.getId());
						}
					}
					int result = ItemService.bagEquipDel(ActionType.action6.getType(), roleInfo, equipIds);
					if (result != 1) {
						return result;
					}
				}
			} else if (itemType == 2) {
				// 宝物
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				Map<Integer, BagItemInfo> itemMap = BagItemMap.getBagItem(roleInfo);
				if (itemMap != null && itemMap.size() > 0) {
					for (BagItemInfo itemInfo : itemMap.values()) {
						if (itemInfo.getItemType() == BagItemInfo.TYPE_PROP) {
							map.put(itemInfo.getItemNo(), itemInfo.getNum());
						}
					}
					int result = ItemService.bagItemDel(ActionType.action6.getType(), roleInfo, map);
					if (result != 1) {
						return result;
					}
				}
			} else if (itemType == 3) {
				// 将星
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				Map<Integer, BagItemInfo> itemMap = BagItemMap.getBagItem(roleInfo);
				if (itemMap != null && itemMap.size() > 0) {
					for (BagItemInfo itemInfo : itemMap.values()) {
						if (itemInfo.getItemType() == BagItemInfo.TYPE_STAR) {
							map.put(itemInfo.getItemNo(), itemInfo.getNum());
						}
					}
					int result = ItemService.bagItemDel(ActionType.action6.getType(), roleInfo, map);
					if (result != 1) {
						return result;
					}
				}
			} else if (itemType == 4) {
				// 宝石
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				Map<Integer, BagItemInfo> itemMap = BagItemMap.getBagItem(roleInfo);
				if (itemMap != null && itemMap.size() > 0) {
					for (BagItemInfo itemInfo : itemMap.values()) {
						if (itemInfo.getItemType() == BagItemInfo.TYPE_STONE) {
							map.put(itemInfo.getItemNo(), itemInfo.getNum());
						}
					}
					int result = ItemService.bagItemDel(ActionType.action6.getType(), roleInfo, map);
					if (result != 1) {
						return result;
					}
				}
			} else if (itemType == 5) {
				// 消耗品
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				Map<Integer, BagItemInfo> itemMap = BagItemMap.getBagItem(roleInfo);
				if (itemMap != null && itemMap.size() > 0) {
					for (BagItemInfo itemInfo : itemMap.values()) {
						if (itemInfo.getItemType() == BagItemInfo.TYPE_OTHER) {
							map.put(itemInfo.getItemNo(), itemInfo.getNum());
						}
					}
					int result = ItemService.bagItemDel(ActionType.action6.getType(), roleInfo, map);
					if (result != 1) {
						return result;
					}
				}
			} else if (itemType == 6) {
				// 材料
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				Map<Integer, BagItemInfo> itemMap = BagItemMap.getBagItem(roleInfo);
				if (itemMap != null && itemMap.size() > 0) {
					for (BagItemInfo itemInfo : itemMap.values()) {
						if (itemInfo.getItemType() == BagItemInfo.TYPE_MAT) {
							map.put(itemInfo.getItemNo(), itemInfo.getNum());
						}
					}
					int result = ItemService.bagItemDel(ActionType.action6.getType(), roleInfo, map);
					if (result != 1) {
						return result;
					}
				}
			}
		}
		return 1;
	}

	/**
	 * 添加经验
	 * @param roleInfo
	 * @param args 经验
	 * @return
	 */
	@CommandMapping("addHeroExp")
	public int addHeroExp(RoleInfo roleInfo, String[] args) {
		if (args.length != 1) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		int exp = NumberUtils.toInt(args[0]);
		synchronized (roleInfo) {
			// 给予武将经验
			if (exp > 0) {
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if (heroInfo == null) {
					return ErrorCode.SYSTEM_ERROR;
				}
				int beforeLevel = heroInfo.getHeroLevel();
				int result = HeroService.heroExpAdd(ActionType.action6.getType(), roleInfo, heroInfo, exp, null);
				if (result != 1) {
					return result;
				}
				if (beforeLevel != heroInfo.getHeroLevel()) {
					if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN ) {
						FuncOpenMgtService.checkIsHasFuncOpen(roleInfo, true,null);
						
						// 升级检测等级礼包红点
						RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, GameValue.RED_POINT_TYPE_LEVEL_GIFT);
						
						//记录玩家升级日志
						GameLogService.insertRoleUpgradeLog(roleInfo, 1, beforeLevel, heroInfo.getHeroLevel(), 0);
					}
				}
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId()+"");				
			}
		}
		return 1;
	}

	/**
	 * 开启关卡
	 * @param roleInfo
	 * @param args 
	 * @return
	 */
	@CommandMapping("openChallenge")
	public int setChallenge(RoleInfo roleInfo, String[] args) {
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
			if(RoleDAO.getInstance().updateChallengeOpen(roleInfo.getId(), 1)){
				roleLoadInfo.setChallengeOpen(1);
			}
		}
		return 1;
	}

	/**
	 * 重置宝石活动
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("resetGem")
	public int resetGem(RoleInfo roleInfo, String[] args) {
		FightGemInfo fightGemInfo = FightGemInfoMap.getFightGemInfo(roleInfo.getId());
		if (fightGemInfo == null) {
			return ErrorCode.GEM_RESET_ERROR_3;
		}
		if (FightGemDAO.getInstance().updateGmResetGem(fightGemInfo.getId(), 0, 0, 0)) {
			fightGemInfo.setFightNum(0);
			fightGemInfo.setLastFightResult(0);
			fightGemInfo.setLastFightBattleNo(0);
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_GEM, "");
		} else {
			return ErrorCode.SQL_DB_ERROR;
		}

		return 1;
	}

	/**
	 * 重置宝物活动
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("resetCampaign")
	public int resetCampaign(RoleInfo roleInfo, String[] args) {
		FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleInfo.getId());
		if (info == null) {
			return ErrorCode.CAMPAIGN_RESET_ERROR_2;
		}
		int type = 0;// 0- 重置 1-通关
		if(args != null && args.length > 0){
			type = NumberUtils.toInt(args[0]);
		}
		switch (type) {
		case 0: // 重置			
			return CampaignService.resetFightCampaignInfobyGm(roleInfo, info);
		case 1:	// 通关
			CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
			if(xmlInfo == null){
				return ErrorCode.CAMPAIGN_RESET_ERROR_2;
			}			
			int lastFightBattleNo = xmlInfo.getLastBattleNo();
			int lastFightResult =  1;			
			if(FightCampaignDAO.getInstance().updateFightCampaignFightResultbyGm(info.getId(), lastFightBattleNo, lastFightResult)){				
				info.setLastFightBattleNo(lastFightBattleNo);
				info.setLastFightResult(lastFightResult);
			} else {
				return ErrorCode.CAMPAIGN_RESET_ERROR_4;
			}	
			return 1;
		default:
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
	}
	
	/**
	 * 设置新手引导结束
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("setGuideEnd")
	public int setGuideEnd(RoleInfo roleInfo, String[] args) {
		if (roleInfo.getRoleLoadInfo() != null) {
			String endGuideInfo = "1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1," +
					"1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";
			if(RoleDAO.getInstance().updateGuideData(roleInfo.getId(), endGuideInfo)){
				roleInfo.getRoleLoadInfo().setGuideInfo(endGuideInfo);
			}else{
				return ErrorCode.ROLE_REF_GUIDE_ERROR_3;
			}
			
			return RESULT_SUCCESS;
		}
		
		return ErrorCode.REQUEST_PARAM_ERROR;
	}
	
	/**
	 * 设置任务完成
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("setQuestFinish")
	public int setQuestFinish(RoleInfo roleInfo, String[] args) {
		try {
			if (args.length >= 1) {
				int otherRoleId = Integer.valueOf(args[0]);
				String questNoStr = args[1];
				
				int questProtoNo = Integer.valueOf(questNoStr);
				
				RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(otherRoleId);
				if (otherRoleInfo == null) {
					return ErrorCode.REQUEST_PARAM_ERROR;
				}
				
				QuestInProgressInfo questInProgressInfo = otherRoleInfo.getQuestInfoMap().getQuestInProgressInfo(questProtoNo);
				if (questInProgressInfo == null) {
					return ErrorCode.REQUEST_PARAM_ERROR;
				}
				questInProgressInfo.setStatus(QuestInProgressInfo.STATUS_FINISH);
				
				QuestDAO.getInstance().updateQuestInProgressInfo(questInProgressInfo.getId(), QuestInProgressInfo.STATUS_FINISH);
				
				if (otherRoleInfo.getLoginStatus() == 1) {
					SceneService.sendRoleRefreshMsg(otherRoleInfo.getId(), SceneService.REFESH_TYPE_QUEST, questInProgressInfo.getQuestProtoNo() + "");
				}
				return RESULT_SUCCESS;
			} else {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
	}
	
	/**
	 * 添加神兵(神兵编号 ， 神兵数量)
	 * @param roleInfo
	 * @param args [weaponNo, num]
	 * @return
	 */
	@CommandMapping("addMagic")
	public int addMagic(RoleInfo roleInfo, String[] args) {
		List<Integer> weaponNos = new ArrayList<Integer>();
		try {
			int num = Integer.parseInt(args[1]);

			for (int i = 0; i < num; i++) {
				weaponNos.add(Integer.parseInt(args[0]));
			}
			List<RoleWeaponInfo> weapList = new ArrayList<RoleWeaponInfo>();
			int result = ItemService.weaponAdd(6, roleInfo, weaponNos, weapList, null);

			if (result == 1) {
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_WEAPON, weapList);
				return result;
			}
		} catch (Exception e) {
			
		}
		
		return ErrorCode.REQUEST_PARAM_ERROR;
	}
	
	/**
	 * 重置银币活动，练兵活动次数
	 * @param roleInfo
	 * @return
	 */
	@CommandMapping("resetExpMoneyTimes")
	public int resetExpMoneyTimes(RoleInfo roleInfo, String[] args) {
		int LBCNum1 = GameValue.ACTIVITY_EXP_TIMES_1 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum2 = GameValue.ACTIVITY_EXP_TIMES_2 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum3 = GameValue.ACTIVITY_EXP_TIMES_3 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum4 = GameValue.ACTIVITY_EXP_TIMES_4 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum5 = GameValue.ACTIVITY_EXP_TIMES_5 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum6 = GameValue.ACTIVITY_EXP_TIMES_6 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		
		if(roleInfo.getExpLeftTimes1() != LBCNum1 || roleInfo.getExpLeftTimes2() != LBCNum2 || roleInfo.getExpLeftTimes3() != LBCNum3 
				|| roleInfo.getExpLeftTimes4() != LBCNum4 || roleInfo.getExpLeftTimes5() != LBCNum5 || roleInfo.getExpLeftTimes6() != LBCNum6
				|| roleInfo.getMoneyLeftTimes() != GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES){
			ActivityDao.getInstance().updateExpMoneyActivityLeftTimes(roleInfo.getId(),
					(byte)LBCNum1, (byte)LBCNum2, (byte)LBCNum3, (byte)LBCNum4, (byte)LBCNum5, (byte)LBCNum6,
					GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES);
			
			roleInfo.setExpLeftTimes1((byte)LBCNum1);
			roleInfo.setExpLeftTimes2((byte)LBCNum2);
			roleInfo.setExpLeftTimes3((byte)LBCNum3);
			roleInfo.setExpLeftTimes4((byte)LBCNum4);
			roleInfo.setExpLeftTimes5((byte)LBCNum5);
			roleInfo.setExpLeftTimes6((byte)LBCNum6);
			roleInfo.setMoneyLeftTimes(GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES);
		}
		
		// 在线推送
		if(roleInfo.getLoginStatus() == 1){
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
		}
		return ErrorCode.REQUEST_PARAM_ERROR;
	}
	
	/**
	 * 添加所有神兵 （神兵数量）
	 * @param roleInfo
	 * @param args [num]
	 * @return
	 */
	@CommandMapping("addAllMagic")
	public int addAllMagic(RoleInfo roleInfo, String[] args) {
		List<Integer> weaponNos = new ArrayList<Integer>();
		try {
			int num = Integer.parseInt(args[0]);
			
			for (int i = 0; i < num; i++) {
				for(Integer no : WeaponXmlInfoMap.getWeaponMap().keySet()){
					weaponNos.add(no);
				}
			}
			List<RoleWeaponInfo> weapList = new ArrayList<RoleWeaponInfo>();
			int result = ItemService.weaponAdd(6, roleInfo, weaponNos, weapList, null);
			
			if (result == 1) {
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_WEAPON, weapList);
				return result;
			}
		} catch (Exception e) {
			
		}
		
		return ErrorCode.REQUEST_PARAM_ERROR;
	}
	
	/**
	 * 显示武将属性
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("showHeroPro")
	public int showHeroPro(RoleInfo roleInfo, String[] args) {
		int heroNo = 0;
		if (args.length > 0) {
			heroNo = NumberUtils.toInt(args[0]);
		}
		HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
		if (heroXmlInfo == null) {
			logger.info("没有此武将");
			return ErrorCode.GM_CMD_ERROR_6;
		}
		HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
		if (heroInfo == null) {
			logger.info("没有此武将");
			return ErrorCode.GM_CMD_ERROR_8;
		} else {
			logger.info(heroXmlInfo.getName()+"属性：");
			HeroPropertyInfo total = HeroProService.getHeroTotalProperty(heroInfo, 1.0);
			for (HeroProType proType : HeroProType.values()) {
				logger.info(proType.getName() + ":" + total.getValue(proType));
			}
			logger.info("fightValue:" + HeroService.recalHeroFightValue(heroInfo,total, HeroService.getSkillMap(heroInfo)));
		}
		return 1;
	}
	
	/**
	 * 显示主武将属性
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("showMainHeroPro")
	public int showMainHeroPro(RoleInfo roleInfo, String[] args) {
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (heroInfo == null) {
			logger.info("没有主武将");
			return ErrorCode.GM_CMD_ERROR_8;
		} else {
			logger.info("主武将属性：");
			HeroPropertyInfo total = HeroProService.getHeroTotalProperty(heroInfo, 1.0);
			for (HeroProType proType : HeroProType.values()) {
				logger.info(proType.getName() + ":" + total.getValue(proType));
			}
			logger.info("fightValue:" + HeroService.recalHeroFightValue(heroInfo,total, HeroService.getSkillMap(heroInfo)));
		}
		return 1;
	}
	
	/**
	 * 清除防守玩法次数
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("cleanDefendTime")
	public int cleanDefendTime(RoleInfo roleInfo, String[] args){
		
		if(RoleDAO.getInstance().updateRoleDefendTime(roleInfo.getId(), (byte)0, roleInfo.getRoleLoadInfo().getLastDefendTime())){
			roleInfo.getRoleLoadInfo().setDefendTime((byte)0);
		}else {
			return ErrorCode.SQL_DB_ERROR;
		}
		return 1;
	}
	
	/**
	 * 清除对攻玩法次数
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("cleanAttackTime")
	public int cleanAttackTime(RoleInfo roleInfo, String[] args){
		
		if(RoleDAO.getInstance().updateRoleAttackAnotherInfo(roleInfo.getId(), (byte)0, 
				roleInfo.getRoleLoadInfo().getLastAttackAnotherTime())){
			roleInfo.getRoleLoadInfo().setAttackAnotherTime((byte)0);
		}else {
			return ErrorCode.SQL_DB_ERROR;
		}
		return 1;
	}
	
	/**
	 * 
	 */
	@CommandMapping("addAllChallengeStar")
	public int addAllChallengeStar(RoleInfo roleInfo, String[] args){
		if (args.length != 1) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		int star = 0;
		String stars = "";
		if (args.length > 0) {
			star = NumberUtils.toInt(args[0]);
		}
		if(star > 3 || star <= 0){
			return ErrorCode.SQL_DB_ERROR;
		}
		if(star == 1){
			stars = "1";
		}else if(star == 2){
			stars = "1,2";
		}else if(star == 3){
			stars = "1,2,3";
		}
		
		synchronized (roleInfo) {
			
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			// 玩家可打的副本信息
			List<BattleDetail> details = ChallengeBattleXmlInfoMap.getTotals();
			if (details != null) {
				for (BattleDetail detail : details) {
					BattleDetail battleDetail = ChallengeBattleXmlInfoMap.getBattleDetail(detail.getChapterType(), detail.getChapterNo(), detail.getBattleNo());
					ChallengeBattleInfo challengeBattleInfo = 
							ChallengeBattleInfoMap.getBattleInfo(roleInfo.getId(), (byte)detail.getChapterType(), detail.getChapterNo(), detail.getBattleNo());

					if (challengeBattleInfo == null) {

						challengeBattleInfo = new ChallengeBattleInfo(roleInfo.getId(), (byte)detail.getChapterType(), detail.getChapterNo(), detail.getBattleNo());
						challengeBattleInfo.setStar(stars);
						challengeBattleInfo.setStars(star);
						//boolean flag = false;
						// 刷新数据库
						if (ChallengeBattleDAO.getInstance().insertChallengeBattleInfo(challengeBattleInfo)) {

							ChallengeBattleInfoMap.addInfo(challengeBattleInfo);
							if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(),
									battleDetail.getBattleNum(), new Timestamp(System.currentTimeMillis()))) {
								challengeBattleInfo.setCanFightNum(battleDetail.getBattleNum());
								challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
							}
							roleLoadInfo.addBattle(challengeBattleInfo.getBattleId());
						}
						ChallengeBattleInfoMap.addInfo(challengeBattleInfo);
					} else {
						//刷新最短通关时间，星级
						String lastStar = challengeBattleInfo.getStar();
						int oldStar  = challengeBattleInfo.getStars();
						if(oldStar != 3 && !lastStar.equals(String.valueOf(star))){
							star = star > oldStar ? star : oldStar;
							
							// 更新星级
							if (ChallengeBattleDAO.getInstance().updateChallengeBattleInfo(stars,
									challengeBattleInfo.getId())) {
								challengeBattleInfo.setStar(stars);
								challengeBattleInfo.setStars(star);
								// 胜利后,更新次数及战斗时间
								if (ChallengeBattleDAO.getInstance().updateChallengeAttackNum(challengeBattleInfo.getId(), battleDetail.getBattleNum(),
										new Timestamp(System.currentTimeMillis()))) {
									challengeBattleInfo.setCanFightNum(battleDetail.getBattleNum());
									challengeBattleInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
								}
							}
						}
					}
					//刷新副本信息
					ChallengeService.refreshBattles(roleInfo, challengeBattleInfo, true, 0);
				}
			}
		}
		return 1;
	}
	
	@CommandMapping("addClubContribution")
	public int addClubContribution(RoleInfo roleInfo, String[] args){
		if (args.length != 1) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		
		try{
			int num = Integer.parseInt(args[0]);
			if(num < 0){
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
			
			if(RoleDAO.getInstance().updateRoleClubContribution(roleInfo.getId(), num)){
				roleInfo.setClubContribution(num);
				
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
				GameLogService.insertPlayActionLog(roleInfo,ActionType.action6.getType(), "ClubContribution - " + num);
				
			}
			
			
		}catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("addClubContribution failure", e);
			}
		}
		
		return 1;
	}
	
	/**
	 * 添加技能点
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addTech")
	public int addTech(RoleInfo roleInfo, String[] args) {
		return addResources(roleInfo, args, ConditionType.TYPE_TECH);
	}
	
	/**
	 * 设置VIP等级
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("setVipLv")
	public int setVipLv(RoleInfo roleInfo, String[] args){
		if (args.length != 1) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		int vipLv = NumberUtils.toInt(args[0]);
		if (vipLv > VipXMLInfoMap.getMaxVipLevel()) {
			vipLv = VipXMLInfoMap.getMaxVipLevel();
		}
		int oldVIPLv = roleInfo.getVipLv();
		if (RoleDAO.getInstance().updateRoleVipLv(roleInfo.getId(), vipLv)) {
			roleInfo.setVipLv(vipLv);
			ActivityService.resNum(roleInfo, oldVIPLv, vipLv);
		} else {
			return ErrorCode.SQL_DB_ERROR;
		}
		// 推送充值及vip信息
		SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CHARGE, "");
		for(int i = 0; i <= roleInfo.getVipLv(); i++){
			TitleService.achieveTitleCheck(GameValue.APPELLATION_TYPE_VIP, i, roleInfo);
		}
		return 1;
	}
	
	/**
	 * 清理押镖次数
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("cleYabiaoNum")
	public int cleYabiaoNum(RoleInfo roleInfo, String[] args){
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		roleDao.updateTodayYabiaoNum(roleInfo.getId(), (byte)0);
		roleLoadInfo.setTodayYabiaoNum((byte) 0);
		return 1;
	}
	
	/**
	 * 清理当天劫镖次数
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("cleJiebiaoNum")
	public int cleJiebiaoNum(RoleInfo roleInfo, String[] args){
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		roleDao.updateTodayJiebiaoNum(roleInfo.getId(), (byte) 0);
		roleLoadInfo.setTodayJiebiaoNum((byte) 0);
		return 1;
	}
	
	/**
	 * 设置武将技能等级
	 * @param roleInfo
	 * @param args [heroNo skillLevel]
	 * @return
	 */
	@CommandMapping("setHeroSkillLv")
	public int setHeroSkillLv(RoleInfo roleInfo, String[] args) {
		int heroNo = 0;
		int skillLevel = 0;
		if (args.length > 1) {
			heroNo = NumberUtils.toInt(args[0]);
			skillLevel = NumberUtils.toInt(args[1]);
		}
		synchronized (roleInfo) {
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
			if (heroXmlInfo == null) {
				return ErrorCode.GM_CMD_ERROR_6;
			}
			HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_8;
			}
			HeroSkillXMLInfo hsxml = null;
			String skillStr = "";
			for (HeroXMLSkill skillXML : heroXmlInfo.getSkillMap().values()) {
				int skillNo = skillXML.getSkillNo();
				int skillPos = skillXML.getSkillPos();
				hsxml = HeroXMLInfoMap.getHeroSkillXMLInfo(skillPos);
				if (hsxml == null) {
					continue;
				}
				if (skillLevel > hsxml.getMaxSkillLv()) {
					skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, hsxml.getMaxSkillLv());
				} else {
					if (hsxml.getUpMap().containsKey(skillLevel)) {
						skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, skillLevel);
					}
				}
			}
			if (SkillDAO.getInstance().addOrUpdateHeroSkill(heroInfo.getId(), skillStr)) {
				heroInfo.setSkillStr(skillStr);
			} else {
				return ErrorCode.GM_CMD_ERROR_9;
			}
			HeroService.refeshHeroProperty(roleInfo, heroInfo);
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
			return 1;
		}
	}

	/**
	 * 设置主武将技能等级
	 * @param roleInfo
	 * @param args [skillLevel]
	 * @return
	 */
	@CommandMapping("setMainHeroSkillLv")
	public int setMainHeroSkillLv(RoleInfo roleInfo, String[] args) {
		int skillLevel = 0;
		if (args.length > 0) {
			skillLevel = NumberUtils.toInt(args[0]);
		}
		synchronized (roleInfo) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_8;
			}
			HeroXMLInfo heroXmlInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXmlInfo == null) {
				return ErrorCode.GM_CMD_ERROR_6;
			}
			HeroSkillXMLInfo hsxml = null;
			String skillStr = "";
			for (HeroXMLSkill skillXML : heroXmlInfo.getSkillMap().values()) {
				int skillNo = skillXML.getSkillNo();
				int skillPos = skillXML.getSkillPos();
				hsxml = HeroXMLInfoMap.getHeroSkillXMLInfo(skillPos);
				if (hsxml == null) {
					continue;
				}
				if (skillLevel > hsxml.getMaxSkillLv()) {
					skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, hsxml.getMaxSkillLv());
				} else {
					if (hsxml.getUpMap().containsKey(skillLevel)) {
						skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, skillLevel);
					}
				}
			}
			if (SkillDAO.getInstance().addOrUpdateHeroSkill(heroInfo.getId(), skillStr)) {
				heroInfo.setSkillStr(skillStr);
			} else {
				return ErrorCode.GM_CMD_ERROR_9;
			}
			HeroService.refeshHeroProperty(roleInfo, heroInfo);
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
			return 1;
		}
	}

	/**
	 * 设置主武将装备强化等级
	 * @param roleInfo
	 * @param args [level]
	 * @return
	 */
	@CommandMapping("setEquipLv")
	public int setEquipLv(RoleInfo roleInfo, String[] args) {
		int level = 0;
		if (args.length > 0) {
			level = NumberUtils.toInt(args[0]);
		}
		synchronized (roleInfo) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_8;
			}
			EquipXMLInfo equipXNL = null;
			EquipStrengthenConfigInfo equipUpXML = null;
			for (EquipInfo equipInfo : heroInfo.getEquipMap().values()) {
				equipXNL = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
				if (equipXNL == null) {
					continue;
				}
				equipUpXML = EquipXMLInfoMap.getEquipUpMap().get(equipXNL.getQuality());
				if (equipUpXML == null) {
					continue;
				}
				if (!equipUpXML.getEquipXMLUpgradeMap().containsKey(level)) {
					continue;
				}

				if (EquipDAO.getInstance().updateEquipLevel(equipInfo.getId(), (short) level, 0)) {
					equipInfo.setLevel((short) level);
					equipInfo.setExp(0);
				} else {
					return ErrorCode.GM_CMD_ERROR_9;
				}
				EquipService.refeshEquipProperty(equipInfo);
			}

			HeroService.refeshHeroProperty(roleInfo, heroInfo);
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
			return 1;
		}
	}
	
	/**
	 * 设置主武将装备精炼等级
	 * @param roleInfo
	 * @param args [level]
	 * @return
	 */
	@CommandMapping("setEquipRefineLv")
	public int setEquipRefineLv(RoleInfo roleInfo, String[] args) {
		int level = 0;
		if (args.length > 0) {
			level = NumberUtils.toInt(args[0]);
		}
		synchronized (roleInfo) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return ErrorCode.GM_CMD_ERROR_8;
			}
			EquipXMLInfo equipXNL = null;
			EquipRefineInfo equipUpXML = null;
			for (EquipInfo equipInfo : heroInfo.getEquipMap().values()) {
				equipXNL = EquipXMLInfoMap.getEquipXMLInfo(equipInfo.getEquipNo());
				if (equipXNL == null) {
					continue;
				}
				equipUpXML = equipXNL.getRefineMap().get(level);
				if (equipUpXML == null) {
					continue;
				}			

				if (EquipDAO.getInstance().updateEquipRefineLevel(equipInfo.getId(), level)){
					equipInfo.setRefineLv((short) level);
				} else {
					return ErrorCode.GM_CMD_ERROR_9;
				}
				EquipService.refeshEquipProperty(equipInfo);
			}

			HeroService.refeshHeroProperty(roleInfo, heroInfo);
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_HERO, heroInfo.getId() + "");
			return 1;
		}
	}
	
	
	@CommandMapping("clearClubEvent")
	public int clearClubEvent(RoleInfo roleInfo, String[] args){
		if(roleInfo == null ){
			return ErrorCode.GM_CMD_ERROR_15;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			return ErrorCode.GM_CMD_ERROR_15;
		}
		
		RoleClubEventDao.getInstance().deleteRoleClubEventInfoByRoleId(roleInfo.getId());
		
		synchronized(GameFlag.ROLE_CLUB){
			RoleClubMemberInfoMap.removeClubEventByRoleId(roleInfo.getId());
		}
		
		
		return 1;
	}
	
	/**
	 * 显示竞技场属性排名
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("showArenaPro")
	public int showArenaPro(RoleInfo roleInfo, String[] args) {
		int place = 0;
		if (args.length > 0) {
			place = NumberUtils.toInt(args[0].trim());
		}
		FightArenaInfo fightArena = FightArenaInfoMap.getFightArenaInfobyPlace(place);
		if (fightArena == null) {
			return ErrorCode.GM_CMD_ERROR_15;
		}
		HeroPropertyInfo total = null;
		Map<Integer, Integer> skillMap = null;
		RoleInfo defendRole = null;
		int fightValue = 0;
		int defendRoleId = fightArena.getRoleId();
		if (defendRoleId != 0) {
			defendRole = RoleInfoMap.getRoleInfo(defendRoleId);
			if (defendRole == null) {
				return ErrorCode.GM_CMD_ERROR_15;
			}
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo != null) {
				Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(FightType.FIGHT_TYPE_2);
				Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(FightType.FIGHT_TYPE_2);
				total = HeroProService.getHeroTotalProperty(heroInfo, mainRate, otherRate);
				if (total.getLookRange() < GameValue.ARENA_LOOK_RANGE) {
					total.setLookRange(GameValue.ARENA_LOOK_RANGE);
				}
				skillMap = HeroService.getSkillMap(heroInfo);
				fightValue = HeroService.recalHeroFightValue(heroInfo,total, skillMap);
			}
		} else {
			Map<Byte, HeroRecord> fightDeployMap = fightArena.getFightDeployMap();
			HeroRecord record = fightDeployMap.get(HeroInfo.DEPLOY_TYPE_MAIN);
			if (record != null) {
				total = ArenaXMLInfoMap.getHeroPro(record.getHeroNo(), place,
						HeroProService.getProRate(GameValue.ARENA_NPC_EQUIP_RATE));
				if (total == null) {
					total = HeroRecordService.recalHeroRecordTotalPro(fightDeployMap, record,
							GameValue.ARENA_NPC_EQUIP_RATE);
				}
				if (total != null && total.getLookRange() < GameValue.ARENA_LOOK_RANGE) {
					total.setLookRange(GameValue.ARENA_LOOK_RANGE);
				}
				skillMap = record.getSkillMap();
				if(record.getFightValue() == 0)
				{
					int recordFightValue = HeroService.recalHeroRecordFightValue(record,total, skillMap);
					record.setFightValue(recordFightValue);
				}
				fightValue = record.getFightValue();
			}
		}
		if (total != null) {
			logger.info("竞技场属性排名" + place + "属性：");
			for (HeroProType proType : HeroProType.values()) {
				logger.info(proType.getName() + ":" + total.getValue(proType));
			}
			logger.info("fightValue:" + fightValue);
		}
		return 1;
	}
	
	@CommandMapping("setClubBuild")
	public int setClubBuild(RoleInfo roleInfo, String[] args){
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		if(roleClubInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_17;
		}
		
		int build = 0;
		if (args.length > 0) {
			build = NumberUtils.toInt(args[0].trim());
		}
		
		synchronized (roleClubInfo) {
			RoleClubInfoDao.getInstance().updateRoleClubInfoBuildAndLevel(roleClubInfo.getId(), build, roleClubInfo.getLevel());
			roleClubInfo.setBuild(build);
		}
		
		return 1;
	}
	
	@CommandMapping("setClubLevel")
	public int setClubLevel(RoleInfo roleInfo, String[] args){
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		if(roleClubInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_17;
		}
		
		int lv = 1;
		if (args.length > 0) {
			lv = NumberUtils.toInt(args[0].trim());
		}
		
		synchronized (roleClubInfo) {
			RoleClubInfoDao.getInstance().updateRoleClubInfoBuildAndLevel(roleClubInfo.getId(), roleClubInfo.getBuild(), lv);
			roleClubInfo.setLevel(lv);
		}
		
		return 1;
	}
	
	@CommandMapping("cleanBuildTime")
	public int cleanBuildTime(RoleInfo roleInfo, String[] args){
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		if(roleClubInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_17;
		}
		
		
		if(roleInfo.getRoleLoadInfo() != null){
			synchronized(roleInfo){
				roleInfo.getClubBuildTime().setTime(0);
			}
		}
		
		return 1;
	}
	
	/**
	 * GM命令充值
	 *  
	 * @param roleInfo
	 * @param args roleId,account,pid,transactionData,transactionIdentifier,serverId,amount
	 * @return
	 */
	@CommandMapping("doAppCharge")
	public int doAppCharge(RoleInfo roleInfo, String[] args){
		try {
			if(logger.isInfoEnabled()){
				logger.info("GM doAppCharge start !!");
			}
			RoleInfo roleInfo1 = RoleInfoMap.getRoleInfoByName(args[0]);
			
			if(roleInfo1 == null){
				logger.info("GM doAppCharge error roleName not exisit!!");
				return ErrorCode.QUERY_ROLE_ERROR_1;
			}
			//int account = Integer.parseInt(args[1]);
			String pid = args[2];
			String transactionData = args[3];
			String transactionIdentifier = args[4];
			int serverId = Integer.parseInt(args[5]);
			int amount = Integer.parseInt(args[6]);
			
			if(serverId != GameConfig.getInstance().getGameServerId()){
				return ErrorCode.CAS_ERR_SERVER_ID;
			}
			AppStoreRechargeReq req = new AppStoreRechargeReq();
			req.setAmount(amount);
			req.setPid(pid);
			req.setTransactionData(transactionData);
			req.setTransactionIdentifier(transactionIdentifier);
			
			AppStoreRechargeResp resp = new AppStoreRechargeResp();
			
			AppService.doAppCharge(roleInfo1, req, resp);
			
			if(resp.getResult() != 1){
				if(logger.isErrorEnabled()){
					logger.error("GM doAppCharge error ! roleId = " + roleInfo1.getId() + ",amount = " + amount
							+ ",pid = " + pid + ",transactionData = " + transactionData 
							+ ",transactionIdentifier = " + transactionIdentifier + ",serverId = " + serverId);
				}
				return ErrorCode.CHARGE_POINT_NOT_ARRIVAL;
				
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("GM doAppCharge exception ! " + e.getMessage());
			}
			return ErrorCode.CHARGE_POINT_NOT_ARRIVAL;
		}
		
		return 1;
	}
	
	/**
	 * 添加所有觉醒材料
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("addJXItem")
	public int addJXItem(RoleInfo roleInfo, String[] args) {
		if (args.length != 1) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		int num = NumberUtils.toInt(args[0]);
		if (num == 0) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		Set<Integer> itemNos = new HashSet<Integer>();
		for (HeroColorXMLUpCost xmlInfo : HeroXMLInfoMap.getColorUpCostMap().values()) {
			for (Map<Integer, HeroColorXMLUpCostItem> map : xmlInfo.getItemMap().values()) {
				for (HeroColorXMLUpCostItem item : map.values()) {
					itemNos.add(item.getItemNo());
				}
			}
		}
		if (itemNos.size() > 0) {
			synchronized (roleInfo) {
				List<DropInfo> list = new ArrayList<DropInfo>();
				for (int itemNo : itemNos) {
					list.add(new DropInfo(itemNo + "", num));
				}
				return ItemService.itemAdd(ActionType.action6.getType(), roleInfo, list, null, null, null, null, true);
			}
		}

		return 1;
	}
	
	/**
	 * 重置所在场景位置
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("resetPoint")
	public int resetPoint(RoleInfo roleInfo, String[] args) {
		if (args.length != 1) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		String roleName = args[0];
		if (roleName == null || roleName.length() <= 0) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		RoleInfo role = RoleInfoMap.getRoleInfoByName(roleName);
		if (role == null) {
			return ErrorCode.REQUEST_PARAM_ERROR;
		}
		synchronized (role) {
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(role);
			if (mainHero == null) {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}

			RolePoint rolePoint = role.getRolePoint();
			if (rolePoint == null) {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
			SceneXMLInfo sceneXMlInfo = SceneXmlInfoMap.getSceneXml(rolePoint.getNo());
			if (sceneXMlInfo == null) {
				return ErrorCode.REQUEST_PARAM_ERROR;
			}
			rolePoint.setPointX(sceneXMlInfo.getBornPointX());
			rolePoint.setPointY(sceneXMlInfo.getBornPointY());
			rolePoint.setPointZ(sceneXMlInfo.getBornPointZ());

			// 自己进场景看到的AI
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setMsgType(Command.IN_SCENE_AI_POINT_RESP);
			header.setUserID0((int) role.getId());
			message.setHeader(header);

			RoleJoinSceneResp joinSceneResp = new RoleJoinSceneResp();
			joinSceneResp.setRoleId(role.getId());
			joinSceneResp.setSceneNo(rolePoint.getNo());
			joinSceneResp.setSceneId(rolePoint.getSceneId());
			List<SceneRolePointInfo> rolePointList = new ArrayList<SceneRolePointInfo>();
			rolePointList.add(SceneService1.getSceneRolePointInfo(role, rolePoint));
			joinSceneResp.setRoleNum(rolePointList.size());
			joinSceneResp.setRolePointList(rolePointList);
			message.setBody(joinSceneResp);
			SceneRefreService.sendRoleRefreshMsg(role.getId(), SceneRefreService.REFRESH_TYPE_SCENE, message);

		}

		return 1;
	}
	
	@CommandMapping("setChatNum")
	public int setChatNum(RoleInfo roleInfo, String[] args){		
		
		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			WorldChatLimitReq req = new WorldChatLimitReq();
			
			RoleInfo otherRole = RoleInfoMap.getRoleInfoByName(args[0]);
			if(otherRole != null)
			{
				req.setLimit(Integer.valueOf(args[1]));
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setUserID0(otherRole.getId());
				header.setMsgType(Command.SEND_WORLD_CHAT_LIMIT_2_MAIL_SERVER_REQ);
				message.setHeader(header);
				message.setBody(req);
				session.write(message);
			}
		}
		
		
		return 1;
	}
	
	@CommandMapping("addTitle")
	public int addTitle(RoleInfo roleInfo, String[] args){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return 1;
		}
		
		int xmlNo = 0;
		
		try{
			xmlNo = Integer.parseInt(args[0]);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		ChenghaoXMLInfo xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByNo(xmlNo);
		if(xmlInfo == null){
			return 1;
		}
		
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
		
		if(map.containsKey(xmlInfo.getNo())){
			return 1;
		}
		
		long time = xmlInfo.getKeepTime();
		if(time > 0){
			time = System.currentTimeMillis() + xmlInfo.getKeepTime() * 1000;
		}
		
		map.put(xmlInfo.getNo(), time);
		
		String allTitles = CommonUtil.Map2String(map);
		
		if(RoleDAO.getInstance().updateAllAppellation(roleInfo.getId(), allTitles)){
			roleInfo.getRoleLoadInfo().setAllAppellation(allTitles);
		}
		
		
		//关联排行榜的 需要关联排行榜名次
		if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_JJC)){
			//No.1
			TitleService.jjcTop.clear();
			for(int i = 0; i < 3; i++){
				if(xmlInfo.getNum() == i + 1){
					TitleService.jjcTop.add(roleInfo.getId());
				}else{
					TitleService.jjcTop.add(0);
				}
			}
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_KFJJC)){
			//No.2
			TitleService.kfjjcTop.clear();
			for(int i = 0; i < 3; i++){
				if(xmlInfo.getNum() == i + 1){
					TitleService.kfjjcTop.add(roleInfo.getId());
				}else{
					TitleService.kfjjcTop.add(0);
				}
			}
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_LV)){
			//No.3
			TitleService.lvTop.clear();
			for(int i = 0; i < 3; i++){
				if(xmlInfo.getNum() == i + 1){
					TitleService.lvTop.add(roleInfo.getId());
				}else{
					TitleService.lvTop.add(0);
				}
			}
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_FIGHTVALUE)){
			//No.4
			TitleService.fightValueTop.clear();
			for(int i = 0; i < 3; i++){
				if(xmlInfo.getNum() == i + 1){
					TitleService.fightValueTop.add(roleInfo.getId());
				}else{
					TitleService.fightValueTop.add(0);
				}
			}
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_WORLDBOSS)){
			//No.5
			TitleService.bossTop.clear();
			for(int i = 0; i < 3; i++){
				if(xmlInfo.getNum() == i + 1){
					TitleService.bossTop.add(roleInfo.getId());
				}else{
					TitleService.bossTop.add(0);
				}
			}
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_HERO_NUM)){
			//No.6
			TitleService.heroNumTop.clear();
			for(int i = 0; i < 3; i++){
				if(xmlInfo.getNum() == i + 1){
					TitleService.heroNumTop.add(roleInfo.getId());
				}else{
					TitleService.heroNumTop.add(0);
				}
			}
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_CBP)){
			//No.7
			TitleService.cbpTop.clear();
			for(int i = 0; i < 3; i++){
				if(xmlInfo.getNum() == i + 1){
					TitleService.cbpTop.add(roleInfo.getId());
				}else{
					TitleService.cbpTop.add(0);
				}
			}
			
		}
		
		ResetTitleInfosResp resp = new ResetTitleInfosResp();
		resp.setResult(1);
		
		resp.setTitles(CommonUtil.Map2String(map));
		
		resp.setTitle(TitleService.nowTitleCheck(roleInfo, map));
		
		HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		if(mainHeroInfo != null){
			HeroService.refeshHeroProperty(roleInfo, mainHeroInfo);
			
			resp.setFightValue(mainHeroInfo.getFightValue());
		}
		
		SceneService.sendRoleRefreshMsg(resp, roleInfo.getId(), Command.RESET_TITLE_RESP);
		
		return 1;
	}
	
	@CommandMapping("delTitle")
	public int removeTitle(RoleInfo roleInfo, String[] args){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return 1;
		}
		
		int xmlNo = 0;
		
		try{
			xmlNo = Integer.parseInt(args[0]);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		ChenghaoXMLInfo xmlInfo = ChenghaoXMLInfoMap.getChenghaoXMLInfoByNo(xmlNo);
		if(xmlInfo == null){
			return 1;
		}
		
		Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
		
		if(map.containsKey(xmlInfo.getNo())){
			map.remove(xmlInfo.getNo());
			String allTitles = CommonUtil.Map2String(map);
			if(RoleDAO.getInstance().updateRoleTitleInfo(roleInfo.getId(), "", allTitles)){
				roleInfo.getRoleLoadInfo().setNowAppellation("");
				roleInfo.getRoleLoadInfo().setAllAppellation(allTitles);
			}
			
			ResetTitleInfosResp resp = new ResetTitleInfosResp();
			resp.setResult(1);
			resp.setTitles(CommonUtil.Map2String(map));
			
			HeroInfo mainHeroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			
			if(mainHeroInfo != null){
				HeroService.refeshHeroProperty(roleInfo, mainHeroInfo);
				
				resp.setFightValue(mainHeroInfo.getFightValue());
			}
			
			resp.setTitle(0);
			SceneService.sendRoleRefreshMsg(resp, roleInfo.getId(), Command.RESET_TITLE_RESP);
			
		}
		
		//关联排行榜的 需要关联排行榜名次
		if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_JJC)){
			//No.1
			TitleService.jjcTop.remove(new Integer(roleInfo.getId()));
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_KFJJC)){
			//No.2
			TitleService.kfjjcTop.remove(new Integer(roleInfo.getId()));
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_LV)){
			//No.3
			TitleService.lvTop.remove(new Integer(roleInfo.getId()));
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_FIGHTVALUE)){
			//No.4
			TitleService.fightValueTop.remove(new Integer(roleInfo.getId()));
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_WORLDBOSS)){
			//No.5
			TitleService.bossTop.remove(new Integer(roleInfo.getId()));
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_HERO_NUM)){
			//No.6
			TitleService.heroNumTop.remove(new Integer(roleInfo.getId()));
			
		}else if(xmlInfo.getType().equals(GameValue.APPELLATION_TYPE_CBP)){
			//No.7
			TitleService.cbpTop.remove(new Integer(roleInfo.getId()));

		}
		
		
		return 1;
	}
	
	@CommandMapping("setStarMoney")
	public int setStarMoney(RoleInfo roleInfo, String[] args){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return 1;
		}
		
		int star = 0;
		if (args.length > 0) {
			star = NumberUtils.toInt(args[0].trim());
		}
		
		if(RoleDAO.getInstance().updateRoleStarMoney4GM(roleInfo.getId(), star)){
			roleInfo.getRoleLoadInfo().setStarMoney(star);
		}
		
		return 1;
	}
	
	@CommandMapping("setSkillLv")
	public int setAllSkillLV(RoleInfo roleInfo, String[] args)
	{
		int quality = 0;
		int skillLv = 0;
		if (args.length > 0) {
			quality = NumberUtils.toInt(args[0].trim());
			skillLv = NumberUtils.toInt(args[1].trim());
		}
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
		for(int heroId : heroMap.keySet())
		{
			HeroInfo heroInfo = heroMap.get(heroId);
			if(heroInfo == null)
			{
				continue;
			}
			
			if (heroDAO.updateHeroQuality(heroInfo.getId(), quality)) {
				heroInfo.setQuality(4);
			} else {
				return 0;
			}
			
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
			if (heroXMLInfo == null) {
				return 0;
			}
			Map<Integer, HeroXMLSkill> skillXmlMap = heroXMLInfo.getSkillMap();
			for(int skillNo : skillXmlMap.keySet())
			{
				String skillStr = HeroService.addOrUpdateSkill(heroInfo, skillNo, skillLv);
				if (SkillDAO.getInstance().addOrUpdateHeroSkill(heroId, skillStr)) {
					heroInfo.setSkillStr(skillStr);
				} else {
					return 0;
				}
			}
			
			HeroService.refeshHeroProperty(roleInfo, heroInfo);
		}
		
		return 1;
	}
	
	@CommandMapping("setRoleResource")
	public int setRoleResource(RoleInfo roleInfo, String[] args){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return 1;
		}
		
		RoleInfo otherRoleInfo = null;
		int type = 0;
		long num = 0; 
		if (args.length > 0 && args.length == 2) {
			otherRoleInfo = RoleInfoMap.getRoleInfoByName(args[0]);
			
			if(otherRoleInfo == null){
				return 1;
			}
			
			String[] parm = args[1].split(":");
			
			try{
				type = NumberUtils.toInt(parm[0].trim());
				num = NumberUtils.toLong(parm[1].trim());
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			ConditionType cType = ConditionType.attrParseType(type);
			
			if(cType == null){
				return 1;
			}
			
			if(!AbstractConditionCheck.isResourceType(cType.getName())){
				return 1;
			}
			
			synchronized(otherRoleInfo){
				if(RoleDAO.getInstance().updateRoleResource4GM(ActionType.action6.getType(), otherRoleInfo.getId(), cType, num)){
					switch (cType) {
					case TYPE_MONEY:
						otherRoleInfo.setMoney(num);
						break;
					case TYPE_COURAGE:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setCourage(num);
						}
						break;
					case TYPE_JUSTICE:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setJustice(num);
						}
						break;
					case TYPE_KUAFU_MONEY:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setKuafuMoney((int) num);
						}
						break;
					case TYPE_TEAM_MONEY:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setTeamMoney((int) num);
						}
						break;
					case TYPE_HIS_EXPLOIT:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setHisExploit((int) num);
						}
						break;
					case TYPE_EQUIP:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setEquip(num);
						}
						break;
					case TYPE_PVP_3_MONEY:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setPvp3Money(num);
						}
						break;
					case TYPE_STAR_MONEY:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setStarMoney((int)num);
						}
						break;
					case TYPE_COIN:
						otherRoleInfo.setCoin(num);
						break;
					case TYPE_EXPLOIT:
						if(otherRoleInfo.getRoleLoadInfo() != null){
							otherRoleInfo.getRoleLoadInfo().setExploit((int)num);
						}
						break;
					case TYPE_ENERGY:
						otherRoleInfo.setEnergy((short) num);
						break;
					case TYPE_SP:
						otherRoleInfo.setSp((short) num);
						break;
					case TYPE_TECH:
						otherRoleInfo.setTech((short) num);
						break;
					case TYPE_CLUB_CONTRIBUTION:
						otherRoleInfo.setClubContribution((int) num);
						break;
					default:
						break;
					}
				}
			
			}
			
		}
		
		
		return 1;
	}
	
	
	/**
	 * 重置所有活动的次数
	 * @param roleInfo
	 * @param args
	 * @return
	 */
	@CommandMapping("clearActivityTimes")
	public int clearActivityTimes(RoleInfo roleInfo, String[] args) {
		synchronized (roleInfo) {
			int roleId = roleInfo.getId();
			
			int LBCNum1 = GameValue.ACTIVITY_EXP_TIMES_1 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum2 = GameValue.ACTIVITY_EXP_TIMES_2 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum3 = GameValue.ACTIVITY_EXP_TIMES_3 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum4 = GameValue.ACTIVITY_EXP_TIMES_4 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum5 = GameValue.ACTIVITY_EXP_TIMES_5 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum6 = GameValue.ACTIVITY_EXP_TIMES_6 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo==null){
				return ErrorCode.GM_CMD_ERROR_14;
			}
			
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			map.put(64000001, 0);
			map.put(64100001, 0);
			map.put(64000002, 0);
			map.put(64100002, 0);
			map.put(64000003, 0);
			map.put(64100003, 0);
			map.put(64000004, 0);
			map.put(64100004, 0);
			map.put(64000005, 0);
			map.put(64100005, 0);
			map.put(64000006, 0);
			map.put(64100006, 0);
			map.put(64000007, 0);
			map.put(64100007, 0);
			map.put(64000008, 0);
			map.put(64100008, 0);
			
			
			roleLoadInfo.setMutualFightCount(0);
			roleLoadInfo.setMutualFightLastTime(0);
			if (roleDao.updateHubiaoNum(roleId, (byte)0)
					||ActivityDao.getInstance().updateExpMoneyActivityLeftTimes(roleInfo.getId(),
							(byte)LBCNum1, (byte)LBCNum2, (byte)LBCNum3, (byte)LBCNum4, (byte)LBCNum5, (byte)LBCNum6,
							GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES )
							||roleDao.updateTodayYabiaoNum(roleId, (byte)0)
							||roleDao.updateHubiaoNum(roleId, (byte)0)
							||roleDao.updateRoleAttackAnotherInfo(roleId, (byte) 0, null)
							||roleDao.updateRoleDefendTime(roleId, (byte) 0, null)
							||roleDao.updateRoleMineNum(roleId, (byte) 0, null)
							||roleDao.updateRoleMinebuyNum(roleId, (byte) 0, null)
							||roleDao.updateRoleTeamChallengeTimesAndLastTime(roleId, map, 0, "")
							||roleDao.updateRoleTeam3V3TimesAndLastTime(roleId, (byte) 0, 0)
							||roleDao.updateRoleAttackAnotherInfo(roleId, (byte) 0, null)
							||roleDao.updateRoleDefendTime(roleId, (byte) 0, null)
							||roleDao.updateRoleMutualCounts(roleLoadInfo)){
				
				
				roleInfo.setExpLeftTimes1((byte)LBCNum1);
				roleInfo.setExpLeftTimes2((byte)LBCNum2);
				roleInfo.setExpLeftTimes3((byte)LBCNum3);
				roleInfo.setExpLeftTimes4((byte)LBCNum4);
				roleInfo.setExpLeftTimes5((byte)LBCNum5);
				roleInfo.setExpLeftTimes6((byte)LBCNum6);
				roleInfo.setMoneyLeftTimes(GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES);
				
				roleLoadInfo.setAttackAnotherTime((byte)0);
				roleLoadInfo.setLastAttackAnotherTime(null);
			
				roleLoadInfo.setDefendTime((byte)0);
				roleLoadInfo.setLastDefendTime(null);
				roleLoadInfo.setTodayJiebiaoNum((byte)0);
				roleLoadInfo.setTodayYabiaoNum((byte)0);
				roleLoadInfo.setHubiaoNum((byte)0);
				
				roleLoadInfo.setMineNum((byte)0);
				roleLoadInfo.setLastMineTime(null);
				roleLoadInfo.setBuyMine((byte)0);
				roleLoadInfo.setLastBuyMineTime(null);
				
				roleLoadInfo.setTeamChallengeTimes(map);
				roleLoadInfo.setTeamChallengeFightLastTime(0);
				roleLoadInfo.setTeamChallengeStr("");
				
				roleLoadInfo.setTeam3V3Times((byte)0);
				roleLoadInfo.setTeam3V3FightLastTime(0);
				
			} else {
				return ErrorCode.GM_CMD_ERROR_14;
			}
			return 1;
		}
	}
	
	@CommandMapping("kickClub")
	public int kickClub(RoleInfo roleInfo, String[] args){
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null){
			return 1;
		}
		
		String clubName = "";
		RoleInfo memberRole = null;
		if (args.length > 0 && args.length == 2) {
			memberRole = RoleInfoMap.getRoleInfoByName(args[0]);
			clubName = args[1];
		}
		
		if(memberRole == null){
			return 1;
		}
		
		RoleClubInfo clubInfo = null;
		
		if(!RoleClubInfoMap.getClubNameSet().contains(clubName)){
			//通过公会名
			for(RoleClubInfo info : RoleClubInfoMap.getAllClub().values()){
				if(info.getClubName().equals(clubName)){
					clubInfo = info;
					break;
				}
			}
			
		}else{
			//通过角色身上的公会
			int clubId = memberRole.getClubId();
			if(clubId <= 0){
				return 1;
			}
			
			clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
			
		}
		
		if(clubInfo == null){
			return 1;
		}
		
		Map<Integer, RoleClubMemberInfo> memberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(clubInfo.getId());
		
		if(memberMap == null || memberMap.size() <= 0 || !memberMap.containsKey(memberRole.getId())){
			return 1;
		}
		
		RoleClubMemberInfo info = memberMap.get(memberRole.getId());
		if(info == null){
			return 1;
		}
		
		if(memberMap.size() == 1){
			//只存在一个人并且是会长，解散公会
			
			if(!RoleClubMemberDao.getInstance().deleteAllRoleMemberInfoByClubId(clubInfo.getId())){
				return 1;
			}
			
			if(!RoleClubInfoDao.getInstance().deleteRoleClubInfo(clubInfo.getId())){
				return 1;
			}
			
			RoleClubEventDao.getInstance().deleteClubEventByClubId(clubInfo.getId());
			
			synchronized(GameFlag.ROLE_CLUB){
				RoleClubMemberInfoMap.removeClubMap(clubInfo.getId());
				RoleClubInfoMap.removeRoleIdMapByClubId(clubInfo.getId());
				RoleClubInfoMap.removeClubName(clubInfo.getClubName());
				RoleClubMemberInfoMap.removeClubEvent(clubInfo.getId());
				
			}
			
			memberRole.setClubId(0);
			
		}else{
			
			if(!ClubService.tryLock(memberRole)){
				return 1;
			}
			
			try{
				if(!RoleClubMemberDao.getInstance().deleteAllRoleMemberInfo4Refuse(info.getRoleId(), info.getClubId())){
					return 1;
				}
				
				//加入退出事件
				ClubEventInfo clubEventInfo = new ClubEventInfo();
				clubEventInfo.setClubId(clubInfo.getId());
				clubEventInfo.setEvent(1);
				clubEventInfo.setRoleId(memberRole.getId());
				clubEventInfo.setTime(new Timestamp(System.currentTimeMillis()));
				
				RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
				
				memberRole.setClubId(0);
				
				if(memberRole.getRoleLoadInfo() != null){
					memberRole.getRoleLoadInfo().getRoleClubMemberInfoSet().clear();
				}
				
				
				synchronized(GameFlag.ROLE_CLUB){
					RoleClubMemberInfoMap.removeClubMemberMap(info.getClubId(), info.getRoleId());
					RoleClubMemberInfoMap.addEvent(clubEventInfo);
				}
				
				synchronized (clubInfo) {
					if(clubInfo.getAdminSet().contains(memberRole.getId())){
						ClubService.removeAdmin(clubInfo, memberRole.getId());
					}
				}
				
				//佣兵结算
				if(memberRole.getHireHeroMap().size() > 0){
					synchronized(memberRole){
						ClubService.hireBalance(memberRole);
					}
				}
				
				
			}finally{
				ClubService.unLock(memberRole);
			}
			
		}
		
		//在场景中 则移除
		Set<Integer> set = ClubSceneInfoMap.getSceneRoleSet(clubInfo.getId(), info.getSceneId());
		if(set.contains(info.getRoleId())){
			set.remove(info.getRoleId());
			ClubSceneService.notifyDelClubSceneRole(info.getRoleId(), set);
		}
		
		//在场景中 切退出场景
		SceneService.sendRoleRefreshMsg(new GetOutClubSceneResp(1) , memberRole.getId(), Command.GET_OUT_CLUB_SCENE_RESP);

		//通知邮件服务器
		ClubService.send2MailServerSynClub(clubInfo.getId(), memberRole.getId(), 1);
		
		
		GameLogService.insertRoleClubLog(memberRole, memberRole.getClubId(), 2);
		
		// 公会名改变通知场景里的人
		SceneService1.roleNameUpdate(memberRole);
		
		//发送公会的ROOMID
		if(memberRole.getLoginStatus() == 1){
			SceneService.sendRoleRefreshMsg(new RoomIdMsgResp(0) , memberRole.getId(), Command.CLUB_ROOM_ID_RESP);
			
		}
		
		if(info.getStatus() == RoleClubMemberInfo.CLUB_BOSS){
			//如果是公会会在被踢 则转让
			resign(clubInfo);
		}
		
		
		return 1;
	}
	
	/**
	 * 检查会长是否流失
	 * @param roleClubInfo
	 */
	private void resign(RoleClubInfo roleClubInfo){
		RoleInfo roleInfo = null;
		//会长换人
		int srcRoleId = roleClubInfo.getCreateRoleId();
		int replceRoleId = -1;
		boolean flag = false;
		synchronized(roleClubInfo){
			RoleClubMemberInfo memberInfo = null;
			for(Integer roleId : roleClubInfo.getAdminSet()){
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				if(roleInfo == null){
					ClubService.removeAdmin(roleClubInfo, roleId);
					continue;
				}
				
				if(roleInfo.getLoginStatus() == 0 && DateUtil.compareDayBalance(roleInfo.getLogoutTime().getTime(), System.currentTimeMillis()) > 7){
					continue;
				}
				
				memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
				if(memberInfo == null){
					ClubService.removeAdmin(roleClubInfo, roleId);
					continue;
				}
				
				if(memberInfo.getStatus() == RoleClubMemberInfo.CLUB_ASSISTANT){
					flag = true;
					replceRoleId = memberInfo.getRoleId();
					break;
				}
				
			}
			
			if(!flag){
				//没找到, 继续找
				long max = 0;
				for(Integer roleId : roleClubInfo.getAdminSet()){
					roleInfo = RoleInfoMap.getRoleInfo(roleId);
					if(roleInfo == null){
						ClubService.removeAdmin(roleClubInfo, roleId);
						continue;
					}
					
					if(roleInfo.getLoginStatus() == 0 && DateUtil.compareDayBalance(roleInfo.getLogoutTime().getTime(), System.currentTimeMillis()) > 7){
						continue;
					}
					
					memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
					if(memberInfo == null){
						ClubService.removeAdmin(roleClubInfo, roleId);
						continue;
					}
					
					if(memberInfo.getStatus() == RoleClubMemberInfo.CLUB_LEADER){
						if(roleInfo.getClubContributionSum() >= max){
							//获得贡献值最多的官员
							max = roleInfo.getClubContributionSum();
							replceRoleId = memberInfo.getRoleId();
						}
					}
				}
			}
			
			
			if(replceRoleId < 0){
				//还没找到, 从所有会员中找贡献值累积最多的
				long max = 0;
				Map<Integer, RoleClubMemberInfo> map = RoleClubMemberInfoMap.getRoleClubMemberMap(roleClubInfo.getId());
				
				if(map != null && map.size() > 0){
					for(Integer roleId : map.keySet()){
						if(roleId == roleClubInfo.getCreateRoleId()){
							continue;
						}
						
						roleInfo = RoleInfoMap.getRoleInfo(roleId);
						if(roleInfo == null){
							continue;
						}
						
						memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
						if(memberInfo == null){
							continue;
						}
						
						if(roleInfo.getClubContributionSum() >= max){
							max = roleInfo.getClubContributionSum();
							replceRoleId = roleId;
						}
						
					}
				}
			}
			
			if(replceRoleId > -1 && RoleInfoMap.getRoleInfo(replceRoleId) != null){
				//有人 替换掉
				if(!RoleClubInfoDao.getInstance().updateRoleClubInfoCreateRoleId(roleClubInfo.getId(), replceRoleId)){
					return;
				}
				
				roleClubInfo.setCreateRoleId(replceRoleId);
				
			}else{
				return;
			}
			
		}
		
		if(RoleClubMemberDao.getInstance().updateClubMemberInfoStatus4Transaction(roleClubInfo.getCreateRoleId(), RoleClubMemberInfo.CLUB_BOSS, srcRoleId, RoleClubMemberInfo.CLUB_MEMBER, roleClubInfo.getId())){
			synchronized(GameFlag.ROLE_CLUB){
				RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), srcRoleId);
				if(memberInfo != null){
					memberInfo.setStatus(RoleClubMemberInfo.CLUB_MEMBER);
				}
				
				memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleClubInfo.getCreateRoleId());
				if(memberInfo != null){
					memberInfo.setStatus(RoleClubMemberInfo.CLUB_BOSS);
				}
			}
		}
		
		ClubEventInfo clubEventInfo = new ClubEventInfo();
		clubEventInfo.setClubId(roleClubInfo.getId());
		clubEventInfo.setRoleId(replceRoleId);
		clubEventInfo.setTime(new Timestamp(System.currentTimeMillis()));
		clubEventInfo.setEvent(12);
		
		RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
		
		synchronized(GameFlag.ROLE_CLUB){
			RoleClubMemberInfoMap.addEvent(clubEventInfo);
		}
		
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 10);
		
	}
}
