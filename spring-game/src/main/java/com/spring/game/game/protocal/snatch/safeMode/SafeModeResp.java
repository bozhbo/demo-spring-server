package com.snail.webgame.game.protocal.snatch.safeMode;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SafeModeResp extends MessageBody {
	private int result;
	private long safeModeLeftTimeMillie;//剩余安全时间
	private long remainGold;//剩余金子
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("safeModeLeftTimeMillie", 0);
		ps.add("remainGold", 0);
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public long getSafeModeLeftTimeMillie() {
		return safeModeLeftTimeMillie;
	}
	public void setSafeModeLeftTimeMillie(long safeModeLeftTimeMillie) {
		this.safeModeLeftTimeMillie = safeModeLeftTimeMillie;
	}
	public long getRemainGold() {
		return remainGold;
	}
	public void setRemainGold(long remainGold) {
		this.remainGold = remainGold;
	}
}
