package com.snail.webgame.game.protocal.relation.recommend;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.relation.service.RoleRelationMgtService;

public class RecommendFriend4AddProcessor extends ProtocolProcessor{
	private static Logger logger = LoggerFactory.getLogger("logs");
	
	private RoleRelationMgtService roleRelationMgtService;
	
	public void setRoleRelationMgtService( RoleRelationMgtService roleRelationMgtService){
		this.roleRelationMgtService = roleRelationMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.RECOMMEND_FRIEND_FOR_ADD_RESP);
		
		int roleId = header.getUserID0();
		
		RecommendFriend4AddReq req = (RecommendFriend4AddReq) msg.getBody();
		
		RecommendFriend4AddResp resp = roleRelationMgtService.recommendFriend4Add(roleId, req);
		
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", "GAME_ROLE_RELATION_INFO_5") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
		
	}

}
