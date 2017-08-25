package com.snail.webgame.game.protocal.fight.team.match;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.fight.team.service.TeamService;

/**
 * 
 * 类介绍:匹配，支持组队和单人匹配
 *
 * @author zhoubo
 * @2015年6月4日
 */
public class TeamMatchPorcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.TEAM_MATCH_RESP);
		int roleId = header.getUserID0();
		TeamMatchReq req = (TeamMatchReq) message.getBody();
		TeamMatchResp resp = TeamService.getTeamService().match(req, roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("TeamMatchPorcessor start match " + ": result= 1,roleId=" + roleId);
		}
	}
}
