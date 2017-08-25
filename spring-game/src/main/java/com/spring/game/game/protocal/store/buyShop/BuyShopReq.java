package com.snail.webgame.game.protocal.store.buyShop;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyShopReq extends MessageBody {

	private byte shopType;// 1-黑市商城 2-异域商城

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("shopType", 0);
	}

	public byte getShopType() {
		return shopType;
	}

	public void setShopType(byte shopType) {
		this.shopType = shopType;
	}

	
}
