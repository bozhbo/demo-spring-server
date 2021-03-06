package com.snail.webgame.game.protocal.equip.strength;

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

public class EquipOneKeyStrengthProcessor extends ProtocolProcessor{
	private static Logger logger = LoggerFactory.getLogger("logs");
	private EquipMgtService equipMgtService;
	
	public void setEquipMgtService(EquipMgtService equipMgtService) {
		this.equipMgtService = equipMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.EQUIP_ONEKEY_STRENGTH_RESP);
		
		int roleId = header.getUserID0();
		
		EquipOneKeyStrengthReq req = (EquipOneKeyStrengthReq) msg.getBody();
		
		EquipOneKeyStrengthResp resp = equipMgtService.equipOneKeyStrength(roleId, req);
		
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", "GAME_EQUIP_INFO_7") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
		
	}

}
