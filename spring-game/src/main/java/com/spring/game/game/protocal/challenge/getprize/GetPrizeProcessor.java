package com.snail.webgame.game.protocal.challenge.getprize;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.challenge.service.ChallengeMgtService;

/**
 * 领取奖励
 * 
 * @author zhangyq
 * 
 */
public class GetPrizeProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private ChallengeMgtService challengeMgtService;

	public void setChallengeMgtService(ChallengeMgtService challengeMgtService) {
		this.challengeMgtService = challengeMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();
		GetPrizeReq req = (GetPrizeReq) message.getBody();
		header.setMsgType(Command.GET_CHALLENGE_CHAPTER_BATTLE_RESP);

		GetPrizeResp resp = challengeMgtService.getPrize(roleId, req);
		message.setBody(resp);
		message.setHeader(header);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_CHALLENGE_INFO_3") + ":result=" + resp.getResult()
					+ ",roleID=" + roleId);
		}
	}

}
