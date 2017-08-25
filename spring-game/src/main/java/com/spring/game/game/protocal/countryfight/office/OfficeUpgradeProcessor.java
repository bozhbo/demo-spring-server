package com.snail.webgame.game.protocal.countryfight.office;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.countryfight.service.CountryCitySerivce;
import com.snail.webgame.game.protocal.countryfight.xuanzhan.XuanzhanReq;

/**
 * 升级官职
 * 
 * @author xiasd
 *
 */
public class OfficeUpgradeProcessor extends ProtocolProcessor{

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.CITY_OFFICE_UPGRADE_RESP);
		XuanzhanReq req = (XuanzhanReq) message.getBody();
		int roleId = header.getUserID0();
		
		OfficeUpgradeResp resp = CountryCitySerivce.officeUpgrade(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("OfficeUpgradeProcessor result = " + resp.getResult() + " , roleId = " + roleId);
		}
	}
}
