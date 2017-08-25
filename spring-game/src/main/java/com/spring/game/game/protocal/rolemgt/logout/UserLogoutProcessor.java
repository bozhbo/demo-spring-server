package com.snail.webgame.game.protocal.rolemgt.logout;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;

/**
 * 玩家切换角色
 * @author hongfm
 *
 */
public class UserLogoutProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	private RoleMgtService roleMgtService;

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		UserLogoutReq req = (UserLogoutReq) message.getBody();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		head.setMsgType(Command.CHECK_LOGIN_OUT_RESP);
		int roleId = req.getRoleId();
		RoleInfo roleInfo = (RoleInfo) RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo != null) {
			if (GameValue.GAME_VALIDATEIN_FLAG == 0) {
			} else {
				//int sequence = Sequence.getSequenceId();
				try {					
					/*ChargeGameService.getChargeService().sendUserLogout(sequence, roleInfo.getAccount(),
							roleInfo.getMd5Pass(), roleInfo.getLoginIp(), roleInfo.getGmRight(),
							roleInfo.getAccountId());*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			UserLogoutResp resp = roleMgtService.userLogout(roleId);
			message.setHeader(head);
			message.setBody(resp);
			response.write(message);
			if (logger.isInfoEnabled()) {
				logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_7") + ":roleId=" + roleId);
			}
		}

	}

}
