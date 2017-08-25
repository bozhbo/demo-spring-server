package com.snail.webgame.game.protocal.vipshop.queryvipbuy;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryVipBuyResp extends MessageBody {

	private int result;
	private int count;
	private List<QueryVipBuyRe> queryList;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("queryList", "com.snail.webgame.game.protocal.vipshop.queryvipbuy.QueryVipBuyRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<QueryVipBuyRe> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<QueryVipBuyRe> queryList) {
		this.queryList = queryList;
	}

}
