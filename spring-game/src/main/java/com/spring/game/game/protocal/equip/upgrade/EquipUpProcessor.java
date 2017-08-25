package com.snail.webgame.game.protocal.equip.upgrade;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.equip.service.EquipMgtService;

/**
 * 装备强化
 * @author zhangyq
 *
 */
public class EquipUpProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private EquipMgtService equipMgtService;

	public void setEquipMgtService(EquipMgtService equipMgtService) {
		this.equipMgtService = equipMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.UP_EQUIP_RESP);
		int roleId = header.getUserID0();
		EquipUpReq req = (EquipUpReq) message.getBody();
		EquipUpResp resp = equipMgtService.newEquipStreng(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_EQUIP_INFO_3") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}

}
