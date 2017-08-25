package com.snail.webgame.game.protocal.scene.queryOtherAI;

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
 * 查看场景其它玩家信息
 * @author hongfm
 *
 */
public class QueryOtherAIProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private SceneMgtService sceneMgtService;
	
	public void setSceneMgtService(SceneMgtService sceneMgtService){
		this.sceneMgtService = sceneMgtService;
	}
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		long roleId = header.getUserID0();
		header.setMsgType(Command.QUERY_OHER_AI_RESP);
	
		QueryOtherAIReq req = (QueryOtherAIReq) message.getBody();
		QueryOtherAIResp resp = sceneMgtService.queryOtherAI(req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);
		
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_4")+"result="+resp.getResult()+",roleId="+roleId+",otherRoleId="+req.getOtherRoleId());
		}
	}

}
