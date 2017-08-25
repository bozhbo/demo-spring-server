package com.snail.webgame.game.protocal.opactivity.weixin.query;

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
 * 查询微信界面信息
 * 
 * @author nijp
 *
 */
public class QueryWeiXinProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private OpActivityMgrService opActivityMgrService;

	public void setOpActivityMgrService(OpActivityMgrService opActivityMgrService) {
		this.opActivityMgrService = opActivityMgrService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_WEIXIN_RESP);
		int roleId = header.getUserID0();
		QueryWeiXinResp resp = opActivityMgrService.queryWeiXin(roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_OPACTIVITY_INFO_6") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}

}