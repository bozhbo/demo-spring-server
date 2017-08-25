package com.snail.webgame.game.protocal.equip.merge;

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
 * 装备合成
 * @author zhangyq
 *
 */
public class MergeEquipProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private EquipMgtService equipMgtService;

	public void setEquipMgtService(EquipMgtService equipMgtService) {
		this.equipMgtService = equipMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.MERGE_EQUIP_RESP);
		int roleId = header.getUserID0();
		MergeEquipReq req = (MergeEquipReq) message.getBody();
		MergeEquipResp resp = equipMgtService.mergeEquip(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_EQUIP_INFO_4") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
