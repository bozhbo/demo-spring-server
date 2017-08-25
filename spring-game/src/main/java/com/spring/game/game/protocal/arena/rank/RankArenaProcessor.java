package com.snail.webgame.game.protocal.arena.rank;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.arena.service.ArenaMgtService;

public class RankArenaProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private ArenaMgtService arenaMgtService;

	public void setArenaMgtService(ArenaMgtService arenaMgtService) {
		this.arenaMgtService = arenaMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		RankArenaReq req = (RankArenaReq) message.getBody();
		header.setMsgType(Command.RANK_ARENA_RESP);
		int roleId = header.getUserID0();
		RankArenaResp resp = arenaMgtService.rankArena(roleId,req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ARENA_INFO_2") + ": result=" + resp.getResult() + ",roleId="
					+ roleId + ",count=" + resp.getCount());
		}
	}

}
