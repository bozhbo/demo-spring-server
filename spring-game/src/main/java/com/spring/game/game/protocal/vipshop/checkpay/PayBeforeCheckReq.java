package com.snail.webgame.game.protocal.vipshop.checkpay;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PayBeforeCheckReq extends MessageBody {

	private int itemId;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("itemId", 0);
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

}
