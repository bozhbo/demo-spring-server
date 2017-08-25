package com.snail.webgame.game.protocal.fight.mutual.match;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MutualMatchReq extends MessageBody {

	private int matchType; // 匹配类型 1-单人匹配 2-组队匹配

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("matchType", 0);
	}

	public int getMatchType() {
		return matchType;
	}

	public void setMatchType(int matchType) {
		this.matchType = matchType;
	}
	
	
}
