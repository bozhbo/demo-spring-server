package com.snail.webgame.game.protocal.snatch.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QuerySnatchInfoResp extends MessageBody {
	private int result;
	private long safeModeLeftTimeMillis;//安全模式剩余时间(毫秒)
	private byte snatchleftTimes;	//夺宝剩余次数
	private byte snatchTotalTimes;	//夺宝总次数
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("safeModeLeftTimeMillis", 0);
//		ps.add("snatchleftTimes", 0);
//		ps.add("snatchTotalTimes", 0);
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public long getSafeModeLeftTimeMillis() {
		return safeModeLeftTimeMillis;
	}
	public void setSafeModeLeftTimeMillis(long safeModeLeftTimeMillis) {
		this.safeModeLeftTimeMillis = safeModeLeftTimeMillis;
	}
	public byte getSnatchleftTimes() {
		return snatchleftTimes;
	}
	public void setSnatchleftTimes(byte snatchleftTimes) {
		this.snatchleftTimes = snatchleftTimes;
	}
	public byte getSnatchTotalTimes() {
		return snatchTotalTimes;
	}
	public void setSnatchTotalTimes(byte snatchTotalTimes) {
		this.snatchTotalTimes = snatchTotalTimes;
	}
}
