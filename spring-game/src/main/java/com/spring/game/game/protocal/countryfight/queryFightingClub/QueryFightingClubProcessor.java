package com.snail.webgame.game.protocal.countryfight.queryFightingClub;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.countryfight.service.CountryCitySerivce;

/**
 * 观战城池战斗信息
 * 
 * @author xiasd
 *
 */
public class QueryFightingClubProcessor extends ProtocolProcessor{

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.CITY_QUERY_FIGHTING_RESP);
		int roleId = header.getUserID0();
		QueryFightingClubResp resp = CountryCitySerivce.queryFightingClub();
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("QueryFightingClubProcessor sucessed...roleId = " + roleId);
		}
	}

}
