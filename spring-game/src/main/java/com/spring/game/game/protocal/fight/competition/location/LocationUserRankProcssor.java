package com.snail.webgame.game.protocal.fight.competition.location;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.fight.competition.rank.QueryRankResp;
import com.snail.webgame.game.protocal.fight.competition.service.CompetitionService;

/**
 * 
 * 类介绍:角色自我排行查询 
 *
 * @author zhoubo
 * @2015年3月30日
 */
public class LocationUserRankProcssor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private CompetitionService competitionService;

	public void setCompetitionService(CompetitionService competitionService) {
		this.competitionService = competitionService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.FIGHT_RANK_QUERY_RESP);
		int roleId = header.getUserID0();
		QueryRankResp resp = this.competitionService.queryUserRank(roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_PVP_INFO_8") + ": result=" + resp.getCount() + ",roleId="
					+ roleId);
		}
	}
}
