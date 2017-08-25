package com.snail.webgame.game.protocal.rolemgt.voice;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 
 * 类介绍:查询角色UID
 *
 * @author zhoubo
 * @2015年7月31日
 */
public class QueryRoleVoiceUidProcessor extends ProtocolProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		
		GameMessageHead head = (GameMessageHead) message.getHeader();
		QueryRoleVoiceUidReq req = (QueryRoleVoiceUidReq)message.getBody();
		QueryRoleVoiceUidResp resp = new QueryRoleVoiceUidResp();
		
		if (req.getRoleName() != null && !"".equals(req.getRoleName())) {
			RoleInfo roleInfo = RoleInfoMap.getRoleInfoByName(req.getRoleName());
			
			if (roleInfo == null) {
				resp.setResult(ErrorCode.QUERY_ROLE_ERROR_1);
			} else {
				if (roleInfo.getVoiceUid() != null && !"".equals(roleInfo.getVoiceUid())) {
					resp.setVoiceUid(roleInfo.getVoiceUid());
					resp.setRoleName(req.getRoleName());
				} else {
					if (roleInfo.getUid() != null) {
						String[] strs = roleInfo.getUid().split("\\+");
						
						if (strs.length == 3) {
							roleInfo.setVoiceUid(strs[2]);
							resp.setVoiceUid(strs[2]);
							resp.setRoleName(req.getRoleName());
						} else {
							if (logger.isWarnEnabled()) {
								logger.warn("UID is error format, UID = " + roleInfo.getUid());
							}
							
							resp.setResult(ErrorCode.QUERY_ROLE_ERROR_1);
						}
					} else {
						resp.setResult(ErrorCode.QUERY_ROLE_ERROR_1);
					}
				}
			}
		} else {
			resp.setResult(ErrorCode.QUERY_ROLE_ERROR_1);
		}
		
		head.setMsgType(Command.QUERY_ROLE_UID_RESP);
		message.setBody(resp);
		response.write(message);
		
		if (logger.isInfoEnabled()) {
			logger.info("Query Role Voice Uid Processor, result = " + resp.getResult());
		}
	}
}
