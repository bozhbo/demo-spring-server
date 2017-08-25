package com.snail.webgame.game.protocal.opactivity.firstcharge.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryFirstChargeReq extends MessageBody {

	private byte actType;// 活动类型 1-首充 2-手机绑定

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("actType", 0);
	}

	public byte getActType() {
		return actType;
	}

	public void setActType(byte actType) {
		this.actType = actType;
	}

}
