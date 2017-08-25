package com.snail.webgame.game.protocal.goldBuy.query;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.goldBuy.service.GoldBuyMgtService;

public class QueryGoldBuyProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private GoldBuyMgtService goldBuyMgtService;

	public void setGoldBuyMgtService(GoldBuyMgtService goldBuyMgtService) {
		this.goldBuyMgtService = goldBuyMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_ROLE_GOLD_BUY_RESP);
		int roleId = header.getUserID0();
		QueryGoldBuyReq req = (QueryGoldBuyReq) message.getBody();
		QueryGoldBuyResp resp = goldBuyMgtService.queryGoldBuy(roleId,req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_GOLD_BUY_INFO_1") + ": result=" + resp.getResult()
					+ ",roleId=" + roleId);
		}
	}

}
