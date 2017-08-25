package com.snail.webgame.game.protocal.fight.mutual.join;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualRefershResp;

public class MutualJoinTeamResp extends MessageBody {

	private int result;
	private int count;
	private List<MutualRefershResp> list;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fight.mutual.refresh.MutualRefershResp", "count");
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

	public List<MutualRefershResp> getList() {
		return list;
	}

	public void setList(List<MutualRefershResp> list) {
		this.list = list;
	}

}
