package com.snail.webgame.game.protocal.fight.fightTime;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.fight.service.FightMgtService;

/**
 * 战斗结束
 * 
 * @author tangjq
 * 
 */
public class FightTimeProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	private FightMgtService fightMgtService;

	public void setFightMgtService(FightMgtService fightMgtService) {
		this.fightMgtService = fightMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.FIGHT_TIME_RESP);
		int roleId = header.getUserID0();
		FightTimeResp resp = fightMgtService.battleTime(roleId);

		message.setHeader(header);
		message.setBody(resp);
		response.write(message);
		
		if (logger.isInfoEnabled()) {
			logger.info("get fight time roleId = "+roleId);
		}
	}
}
