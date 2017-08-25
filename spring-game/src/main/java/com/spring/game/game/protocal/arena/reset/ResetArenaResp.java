package com.snail.webgame.game.protocal.arena.reset;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ResetArenaResp extends MessageBody {

	private int result;
	private long resetTime;// 下次重置时间 0-不需重置

	private byte sourceType;
	private int sourceChange;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("resetTime", 0);

		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getResetTime() {
		return resetTime;
	}

	public void setResetTime(long resetTime) {
		this.resetTime = resetTime;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}
}
