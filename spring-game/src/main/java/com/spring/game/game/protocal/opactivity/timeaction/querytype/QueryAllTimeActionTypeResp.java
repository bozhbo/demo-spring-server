package com.snail.webgame.game.protocal.opactivity.timeaction.querytype;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryAllTimeActionTypeResp extends MessageBody {

	private int result;
	private int count;
	private List<TimeActionTypeRe> queryTypes;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("queryTypes", "com.snail.webgame.game.protocal.opactivity.timeaction.querytype.TimeActionTypeRe", "count");
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

	public List<TimeActionTypeRe> getQueryTypes() {
		return queryTypes;
	}

	public void setQueryTypes(List<TimeActionTypeRe> queryTypes) {
		this.queryTypes = queryTypes;
	}

}
