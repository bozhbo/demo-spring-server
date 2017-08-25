package com.snail.webgame.game.protocal.opactivity.sevenday.getaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetSevenDayAwardReq extends MessageBody {

	private byte getDay;// 领取天数
	private int subNo;// 领取编号

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("getDay", 0);
		ps.add("subNo", 0);
	}

	public byte getGetDay() {
		return getDay;
	}

	public void setGetDay(byte getDay) {
		this.getDay = getDay;
	}

	public int getSubNo() {
		return subNo;
	}

	public void setSubNo(int subNo) {
		this.subNo = subNo;
	}

}
