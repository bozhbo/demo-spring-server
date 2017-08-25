package com.snail.webgame.game.protocal.rolemgt.sp;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class NotifyRefTechResp extends MessageBody {

	private int tech;// 最新的技能点
	private long lastRefTechTime;// 最新的技能点回复时间
	private short buyTimeNum;// 今日购买次数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("tech", 0);
		ps.add("lastRefTechTime", 0);
		ps.add("buyTimeNum", 0);
	}

	public int getTech() {
		return tech;
	}

	public void setTech(int tech) {
		this.tech = tech;
	}

	public long getLastRefTechTime() {
		return lastRefTechTime;
	}

	public void setLastRefTechTime(long lastRefTechTime) {
		this.lastRefTechTime = lastRefTechTime;
	}

	public short getBuyTimeNum() {
		return buyTimeNum;
	}

	public void setBuyTimeNum(short buyTimeNum) {
		this.buyTimeNum = buyTimeNum;
	}
}
