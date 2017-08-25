package com.snail.webgame.game.protocal.opactivity.sevenday.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QuerySevenDayResp extends MessageBody {

	private int result;
	private byte curDay;// 当前登录天数
	private int remainTime;// 剩余时间(秒)
	private int count;
	private List<QuerySevenDayListRe> queryList; // 七日礼包信息

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("curDay", 0);
		ps.add("remainTime", 0);
		ps.add("count", 0);
		ps.addObjectArray("queryList", "com.snail.webgame.game.protocal.opactivity.sevenday.query.QuerySevenDayListRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getCurDay() {
		return curDay;
	}

	public void setCurDay(byte curDay) {
		this.curDay = curDay;
	}

	public int getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(int remainTime) {
		this.remainTime = remainTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<QuerySevenDayListRe> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<QuerySevenDayListRe> queryList) {
		this.queryList = queryList;
	}

}
