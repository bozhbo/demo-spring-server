package com.snail.webgame.game.protocal.quest.query;

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
 * 查询用户任务信息
 * @author zenggang
 * 
 */
public class QueryQuestProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private QuestMgrService questMgrService;

	public void setQuestMgrService(QuestMgrService questMgrService) {
		this.questMgrService = questMgrService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_QUEST_RESP);
		int roleId = header.getUserID0();
		QueryQuestReq req = (QueryQuestReq) message.getBody();
		QueryQuestResp resp = questMgrService.queryQuest(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_QUEST_INFO_1") + ": result=" + resp.getResult() + ",roleId="
					+ roleId + ",count=" + resp.getCount());
		}
	}

}