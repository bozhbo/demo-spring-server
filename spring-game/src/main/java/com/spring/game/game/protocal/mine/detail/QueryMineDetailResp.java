package com.snail.webgame.game.protocal.mine.detail;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.mine.query.MineRoleRe;

public class QueryMineDetailResp extends MessageBody {

	private int result;

	private int count;
	private List<MineRoleRe> list = new ArrayList<MineRoleRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.mine.detail.MineRoleRe", "count");
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

	public List<MineRoleRe> getList() {
		return list;
	}

	public void setList(List<MineRoleRe> list) {
		this.list = list;
	}
}
