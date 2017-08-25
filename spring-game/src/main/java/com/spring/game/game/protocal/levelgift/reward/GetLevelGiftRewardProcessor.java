package com.snail.webgame.game.protocal.levelgift.reward;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.checkIn.service.CheckInMgtService;

public class GetLevelGiftRewardProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private CheckInMgtService checkInMgtService;

	public void setCheckInMgtService(CheckInMgtService checkInMgtService) {
		this.checkInMgtService = checkInMgtService;
	}

	@Override
	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GetLevelGiftRewardReq req = (GetLevelGiftRewardReq) message.getBody();
		
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();

		header.setProtocolId(Command.GET_LEVEL_GIFT_RESP);

		GetLevelGiftRewardResp resp = checkInMgtService.getLevelGiftReward(roleId, req);

		message.setBody(resp);
		message.setHeader(header);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_26") + ":result=" + resp.getResult() + ",roleID=" + roleId);
		}
	}

}
