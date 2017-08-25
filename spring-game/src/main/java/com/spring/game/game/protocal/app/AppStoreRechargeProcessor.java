package com.snail.webgame.game.protocal.app;

import org.epilot.ccf.core.processor.ProtocolProcessor;
import org.epilot.ccf.core.processor.Request;
import org.epilot.ccf.core.processor.Response;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.app.service.AppService;

/**
 * 成就奖励领取
 * 
 * @author shenggm
 * @since 2013-10-21
 * @version V1.0.0
 */
public class AppStoreRechargeProcessor extends ProtocolProcessor {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	@Override
	public void execute(Request request, Response response) {
		Message message = request.getMessage();
		GameMessageHead head = (GameMessageHead) message.getHeader();
		AppStoreRechargeReq req = (AppStoreRechargeReq) message.getBody();

		AppStoreRechargeResp resp = new AppStoreRechargeResp();
		resp.setAppOrderId(req.getTransactionIdentifier());
		head.setMsgType(Command.APP_CHARGE);
		resp.setResult(ErrorCode.CHARGE_POINT_NOT_ARRIVAL);
		message.setBody(resp);

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(head.getUserID0());

		if (roleInfo == null) {
			logger.error("APP rechare error roleInfo is null");
		} else {
			synchronized (roleInfo) {
				if (logger.isInfoEnabled()) {
					logger.info("AppStoreRechargeProcessor roleId = " + head.getUserID0());
				}
				
				try {
					String sCardType = req.getPid();// 卡类型 900

					if(logger.isWarnEnabled()){
						logger.warn("AppStoreRechargeProcessor  :  account = " + roleInfo.getAccount() + ", sCardType = " + sCardType);
					}
					
					AppService.doAppCharge(roleInfo, req, resp);
//					response.write(message);
					
				} catch (Exception e) {
					logger.error("APP rechare error " , e);
					
					resp.setResult(ErrorCode.CHARGE_POINT_NOT_ARRIVAL);
//					response.write(message);
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("AppStoreRechargeProcessor :result=" + resp.getResult() + ",roleId = " + head.getUserID0());
			}
		}
	}
}
