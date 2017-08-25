package com.snail.webgame.game.protocal.vipshop.vipaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetVipAwardReq extends MessageBody {

	private int vipNo;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("vipNo", 0);
	}

	public int getVipNo() {
		return vipNo;
	}

	public void setVipNo(int vipNo) {
		this.vipNo = vipNo;
	}

}
