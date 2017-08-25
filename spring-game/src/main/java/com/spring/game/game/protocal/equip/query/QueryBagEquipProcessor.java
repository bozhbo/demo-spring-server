package com.snail.webgame.game.protocal.equip.query;

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

public class QueryBagEquipProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private EquipMgtService equipMgtService;

	public void setEquipMgtService(EquipMgtService equipMgtService) {
		this.equipMgtService = equipMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_BAG_EQUIP_RESP);
		int roleId = header.getUserID0();
		QueryBagEquipReq req = (QueryBagEquipReq) message.getBody();
		QueryBagEquipResp resp = equipMgtService.queryBagEquip(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_EQUIP_INFO_1") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}

}
