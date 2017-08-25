package com.snail.webgame.game.protocal.mine.loot;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.mine.query.MineInfoRe;

public class LootMineResp extends MessageBody {

	private int result;

	private int count;
	private List<MineInfoRe> list = new ArrayList<MineInfoRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.mine.query.MineInfoRe", "count");
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

	public List<MineInfoRe> getList() {
		return list;
	}

	public void setList(List<MineInfoRe> list) {
		this.list = list;
	}
}
