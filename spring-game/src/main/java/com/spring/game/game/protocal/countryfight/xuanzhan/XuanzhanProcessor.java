package com.snail.webgame.game.protocal.countryfight.xuanzhan;

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
 * 宣战
 * 
 * @author xiasd
 *
 */
public class XuanzhanProcessor extends ProtocolProcessor{

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.CITY_XUANZHAN_RESP);
		XuanzhanReq req = (XuanzhanReq) message.getBody();
		int roleId = header.getUserID0();
		
		XuanzhanResp resp = CountryCitySerivce.xuanzhan(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("QueryCityInfoProcessor result = " + resp.getResult() + " , roleId = " + roleId);
		}
	}

}
