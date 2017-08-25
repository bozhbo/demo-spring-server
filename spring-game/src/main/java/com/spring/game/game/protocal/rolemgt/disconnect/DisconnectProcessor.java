package com.snail.webgame.game.protocal.rolemgt.disconnect;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;

/**
 * 1、玩家主动退出
 * 2、客户端连接断开
 * @author hongfm
 *
 */
public class DisconnectProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	private RoleMgtService roleMgtService;

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		DisconnectReq req = (DisconnectReq) message.getBody();
		int roleId = req.getRoleId();
		String account = req.getAccount();

		roleMgtService.disconnect(account,roleId,req);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_28") + ":roleId=" + roleId + ",disconnectPhase="
					+req.getDisconnectPhase() +",result="+req.getResult()+",account="+req.getAccount());
		}
	}

}
