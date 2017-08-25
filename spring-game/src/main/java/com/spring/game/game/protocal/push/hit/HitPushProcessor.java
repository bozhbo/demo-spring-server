package com.snail.webgame.game.protocal.push.hit;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.push.service.PushMgrService;

public class HitPushProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private PushMgrService pushMgrService;

	public void setPushMgrService(PushMgrService pushMgrService) {
		this.pushMgrService = pushMgrService;
	}

	@Override
	public void execute(Request request, Response response) {

		Message message = request.getMessage();

		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();
		
		HitPushInfoReq req = (HitPushInfoReq) message.getBody();

		header.setProtocolId(Command.HIT_PUSH_RESP);

		HitPushInfoResp resp = pushMgrService.hitPushInfo(roleId, req.getNo(), req.getState());

		message.setBody(resp);
		message.setHeader(header);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_PUSH_2") + ":result=" + resp.getResult() + ",roleId=" + roleId);
		}
	}

}
