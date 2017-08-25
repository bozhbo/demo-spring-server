package com.snail.webgame.game.protocal.stone.inlay;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.stone.service.StoneMgtService;

public class InlayStoneProcessor extends ProtocolProcessor {

private StoneMgtService stoneMgtService;
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public InlayStoneProcessor()
	{
		stoneMgtService = new StoneMgtService();
	}

	@Override
	public void execute(Request request, Response response) 
	{
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead)message.getHeader();
		int roleId = header.getUserID0();
		header.setProtocolId(Command.STONE_ADD_EQUIP_RESP);
		
		InlayStoneReq req = (InlayStoneReq)message.getBody();
		
		InlayStoneResp resp = stoneMgtService.inlayStone(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);
		if(logger.isInfoEnabled())
		{
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_20")+":result="+resp.getResult()+",roleID="+roleId);
		}

	}

}
