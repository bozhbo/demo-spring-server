package com.snail.webgame.game.protocal.hero.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryHeroReq extends MessageBody {

	private String idStr;

	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("idStr", "flashCode", 0);
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
}