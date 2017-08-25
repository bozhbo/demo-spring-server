package com.snail.webgame.game.protocal.scene.mapReachNPC;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;

/**
 * 大地图国家任务完成
 * @author hongfm
 *
 */
public class MapReachNPCProcessor extends ProtocolProcessor {

	//private static final Logger logger = LoggerFactory.getLogger("logs");
	
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
	
		MapReachNPCReq req = (MapReachNPCReq) message.getBody();
		sceneMgtService.mapReachNPC(roleId,req);
	}

}
