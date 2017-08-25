package com.snail.webgame.game.protocal.mine.buy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyMineNumResp extends MessageBody {

	private int result;
	private int mineNum;// 当日矿抢夺次数
	private int mineLimit;// 当日矿抢夺次数上限
	private int buyMine;// 当日抢夺购买次数

	private byte sourceType;
	private int sourceChange;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("mineNum", 0);
		ps.add("mineLimit", 0);
		ps.add("buyMine", 0);

		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getMineNum() {
		return mineNum;
	}

	public void setMineNum(int mineNum) {
		this.mineNum = mineNum;
	}

	public int getMineLimit() {
		return mineLimit;
	}

	public void setMineLimit(int mineLimit) {
		this.mineLimit = mineLimit;
	}

	public int getBuyMine() {
		return buyMine;
	}

	public void setBuyMine(int buyMine) {
		this.buyMine = buyMine;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}
}
