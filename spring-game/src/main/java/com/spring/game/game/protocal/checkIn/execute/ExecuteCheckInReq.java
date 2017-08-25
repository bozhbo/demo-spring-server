package com.snail.webgame.game.protocal.checkIn.execute;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ExecuteCheckInReq extends MessageBody {
	private int day;// 第几天签到
	private int isRemedy;// 是否是补签 1-补签
	private int isGetVip;// 是否是领取vip奖励 1-是

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("day", 0);
		ps.add("isRemedy", 0);
		ps.add("isGetVip", 0);
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getIsRemedy() {
		return isRemedy;
	}

	public void setIsRemedy(int isRemedy) {
		this.isRemedy = isRemedy;
	}

	public int getIsGetVip() {
		return isGetVip;
	}

	public void setIsGetVip(int isGetVip) {
		this.isGetVip = isGetVip;
	}
	
}
