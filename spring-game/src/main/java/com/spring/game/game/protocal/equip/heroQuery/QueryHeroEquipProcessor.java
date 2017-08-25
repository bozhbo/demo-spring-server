package com.snail.webgame.game.protocal.equip.heroQuery;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;

public class QueryHeroEquipProcessor extends ProtocolProcessor {

//	private static Logger logger = LoggerFactory.getLogger("logs");
//
//	private EquipMgtService equipMgtService;
//
//	public void setEquipMgtService(EquipMgtService equipMgtService) {
//		this.equipMgtService = equipMgtService;
//	}

	public void execute(Request request, Response response) {
//		Message message = request.getMessage();
//		GameMessageHead header = (GameMessageHead) message.getHeader();
//		header.setMsgType(Command.QUERY_BAG_EQUIP_RESP);
//		int roleId = header.getUserID0();
//		QueryHeroEquipReq req = (QueryHeroEquipReq) message.getBody();
//		QueryHeroEquipResp resp = equipMgtService.queryBagEquip(roleId, req);
//		message.setHeader(header);
//		message.setBody(resp);
//		response.write(message);
//
//		if (logger.isInfoEnabled()) {
//			logger.info(Resource.getMessage("game", "GAME_EQUIP_INFO_1") + ": result=" + resp.getResult() + ",roleId="
//					+ roleId);
//		}
	}

}
