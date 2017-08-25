package com.snail.webgame.game.protocal.fight.checkFight;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.fight.service.FightMgtService;

public class CheckFightProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private FightMgtService fightMgtService;

	public void setFightMgtService(FightMgtService fightMgtService) {
		this.fightMgtService = fightMgtService;
	}

	/**
	 * PVE战斗输出验证
	 */
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.CHECK_FIGHT_RESP);
		int roleId = header.getUserID0();
		CheckFightReq req = (CheckFightReq) message.getBody();
		fightMgtService.checkFight(roleId, req);

		if (logger.isInfoEnabled()) {
			//logger.info(Resource.getMessage("game", "GAME_FIGHT_INFO_5") + ",roleId="	+ roleId);
		}
	}

}
