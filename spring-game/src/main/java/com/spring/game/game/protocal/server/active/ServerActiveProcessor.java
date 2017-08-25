package com.snail.webgame.game.protocal.server.active;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.protocal.server.service.ServerActiveService;

public class ServerActiveProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ServerActiveService service;

	public ServerActiveProcessor() {
		service = new ServerActiveService();
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		ServerActiveReq req = (ServerActiveReq) message.getBody();

		if (req.getServerName() != null && req.getServerName().startsWith(ServerName.GATE_SERVER_NAME)) {
			service.activeGateConnect(req, request.getSession());
		} else if (req.getServerName() != null && req.getServerName().startsWith(ServerName.GAME_SCENE_SERVER)) {
			service.activeSceneConnect(req, request.getSession());
		}

		if (logger.isInfoEnabled()) {
			if(req.getFlag() == 0)
			{
				logger.info(Resource.getMessage("game", "GAME_SERVER_INFO_1") + ":serverName=" + req.getServerName());
			}
		}
	}

}
