package com.snail.webgame.game.protocal.checkIn7Day.queryList;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryCheckIn7DayListResp extends MessageBody {
	private int result;
	private int today;	//当日连续登陆天数
	private int maxDay;	//最大连续登陆天数
	private int checkInListItemListSize;	//list长度
	private List<QueryCheckIn7DayListItemRe> checkInListItemList;	//每日奖励信息
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("today", 0);
		ps.add("maxDay", 0);
		ps.add("checkInListItemListSize", 0);
		ps.addObjectArray("checkInListItemList", "com.snail.webgame.game.protocal.checkIn7Day.queryList.QueryCheckIn7DayListItemRe", "checkInListItemListSize");
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getToday() {
		return today;
	}
	public void setToday(int today) {
		this.today = today;
	}
	public int getMaxDay() {
		return maxDay;
	}
	public void setMaxDay(int maxDay) {
		this.maxDay = maxDay;
	}
	public int getCheckInListItemListSize() {
		return checkInListItemListSize;
	}
	public void setCheckInListItemListSize(int checkInListItemListSize) {
		this.checkInListItemListSize = checkInListItemListSize;
	}
	public List<QueryCheckIn7DayListItemRe> getCheckInListItemList() {
		return checkInListItemList;
	}
	public void setCheckInListItemList(
			List<QueryCheckIn7DayListItemRe> checkInListItemList) {
		this.checkInListItemList = checkInListItemList;
	}
}
