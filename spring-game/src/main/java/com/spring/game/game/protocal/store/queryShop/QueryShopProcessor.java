package com.snail.webgame.game.protocal.store.queryShop;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.store.service.StoreMgtService;

public class QueryShopProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private StoreMgtService storeMgtService;

	public void setStoreMgtService(StoreMgtService storeMgtService) {
		this.storeMgtService = storeMgtService;
	}
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		head.setMsgType(Command.QUERY_SHOP_RESP);
		int roleId = head.getUserID0();
		QueryShopResp resp = storeMgtService.queryShop(roleId);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_STORE_INFO_1") + ":result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
