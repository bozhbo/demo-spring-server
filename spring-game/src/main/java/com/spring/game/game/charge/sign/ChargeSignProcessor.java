package com.snail.webgame.game.charge.sign;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.util.Sequence;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.TempMsgrMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.core.ChargeGameService;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 生成签名
 * 
 * @author nijp
 *
 */
public class ChargeSignProcessor extends ProtocolProcessor {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();
		header.setProtocolId(0xA046);
		ChargeSignReq req = (ChargeSignReq) message.getBody();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		
		int sequenceId = Sequence.getSequenceId();
		TempMsgrMap.addMessage(sequenceId, message);
		try {
			int totalPrice = req.getBuyAmount() * Integer.valueOf(req.getProductPerPrice());
			ChargeGameService.getChargeService().sendMeizuSign(sequenceId, roleInfo.getRoleName(), req.getAppId(), req.getOrderId(), 
					req.getUid(), req.getProductId(), req.getProductSubject(), req.getProductBody(), "", req.getBuyAmount(), 
					req.getProductPerPrice(), totalPrice + "", req.getCreateTime(), 0, "");
			if (logger.isInfoEnabled()) {
				logger.info("#####ChargeSignProcessor" + ",sequenceId=" + sequenceId+",roleName="+roleInfo.getRoleName()+",req.getAppId()="+req.getAppId()
						+",req.getProductId()="+req.getProductId()+",totalPrice="+totalPrice);
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.error("", e);
			}
		}
	}
}
