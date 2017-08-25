package com.snail.webgame.game.protocal.rolemgt.loginqueue.checkqueue;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;

public class CheckQueueProcessor extends ProtocolProcessor {
	private RoleMgtService roleMgtService;

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		CheckQueueResp resp = (CheckQueueResp) message.getBody();
		roleMgtService.checkLoginQueue(resp.getAccount().toUpperCase());
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_9")
					+ ":result=" + resp.getResult() + ",account="
					+ resp.getAccount());
		}
	}
}
