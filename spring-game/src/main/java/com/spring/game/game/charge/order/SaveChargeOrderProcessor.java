package com.snail.webgame.game.charge.order;

import java.util.Date;

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
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.core.ChargeGameService;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.xml.cache.PayXMLInfoMap;
import com.snail.webgame.game.xml.info.PayXMLInfo;

/**
 * 保存订单信息
 * 
 * @author nijp
 *
 */
public class SaveChargeOrderProcessor extends ProtocolProcessor {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead header = (GameMessageHead) message.getHeader();
		int roleId = header.getUserID0();
		header.setProtocolId(0xA042);
		SaveChargeOrderReq req = (SaveChargeOrderReq) message.getBody();
		
		if (logger.isInfoEnabled()) {
			logger.info("receive client order, roleId = " + roleId + ", orderId = " + req.getOrderStr() + ", itemId = " + req.getItemId());
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null || req.getOrderStr() == null) {
			return;
		}
		
		RoleChargeService.saveChargeOrderInfo(roleInfo, req.getOrderStr(), Integer.valueOf(req.getItemId()),0, null);
		
		if (req.getNotifyUrl() != null && !"".equals(req.getNotifyUrl())) {
			// vivo渠道处理
			PayXMLInfo payXMLInfo = PayXMLInfoMap.fetchPayXMLInfo(Integer.valueOf(req.getItemId()));
			if (payXMLInfo == null) {
				return;
			}
			synchronized (roleInfo) {
				int sequenceId = Sequence.getSequenceId();
				TempMsgrMap.addMessage(sequenceId, message);
				try {
					int chargePrice = req.getChargePrice() * 100;
					ChargeGameService.getChargeService().sendVivoOrder(sequenceId, roleInfo.getRoleName(), req.getOrderStr(), req.getNotifyUrl(), 
							DateUtil.getDateStrFromDate(new Date()), "" + chargePrice, payXMLInfo.getName(), payXMLInfo.getName(), null);
					if (logger.isInfoEnabled()) {
						logger.info("#####SaveChargeOrderProcessor" + ",sequenceId=" + sequenceId+",roleName="+roleInfo.getRoleName()+",orderStr="+req.getOrderStr()
								+",payXMLInfo.getName()="+payXMLInfo.getName()+",chargePrice="+chargePrice);
					}
				} catch (Exception e) {
					if (logger.isInfoEnabled()) {
						logger.error("", e);
					}
				}
			}
			
			return;
		}
		
		SaveChargeOrderResp resp = new SaveChargeOrderResp();
		resp.setItemId(req.getItemId());
		resp.setOrderIdStr(req.getOrderStr());
		resp.setResult(1);
		
		message.setBody(resp);
		
		response.write(message);
	}
}
