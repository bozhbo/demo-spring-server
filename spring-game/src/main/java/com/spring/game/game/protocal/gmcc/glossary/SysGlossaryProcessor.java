package com.snail.webgame.game.protocal.gmcc.glossary;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.protocal.gmcc.service.GmccMgtService;

public class SysGlossaryProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private GmccMgtService gmccMgtService;

	public void setGmccMgtService(GmccMgtService gmccMgtService) {
		this.gmccMgtService = gmccMgtService;
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		SysGlossaryReq req = (SysGlossaryReq) message.getBody();
		gmccMgtService.listenWord(req);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_OTHER_INFO_43") + ",roleId=" + req.getAccountId() + ",word="
					+ req.getWord());
		}

	}

}
