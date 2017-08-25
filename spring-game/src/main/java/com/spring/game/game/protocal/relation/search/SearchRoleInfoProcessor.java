package com.snail.webgame.game.protocal.relation.search;

import org.apache.log4j.Logger;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.relation.service.RoleRelationMgtService;

public class SearchRoleInfoProcessor extends ProtocolProcessor{
	private static final Logger logger = Logger.getLogger("logs");

	private RoleRelationMgtService roleRelationMgtService;
	
	public void setRoleRelationMgtService(RoleRelationMgtService roleRelationMgtService) {
		this.roleRelationMgtService = roleRelationMgtService;
	}
	
	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.SEARCH_ROLE_INFO_RESP);
		
		int roleId = header.getUserID0();
		
		SearchRoleInfoReq req = (SearchRoleInfoReq) msg.getBody();
		
		SearchRoleInfoResp resp = roleRelationMgtService.searchRoleInfo4Add(roleId, req);
		
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", "GAME_ROLE_RELATION_INFO_5") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
		
	}

}
