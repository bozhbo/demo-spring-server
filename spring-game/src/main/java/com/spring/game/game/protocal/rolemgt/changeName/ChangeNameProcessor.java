package com.snail.webgame.game.protocal.rolemgt.changeName;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;

public class ChangeNameProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleMgtService roleMgtService;

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		ChangeNameReq req = (ChangeNameReq) message.getBody();
		head.setMsgType(Command.USER_CHANGE_ROLE_RESP);
		ChangeNameResp resp = roleMgtService.updateRoleName(req, roleId);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_22") + ":result=" + resp.getResult() + ",rolename="
					+ req.getRoleName());
		}
	}

}
