package com.snail.webgame.game.protocal.opactivity.sevenday.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QuerySevenDayListRe extends MessageBody {
	private byte day;// 天数
	private int subNo; // 编号
	private byte isGet;// 是否可领取 0-不可领取 1-可领取 2-已领取(已购买)

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("day", 0);
		ps.add("subNo", 0);
		ps.add("isGet", 0);
	}

	public byte getDay() {
		return day;
	}

	public void setDay(byte day) {
		this.day = day;
	}

	public int getSubNo() {
		return subNo;
	}

	public void setSubNo(int subNo) {
		this.subNo = subNo;
	}

	public byte getIsGet() {
		return isGet;
	}

	public void setIsGet(byte isGet) {
		this.isGet = isGet;
	}

}
