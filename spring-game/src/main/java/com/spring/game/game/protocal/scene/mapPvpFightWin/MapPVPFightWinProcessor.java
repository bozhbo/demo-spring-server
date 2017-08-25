package com.snail.webgame.game.protocal.scene.mapPvpFightWin;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.scene.outCity.QueryMePointResp;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;

/**
 * 大地图PVP战斗胜利,切换场景至世界场景
 * @author hongfm
 *
 */
public class MapPVPFightWinProcessor extends ProtocolProcessor {

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
	
		QueryMePointResp resp = sceneMgtService.mapPVPFightEnd(roleId, 0);
		
		header.setMsgType(Command.SCREEN_ME_POSITION_RESP);
		header.setUserID0(roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_9")+",roleId="+roleId+",result="+resp.getResult());
		}
	}

}
