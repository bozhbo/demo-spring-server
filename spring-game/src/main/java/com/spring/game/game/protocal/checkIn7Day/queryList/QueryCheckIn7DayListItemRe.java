package com.snail.webgame.game.protocal.checkIn7Day.queryList;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryCheckIn7DayListItemRe extends MessageBody {
	private int no;		//编号(领取请求参数)
	private int day;	//天数
	private byte checkIn;//是否已领取(0未领取，1已领取)
	private String itemNo;//领取物品编号
	private int itemNum;//物品数量
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("no", 0);
		ps.add("day", 0);
		ps.add("checkIn", 0);
		ps.addString("itemNo", "flashCode", 0);
		ps.add("itemNum", 0);
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
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public int getItemNum() {
		return itemNum;
	}
	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
}
