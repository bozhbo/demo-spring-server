package com.snail.webgame.game.protocal.shizhuang.getReward;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.shizhuang.service.ShizhuangMgtService;

/**
 * 集齐X套时装奖励消息管理器
 * @author nijy
 */
public class GetRewardProcessor extends ProtocolProcessor {
	private static Logger logger = LoggerFactory.getLogger("logs");

	private ShizhuangMgtService shizhuangMgtService;

	public void setShizhuangMgtService(ShizhuangMgtService shizhuangMgtService) {
		this.shizhuangMgtService = shizhuangMgtService;
	}

	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		GetRewardReq req = (GetRewardReq) message.getBody();
		int roleId = head.getUserID0();
		GetRewardResp resp = shizhuangMgtService.getShizhuangReward(roleId, req);
		head.setMsgType(Command.SHIZHUANG_REWARD_RESP);
		message.setBody(resp);
		response.write(message);
		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_SHIZHUANG_INFO_3") + ": result = " + resp.getResult()
					+ ",roleId=" + roleId);
		}

	}

}
