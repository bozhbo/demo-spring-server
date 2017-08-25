package com.snail.webgame.game.protocal.soldier.upgrade;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.soldier.service.SoldierMgtService;

public class UpgradeSoldierProcessor extends ProtocolProcessor {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	private SoldierMgtService soldierMgtService;

	public void setSoldierMgtService(SoldierMgtService soldierMgtService) {
		this.soldierMgtService = soldierMgtService;
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		UpgradeSoldierReq req = (UpgradeSoldierReq)message.getBody();
		UpgradeSoldierResp resp = soldierMgtService.upgradeSoldier(roleId, req);
		head.setMsgType(Command.SOLDIER_UPGRADE_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_HERO_INFO_8") + ":result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
