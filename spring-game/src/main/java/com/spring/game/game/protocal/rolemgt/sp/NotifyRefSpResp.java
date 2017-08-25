package com.snail.webgame.game.protocal.rolemgt.sp;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class NotifyRefSpResp extends MessageBody {
	
	private int sp;// 最新的体力
	private long lastRefSpTime;// 最新的体力回复时间
	private byte refFlag;// 更新标记 0-表示都更新 1-表示只跟体力，时间不变
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("sp", 0);
		ps.add("lastRefSpTime", 0);
		ps.add("refFlag", 0);
	}

	public int getSp() {
		return sp;
	}

	public void setSp(int sp) {
		this.sp = sp;
	}

	public long getLastRefSpTime() {
		return lastRefSpTime;
	}

	public void setLastRefSpTime(long lastRefSpTime) {
		this.lastRefSpTime = lastRefSpTime;
	}

	public byte getRefFlag() {
		return refFlag;
	}

	public void setRefFlag(byte refFlag) {
		this.refFlag = refFlag;
	}

}
