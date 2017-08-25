package com.snail.webgame.game.protocal.countryfight.querycity;

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
 * 查询所有城市信息
 * 
 * @author xiasd
 *
 */
public class QueryCityInfoProcessor extends ProtocolProcessor{

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.CITY_QUERY_INFO_RESP);
		int roleId = header.getUserID0();
		QueryCityInfoResp resp = CountryCitySerivce.queryCityInfo();
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("QueryCityInfoProcessor sucessed...roleId = " + roleId);
		}
	}

}
