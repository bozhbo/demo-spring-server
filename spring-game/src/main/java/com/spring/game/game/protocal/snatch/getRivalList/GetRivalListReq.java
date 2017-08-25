package com.snail.webgame.game.protocal.snatch.getRivalList;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetRivalListReq extends MessageBody {
	private int stoneNo;	//石头编号
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("stoneNo", 0);
	}
	public int getStoneNo() {
		return stoneNo;
	}
	public void setStoneNo(int stoneNo) {
		this.stoneNo = stoneNo;
	}
}
