package com.snail.webgame.game.protocal.challenge.queryBattleDetail;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.challenge.service.ChallengeMgtService;

/**
 * 查询玩家已通过的副本及可打的副本
 * @author zhangyq
 *
 */
public class QueryBattleDetailProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private ChallengeMgtService challengeMgtService;

	public void setChallengeMgtService(ChallengeMgtService challengeMgtService) {
		this.challengeMgtService = challengeMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_CHALLENGE_DETAIL_RESP);
		int roleId = header.getUserID0();
		QueryBattleDetailResp resp = challengeMgtService.queryDetail(roleId);

		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_CHALLENGE_INFO_6") + ": result=" + resp.getResult()
					+ ",roleId=" + roleId);
		}
	}
}
