package com.snail.webgame.game.protocal.worship.doWorship;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.worship.service.WorshipMgtService;

/**
 * 膜拜入口类
 * @author luwd
 *
 */
public class DoWorshipProcessor extends ProtocolProcessor{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	WorshipMgtService worshipMgtService ;
	
	public void setWorshipMgtService(WorshipMgtService worshipMgtService) {
		this.worshipMgtService = worshipMgtService;
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		DoWorshipReq req = (DoWorshipReq) message.getBody();
		DoWorshipResp resp = worshipMgtService.doWorship(roleId, req);
		head.setMsgType(Command.DO_WORSHIP_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info("DoWorshipProcessor : result = " + resp.getResult() + ",roleId=" + roleId);
		}
	}
}
