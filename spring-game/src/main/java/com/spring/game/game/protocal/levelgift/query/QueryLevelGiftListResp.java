package com.snail.webgame.game.protocal.levelgift.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryLevelGiftListResp extends MessageBody {
	private int result;
	private int queryListSize; // list长度
	private List<QueryLevelGiftListRe> queryList; // 等级礼包信息

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("queryListSize", 0);
		ps.addObjectArray("queryList", "com.snail.webgame.game.protocal.levelgift.query.QueryLevelGiftListRe", "queryListSize");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getQueryListSize() {
		return queryListSize;
	}

	public void setQueryListSize(int queryListSize) {
		this.queryListSize = queryListSize;
	}

	public List<QueryLevelGiftListRe> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<QueryLevelGiftListRe> queryList) {
		this.queryList = queryList;
	}

}
