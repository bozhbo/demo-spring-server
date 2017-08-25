package com.snail.webgame.game.protocal.quest.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryQuestReq extends MessageBody {

	private String noStr;

	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("noStr", "flashCode", 0);
	}

	public String getNoStr() {
		return noStr;
	}

	public void setNoStr(String noStr) {
		this.noStr = noStr;
	}
}
