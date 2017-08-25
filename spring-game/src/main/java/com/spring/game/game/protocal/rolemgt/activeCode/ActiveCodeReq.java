package com.snail.webgame.game.protocal.rolemgt.activeCode;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ActiveCodeReq extends MessageBody {
	private String redeemCode; //兑换码
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("redeemCode", "flashCode", 0);
	}
	public String getRedeemCode() {
		return redeemCode;
	}
	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}
}
