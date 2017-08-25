package com.snail.webgame.game.protocal.rank.rank;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.rank.service.RankMgtService;

public class RankProcessor extends ProtocolProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private RankMgtService service;
	
	public RankProcessor()
	{
		service = new RankMgtService();
	}

	public void execute(Request request, Response response) 
	{
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		int roleId = head.getUserID0();
		RankReq req = (RankReq) message.getBody();
		head.setMsgType(Command.RANK_LIST_RESP);
		
		RankResp resp = service.rank(roleId, req);
		
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info("RankProcessor : result = " + resp.getResult() + ",roleId="
					+ roleId);
		}
	}

}
