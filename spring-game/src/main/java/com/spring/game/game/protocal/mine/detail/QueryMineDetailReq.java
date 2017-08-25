package com.snail.webgame.game.protocal.mine.detail;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryMineDetailReq extends MessageBody {

	private int mineId;// 矿id

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mineId", 0);
	}

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}
}
