package com.snail.webgame.game.protocal.relation.onekey.op;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class OneKeyOperationReq extends MessageBody {
	private int flag; // 0 - 一键添加 1 - 一键拒绝

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
