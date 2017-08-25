package com.snail.webgame.game.protocal.scene.biaocheVipBuy;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;

public class BiaocheVipBuyProcessor extends ProtocolProcessor{

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private SceneMgtService sceneMgtService;
	
	public void setSceneMgtService(SceneMgtService sceneMgtService){
		this.sceneMgtService = sceneMgtService;
	}
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.BIAO_CHE_VIP_BUY);
		
		int roleId = header.getUserID0();
		BiaocheVipBuyResp resp = sceneMgtService.biaocheVipBuy(roleId);
		
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);
		
		if (logger.isInfoEnabled()) {
			logger.info("BiaocheVipBuyProcessor : roleId="+roleId+",result="+resp.getResult());
		}
	}

}
