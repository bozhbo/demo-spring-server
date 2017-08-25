package com.snail.webgame.game.protocal.gem.buy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyGemResp extends MessageBody {

	private int result;
	private int resetLimit;// 当日可重置上限
	private int buyNum;// 当日购买次数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("resetLimit", 0);
		ps.add("buyNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getResetLimit() {
		return resetLimit;
	}

	public void setResetLimit(int resetLimit) {
		this.resetLimit = resetLimit;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
}
