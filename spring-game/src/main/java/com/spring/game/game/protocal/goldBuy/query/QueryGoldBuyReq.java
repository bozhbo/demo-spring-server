package com.snail.webgame.game.protocal.goldBuy.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryGoldBuyReq extends MessageBody {

	private byte buyType;// 购买类型 GoldBuy.xml no

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("buyType", 0);
	}

	public byte getBuyType() {
		return buyType;
	}

	public void setBuyType(byte buyType) {
		this.buyType = buyType;
	}
}

