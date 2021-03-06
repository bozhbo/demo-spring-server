package com.snail.webgame.game.protocal.scene.mapMove;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;

/**
 * 大地图移动
 * @author hongfm
 *
 */
public class MapMvoeProcessor extends ProtocolProcessor {

	// private static final Logger logger = LoggerFactory.getLogger("logs");

	private SceneMgtService sceneMgtService;

	public void setSceneMgtService(SceneMgtService sceneMgtService) {
		this.sceneMgtService = sceneMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();

		MapMoveReq req = (MapMoveReq) message.getBody();
		sceneMgtService.mapMove(roleId, req);

		/*
		 * if (logger.isInfoEnabled()) { logger.info(Resource.getMessage("game",
		 * "GAME_SCENE_INFO_6")+",roleId="+roleId); }
		 */
	}

}
