package com.snail.webgame.game.protocal.opactivity.dailyonline.getaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DailyOnlineGetAwardResp extends MessageBody {

	private int result;
	private int dailyOnlineNo;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("dailyOnlineNo", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getDailyOnlineNo() {
		return dailyOnlineNo;
	}

	public void setDailyOnlineNo(int dailyOnlineNo) {
		this.dailyOnlineNo = dailyOnlineNo;
	}

}
