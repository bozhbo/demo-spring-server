package com.snail.webgame.game.protocal.rolemgt.create;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.rolemgt.login.UserLoginResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;

public class CreateRoleProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleMgtService roleMgtService;

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		short gateServerId = (short)head.getUserID1();
		head.setMsgType(Command.USER_CREATE_ROLE_RESP);
		CreateRoleReq req = (CreateRoleReq) message.getBody();
		req.setIP(head.getUserID3());
		UserLoginResp resp = new UserLoginResp();
		if (GameValue.IS_ALLOW_LOGIN == 0) {
			resp.setResult(ErrorCode.GAME_LOGIN_ERROR_1);
		} else {
			resp = roleMgtService.createRoleInfo(req, gateServerId);
		}
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_3") + ":result=" + resp.getResult() + ","
					+ "account=" + req.getAccount() + ",roleId=" + resp.getRoleId());
		}

	}
}
