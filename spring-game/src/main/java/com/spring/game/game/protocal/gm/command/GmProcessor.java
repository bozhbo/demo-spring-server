package com.snail.webgame.game.protocal.gm.command;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.gm.service.GmMgtService;

public class GmProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private GmMgtService gmMgtService;

	public void setGmMgtService(GmMgtService gmMgtService) {
		this.gmMgtService = gmMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		head.setMsgType(Command.ROLE_GM_RESP);
		GmReq req = (GmReq) message.getBody();
		int roleId = head.getUserID0();
		GmResp resp = gmMgtService.dealGm(roleId, req);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_GM_INFO_1") + ":result=" + resp.getResult() + ",roleId="
					+ roleId + ",command=" + req.getCommand());
		}
		message.setBody(resp);
		response.write(message);
	}
}
