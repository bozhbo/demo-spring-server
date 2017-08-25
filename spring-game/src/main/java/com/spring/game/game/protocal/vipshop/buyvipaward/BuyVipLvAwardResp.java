package com.snail.webgame.game.protocal.vipshop.buyvipaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyVipLvAwardResp extends MessageBody {

	private int result;
	private int no;
	private int buyNum;// 已经购买次数

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("no", 0);
		ps.add("buyNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

}
