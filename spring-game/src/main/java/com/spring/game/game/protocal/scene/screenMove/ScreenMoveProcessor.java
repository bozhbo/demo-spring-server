package com.snail.webgame.game.protocal.scene.screenMove;

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
 * 玩家设备屏幕移到哪,就能看到哪的人
 * @author hongfm
 *
 */
public class ScreenMoveProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private SceneMgtService sceneMgtService;

	public void setSceneMgtService(SceneMgtService sceneMgtService) {
		this.sceneMgtService = sceneMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.SCREEN_MOVE_RESP);
		int roleId = header.getUserID0();

		ScreenMoveReq req = (ScreenMoveReq) message.getBody();
		ScreenMoveResp resp = sceneMgtService.screenMove(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_12") + ": result=" + resp.getResult());
		}
	}

}
