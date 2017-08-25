package com.snail.webgame.game.protocal.recruit.query;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.recruit.service.ChestMgtService;

public class QueryChestProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private ChestMgtService recruitMgtService;

	public void setRecruitMgtService(ChestMgtService recruitMgtService) {
		this.recruitMgtService = recruitMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_RECRUIT_RESP);
		int roleId = header.getUserID0();
		QueryChestResp resp = recruitMgtService.queryRecruit(roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_RECRUIT_INFO_1") + ": result=" + resp.getResult()
					+ ",roleId=" + roleId);
		}
	}

}