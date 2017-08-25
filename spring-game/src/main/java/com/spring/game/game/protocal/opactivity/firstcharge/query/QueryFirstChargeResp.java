package com.snail.webgame.game.protocal.opactivity.firstcharge.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryFirstChargeResp extends MessageBody {

	private int result;
	private byte actType;// 活动类型 1-首充 2-手机绑定
	private byte actState;// 0-活动未完成 1-可领取奖励 2-已领奖

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("actType", 0);
		ps.add("actState", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getActType() {
		return actType;
	}

	public void setActType(byte actType) {
		this.actType = actType;
	}

	public byte getActState() {
		return actState;
	}

	public void setActState(byte actState) {
		this.actState = actState;
	}

}
