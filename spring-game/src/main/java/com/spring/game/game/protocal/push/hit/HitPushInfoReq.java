package com.snail.webgame.game.protocal.push.hit;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HitPushInfoReq extends MessageBody {
	private int no;
	private byte state;// 1-勾选 2-去勾

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("no", 0);
		ps.add("state", 0);
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

}
