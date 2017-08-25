package com.snail.webgame.game.protocal.opactivity.wonder.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryWonderListRe extends MessageBody {
	private int no; // 编号
	private byte isGet;// 是否可领取 0-不可领取 1-可领取 2-已领取

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("no", 0);
		ps.add("isGet", 0);
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public byte getIsGet() {
		return isGet;
	}

	public void setIsGet(byte isGet) {
		this.isGet = isGet;
	}

}
