package com.snail.webgame.game.protocal.vipshop.checkpay;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PayBeforeCheckResp extends MessageBody {

	private int result;
	private int itemId;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("itemId", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

}
