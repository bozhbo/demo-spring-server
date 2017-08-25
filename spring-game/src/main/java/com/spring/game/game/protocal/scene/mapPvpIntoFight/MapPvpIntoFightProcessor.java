package com.snail.webgame.game.protocal.scene.mapPvpIntoFight;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.scene.mapPvpFight.MapPvpFightResp;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;

/**
 * 大地图移动
 * @author hongfm
 *
 */
public class MapPvpIntoFightProcessor extends ProtocolProcessor {

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
		header.setMsgType(Command.MAP_PVP_FIGHT_RESP);
	
		MapPvpIntoFightReq req = (MapPvpIntoFightReq) message.getBody();
		MapPvpFightResp resp = sceneMgtService.mapPvpIntoFight(roleId,req);
		if(resp.getResult() != 1)
		{
			message.setHeader(header);
			message.setBody(resp);
			response.write(message);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_13")+",roleId="+roleId+",beRoleName="+req.getBeRoleName());
		}
	}

}
