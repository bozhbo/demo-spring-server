package com.snail.webgame.game.protocal.appellation.entity;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.appellation.service.TitleMgtService;

public class ResetTitleInfosProcessor extends ProtocolProcessor{
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private TitleMgtService titleMgtService;

	public void setTitleMgtService(TitleMgtService titleMgtService) {
		this.titleMgtService = titleMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.RESET_TITLE_RESP);

		int roleId = header.getUserID0();

		ResetTitleInfosReq req = (ResetTitleInfosReq) msg.getBody();

		ResetTitleInfosResp resp = titleMgtService.refreshTitle(roleId, req);

		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_TITLE_INFO_3") + ": result=" + resp.getResult() + ",roleId=" + roleId);
		}

	}

}
