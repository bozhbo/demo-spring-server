package com.snail.webgame.game.protocal.redPoint.service;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PopRedPointResp extends MessageBody {
	private int result = 1;
	private int redPoint;//红点数
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("redPoint", 0);
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getRedPoint() {
		return redPoint;
	}
	public void setRedPoint(int redPoint) {
		this.redPoint = redPoint;
	}
}
