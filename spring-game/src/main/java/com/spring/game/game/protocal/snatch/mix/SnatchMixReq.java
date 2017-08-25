package com.snail.webgame.game.protocal.snatch.mix;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SnatchMixReq extends MessageBody {
	
	private int stoneNo; // 合成的石头编号
	private byte mixType;// 0-普通合成 1-一键合成

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("stoneNo", 0);
		ps.add("mixType", 0);
	}

	public int getStoneNo() {
		return stoneNo;
	}

	public void setStoneNo(int stoneNo) {
		this.stoneNo = stoneNo;
	}

	public byte getMixType() {
		return mixType;
	}

	public void setMixType(byte mixType) {
		this.mixType = mixType;
	}
}