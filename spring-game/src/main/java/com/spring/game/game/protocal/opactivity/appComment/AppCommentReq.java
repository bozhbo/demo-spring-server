package com.snail.webgame.game.protocal.opactivity.appComment;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class AppCommentReq extends MessageBody {

	private byte flag; //1-评论，2-再等一下，3-不再提示

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}
	
}
