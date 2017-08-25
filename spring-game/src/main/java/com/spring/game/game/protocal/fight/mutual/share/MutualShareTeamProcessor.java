package com.snail.webgame.game.protocal.fight.mutual.share;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;

/**
 * 
 * 类介绍:分享组队到事件频道
 *
 * @author zhoubo
 * @2015年6月4日
 */
public class MutualShareTeamProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();
		MutualShareTeamReq req = (MutualShareTeamReq) message.getBody();
		MutualService.getMutualService().shareTeam(roleId, req.getActivityType(), req.getDuplicateNo());

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_PVP_INFO_18") + ": result= 1,roleId=" + roleId);
		}
	}
}
