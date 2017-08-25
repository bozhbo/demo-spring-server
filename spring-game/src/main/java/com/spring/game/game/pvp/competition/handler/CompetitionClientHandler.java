package com.snail.webgame.game.pvp.competition.handler;

import java.util.HashMap;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.common.ServiceAccept;
import org.epilot.ccf.config.Config;

import com.snail.webgame.engine.component.room.protocol.handler.SessionStateHandler;
import com.snail.webgame.engine.component.room.protocol.info.IRoomHead;
import com.snail.webgame.game.config.GameConfig;

public class CompetitionClientHandler implements SessionStateHandler {

//	@Override
	public void sessionClose(IoSession session) {
		String serverName = session.getAttribute("serverName") == null ? null : session.getAttribute("serverName").toString();
		
		if (serverName != null) {
			if (serverName.equals(GameConfig.getInstance().getRoomId())) {
				System.out.println(serverName + " is down");
			}
		}
	}

	@Override
	public void sessionOpened(IoSession session) {
		
	}

	@Override
	public boolean checkRoleMessage(IoSession session, IRoomHead roomHead) {
		return true;
	}

	@Override
	public String getRegisterReserve() {
		// 取得当前游戏服务器Ip
		String ip = "";
		
		if (Config.getInstance().getAccptorService() != null) {
			Object obj = Config.getInstance().getAccptorService().get("service");
			
			if (obj != null && obj instanceof HashMap) {
				if (((HashMap<?, ?>)obj).get("GameServer") != null && ((HashMap<?, ?>)obj).get("GameServer") instanceof ServiceAccept) {
					ServiceAccept service = (ServiceAccept)((HashMap<?, ?>)obj).get("GameServer");
					ip = service.getLocalHost();
				}
			}
		}
		
		return ip;
	}

	@Override
	public void registerEnd(IoSession session, String serverReserve) {
	}

	@Override
	public String execCommand(String command) {
		return null;
	}
}
