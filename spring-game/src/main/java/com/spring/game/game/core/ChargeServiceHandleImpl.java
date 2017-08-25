package com.snail.webgame.game.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.Flag;
import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.engine.common.util.Sequence;
import com.snail.webgame.engine.component.charge.common.GameItemPoint;
import com.snail.webgame.engine.component.charge.common.GiftInfo;
import com.snail.webgame.engine.component.charge.facade.ChargeService;
import com.snail.webgame.engine.component.charge.facade.ChargeServiceHandle;
import com.snail.webgame.game.cache.BlackWriteAccountMap;
import com.snail.webgame.game.cache.QueryChargePointList;
import com.snail.webgame.game.cache.RoleChargeMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginQueueInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.cache.TempMsgrMap;
import com.snail.webgame.game.cache.ToolItemMap;
import com.snail.webgame.game.cache.UserAccountMap;
import com.snail.webgame.game.charge.order.SaveChargeOrderReq;
import com.snail.webgame.game.charge.order.SaveChargeOrderResp;
import com.snail.webgame.game.charge.sign.ChargeSignResp;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.dao.RoleChargeDAO;
import com.snail.webgame.game.dao.RoleChargeErrorDao;
import com.snail.webgame.game.info.ChargeAccountInfo;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.RoleChargeInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.app.AppStoreRechargeResp;
import com.snail.webgame.game.protocal.app.common.EChargeState;
import com.snail.webgame.game.protocal.mail.service.MailService;
import com.snail.webgame.game.protocal.rolemgt.activateAccount.ActivateAccountResp;
import com.snail.webgame.game.protocal.rolemgt.activeCode.ActiveCodeResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleCmdSend;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;
import com.snail.webgame.game.protocal.rolemgt.verify.UserVerifyReq;
import com.snail.webgame.game.protocal.rolemgt.verify.UserVerifyResp;
import com.snail.webgame.game.protocal.vipshop.service.VipShopMgtService;

public class ChargeServiceHandleImpl implements ChargeServiceHandle {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	//private RoleMgtService roleMgtService = new RoleMgtService();

	/**
	 * 注册计费结果
	 * 
	 * @param result 计费结果 1-注册成功 otherwise-注册失败
	 */
	public void chargeRegister(int result) {
		if(result==1)
		{
			Flag.flag = 0;//注册成功了
		}
		if (logger.isInfoEnabled()) {
			logger.info("register charge :result=" + result);
		}
	}

	/**
	 * 查询物品价格，只有当全部查询或有变更时才有返回
	 * 
	 * @param list 查询物品集合
	 */
	public void chargeQueryItemPrice(List<GameItemPoint> list) {
		// TODO Auto-generated method stub

	}

	/**
	 * 验证账号
	 * 
	 * @param sequenceId 唯一序列号
	 * @param account 角色账号
	 * @param result 验证结果 1-验证通过 otherwise-验证不通过
	 * @param flag 验证表示 0-一般验证 1-创建角色验证
	 */
	public void chargeVerfiyAccount(int sequenceId, String account, int result, int flag, String hmacStr, int nLogType) {}

	/**
	 * 账号登录
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 验证结果 1-登录成功 otherwise-登录不成功
	 * @param account 角色账号
	 * @param accountId 角色账号Id
	 * @param loginId 角色登陆Id
	 * @param gmLevel 角色权限等级
	 * @param accInfo 角色防沉迷信息 返回格式 SYYYY-MM-DD,-1,255,
	 *        第一个和第二个逗号之间内容为防沉迷信息[-1:不防沉迷 ,其余都防] 玩家属于防沉迷用户， 发送防沉迷请求， 不防沉迷的不要发
	 * @param issuerID 运营商Id
	 * @param HmacStr 一次性串
	 */
	public void chargeLogin(int sequenceId, int result, String account, int accountId, String loginId, int gmLevel,
			String accInfo, int issuerID, String hmacStr) {
		Message message = TempMsgrMap.getMessage(sequenceId);
		logger.info("22------charge login resp:account="+account+",sequenceId=" + sequenceId+",accountId="+accountId);
		if (message != null) {
			GameMessageHead head = (GameMessageHead) message.getHeader();
			UserVerifyReq req = (UserVerifyReq) message.getBody();
			UserVerifyResp resp = new UserVerifyResp();
			resp.setResult(result);
			if (result == 1) {
				
				resp = RoleMgtService.getUserVerifyResp(account.toUpperCase(), head);
				
				// 账号过滤
				if(GameValue.ACCOUNT_FLAG == 1)
				{
					if(BlackWriteAccountMap.getBlackAccountList().contains(req.getAccount().toUpperCase())
							|| !BlackWriteAccountMap.getWriteAccountList().contains(req.getAccount().toUpperCase()))
					{
						resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_12);
						
						RoleCmdSend.sendUserVerifyResp(resp, sequenceId);
						return;
					}
				}
				
				ChargeAccountInfo info = new ChargeAccountInfo();
				info.setChargeAccount(account.toUpperCase());
				info.setAccountId(accountId);
				info.setValidate(req.getValidate());
				info.setGmLevel(gmLevel);
				info.setAccInfo(accInfo);
				info.setIssuerID(issuerID);
				info.setHmacStr(hmacStr);
				info.setAddTime(System.currentTimeMillis());
				
				
				if (accInfo != null  && !"".equals(accInfo)) {
					//360专用
					int first = accInfo.indexOf("$snail360$");
					if (first != -1) {
						String tempStr = accInfo.substring(first + 10);
						first = tempStr.indexOf('$');
						String qihooUserId = tempStr.substring(0, first);
						String qihooToken = tempStr.substring(first + 1);
						
						info.setQihooUserId(qihooUserId);
						info.setQihooToken(qihooToken);
					}
				}
				
				if (hmacStr != null  && !"".equals(hmacStr)) {
					// 酷派 $tjxm$access_token
					int first = hmacStr.indexOf("$tjxm$");
					if (first != -1) {
						String token = hmacStr.substring(first + 6);
						
						info.setQihooUserId(account);
						info.setQihooToken(token);
					}
				}
				
				UserAccountMap.addForChargeAccount(info.getChargeAccount().toUpperCase(), info);
				
				// 检查是否需要排队
				int loginResult = RoleMgtService.isNotNeedQuene(account, head);
				if(loginResult != 1){
					return;
				}
				
				if (GameValue.GAME_LOGIN_QUEUE_FLAG == 1)
				{
					RoleLoginQueueInfoMap.addLoginList(info.getChargeAccount().toUpperCase());
				}
			}
			
			logger.info("charge login:result=" + resp.getResult()+",reqAccount = "+req.getAccount()+",GMLevel="+gmLevel+",ChargeAccount = "+account);
			
			RoleCmdSend.sendUserVerifyResp(resp, sequenceId);
		}
	}

	/**
	 * 账号登出
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 验证结果 1-登出成功 otherwise-登出不成功
	 */
	public void chargeLogout(int sequenceId, int result) {
		if (logger.isInfoEnabled()) {
			logger.info("charge logout:result=" + result);
		}
	}

	/**
	 * 账号防沉迷刷新
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 刷新结果
	 * @param point 玩家的总在线时间
	 */
	public void chargeRefreshAccount(int sequenceId, int result, int point) {
		// TODO Auto-generated method stub

	}

	/**
	 * 返回查询点数
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 查询结果
	 * @param points 点数
	 */
	public void chargeQueryPoints(int sequenceId, int result, int points) {
		// TODO Auto-generated method stub
	}

	/**
	 * 购买物品
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 购买结果
	 * @param accountId 角色账号Id
	 * @param counterItemID 柜面道具综合ID
	 * @param itemNo 物品Id
	 * @param amount 物品数量
	 * @param giveAccount 购买给予账号
	 * @param retPoints 剩余点数
	 * @param orderID 订单编号
	 */
	public void chargeBuyItem(int sequenceId, int result, int accountId, int counterItemID, String itemNo, int amount,
			String giveAccount, int retPoints, int orderID) {
		// TODO Auto-generated method stub
	}

	/**
	 * 取得赠品
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 取得结果
	 * @param accountId 角色账号Id
	 * @param giftId 赠品Id
	 * @param giftName 赠品名称
	 * @param giftNum 赠品数量
	 */
	public void chargeGetGift(int sequenceId, int result, int accountId, int giftId, String giftName, int giftNum) {
		Message message = TempMsgrMap.getMessage(sequenceId);
		if (logger.isInfoEnabled()) {
			logger.info("########chargeGetGift,accountId ="+accountId+"sequenceId="+sequenceId+",result="+result+",giftId="+giftId
					+",giftName="+giftName+",giftNum="+giftNum);
		}
		if(message!=null){
			
			GameMessageHead gameMessageHead = (GameMessageHead)message.getHeader();
			int roleId = gameMessageHead.getUserID0();
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if (roleInfo == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("chargeGetGift, roleId = " + roleId + " not exist");
				}
				return;
			}
			HashMap<Integer, RoleInfo> roleMap = RoleInfoMap.getRoleMapByAccount(roleInfo.getAccount());
			if(roleMap==null||roleMap.size()==0){
				if (logger.isWarnEnabled()) {
					logger.warn("chargeGetGift, roleList = null, account " + roleInfo.getAccount());
				}
				return;
			}
			
			if(result == 1){					
				String itemNo = ToolItemMap.getToolItem(Integer.parseInt(giftName));
				if(itemNo==null){
					if (logger.isWarnEnabled()) {
						logger.warn("chargeGetGift,itemNo==null , account " + roleInfo.getAccount() +" ,giftName = " +giftName);
					}
					return;
				}
				List<DropInfo> addList = new ArrayList<DropInfo>();
				addList.add(new DropInfo(itemNo, giftNum));

				List<MailAttachment> attachments = new ArrayList<MailAttachment>();
				MailAttachment att = null;
				for (DropInfo drop : addList) {
					att = new MailAttachment(drop.getItemNo(), drop.getItemNum(), NumberUtils.toInt(drop.getParam()),0);
					attachments.add(att);
				}

				String attachment = MailDAO.encoderAttachment(attachments);
				String title = Resource.getMessage("game", "ACTIVE_CODE_TITLE");
				String content = Resource.getMessage("game", "ACTIVE_CODE_CONTENT");
				SimpleDateFormat time = new SimpleDateFormat("HH:MM");
				String reserve = time.format(System.currentTimeMillis());
					
				if(gameMessageHead.getUserID2()!=1){//是兑换码兑换的
					// 发邮件时不需要再给客户端响应
//					IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
//					if (session != null && session.isConnected()) {
//						Message message1 = new Message();
//						GameMessageHead gameMessageHead1 = new GameMessageHead();
//						gameMessageHead1.setUserID0(roleId);
//						gameMessageHead1.setMsgType(Command.USER_ACTIVE_CODE_RESP);
//						message1.setHeader(gameMessageHead1);
//						ActiveCodeResp resp = new ActiveCodeResp();
//						resp.setResult(result);
//						message1.setBody(resp);
//						if (session != null && session.isConnected()) {
//							session.write(message1);
//						}
//					}
					QueryChargePointList.removeGiftRole(roleId);
				}else{//系统赠送
					title = Resource.getMessage("game", "SYSTEM_SEND_TITLE");
					content = Resource.getMessage("game", "SYSTEM_SEND_CONTENT");
				}
				//每个角色都发一封邮件，一个角色领了就都删掉
				for(int roleId1 : roleMap.keySet())
				{
					MailService.pushMailPrize(roleId1 + "", attachment, title, content, reserve);
				}
				if (logger.isInfoEnabled()) {
					logger.info("chargeGetGift,  roleId = " + roleId + " giftNo = "+giftName+" giftNum = "+giftNum+" success!");
				}
			}
			
		}else{
			if (logger.isWarnEnabled()) {
				logger.warn("chargeGetGift, message = null");
			}
		}
		TempMsgrMap.removeMessage(sequenceId);
	}

	/**
	 * 查询赠品
	 * 
	 * @param sequenceId 唯一序列号
	 * @param accountId 角色账号Id
	 * @param count 数量
	 * @param list 赠品集合
	 */
	public void chargeQueryGift(int sequenceId, int accountId, int count, List<GiftInfo> list) {
		Message message = TempMsgrMap.getMessage(sequenceId);
		if (logger.isInfoEnabled()) {
			logger.info("#####chargeQueryGift11, accountId = " + accountId + ", sequenceId = " + sequenceId);
		}
		if(message!=null){
			
			GameMessageHead gameMessageHead = (GameMessageHead)message.getHeader();
			int roleId = gameMessageHead.getUserID0();
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if (roleInfo == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("chargeQueryGift, roleId = " + roleId + " not exist");
				}
				return;
			}
			if(list==null){
				if (logger.isWarnEnabled()) {
					logger.warn("chargeQueryGift, list = null");
				}
				return;
			}
			synchronized (roleInfo) {
				try {
					ChargeService chargeService = ChargeGameService.getChargeService();
					for(int i = 0;i<list.size();i++){
						GiftInfo giftInfo = list.get(i);
						int newSequenceId = Sequence.getSequenceId();
						chargeService.sendGetGift(newSequenceId, accountId, giftInfo.getGiftId(), giftInfo.getGiftName(), giftInfo.getGiftNum());
						TempMsgrMap.addMessage(newSequenceId,message);
						if (logger.isInfoEnabled()) {
							logger.info("#####chargeQueryGift22" + ",newSequenceId=" + sequenceId+",accountId="+roleInfo.getAccountId()+",giftId="+giftInfo.getGiftId()
									+",giftName="+giftInfo.getGiftName()+",giftNum="+giftInfo.getGiftNum());
						}
					}
				} catch (Exception e) {
					if(logger.isInfoEnabled()){
						logger.error("",e);
					}
				}
			}
		}
		TempMsgrMap.removeMessage(sequenceId);
	}

	/**
	 * 直接扣费
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 扣除结果
	 * @param accountId 角色账号Id
	 * @param deductPoints 扣除点数
	 * @param retPoints 剩余点数
	 * @param eventType 事件类型
	 * @param reason 扣费原因
	 */
	public void chargeDeductPoint(int sequenceId, int result, int accountId, int deductPoints, int retPoints,
			int eventType, String reason) {
		// TODO Auto-generated method stub
	}

	/**
	 * 第一代购买物品
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 购买结果
	 * @param accountId 角色账号Id
	 * @param itemNo 物品Id
	 * @param amount 物品数量
	 * @param price 物品价格
	 * @param points 剩余点数
	 */
	public void chargeBuyItemGen1(int sequenceId, int result, int accountId, String itemNo, int amount, int price,
			int points) {
		// TODO Auto-generated method stub

	}

	/**
	 * 查询充值金额
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 查询结果
	 * @param accountId 角色账号Id
	 * @param roleName 角色名称
	 * @param startTime 开始时间(yyyy-mm-dd hour:min:second)
	 * @param endTime 结束时间(yyyy-mm-dd hour:min:second)
	 * @param money 充值金额
	 */
	public void chargeQueryRechargeMoney(int sequenceId, int result, int accountId, String roleName, String startTime,
			String endTime, int money) {
		// TODO Auto-generated method stub
	}

	/**
	 * 获取订单信息
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 请求结果
	 * @param type 请求类型【原样返回】
	 * @param accountId 账号id
	 * @param info 信息内容
	 * @param extend 备用参数
	 */
	public void chargeGetOrder(int sequenceId, int result, int type, int accountId, String info, String extend) {
		// TODO Auto-generated method stub
	}

	/**
	 * 直接扣费,计费中的金子转移到游戏中
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 请求结果
	 * @param accountId 账号ID
	 * @param costPoints 将扣费点数
	 * @param costRemain 剩余点数
	 * @param costReason 扣费原因
	 */
	public void chargeConsumePoint(int sequenceId, int result, int accountId, int costPoints, int costRemain,
			String costReason) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 计费消息推送
	 * 
	 * @param accountId 账号ID
	 * @param type 变化类型（1,江湖月卡变化 2 ,点数变化 3, 发送道具 4,踢人）
	 * @param desc 描述信息
	 */
	public void chargeEventPush(int accountId, int type, String desc) {
		if (logger.isInfoEnabled()) {
			logger.info("receive charge push, accountId = " + accountId + ", type = " + type + ", desc = " + desc);
		}
		
		if (type == 4) {
			return;
		}
		
		AppStoreRechargeResp resp = new AppStoreRechargeResp();// 案桌和IOS充值成功通知，都走这个协议
		int roleId = 0;
		
		try {
			int idx = desc.indexOf("$", 1);
			String newDesc = desc.substring(idx + 1);
			JSONObject json = JSONObject.fromObject(newDesc);
			int chargeMoney = Integer.valueOf(json.getString("money"));
			
			// 免商店和appStor等渠道订单号
			String orderIdStr1 = null;
			if (json.containsKey("s_card_no")) {
				orderIdStr1 = json.getString("s_card_no");
			}
			// 部分渠道订单号
			String orderIdStr2 = null;
			if (json.containsKey("wnorder")) {
				orderIdStr2 = json.getString("wnorder");
			}
			
			RoleChargeInfo roleChargeInfo1 = RoleChargeMap.fetchRoleChargeInfo(orderIdStr1);
			RoleChargeInfo roleChargeInfo2 = RoleChargeMap.fetchRoleChargeInfo(orderIdStr2);
			if (roleChargeInfo1 == null && roleChargeInfo2 == null) {
				logger.warn("----- role charge fail, roleChargeInfo is miss , accountId = " + accountId + ", type = " + type 
						+ ", desc = " + desc + ", orderIdStr1 = " + orderIdStr1 + ", orderIdStr2 = " + orderIdStr2);
				return;
			}
			
			String orderIdStr = orderIdStr1;
			RoleChargeInfo roleChargeInfo = roleChargeInfo1;
			if (roleChargeInfo == null) {
				orderIdStr = orderIdStr2;
				roleChargeInfo = roleChargeInfo2;
			} else {
				roleChargeInfo.setState(EChargeState.ORDER_SUCCESSED);
				// 苹果充值，删除极效异常表里的数据
				if(!roleChargeInfo.isNeedDel()){
					// 极效补单过来的，则只需要改状态
					if(!RoleChargeErrorDao.updateRecordStatus(roleChargeInfo.getSeqId(), EChargeState.ORDER_SUCCESSED.getValue(), 1)){
						logger.error("chargeServiceImpl, RoleChargeErrorDao.updateRecordStatus error!");
					}
				} else{
					// 正常充值，删除数据
					if(!RoleChargeErrorDao.deleteRecord(roleChargeInfo.getSeqId())){
						logger.error("chargeServiceImpl, RoleChargeErrorDao.deleteRecord error!");
					}
				}
			}
			
			// 充值游戏内处理
			if (VipShopMgtService.dealRoleCharege(roleChargeInfo.getRoleId(), orderIdStr, roleChargeInfo.getItemId(), chargeMoney, roleChargeInfo.getItemStr())) {
				RoleChargeMap.removeRoleChargeInfo(orderIdStr);
				RoleChargeDAO.getInstance().removeRoleChargeInfo(roleChargeInfo);
			}
			roleId = roleChargeInfo.getRoleId();
			resp.setResult(1);
			resp.setAppOrderId(roleChargeInfo.getOrderStr());
		} catch (Exception e) {
			logger.warn("----- role charge fail, accountId = " + accountId + ", type = " + type + ", desc = " + desc);
			e.printStackTrace();
			resp.setResult(ErrorCode.CHARGE_POINT_NOT_ARRIVAL);
		}
		SceneService.sendRoleRefreshMsg(resp, roleId, Command.APP_CHARGE);
	}
	
	/**
	 * ios充值票据推送
	 * 
	 * @param sequenceId	唯一序列号
	 * @param result		结果（1成功）
	 * @param userName 		账号
	 * @param strExc		订单描述
	 */
	@Override
	public void chargePushBack(int sequenceId, int result, String userName,
			String strExc) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 账号激活
	 * 
	 * @param sequenceId	唯一序列号
	 * @param result		结果（1成功）
	 * @param userName 		账号
	 * @param strExc		订单描述
	 */
	@Override
	public void chargeActivePassport(int sequenceId, int result, String userName,
			String strExc) {
		Message message = TempMsgrMap.getMessage(sequenceId);
		if (logger.isInfoEnabled()) {
			logger.info("########chargeActivePassport,sequenceId="+sequenceId+",result="+result+",userName="+userName+",strExc="+strExc);
		}
		if(message!=null){
			
			GameMessageHead gameMessageHead = (GameMessageHead)message.getHeader();
			gameMessageHead.setMsgType(Command.USER_LOGIN_ACTIVATE_RESP);
			ActivateAccountResp resp = new ActivateAccountResp();
			resp.setResult(result);
			message.setBody(resp);
			IoSession ioSession = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + gameMessageHead.getUserID1());
			if(ioSession!=null&&ioSession.isConnected()){
				ioSession.write(message);
			}
			TempMsgrMap.removeMessage(sequenceId);
		}
		if (logger.isWarnEnabled()) {
			logger.warn("chargeActivePassport, userName = " + userName+", result = "+result+", strExc = "+strExc);
		}
	}

	/**
	 * 使用兑换码
	 * 
	 * @param sequenceId 唯一序列号
	 * @param result 请求结果
	 */
	@Override
	public void chargeUseActivelyCodeError(int sequenceId, int result,
			String pStrResult) {
		if (logger.isInfoEnabled()) {
			logger.info("########chargeUseActivelyCodeError,sequenceId="+sequenceId+",result="+result+",pStrResult="+pStrResult);
		}
		Message message = TempMsgrMap.getMessage(sequenceId);
		if (message != null) {
			GameMessageHead gameMessageHead = (GameMessageHead)message.getHeader();
			int roleId = gameMessageHead.getUserID0();
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if (roleInfo == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("chargeUseActivelyCodeError, roleId = " + roleId + " not exist");
				}
				return;
			}
			
			synchronized (roleInfo) {
				if (result==1) {
					QueryChargePointList.addGiftRole(roleId);
				} else {
					if (result==0) {
						result = ErrorCode.REQUEST_PARAM_ERROR_1;
					}
				}
				
				IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
				if (session != null && session.isConnected()) {
					Message message1 = new Message();
					GameMessageHead gameMessageHead1 = new GameMessageHead();
					gameMessageHead1.setUserID0(roleId);
					gameMessageHead1.setMsgType(Command.USER_ACTIVE_CODE_RESP);
					message1.setHeader(gameMessageHead1);
					ActiveCodeResp resp = new ActiveCodeResp();
					resp.setResult(result);
					message1.setBody(resp);
					if (session != null && session.isConnected()) {
						session.write(message1);
					}
				}	
			}
			
			TempMsgrMap.removeMessage(sequenceId);
		}
	}

	@Override
	public void chargeCommonCode(int sequenceId, int result, int accountId) {
		Message message = TempMsgrMap.getMessage(sequenceId);
		if (logger.isInfoEnabled()) {
			logger.info("########chargeCommonCode,accountId="+accountId+",result="+result+",sequenceId="+sequenceId);
		}
		if (message != null) {
			
			GameMessageHead gameMessageHead = (GameMessageHead) message.getHeader();
			int roleId = gameMessageHead.getUserID0();
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if (roleInfo == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("chargeCommonCode, roleId = " + roleId + " not exist");
				}
				return;
			}
			
			synchronized (roleInfo) {
				if (result==1) {
					QueryChargePointList.addGiftRole(roleId);
				} else {
					if (result==0) {
						result = ErrorCode.REQUEST_PARAM_ERROR_1;
					}
				}
				
				IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
				if (session != null && session.isConnected()) {
					Message message1 = new Message();
					GameMessageHead gameMessageHead1 = new GameMessageHead();
					gameMessageHead1.setUserID0(roleId);
					gameMessageHead1.setMsgType(Command.USER_ACTIVE_CODE_RESP);
					message1.setHeader(gameMessageHead1);
					ActiveCodeResp resp = new ActiveCodeResp();
					resp.setResult(result);
					message1.setBody(resp);
					if (session != null && session.isConnected()) {
						session.write(message1);
					}
				}
			}
			
			TempMsgrMap.removeMessage(sequenceId);
		}
	}

	@Override
	public void chargeVivoOrder(int sequenceId, int result, String roleName, String signature, 
			String vivoSignature, String vivoOrder, String orderAmount, String strExc) {
		Message message = TempMsgrMap.getMessage(sequenceId);
		if (logger.isInfoEnabled()) {
			logger.info("########chargeVivoOrder,sequenceId="+sequenceId+",result="+result+",roleName="+roleName+",signature="+signature
					+",vivoSignature="+vivoSignature+",vivoOrder="+vivoOrder+",orderAmount="+orderAmount+",strExc="+strExc);
		}
		if (message != null) {
			GameMessageHead gameMessageHead = (GameMessageHead) message.getHeader();
			int roleId = gameMessageHead.getUserID0();
			SaveChargeOrderReq req = (SaveChargeOrderReq) message.getBody();
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if (roleInfo == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("chargeVivoOrder, roleId = " + roleId + " not exist");
				}
				return;
			}
			
			IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
			if (session != null && session.isConnected()) {
				
				SaveChargeOrderResp resp = new SaveChargeOrderResp();
				resp.setItemId(req.getItemId());
				resp.setOrderIdStr(req.getOrderStr());
				resp.setVivoOrder(vivoOrder);
				resp.setVivoSignature(vivoSignature);
				
				resp.setResult(result);
				message.setBody(resp);
				
				if (session != null && session.isConnected()) {
					session.write(message);
				}
			}
			
			TempMsgrMap.removeMessage(sequenceId);
		}
	}

	@Override
	public void chargeMeizuSign(int sequenceId, String roleName, String appId, String orderId, String uid, 
			String productId, String productSubject, String productBody, String productUnit, int buyAmount, 
			String productPerPrice, String totalPrice, String createTime, int payType, String userInfo, String sign) {
		Message message = TempMsgrMap.getMessage(sequenceId);
		if (logger.isInfoEnabled()) {
			logger.info("#####chargeMeizuSign" + ",sequenceId=" + sequenceId+",roleName="+roleName+",req.getAppId()="+appId
					+",req.getProductId()="+productId+",totalPrice="+totalPrice);
		}
		if (message != null) {
			GameMessageHead gameMessageHead = (GameMessageHead) message.getHeader();
			int roleId = gameMessageHead.getUserID0();
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if (roleInfo == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("chargeMeizuSign, roleId = " + roleId + " not exist");
				}
				return;
			}
			
			IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
			if (session != null && session.isConnected()) {
				
				ChargeSignResp resp = new ChargeSignResp();
				resp.setResult(1);
				resp.setOrderId(orderId);
				resp.setProductId(productId);
				resp.setSign(sign);
				
				message.setBody(resp);
				
				if (session != null && session.isConnected()) {
					session.write(message);
				}
			}
			
			TempMsgrMap.removeMessage(sequenceId);
		}
	}

}
