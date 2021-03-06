package com.snail.webgame.game.protocal.opactivity.wonder.getaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetWonderAwardResp extends MessageBody {

	private int result;
	private int no;// 领取编号

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("no", 0);
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

}
