package com.snail.webgame.game.protocal.fight.competition.rank;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryRankResp extends MessageBody {

	private int count;
	private List<QueryRankRes> list = null;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fight.competition.rank.QueryRankRes", "count");
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<QueryRankRes> getList() {
		return list;
	}

	public void setList(List<QueryRankRes> list) {
		this.list = list;
	}
	
	
}
