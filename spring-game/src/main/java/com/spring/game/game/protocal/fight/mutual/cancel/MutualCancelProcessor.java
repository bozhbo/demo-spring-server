package com.snail.webgame.game.protocal.fight.mutual.cancel;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;

/**
 * 
 * 类介绍:取消匹配，取消匹配不会立即生效，会延迟2秒，用于战斗匹配成功后如果是刚刚取消也需要进入战斗
 * 此功能需要报名配合，刚刚取消不能立即开始报名匹配
 * @author zhoubo
 * @2015年6月4日
 */
public class MutualCancelProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.MUTUAL_CANCEL_MATCH_RESP);
		int roleId = header.getUserID0();
		MutualCancelResp resp = MutualService.getMutualService().cancelMatch(roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_PVP_INFO_10") + ": result= 1,roleId=" + roleId);
		}
	}
}
