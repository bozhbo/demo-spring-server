package com.snail.webgame.game.protocal.server.service;

import org.apache.mina.common.IoSession;

import com.snail.webgame.game.cache.RoleLoginQueueInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;
import com.snail.webgame.game.protocal.server.active.ServerActiveReq;
import com.snail.webgame.game.protocal.server.time.SysTimeResp;

public class ServerActiveService {

	public void activeGateConnect(ServerActiveReq req, IoSession session) {
		String serverName = req.getServerName();
		int flag = req.getFlag();
		if (serverName == null || serverName.trim().length() == 0) {
			return;
		}
		if (flag == 0) {
			// 通讯服务器重启，对应用户下线
			String str[] = serverName.split("-");

			if (str != null && str.length > 0) {

				// 所有人下线
				RoleMgtService.allUserLoginOut();

				RoleLoginQueueInfoMap.removeAll();
			} else {
				return;
			}
		}
		ServerMap.addServer(serverName, session);
		session.setAttribute("serverName", serverName);
	}

	public void activeSceneConnect(ServerActiveReq req, IoSession session) {
		int fightNum = 0;
		if (req.getReserve() != null && req.getReserve().length() > 0) {
			fightNum = Integer.valueOf(req.getReserve());
		}
		String serverName = req.getServerName();
		session.setAttribute("num", fightNum);
		session.setAttribute("serverName", serverName);
		ServerMap.addServer(serverName, session);

	}

	/**
	 * 获得系统当前时间
	 * 
	 * @return
	 */
	public SysTimeResp getSysTime() {
		SysTimeResp resp = new SysTimeResp();
		resp.setResult(1);
		resp.setCurrTime(System.currentTimeMillis());
		return resp;
	}

}
