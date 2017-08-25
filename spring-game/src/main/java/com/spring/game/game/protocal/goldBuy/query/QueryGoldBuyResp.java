package com.snail.webgame.game.protocal.goldBuy.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryGoldBuyResp extends MessageBody {

	private int result;
	private byte buyType;// 购买类型 GoldBuy.xml no
	private byte buyNum;// 购买次数
	private byte maxBuyNum;// 购买上限(-1 无上限)

	private int costGold;// 单次使用消耗金子
	private int gain;// 单次使用获取值(公式计算)

	private int repeatNum;// 连续使用次数
	private int costGoldRepeat;// 连续使用消耗金子
	private int gainRepeat;// 连续使用获取值(公式计算)

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("buyType", 0);
		ps.add("buyNum", 0);
		ps.add("maxBuyNum", 0);

		ps.add("costGold", 0);
		ps.add("gain", 0);

		ps.add("repeatNum", 0);
		ps.add("costGoldRepeat", 0);
		ps.add("gainRepeat", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getBuyType() {
		return buyType;
	}

	public void setBuyType(byte buyType) {
		this.buyType = buyType;
	}

	public byte getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(byte buyNum) {
		this.buyNum = buyNum;
	}

	public byte getMaxBuyNum() {
		return maxBuyNum;
	}

	public void setMaxBuyNum(byte maxBuyNum) {
		this.maxBuyNum = maxBuyNum;
	}

	public int getCostGold() {
		return costGold;
	}

	public void setCostGold(int costGold) {
		this.costGold = costGold;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		this.gain = gain;
	}

	public int getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(int repeatNum) {
		this.repeatNum = repeatNum;
	}

	public int getCostGoldRepeat() {
		return costGoldRepeat;
	}

	public void setCostGoldRepeat(int costGoldRepeat) {
		this.costGoldRepeat = costGoldRepeat;
	}

	public int getGainRepeat() {
		return gainRepeat;
	}

	public void setGainRepeat(int gainRepeat) {
		this.gainRepeat = gainRepeat;
	}
}
