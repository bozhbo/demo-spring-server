package com.snail.webgame.game.protocal.quest.onekey;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.quest.service.QuestMgrService;

/**
 * 一键秒任务
 * 
 * @author nijp
 *
 */
public class OneKeyQuestProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private QuestMgrService questMgrService;

	public void setQuestMgrService(QuestMgrService questMgrService) {
		this.questMgrService = questMgrService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.ONE_KEY_QUEST_RESP);
		int roleId = header.getUserID0();
		OneKeyQuestReq req = (OneKeyQuestReq) message.getBody();
		OneKeyQuestResp resp = questMgrService.oneKeyQuest(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_QUEST_INFO_3") + ": result=" + resp.getResult() + ",roleId="
					+ roleId + ",QuestProtoNoStr=" + req.getQuestProtoNoStr());
		}
	}

}