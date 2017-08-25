package com.snail.webgame.game.protocal.fight.fightOut;

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

/**
 * 战斗中途退出
 * @author zhangyq
 */
public class FightOutProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private FightMgtService fightMgtService;

	public void setFightMgtService(FightMgtService fightMgtService) {
		this.fightMgtService = fightMgtService;
	}

	@Override
	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();

		int roleId = header.getUserID0();

		header.setProtocolId(Command.FIGHT_OUT_RESP);
		FightOutResp resp = fightMgtService.battleOut(roleId);

		message.setBody(resp);
		message.setHeader(header);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_FIGHT_INFO_5") + ":roleId=" + roleId);
		}
	}
}
