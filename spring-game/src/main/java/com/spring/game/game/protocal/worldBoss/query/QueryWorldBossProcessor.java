package com.snail.webgame.game.protocal.worldBoss.query;

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
 * 查询神兵处理类
 * 
 * @author xiasd
 *
 */
public class QueryWorldBossProcessor extends ProtocolProcessor{

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private WorldBossMgtService worldBossMgtService;
	
	public QueryWorldBossProcessor()
	{
		worldBossMgtService = new WorldBossMgtService();
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		QueryWorldBossResp resp = worldBossMgtService.query(roleId);
		head.setMsgType(Command.QUERY_WORLD_BOSS_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info("QueryRoleWorldBossProcessor : result = " + resp.getResult() + ",roleId="
					+ roleId);
		}
	}
}
