package com.snail.webgame.game.protocal.server.time;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.server.service.ServerActiveService;

public class SysTimeProcessor extends ProtocolProcessor {

	private ServerActiveService service;

	public SysTimeProcessor() {
		service = new ServerActiveService();
	}

	public void execute(Request request, Response response) {

		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		head.setMsgType(Command.GAME_SERVER_TIME_RESP);

		SysTimeResp resp = service.getSysTime();
		message.setBody(resp);

		response.write(message);

	}

}
