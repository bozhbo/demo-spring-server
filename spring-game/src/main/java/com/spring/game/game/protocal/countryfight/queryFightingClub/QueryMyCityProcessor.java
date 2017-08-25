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
 * 自己的工会今日战事
 * 
 * @author xiasd
 *
 */
public class QueryMyCityProcessor extends ProtocolProcessor{

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.CITY_MY_CITY_RESP);
		int roleId = header.getUserID0();
		QueryFightingClubResp resp = CountryCitySerivce.queryMyCityInfo();
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("QueryMyCityProcessor sucessed... roleId = " + roleId);
		}
	}

}
