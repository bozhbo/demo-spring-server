package com.snail.webgame.game.protocal.rolemgt.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.client.Client;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.Flag;
import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.engine.common.util.FixedDes;
import com.snail.webgame.engine.common.util.IPCode;
import com.snail.webgame.engine.common.util.Sequence;
import com.snail.webgame.engine.db.SqlMapDaoSupport;
import com.snail.webgame.engine.db.session.client.SqlMapClient;
import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.FightCampaignInfoMap;
import com.snail.webgame.game.cache.FightInfoMap;
import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.MineInfoMap;
import com.snail.webgame.game.cache.RoleAddRquestMap;
import com.snail.webgame.game.cache.RoleBlackMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleFriendMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginMap;
import com.snail.webgame.game.cache.RoleLoginQueueInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.cache.TempMsgrMap;
import com.snail.webgame.game.cache.UserAccountMap;
import com.snail.webgame.game.cache.VoiceRoomMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameFlag;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.RoleLoginQueueInfo;
import com.snail.webgame.game.common.RoleLogoutInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.StageXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.ChargeGameService;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.GmccGameService;
import com.snail.webgame.game.dao.ActivityDao;
import com.snail.webgame.game.dao.GameLogDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.base.DbConstants;
import com.snail.webgame.game.dao.typehandler.IntegerListTypeHandler;
import com.snail.webgame.game.dao.typehandler.IntegerMapTypeHandler;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.ChargeAccountInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.FightCampaignInfo;
import com.snail.webgame.game.info.GameSettingInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.campaign.service.CampaignService;
import com.snail.webgame.game.protocal.challenge.queryBattleDetail.BattleDetailRe;
import com.snail.webgame.game.protocal.challenge.queryBattleDetail.ChapterDetailRe;
import com.snail.webgame.game.protocal.challenge.service.ChallengeService;
import com.snail.webgame.game.protocal.checkIn.service.CheckInService;
import com.snail.webgame.game.protocal.club.scene.service.ClubSceneService;
import com.snail.webgame.game.protocal.equip.query.EquipDetailRe;
import com.snail.webgame.game.protocal.equip.service.EquipService;
import com.snail.webgame.game.protocal.fight.competition.service.CompetitionService;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.funcopen.service.FuncOpenMgtService;
import com.snail.webgame.game.protocal.hero.query.HeroDetailRe;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.query.BagItemRe;
import com.snail.webgame.game.protocal.item.service.ItemMgtService;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityMgrService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.rank.service.RankInfo;
import com.snail.webgame.game.protocal.rank.service.RankService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.relation.entity.FriendDetailRe;
import com.snail.webgame.game.protocal.rolemgt.activateAccount.ActivateAccountReq;
import com.snail.webgame.game.protocal.rolemgt.activeCode.ActiveCodeReq;
import com.snail.webgame.game.protocal.rolemgt.changeName.ChangeNameReq;
import com.snail.webgame.game.protocal.rolemgt.changeName.ChangeNameResp;
import com.snail.webgame.game.protocal.rolemgt.check.CheckNameReq;
import com.snail.webgame.game.protocal.rolemgt.check.CheckNameResp;
import com.snail.webgame.game.protocal.rolemgt.checkTime.CheckTimeResp;
import com.snail.webgame.game.protocal.rolemgt.create.CreateRoleReq;
import com.snail.webgame.game.protocal.rolemgt.detail.RoleDetailResp;
import com.snail.webgame.game.protocal.rolemgt.disconnect.DisconnectReq;
import com.snail.webgame.game.protocal.rolemgt.info.QueryRoleInfoResp;
import com.snail.webgame.game.protocal.rolemgt.login.UserLoginReq;
import com.snail.webgame.game.protocal.rolemgt.login.UserLoginResp;
import com.snail.webgame.game.protocal.rolemgt.loginqueue.LoginQueueResp;
import com.snail.webgame.game.protocal.rolemgt.logout.UserLogoutResp;
import com.snail.webgame.game.protocal.rolemgt.verify.UserRoleRe;
import com.snail.webgame.game.protocal.rolemgt.verify.UserVerifyResp;
import com.snail.webgame.game.protocal.scene.biaocheEnd.BiaocheEndResp;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.sceneRefre.SceneRefreService;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.protocal.soldier.service.SoldierService;
import com.snail.webgame.game.protocal.vipshop.service.VipShopMgtService;
import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.cache.GuildTechXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropXMLInfoMap;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;

public class RoleMgtService extends SqlMapDaoSupport{

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 检查角色是否重复
	 * @param req
	 * @return
	 */
	public CheckNameResp checkRoleName(CheckNameReq req) {
		CheckNameResp resp = new CheckNameResp();
		String roleName = req.getRoleName();
		String account = req.getAccount();
		if (account == null || account.trim().length() == 0) {
			resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_6);
			return resp;
		}
		synchronized (GameFlag.OBJ_ROLE) {
			int result = RoleService.checkRoleName(roleName);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			resp.setResult(1);
			return resp;
		}
	}

	/**
	 * 修改角色名
	 * @param req
	 * @param roleId
	 * @return
	 */
	public ChangeNameResp updateRoleName(ChangeNameReq req, int roleId) {
		ChangeNameResp resp = new ChangeNameResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) 
		{
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_23);
			return resp;
		}
		String roleName = req.getRoleName().trim();
		
		synchronized (GameFlag.OBJ_ROLE) 
		{
			synchronized(roleInfo)
			{
				String oldName = roleInfo.getRoleName();
				// 判断名称是否合法
				if (roleInfo.getRoleName().equalsIgnoreCase(roleName))
				{
					resp.setResult(ErrorCode.ROLE_NAME_ERROR_12);
					return resp;
				}
				
				int result = RoleService.checkRoleName(roleName);
				if (result != 1) 
				{
					resp.setResult(result);
					return resp;
				}
				
				if (roleInfo.getChangeRoleNameTimes() > 0)
				{
					// 判断钱是否足够
					if (roleInfo.getCoin() < GameValue.ROLE_CHANGE_NAME_COST_COIN) 
					{
						resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
						return resp;
					}
					// 扣钱
					if (!RoleService.subRoleRoleResource(ActionType.action15.getType(), roleInfo, ConditionType.TYPE_COIN,
							GameValue.ROLE_CHANGE_NAME_COST_COIN , null))
					{
						resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_11);
						return resp;
					}
				}
				// 更新改名次数
				byte changeTimes = roleInfo.getChangeRoleNameTimes();
				if (changeTimes < Byte.MAX_VALUE)
				{
					changeTimes++;
				}
				// 改名
				if (RoleDAO.getInstance().updateRoleName(roleId, roleName, changeTimes)) 
				{
					roleInfo.setRoleName(roleName);
					roleInfo.setChangeRoleNameTimes(changeTimes);

					RoleInfoMap.changeRoleName(roleInfo, oldName);
				} 
				else 
				{
					resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_12);
					return resp;
				}
				// 更新排行榜名字
				RankInfo levelRankInfo = roleInfo.getLevelRankInfo();
				if (levelRankInfo != null) {
					levelRankInfo.setName(roleName);
				}
				RankInfo heroNumRankInfo = roleInfo.getHeroNumRankInfo();
				if (heroNumRankInfo != null) {
					heroNumRankInfo.setName(roleName);
				}
				RankInfo fightValueRankInfo = roleInfo.getFightValueRankInfo();
				if (fightValueRankInfo != null) {
					fightValueRankInfo.setName(roleName);
				}
				
				//更新异步竞技场名字
				FightArenaInfo arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleId);
				if(arenaInfo != null)
				{
					if(RoleDAO.getInstance().updateArenaRoleName(arenaInfo.getId(), roleName))
					{
						arenaInfo.setRoleName(roleName);
					}
				}
				GameLogService.insertRoleNameLog(roleInfo, oldName, roleName);
			}
		}
		sendToMailRoleNameChange(roleInfo);

		// 改名后通知其它人
		SceneService1.roleNameUpdate(roleInfo);
		RankService.notifySuperRChange();
//		SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, null);
		
		resp.setRoleName(roleName);
		resp.setResult(1);
		return resp;
	}

	/**
	 * 发送给邮件服角色名发生变化
	 * @param roleInfo
	 */
	public static void sendToMailRoleNameChange(RoleInfo roleInfo) {

		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			ChangeNameReq changeNameReq = new ChangeNameReq();
			changeNameReq.setRoleName(roleInfo.getRoleName());
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setUserID0((int) (roleInfo.getId()));
			header.setMsgType(Command.CHANGE_NAME_NOFI_MAIL);
			message.setHeader(header);
			message.setBody(changeNameReq);
			session.write(message);
		}
	}

	/**
	 * 创建角色
	 * @param req
	 * @param gateServerId
	 * @return
	 */
	public UserLoginResp createRoleInfo(CreateRoleReq req, short gateServerId) {
		UserLoginResp resp = new UserLoginResp();

		if (GameValue.IS_ALLOW_LOGIN == 0) {
			resp.setResult(ErrorCode.GAME_LOGIN_ERROR_1);
			return resp;
		}
		// 检查账号是否为空
		if (req.getAccount() == null || "".equals(req.getAccount())) {
			resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_1);
			return resp;
		}
		// 检查账号是否为空
		if (req.getRoleName() == null || "".equals(req.getRoleName())) {
			resp.setResult(ErrorCode.ROLE_NAME_ERROR_1);
			return resp;
		}
		String roleName = req.getRoleName().trim();
		String account = req.getAccount().toUpperCase();
		ChargeAccountInfo chargeInfo = UserAccountMap.getForChargeAccount(account);
		if (chargeInfo == null) {
			resp.setResult(ErrorCode.REQUEST_PARAM_ERROR_9);
			return resp;
		}
		synchronized (GameFlag.OBJ_ROLE) {

			// 登陆进去后，清除排队那边的可登陆缓存
			if (GameValue.GAME_LOGIN_QUEUE_FLAG == 1)
			{
				RoleLoginQueueInfoMap.removeMessageAccount(chargeInfo.getChargeAccount());
				RoleLoginQueueInfoMap.removeUserAccountLogout(chargeInfo.getChargeAccount());
			}
			
			if(RoleInfoMap.getMap().size() > GameValue.SERVER_ALL_ROLE_NUM)
			{
				resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_15);
				return resp;
			}
			
			if(RoleInfoMap.getOnlineSize() >= GameValue.GAME_ONLINE_MAX_NUM)
			{
				resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_16);
				return resp;
			}
			
			// 检查人物数量
			if (!RoleService.checkRoleAccountNum(account)) {
				resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_3);
				return resp;
			}
			
			//检查玩家是否选过相同类型的角色
			if(!RoleService.checkRoleInitHero(account,req.getHeroNo()))
			{
				resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_10);
				return resp;
			}

			int result = RoleService.checkRoleName(roleName);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			String uid = null;// 全服唯一标识符
			uid = account + "+" + GameConfig.getInstance().getGameServerId() + "+" + UUID.randomUUID().toString();

			int heroNo = req.getHeroNo();
			HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroNo);
			if (heroXMLInfo == null) {
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_13);
				return resp;
			}
			if (heroXMLInfo.getInitial() != 1) {
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_14);
				return resp;
			}

			long now = System.currentTimeMillis();
			RoleInfo roleInfo = new RoleInfo();
			roleInfo.setRoleLoadInfo(new RoleLoadInfo());

			roleInfo.setAccount(chargeInfo.getChargeAccount());
			roleInfo.setRoleName(roleName);
			roleInfo.setUid(uid);
			roleInfo.setRoleRace((byte) req.getRoleRace());
			roleInfo.setFightValue(0);

			// 基本货币、资源
			roleInfo.setMoney(GameValue.ROLE_INIT_MONEY);
			roleInfo.setTotalCoin(GameValue.ROLE_INIT_COIN);
			roleInfo.setCoin(GameValue.ROLE_INIT_COIN);
			roleInfo.setSp(GameValue.ROLE_INIT_SP_VAL);
			roleInfo.setEnergy(GameValue.ROLE_INIT_ENERGY_VAL);
			roleInfo.setLastRecoverSPTime(new Timestamp(now));
			roleInfo.setLastRecoverEnergyTime(new Timestamp(now));
			roleInfo.setTech(GameValue.ROLE_INIT_TECH_VAL);
			roleInfo.setLastRecoverTechTime(new Timestamp(now));

			roleInfo.setCreateTime(new Timestamp(now));
			roleInfo.setLoginTime(new Timestamp(now));
			roleInfo.setLogoutTime(new Timestamp(now));
			roleInfo.setLoginIp(IPCode.intToIP(req.getIP()));
			roleInfo.setClientType(req.getClientType());
			roleInfo.setMac(req.getMac());
			roleInfo.setPackageName(req.getPackageName());
			

			// 经验活动，银币活动
			int LBCNum1 = GameValue.ACTIVITY_EXP_TIMES_1 +  VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum2 = GameValue.ACTIVITY_EXP_TIMES_2 +  VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum3 = GameValue.ACTIVITY_EXP_TIMES_3 +  VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum4 = GameValue.ACTIVITY_EXP_TIMES_4 +  VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum5 = GameValue.ACTIVITY_EXP_TIMES_5 +  VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			int LBCNum6 = GameValue.ACTIVITY_EXP_TIMES_6 +  VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
			roleInfo.setExpLeftTimes1((byte)LBCNum1);
			roleInfo.setExpLeftTimes2((byte)LBCNum2);
			roleInfo.setExpLeftTimes3((byte)LBCNum3);
			roleInfo.setExpLeftTimes4((byte)LBCNum4);
			roleInfo.setExpLeftTimes5((byte)LBCNum5);
			roleInfo.setExpLeftTimes6((byte)LBCNum6);
			roleInfo.setMoneyLeftTimes(GameValue.ACTIVITY_MONYEY_MAX_ATTEND_TIMES);
			// 竞技场段位
			roleInfo.setCompetitionStage(StageXMLInfoMap.minStageXMLInfo.getId());
			roleInfo.setStageAward(StageXMLInfoMap.minStageXMLInfo.getStageBit());
			roleInfo.getRoleLoadInfo().setCompetitionAward(StageXMLInfoMap.minStageXMLInfo.getStageBit());
			// 缓存
			roleInfo.setLoginStatus((byte) 1);// 创建并登录
			roleInfo.setGateServerId(gateServerId);
			
			roleInfo.setScoreValue(GameValue.MUTUAL_FIGHT_INIT_SCOREVALUE);
			//创建角色时初始化膜拜字段
			roleInfo.setWorshipCount(GameValue.WORSHIP_MAX_NUM);
			
			//测试服玩家初始VIP5
			if(GameValue.TEST_SERVER_FLAG == 1)
			{
				roleInfo.setVipLv(GameValue.TEST_SERVER_ROLE_VIP_LV);
			}

			HeroInfo heroInfo = HeroService.initNewHeroInfo(roleInfo, heroXMLInfo, HeroInfo.DEPLOY_TYPE_MAIN);
			// 角色背包初始道具
			List<BagItemInfo> addBagItem = new ArrayList<BagItemInfo>();
			for (int propNo : GameValue.NEW_BAG_PROP) {
				PropXMLInfo xmlInfo = PropXMLInfoMap.getPropXMLInfo(propNo);
				if (xmlInfo == null) {
					continue;
				}
				int itemType = BagItemInfo.getItemType(propNo + "");
				if (itemType == 0) {
					continue;
				}
				addBagItem.add(new BagItemInfo(0, itemType, propNo, 1, xmlInfo.getColour(), 0));
			}

			// 角色背包初始装备
			EquipXMLInfo equipXMLInfo=EquipXMLInfoMap.getEquipXMLInfo(GameValue.NEW_BAG_EQUIP);
			if (equipXMLInfo == null) {
				resp.setResult(ErrorCode.EQUIP_INIT_ERROR_1);
				return resp;
			}
			List<EquipInfo> addBagEquips = new ArrayList<EquipInfo>();
			addBagEquips.add(new EquipInfo(equipXMLInfo.getNo(),equipXMLInfo.getEquipType(), (short) 0));

			if (RoleDAO.getInstance().insertRoleInfo(roleInfo, heroInfo,addBagItem, addBagEquips)) {
				// 用户缓存
				RoleInfoMap.addRoleInfo(roleInfo);
				RoleInfoMap.registerNum++;
				HeroInfoMap.addHeroInfo(heroInfo, false);
				if(roleInfo.getRoleLoadInfo()==null){
					roleInfo.setRoleLoadInfo(new RoleLoadInfo());
				}
				
				for(BagItemInfo item:addBagItem){
					BagItemMap.addBagItem(roleInfo, item);
				}
				
				for (EquipInfo bagEquipInfo : addBagEquips) {
					// 添加新纪录
					EquipService.refeshEquipProperty(bagEquipInfo);
					EquipInfoMap.addBagEquipInfo(roleInfo.getId(), bagEquipInfo);
				}
				
				 
				// 创建并登录后缓存处理
				userLoginCacheInit(roleInfo);
			} else {
				resp.setResult(ErrorCode.EQUIP_MERGE_ERROR_15);
				return resp;
			}

			roleInfo.getRoleLoadInfo().setId(roleInfo.getId());
			resp.setResult(1);
			resp.setRoleId((int) roleInfo.getId());
			resp.setAccount(roleInfo.getAccount());
			resp.setRoleName(roleInfo.getRoleName());
			resp.setGateServerId(roleInfo.getGateServerId());
			
			resp.setRoleRace(roleInfo.getRoleRace());
			resp.setPunishStatus(roleInfo.getPunishStatus());
			if (roleInfo.getPunishTime() != null) {
				resp.setPunishTime(roleInfo.getPunishTime().getTime());
			}
			resp.setGuildId((short)0);
			resp.setVipLevel((byte)roleInfo.getVipLv());
			resp.setIsAdvert(roleInfo.getIsAdvert());
			
			// 同一账号,同时只允许一个角色在线(新手进第一场战斗的时候没有走登录,所以在此也加一个)
			HashMap<Integer, RoleInfo> roleMap = RoleInfoMap.getRoleMapByAccount(account);
			if (roleMap != null && roleMap.size() > 0) {
				for (RoleInfo otherRole : roleMap.values()) {
					if (otherRole != null && otherRole.getId() != roleInfo.getId() && otherRole.getLoginStatus() == 1) {
						
						// 这个账户已经被人登录，通知通讯服务器断开这个链接
						UserLogoutResp resp1 = new UserLogoutResp();
						resp1.setResult(ErrorCode.USER_LOGIN_ERROR_2);
						resp1.setRoleId((int) otherRole.getId());
						List<UserRoleRe> userRoleRes = new ArrayList<UserRoleRe>();
						resp1.setUserRoleResSize(userRoleRes.size());
						resp1.setUserRoleRes(userRoleRes);
						RoleCmdSend.sendUserLogoutResp(otherRole, resp1);

						userLogoutDeal(otherRole);
						
						if(otherRole.getId() != roleInfo.getId())
						{
							RoleService.nofityMailRoleOut(otherRole.getId()+"");
						}

						if (logger.isInfoEnabled()) {
							logger.info("user logout by other user :roleId=" + roleInfo.getId() + ",newIP:"
									+ IPCode.intToIP(req.getIP()));
						}
					}
				}
			}

			return resp;
		}

	}

	/**
	 * 查询角色详细信息
	 * @param roleId
	 * @return
	 */
	public RoleDetailResp getRoleDetailResp(int roleId) {
		RoleDetailResp resp = new RoleDetailResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_6);
			return resp;
		}

		synchronized (roleInfo) {
			if (roleInfo.getRoleStatus() == 2) {
				resp.setResult(ErrorCode.ROLE_STATUS_ERROR_1);
				return resp;
			} else if (roleInfo.getRoleStatus() == 1
					&& roleInfo.getRoleStatusTime().getTime() > System.currentTimeMillis()) {
				resp.setResult(ErrorCode.ROLE_STATUS_ERROR_2);
				return resp;
			}

			// 获取用户信息前数据缓存处理
			int result = initRoleData(roleInfo);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}

			QueryRoleInfoResp roleInfoResp = getQueryRoleInfoResp(roleInfo);
			resp.setRoleInfo(roleInfoResp);

			// 武将
			List<HeroDetailRe> heroList = HeroService.getHeroDetailList(roleInfo, "");
			resp.setHeroCount(heroList.size());
			resp.setHeroList(heroList);

			// 装备
			List<EquipDetailRe> bagEquipList = EquipService.getBagEquipList(roleInfo, "");
			resp.setBagEquipCount(bagEquipList.size());
			resp.setBagEquipList(bagEquipList);

			// 用户背包
			List<BagItemRe> roleBagList = ItemMgtService.getRoleItem(roleInfo);
			resp.setRoleBagCount(roleBagList.size());
			resp.setRoleBagList(roleBagList);
			
			// 副本
			List<String> newList =  new ArrayList<String>();
			List<BattleDetailRe> challengeList = ChallengeService.getBattleList(roleInfo, newList);
			resp.setBattleCount(challengeList.size());
			resp.setBattleList(challengeList);
			String newBattle = "";
			if(newList != null && newList.size() > 0){
				for(String battle : newList){
					if(newBattle.length() <= 0){
						newBattle = battle;
					} else {
						newBattle = newBattle + "," + battle;
					}
				}
			}
			resp.setBattle(newBattle);
			
			// 领取过的宝箱
			List<ChapterDetailRe> prizeList = new ArrayList<ChapterDetailRe>();
			Map<Integer, List<Integer>> prizeMap1 = ChallengeService.getPrizeMap(roleInfo, (byte)1);
			if (prizeMap1 != null && prizeMap1.size() > 0) {
				for (int key : prizeMap1.keySet()) {
					ChapterDetailRe re = ChallengeService.getChapterDetailRe((byte)1, key, prizeMap1.get(key));
					if (re != null) {
						prizeList.add(re);
					}
				}
			}
			Map<Integer, List<Integer>> prizeMap2 = ChallengeService.getPrizeMap(roleInfo, (byte)2);
			if (prizeMap2 != null && prizeMap2.size() > 0) {
				for (int key : prizeMap2.keySet()) {
					ChapterDetailRe re = ChallengeService.getChapterDetailRe((byte)2, key, prizeMap2.get(key));
					if (re != null) {
						prizeList.add(re);
					}
				}
			}
			resp.setPrizeCount(prizeList.size());
			resp.setChapterList(prizeList);

			resp.setSp(roleInfo.getSp());
			if (roleInfo.getLastRecoverSPTime() != null) {
				resp.setLastRecoverSpTime(roleInfo.getLastRecoverSPTime().getTime());
			}
			resp.setEnergy(roleInfo.getEnergy());
			if (roleInfo.getLastRecoverEnergyTime() != null) {
				resp.setLastRecoverEnergyTime(roleInfo.getLastRecoverEnergyTime().getTime());
			}
			resp.setCurFuncOpenStr(roleInfo.getRoleLoadInfo().getFuncOpenStr());

			resp.setSoldierList(SoldierService.getSoldierInfoRe(roleInfo));
			resp.setSoldierCount(resp.getSoldierList().size());
			
			resp.setAccountId(roleInfo.getAccountId());
			resp.setIssuerID(roleInfo.getIssuerID());
			resp.setServerId(GameConfig.getInstance().getGameServerId());

			resp.setResult(1);
			resp.setDeployPosOpenStr(roleInfo.getRoleLoadInfo().getDeployPosOpenStr());
			resp.setTech(roleInfo.getTech());
			if (roleInfo.getLastRecoverTechTime() != null) {
				resp.setLastRecoverTechTime(roleInfo.getLastRecoverTechTime().getTime());
			}
			resp.setHmacStr(roleInfo.getHmacStr());
			
			if (roleInfo.getQihooUserId() != null) {
				resp.setQihooUserId(roleInfo.getQihooUserId());
			}
			
			if (roleInfo.getQihooToken() != null) {
				resp.setQihooToken(roleInfo.getQihooToken());
			}
			

			//称号
			TitleService.userLoginTitleCheck(roleInfo, resp);
			
			resp.setShowPlanId(roleInfo.getIsShowShizhuang());
			resp.setLockShizhuang(IntegerMapTypeHandler.getString(roleInfo.getLockShizhuang()));
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo != null){
				resp.setHaveReward(IntegerListTypeHandler.getString(roleLoadInfo.getHaveReward()));
			}
			
			GameSettingInfo info = GameSettingMap.getValue(GameSettingKey.POP_UP_BOX_TYPE);
			if(info != null){
				resp.setPopUpType(NumberUtils.toInt(info.getValue()));
			}
			
			info = GameSettingMap.getValue(GameSettingKey.TOP_SYSTEM_NOTICE);
			if(info != null){
				resp.setSystemNotice(info.getValue());
			}
			
			if(roleInfo.getClubId() > 0){
				//只有存在公会的玩家才会取
				if(roleInfo.getClubTechPlusInfo() == null || "".equals(roleInfo.getClubTechPlusInfo())){
					if(RoleDAO.getInstance().updateRoleClubTechPlusInfo(roleInfo.getId(), GameValue.INIT_ROLE_CLUB_TECH_PLUS)){
						roleInfo.setClubTechPlusInfo(GameValue.INIT_ROLE_CLUB_TECH_PLUS);
						
					}
				}
			}
			
			if(roleInfo.getClubTechPlusInfo() != null && !"".equals(roleInfo.getClubTechPlusInfo())){
				//公会科技个性相关属性
				StringBuffer sb = new StringBuffer();
				try{
					int buildType = 0;
					int lv = 0;
					int xmlNo = 0;
					String[] strs = roleInfo.getClubTechPlusInfo().split(";");
					for(String str : strs){
						String[] subStrs = str.split(":");
						if(subStrs.length != 2){
							continue;
						}
						
						buildType = Integer.parseInt(subStrs[0]);
						lv = Integer.parseInt(subStrs[1]);
						
						xmlNo = GuildTechXMLInfoMap.getXmkNoByBuildTypeAndLevel(buildType, lv);
						
						if(xmlNo > 0){
							sb.append(xmlNo).append(":");
						}
						
					}
					
				}catch (Exception e) {
					if(logger.isErrorEnabled()){
						logger.error("tech role plus parse error", e);
					}
				}
				
				if(sb.toString().endsWith(":")){
					resp.setClubTechXmlNo(sb.substring(0, sb.length() - 1));
				}else{
					resp.setClubTechXmlNo(sb.toString());
				}
				
			}
			
			// 红点全部检测
			RedPointMgtService.check2PopRedPoint(roleId, null, true, RedPointMgtService.ALL_TYPES);
		}
		
		// 对攻战角色下线处理
		MutualService.getMutualService().roleLogout(roleInfo);
		
		RoleService.sendWorldChatLimit2MailServer(roleInfo, 0);
		
		return resp;
	}

	/**
	 * 查询角色信息
	 * @param roleId
	 * @param reqRoleId
	 * @param roleName
	 * @return
	 */
	public QueryRoleInfoResp getRoleInfo(int roleId) {
		QueryRoleInfoResp resp = new QueryRoleInfoResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_7);
			return resp;
		}

		synchronized (roleInfo) {
			if (roleInfo.getRoleStatus() == 2) {
				resp.setResult(ErrorCode.ROLE_STATUS_ERROR_3);
				return resp;
			} else if (roleInfo.getRoleStatus() == 1
					&& roleInfo.getRoleStatusTime().getTime() > System.currentTimeMillis()) {
				resp.setResult(ErrorCode.ROLE_STATUS_ERROR_4);
				return resp;
			}
			resp = getQueryRoleInfoResp(roleInfo);
			return resp;
		}
	}

	/**
	 * 获取QueryRoleInfoResp
	 * @param roleInfo
	 * @return
	 */
	private QueryRoleInfoResp getQueryRoleInfoResp(RoleInfo roleInfo) {
		QueryRoleInfoResp resp = new QueryRoleInfoResp();
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		resp.setResult(1);
		resp.setRoleId((int) roleInfo.getId());
		
		if (roleInfo.getUid() != null) {
			String[] strs = roleInfo.getUid().split("\\+");
			
			if (strs.length == 3) {
				resp.setUid(strs[2]);
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("UID is error format, UID = " + roleInfo.getUid());
				}
			}
		}
		
		resp.setRoleName(roleInfo.getRoleName());
		resp.setRoleRace((byte) roleInfo.getRoleRace());
		resp.setMaxFightValue(roleInfo.getFightValue());

		resp.setMoney((int) roleInfo.getMoney());
		resp.setCoin((int) roleInfo.getCoin());	
		resp.setClubContribution(roleInfo.getClubContribution());
		if (roleLoadInfo != null) {
			resp.setKuafuMoney(roleLoadInfo.getKuafuMoney());
			resp.setExploit(roleLoadInfo.getExploit());
			resp.setCourage((int)roleLoadInfo.getCourage());
			resp.setJustice((int)roleLoadInfo.getJustice());
			resp.setActiveVal(roleLoadInfo.getTodayActive());
			resp.setTeamMoney(roleLoadInfo.getTeamMoney());

			resp.setBuySpTime((byte) roleLoadInfo.getTodayBuySpNum());
			resp.setBuyMoneyTime((byte) roleLoadInfo.getTodayBuyMoneyNum());
			resp.setBuyEnergyTime((byte)roleLoadInfo.getTodayBuyEnergyNum());
			resp.setRoleCompetitionState(roleLoadInfo.getInFight());
			
			if (roleLoadInfo.getMutualFightLastTime() == 0) {
				// 第一次
				resp.setMutualFightTimes((byte) GameValue.MUTUAL_DAILY_FIGHT_COUNTS);
				roleLoadInfo.setMutualFightCount(0);
			} else {
				if (DateUtil.isSameDay(roleLoadInfo.getMutualFightLastTime(), System.currentTimeMillis())) {
					// 同一天
					byte leftTimes = (byte) (GameValue.MUTUAL_DAILY_FIGHT_COUNTS - roleLoadInfo.getMutualFightCount());
					
					resp.setMutualFightTimes(leftTimes < 0 ? 0 : leftTimes);
				} else {
					// 已经隔天
					resp.setMutualFightTimes((byte) GameValue.MUTUAL_DAILY_FIGHT_COUNTS);
					roleLoadInfo.setMutualFightCount(0);
				}
			}
			
			if (roleLoadInfo.getTeamChallengeFightLastTime() == 0) {
				// 第一次
				resp.setTeamChallengeTimes("");
				roleLoadInfo.setTeamChallengeTimes(null);
			} else {
				if (DateUtil.isSameDay(roleLoadInfo.getTeamChallengeFightLastTime(), System.currentTimeMillis())) {
					// 同一天
					resp.setTeamChallengeTimes(roleLoadInfo.getTeamChallengeAllTimesStr());
				} else {
					// 已经隔天
					resp.setTeamChallengeTimes("");
					roleLoadInfo.setTeamChallengeTimes(null);
				}
			}
			
			if (roleLoadInfo.getTeam3V3FightLastTime() == 0) {
				// 第一次
				resp.setTeam3V3Times((byte) 0);
				roleLoadInfo.setTeam3V3Times((byte) 0);
			} else {
				if (DateUtil.isSameDay(roleLoadInfo.getTeam3V3FightLastTime(), System.currentTimeMillis())) {
					// 同一天
					resp.setTeam3V3Times(roleLoadInfo.getTeam3V3Times());
				} else {
					// 已经隔天
					resp.setTeam3V3Times((byte) 0);
					roleLoadInfo.setTeam3V3Times((byte) 0);
				}
			}
			//狭路相逢次数
			int XLXFNum = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.XLXFDB_NUM)+GameValue.ATTACK_ANOTHER_EVERY_DAY_TIME;
			if(roleLoadInfo.getLastAttackAnotherTime()==null){//没玩过对攻
				resp.setAttackFightTimes((byte)XLXFNum);
			}else{
				if(!DateUtil.isSameDay(roleLoadInfo.getLastAttackAnotherTime().getTime(), System.currentTimeMillis())&&
						roleLoadInfo.getAttackAnotherTime()>0){
					//隔天了并且有玩的次数就重置玩的次数
					synchronized (roleInfo) {
						Timestamp now = new Timestamp(System.currentTimeMillis());
						if(RoleDAO.getInstance().updateRoleAttackAnotherInfo(roleInfo.getId(), (byte)0, now)){
							roleLoadInfo.setAttackAnotherTime((byte)0);
							roleLoadInfo.setLastAttackAnotherTime(new Timestamp(System.currentTimeMillis()));
						}else{
							resp.setResult(ErrorCode.ATTACK_ANOTHER_ERROR_5);
							return resp;
						}
					}
				}
				resp.setAttackFightTimes((byte)(XLXFNum - roleLoadInfo.getAttackAnotherTime()));
			}
			
			//押镖次数
			if(roleLoadInfo.getBiaocheQueryTime() == null || !DateUtil.isSameDay(System.currentTimeMillis(), roleLoadInfo.getBiaocheQueryTime().getTime()))
			{
				synchronized (roleInfo) 
				{
					if(!RoleDAO.getInstance().resetAllBiaocheNum(roleInfo.getId()))
					{
						resp.setResult(ErrorCode.BIAO_CHE_ERROR_44);
						return resp;
					}
					roleLoadInfo.setRefBiaoCheNum((byte) 0);
					roleLoadInfo.setTodayJiebiaoNum((byte) 0);
					roleLoadInfo.setTodayYabiaoNum((byte) 0);
					roleLoadInfo.setHubiaoNum((byte)0);
					roleLoadInfo.setBiaocheQueryTime(new Timestamp(System.currentTimeMillis()));
				}
			}
			int yabiaoNum = SceneMgtService.getTodayFreeYaBiaoNum(roleInfo) -  roleLoadInfo.getTodayYabiaoNum();
			resp.setBiaoCheTimes((byte)yabiaoNum);
			
			resp.setPvp3Money((int)roleLoadInfo.getPvp3Money());
			resp.setBuySkillPointTime((short)roleLoadInfo.getTodayBuyTechNum());
		}
		int LBCNum1 = GameValue.ACTIVITY_EXP_TIMES_1 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum2 = GameValue.ACTIVITY_EXP_TIMES_2 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum3 = GameValue.ACTIVITY_EXP_TIMES_3 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum4 = GameValue.ACTIVITY_EXP_TIMES_4 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum5 = GameValue.ACTIVITY_EXP_TIMES_5 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);
		int LBCNum6 = GameValue.ACTIVITY_EXP_TIMES_6 + VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.LBCDB_NUM);

		if(LBCNum1 < roleInfo.getExpLeftTimes1() || LBCNum2 < roleInfo.getExpLeftTimes2() || LBCNum3 < roleInfo.getExpLeftTimes3()
				|| LBCNum4 < roleInfo.getExpLeftTimes4() || LBCNum5 < roleInfo.getExpLeftTimes5() || LBCNum6 < roleInfo.getExpLeftTimes6())
		{
			synchronized (roleInfo) 
			{
				if(ActivityDao.getInstance().updateAllExpActivityLeftTimes(LBCNum1, LBCNum2, LBCNum3, LBCNum4, LBCNum5, LBCNum6, (int)roleInfo.getId()))
				{
					roleInfo.setExpLeftTimes1((byte)LBCNum1);
					roleInfo.setExpLeftTimes2((byte)LBCNum2);
					roleInfo.setExpLeftTimes3((byte)LBCNum3);
					roleInfo.setExpLeftTimes4((byte)LBCNum4);
					roleInfo.setExpLeftTimes5((byte)LBCNum5);
					roleInfo.setExpLeftTimes6((byte)LBCNum6);
				}
			}
		}
		//练兵场次数
		int dayofWeek = DateUtil.getDayofWeek();
		if(dayofWeek == 1){
			dayofWeek = 7;
		} else {
			dayofWeek--;
		}
		// 判断是否活动开启
		if(GameValue.ACTIVITY_EXP_TYPE1.contains(String.valueOf(dayofWeek))){
			resp.setExpLeftTimes1(roleInfo.getExpLeftTimes1());
		}
		if(GameValue.ACTIVITY_EXP_TYPE2.contains(String.valueOf(dayofWeek))){
			resp.setExpLeftTimes2(roleInfo.getExpLeftTimes2());
		}
		if(GameValue.ACTIVITY_EXP_TYPE3.contains(String.valueOf(dayofWeek))){
			resp.setExpLeftTimes3(roleInfo.getExpLeftTimes3());
		}
		if(GameValue.ACTIVITY_EXP_TYPE4.contains(String.valueOf(dayofWeek))){
			resp.setExpLeftTimes4(roleInfo.getExpLeftTimes4());
		}
		if(GameValue.ACTIVITY_EXP_TYPE5.contains(String.valueOf(dayofWeek))){
			resp.setExpLeftTimes5(roleInfo.getExpLeftTimes5());
		}
		if(GameValue.ACTIVITY_EXP_TYPE6.contains(String.valueOf(dayofWeek))){
			resp.setExpLeftTimes6(roleInfo.getExpLeftTimes6());
		}
		//攻城略地次数
		int GCLDNum = 0;
		FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleInfo.getId());
		if(info != null)
		{
			int resetNum = info.getCurrResetNum();
			int resetLimit = info.getCurrResetLimit();
			GCLDNum = resetLimit - resetNum;
		}
		else
		{
			GCLDNum = VipXMLInfoMap.getVipVal(roleInfo.getVipLv(), VipType.GCLD_NUM);
		}
		
		resp.setMoneyLeftTimes((byte)GCLDNum);
		resp.setChangeRoleNameTimes(roleInfo.getChangeRoleNameTimes());
		
		if (VoiceRoomMap.worldVoiceInfo != null) {
			resp.setWorldRoomId(VoiceRoomMap.worldVoiceInfo.getRoomId());
		}
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		
		if (roleClubInfo != null && roleClubInfo.getVoiceInfo() != null) {
			resp.setClubRoomId(roleClubInfo.getVoiceInfo().getRoomId());
		}
		
		return resp;
	}

	/**
	 * 获取排队队列
	 * @param roleId
	 * @return
	 */
	public LoginQueueResp getLoginQueueResp(String account) {
		LoginQueueResp resp = new LoginQueueResp();
		account = account.trim().toUpperCase();
		resp.setResult(1);

		int index = RoleLoginQueueInfoMap.getIndex(account);

		if (index == -1) {
			resp.setResult(-1);
			return resp;
		}

		resp.setIndex(index);
		resp.setNum(RoleLoginQueueInfoMap.getListSize());
		resp.setAccount(account);

		return resp;
	}

	/**
	 * 移除断线账号
	 * @param account
	 */
	public void checkLoginQueue(String account) {
		account = account.trim().toUpperCase();
		RoleLoginQueueInfoMap.removeQueueInfo(account);
	}

	/**
	 * 正常或者异常断线后数据处理
	 * @param roleInfo
	 */
	public void disconnect(String account,int roleId,DisconnectReq req) {
		if(roleId > 0)
		{
			RoleInfo roleInfo = (RoleInfo) RoleInfoMap.getRoleInfo(roleId);
			if (roleInfo != null) {
				byte disconnectPhase = 0;
				
				synchronized (roleInfo) {
					roleInfo.setDisconnectPhase(req.getDisconnectPhase());
				}
					
				disconnectPhase = req.getDisconnectPhase();
				
				if(disconnectPhase == 1)
				{
					//暂时断开
					// 记录登出时间
					RoleLoginQueueInfoMap.changeAccountLogoutTime(roleInfo.getAccount());
					System.out.println("[--------disconnectPhase:"+disconnectPhase+"---------]");
				}
				else if(disconnectPhase == 2)
				{
					//暂时断开后重新连接上了
					RoleLoginQueueInfoMap.removeUserAccountLogout(roleInfo.getAccount());
					roleInfo.setLoginStatus((byte) 1);
					System.out.println("[--------disconnectPhase:"+disconnectPhase+"---------]");
					
					// 超长时间重连后，缓存可能清除，这时重新获取
					if (roleInfo.getServerStatus() == 0) {
						initRoleData(roleInfo);
					}
					
					// 重连成功后推送新的角色信息
					RoleDetailResp resp = getRoleDetailResp(roleId);
					Message message = new Message();
					GameMessageHead head = new GameMessageHead();
					head.setMsgType(Command.QUERY_ROLE_DETAIL_RESP);
					message.setHeader(head);
					message.setBody(resp);
					
					IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());

					if (session != null && session.isConnected()){
						session.write(message);
					}
				}
				else if(disconnectPhase == 3)
				{
					//临时断开超时(超过12小时gate服的reconnect-flag)
					System.out.println("[--------disconnectPhase:"+disconnectPhase+"---------]");
					
					UserAccountMap.cleanForChargeAccount(roleInfo.getAccount());
				}
				else if(disconnectPhase == 31)
				{
					//临时断开未超时(超过5分钟gate服的disconnect-notify,未超过12小时),角色状态置为下线状态
					System.out.println("[--------disconnectPhase:"+disconnectPhase+"---------]");
					
					// 用户登出后缓存处理
					synchronized (roleInfo) {
						userLogoutDeal(roleInfo);
					}
					RoleService.nofityMailRoleOut(roleInfo.getId()+"");
				}
				else if(disconnectPhase == 4)
				{
					//直接断开
					System.out.println("[--------disconnectPhase:"+disconnectPhase+"---------]");
					// 用户登出后缓存处理
					synchronized (roleInfo) {
						userLogoutDeal(roleInfo);
					}
					
					UserAccountMap.cleanForChargeAccount(roleInfo.getAccount());
					
					RoleService.nofityMailRoleOut(roleInfo.getId()+"");
				}
				
				
				if (disconnectPhase == 1) {
					// 断开连接
					MutualService.getMutualService().roleLogout(roleInfo);
				}
			}
		}
		else if(account != null && !"".equals(account))
		{
			System.out.println("[--------disconnect,roleId = 0,account=:"+account);
			if(RoleLoginQueueInfoMap.isMessageLogin(account.toUpperCase()))
			{
				// 记录登出时间,只账号登陆，没登陆角色的情况
				RoleLoginQueueInfoMap.changeAccountLogoutTime(account.toUpperCase());
				// 删除缓存
				RoleLoginQueueInfoMap.removeMessageAccount(account.toUpperCase());
			}
			else
			{
				//退出排队
				RoleLoginQueueInfoMap.removeQueueInfo(account.toUpperCase());
			}
			
		}
	}

	/**
	 * 客户端更换角色（退出当前账号返回角色列表信息）
	 * @param roleId
	 */
	public UserLogoutResp userLogout(int roleId) {
		UserLogoutResp resp = new UserLogoutResp();
		RoleInfo roleInfo = (RoleInfo) RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_24);
			return resp;
		}
		
		// 用户登出后缓存处理
		userLogoutDeal(roleInfo);

		String account = roleInfo.getAccount().trim().toUpperCase();
		List<UserRoleRe> list = getUserRoleReList(account);
		if (list == null) {
			resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_8);
			return resp;
		}

		resp.setResult(1);
		resp.setRoleId((int) roleId);
		resp.setUserRoleRes(list);
		resp.setUserRoleResSize(list.size());
		
		RoleService.nofityMailRoleOut(roleId+"");
		
		// 对攻战角色下线处理
		MutualService.getMutualService().roleLogout(roleInfo);
	
		return resp;
	}

	/**
	 * 用户登出后缓存处理
	 * @param roleInfo
	 * @param recordLogOutLog 直接记录登录登出日志
	 */
	private static void userLogoutDeal(RoleInfo roleInfo) {
		
		// 处理镖车相关
		clearBiaocheRelation(roleInfo);
		
		synchronized(roleInfo)
		{
			// 登出缓存处理
			//roleInfo.setGateServerId((short)0);
			long nowTime = System.currentTimeMillis();
			roleInfo.setLogoutTime(new Timestamp(nowTime));
			// 用户下线标识
			roleInfo.setLoginStatus((byte) 0);
			
			RoleLoginMap.removeRoleInfo(roleInfo.getId());
			
			// 更新角色上下线的日志
			List<String> loginIdList = new ArrayList<String>();
			String loginLogId = roleInfo.getLoginLogId();
			if(loginLogId != null && loginLogId.length() > 0)
			{
				loginIdList.add(loginLogId);
				roleInfo.setLoginLogId("");
			}
			
			GameLogDAO.getInstance().updateRoleLog(loginIdList, roleInfo.getLogoutTime());
			
			List<RoleLogoutInfo> outList = new ArrayList<RoleLogoutInfo>();
			outList.add(new RoleLogoutInfo(roleInfo));
			
			// 玩家下线，更新下线时间及保存武将战力
			RoleDAO.getInstance().updateLoginOut(outList);

			// 玩家下线,场景处理
			SceneService1.userLoginOutExecu(roleInfo);
			
			
			// 玩家下线后 公会场景中的角色判定
			ClubSceneService.savePoints(roleInfo);
			
			// 取消PVP竞技场报名
			if (roleInfo.getRoleLoadInfo() != null && (roleInfo.getRoleLoadInfo().getInFight() == 1 || roleInfo.getRoleLoadInfo().getInFight() == 5)) {
				roleInfo.getRoleLoadInfo().setInFight((byte) 0);
				
				if(roleInfo.getRoleLoadInfo().getInFight() == 1)
				{
					CompetitionService.cancelCompetition(roleInfo, (byte) 1, 0, 0);
				}
			}

			if(roleInfo.getRoleLoadInfo() != null) {
				// 清除红点记录
				roleInfo.getRoleLoadInfo().setRedPoint(new byte[GameValue.REDPOINT_NO]);
			}

			// 发送角色登出到GMCC
			if (GameValue.GAME_GMCC_FLAG == 1) {
				GmccGameService.getService().sendRoleLogout((int) roleInfo.getId(), roleInfo.getAccount());
				roleInfo.setSendGmccFlag(false);
			}

			// pve 战斗中途退出，强制退出处理
			FightService.dealFightOut(roleInfo);
			
			//推送好友状态 通知已改变

//			Set<Integer> set = RoleFriendMap.getRoleFriendKeySet();
//			
//			notifyClientRoleLogou4changeStatus(1, set, roleInfo.getId(), roleInfo.getLoginStatus());
//			
//			set = RoleBlackMap.getBlackRoleKeySet();
//			
//			notifyClientRoleLogou4changeStatus(2, set, roleInfo.getId(),  roleInfo.getLoginStatus());
//			
//			set = RoleAddRquestMap.getRequestKeySet();
//			
//			notifyClientRoleLogou4changeStatus(3, set, roleInfo.getId(),  roleInfo.getLoginStatus());
			
			// 结算上一次在线时间
			OpActivityMgrService.calLogoutOnlineTime(roleInfo);
		}
		
	}
	
	
	
	/**
	 * 处理镖车相关
	 * 
	 * @param roleInfo
	 */
	public static void clearBiaocheRelation(RoleInfo roleInfo) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		if(roleLoadInfo == null)
		{
			return;
		}
		
		if(roleLoadInfo.getYabiaoFriendRoleId() != 0)
		{
			//护镖人下线
			roleInfo.setFightEndClearBiaoche(true);
			RoleInfo yaBiaoRole = RoleInfoMap.getRoleInfo(roleLoadInfo.getYabiaoFriendRoleId());
			if(yaBiaoRole == null)
			{
				return;
			}
			
			if(roleInfo.getRoleLoadInfo().getInFight() != 2 && roleInfo.getRoleLoadInfo().getInFight() != 4)
			{
				// 护镖人非战斗时下线
				roleLoadInfo.setYabiaoFriendRoleId(0);
				MapRolePointInfo yabiaoRolePointInfo = MapRoleInfoMap.getMapPointInfo(yaBiaoRole.getId());
				
				if(yabiaoRolePointInfo != null){
					yabiaoRolePointInfo.setBiaocheOtherRoleId(0);
				}
				MapRolePointInfo helpPointInfo = MapRoleInfoMap.getMapPointInfo(roleInfo.getId());
				
				if (helpPointInfo != null) {
					helpPointInfo.setBiaocheOtherRoleId(0);
					helpPointInfo.setStatus((byte) 0);
				}
				yaBiaoRole.getRoleLoadInfo().setHubiaoRoleId(0);
				SceneService1.brocastRolePointStatus(yabiaoRolePointInfo, yaBiaoRole);

				BiaocheEndResp resp1 = new BiaocheEndResp();
				resp1.setResult(ErrorCode.BIAO_CHE_ERROR_43);
				resp1.setRoleName(roleInfo.getRoleName());
				//提示押镖人，护镖结束
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.BIAO_CHE_END_RESP);
				header.setUserID0(yaBiaoRole.getId());
				message.setHeader(header);
				message.setBody(resp1);
				SceneRefreService.sendRoleRefreshMsg(yaBiaoRole.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
				roleInfo.setFightEndClearBiaoche(false);
			}
			
		}
		else if(roleLoadInfo.getHubiaoRoleId() != 0)
		{
			// 押镖人下线
			roleInfo.setFightEndClearBiaoche(true);
			RoleInfo huBiaoRole = RoleInfoMap.getRoleInfo(roleLoadInfo.getHubiaoRoleId());
			if(huBiaoRole == null)
			{
				return;
			}
			
			if(roleInfo.getRoleLoadInfo().getInFight() != 2 && roleInfo.getRoleLoadInfo().getInFight() != 4)
			{
				// 押镖人非战斗时下线
				huBiaoRole.getRoleLoadInfo().setYabiaoFriendRoleId(0);
				
				if(roleInfo.getRoleLoadInfo().getHubiaoRoleId() != 0){
					MapRolePointInfo yabiaoRolePointInfo = MapRoleInfoMap.getMapPointInfo(roleInfo.getId());
					
					if(yabiaoRolePointInfo != null){
						yabiaoRolePointInfo.setBiaocheOtherRoleId(0);
					}
					MapRolePointInfo helpPointInfo = MapRoleInfoMap.getMapPointInfo(roleLoadInfo.getHubiaoRoleId());
					
					if (helpPointInfo != null) {
						helpPointInfo.setBiaocheOtherRoleId(0);
						helpPointInfo.setStatus((byte) 0);
					}
					SceneService1.brocastRolePointStatus(helpPointInfo, huBiaoRole);
				}
				roleLoadInfo.setHubiaoRoleId(0);
				
				BiaocheEndResp resp1 = new BiaocheEndResp();
				resp1.setResult(ErrorCode.BIAO_CHE_ERROR_42);
				resp1.setRoleName(roleInfo.getRoleName());
				//提示护镖人,押镖结束
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.BIAO_CHE_END_RESP);
				header.setUserID0(huBiaoRole.getId());
				message.setHeader(header);
				message.setBody(resp1);
				SceneRefreService.sendRoleRefreshMsg(huBiaoRole.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
				roleInfo.setFightEndClearBiaoche(false);
			}
		}
		else
		{
			//此人为劫镖人或正常人
		}
	}

	/**
	 * 获取登录结果
	 * @param account
	 * @param gateServerId
	 * @param sequenceId
	 * @return
	 */
	public UserLoginResp getUserLoginResp(UserLoginReq req, String chargeAcc, GameMessageHead head) {
		UserLoginResp resp = new UserLoginResp();

		int roleId = req.getRoleId();
		String account = chargeAcc.trim().toUpperCase();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_8);
			return resp;
		}
		
		if(RoleInfoMap.getOnlineSize() >= GameValue.GAME_ONLINE_MAX_NUM)
		{
			resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_16);
			return resp;
		}
		
		
		// 同一账号,同时只允许一个角色在线
		HashMap<Integer, RoleInfo> roleMap = RoleInfoMap.getRoleMapByAccount(account);
		if (roleMap != null && roleMap.size() > 0) 
		{
			synchronized(roleMap)
			{
				for (RoleInfo otherRole : roleMap.values())
				{
					if (otherRole != null) 
					{
						if(otherRole.getLoginStatus() == 1)
						{
							// 这个账户已经被人登录，通知通讯服务器断开这个链接
							UserLogoutResp resp1 = new UserLogoutResp();
							resp1.setResult(ErrorCode.USER_LOGIN_ERROR_2);
							resp1.setRoleId((int) otherRole.getId());
							List<UserRoleRe> userRoleRes = new ArrayList<UserRoleRe>();
							resp1.setUserRoleResSize(userRoleRes.size());
							resp1.setUserRoleRes(userRoleRes);
							RoleCmdSend.sendUserLogoutResp(otherRole, resp1);

							userLogoutDeal(otherRole);
							
							if(otherRole.getId() != roleInfo.getId())
							{
								RoleService.nofityMailRoleOut(otherRole.getId()+"");
							}

							if (logger.isInfoEnabled()) {
								logger.info("user logout by other user :roleId=" + roleInfo.getId() + ",newIP:"
										+ IPCode.intToIP(req.getIP()));
							}
						}
					}
				}
				
				// 清除护镖人,劫镖人
				clearBiaocheRelation(roleInfo);
				
				synchronized (roleInfo)
				{
					// 玩家在断线一段时间内重新走登录流程进入游戏，清除之前断线后场景中未清除的数据
					if(roleInfo.getDisconnectPhase() == 1 || roleInfo.getDisconnectPhase() == 3)
					{
						SceneService1.userLoginOutExecu(roleInfo);
						roleInfo.setDisconnectPhase((byte)0);
					}
					
					long now = System.currentTimeMillis();
					roleInfo.setLoginStatus((byte) 1);// 用户登录标识
					roleInfo.setGateServerId((short)head.getUserID1());
					roleInfo.setLoginTime(new Timestamp(now));
					String ipStr = IPCode.intToIP(req.getIP());
					roleInfo.setLoginIp(ipStr);
					roleInfo.setMd5Pass(req.getMd5Pass());
					roleInfo.setValidate(req.getValidate());
					
					//角色登录状态修改
					RoleDAO.getInstance().updateRoleLogin(roleId,ipStr,req.getMac(), req.getPackageName(), req.getClientType());

					// 登录后缓存初始化
					userLoginCacheInit(roleInfo);
					
					// 清除战斗缓存
					FightInfoMap.removeFightInfoByRoleId(roleInfo.getId());
					MineInfoMap.removeFightInfo(roleInfo.getId());
					
					
					//通知好友上线
//					Set<Integer> set = RoleFriendMap.getRoleFriendKeySet();
//					
//					notifyClientRoleLogou4changeStatus(1, set, roleInfo.getId(), roleInfo.getLoginStatus());
//					
//					set = RoleBlackMap.getBlackRoleKeySet();
//					
//					notifyClientRoleLogou4changeStatus(2, set, roleInfo.getId(), roleInfo.getLoginStatus());
//					
//					set = RoleAddRquestMap.getRequestKeySet();
//					
//					notifyClientRoleLogou4changeStatus(3, set, roleInfo.getId(), roleInfo.getLoginStatus());

					resp.setResult(1);
					resp.setRoleId((int) roleInfo.getId());
					resp.setAccount(roleInfo.getAccount());
					resp.setRoleName(roleInfo.getRoleName());
					resp.setGateServerId(roleInfo.getGateServerId());

					resp.setRoleRace(roleInfo.getRoleRace());
					resp.setPunishStatus(roleInfo.getPunishStatus());
					resp.setPunishTime(roleInfo.getCurrPunishTime());
					
					resp.setGuildId((short)roleInfo.getClubId());
					
					int vipLv = roleInfo.getVipLv();
					resp.setVipLevel((byte)vipLv);
					resp.setIsAdvert(roleInfo.getIsAdvert());

					// 登陆的时候查询计费点数和礼物
					if (Flag.flag == 0) {
						int sequenceId1 = Sequence.getSequenceId();
						GameMessageHead gameMessageHead = new GameMessageHead();
						gameMessageHead.setUserID0((int) roleInfo.getId());
						Message message = new Message();
						message.setHeader(gameMessageHead);
						TempMsgrMap.addMessage(sequenceId1, message);
						try {
							ChargeGameService.getChargeService().sendQueryGift(sequenceId1, roleInfo.getAccountId());
							TempMsgrMap.addMessage(sequenceId1, message);
							if (logger.isInfoEnabled()) {
								logger.info("#####getUserLoginResp" + ",sequenceId1=" + sequenceId1+",accountId="+roleInfo.getAccountId()
										+",account="+roleInfo.getAccount());
							}
						} catch (Exception e) {
							if (logger.isInfoEnabled()) {
								logger.error("", e);
							}
						}
					}
				}
			}
		}
		else
		{
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_8);
			return resp;
		}
		return resp;
	}

	/**
	 * 用户登录后缓存处理
	 * @param roleInfo
	 */
	private void userLoginCacheInit(RoleInfo roleInfo) {
		
		RoleLoginMap.addRoleInfo(roleInfo.getId(), roleInfo);
		ChargeAccountInfo chargeAccountInfo = UserAccountMap.getForChargeAccount(roleInfo.getAccount().toUpperCase());
		if (chargeAccountInfo != null) {
			roleInfo.setAccountId(chargeAccountInfo.getAccountId());
			roleInfo.setGmRight((byte) chargeAccountInfo.getGmLevel());
			roleInfo.setAccInfo(chargeAccountInfo.getAccInfo());
			roleInfo.setIssuerID(chargeAccountInfo.getIssuerID());
			roleInfo.setHmacStr(chargeAccountInfo.getHmacStr());
			roleInfo.setQihooUserId(chargeAccountInfo.getQihooUserId());
			roleInfo.setQihooToken(chargeAccountInfo.getQihooToken());
		}
		else
		{
			logger.info("#############can not get from UserAccountMap,account="+roleInfo.getAccount().toUpperCase());
		}
		
		// 记录最大在线人数
		RoleInfoMap.recalMaxOnlineNum();
		


		// 发送角色登录到GMCC
		if (GameValue.GAME_GMCC_FLAG == 1) {
			GmccGameService.getService().sendRoleLogin((int) roleInfo.getId(), roleInfo.getAccount(),
					roleInfo.getRoleName());
			roleInfo.setSendGmccFlag(true);
		}
		// 登录日志
		Timestamp now = new Timestamp(System.currentTimeMillis());
		GameLogService.insertRoleLog(roleInfo, roleInfo.getLoginIp(),roleInfo.getMac()+"",roleInfo.getClientType()+"", now,now,"");
	}

	/**
	 * 获取用户信息前数据缓存处理
	 * @param roleInfo
	 */
	private int initRoleData(RoleInfo roleInfo) {
		
		SqlMapClient client = null;
		try{
			client = getSqlMapClient(DbConstants.GAME_DB,false);
			// 加载相关信息
			if (roleInfo.getServerStatus() == 0) {
				// 加载相关信息
				if (!RoleService.loadRoleOtherData(roleInfo,client)) {
					return ErrorCode.LOAD_ROLE_DATA_1;
				}
				// 数据初始化过标识
				roleInfo.setServerStatus((byte) 1);
				// 初始化角色属性
				RoleService.recalRoleInfo(roleInfo);
				// 武将
				HeroService.loginInitHeroProperty(roleInfo);
				// 初始化装备背包属性
				EquipService.loginInitBagEquipProperty(roleInfo);
				// 检测功能开启
				FuncOpenMgtService.checkIsHasFuncOpen(roleInfo, false,client);
				// 初始化关卡血量，战斗力
				CampaignService.loginInitCampaignProperty(roleInfo);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			if(client != null) {
				client.rollback();
			}
		}
		finally
		{
			if(client != null)
			{
				client.commit();
			}
		}
		
		

		// 记录7天签到
		CheckInService.plusOneDayForCheckIn7Days(roleInfo, false);
		// 计算登录后体力恢复
		RoleService.timerRecoverSp(roleInfo);
		// 计算登录后精力恢复
		RoleService.timerRecoverEnergy(roleInfo);
		// 计算登录后技能点恢复
		RoleService.timerRecoverTech(roleInfo);
		
		// 月签到同步处理
		CheckInService.dealCheckInSync(roleInfo, false);
		// vip特权礼包登录处理
		VipShopMgtService.dealVipBuyAward(roleInfo, false);
		// 登录后检测任务变化
		QuestService.checkQuest(roleInfo, 0, null, false, false);
		// 运营活动登录检测
		OpActivityMgrService.dealOpActivityCheck(roleInfo, false);
		// 运营礼包版本检测
		VipShopMgtService.checkRoleBoxVersionChg(roleInfo);
		return 1;
	}

	/**
	 * 帐号验证 (不登录)
	 * @param charegeAccount
	 * @return
	 */
	public static UserVerifyResp getUserVerifyResp(String chargeAccount, GameMessageHead head) {
		UserVerifyResp resp = new UserVerifyResp();
		if (chargeAccount == null || chargeAccount.length() <= 0) {
			resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_4);
			return resp;
		}
		String account = chargeAccount.trim().toUpperCase();
		List<UserRoleRe> list = getUserRoleReList(account);
		
		resp.setResult(1);
		resp.setAccount(account);
		resp.setAcccode(FixedDes.encrypt(account));
		resp.setList(list);
		resp.setCount(resp.getList().size());
		return resp;
	}

	/**
	 * 获取用户验证 结果
	 * @param accountId
	 * @return
	 */
	public static List<UserRoleRe> getUserRoleReList(String account) {
		List<UserRoleRe> list = new ArrayList<UserRoleRe>();
		Map<Integer, RoleInfo> roleMap = RoleInfoMap.getRoleMapByAccount(account);
		if (roleMap != null) {
			for (RoleInfo roleInfo : roleMap.values()) {
				if (!roleInfo.getAccount().equalsIgnoreCase(account.trim())) {
					continue;
				}

				UserRoleRe re = new UserRoleRe();
				re.setRoleId((int) roleInfo.getId());
				re.setRoleName(roleInfo.getRoleName());
				re.setRoleRace(roleInfo.getRoleRace());
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if (heroInfo == null) {
					return null;
				}
				re.setRoleLevel(heroInfo.getHeroLevel());
				re.setHeroNo(heroInfo.getHeroNo());

				re.setRoleStatus(roleInfo.getCurrRoleStatus());
				re.setRoleStatusTime(roleInfo.getCurrRoleStatusTime());

				list.add(re);
			}
		}

		return list;
	}
	
	
	/**
	 * 服务器各时间保存关闭
	 */
	public static void allUserLoginOut()
	{
		long now = System.currentTimeMillis();
		List<RoleLoadInfo> roleLoadList = new ArrayList<RoleLoadInfo>();
		List<String> loginIdList = new ArrayList<String>();
		List<RoleLogoutInfo> outList = new ArrayList<RoleLogoutInfo>();
		
		Set<Integer> set = RoleLoginMap.getSet();
		for(Integer roleId:set)
		{
			RoleInfo roleInfo =  RoleLoginMap.getRoleInfo(roleId);
			if(roleInfo!=null)
			{
				RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
				if (roleLoadInfo == null)
				{
					continue;
				}
				
				// 更新角色上下线的日志
				String loginLogId = roleInfo.getLoginLogId();
				if(loginLogId != null && loginLogId.length() > 0)
				{
					loginIdList.add(loginLogId);
					roleInfo.setLoginLogId("");
				}
				
				outList.add(new RoleLogoutInfo(roleInfo));
				
				// 结算上一次在线时间
				long curOnlineTime = 0;
				if (roleLoadInfo.getLastOnlineTime() != null) 
				{
					curOnlineTime = now - roleLoadInfo.getLastOnlineTime().getTime() <= 0 ? 0 : now - roleLoadInfo.getLastOnlineTime().getTime();
				}
				
				roleLoadInfo.setTotalTodayOnline(roleLoadInfo.getTotalTodayOnline() + curOnlineTime);
				roleLoadInfo.setLastOnlineTime(new Timestamp(now));
				
				roleLoadList.add(roleLoadInfo);
			}
		}
		
		// 更新下线时间
		RoleDAO.getInstance().updateLoginOut(outList);
		
		// 更新角色下线日志
		GameLogDAO.getInstance().updateRoleLog(loginIdList, new Timestamp(now));
		
		// 更新所有玩家在线时长
		RoleDAO.getInstance().batchUupdateRoleOnlineTime(roleLoadList);
	}

	/**
	 * 序列号兑换
	 * @param roleId
	 * @param req
	 * @return 返回值
	 */
	public void activeCode(int roleId, ActiveCodeReq req) {

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		
		synchronized (roleInfo) {
			int sequenceId = Sequence.getSequenceId();
			GameMessageHead gameMessageHead = new GameMessageHead();
			gameMessageHead.setUserID0((int) roleInfo.getId());
			Message message = new Message();
			message.setHeader(gameMessageHead);
			message.setBody(req);
			TempMsgrMap.addMessage(sequenceId, message);
			try {
				if (req.getRedeemCode().length() == 16) {
					// 长度16的为兑换码
					ChargeGameService.getChargeService().sendActiveCodeGiftCharge(sequenceId, req.getRedeemCode(), null,
							roleInfo.getAccountId(), 1, GameConfig.getInstance().getGameServerId(), roleInfo.getLoginIp(),null);
					if (logger.isInfoEnabled()) {
						logger.info("#####activeCode11" + ",sequenceId=" + sequenceId+",accountId="+roleInfo.getAccountId()+",redeemCode="+req.getRedeemCode());
					}
				} else {
					// 其他为通码
					ChargeGameService.getChargeService().sendCommonCode(sequenceId, roleInfo.getAccountId(), req.getRedeemCode());
					logger.info("#####activeCode22" + ",sequenceId=" + sequenceId+",accountId="+roleInfo.getAccountId()+",redeemCode="+req.getRedeemCode());
				}
				
			} catch (Exception e) {
				if (logger.isInfoEnabled()) {
					logger.error("", e);
				}
			}
		}

	}

	/**
	 * 账号激活
	 * @param req
	 */
	public void activateAccount(ActivateAccountReq req, IoSession ioSession, GameMessageHead gameMessageHead) {

		int sequenceId = Sequence.getSequenceId();
		Message message = new Message();
		message.setBody(req);
		message.setHeader(gameMessageHead);
		TempMsgrMap.addMessage(sequenceId, message);
		try {
			ChargeGameService.getChargeService().sendActivePassport(sequenceId, req.getActivateCode(),
					req.getAccount(), GameConfig.getInstance().getGameType(),
					GameConfig.getInstance().getGameServerId(), "");
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.error("", e);
			}
		}

	}
	
	/**
	 * 通知客户端改变好友功能角色状态
	 * @param set
	 */
	private static void notifyClientRoleLogou4changeStatus(int flag, Set<Integer> set, int roleId, int status){
		RoleInfo relRoleInfo = null;
		for(Integer relRoleId : set){
			relRoleInfo = RoleInfoMap.getRoleInfo(relRoleId);
			if(relRoleInfo == null){
				continue;
			}
			
			if(flag == 1 && RoleFriendMap.getRoleFriendIdSet(relRoleId).contains(roleId)
					&& relRoleInfo.getLoginStatus() == 1){
				//通知客户端
				
				notifySent(relRoleId, roleId, 10, status);
				
			}else if(flag == 2 && RoleBlackMap.getBlackRoleIdSet(relRoleId).contains(roleId)
					&& relRoleInfo.getLoginStatus() == 1){
				//通知客户端
				notifySent(relRoleId, roleId, 11, status);
				
			}else if(flag == 3 && RoleAddRquestMap.getAddRequestRoleIdSet(relRoleId).contains(roleId)
					&& relRoleInfo.getLoginStatus() == 1){
				//通知客户端
				
				notifySent(relRoleId, roleId, 12, status);
			}
			
		}
	}
	
	/**
	 * 推送消息给客户端
	 * @param relRoleId
	 */
	private static void notifySent(int relRoleId,int roleId, int type, int status){
		FriendDetailRe friendDetailRe = new FriendDetailRe();
		friendDetailRe.setRoleId(roleId);
		friendDetailRe.setStatus(status);
		
		SceneService1.sendMessage2ChangeRoleRelation(friendDetailRe, relRoleId, type);
	}

	/**
	 * 判断是否需要排队   1-不需要排队   else-不需要
	 * 
	 * @param account
	 * @param head
	 * @return
	 */
	public static int isNotNeedQuene(String account, GameMessageHead head){

		if (GameValue.GAME_LOGIN_QUEUE_FLAG == 1) {
			
			// 判断玩家下线时间是否超过6分钟,没超过6分钟则不管是否到达最大在线人数都让其登陆游戏
			Long lastLogoutTime = RoleLoginQueueInfoMap.getAccountLogoutTime(account); // 账号最后登录时间
			if(lastLogoutTime > 0)
			{
				boolean isTimeOut = RoleService.isLogoutTimeOut(lastLogoutTime);// 是否下线太久超时  true-太久，超过6分钟
				
				// 短时间下线
				if(!isTimeOut){
					return 1;
				}
			}
			
			// 已经在待登区
			if(RoleLoginQueueInfoMap.isMessageLogin(account)){
				return 1;
			}
			
			//游戏内人数已满,进入排队
			if(RoleLoginQueueInfoMap.getPermitNum() <= 0)
			{
				RoleLoginQueueInfoMap.addQueueInfo(new RoleLoginQueueInfo(account, head));
				LoginQueueResp queueResp = RoleService.getLoginQueueResp(account);
				
				RoleCmdSend.sendLoginQueueResp(queueResp, head);
				logger.info("need add loginQueue,onlineNum="+RoleInfoMap.getOnlineSize()+",could login num="+RoleLoginQueueInfoMap.getLoginSetNum()
						+",index="+queueResp.getIndex()+",allNum="+queueResp.getNum()+",account="+account);
				return ErrorCode.GAME_LOGIN_ERROR_3;
			}
		}
		
		logger.info("no need add loginQueue,congratulation");
		return 1;
	}
	
	
	public static CheckTimeResp checkTime(int roleId)
	{
		CheckTimeResp resp = new CheckTimeResp();
		resp.setResult(1);
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null)
		{
			resp.setResult(ErrorCode.CHECK_TIME_ERROR_1);
			return resp;
		}
		synchronized(roleInfo)
		{
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null)
			{
				resp.setResult(ErrorCode.CHECK_TIME_ERROR_1);
				return resp;
			}
			
			long curTime = System.currentTimeMillis();
			long preTime = roleLoadInfo.getServerCheckTime();
			if(preTime == 0)
			{
				roleLoadInfo.setServerCheckTime(curTime);
			}
			else if(curTime - preTime < 1400)
			{
				roleLoadInfo.setServerCheckTime(curTime);
				roleLoadInfo.setServerCheckTimeNum(roleLoadInfo.getServerCheckTimeNum()+1);
				if(roleLoadInfo.getServerCheckTimeNum() > 5)
				{
					if(GameValue.CHECK_TIME_FLAG == 1)
					{
						resp.setResult(ErrorCode.CHECK_TIME_ERROR_2);
						return resp;
					}
				}
				GameLogService.insertCheckTimeLog(roleInfo, curTime, preTime,roleLoadInfo.getServerCheckTimeNum());
			}
			else
			{
				if(curTime - preTime > 2250)
				{
					logger.info("#####role delay time,count="+roleInfo.getAccount()+",ip="+roleInfo.getLoginIp()+",delaytime="+(curTime - preTime));
				}
				roleLoadInfo.setServerCheckTime(curTime);
				roleLoadInfo.setServerCheckTimeNum(0);
			}
		}
		return resp;
		}
}
