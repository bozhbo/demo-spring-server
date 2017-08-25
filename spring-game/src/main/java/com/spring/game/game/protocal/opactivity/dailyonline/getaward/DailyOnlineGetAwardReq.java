package com.snail.webgame.game.protocal.opactivity.dailyonline.getaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DailyOnlineGetAwardReq extends MessageBody {

	private int dailyOnlineNo;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("dailyOnlineNo", 0);
	}

	public int getDailyOnlineNo() {
		return dailyOnlineNo;
	}

	public void setDailyOnlineNo(int dailyOnlineNo) {
		this.dailyOnlineNo = dailyOnlineNo;
	}

}
