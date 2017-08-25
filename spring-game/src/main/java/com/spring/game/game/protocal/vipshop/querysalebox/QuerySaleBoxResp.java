package com.snail.webgame.game.protocal.vipshop.querysalebox;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QuerySaleBoxResp extends MessageBody {

	private int result;
	private int count;
	private List<QuerySaleBoxRe> queryList;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("queryList", "com.snail.webgame.game.protocal.vipshop.querysalebox.QuerySaleBoxRe", "count");
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

	public List<QuerySaleBoxRe> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<QuerySaleBoxRe> queryList) {
		this.queryList = queryList;
	}

}
