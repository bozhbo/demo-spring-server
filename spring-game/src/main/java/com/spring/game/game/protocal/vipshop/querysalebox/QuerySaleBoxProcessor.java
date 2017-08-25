package com.snail.webgame.game.protocal.vipshop.querysalebox;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.protocal.vipshop.service.VipShopMgtService;

/**
 * 查询促销礼包信息
 * 
 * @author nijp
 *
 */
public class QuerySaleBoxProcessor extends ProtocolProcessor {

	private static Logger logger = LoggerFactory.getLogger("logs");

	private VipShopMgtService vipShopMgtService;

	public void setVipShopMgtService(VipShopMgtService vipShopMgtService) {
		this.vipShopMgtService = vipShopMgtService;
	}

	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		header.setMsgType(Command.QUERY_SALE_BOX_RESP);
		int roleId = header.getUserID0();
		QuerySaleBoxResp resp = vipShopMgtService.querySaleBoxInfo(roleId);
		message.setHeader(header);
		message.setBody(resp);
		response.write(message);

		if (logger.isInfoEnabled()) {
			logger.info(Resource.getMessage("game", "GAME_VIP_SHOP_INFO_4") + ": result=" + resp.getResult() + ",roleId="
					+ roleId);
		}
	}

}