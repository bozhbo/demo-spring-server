package com.snail.webgame.game.protocal.gmcc.service;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.client.Client;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.core.GmccGameService;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.gmcc.glossary.SysGlossaryReq;
import com.snail.webgame.game.protocal.gmcc.receive.ChatGmccMsgReq;
import com.snail.webgame.game.protocal.gmcc.rolecount.SysRoleCountListReq;
import com.snail.webgame.game.protocal.gmcc.send.SendGmccMsgReq;
import com.snail.webgame.game.protocal.gmcc.status.ChatStatusReq;
import com.snail.webgame.game.protocal.gmcc.wordlist.SysWordListReq;
import com.snail.webgame.game.protocal.mail.chat.ChatResp;

public class GmccMgtService {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	/**
	 * 解析玩家的消息发送给GMCC
	 * @param roleId
	 * @param req
	 */
	public void sendMsgToGMCC(int roleId, SendGmccMsgReq req) {
		int flag = req.getFlag();
		String content = req.getContent();
		String addContent = req.getAddContent();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo != null) {
			int accountId = (int) roleId;
			String roleName = roleInfo.getRoleName();
			String msgContent = "";

			switch (flag) {
			case 0:
				// 初始化
				msgContent = "#EXTE#regard";
				break;
			case 1:
				// 申请GM
				msgContent = "#EXTE#con#" + content;
				break;
			case 2:
				// 聊天
				if (content.startsWith("#EXTE#")) {
					content = content.substring(6);
				}
				msgContent = content;
				break;
			case 3:
				// 评价
				msgContent = "#EXTE#voteRes#" + content + "#" + addContent;
				break;
			default:
				break;
			}
			if (GameValue.GAME_GMCC_FLAG == 1 && msgContent.length() > 0) {
				GmccGameService.getService().sendMessageToGm(accountId, roleName, msgContent);
			}
		}
	}

	/**
	 * 处理邮件服务器返回的检查到得监控词汇消息
	 * @param req
	 */
	public void listenWord(SysGlossaryReq req) {
		if (req == null) {
			return;
		}
		byte type = req.getType();// 0-聊天 1-邮件 2-角色
		String accountId = req.getAccountId();
		String sendRoleName = req.getSendRoleName();
		String recRoleName = req.getRecRoleName();
		String word = req.getWord();
		String chatRecord = req.getChatRecord();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(Integer.parseInt(accountId));
		if (roleInfo == null) {
			return;
		}

		if (GameValue.GAME_GMCC_FLAG == 1) {
			switch (type) {
			case 0:
				GmccGameService.getService().sendChatWord(roleInfo.getAccount(), sendRoleName, recRoleName, word,
						chatRecord);
				break;
			case 1:
				GmccGameService.getService().sendMailWord(roleInfo.getAccount(), sendRoleName, recRoleName, word,
						chatRecord);
				break;
			case 2:
				GmccGameService.getService().sendChatRole(roleInfo.getAccount(), sendRoleName, recRoleName, chatRecord);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 发送GMCC聊天消息
	 * @param roleInfo
	 * @param req
	 */
	public static void sendGmMessage(RoleInfo roleInfo, ChatGmccMsgReq req) {
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.GMCC_RECEIVE);
		head.setUserID0((int) roleInfo.getId());

		message.setHeader(head);
		message.setBody(req);
		IoSession gateSession = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-"
				+ roleInfo.getGateServerId());
		if (gateSession != null && gateSession.isConnected()) {
			gateSession.write(message);
		}
	}

	/**
	 * GMCC系统广播
	 * @param message
	 */
	public static void sendChatMessage(String content) {
		/*Message message = new Message();

		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.CHAT_REQ);

		ChatReq chatReq = new ChatReq();
		chatReq.setMsgType(Command.MSG_CAHNNEL_REMIND);
		chatReq.setMsgContent(content);

		message.setHeader(head);
		message.setBody(chatReq);

		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			session.write(message);
		}*/
		
		
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setUserID1(Command.CHAT_RESP);
		head.setMsgType(Command.USER_MSG_SEND_RESP);
		message.setHeader(head);
		
		RoleInfo sendRoleInfo = RoleLoginMap.getRoleInfo(0);
		
		ChatResp resp = new ChatResp();
		resp.setResult(1);
		resp.setMsgType(Command.MSG_CAHNNEL_REMIND);
		resp.setSendRoleId(0);
		resp.setSendRace((byte) 0);
		resp.setSendRoleName("");
		resp.setMsgContent(content.trim());
		resp.setSendTime(System.currentTimeMillis());
		if (sendRoleInfo != null) {
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(sendRoleInfo);
			if(heroInfo != null)
			{
				resp.setSendMainHeroNo(heroInfo.getHeroNo());
				resp.setVipLevel((byte) sendRoleInfo.getVipLv());
			}
		}
		
		message.setBody(resp);
		
		Set<Entry<String, IoSession>> set = ServerMap.getMap().entrySet();
		
		for (Entry<String, IoSession> entry : set) {
			if (entry.getKey().startsWith(ServerName.GATE_SERVER_NAME)) {
				if (entry.getValue() != null && entry.getValue().isConnected()) {
					entry.getValue().write(message);
					if(logger.isInfoEnabled()){
						logger.info("Message Sended msgType is " + resp.getMsgType() + " content is " + resp.getMsgContent());
					}
				}
			}
		}
		
	}

	/**
	 * 与邮件服务器同步监控词汇
	 * @param words
	 * @param type 0-聊天 1-邮件
	 */
	public static void sendSysWordList(String words, int type) {
		Message message = new Message();

		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.GMCC_SYS_WORD);

		SysWordListReq req = new SysWordListReq();
		req.setType((byte) type);
		req.setWord(words);

		message.setHeader(head);
		message.setBody(req);

		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			session.write(message);
		}
	}

	/**
	 * 与邮件服务器同步被监控角色信息
	 * @param words
	 * @param type 0-聊天 1-邮件
	 */
	public static void sendRoleCount(String roles, int type) {
		Message message = new Message();

		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.GMCC_SYS_ROLECOUNT);

		SysRoleCountListReq req = new SysRoleCountListReq();
		req.setType((byte) type);
		req.setRoleCounts(roles);

		message.setHeader(head);
		message.setBody(req);

		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			session.write(message);
		}
	}

	/**
	 * 与邮件服务器同步角色禁言信息
	 * @param roleId
	 * @param chatStatus 1-请求禁言 0-请求解禁
	 * @param time
	 */
	public static void sendRolePublish(long roleId, byte chatStatus, long time) {
		Message message = new Message();

		GameMessageHead head = new GameMessageHead();
		head.setMsgType(Command.GMCC_SYS_ROLE_PUBLISH);
		head.setUserID0((int) roleId);

		ChatStatusReq req = new ChatStatusReq();
		req.setRoleId((int) roleId);
		req.setChatStatus(chatStatus);
		req.setTime(time);

		message.setHeader(head);
		message.setBody(req);

		IoSession mailSession = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (mailSession != null && mailSession.isConnected()) {
			mailSession.write(message);
		}
	}
}
