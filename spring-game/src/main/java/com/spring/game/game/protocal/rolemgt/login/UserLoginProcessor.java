package com.snail.webgame.game.protocal.rolemgt.login;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.RoleLoginQueueInfoMap;
import com.snail.webgame.game.cache.UserAccountMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.info.ChargeAccountInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;

public class UserLoginProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleMgtService roleMgtService;

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	public void execute(Request request, Response response) {
		long startTime = System.currentTimeMillis();
		
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		UserLoginReq req = (UserLoginReq) message.getBody();
		req.setIP(head.getUserID3());
		head.setMsgType(Command.USER_LOGIN_RESP);
		UserLoginResp resp = new UserLoginResp();
		
		ChargeAccountInfo chargeInfo = UserAccountMap.getForChargeAccount(req.getAccount().toUpperCase());
		if (chargeInfo == null) {
			logger.info("---------------this is charge acc miss, it is bug!!!-------------------,recAccount="+req.getAccount());
			resp.setResult(ErrorCode.REQUEST_PARAM_ERROR_9);
			head.setMsgType(Command.USER_LOGIN_RESP);
			message.setBody(resp);
			response.write(message);

			if (logger.isInfoEnabled()) {
				logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_5") + ":result=" + resp.getResult() + ",roleId="
						+ req.getRoleId()+",account="+req.getAccount());
			}
			return;
		}
		
		int verify = verify(chargeInfo.getChargeAccount(), req.getRoleId());
		if (verify != 1) {
			resp.setResult(verify);
		} else {
			resp = roleMgtService.getUserLoginResp(req, chargeInfo.getChargeAccount(), head);
			if (resp.getResult() == 1) {
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(resp.getRoleId());
				if (roleInfo != null) {
					roleInfo.setMd5Pass(req.getMd5Pass());
					roleInfo.setValidate(req.getValidate());
					roleInfo.setClientType(req.getClientType());
					roleInfo.setPackageName(req.getPackageName());
					roleInfo.setMac(req.getMac());
				}
				
				// 登陆进去后，清除排队那边的可登陆缓存
				if (GameValue.GAME_LOGIN_QUEUE_FLAG == 1)
				{
					RoleLoginQueueInfoMap.removeMessageAccount(chargeInfo.getChargeAccount());
					RoleLoginQueueInfoMap.removeUserAccountLogout(chargeInfo.getChargeAccount());
				}
			}

		}
		head.setMsgType(Command.USER_LOGIN_RESP);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_5") + ":result=" + resp.getResult() + ",roleId="
					+ resp.getRoleId());
		}
		
		logger.info("userLoing = " + (System.currentTimeMillis() - startTime));
	}

	private int verify(String account, int roleId) {
		if (GameValue.IS_ALLOW_LOGIN == 0) {
			return ErrorCode.GAME_LOGIN_ERROR_5;
		}

		if (!RoleService.isCanLogin(1)) {
			return ErrorCode.GAME_LOGIN_ERROR_4;
		}
		if (account == null || account.trim().length() == 0) {
			return ErrorCode.ROLE_ACCOUNT_ERROR_4;
		}
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return ErrorCode.ROLE_NOT_EXIST_ERROR_25;
		}
		if (!roleInfo.getAccount().equalsIgnoreCase(account.trim())) {
			return ErrorCode.ROLE_NOT_EXIST_ERROR_26;
		}
		if (roleInfo.getCurrRoleStatus() != 0) {
			return ErrorCode.ROLE_STATUS_ERROR_6;
		}
		return 1;
	}
}
