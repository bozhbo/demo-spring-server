package com.snail.webgame.game.protocal.vipshop.queryvipbuy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryVipBuyRe extends MessageBody {

	private int no;
	private int buyNum;// 已经购买次数
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("no", 0);
		ps.add("buyNum", 0);
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
