package com.snail.webgame.game.protocal.rolemgt.detail;

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

public class RoleDetailProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleMgtService roleMgtService;

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	public void execute(Request request, Response response) {
		long startTime = System.currentTimeMillis();
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		RoleDetailResp resp = roleMgtService.getRoleDetailResp(roleId);
		head.setMsgType(Command.QUERY_ROLE_DETAIL_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_12") + ":result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
		
		logger.info("RoleDetailProcessor time = " + (System.currentTimeMillis() - startTime));
	}

}