package com.snail.webgame.game.protocal.club.scene.check;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.club.scene.service.ClubSecenMgtService;

public class CheckIsInSceneProcessor extends ProtocolProcessor{
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private ClubSecenMgtService clubSecenMgtService;
	
	public void setClubSecenMgtService(ClubSecenMgtService clubSecenMgtService) {
		this.clubSecenMgtService = clubSecenMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		
		int roleId = header.getUserID0();
		
		header.setMsgType(Command.IS_IN_SCENE_RESP);
		
		CheckIsInSceneReq req = (CheckIsInSceneReq) msg.getBody();
		
		CheckIsInSceneResp resp = clubSecenMgtService.checkIsInScene(roleId, req);
		
		msg.setHeader(header);
		msg.setBody(resp);
		response.write(msg);
		
		if(logger.isInfoEnabled()){
			logger.info(Resource.getMessage("game", "GAME_ROLE_CLUB_INFO_11") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
		
	}

}
