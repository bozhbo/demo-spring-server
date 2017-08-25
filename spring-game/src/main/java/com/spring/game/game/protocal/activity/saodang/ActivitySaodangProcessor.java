package com.snail.webgame.game.protocal.activity.saodang;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.activity.service.ActivityService;

/**
 * 练兵场扫荡
 * 
 * @author nijy
 *
 */
public class ActivitySaodangProcessor extends ProtocolProcessor {
	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.ACTIVITY_SAODANG_RESP);
		int roleId = header.getUserID0();
		ActivitySaodangReq req = (ActivitySaodangReq) message.getBody();
		ActivitySaodangResp resp = ActivityService.saodangExpAction(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("SaodangProcessor " + ": result=" + resp.getResult() + ",roleId="
					+ roleId + ",saodangType=" + req.getSaodangType());
		}
	}

}
