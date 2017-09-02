package com.snail.webgame.engine.gate.send.manage;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.cache.SequenceMap;
import com.snail.webgame.engine.gate.common.ContentValue;
import com.snail.webgame.engine.gate.common.DisconnectPhase;
import com.snail.webgame.engine.gate.config.Command;
import com.snail.webgame.engine.gate.threadpool.GlobalSendMessageManager;
import com.snail.webgame.engine.gate.util.IdentityMap;
import com.snail.webgame.engine.gate.util.MessageServiceManage;
import com.spring.common.ErrorCode;
import com.spring.common.ServerName;

public class RecMsgMgtRunnable  {
	
	private static final Logger log =LoggerFactory.getLogger("logs");
	
	public RecMsgMgtRunnable()
	{
		 
	}
	/**
	 * 转发消息给客户端
	 * @param message
	 * @param msgmgt
	 */
	public static void sendMsg(byte[] message,MessageServiceManage msgmgt)
	{
		int msgType = msgmgt.getMessageType(message);
		
		if(msgType == Command.CHECK_LOGIN_OUT_RESP)//切换账号  被挤下线 GM踢人(错误码119997)
		{
			int roleId = msgmgt.getRoleId(message);
			IoSession clientSession = IdentityMap.getSession(roleId);
			if(clientSession != null && clientSession.isConnected())
			{
				clientSession.write(message);
				// 用户退出，清除用户记录
				clientSession.removeAttribute("identity");//如果该标识清除,则在选角色界面发生断线重连就会认为该角色尚未验证（该问题已解决）
				int result = msgmgt.getResult(message);
				if(result == ErrorCode.USER_LOGIN_ERROR_2)
				{
					clientSession.setAttribute(ContentValue.MSG_SENT_CLOSE);
				}
				
				IdentityMap.removeSession(roleId);
			}
			//给其它（邮件服）服务器广播该用户退出
			List<String> serverList = new ArrayList<String>();
			serverList.add(ServerName.MAIL_SERVER_NAME);
			msgmgt.reportUserDisconnect(serverList, "",roleId,DisconnectPhase.DISCONNECT);
			return ; 
		}
		else if(msgType == Command.USER_DISCONNECT_REQ)//断开
		{
			int roleId = msgmgt.getRoleId(message);
			IoSession clientSession = IdentityMap.getSession(roleId);
			if(clientSession != null && clientSession.isConnected())
			{
				clientSession.write(message);
				clientSession.removeAttribute("identity");
				clientSession.setAttribute(ContentValue.MSG_SENT_CLOSE);
				IdentityMap.removeSession(roleId);
			}
			//给其它（邮件服）服务器广播该用户退出
			List<String> serverList = new ArrayList<String>();
			serverList.add(ServerName.MAIL_SERVER_NAME);
			msgmgt.reportUserDisconnect(serverList, "",roleId,DisconnectPhase.DISCONNECT);
			return ; 
		}
		else if(msgType == Command.USER_VERIFY_ROLE_RESP||
				msgType == Command.USER_LOGIN_RESP||
				msgType == Command.USER_ROLE_RESP||
				msgType == Command.USER_LOGIN_QUEUE_RESP||
				msgType == Command.USER_LOGIN_ACTIVATE_RESP)
		{
			 
			int roleId = 0;
			int result = 0;
			int sequenceId =msgmgt.getMsgSequence(message);
			IoSession sendSess = SequenceMap.getSession(sequenceId);
			if(msgType == Command.USER_LOGIN_RESP)
			{
				int[] ret = msgmgt.checkLoginResult(sendSess,sequenceId, message);
				result = ret[0];
				roleId = ret[1];
			}
			else if(msgType == Command.USER_VERIFY_ROLE_RESP || msgType == Command.USER_LOGIN_QUEUE_RESP)
			{
				String account = msgmgt.checkLoginAccount(sequenceId, message);
				if(account == null)
				{
					if(log.isWarnEnabled())
					{
						log.warn("account is null."+"msgType=0x"+Integer.toHexString(msgType)+",roleId="+roleId);
					}
				}
				else
				{
					if(sendSess != null && sendSess.isConnected())
					{
						sendSess.setAttribute("Account", account);
					}
				}
			}
			
			if(sendSess!=null&&sendSess.isConnected())
			{
				sendSess.write(message);
				//MessageServiceManage.sendClientMessage(sendSess,message);
			}
			else
			{
				if(log.isWarnEnabled())
				{
					log.warn("Message can not be send to game client,client maybe is closed,"+"msgType=0x"+Integer.toHexString(msgType)+
							",result="+result+",roleId="+roleId);
				}
			}
//			SequenceMap.removeSession(sequenceId);
			
			//登录过于频繁，主动断开连接
			if(result==ErrorCode.LOGIN_FREQ_ERROR_1)
			{
				if(sendSess!=null&&sendSess.isConnected())
				{
					log.warn("RecMsgMgtRunnable : close for login frequent");
					sendSess.close();
				}
			}
			
			return ; 
		} else if (msgType == Command.USER_MSG_SEND_RESP) {
			// 批量发送消息
			GlobalSendMessageManager.nextProcessor().addMessage(message);
			return;
		}

		int roleId = msgmgt.getRoleId((byte[]) message);
		 
		IoSession sendSess = IdentityMap.getSession(roleId);
		
		if(sendSess!=null&&sendSess.isConnected())
		{
			sendSess.write(message);
			//MessageServiceManage.sendClientMessage(sendSess,message);
		}
		else
		{
			String ip = "";
			
			if (sendSess != null) {
				InetSocketAddress address1 = (InetSocketAddress) sendSess.getRemoteAddress();
				ip = address1.getAddress().getHostAddress();
			}

			if (log.isWarnEnabled()) {
				log.warn("Message can not be send to game client," + "client maybe is closed,roleId = " + roleId + ",msgType=0x" + Integer.toHexString(msgType) + ",ip=" + ip);
			}
		}
	}
}
