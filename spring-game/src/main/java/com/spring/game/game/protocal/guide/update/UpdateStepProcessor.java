package com.snail.webgame.game.protocal.guide.update;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.guide.service.GuideMgtService;

/**
 * 更新新手引导信息
 * 
 * @author wanglinhui
 *
 */
public class UpdateStepProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private GuideMgtService guideMgtService;

	public void setGuideMgtService(GuideMgtService guideMgtService) {
		this.guideMgtService = guideMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();

		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();

		UpdateStepReq req = (UpdateStepReq) message.getBody();

		UpdateStepResp resp = guideMgtService.updateGuideStep(roleId, req);

		header.setMsgType(Command.UPDATE_ROLE_GUIDE_RESP);
		message.setBody(resp);
		message.setHeader(header);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ROLE_INFO_17")+" result = "+resp.getResult());
		}
	}
}
