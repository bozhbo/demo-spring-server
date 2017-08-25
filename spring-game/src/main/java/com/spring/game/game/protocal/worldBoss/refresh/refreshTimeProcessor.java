package com.snail.webgame.game.protocal.worldBoss.refresh;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.worldBoss.service.WorldBossMgtService;

/**
 * 刷新Boss
 * 
 * @author xiasd
 *
 */
public class refreshTimeProcessor extends ProtocolProcessor{

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private WorldBossMgtService worldBossMgtService;
	
	public refreshTimeProcessor()
	{
		worldBossMgtService = new WorldBossMgtService();
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		refreshTimeResp resp = worldBossMgtService.refresh(roleId);
		head.setMsgType(Command.REFRESH_WORLD_BOSS_TIME_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info("RefreshRoleWorldBossProcessor : result = " + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
