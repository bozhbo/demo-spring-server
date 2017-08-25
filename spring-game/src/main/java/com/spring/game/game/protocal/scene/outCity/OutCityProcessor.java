package com.snail.webgame.game.protocal.scene.outCity;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;

/**
 * 出城
 * @author hongfm
 *
 */
public class OutCityProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private SceneMgtService sceneMgtService;
	
	public void setSceneMgtService(SceneMgtService sceneMgtService){
		this.sceneMgtService = sceneMgtService;
	}
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();
	
		QueryMePointResp resp = sceneMgtService.outCity(roleId, 0, 0);
		//自己在外城的坐标
		header.setMsgType(Command.SCREEN_ME_POSITION_RESP);
		header.setUserID0(roleId);
		message.setHeader(header);
		message.setBody(resp);
		
		if (resp != null && logger.isInfoEnabled()) {
			response.write(message);
			logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_5")+",roleId="+roleId +",result="+resp.getResult());
		}
	}

}
