package com.snail.webgame.game.protocal.opactivity.appComment;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.opactivity.service.OpActivityMgrService;

/**
 * 领取苹果五星评论奖励
 * 
 * @author hongfm
 *
 */
public class AppCommentProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private OpActivityMgrService opActivityMgrService;

	public void setOpActivityMgrService(OpActivityMgrService opActivityMgrService) {
		this.opActivityMgrService = opActivityMgrService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.APP_COMMENT_RESP);
		int roleId = header.getUserID0();
		AppCommentReq req = (AppCommentReq) message.getBody();
		AppCommentResp resp = opActivityMgrService.appCommentAward(roleId,req);
		message.setHeader(header);
		if(resp.getResult() == 1)
		{
			message.setBody(resp);
		}

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_OPACTIVITY_INFO_17") + ": result=" + resp.getResult());
		}
	}

}