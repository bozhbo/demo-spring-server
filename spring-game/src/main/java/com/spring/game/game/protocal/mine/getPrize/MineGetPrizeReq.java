package com.snail.webgame.game.protocal.mine.getPrize;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MineGetPrizeReq extends MessageBody {

	private int mineId;// 矿id
	private int minePointId; // 矿点编号

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mineId", 0);
		ps.add("minePointId", 0);
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
}
