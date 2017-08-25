package com.snail.webgame.game.protocal.store.refresh;

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

public class RefreshStoreProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private StoreMgtService storeMgtService;

	public void setStoreMgtService(StoreMgtService storeMgtService) {
		this.storeMgtService = storeMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		head.setMsgType(Command.REFRESH_STORE_ITEM_RESP);
		RefreshStoreReq req = (RefreshStoreReq) message.getBody();
		int roleId = head.getUserID0();
		RefreshStoreResp resp = storeMgtService.refreshStore(roleId, req);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_STORE_INFO_2") + ":result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
