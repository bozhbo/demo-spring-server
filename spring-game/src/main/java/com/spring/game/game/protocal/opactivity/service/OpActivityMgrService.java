package com.snail.webgame.game.protocal.opactivity.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.OpActivityProgressMap;
import com.snail.webgame.game.cache.PhoneRecordMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.cache.ToolOpActMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.dao.OpActivityProgressDAO;
import com.snail.webgame.game.dao.PhoneRecordDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.OpActivityProgressInfo;
import com.snail.webgame.game.info.PhoneRecordInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.ToolOpActivityInfo;
import com.snail.webgame.game.info.ToolOpActivityRewardInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.mail.service.MailService;
import com.snail.webgame.game.protocal.opactivity.appComment.AppCommentReq;
import com.snail.webgame.game.protocal.opactivity.appComment.AppCommentResp;
import com.snail.webgame.game.protocal.opactivity.dailyonline.getaward.DailyOnlineGetAwardResp;
import com.snail.webgame.game.protocal.opactivity.dailyonline.query.QueryDailyOnlineListRe;
import com.snail.webgame.game.protocal.opactivity.dailyonline.query.QueryDailyOnlineResp;
import com.snail.webgame.game.protocal.opactivity.firstcharge.getaward.FirstChargeGetAwardResp;
import com.snail.webgame.game.protocal.opactivity.firstcharge.linkphone.PhoneLinkResp;
import com.snail.webgame.game.protocal.opactivity.firstcharge.query.QueryFirstChargeResp;
import com.snail.webgame.game.protocal.opactivity.sevenday.getaward.GetSevenDayAwardResp;
import com.snail.webgame.game.protocal.opactivity.sevenday.query.QuerySevenDayListRe;
import com.snail.webgame.game.protocal.opactivity.sevenday.query.QuerySevenDayResp;
import com.snail.webgame.game.protocal.opactivity.timeaction.getaward.GetTimeActionAwardReq;
import com.snail.webgame.game.protocal.opactivity.timeaction.getaward.GetTimeActionAwardResp;
import com.snail.webgame.game.protocal.opactivity.timeaction.querysingle.QueryTimeActionSingleResp;
import com.snail.webgame.game.protocal.opactivity.timeaction.querysingle.TimeActionRewardRe;
import com.snail.webgame.game.protocal.opactivity.timeaction.querytype.QueryAllTimeActionTypeResp;
import com.snail.webgame.game.protocal.opactivity.timeaction.querytype.TimeActionTypeRe;
import com.snail.webgame.game.protocal.opactivity.weixin.getaward.WeiXinGetAwardResp;
import com.snail.webgame.game.protocal.opactivity.weixin.query.QueryWeiXinResp;
import com.snail.webgame.game.protocal.opactivity.weixin.share.ShareWeiXinResp;
import com.snail.webgame.game.protocal.opactivity.wonder.buyplan.BuyWonderPlanResp;
import com.snail.webgame.game.protocal.opactivity.wonder.getaward.GetWonderAwardResp;
import com.snail.webgame.game.protocal.opactivity.wonder.query.QueryWonderListRe;
import com.snail.webgame.game.protocal.opactivity.wonder.query.QueryWonderResp;
import com.snail.webgame.game.protocal.recruit.recruit.ChestItemRe;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.OnlineGiftXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.SevenDayXMLInfoMap;
import com.snail.webgame.game.xml.cache.WonderXMLInfoMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.OnlineGiftXMLInfo;
import com.snail.webgame.game.xml.info.SevenDayXMLInfo;
import com.snail.webgame.game.xml.info.WonderXMLInfo;

public class OpActivityMgrService {

	/**
	 * 查询首冲和手机绑定界面信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryFirstChargeResp queryFirstCharge(int roleId, int actType) {
		QueryFirstChargeResp resp = new QueryFirstChargeResp();
		if (actType != 1 && actType != 2) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			int actState = 0;
			if (actType == 1) {
				if (roleInfo.getTotalCharge() > 0) {
					actState = 1;
				}
				
				// 已领取过奖励
				if (roleLoadInfo.getIsGetFirstCharge() == 1) {
					actState = 2;
				}
			} else if(actType == 2) {
				
				if (roleLoadInfo.getLinkPhoneState() == 1) {
					actState = 2;
				} else {
					// 手机绑定状态
					if (PhoneRecordMap.isPhoneLink(roleInfo.getAccount())) {
						// 直接发奖励
						sendPhoneMail(roleInfo);
						
						actState = 2;
					}
				}
			}
			
			resp.setActState((byte) actState);
		}
		resp.setResult(1);
		resp.setActType((byte) actType);
		return resp;
	}
	
	public static void sendPhoneMail(RoleInfo roleInfo) {
		List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(GameValue.PHONE_LINK_PRIZE_NO);
		// 奖励
		List<DropInfo> addList = new ArrayList<DropInfo>();

		ItemService.getDropXMLInfo(roleInfo, list, addList);

		List<MailAttachment> attachments = new ArrayList<MailAttachment>();
		MailAttachment att = null;
		for (DropInfo drop : addList) {
			att = new MailAttachment(drop.getItemNo(), drop.getItemNum(), NumberUtils.toInt(drop.getParam()), 0);
			attachments.add(att);
		}

		String attachment = MailDAO.encoderAttachment(attachments);
		if (attachment.length() > 0) {
			String title = Resource.getMessage("game", "GAME_PHONE_TITLE");
			String content = Resource.getMessage("game", "GAME_PHONE_CONTENT");
			String reserve = DateUtil.getDateStrFromDate(new Date());

			MailService.pushMailPrize(roleInfo.getId() + "", attachment, title, content, reserve);
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		roleLoadInfo.setLinkPhoneState(1);
		RoleDAO.getInstance().updateFirstChargeAndPhoneState(roleLoadInfo);
	}
	
	/**
	 * 手机绑定处理(弃用)
	 * 
	 * @param roleId
	 */
	public PhoneLinkResp dealPhoneLink(int roleId) {
		PhoneLinkResp resp = new PhoneLinkResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			// 已绑定过
			if (PhoneRecordMap.isPhoneLink(roleInfo.getAccount())) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			PhoneRecordInfo info = new PhoneRecordInfo();
			info.setAccount(roleInfo.getAccount());
			PhoneRecordMap.addPhoneRecordInfo(info);
			PhoneRecordDAO.getInstance().insertPhoneLink(info);
			
			// 发奖励 走邮件
			sendPhoneMail(roleInfo);
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 领取首冲和手机绑定奖励
	 * 
	 * @param roleId
	 * @return
	 */
	public FirstChargeGetAwardResp getFirstChargeAward(int roleId, int actType) {
		FirstChargeGetAwardResp resp = new FirstChargeGetAwardResp();
		
		if (actType != 1 && actType != 2) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			// 首冲奖励
			if (actType == 1) {
				// 判断是否已领取过
				if (roleLoadInfo.getIsGetFirstCharge() == 1) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE23);
					return resp;
				}
				
				// 是否充值
				if (roleInfo.getTotalCharge() <= 0) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE24);
					return resp;
				}
				
				int result = ItemService.addItemAndEquipCheck(roleInfo);
				//背包格子上限判断
				if (result != 1) {
					resp.setResult(ErrorCode.QUEST_ERROR_23);
					return resp;
				}
								
				roleLoadInfo.setIsGetFirstCharge(1);
				RoleDAO.getInstance().updateFirstChargeAndPhoneState(roleLoadInfo);
				
				// 发奖励
				String prizeNo = GameValue.FIRST_CHARGE_PRIZE_NO;
				int gameAction = ActionType.action400.getType();
				
				List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
				List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
				List<ChestItemRe> list = new ArrayList<ChestItemRe>();// 用于客户端播特效奖励武将是否转化星石
				
				ItemService.addPrizeForPropBag(gameAction, roleInfo, drops, null,
						getPropList, null, null, list, false);
				
				if (list != null) {
					resp.setCount(list.size());
					resp.setList(list);
				}
				
			} else if(actType == 2){
				// 手机绑定奖励
				// 手机绑定奖励改为走邮件
				if (roleLoadInfo.getLinkPhoneState() == 1) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE25);
					return resp;
				}
				
				// 已绑定过
				if (PhoneRecordMap.isPhoneLink(roleInfo.getAccount())) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE26);
					return resp;
				}
				
				PhoneRecordInfo info = new PhoneRecordInfo();
				info.setAccount(roleInfo.getAccount());
				PhoneRecordMap.addPhoneRecordInfo(info);
				PhoneRecordDAO.getInstance().insertPhoneLink(info);
				
				// 发奖励 走邮件
				sendPhoneMail(roleInfo);
			}else{
				//五星评论奖励
			}
		}
		resp.setResult(1);
		resp.setActType((byte) actType);
		return resp;
	}

	/**
	 * 查询在线礼包信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryDailyOnlineResp queryDailyOnline(int roleId) {
		QueryDailyOnlineResp resp = new QueryDailyOnlineResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			Map<Integer, Integer> map = generateRewardRecordMap(roleLoadInfo.getDrawOnlineAwardStr());
			
			List<QueryDailyOnlineListRe> queryList = new ArrayList<QueryDailyOnlineListRe>();
			QueryDailyOnlineListRe re = null;
			Map<Integer, OnlineGiftXMLInfo> onlineGiftMap = OnlineGiftXMLInfoMap.getMap();
			for (OnlineGiftXMLInfo xmlInfo : onlineGiftMap.values()) {
				
				re = new QueryDailyOnlineListRe();
				re.setNo(xmlInfo.getNo());
				
				re.setIsGet((byte) 0);
				if (map.containsKey(xmlInfo.getNo())) {
					re.setIsGet((byte) 1);
				}
				
				queryList.add(re);
			}
			
			if (queryList.size() > 0) {
				resp.setCount(queryList.size());
				resp.setQueryList(queryList);
			}
			
			// 临时结算
			long totalOnline = roleLoadInfo.getTotalTodayOnline() + System.currentTimeMillis() - roleLoadInfo.getLastOnlineTime().getTime();
			resp.setTotalOnline((int) (totalOnline / 1000));
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 下线时结算在线时间
	 * 
	 * @param roleLoadInfo
	 */
	public static void calLogoutOnlineTime(RoleInfo roleInfo) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return;
		}
		// 下线时间
		long now = System.currentTimeMillis();
		
		long curOnlineTime = 0;
		if (roleLoadInfo.getLastOnlineTime() != null) {
			curOnlineTime = now - roleLoadInfo.getLastOnlineTime().getTime() <= 0 ? 0 : now - roleLoadInfo.getLastOnlineTime().getTime();
		}
		
		roleLoadInfo.setTotalTodayOnline(roleLoadInfo.getTotalTodayOnline() + curOnlineTime);
		roleLoadInfo.setLastOnlineTime(new Timestamp(now));
		
		RoleDAO.getInstance().updateRoleOnlineTime(roleLoadInfo);
	}
	
	/**
	 * 领取在线礼包奖励
	 * 
	 * @param roleId
	 * @param dailyOnlineNo
	 * @return
	 */
	public DailyOnlineGetAwardResp getDailyOnlineAward(int roleId, int dailyOnlineNo) {
		DailyOnlineGetAwardResp resp = new DailyOnlineGetAwardResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			OnlineGiftXMLInfo xmlInfo = OnlineGiftXMLInfoMap.fetchOnlineGiftXMLInfo(dailyOnlineNo);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE3);
				return resp;
			}
			
			// 是否已经领取
			Map<Integer, Integer> map = generateRewardRecordMap(roleLoadInfo.getDrawOnlineAwardStr());
			if (map.containsKey(dailyOnlineNo)) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE2);
				return resp;
			}
			
			// 需要临时结算一下在线时间,但不必改变缓存和库
			// 临时结算 检测是否满足在线领取时间
			long totalOnline = roleLoadInfo.getTotalTodayOnline() + System.currentTimeMillis() - roleLoadInfo.getLastOnlineTime().getTime();
			if (totalOnline < xmlInfo.getNeedOnlineTime() * 60 * 1000l) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE1);
				return resp;
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}
			
			//
			String drawOnlineGiftStr = dailyOnlineNo + "";
			if (roleLoadInfo.getDrawOnlineAwardStr() != null) {
				drawOnlineGiftStr = roleLoadInfo.getDrawOnlineAwardStr() + "," + dailyOnlineNo;
			}
			roleLoadInfo.setDrawOnlineAwardStr(drawOnlineGiftStr);
			RoleDAO.getInstance().updateRoleOnlineTime(roleLoadInfo);
			
			// 领取奖励
			String prizeNo = xmlInfo.getPrizeNo();
			List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
			List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
			int result1 = ItemService.addPrizeForPropBag(ActionType.action401.getType(), roleInfo, drops, null,
					getPropList, null, null, null, false);
			
			if (result1 != 1) {
				resp.setResult(result1);
				return resp;
			}
		}
		resp.setResult(1);
		resp.setDailyOnlineNo(dailyOnlineNo);
		return resp;
	}

	/**
	 * 查询微信界面信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryWeiXinResp queryWeiXin(int roleId) {
		QueryWeiXinResp resp = new QueryWeiXinResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			long now = System.currentTimeMillis();
			// 判断是否第二天了，重置每日分享微信标记
			if (roleLoadInfo.getLastWeiXinTime() != null) {
				if (!DateUtil.isSameDay(now, roleLoadInfo.getLastWeiXinTime().getTime())) {
					roleLoadInfo.setLastWeiXinTime(new Timestamp(now));
					roleLoadInfo.setWeiXinGiftflag(0);
					
					RoleDAO.getInstance().updateWeiXinInfo(roleLoadInfo);
				}
			}
			
			resp.setDailyFlag((byte) roleLoadInfo.getWeiXinGiftflag());
			resp.setFirstFlag((byte) roleLoadInfo.getFirstWeiXinGiftflag());
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 微信分享
	 * 
	 * @param roleId
	 * @return
	 */
	public ShareWeiXinResp shareWeiXin(int roleId) {
		ShareWeiXinResp resp = new ShareWeiXinResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			boolean isRefresh = false;
			long now = System.currentTimeMillis();
			// 判断是否第二天了，重置每日分享微信标记
			if (roleLoadInfo.getLastWeiXinTime() != null) {
				if (!DateUtil.isSameDay(now, roleLoadInfo.getLastWeiXinTime().getTime())) {
					roleLoadInfo.setLastWeiXinTime(new Timestamp(now));
					roleLoadInfo.setWeiXinGiftflag(0);
					
					isRefresh = true;
				}
			}
			
			if (roleLoadInfo.getFirstWeiXinGiftflag() == 0) {
				// 首次分享
				roleLoadInfo.setFirstWeiXinGiftflag(1);
				
				isRefresh = true;
			} else {
				if (roleLoadInfo.getWeiXinGiftflag() == 0) {
					roleLoadInfo.setWeiXinGiftflag(1);
					roleLoadInfo.setLastWeiXinTime(new Timestamp(now));
					
					isRefresh = true;
				}
			}
			
			if (isRefresh) {
				RoleDAO.getInstance().updateWeiXinInfo(roleLoadInfo);
			}
			
			resp.setDailyFlag((byte) roleLoadInfo.getWeiXinGiftflag());
			resp.setFirstFlag((byte) roleLoadInfo.getFirstWeiXinGiftflag());
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 领取微信奖励
	 * 
	 * @param roleId
	 * @param awardType
	 * @return
	 */
	public WeiXinGetAwardResp getWeiXinAward(int roleId, int awardType) {
		WeiXinGetAwardResp resp = new WeiXinGetAwardResp();
		if (awardType != 1 && awardType != 2) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			if (awardType == 1) {
				// 领取首次奖励
				if (roleLoadInfo.getFirstWeiXinGiftflag() != 1) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE4);
					return resp;
				}
			} else {
				if (roleLoadInfo.getWeiXinGiftflag() != 1) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE5);
					return resp;
				}
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}
			
			// 发奖励
			String prizeNo = GameValue.FIRST_WEIXIN_PRIZE_NO;
			int gameAction = ActionType.action403.getType();
			if (awardType == 2) {
				prizeNo = GameValue.DAILY_WEIXIN_PRIZE_NO;
				gameAction = ActionType.action404.getType();
			}
						
			if (awardType == 1) {
				roleLoadInfo.setFirstWeiXinGiftflag(2);
			} else {
				roleLoadInfo.setWeiXinGiftflag(2);
				roleLoadInfo.setLastWeiXinTime(new Timestamp(System.currentTimeMillis()));
			}
			
			RoleDAO.getInstance().updateWeiXinInfo(roleLoadInfo);
			
			List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
			List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
			int result1 = ItemService.addPrizeForPropBag(gameAction, roleInfo, drops, null,
					getPropList, null, null, null, false);
			
			if (result1 != 1) {
				resp.setResult(result1);
				return resp;
			}
		}
		
		resp.setResult(1);
		resp.setAwardType((byte) awardType);
		return resp;
	}

	/**
	 * 查询七日活动信息
	 * 
	 * @param roleId
	 * @return
	 */
	public QuerySevenDayResp querySevenDay(int roleId) {
		QuerySevenDayResp resp = new QuerySevenDayResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			Map<Integer, List<Integer>> map = parseSevenDayAwardStr(roleLoadInfo.getSevenDayAwardStr());
			int curLoginDay = roleLoadInfo.getCurSevenLoginDay() >= 7 ? 7 : roleLoadInfo.getCurSevenLoginDay();
			
			List<QuerySevenDayListRe> list = new ArrayList<QuerySevenDayListRe>();
			QuerySevenDayListRe re = null;
			for (SevenDayXMLInfo xmlInfo : SevenDayXMLInfoMap.getAllList()) {
				re = new QuerySevenDayListRe();
				list.add(re);
				
				re.setSubNo(xmlInfo.getSubNo());
				re.setDay((byte) xmlInfo.getDay());
				
				if (curLoginDay < xmlInfo.getDay()) {
					re.setIsGet((byte) 0);
					
					// 登录天数不满足都不能领
					continue;
				}
				
				if (isGetSevenReward(map, xmlInfo.getDay(), xmlInfo.getSubNo())) {
					re.setIsGet((byte) 2);
					
					continue;
				}
				
				if (xmlInfo.getType() == SevenDayXMLInfo.SEVEN_TYPE_1) {
					if (xmlInfo.getDay() <= curLoginDay) {
						re.setIsGet((byte) 1);
					}
				} else if (xmlInfo.getType() == SevenDayXMLInfo.SEVEN_TYPE_2) {
					if (xmlInfo.getNeedGold() <= roleInfo.getTotalCharge()) {
						re.setIsGet((byte) 1);
					}
				} else if (xmlInfo.getType() == SevenDayXMLInfo.SEVEN_TYPE_3) {
					// 可购买
					re.setIsGet((byte) 1);
				}
			}
			
			resp.setCurDay((byte) curLoginDay);
			resp.setRemainTime(fetchSevenDayRemainTime(roleLoadInfo));
			resp.setCount(list.size());
			resp.setQueryList(list);
		}
		resp.setResult(1);
		return resp;
	}
	
	private static Map<Integer, List<Integer>> parseSevenDayAwardStr(String sevenDayAwardStr) {
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		if (sevenDayAwardStr != null) {
			// day-no,day-no
			String[] tempArr = sevenDayAwardStr.split(",");
			for (String subStr : tempArr) {
				String[] subArr = subStr.split("-");
				
				List<Integer> list = map.get(Integer.valueOf(subArr[0]));
				if (list == null) {
					list = new ArrayList<Integer>();
					map.put(Integer.valueOf(subArr[0]), list);
				}
				
				list.add(Integer.valueOf(subArr[1]));
			}
		}
		
		return map;
	}
	
	private static boolean isGetSevenReward(Map<Integer, List<Integer>> map, int day, int subNo) {
		List<Integer> list = map.get(day);
		if (list == null || list.isEmpty()) {
			return false;
		}
		
		if (list.contains(subNo)) {
			return true;
		}
		
		return false;
	}
	
	private static int fetchSevenDayRemainTime(RoleLoadInfo roleLoadInfo) {
		if (roleLoadInfo.getCurSevenLoginDay() >= GameValue.SEVEN_DAY_MAX_DAY 
				&& !DateUtil.isSameDay(System.currentTimeMillis(), roleLoadInfo.getLastSevenDayTime().getTime())) {
			return 0;
		}
		
		return (int) ((DateUtil.getZeroDiff() + (GameValue.SEVEN_DAY_MAX_DAY - roleLoadInfo.getCurSevenLoginDay()) * DateUtil.ONE_DAY_MILLIS) / 1000);
	}
	
	/**
	 * 运营活动检测相关重置处理
	 * 
	 * @param roleInfo
	 */
	public static void dealOpActivityCheck(RoleInfo roleInfo, boolean isRefresh) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return;
		}
		
		long now = System.currentTimeMillis();
		
		// 在线礼包
//		if (roleLoadInfo.getLastOnlineTime() == null || !DateUtil.isSameDay(roleLoadInfo.getLastOnlineTime().getTime(), now)) {
//			roleLoadInfo.setTotalTodayOnline(0);
//			roleLoadInfo.setDrawOnlineAwardStr(null);
//		} 
		
		// 在线礼包改为时间不清零 
		if (!isRefresh) {
			// 登录时记录时间,凌晨不处理,只在下线处才结算在线时间
			roleLoadInfo.setLastOnlineTime(new Timestamp(now));
//			RoleDAO.getInstance().updateRoleOnlineTime(roleLoadInfo);
		}
		
		// 7日活动处理
		boolean sevenDayFlag = false;
		if (roleLoadInfo.getLastSevenDayTime() == null) {
			roleLoadInfo.setLastSevenDayTime(new Timestamp(now));
			roleLoadInfo.setCurSevenLoginDay(1);
			
			sevenDayFlag = true;
		} else {
			if (!DateUtil.isSameDay(now, roleLoadInfo.getLastSevenDayTime().getTime())) {
				if (roleLoadInfo.getCurSevenLoginDay() < GameValue.SEVEN_DAY_MAX_DAY) {
					roleLoadInfo.setCurSevenLoginDay(roleLoadInfo.getCurSevenLoginDay() + 1);
					roleLoadInfo.setLastSevenDayTime(new Timestamp(now));
					
					sevenDayFlag = true;
				}
			}
		}
		
		if (sevenDayFlag) {
			RoleDAO.getInstance().updateSevenDayLogin(roleLoadInfo);
		}
		
		// 时限活动
		OpActivityService.dealOpActProInfoCheck(roleInfo, 0, 0, isRefresh);
				
		if (isRefresh) {
			// 推送相关刷新
			// 7日活动
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_SEVEN, "");
		}
		
	}

	/**
	 * 领取七日奖励
	 * 
	 * @param roleId
	 * @param getDay
	 * @param no
	 * @return
	 */
	public GetSevenDayAwardResp getSevenDayAward(int roleId, int getDay, int no) {
		GetSevenDayAwardResp resp = new GetSevenDayAwardResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			SevenDayXMLInfo xmlInfo = SevenDayXMLInfoMap.fetchSevenDayXMLInfo(getDay, no);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE6);
				return resp;
			}
			
			// 是否已领取
			Map<Integer, List<Integer>> map = parseSevenDayAwardStr(roleLoadInfo.getSevenDayAwardStr());
			if (isGetSevenReward(map, getDay, no)) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE7);
				return resp;
			}
			
			// 验证天数是否达到
			int curLoginDay = roleLoadInfo.getCurSevenLoginDay() >= 7 ? 7 : roleLoadInfo.getCurSevenLoginDay();
			if (curLoginDay < xmlInfo.getDay()) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE8);
				return resp;
			}
			
			// 验证类型
			if (xmlInfo.getType() == SevenDayXMLInfo.SEVEN_TYPE_1) {
				if (curLoginDay < xmlInfo.getDay()) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE9);
					return resp;
				}
			} else if (xmlInfo.getType() == SevenDayXMLInfo.SEVEN_TYPE_2) {
				if (roleInfo.getTotalCharge() < xmlInfo.getNeedGold()) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE10);
					return resp;
				}
			} else if (xmlInfo.getType() == SevenDayXMLInfo.SEVEN_TYPE_3) {
				// 验证金币是否足够
				if (roleInfo.getCoin() < xmlInfo.getNeedGold()) {
					resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
					return resp;
				}
			} 
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}
			
			int gameAction = ActionType.action405.getType();
			if (xmlInfo.getType() == SevenDayXMLInfo.SEVEN_TYPE_3) {
				// 扣除金币
				if (!RoleService.subRoleRoleResource(ActionType.action406.getType(), roleInfo, ConditionType.TYPE_COIN, xmlInfo.getNeedGold(),null)) {
					resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
					return resp;
				}
				
				gameAction = ActionType.action406.getType();
				SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
			}
			
			// 修改缓存数据库
			String sevenDayAwardStr = getDay + "-" + no;
			if (roleLoadInfo.getSevenDayAwardStr() != null) {
				sevenDayAwardStr = roleLoadInfo.getSevenDayAwardStr() + "," + getDay + "-" + no;
			}
			
			if (RoleDAO.getInstance().updateSevenRewardStr(roleId, sevenDayAwardStr)) {
				roleLoadInfo.setSevenDayAwardStr(sevenDayAwardStr);
			}
			
			// 发奖励
			String prizeNo = xmlInfo.getPrizeNo();
			List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
			List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
			List<ChestItemRe> list = new ArrayList<ChestItemRe>();// 用于客户端播特效奖励武将是否转化星石
			
			ItemService.addPrizeForPropBag(gameAction, roleInfo, drops, null,
					getPropList, null, null, list, false);
			
			if (list.size() > 0) {
				resp.setCount(list.size());
				resp.setList(list);
			}
			
			if(gameAction == ActionType.action405.getType() && getDay == 7){
				OpActivityMgrService.appCommentNotify(roleInfo);
			}
			
		}
		
		resp.setResult(1);
		resp.setGetDay((byte) getDay);
		resp.setSubNo(no);
		return resp;
	}
	
	/**
	 * 查询精彩活动界面信息
	 * 
	 * @param roleId
	 * @param noStr 更新的编号
	 * @return
	 */
	public QueryWonderResp queryWonder(int roleId, String noStr) {
		QueryWonderResp resp = new QueryWonderResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			Map<Integer, Integer> map = generateRewardRecordMap(roleLoadInfo.getWonderAwardStr());
			
			List<QueryWonderListRe> queryList = new ArrayList<QueryWonderListRe>();
			QueryWonderListRe re = null;
			for (WonderXMLInfo xmlInfo : WonderXMLInfoMap.getMap().values()) {
				re = new QueryWonderListRe();
				queryList.add(re);
				
				re.setNo(xmlInfo.getNo());
				
				// 投资计划必须先购买才能领取
				if (xmlInfo.getWonderType() == WonderXMLInfo.WONDER_TYPE_PLAN && roleLoadInfo.getIsBuyPlan() != 1) {
					continue;
				}

				// 检测是否已领取
				if (map.containsKey(xmlInfo.getNo())) {
					re.setIsGet((byte) 2);
					
					continue;
				}
				
				// 未领取的是否满足领取条件
				int rt = AbstractConditionCheck.check(xmlInfo.getConds(), roleInfo, null, 0, null);
				if (rt == 1) {
					re.setIsGet((byte) 1);
				}
			}
			
			if (queryList.size() > 0) {
				resp.setCount(queryList.size());
				resp.setQueryList(queryList);
			}
			
			if (roleLoadInfo.getIsBuyPlan() == 1) {
				resp.setIsBuyPlan((byte) 2);
			} else {
				if (roleInfo.getVipLv() >= GameValue.BUY_WONDER_PLAN_VIP_LV) {
					resp.setIsBuyPlan((byte) 1);
				}
			}
			
			int totalCost = (int) (roleInfo.getTotalCoin() - roleInfo.getCoin());
			resp.setTotalCost(totalCost);
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 购买投资计划
	 * 
	 * @param roleId
	 * @return
	 */
	public BuyWonderPlanResp buyWonderPlan(int roleId) {
		BuyWonderPlanResp resp = new BuyWonderPlanResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			// 是否已购买
			if (roleLoadInfo.getIsBuyPlan() == 1) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE11);
				return resp;
			}
			
			// 是否满足vip等级要求
			if (roleInfo.getVipLv() < GameValue.BUY_WONDER_PLAN_VIP_LV) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE12);
				return resp;
			}
			
			// 金币是否足够
			if (roleInfo.getCoin() < GameValue.BUY_WONDER_PLAN_COST_COIN) {
				resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
				return resp;
			}
			
			// 扣除金币
			if (!RoleService.subRoleRoleResource(ActionType.action408.getType(), roleInfo, ConditionType.TYPE_COIN, GameValue.BUY_WONDER_PLAN_COST_COIN , null)) {
				resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
				return resp;
			}
			
			SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
			
			if (RoleDAO.getInstance().updateWonderPlanFlag(roleId)) {
				roleLoadInfo.setIsBuyPlan(1);
			}
			
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 领取精彩活动奖励
	 * 
	 * @param roleId
	 * @param no
	 * @return
	 */
	public GetWonderAwardResp getWonderAward(int roleId, int no) {
		GetWonderAwardResp resp = new GetWonderAwardResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			WonderXMLInfo xmlInfo = WonderXMLInfoMap.fetchWonderXMLInfo(no);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			// 若是领取投资计划，判断是否购买
			if (xmlInfo.getWonderType() == WonderXMLInfo.WONDER_TYPE_PLAN && roleLoadInfo.getIsBuyPlan() != 1) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE14);
				return resp;
			}
			
			// 是否已领取
			Map<Integer, Integer> map = generateRewardRecordMap(roleLoadInfo.getWonderAwardStr());
			if (map.containsKey(xmlInfo.getNo())) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE15);
				return resp;
			}
			
			// 检测是否满足领取条件
			int rt = AbstractConditionCheck.check(xmlInfo.getConds(), roleInfo, null, 0, null);
			if (rt != 1) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE16);
				return resp;
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}
			
			// 修改缓存数据库
			String wonderAwardStr = "" + no;
			if (roleLoadInfo.getWonderAwardStr() != null) {
				wonderAwardStr = roleLoadInfo.getWonderAwardStr() + "," + no;
			}
			
			if (RoleDAO.getInstance().updateWonderRewardStr(roleId, wonderAwardStr)) {
				roleLoadInfo.setWonderAwardStr(wonderAwardStr);
			}
			
			// 发奖励
			String prizeNo = xmlInfo.getPrizeNo();
			List<DropXMLInfo> drops = PropBagXMLMap.getPropBagXMLListbyStr(prizeNo);
			List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
			
			int result1 = ItemService.addPrizeForPropBag(ActionType.action407.getType(), roleInfo, drops, null,
					getPropList, null, null, null, false);
			
			if (result1 != 1) {
				resp.setResult(result1);
				return resp;
			}
			
		}
		resp.setResult(1);
		resp.setNo(no);
		return resp;
	}
	
	/**
	 * 推送有变化的活动
	 * 
	 * @param roleId
	 * @param idStr
	 * @return
	 */
	public QueryAllTimeActionTypeResp refreshTimeAction(int roleId, String idStr) {
		QueryAllTimeActionTypeResp resp = new QueryAllTimeActionTypeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			List<TimeActionTypeRe> queryTypes = new ArrayList<TimeActionTypeRe>();
			TimeActionTypeRe re = null;
			
			String[] tempArr = idStr.split(",");
			ToolOpActivityInfo toolOpActInfo = null;
			for (String actIdStr : tempArr) {
				toolOpActInfo = ToolOpActMap.fetchToolOpActivityInfo(Integer.valueOf(actIdStr));
				if (toolOpActInfo == null) {
					// 活动过期
					toolOpActInfo = ToolOpActMap.fetchOutTimeToolOpAct(Integer.valueOf(actIdStr));
				}
				
				if (toolOpActInfo == null) {
					continue;
				}
				
				re = new TimeActionTypeRe();
				re.setActId(toolOpActInfo.getId());
				re.setActName(toolOpActInfo.getActName());
				re.setActState((byte) toolOpActInfo.getActState());
				
				queryTypes.add(re);
			}
			
			if (queryTypes.size() > 0) {
				resp.setCount(queryTypes.size());
				resp.setQueryTypes(queryTypes);
			}
			
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 查询已开启的时限活动
	 * 
	 * @param roleId
	 * @return
	 */
	public QueryAllTimeActionTypeResp queryAllTimeActionType(int roleId) {
		QueryAllTimeActionTypeResp resp = new QueryAllTimeActionTypeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			long now = System.currentTimeMillis();
			
			List<TimeActionTypeRe> queryTypes = new ArrayList<TimeActionTypeRe>();
			TimeActionTypeRe re = null;
			Map<Integer, ToolOpActivityInfo> map = ToolOpActMap.getOpActMap();
			if (!map.isEmpty()) {
				for (ToolOpActivityInfo opActInfo : map.values()) {
					if (opActInfo.getActState() == 0) {
						// 活动未开启
						continue;
					}
					
					if (now < opActInfo.getStartTime().getTime() || now > opActInfo.getEndTime().getTime()) {
						// 时间未到或已过期
						continue;
					}
					
					re = new TimeActionTypeRe();
					re.setActId(opActInfo.getId());
					re.setActName(opActInfo.getActName());
					re.setActState((byte) 1);
					
					queryTypes.add(re);
				}
			}
			
			if (queryTypes.size() > 0) {
				resp.setCount(queryTypes.size());
				resp.setQueryTypes(queryTypes);
			}
			
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 查询单个时限活动界面信息
	 * 
	 * @param roleId
	 * @param actId
	 * @return
	 */
	public QueryTimeActionSingleResp querySingleTimeActionInfo(int roleId, int actId) {
		QueryTimeActionSingleResp resp = new QueryTimeActionSingleResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			ToolOpActivityInfo toolOpActivityInfo = ToolOpActMap.fetchToolOpActivityInfo(actId);
			if (toolOpActivityInfo == null) {
				resp.setResult(ErrorCode.OP_ACT_IS_OLD_ERROR);
				return resp;
			}
			
			OpActivityProgressMap opActProMap = roleInfo.getOpActProMap();
			if (opActProMap == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE17);
				return resp;
			}
			
			resp.setActId(toolOpActivityInfo.getId());
			resp.setActIntroduce(toolOpActivityInfo.getActIntroduce());
			resp.setActName(toolOpActivityInfo.getActName());
			resp.setActType((byte) toolOpActivityInfo.getActType());
			
			if (toolOpActivityInfo.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_3) {
				// 限时武将
				resp.setLotCost(toolOpActivityInfo.getLotPrice());
				
				StringBuilder sb = new StringBuilder();
				if (toolOpActivityInfo.getLotHeroStr() != null) {
					sb.append(toolOpActivityInfo.getLotHeroStr().split(":")[0]).append(",");
				}
				
				if (toolOpActivityInfo.getLotRewardStr() != null) {
					String[] tempArr = toolOpActivityInfo.getLotRewardStr().split(";");
					for (String subStr : tempArr) {
						String[] subArr = subStr.split(":");
						sb.append(subArr[1]).append(",");
					}
				}
				
				if (sb.length() > 1) {
					resp.setLotRewardStr(sb.deleteCharAt(sb.length() - 1).toString());
				}
				
				int specVal = 0;
				OpActivityProgressInfo opActProInfo = opActProMap.fetchOpActProInfo(actId, 1);
				if (opActProInfo != null) {
					specVal = (int) opActProInfo.getValue1();
				}
				
				int remainNum = toolOpActivityInfo.getLotHitNum() - specVal % toolOpActivityInfo.getLotHitNum();
				remainNum = remainNum <= 0 ? 0 : remainNum;
				resp.setRemainNum(remainNum);
			} else if (toolOpActivityInfo.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_0) {
				Map<Integer, ToolOpActivityRewardInfo> rewardMap = ToolOpActMap.fetchRewardsByActId(actId);
				if (rewardMap != null && !rewardMap.isEmpty()) {
					
					List<TimeActionRewardRe> rewards = new ArrayList<TimeActionRewardRe>();
					TimeActionRewardRe re = null;
					for (ToolOpActivityRewardInfo rewardInfo : rewardMap.values()) {
						re = new TimeActionRewardRe();
						re.setRewardNo((byte) rewardInfo.getRewardNo());
						re.setRewardName(rewardInfo.getRewardName());
						re.setRewardItems(rewardInfo.getRewardItems());
						
						OpActivityProgressInfo opActProInfo = opActProMap.fetchOpActProInfo(actId, rewardInfo.getRewardNo());
						if (opActProInfo != null) {
							re.setIsGet((byte) opActProInfo.getRewardState());
							re.setCurVal((int) opActProInfo.getValue1());
						}
						re.setActTargetVal(rewardInfo.getGoalVal());
						
						if (rewardInfo.isShowCond()) {
							re.setIsShowPro((byte) 1);
						}
						re.setMultiple(rewardInfo.getMultiple());
						
						rewards.add(re);
					}
					
					if (rewards.size() > 0) {
						resp.setCount(rewards.size());
						resp.setRewards(rewards);
					}
				}
			}
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 领取时限活动奖励
	 * 
	 * @param roleId
	 * @param actId
	 * @param rewardNo
	 * @return
	 */
	public GetTimeActionAwardResp getTimeActionReward(int roleId, GetTimeActionAwardReq req) {
		GetTimeActionAwardResp resp = new GetTimeActionAwardResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_15);
			return resp;
		}
		int actId = req.getActId();
		int rewardNo = req.getRewardNo();
		
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_9);
				return resp;
			}
			
			OpActivityProgressMap opActProMap = roleInfo.getOpActProMap();
			if (opActProMap == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE18);
				return resp;
			}

			// 验证活动是否过期
			ToolOpActivityInfo toolOpActivityInfo = ToolOpActMap.fetchToolOpActivityInfo(actId);
			if (toolOpActivityInfo == null) {
				resp.setResult(ErrorCode.OP_ACT_IS_OLD_ERROR);
				return resp;
			}
			
			long now = System.currentTimeMillis();
			if (now < toolOpActivityInfo.getStartTime().getTime() || now > toolOpActivityInfo.getEndTime().getTime()) {
				// 活动已过期
				resp.setResult(ErrorCode.OP_ACT_IS_OLD_ERROR);
				return resp;
			}
			
			OpActivityProgressInfo opActProInfo = null;
			if (toolOpActivityInfo.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_3) {
				// 限时武将
				opActProInfo = opActProMap.fetchOpActProInfo(actId, 1);
			} else {
				opActProInfo = opActProMap.fetchOpActProInfo(actId, rewardNo);
			}
			
			if (opActProInfo == null) {
				resp.setResult(ErrorCode.SYSTEM_ERROR_CODE19);
				return resp;
			}
			
			int result = ItemService.addItemAndEquipCheck(roleInfo);
			//背包格子上限判断
			if (result != 1) {
				resp.setResult(ErrorCode.QUEST_ERROR_23);
				return resp;
			}
			
			if (toolOpActivityInfo.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_0) {
				ToolOpActivityRewardInfo toolOpActRewardInfo = ToolOpActMap.fetchToolOpActRewardInfo(actId, rewardNo);
				if (toolOpActRewardInfo == null) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE20);
					return resp;
				}
				
				List<DropInfo> itemList = null;
				if(toolOpActRewardInfo.getMultiple() == 1){
					itemList = toolOpActRewardInfo.getDropInfoList(req.getIndex());
				}else{
					itemList = toolOpActRewardInfo.getDropInfoList();
				}
				if(itemList == null){
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE21);
					return resp;
				}
				
				// 是否可领奖
				if (opActProInfo.getRewardState() != 1) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE23);
					return resp;
				}
				
				
				// 更新数据库缓存
				if (OpActivityProgressDAO.getInstance().updateOpActProRewardState(opActProInfo.getId(), 2)) {
					opActProInfo.setRewardState(2);
				}
				
				// 发奖励
				if (itemList != null && itemList.size()> 0) {					
					int result1 = ItemService.addPrize(ActionType.action409.getType(), roleInfo, itemList, null,
							null, null, null, null, null, null, null, null, true);
					if (result1 != 1) {
						resp.setResult(result1);
						return resp;
					}
				}
			}
			
			// 限时武神抽奖
			if (toolOpActivityInfo.getActType() == ToolOpActivityInfo.OP_ACT_TYPE_3) {
				// 金币是否足够
				if (roleInfo.getCoin() < toolOpActivityInfo.getLotPrice()) {
					resp.setResult(ErrorCode.ROLE_COIN_ERROR_1);
					return resp;
				}
				
				// 掉落库添加奖励，检测是否必得武将
				int specVal = (int) opActProInfo.getValue1();
				specVal++;
				
				
				boolean isMust = false;
				if (specVal % toolOpActivityInfo.getLotHitNum() == 0) {
					isMust = true;
				}
				
				List<ChestItemRe> chestItemList = new ArrayList<ChestItemRe>();
				int rt = OpActivityService.lottOpActHero(roleInfo, toolOpActivityInfo, chestItemList, isMust);
				if (rt != 1) {
					resp.setResult(rt);
					return resp;
				}
				
				// 扣除金币
				if (!RoleService.subRoleRoleResource(ActionType.action410.getType(), roleInfo, ConditionType.TYPE_COIN, toolOpActivityInfo.getLotPrice() , null)) {
					resp.setResult(ErrorCode.SYSTEM_ERROR_CODE22);
					return resp;
				}
				
				// 推送金币变化
				SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, "");
				
				// 更新数据库缓存
				if (OpActivityProgressDAO.getInstance().updateOpActProVal1(opActProInfo.getId(), specVal)) {
					opActProInfo.setValue1(specVal);
				}
				
				int remainNum = toolOpActivityInfo.getLotHitNum() - specVal % toolOpActivityInfo.getLotHitNum();
				remainNum = remainNum <= 0 ? 0 : remainNum;
				resp.setRemainNum(remainNum);
				
				if (chestItemList.size() > 0) {
					resp.setCount(chestItemList.size());
					resp.setItemList(chestItemList);
				}
			}
			
		}
		resp.setResult(1);
		resp.setActId(actId);
		resp.setRewardNo((byte) rewardNo);
		resp.setIndex(req.getIndex());
		return resp;
	}

	public static Map<Integer,Integer> generateRewardRecordMap(String drawAwardStr) {
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		if (drawAwardStr != null) {
			String[] tempArr = drawAwardStr.split(",");
			for (String str : tempArr) {
				map.put(Integer.valueOf(str), 1);
			}		
		}
			
		return map;
	}
	
	/**
	 * 领取苹果五星评论奖励
	 * 
	 * @param roleId
	 * @return
	 */
	public AppCommentResp appCommentAward(int roleId,AppCommentReq req) {
		AppCommentResp resp = new AppCommentResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_1);
				return resp;
			}
			
			if(req.getFlag() == 1)
			{
				// 判断是否已领取过
				if (roleLoadInfo.getCommentGameState() == 1) {
					resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_2);
					return resp;
				}
				
				if(!RoleDAO.getInstance().updateAppCommentState(roleInfo.getId()))
				{
					resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_3);
					return resp;
				}
				roleLoadInfo.setCommentGameState(1);
				
				//TitleService.achieveTitleCheck(null, GameValue.APP_COMMENT_AWARD, roleInfo);
				
			}
			else if(req.getFlag() == 3)
			{
				if(!RoleDAO.getInstance().updateAppCommentState(roleInfo.getId()))
				{
					resp.setResult(ErrorCode.APP_COMMENT_ERROR_ERROR_4);
					return resp;
				}
				roleLoadInfo.setCommentGameState(1);
			}
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 提示五星评论
	 * @param roleInfo
	 */
	public static void appCommentNotify(RoleInfo roleInfo)
	{
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) 
		{
			return;
		}
		
		if(roleLoadInfo.getCommentGameState() == 1)
		{
			return;
		}
		
		if(GameConfig.getInstance().getAndroid_Ios_Flag() != 2)
		{
			return;
		}
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.APP_COMMENT_NOTIFY_RESP);
		head.setUserID0(roleInfo.getId());
		message.setHeader(head);
		
		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		if(session != null && session.isConnected())
		{
			session.write(message);
		}
	}

}
