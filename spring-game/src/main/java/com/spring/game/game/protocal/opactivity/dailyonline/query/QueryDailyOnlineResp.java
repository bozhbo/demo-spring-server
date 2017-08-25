package com.snail.webgame.game.protocal.opactivity.dailyonline.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryDailyOnlineResp extends MessageBody {

	private int result;
	private int totalOnline;// 今日总在线时长 单位(秒)
	private int count;
	private List<QueryDailyOnlineListRe> queryList; // 在线礼包信息

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("totalOnline", 0);
		ps.add("count", 0);
		ps.addObjectArray("queryList", "com.snail.webgame.game.protocal.opactivity.dailyonline.query.QueryDailyOnlineListRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getTotalOnline() {
		return totalOnline;
	}

	public void setTotalOnline(int totalOnline) {
		this.totalOnline = totalOnline;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<QueryDailyOnlineListRe> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<QueryDailyOnlineListRe> queryList) {
		this.queryList = queryList;
	}

}
