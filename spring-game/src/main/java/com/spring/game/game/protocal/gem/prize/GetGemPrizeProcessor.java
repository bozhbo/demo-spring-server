package com.snail.webgame.game.protocal.gem.prize;

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

public class GetGemPrizeProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private GemMgtService gemMgtService;

	public void setGemMgtService(GemMgtService gemMgtService) {
		this.gemMgtService = gemMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.GET_GEM_PRIZE_RESP);
		int roleId = header.getUserID0();
		GetGemPrizeReq req = (GetGemPrizeReq) message.getBody();
		GetGemPrizeResp resp = gemMgtService.getGemPrize(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_GEM_INFO_5") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}

}
