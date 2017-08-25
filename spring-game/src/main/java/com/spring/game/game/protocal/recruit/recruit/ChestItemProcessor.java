package com.snail.webgame.game.protocal.recruit.recruit;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.protocal.recruit.service.ChestMgtService;

public class ChestItemProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private ChestMgtService recruitMgtService;

	public void setRecruitMgtService(ChestMgtService recruitMgtService) {
		this.recruitMgtService = recruitMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.RECRUIT_ITEM_RESP);
		int roleId = header.getUserID0();
		ChestItemReq req = (ChestItemReq) message.getBody();
		ChestItemResp resp = recruitMgtService.recruitItem(roleId, req);
		if(resp.getSourceType() == 2)
		{
			RoleInfoMap.chestCoinCost += resp.getSourceChange();
		}
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_RECRUIT_INFO_2") + ": result=" + resp.getResult()
					+ ",roleId=" + roleId + ",count=" + resp.getCount()+",action="+req.getAction());
		}
	}

}
