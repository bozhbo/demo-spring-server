package com.snail.webgame.game.protocal.arena.rank;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.arena.query.ArenaRe;

public class RankArenaResp extends MessageBody {

	private int result;
	private int count;
	private List<ArenaRe> list = new ArrayList<ArenaRe>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.arena.query.ArenaRe", "count");
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

	public List<ArenaRe> getList() {
		return list;
	}

	public void setList(List<ArenaRe> list) {
		this.list = list;
	}
}
