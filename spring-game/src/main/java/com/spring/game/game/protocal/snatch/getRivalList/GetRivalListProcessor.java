package com.snail.webgame.game.protocal.snatch.getRivalList;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.snatch.service.SnatchMgtService;

public class GetRivalListProcessor extends ProtocolProcessor {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private SnatchMgtService snatchMgtService;

	public void setSnatchMgtService(SnatchMgtService snatchMgtService) {
		this.snatchMgtService = snatchMgtService;
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		GetRivalListReq req = (GetRivalListReq)message.getBody();
		GetRivalListResp resp = snatchMgtService.getRivalList(roleId, req);
		head.setMsgType(Command.SNATCH_GET_RIVAL_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SNATCH_INFO_2") + ":result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
