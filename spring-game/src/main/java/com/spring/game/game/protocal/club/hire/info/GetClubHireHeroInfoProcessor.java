package com.snail.webgame.game.protocal.club.hire.info;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.club.hire.service.HireHeroMgtService;

public class GetClubHireHeroInfoProcessor extends ProtocolProcessor {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private HireHeroMgtService hireHeroMgtService;

	public void setHireHeroMgtService(HireHeroMgtService hireHeroMgtService) {
		this.hireHeroMgtService = hireHeroMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.GET_CLUB_HIRE_HERO_INFO_RESP);
		int roleId = header.getUserID0();
		GetClubHireHeroInfoReq req = (GetClubHireHeroInfoReq) msg.getBody();
		GetClubHireHeroInfoResp resp = hireHeroMgtService.getHireHeroInfo(roleId, req);
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_CLUB_INFO_15") + ": result=" + resp.getResult()
					+ ",roleId=" + roleId);
		}

	}

}
