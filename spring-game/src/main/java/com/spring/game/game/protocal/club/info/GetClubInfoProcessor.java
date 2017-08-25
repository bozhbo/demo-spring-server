package com.snail.webgame.game.protocal.club.info;

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


public class GetClubInfoProcessor extends ProtocolProcessor{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private ClubMgtService clubMgtService;
	
	public void setClubMgtService(ClubMgtService clubMgtService){
		this.clubMgtService = clubMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		header.setMsgType(Command.GET_CLUB_INFO_RESP);
		
		int roleId = header.getUserID0();
		
		GetClubInfoReq req = (GetClubInfoReq)msg.getBody();
		
		GetClubInfoResp resp = clubMgtService.getClubInfo(roleId, req);
		
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", "GAME_ROLE_CLUB_INFO_2") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
		
		
		
	}


}
