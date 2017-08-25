package com.snail.webgame.game.protocal.fight.startFight;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.fight.service.FightMgtService;

public class StartFightProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private FightMgtService fightMgtService;

	public void setFightMgtService(FightMgtService fightMgtService) {
		this.fightMgtService = fightMgtService;
	}

	@Override
	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();

		StartFightReq req = (StartFightReq) message.getBody();

		header.setMsgType(Command.CHG_FIGHT_DEPLOY_POS_RESP);
		int roleId = header.getUserID0();

		StartFightResp resp = fightMgtService.startFight(roleId, req);
		message.setBody(resp);
		message.setHeader(header);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_FIGHT_INFO_1") + ":result=" + resp.getResult()
					+ ",roleID=" + roleId);
		}
	}

}
