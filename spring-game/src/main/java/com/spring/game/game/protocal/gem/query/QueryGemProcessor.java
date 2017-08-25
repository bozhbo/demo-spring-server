package com.snail.webgame.game.protocal.gem.query;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.gem.service.GemMgtService;

public class QueryGemProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private GemMgtService gemMgtService;

	public void setGemMgtService(GemMgtService gemMgtService) {
		this.gemMgtService = gemMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_GEM_RESP);
		int roleId = header.getUserID0();
		QueryGemResp resp = gemMgtService.queryGem(roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_GEM_INFO_1") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}

}
