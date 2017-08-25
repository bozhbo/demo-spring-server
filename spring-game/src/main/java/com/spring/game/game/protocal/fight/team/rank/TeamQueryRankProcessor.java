package com.snail.webgame.game.protocal.fight.team.rank;

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
 * 类介绍:查询组队副本首杀速杀
 * 
 * @author xiasd
 *
 */
public class TeamQueryRankProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.TEAM_RANK_RESP);
		int roleId = header.getUserID0();
		TeamQueryRankReq req = (TeamQueryRankReq) message.getBody();
		
		TeamQueryRankResp resp = TeamService.getTeamService().queryRank(req.getDuplicateId());
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info("TeamQueryRankProcessor : result= 1,roleId=" + roleId);
		}
	}
}
