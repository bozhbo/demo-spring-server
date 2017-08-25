package com.snail.webgame.game.protocal.fight.competition.history;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FightHistoryResp extends MessageBody {

	private int count;
	private List<FightHistoryRes> list;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fight.competition.history.FightHistoryRes", "count");
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FightHistoryRes> getList() {
		return list;
	}

	public void setList(List<FightHistoryRes> list) {
		this.list = list;
	}

	
}
