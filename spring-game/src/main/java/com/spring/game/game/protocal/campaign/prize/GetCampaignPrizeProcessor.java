package com.snail.webgame.game.protocal.campaign.prize;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.campaign.service.CampaignMgtService;

public class GetCampaignPrizeProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private CampaignMgtService campaignMgtService;

	public void setCampaignMgtService(CampaignMgtService campaignMgtService) {
		this.campaignMgtService = campaignMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.GET_CAMPAIGN_PRIZE_RESP);
		int roleId = header.getUserID0();
		GetCampaignPrizeReq req = (GetCampaignPrizeReq) message.getBody();
		GetCampaignPrizeResp resp = campaignMgtService.getCampaignPrize(roleId, req);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_CAMPAIGN_INFO_4") + ": result=" + resp.getResult()
					+ ",roleId=" + roleId);
		}
	}

}
