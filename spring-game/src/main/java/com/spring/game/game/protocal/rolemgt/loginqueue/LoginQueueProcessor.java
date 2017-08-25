package com.snail.webgame.game.protocal.rolemgt.loginqueue;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;


public class LoginQueueProcessor extends ProtocolProcessor 
{
	private RoleMgtService roleMgtService;
	
	private static final Logger logger=LoggerFactory.getLogger("logs");
	

	public void setRoleMgtService(RoleMgtService roleMgtService) {
		this.roleMgtService = roleMgtService;
	}


	@Override
	public void execute(Request request, Response response) 
	{
		 
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		LoginQueueReq req = (LoginQueueReq) message.getBody();		
		head.setMsgType(Command.USER_LOGIN_QUEUE_RESP);		
		LoginQueueResp resp = roleMgtService.getLoginQueueResp(req.getAccount());		
		message.setBody(resp);
		response.write(message);
		
		if(logger.isInfoEnabled())
		{
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_6")+":result="+resp.getResult()+
					",index="+resp.getIndex()+",num="+resp.getNum());
		}
	}

}
