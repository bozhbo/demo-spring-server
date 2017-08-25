package com.snail.webgame.game.protocal.opactivity.timeaction.querysingle;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryTimeActionSingleReq extends MessageBody {

	private int actId;// 已开启的活动id

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("actId", 0);
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

}
