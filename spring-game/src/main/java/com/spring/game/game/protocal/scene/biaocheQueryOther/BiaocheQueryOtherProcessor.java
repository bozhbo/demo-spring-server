package com.snail.webgame.game.protocal.scene.biaocheQueryOther;

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
 * 查看世界地图上的他人镖车
 * @author hongfm
 *
 */
public class BiaocheQueryOtherProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private SceneMgtService sceneMgtService;
	
	public void setSceneMgtService(SceneMgtService sceneMgtService){
		this.sceneMgtService = sceneMgtService;
	}
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_OTHER_ROLE_BIAO_CHE_RESP);
		
		BiaocheQueryOtherReq req = (BiaocheQueryOtherReq) message.getBody();
		
		int roleId = header.getUserID0();
		BiaocheQueryOtherResp resp = sceneMgtService.queryOtherBiaoche(roleId,req);
		
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);
		
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SCENE_INFO_20")+",roleId="+roleId+",result="+resp.getResult());
		}
	}

}
