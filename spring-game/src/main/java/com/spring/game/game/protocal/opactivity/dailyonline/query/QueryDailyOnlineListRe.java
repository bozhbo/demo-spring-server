package com.snail.webgame.game.protocal.opactivity.dailyonline.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryDailyOnlineListRe extends MessageBody {
	private int no; // 编号
	private byte isGet;// 是否已领取(0未领取，1已领取)

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("no", 0);
		ps.add("isGet", 0);
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public byte getIsGet() {
		return isGet;
	}

	public void setIsGet(byte isGet) {
		this.isGet = isGet;
	}

}
