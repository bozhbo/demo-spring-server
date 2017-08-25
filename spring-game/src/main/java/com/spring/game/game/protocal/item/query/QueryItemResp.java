package com.snail.webgame.game.protocal.item.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryItemResp extends MessageBody {

	private int result;
	//物品信息
	private int count;
	private List<BagItemRe> list = new ArrayList<BagItemRe>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.item.query.BagItemRe", "count");
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

	public List<BagItemRe> getList() {
		return list;
	}

	public void setList(List<BagItemRe> list) {
		this.list = list;
	}
}