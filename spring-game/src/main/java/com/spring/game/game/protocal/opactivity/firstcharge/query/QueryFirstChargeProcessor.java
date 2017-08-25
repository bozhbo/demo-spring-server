package com.snail.webgame.game.protocal.opactivity.firstcharge.query;

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
 * 查询首冲界面信息
 * 
 * @author nijp
 *
 */
public class QueryFirstChargeProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private OpActivityMgrService opActivityMgrService;

	public void setOpActivityMgrService(OpActivityMgrService opActivityMgrService) {
		this.opActivityMgrService = opActivityMgrService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_FIRST_CHARGE_RESP);
		QueryFirstChargeReq req = (QueryFirstChargeReq) message.getBody();
		int roleId = header.getUserID0();
		QueryFirstChargeResp resp = opActivityMgrService.queryFirstCharge(roleId, req.getActType());
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_OPACTIVITY_INFO_1") + ": result=" + resp.getResult() + ",roleId="
					+ roleId + ",chargeState=" + resp.getActState());
		}
	}

}