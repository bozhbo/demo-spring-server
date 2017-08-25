package com.snail.webgame.game.protocal.quest.talk;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.quest.service.QuestMgrService;

/**
 * 记录当前任务的对话顺序
 * 
 * @author nijp
 * 
 */
public class NpcTalkOrderProcessor extends ProtocolProcessor {

	private QuestMgrService questMgrService;

	public void setQuestMgrService(QuestMgrService questMgrService) {
		this.questMgrService = questMgrService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.TALK_ORDER_REQ);
		int roleId = header.getUserID0();
		NpcTalkOrderReq req = (NpcTalkOrderReq) message.getBody();
		questMgrService.dealTalkOrder(roleId, req);
		
	}

}