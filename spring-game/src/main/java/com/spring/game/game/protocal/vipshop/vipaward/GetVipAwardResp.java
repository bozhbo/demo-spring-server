package com.snail.webgame.game.protocal.vipshop.vipaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetVipAwardResp extends MessageBody {

	private int result;
	private int vipNo;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("vipNo", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getVipNo() {
		return vipNo;
	}

	public void setVipNo(int vipNo) {
		this.vipNo = vipNo;
	}

}
