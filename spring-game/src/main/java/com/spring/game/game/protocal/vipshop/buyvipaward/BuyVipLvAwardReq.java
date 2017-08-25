package com.snail.webgame.game.protocal.vipshop.buyvipaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyVipLvAwardReq extends MessageBody {

	private int no;
	private int num;// 购买数量

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("no", 0);
		ps.add("num", 0);
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
