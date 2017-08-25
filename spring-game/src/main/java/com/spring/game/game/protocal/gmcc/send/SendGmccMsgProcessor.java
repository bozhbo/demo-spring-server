package com.snail.webgame.game.protocal.gmcc.send;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.gmcc.service.GmccMgtService;

public class SendGmccMsgProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private GmccMgtService service;

	public SendGmccMsgProcessor() {
		service = new GmccMgtService();
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		SendGmccMsgReq req = (SendGmccMsgReq) message.getBody();
		service.sendMsgToGMCC(roleId, req);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ITEM_INFO_2") + ": roleId=" + roleId + ",flag="
					+ req.getFlag());
		}
	}
}
