package com.snail.webgame.game.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.epilot.ccf.core.protocol.Message;
import org.epilot.ccf.core.protocol.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.GameSettingMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameSettingKey;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.info.GameSettingInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.PresentEnergyInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;
import com.snail.webgame.game.protocal.campaign.query.QueryCampaignHeroResp;
import com.snail.webgame.game.protocal.campaign.query.QueryCampaignResp;
import com.snail.webgame.game.protocal.campaign.service.CampaignMgtService;
import com.snail.webgame.game.protocal.challenge.refresh.RefreshBattlesResp;
import com.snail.webgame.game.protocal.checkIn.queryList.QueryCheckInListResp;
import com.snail.webgame.game.protocal.checkIn.service.CheckInMgtService;
import com.snail.webgame.game.protocal.checkIn7Day.queryList.QueryCheckIn7DayListResp;
import com.snail.webgame.game.protocal.fightdeploy.query.QueryFightDeployReq;
import com.snail.webgame.game.protocal.fightdeploy.query.QueryFightDeployResp;
import com.snail.webgame.game.protocal.fightdeploy.service.FightDeployMgtService;
import com.snail.webgame.game.protocal.funcopen.notify.NotifyFuncOpenResp;
import com.snail.webgame.game.protocal.gem.query.QueryGemResp;
import com.snail.webgame.game.protocal.gem.service.GemMgtService;
import com.snail.webgame.game.protocal.goldBuy.query.QueryGoldBuyReq;
import com.snail.webgame.game.protocal.goldBuy.query.QueryGoldBuyResp;
import com.snail.webgame.game.protocal.goldBuy.service.GoldBuyMgtService;
import com.snail.webgame.game.protocal.guide.query.QueryGuideResp;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;
import com.snail.webgame.game.protocal.hero.query.QueryHeroReq;
import com.snail.webgame.game.protocal.hero.query.QueryHeroResp;
import com.snail.webgame.game.protocal.hero.service.HeroMgtService;
import com.snail.webgame.game.protocal.item.query.QueryEquipResp;
import com.snail.webgame.game.protocal.item.query.QueryItemResp;
import com.snail.webgame.game.protocal.item.service.ItemMgtService;
import com.snail.webgame.game.protocal.opactivity.firstcharge.query.QueryFirstChargeResp;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityMgrService;
import com.snail.webgame.game.protocal.opactivity.sevenday.query.QuerySevenDayResp;
import com.snail.webgame.game.protocal.opactivity.timeaction.querytype.QueryAllTimeActionTypeResp;
import com.snail.webgame.game.protocal.opactivity.wonder.query.QueryWonderResp;
import com.snail.webgame.game.protocal.quest.query.QueryQuestReq;
import com.snail.webgame.game.protocal.quest.query.QueryQuestResp;
import com.snail.webgame.game.protocal.quest.service.QuestMgrService;
import com.snail.webgame.game.protocal.recruit.query.QueryChestResp;
import com.snail.webgame.game.protocal.recruit.service.ChestMgtService;
import com.snail.webgame.game.protocal.relation.entity.FriendDetailRe;
import com.snail.webgame.game.protocal.relation.entity.FriendDetailResp;
import com.snail.webgame.game.protocal.rolemgt.info.QueryRoleInfoResp;
import com.snail.webgame.game.protocal.rolemgt.notice.NotifyUpdateResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;
import com.snail.webgame.game.protocal.rolemgt.sourceRefresh.NotifyRefSourceResp;
import com.snail.webgame.game.protocal.rolemgt.sp.NotifyRefEnergyResp;
import com.snail.webgame.game.protocal.rolemgt.sp.NotifyRefSpResp;
import com.snail.webgame.game.protocal.rolemgt.sp.NotifyRefTechResp;
import com.snail.webgame.game.protocal.store.query.QueryStoreReq;
import com.snail.webgame.game.protocal.store.query.QueryStoreResp;
import com.snail.webgame.game.protocal.store.service.StoreMgtService;
import com.snail.webgame.game.protocal.vipshop.query.QueryVipShopResp;
import com.snail.webgame.game.protocal.vipshop.querysalebox.QuerySaleBoxResp;
import com.snail.webgame.game.protocal.vipshop.queryvipbuy.QueryVipBuyResp;
import com.snail.webgame.game.protocal.vipshop.service.VipShopMgtService;
import com.snail.webgame.game.protocal.weapon.NotifyNewWeaponResp;
import com.snail.webgame.game.protocal.weapon.query.RoleWeaponInfoRe;
import com.snail.webgame.game.thread.ScreanNotifyThread;

public class SceneService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 刷新类型
	 */
	public static final int REFRESH_TYPE_COMMON = 99;// 普通刷新
	public static final int REFESH_TYPE_ROLE = 1;// 角色刷新
	public static final int REFESH_TYPE_HERO = 2;// 英雄刷新
	public static final int REFESH_TYPE_GUIDE = 3;// 推送剧情

	public static final int REFESH_TYPE_EQUIP_BAG = 6;// 用户装备背包刷新
	public static final int REFESH_TYPE_ITEM = 7;// 用户道具背包刷新
	public static final int REFESH_TYPE_EQUIP = 8;// 用户英雄装备刷新
	public static final int REFESH_TYPE_FIGHT_DEPLOY = 10;// 用户布阵信息刷新
	public static final int REFESH_TYPE_QUEST = 11;// 用户任务刷新
	public static final int REFRESH_TYPE_CHALLENGE_BATTLE = 13;// 副本刷新
	public static final int REFRESH_TYPE_CHALLENGE_PRIZE = 14;// 章节奖励刷新
	public static final int REFRESH_TYPE_STORE_ITEM = 15;// 商店商品刷新
	public static final int REFRESH_TYPE_GOLD_BUY = 4;// 金币购买刷新
	public static final int REFESH_TYPE_RECRUIT = 5;// 抽卡刷新
	public static final int REFRESH_TYPE_GEM = 17;// 用户宝石活动刷新
	public static final int REFRESH_TYPE_CAMPAIGN = 12;// 用户宝物活动刷新(攻城略地)
	public static final int REFRESH_TYPE_CAMPAIGN_HERO = 18;// 用户宝物活动武将信息刷新
	public static final int REFRESH_TYPE_DEPLOY = 19;// 上阵武将信息刷新
	public static final int REFRESH_TYPE_SP = 20;// 用户体力刷新
	public static final int REFRESH_TYPE_FUNCOPEN = 21;// 用户功能开启刷新
	public static final int REFRESH_TYPE_WEAPON = 22;// 获得新神兵刷新
	public static final int REFRESH_TYPE_ENERGY = 23;// 用户精力刷新
	public static final int REFRESH_TYPE_GUIDE = 24;// 新手引导刷新
	public static final int REFRESH_TYPE_GOLD_MONEY_COURAGE_JUSTICE = 25;// 金子,银子,勇气令,正义令刷新（客户端未实现方法,可以立即返回变化的不建议调用此方法）
	public static final int REFRESH_TYPE_ADD_FRIEND_REQUEST = 26;// 添加好友请求
	public static final int REFRESH_TYPE_FRIEND_LIST = 27;// 添加好友请求
	public static final int REFRESH_TYPE_REMOVE_FRIEND = 28;// 移除好友请求
	public static final int REFRESH_TYPE_PRESENT_ENERGY = 29;// 移除赠送精力请求
	public static final int REFRESH_TYPE_TECH = 30;// 用户技能点刷新
	
	public static final int REFRESH_TYPE_CHARGE = 31;// 刷新用户充值相关(包括充值礼包和vip等级)
	public static final int REFRESH_TYPE_COIN_BOX = 32;// 刷新金币礼包信息
	public static final int REFRESH_TYPE_OPACT = 33;// 刷新运营时限活动信息
	
	public static final int REFRESH_TYPE_FIRST = 34;// 刷新用户首冲活动信息
	public static final int REFRESH_TYPE_SEVEN = 35;// 刷新用户7日活动信息
	public static final int REFRESH_TYPE_WONDER = 36;// 刷新用户精彩活动信息
	
	public static final int REFRESH_TYPE_VIP_BUY = 37;// 刷新用户vip特权礼包购买信息
	public static final int REFRESH_TYPE_CHECKIN_7DAY = 38;// 开服礼包
	public static final int REFRESH_TYPE_CHECKIN = 39;// 签到礼包
	
	public static final int REFRESH_TYPE_SYSTEM_NOTIFY = 40;// 聊天框置顶公告推送
	
	/**
	 * 通知客户端刷新相关信息
	 * @param roleId 角色ID
	 * @param type 刷新类型
	 * @param reserve
	 */
	public static void sendRoleRefreshMsg(int roleId, int type, Object reserve) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		//  1-在线
		if (roleInfo != null && roleInfo.getLoginStatus() == 1) {
			if(roleInfo.getDisconnectPhase() == 1){
				// 1-临时断开
				return;
			}
			
			ScreanNotifyRunnable r = new ScreanNotifyRunnable(roleInfo, type, reserve);
			if (ScreanNotifyThread.getInstance().isCanAdd()) {
				ScreanNotifyThread.getInstance().run(r);
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("Notify the client to refresh the thread queue is full......" + "roleId=" + roleId
							+ ",type=" + type + ",reserve=" + reserve);
				}
			}
		}
	}
	
	public static void sendRoleRefreshMsg(MessageBody obj, int roleId, int msgType) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		//  1-在线
		if (roleInfo != null && roleInfo.getLoginStatus() == 1) {
			if(roleInfo.getDisconnectPhase() == 1){
				// 1-临时断开
				return;
			}
			
			ScreanNotifyRunnable r = new ScreanNotifyRunnable(roleInfo, REFRESH_TYPE_COMMON, msgType, obj);
			if (ScreanNotifyThread.getInstance().isCanAdd()) {
				ScreanNotifyThread.getInstance().run(r);
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("Notify the client to refresh the thread queue is full......" + "roleId=" + roleId+",msgType="+msgType);
				}
			}
		}
	}

	/**
	 * 角色信息刷新
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_1(int roleId) {
		RoleMgtService service = new RoleMgtService();
		QueryRoleInfoResp resp = service.getRoleInfo(roleId);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_ROLE_INFO_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);

		return message;
	}

	/**
	 * 英雄信息刷新
	 * @param roleId
	 * @param idStr
	 * @return
	 */
	public static Message notifyType_2(int roleId, String idStr) {
		HeroMgtService service = new HeroMgtService();
		QueryHeroReq req = new QueryHeroReq();
		req.setIdStr(idStr);
		QueryHeroResp resp = service.queryHero(roleId, req);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_HERO_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 金币购买刷新
	 * @param roleId
	 * @param obj
	 * @return
	 */
	public static Message notifyType_4(int roleId, String buyType) {
		Message message = new Message();
		GoldBuyMgtService service = new GoldBuyMgtService();
		QueryGoldBuyReq req = new QueryGoldBuyReq();
		req.setBuyType(Byte.parseByte(buyType));
		QueryGoldBuyResp resp = service.queryGoldBuy(roleId, req);

		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_ROLE_GOLD_BUY_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 抽卡刷新
	 * 
	 * @param roleId
	 * @param idStr
	 * @return
	 */
	public static Message notifyType_5(int roleId) {
		ChestMgtService service = new ChestMgtService();
		QueryChestResp resp = service.queryRecruit(roleId);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_RECRUIT_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 用户背包装备刷新
	 * @param roleId
	 * @param idStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Message notifyType_6(int roleId, Object idStr) {
		ItemMgtService service = new ItemMgtService();

		QueryEquipResp resp = service.refeshEquip((int) roleId, (List<Integer>) idStr);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_EQUIP_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 用户背包刷新
	 * @param roleId
	 * @param idStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Message notifyType_7(int roleId, Object idStr) {
		ItemMgtService service = new ItemMgtService();

		QueryItemResp resp = service.refeshItem((int) roleId, (Map<Integer,Integer>) idStr);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_ITEM_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 用户布阵信息刷新
	 * @param roleId
	 * @param noStr
	 * @return
	 */
	public static Message notifyType_10(int roleId, Object noStr) {
		FightDeployMgtService service = new FightDeployMgtService();
		QueryFightDeployReq req = new QueryFightDeployReq();
		req.setDeployType((Byte) noStr);
		QueryFightDeployResp resp = service.queryFightDeploy(roleId, req);

		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_FIGHT_DEPLOY_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 用户任务刷新
	 * @param roleId
	 * @param noStr
	 * @return
	 */
	public static Message notifyType_11(int roleId, String noStr) {
		QuestMgrService service = new QuestMgrService();
		QueryQuestReq req = new QueryQuestReq();
		req.setNoStr(noStr);
		QueryQuestResp resp = service.queryQuest(roleId, req);

		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_QUEST_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 副本刷新
	 * @param roleId
	 * @param obj
	 * @return
	 */
	public static Message notifyType_13(int roleId, Object obj) {
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.REFRESH_CHALLENGE_BATTLE);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody((RefreshBattlesResp) obj);
		return message;
	}

	/**
	 * 商店商品刷新
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_15(int roleId, Object obj) {
		StoreMgtService service = new StoreMgtService();
		QueryStoreReq req = new QueryStoreReq();
		req.setStoreType(((Integer) obj).byteValue());

		QueryStoreResp resp = service.queryStore((int) roleId, req);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_STORE_ITEM_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 用户宝石活动刷新
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_17(int roleId) {
		GemMgtService service = new GemMgtService();
		QueryGemResp resp = service.queryGem(roleId);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_GEM_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 用户宝物活动刷新
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_12(int roleId) {
		CampaignMgtService service = new CampaignMgtService();
		QueryCampaignResp resp = service.queryCampaign(roleId);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_CAMPAIGN_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 用户宝物活动武将信息刷新
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_18(int roleId, String heroIdStr) {
		CampaignMgtService service = new CampaignMgtService();
		QueryCampaignHeroResp resp = service.refreshCampaignHero(roleId, heroIdStr);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_CAMPAIGN_HERO_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 上阵武将信息刷新
	 * @param roleId
	 * @return
	 */
	public static Message notifyType_19(int roleId) {
		FightDeployMgtService service = new FightDeployMgtService();
		QueryFightDeployReq req = new QueryFightDeployReq();
		req.setDeployType((byte) 1);
		QueryFightDeployResp resp = service.queryFightDeploy(roleId, req);
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_FIGHT_DEPLOY_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 用户体力刷新
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_20(int roleId, String spStr) {
		String[] tempArr = spStr.split(",");
		
		NotifyRefSpResp resp = new NotifyRefSpResp();
		resp.setSp(Integer.valueOf(tempArr[0]));
		resp.setLastRefSpTime(Long.valueOf(tempArr[1]));
		resp.setRefFlag(Byte.valueOf(tempArr[3]));
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.NOTIFY_REF_SP_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 用户功能开启刷新
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_21(int roleId, String funcOpenStr) {
		NotifyFuncOpenResp resp = new NotifyFuncOpenResp();
		resp.setNewFuncOpenStr(funcOpenStr);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.NOTIFY_FUNC_OPEN_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 获得新神兵刷新
	 * 
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object notifyType_22(int roleId, Object reserve) {
		NotifyNewWeaponResp resp = new NotifyNewWeaponResp();
		List<RoleWeaponInfo> list = (List<RoleWeaponInfo>) reserve;
		List<RoleWeaponInfoRe> reList = new ArrayList<RoleWeaponInfoRe>();
		RoleWeaponInfoRe infoRe;
		
		for(RoleWeaponInfo info : list){
			infoRe = new RoleWeaponInfoRe();
			infoRe.setPosition(info.getPosition());
			infoRe.setExp(info.getExp());
			infoRe.setLevel(info.getLevel());
			infoRe.setWeaponId(info.getId());
			infoRe.setWeaponNo(info.getWeaponNo());
			reList.add(infoRe);
		}
		resp.setCount(reList.size());
		resp.setList(reList);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.NOTIFY_FUNC_WEAPON_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 用户精力刷新
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_23(int roleId, String energyStr) {
		String[] tempArr = energyStr.split(",");
		
		NotifyRefEnergyResp resp = new NotifyRefEnergyResp();
		resp.setEnergy(Integer.valueOf(tempArr[0]));
		resp.setLastRefEnergyTime(Long.valueOf(tempArr[1]));
		resp.setRefFlag(Byte.valueOf(tempArr[3]));

		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.NOTIFY_REF_ENERGY_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 新手引导刷新
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_24(int roleId) {
		
		QueryGuideResp resp = GuideMgtService.queryGuide(roleId);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_ROLE_GUIDE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 金子,银子,勇气令,正义令,刷新
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_25(int roleId) {
		
		NotifyRefSourceResp resp = new NotifyRefSourceResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo != null)
		{
			resp.setGold((int)roleInfo.getCoin());
			resp.setMoney(roleInfo.getMoney());
			resp.setCourage((int)roleInfo.getRoleLoadInfo().getCourage());
			resp.setJustice((int)roleInfo.getRoleLoadInfo().getJustice());
		}
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.NOTIFY_REFRESH_SOURCE);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 推送好友请求
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_26(int roleId, Object reserve) {
		FriendDetailResp resp = new FriendDetailResp();
		FriendDetailRe re = new FriendDetailRe();
		int reqRoleId = 0;
		
		try{
			reqRoleId = Integer.valueOf((String) reserve);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(reqRoleId);
		if(roleInfo != null){
			
			resp.setResult(1);
			resp.setPushType(0);
			
			
			re.setFightValue(roleInfo.getFightValue());
			re.setRoleId(roleInfo.getId());
			re.setRoleName(roleInfo.getRoleName());
			re.setStatus(roleInfo.getLoginStatus());
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if(heroInfo != null){
				re.setLevel(heroInfo.getHeroLevel());
				re.setHeroNo(heroInfo.getHeroNo());
			}
			
			RoleInfo sendRoleInfo = RoleInfoMap.getRoleInfo(roleId);
			if(sendRoleInfo != null && sendRoleInfo.getRoleLoadInfo() != null){
				long time = System.currentTimeMillis();
				for(Map.Entry<Integer, Long> entry : sendRoleInfo.getRoleLoadInfo().getRecordPresentTimeMap().entrySet()){
					//获取今日已经赠送过的玩家Id
					if(re.getRoleId() == entry.getKey() && DateUtil.isSameDay(entry.getValue(), time)){
						re.setCanGive(1);
					}
				}
			}
			
			resp.getList().add(re);
			resp.setCount(resp.getList().size());
			
		}
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.FRIEND_INFO_CHANGE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
		
	}
	
	/**
	 * 更新好友请求列表
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_27(int roleId, Object reserve) {
		FriendDetailResp resp = new FriendDetailResp();
		FriendDetailRe re = new FriendDetailRe();
		
		int relRoleId = 0;
		
		try{
			relRoleId = Integer.valueOf((String) reserve);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(relRoleId);
		if(roleInfo != null){
			
			resp.setResult(1);
			resp.setPushType(1);
			re.setFightValue(roleInfo.getFightValue());
			re.setRoleId(roleInfo.getId());
			re.setRoleName(roleInfo.getRoleName());
			re.setStatus(roleInfo.getLoginStatus());
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if(heroInfo != null){
				re.setLevel(heroInfo.getHeroLevel());
				re.setHeroNo(heroInfo.getHeroNo());
			}
			
			RoleInfo sendRoleInfo = RoleInfoMap.getRoleInfo(roleId);
			if(sendRoleInfo != null && sendRoleInfo.getRoleLoadInfo() != null){
				long time = System.currentTimeMillis();
				for(Map.Entry<Integer, Long> entry : sendRoleInfo.getRoleLoadInfo().getRecordPresentTimeMap().entrySet()){
					//获取今日已经赠送过的玩家Id
					if(re.getRoleId() == entry.getKey() && DateUtil.isSameDay(entry.getValue(), time)){
						re.setCanGive(1);
					}
				}
			}
			
			resp.getList().add(re);
			resp.setCount(resp.getList().size());
		}
		
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.FRIEND_INFO_CHANGE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 删除好友列表
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_28(int roleId, Object reserve) {
		FriendDetailResp resp = new FriendDetailResp();
		FriendDetailRe re = new FriendDetailRe();
		
		int relRoleId = 0;
		
		try{
			String[] str = (String[]) reserve;
			relRoleId = Integer.valueOf(str[0]);
			resp.setPushType(Integer.valueOf(str[1]));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(relRoleId);
		if(roleInfo != null){
			
			resp.setResult(1);
			re.setRoleId(roleInfo.getId());
			
			resp.getList().add(re);
			resp.setCount(resp.getList().size());
			
		}
		
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.FRIEND_INFO_CHANGE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 删除赠送精力请求
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_29(int roleId, Object reserve) {
		FriendDetailResp resp = new FriendDetailResp();
		FriendDetailRe re = new FriendDetailRe();
		
		int relRoleId = 0;
		int id = 0;
		
		try{
			String[] str = (String[]) reserve;
			relRoleId = Integer.valueOf(str[0]);
			id = Integer.valueOf(str[1]);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(relRoleId);
		if(roleInfo != null){
			
			resp.setResult(1);
			resp.setPushType(2);
			re.setFightValue(roleInfo.getFightValue());
			re.setRoleId(roleInfo.getId());
			re.setRoleName(roleInfo.getRoleName());
			re.setStatus(roleInfo.getLoginStatus());
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if(heroInfo != null){
				re.setLevel(heroInfo.getHeroLevel());
				re.setHeroNo(heroInfo.getHeroNo());
			}
			
			re.setId(id);
			
			roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if(roleInfo.getRoleLoadInfo() != null){
				PresentEnergyInfo info = roleInfo.getRoleLoadInfo().getPresentEnergyMap().get(id);
				if(info != null){
					re.setApplyTime(info.getPresentDate().getTime());
				}
				
			}
			
			resp.getList().add(re);
			resp.setCount(resp.getList().size());
			
		}
		
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.FRIEND_INFO_CHANGE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	public static Message notifyType_99(int roleId, int messageType, MessageBody messageBody) {
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(messageType);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(messageBody);
		return message;
	}
	
	/**
	 * 用户体力刷新
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_30(int roleId, String spStr) {
		String[] tempArr = spStr.split(",");
		
		NotifyRefTechResp resp = new NotifyRefTechResp();
		resp.setTech(Integer.valueOf(tempArr[0]));
		resp.setLastRefTechTime(Long.valueOf(tempArr[1]));
		resp.setBuyTimeNum(Short.valueOf(tempArr[2]));
		//resp.setRefFlag(Byte.valueOf(tempArr[3]));
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.NOTIFY_REF_TECH_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 刷新用户充值及vip信息
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_31(int roleId) {
		VipShopMgtService service = new VipShopMgtService();
		QueryVipShopResp resp = service.queryVipShopInfo(roleId);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_VIP_SHOP_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 推送金币礼包变化
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_32(int roleId) {
		VipShopMgtService service = new VipShopMgtService();
		QuerySaleBoxResp resp = service.querySaleBoxInfo(roleId);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_SALE_BOX_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 
	 * @param roleId
	 * @param reserve
	 * @return
	 */
	public static Object notifyType_33(int roleId, String reserve) {
		OpActivityMgrService service = new OpActivityMgrService();
		QueryAllTimeActionTypeResp resp = service.refreshTimeAction(roleId, reserve);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_TIME_ACTION_TYPE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 推送首冲活动信息
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_34(int roleId) {
		OpActivityMgrService service = new OpActivityMgrService();
		QueryFirstChargeResp resp = service.queryFirstCharge(roleId, 1);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_FIRST_CHARGE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 推送7日活动信息
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_35(int roleId) {
		OpActivityMgrService service = new OpActivityMgrService();
		QuerySevenDayResp resp = service.querySevenDay(roleId);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_SEVEN_DAY_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 推送精彩活动信息
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_36(int roleId) {
		OpActivityMgrService service = new OpActivityMgrService();
		QueryWonderResp resp = service.queryWonder(roleId, "");
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_WONDER_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

	/**
	 * 推送vip特权购买礼包
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_37(int roleId) {
		VipShopMgtService service = new VipShopMgtService();
		QueryVipBuyResp resp = service.queryVipBuyInfo(roleId);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.QUERY_VIP_BUY_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 推送开服礼包
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_38(int roleId) {
		CheckInMgtService service = new CheckInMgtService();
		QueryCheckIn7DayListResp resp = service.query7DayList(roleId);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.CHECKIN_7DAY_QUERY_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 推送签到礼包
	 * 
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_39(int roleId) {
		CheckInMgtService service = new CheckInMgtService();
		QueryCheckInListResp resp = service.queryList(roleId);
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.CHECKIN_QUERY_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}
	
	/**
	 * 聊天框置顶公告推送
	 * @param roleId
	 * @return
	 */
	public static Object notifyType_40(int roleId) {
		NotifyUpdateResp resp = new NotifyUpdateResp();
		resp.setResult(1);		
		GameSettingInfo info = GameSettingMap.getValue(GameSettingKey.TOP_SYSTEM_NOTICE);
		if(info != null){
			resp.setContent(info.getValue());
		}
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.NOTIFY_UPDATE_RESP);
		head.setUserID0((int) roleId);
		message.setHeader(head);
		message.setBody(resp);
		return message;
	}

}
