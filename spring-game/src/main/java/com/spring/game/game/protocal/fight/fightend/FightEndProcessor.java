package com.snail.webgame.game.protocal.fight.fightend;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.fightdata.ServerFightEndReq;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.fight.service.FightMgtService;

/**
 * 战斗结束
 * 
 * @author tangjq
 * 
 */
public class FightEndProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	private FightMgtService fightMgtService;

	public void setFightMgtService(FightMgtService fightMgtService) {
		this.fightMgtService = fightMgtService;
	}

	@Override
	public void execute(Request request, Response response) {

		long now = System.currentTimeMillis();
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();

		int roleId = header.getUserID0();
		ServerFightEndReq req = (ServerFightEndReq) message.getBody();
		header.setMsgType(Command.FIGHT_END_RESP);
		FightEndResp resp = fightMgtService.battleEnd(roleId, req);
		message.setBody(resp);
		message.setHeader(header);
		response.write(message);

		long costTime = System.currentTimeMillis() - now;
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_FIGHT_INFO_4") + ":fightId=" + req.getFightId() + ",roleId="
					+ roleId + " cost time=" + costTime + "ms"+",result="+resp.getResult());
		}
	}
}
