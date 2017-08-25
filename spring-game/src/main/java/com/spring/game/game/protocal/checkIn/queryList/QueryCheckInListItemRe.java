package com.snail.webgame.game.protocal.checkIn.queryList;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryCheckInListItemRe extends MessageBody {
	private int day; // 第几天
	private byte checkIn;// 是否已领取(0未领取，1已领取)
	private String prizeNo;// itemNo,itemNum;itemNo,itemNum;...
	
	private byte isGetVip;// 是否已领取vip签到 (0未领取，1已领取)
	private String vipPrizeNo; //itemNo,itemNum;itemNo,itemNum;...
	private byte needVipLv;// vip奖励条件限制

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("day", 0);
		ps.add("checkIn", 0);
		ps.addString("prizeNo", "flashCode", 0);
		ps.add("isGetVip", 0);
		ps.addString("vipPrizeNo", "flashCode", 0);
		ps.add("needVipLv", 0);
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public byte getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(byte checkIn) {
		this.checkIn = checkIn;
	}

	public String getPrizeNo() {
		return prizeNo;
	}

	public void setPrizeNo(String prizeNo) {
		this.prizeNo = prizeNo;
	}

	public byte getIsGetVip() {
		return isGetVip;
	}

	public void setIsGetVip(byte isGetVip) {
		this.isGetVip = isGetVip;
	}

	public String getVipPrizeNo() {
		return vipPrizeNo;
	}

	public void setVipPrizeNo(String vipPrizeNo) {
		this.vipPrizeNo = vipPrizeNo;
	}

	public byte getNeedVipLv() {
		return needVipLv;
	}

	public void setNeedVipLv(byte needVipLv) {
		this.needVipLv = needVipLv;
	}

}
