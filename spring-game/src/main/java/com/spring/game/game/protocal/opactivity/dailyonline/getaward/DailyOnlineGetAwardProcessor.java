package com.snail.webgame.game.protocal.opactivity.dailyonline.getaward;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityMgrService;

/**
 * 领取在线礼包奖励
 * 
 * @author nijp
 *
 */
public class DailyOnlineGetAwardProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private OpActivityMgrService opActivityMgrService;

	public void setOpActivityMgrService(OpActivityMgrService opActivityMgrService) {
		this.opActivityMgrService = opActivityMgrService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.GET_DAILY_ONLINE_AWARD_RESP);
		DailyOnlineGetAwardReq req = (DailyOnlineGetAwardReq) message.getBody();
		int roleId = header.getUserID0();
		DailyOnlineGetAwardResp resp = opActivityMgrService.getDailyOnlineAward(roleId, req.getDailyOnlineNo());
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_OPACTIVITY_INFO_5") + ": result=" + resp.getResult() + ",roleId="
					+ roleId + ",dailyOnlineNo=" + resp.getDailyOnlineNo());
		}
	}

}