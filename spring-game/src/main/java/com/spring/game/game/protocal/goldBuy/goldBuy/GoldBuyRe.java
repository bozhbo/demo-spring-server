package com.snail.webgame.game.protocal.goldBuy.goldBuy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GoldBuyRe extends MessageBody {

	private int costGold;// 消耗金子
	private int gain;// 获取值(公式计算)
	private float mul;// 倍数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("costGold", 0);
		ps.add("gain", 0);
		ps.add("mul", 0);
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

	public float getMul() {
		return mul;
	}

	public void setMul(float mul) {
		this.mul = mul;
	}
}
