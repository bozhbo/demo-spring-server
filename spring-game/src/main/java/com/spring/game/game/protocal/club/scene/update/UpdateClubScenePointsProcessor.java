package com.snail.webgame.game.protocal.club.scene.update;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.club.scene.service.ClubSecenMgtService;

public class UpdateClubScenePointsProcessor extends ProtocolProcessor{
	//private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private ClubSecenMgtService clubSecenMgtService;
	
	public void setClubSecenMgtService(ClubSecenMgtService clubSecenMgtService) {
		this.clubSecenMgtService = clubSecenMgtService;
	}
 

	@Override
	public void execute(Request request, Response response) {
		Message msg = request.getMessage();
		
		GameMessageHead header = (GameMessageHead) msg.getHeader();
		
		int roleId = header.getUserID0();
		
		UpdateClubScenePointsReq req = (UpdateClubScenePointsReq) msg.getBody();
		
		clubSecenMgtService.updateClubScenePoints(roleId, req);
		
	}

}
