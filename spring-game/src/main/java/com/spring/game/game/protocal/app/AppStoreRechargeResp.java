package com.snail.webgame.game.protocal.app;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * @author shenggm
 * @since 2013-10-21
 * @version V1.0.0
 */
public class AppStoreRechargeResp extends MessageBody {

	private int result;// 充值结果
	private String appOrderId;// 订单号

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("appOrderId", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getAppOrderId() {
		return appOrderId;
	}

	public void setAppOrderId(String appOrderId) {
		this.appOrderId = appOrderId;
	}

	
}
