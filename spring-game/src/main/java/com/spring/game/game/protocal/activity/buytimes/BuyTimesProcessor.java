package com.snail.webgame.game.protocal.activity.buytimes;

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
 * 购买活动次数
 * 
 * @author xiasd
 *
 */
public class BuyTimesProcessor extends ProtocolProcessor {
	private static Logger logger = LoggerFactory.getLogger("logs");

	private ActivityService activityService;

	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.BUY_TIMES_RESP);
		int roleId = header.getUserID0();
		BuyTimesReq req = (BuyTimesReq) message.getBody();
		BuyTimesResp resp = activityService.buyTimes(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("BuyTimesProcessor " + ": result=" + resp.getResult() + ",roleId="
					+ roleId + ",buyType=" + req.getBuyType());
		}
	}

}
