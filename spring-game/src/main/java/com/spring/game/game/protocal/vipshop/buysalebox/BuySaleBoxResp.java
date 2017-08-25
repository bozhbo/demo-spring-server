package com.snail.webgame.game.protocal.vipshop.buysalebox;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuySaleBoxResp extends MessageBody {

	private int result;
	private int boxId;
	private int remainBoxNum;// 剩余礼包个数

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("boxId", 0);
		ps.add("remainBoxNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public int getRemainBoxNum() {
		return remainBoxNum;
	}

	public void setRemainBoxNum(int remainBoxNum) {
		this.remainBoxNum = remainBoxNum;
	}

}
