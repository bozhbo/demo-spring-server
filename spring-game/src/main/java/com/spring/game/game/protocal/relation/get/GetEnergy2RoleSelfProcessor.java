package com.snail.webgame.game.protocal.relation.get;

import org.apache.log4j.Logger;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.relation.service.RoleRelationMgtService;

public class GetEnergy2RoleSelfProcessor extends ProtocolProcessor{
	private static final Logger logger = Logger.getLogger("logs");

	private RoleRelationMgtService roleRelationMgtService;
	
	public void setRoleRelationMgtService(RoleRelationMgtService roleRelationMgtService) {
		this.roleRelationMgtService = roleRelationMgtService;
	}
	
	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.GET_PRESENT_ENERGY_2_ROLE_RESP);
		
		int roleId = header.getUserID0();

		GetEnergy2RoleSelfReq req = (GetEnergy2RoleSelfReq) msg.getBody();
		
		GetEnergy2RoleSelfResp resp = roleRelationMgtService.getPresentEnergy2RoleSelf(roleId, req);
		
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", "GAME_ROLE_RELATION_INFO_8") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}

}
