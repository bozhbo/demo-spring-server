package com.snail.webgame.game.protocal.weapon.upgrade;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.weapon.WeaponService;

public class UpgradeWeaponProcessor extends ProtocolProcessor{

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		UpgradeWeaponReq req = (UpgradeWeaponReq) message.getBody();
		UpgradeWeaponResp resp = WeaponService.upgradeWeapon(req, roleId);
		head.setMsgType(Command.UPGRADE_WEAPON_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info("UpgradeWeaponProcessor : result = " + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
