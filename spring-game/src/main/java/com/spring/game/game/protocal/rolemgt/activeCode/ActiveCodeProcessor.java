package com.snail.webgame.game.protocal.rolemgt.activeCode;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.Flag;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;

public class ActiveCodeProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleMgtService roleMgtService;

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	@Override
	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		ActiveCodeReq req = (ActiveCodeReq) message.getBody();
		if (Flag.flag == 0) {
			roleMgtService.activeCode(roleId, req);
		}else{
			head.setMsgType(Command.USER_ACTIVE_CODE_RESP);
			ActiveCodeResp resp = new ActiveCodeResp();
			resp.setResult(ErrorCode.ROLE_ACTIVE_CODE_ERROR_1);
			message.setBody(resp);
			response.write(message);
		}
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_23") + ",redeemCode=" + req.getRedeemCode());
		}
	}

}
