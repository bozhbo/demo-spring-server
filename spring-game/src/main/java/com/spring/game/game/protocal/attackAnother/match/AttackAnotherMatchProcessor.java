package com.snail.webgame.game.protocal.attackAnother.match;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.attackAnother.service.AttackAnotherMgtService;

public class AttackAnotherMatchProcessor extends ProtocolProcessor {
	
	private static Logger logger = LoggerFactory.getLogger("logs");
	private AttackAnotherMgtService attackAnotherMgtService;

	@Override
	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		AttackAnotherMatchReq req = (AttackAnotherMatchReq) message.getBody();
		header.setMsgType(Command.ATTACK_ANOTHER_MATCH_RESP);
		int roleId = header.getUserID0();
		long time1 = System.currentTimeMillis();
		AttackAnotherMatchResp resp = attackAnotherMgtService.attackAnotherMatch(roleId, req);
		long time2 = System.currentTimeMillis();
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ATTACK_ANOTHER_1") + ": result=" + resp.getResult() + ",roleId="
					+ roleId+",costTime="+(time2-time1)+",heroSize="+resp.getMatchHeroListSize());
		}
		
	}

	public void setAttackAnotherMgtService(
			AttackAnotherMgtService attackAnotherMgtService) {
		this.attackAnotherMgtService = attackAnotherMgtService;
	}

}
