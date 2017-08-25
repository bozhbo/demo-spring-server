package com.snail.webgame.game.protocal.club.operation;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.club.service.ClubMgtService;


public class OperationClubProcessor extends ProtocolProcessor{

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private ClubMgtService service;
	
	public void setClubMgtService(ClubMgtService service){
		this.service = service;
	}

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.OPERATION_CLUB_RESP);
		
		int roleId = header.getUserID0();
		
		OperationClubReq req = (OperationClubReq)msg.getBody();
		
		OperationClubResp resp = service.operationClub(roleId, req);
		
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", "GAME_ROLE_CLUB_INFO_4") + " Action Type " + req.getAction() +  ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
		
	}


}
