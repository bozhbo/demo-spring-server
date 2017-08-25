package com.snail.webgame.game.protocal.relation.addOrDel;

import org.apache.log4j.Logger;
import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.relation.service.RoleRelationMgtService;

public class AddOrDelFriendProcessor extends ProtocolProcessor{
	private static final Logger logger = Logger.getLogger("logs");

	private RoleRelationMgtService roleRelationMgtService;
	
	public void setRoleRelationMgtService(RoleRelationMgtService roleRelationMgtService) {
		this.roleRelationMgtService = roleRelationMgtService;
	}
	
	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.ADD_OR_DEL_FRIEND_RESP);
		
		int roleId = header.getUserID0();
		
		AddOrDelFriendReq req = (AddOrDelFriendReq) msg.getBody();
		
		AddOrDelFriendResp resp = roleRelationMgtService.requestOperation(roleId, req);
		
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		
		String str = "";
		
		if(req.getFlag() == 0){
			str = "GAME_ROLE_RELATION_INFO_3";
		}else if(req.getFlag() == 1){
			str = "GAME_ROLE_RELATION_INFO_2";
		}
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", str) + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
		
	}

}
