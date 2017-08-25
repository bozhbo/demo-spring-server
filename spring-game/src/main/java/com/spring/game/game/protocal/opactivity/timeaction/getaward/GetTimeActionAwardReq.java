package com.snail.webgame.game.protocal.opactivity.timeaction.getaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetTimeActionAwardReq extends MessageBody {

	private int actId;// 已开启的活动id
	private byte rewardNo;//
	private int index;// 多选一时选择的物品位置(0开始)

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("actId", 0);
		ps.add("rewardNo", 0);
		ps.add("index", 0);
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public byte getRewardNo() {
		return rewardNo;
	}

	public void setRewardNo(byte rewardNo) {
		this.rewardNo = rewardNo;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
