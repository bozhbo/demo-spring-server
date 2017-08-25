package com.snail.webgame.game.protocal.fight.competition.rank;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryRankReq extends MessageBody {
	private int startIndex; // 查询页数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("startIndex", 0);
		
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	
}
