package com.snail.webgame.game.protocal.mine.scene;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SceneMineRe extends MessageBody {

	private int mineId;// id
	private int position;// 矿位置编号
	private int mineNo; // 矿类型编号
	private long createTime;// 矿生成时间
	private int zlNum; // 矿占领人数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mineId", 0);
		ps.add("position", 0);
		ps.add("mineNo", 0);
		ps.add("createTime", 0);
		ps.add("zlNum", 0);
	}

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getMineNo() {
		return mineNo;
	}

	public void setMineNo(int mineNo) {
		this.mineNo = mineNo;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getZlNum() {
		return zlNum;
	}

	public void setZlNum(int zlNum) {
		this.zlNum = zlNum;
	}
}
