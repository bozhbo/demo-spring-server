package com.snail.webgame.game.protocal.rolemgt.service;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.cache.TempMsgrMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.rolemgt.login.UserLoginResp;
import com.snail.webgame.game.protocal.rolemgt.loginqueue.LoginQueueResp;
import com.snail.webgame.game.protocal.rolemgt.logout.UserLogoutResp;
import com.snail.webgame.game.protocal.rolemgt.verify.UserVerifyResp;

public class RoleCmdSend {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	/**
	 * 发送排队队列
	 * @param queueResp
	 * @param roleId
	 * @param head
	 * @param sequenceId
	 */
	public static void sendLoginQueueResp(LoginQueueResp queueResp,GameMessageHead head){
		Message message = new Message();

		head.setMsgType(Command.USER_LOGIN_QUEUE_RESP);
		head.setProtocolId(Command.USER_LOGIN_QUEUE_RESP);
		message.setHeader(head);
		message.setBody(queueResp);

		IoSession session = ServerMap
				.getServerSession(GameConfig.getInstance().getGateServerName() + "-"
						+ head.getUserID1());

		if (session != null && session.isConnected()) {
			session.write(message);
		}

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_8")
					+ ":result=" + queueResp.getResult() + ",chargeAccout="
					+ queueResp.getAccount() + ",index=" + queueResp.getIndex()
					+ ",num=" + queueResp.getNum());
		}
	}

	/**
	 * 发送用户登出
	 * @param roleInfo
	 * @param resp
	 */
	public static void sendUserLogoutResp(RoleInfo roleInfo,UserLogoutResp resp){
		Message message = new Message();

		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.CHECK_LOGIN_OUT_RESP);
		head.setUserID0((int) roleInfo.getId());
		
		message.setHeader(head);
		message.setBody(resp);

		int gateServerId = roleInfo.getGateServerId();
		
		IoSession session = ServerMap
				.getServerSession(ServerName.GATE_SERVER_NAME + "-" + gateServerId);
		if (session != null && session.isConnected()) {
			session.write(message);
		}
	}

	/**
	 * 发送用户登录
	 * @param resp
	 * @param gateServerId
	 */
	public static void sendUserLoginResp(UserLoginResp resp,int sequenceId){
		Message message = TempMsgrMap.getMessage(sequenceId);
		if(message!=null){
			GameMessageHead head = (GameMessageHead) message.getHeader();
			int gateServerId=head.getUserID1();
			head.setMsgType(Command.USER_LOGIN_RESP);	
			if(resp.getResult()==1 && resp.getRoleId()!=0 && resp.getRoleId()!=-1){
				head.setUserID0((int)resp.getRoleId());
			}
			
			message.setHeader(head);
			message.setBody(resp);
			
			IoSession session = ServerMap
					.getServerSession(ServerName.GATE_SERVER_NAME + "-"
							+ gateServerId);
			if (session != null && session.isConnected()) {
				session.write(message);
				TempMsgrMap.removeMessage(sequenceId);
			}			
		}
	}
	
	/**
	 * 发送验证账号
	 * @param resp
	 * @param gateServerId
	 */
	public static void sendUserVerifyResp(UserVerifyResp resp,int sequenceId){
		Message message = TempMsgrMap.getMessage(sequenceId);
		if(message!=null){
			GameMessageHead head = (GameMessageHead) message.getHeader();
			int gateServerId=head.getUserID1();
			head.setMsgType(Command.USER_VERIFY_ROLE_RESP);	
			message.setHeader(head);
			message.setBody(resp);
	
			IoSession session = ServerMap
					.getServerSession(ServerName.GATE_SERVER_NAME + "-"
							+ gateServerId);
			if (session != null && session.isConnected()) {
				session.write(message);
				TempMsgrMap.removeMessage(sequenceId);
			}
		}
	}

	
}
