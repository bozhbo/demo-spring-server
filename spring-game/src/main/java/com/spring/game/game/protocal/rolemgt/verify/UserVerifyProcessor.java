package com.snail.webgame.game.protocal.rolemgt.verify;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.util.IPCode;
import com.snail.webgame.engine.common.util.Sequence;
import com.snail.webgame.game.cache.BlackWriteAccountMap;
import com.snail.webgame.game.cache.RoleLoginQueueInfoMap;
import com.snail.webgame.game.cache.TempMsgrMap;
import com.snail.webgame.game.cache.UserAccountMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.core.ChargeGameService;
import com.snail.webgame.game.info.ChargeAccountInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;

/**
 * 接收到游戏客户端的登录请求，并处理
 * @author cici
 */
public class UserVerifyProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private RoleMgtService roleMgtService;

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}

	public void execute(Request request, Response response) {
		int sequenceId = Sequence.getSequenceId();
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		head.setMsgType(Command.USER_VERIFY_ROLE_RESP);
		UserVerifyReq req = (UserVerifyReq) message.getBody();
		req.setIP(head.getUserID3());
		if (request.getSession() != null && request.getSession().isConnected()) {
			if (GameValue.GAME_VALIDATEIN_FLAG == 0) {
				
				UserVerifyResp resp = RoleMgtService.getUserVerifyResp(req.getAccount(), head);
				
				// 账号过滤
				if(GameValue.ACCOUNT_FLAG == 1)
				{
					if(BlackWriteAccountMap.getBlackAccountList().contains(req.getAccount().toUpperCase())
							|| !BlackWriteAccountMap.getWriteAccountList().contains(req.getAccount().toUpperCase()))
					{
						resp.setResult(ErrorCode.ROLE_ACCOUNT_ERROR_11);
						message.setBody(resp);
						response.write(message);
						if (logger.isInfoEnabled()) {
							logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_10") + ":result=" + resp.getResult()
									+ ";account=" + req.getAccount() + "");
							
							return;
						}
					}
				}
				
				ChargeAccountInfo info =  new ChargeAccountInfo();
				info.setChargeAccount(req.getAccount());
				info.setAccountId(0);
				info.setValidate("");
				info.setGmLevel(1);
				info.setAccInfo("");
				info.setIssuerID(0);
				info.setHmacStr("");
				info.setAddTime(System.currentTimeMillis());
				UserAccountMap.addForChargeAccount(info.getChargeAccount().toUpperCase(), info);
				
				// 检查是否需要排队
				int loginResult = RoleMgtService.isNotNeedQuene(req.getAccount(), head);
				if(loginResult != 1){
					return;
				}
				
				if (GameValue.GAME_LOGIN_QUEUE_FLAG == 1)
				{
					RoleLoginQueueInfoMap.addLoginList(info.getChargeAccount().toUpperCase());
				}
				
				
				//提示玩家排队后就不发送A002,直到排队结束后才让玩家进入角色选择界面
				message.setBody(resp);
				response.write(message);
				
				if (logger.isInfoEnabled()) {
					logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_10") + ":result=" + resp.getResult()
							+ ";account=" + req.getAccount() + "");
				}
			} else {
				request.getSession().setAttribute("sequenceId", sequenceId);
				TempMsgrMap.addMessage(sequenceId, message);
				try {
					String ip = IPCode.intToIP(req.getIP());
					
					// 处理华为渠道串中出现反斜杠问题
					String validate = req.getValidate();
					if (validate != null && !"".equals(validate.trim())) {
						if (validate.startsWith("#huawei")) {
							validate = validate.replaceAll("\\\\","");
						}
					}
					
					// 公司官网validate去除
					String validate1 = req.getValidate();
					if (validate1 != null && !"".equals(validate1.trim())) {
						if (validate1.equalsIgnoreCase("LoginByPass")) {
							validate = "";
						}
					}
					
					ChargeGameService.getChargeService().sendUserLogin(sequenceId, req.getAccount(),
							req.getMd5Pass(), ip, validate, req.getClientType());
					logger.info("11------charge login req:account="+req.getAccount()+",sequenceId=" + sequenceId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
