package com.snail.webgame.engine.gate.main;

import java.util.Map;
import java.util.Set;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.cache.SequenceMap;
import com.snail.webgame.engine.gate.cache.ServerMap;
import com.snail.webgame.engine.gate.common.ConnectConfig;
import com.snail.webgame.engine.gate.common.DisconnectPhase;
import com.snail.webgame.engine.gate.config.Command;
import com.snail.webgame.engine.gate.config.WebGameConfig;
import com.snail.webgame.engine.gate.util.IdentityMap;
import com.snail.webgame.engine.gate.util.MessageServiceManage;
import com.spring.common.ServerName;

public class ShutDownThread extends Thread {
	private MessageServiceManage msgmgt = new MessageServiceManage();
	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void run() {
		Set<Integer> sessionKeys = IdentityMap.keySet();
		int i = 0;
		for(Integer sessionKey : sessionKeys){
			IoSession session = IdentityMap.getSession(sessionKey);
			if (session.getAttribute("identity") != null) {
				int roleId = (Integer) session.getAttribute("identity");

				// 通知场景服务器
				Map<String, ConnectConfig> map = WebGameConfig.getInstance().getConnectConfig();

				Set<String> set = map.keySet();

				for (String key : set) {
					if (key.equalsIgnoreCase(ServerName.GAME_SERVER_NAME)) {
						IoSession gameSession = ServerMap.getSession(key);
						if (gameSession != null && gameSession.isConnected()) {
							msgmgt.sendDisconnectMsg(gameSession, "",roleId, Command.USER_DISCONNECT_REQ,1,DisconnectPhase.DISCONNECT,false);
						}
					}
					if(key.equalsIgnoreCase(ServerName.MAIL_SERVER_NAME)){
						//通知聊天服务器
						 IoSession chatSess = ServerMap.getSession(ServerName.MAIL_SERVER_NAME);
				
						 if(chatSess!=null&&chatSess.isConnected())
						 {
							 msgmgt.sendDisconnectMsg(chatSess, "",roleId, Command.USER_DISCONNECT_REQ,1,DisconnectPhase.DISCONNECT,false);
						 }
					 }
					if (key.startsWith(ServerName.GAME_SCENE_SERVER)) {
						IoSession sceneSession = ServerMap.getSession(key);
						if (sceneSession != null && sceneSession.isConnected()) {
							msgmgt.sendDisconnectMsg(sceneSession,"", roleId, Command.USER_DISCONNECT_REQ,1,DisconnectPhase.DISCONNECT,false);
						}
					}
				}

			}
			else {
				if (session.getAttribute("Account") != null) {
					String account = (String) session.getAttribute("Account");
					// 通知游戏服务器
					IoSession gameSess = ServerMap.getSession(ServerName.GAME_SERVER_NAME);
					if (gameSess != null && gameSess.isConnected()) {
						msgmgt.reportUpdateLoginQueue(gameSess, account);
					}
				}

			}
			
			if (session.getAttribute("SequenceId") != null) {
				int sequenceId = (Integer) session.getAttribute("SequenceId");
				SequenceMap.removeSession(sequenceId);
			}
			
			
			i ++;
			if(i % 100 == 0){
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		IdentityMap.clear();
		// 系统保护
		try {
			Thread.sleep(10000);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (logger.isWarnEnabled()) {
			logger.warn("Normal closure of the server!");
		}
	}
}
