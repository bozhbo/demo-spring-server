package com.snail.webgame.game.protocal.opactivity.wonder.getaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetWonderAwardReq extends MessageBody {

	private int no;// 领取编号

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("no", 0);
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

}
