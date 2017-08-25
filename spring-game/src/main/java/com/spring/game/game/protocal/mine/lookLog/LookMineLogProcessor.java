package com.snail.webgame.game.protocal.mine.lookLog;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.mine.service.MineMgtService;

public class LookMineLogProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private MineMgtService mineMgtService;

	public void setMineMgtService(MineMgtService mineMgtService) {
		this.mineMgtService = mineMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();
		mineMgtService.lookMineLog(roleId);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_MINE_INFO_13") + ": roleId=" + roleId);
		}
	}

}
