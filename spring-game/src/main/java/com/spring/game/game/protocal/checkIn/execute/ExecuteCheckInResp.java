package com.snail.webgame.game.protocal.checkIn.execute;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ExecuteCheckInResp extends MessageBody {
	private int result;
	private int day;// 领取天数
	private int isGetVip;// 是否是领取vip奖励 1-是

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("day", 0);
		ps.add("isGetVip", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getIsGetVip() {
		return isGetVip;
	}

	public void setIsGetVip(int isGetVip) {
		this.isGetVip = isGetVip;
	}
	
}
