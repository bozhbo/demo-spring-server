package com.snail.webgame.game.protocal.attackAnother.sweep;

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

public class AttackAnotherSweepProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private AttackAnotherMgtService attackAnotherMgtService;

	public void setAttackAnotherMgtService(AttackAnotherMgtService attackAnotherMgtService) {
		this.attackAnotherMgtService = attackAnotherMgtService;
	}

	@Override
	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		AttackAnotherSweepReq req = (AttackAnotherSweepReq) message.getBody();
		header.setMsgType(Command.ATTACK_ANOTHER_SWEEP_RESP);
		int roleId = header.getUserID0();
		long time1 = System.currentTimeMillis();
		AttackAnotherSweepResp resp = attackAnotherMgtService.attackAnotherSweep(roleId, req);
		long time2 = System.currentTimeMillis();
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_ATTACK_ANOTHER_2") + ": result=" + resp.getResult()
					+ ",roleId=" + roleId + ",costTime=" + (time2 - time1));
		}
	}

}