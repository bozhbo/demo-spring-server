package com.snail.webgame.game.protocal.quest.finish;

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
 * 领取任务奖励
 * @author zenggang
 * 
 */
public class FinishQuestProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private QuestMgrService questMgrService;

	public void setQuestMgrService(QuestMgrService questMgrService) {
		this.questMgrService = questMgrService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.FINISH_QUEST_RESP);
		int roleId = header.getUserID0();
		FinishQuestReq req = (FinishQuestReq) message.getBody();
		FinishQuestResp resp = questMgrService.finishQuest(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_QUEST_INFO_2") + ": result=" + resp.getResult() + ",roleId="
					+ roleId + ",QuestProtoNo=" + req.getQuestProtoNo());
		}
	}

}