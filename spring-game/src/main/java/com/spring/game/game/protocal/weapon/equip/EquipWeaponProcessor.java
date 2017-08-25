package com.snail.webgame.game.protocal.weapon.equip;

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
 * 装备神兵处理类
 * 
 * @author xiasd
 *
 */
public class EquipWeaponProcessor extends ProtocolProcessor{

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		EquipWeaponReq req = (EquipWeaponReq) message.getBody();
		EquipWeaponResp resp = WeaponService.equipWeapon(roleId, req);
		head.setMsgType(Command.EQUIP_WEAPON_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info("EquipWeaponProcessor : result = " + resp.getResult() + ",roleId=" + roleId);
		}
	}

}
