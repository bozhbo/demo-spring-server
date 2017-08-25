package com.snail.webgame.game.protocal.mine.getPrize;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class MineGetPrizeResp extends MessageBody {

	private int result;

	private int mineId;// 矿id
	private int minePointId; // 矿点编号

	private int count;
	private List<BattlePrize> list = new ArrayList<BattlePrize>(); // 奖励道具

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);

		ps.add("mineId", 0);
		ps.add("minePointId", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fight.fightend.BattlePrize", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getMinePointId() {
		return minePointId;
	}

	public void setMinePointId(int minePointId) {
		this.minePointId = minePointId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<BattlePrize> getList() {
		return list;
	}

	public void setList(List<BattlePrize> list) {
		this.list = list;
	}

}
