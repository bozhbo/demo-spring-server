package com.snail.webgame.game.protocal.scene.sweepMapNpc;

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

public class SweepMapNpcProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private SceneMgtService sceneMgtService;

	public void setSceneMgtService(SceneMgtService sceneMgtService) {
		this.sceneMgtService = sceneMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.SWEEP_MAPNPC_RESP);
		SweepMapNpcReq req = (SweepMapNpcReq) message.getBody();
		int roleId = header.getUserID0();
		SweepMapNpcResp resp = sceneMgtService.sweepMapNpcNo(roleId, req);
		if (resp != null) {
			message.setHeader(header);
			message.setBody(resp);
			response.write(message);
			if (logger.isInfoEnabled()) {
				logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_21") + ",roleId=" + roleId + ",result="
						+ resp.getResult());
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_21") + ",roleId=" + roleId + " start fight");
			}
		}
	}

}
