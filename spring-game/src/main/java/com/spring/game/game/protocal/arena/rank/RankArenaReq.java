package com.snail.webgame.game.protocal.arena.rank;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RankArenaReq extends MessageBody {

	private short arenaPage;//查看排行榜第几页

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("arenaPage", 0);
	}

	public short getArenaPage() {
		return arenaPage;
	}

	public void setArenaPage(short arenaPage) {
		this.arenaPage = arenaPage;
	}
	
}
