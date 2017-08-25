package com.snail.webgame.game.protocal.hero.skillUp;

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

public class HeroSkillUpProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private HeroMgtService heroMgtService;

	public void setHeroMgtService(HeroMgtService heroMgtService) {
		this.heroMgtService = heroMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.HELO_SKILL_UP_RESP);
		int roleId = header.getUserID0();
		HeroSkillUpReq req = (HeroSkillUpReq) message.getBody();
		HeroSkillUpResp resp = heroMgtService.heroSkillUp(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_HERO_INFO_6") + ": result=" + resp.getResult()
					+ ",roleId=" + roleId );
		}
	}
}
