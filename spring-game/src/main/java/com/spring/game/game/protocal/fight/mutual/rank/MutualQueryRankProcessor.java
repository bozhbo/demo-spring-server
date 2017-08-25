package com.snail.webgame.game.protocal.fight.mutual.rank;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;

/**
 * 
 * 类介绍:排行查询
 *
 * @author zhoubo
 * @2015年6月4日
 */
public class MutualQueryRankProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.MUTUAL_RANK_QUERY_RESP);
		int roleId = header.getUserID0();
		
		MutualQueryRankResp resp = MutualService.getMutualService().queryRank(roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_PVP_INFO_17") + ": result= 1,roleId=" + roleId);
		}
	}
}
