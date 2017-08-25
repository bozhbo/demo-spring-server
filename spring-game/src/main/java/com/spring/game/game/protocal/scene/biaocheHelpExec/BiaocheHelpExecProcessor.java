package com.snail.webgame.game.protocal.scene.biaocheHelpExec;

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
 * 接受或拒绝好友护送压镖请求
 * @author hongfm
 *
 */
public class BiaocheHelpExecProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private SceneMgtService sceneMgtService;
	
	public void setSceneMgtService(SceneMgtService sceneMgtService){
		this.sceneMgtService = sceneMgtService;
	}
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.BIAO_CHE_EXEC_HELP_RESP);
		int roleId = header.getUserID0();
		
		BiaocheHelpExecReq req = (BiaocheHelpExecReq) message.getBody();
		BiaocheHelpExecResp resp = sceneMgtService.BiaocheHelpExecResp(roleId,req);
		
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);
		
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_17")+",roleId="+roleId+"" +
					",result="+resp.getResult()+",helpRoleId="+req.getBeHelpRoleId());
		}
	}

}
