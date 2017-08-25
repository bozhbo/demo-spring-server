package com.snail.webgame.game.protocal.checkIn.queryList;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryCheckInListResp extends MessageBody {
	private int result;
	private int today;// 当前可签的第几天
	private int remedyCost;// 补签花费
	private int checkInListItemListSize;// list长度
	private List<QueryCheckInListItemRe> checkInListItemList;// 每日信息

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("today", 0);
		ps.add("remedyCost", 0);
		ps.add("checkInListItemListSize", 0);
		ps.addObjectArray(
				"checkInListItemList",
				"com.snail.webgame.game.protocal.checkIn.queryList.QueryCheckInListItemRe",
				"checkInListItemListSize");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getCheckInListItemListSize() {
		return checkInListItemListSize;
	}

	public void setCheckInListItemListSize(int checkInListItemListSize) {
		this.checkInListItemListSize = checkInListItemListSize;
	}

	public List<QueryCheckInListItemRe> getCheckInListItemList() {
		return checkInListItemList;
	}

	public void setCheckInListItemList(List<QueryCheckInListItemRe> checkInListItemList) {
		this.checkInListItemList = checkInListItemList;
	}

	public int getToday() {
		return today;
	}

	public void setToday(int today) {
		this.today = today;
	}

	public int getRemedyCost() {
		return remedyCost;
	}

	public void setRemedyCost(int remedyCost) {
		this.remedyCost = remedyCost;
	}

}
