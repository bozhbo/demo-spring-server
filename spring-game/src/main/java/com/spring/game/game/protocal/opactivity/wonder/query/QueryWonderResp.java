package com.snail.webgame.game.protocal.opactivity.wonder.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryWonderResp extends MessageBody {

	private int result;
	private byte isBuyPlan;// 是否已购买投资计划 0-不可以买 1-可以买 2-已购买
	private int totalCost;// 累计消费
	private int count;
	private List<QueryWonderListRe> queryList; // 七日礼包信息

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("isBuyPlan", 0);
		ps.add("totalCost", 0);
		ps.add("count", 0);
		ps.addObjectArray("queryList", "com.snail.webgame.game.protocal.opactivity.wonder.query.QueryWonderListRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getIsBuyPlan() {
		return isBuyPlan;
	}

	public void setIsBuyPlan(byte isBuyPlan) {
		this.isBuyPlan = isBuyPlan;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<QueryWonderListRe> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<QueryWonderListRe> queryList) {
		this.queryList = queryList;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

}
