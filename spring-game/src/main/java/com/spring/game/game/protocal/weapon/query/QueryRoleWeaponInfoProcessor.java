package com.snail.webgame.game.protocal.weapon.query;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.weapon.WeaponService;

/**
 * 查询神兵处理类
 * 
 * @author xiasd
 *
 */
public class QueryRoleWeaponInfoProcessor extends ProtocolProcessor{

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		QueryRoleWeaponInfoResp resp = WeaponService.getRoleWeaponInfo(roleId);
		head.setMsgType(Command.QUERY_WEAPON_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info("QueryRoleWeaponInfoProcessor : result = " + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
