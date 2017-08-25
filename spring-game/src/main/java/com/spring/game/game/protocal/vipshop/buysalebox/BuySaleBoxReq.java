package com.snail.webgame.game.protocal.vipshop.buysalebox;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuySaleBoxReq extends MessageBody {

	private int boxId;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("boxId", 0);
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

}
