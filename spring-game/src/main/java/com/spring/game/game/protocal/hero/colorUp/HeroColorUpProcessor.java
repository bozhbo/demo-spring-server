package com.snail.webgame.game.protocal.hero.colorUp;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.hero.service.HeroMgtService;

public class HeroColorUpProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private HeroMgtService heroMgtService;

	public void setHeroMgtService(HeroMgtService heroMgtService) {
		this.heroMgtService = heroMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();

		HeroColorUpReq req = (HeroColorUpReq) message.getBody();
		head.setMsgType(Command.HELO_COLOR_UP_RESP);
		int roleId = head.getUserID0();
		HeroColorUpResp resp = heroMgtService.heroColorUp(roleId, req);

		message.setBody(resp);
		message.setHeader(head);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_HERO_INFO_4") + ":result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
