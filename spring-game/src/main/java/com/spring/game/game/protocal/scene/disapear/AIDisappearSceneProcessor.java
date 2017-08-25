package com.snail.webgame.game.protocal.scene.disapear;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;

/**
 * AI从场景消失
 * @author hongfm
 *
 */
public class AIDisappearSceneProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
//	private SceneMgtService sceneMgtService;
//	
//	public void setSceneMgtService(SceneMgtService sceneMgtService){
//		this.sceneMgtService = sceneMgtService;
//	}
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();
		SceneMgtService.AIDisapperForMoment(roleId);
		
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_3")+",roleId="+roleId);
		}
	}

}
