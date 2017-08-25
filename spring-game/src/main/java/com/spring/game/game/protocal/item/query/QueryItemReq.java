package com.snail.webgame.game.protocal.item.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryItemReq extends MessageBody {

	private byte type1; //查询类型

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("type1", 0);
	}

	public QueryItemReq(){}
	
	public QueryItemReq(byte type)
	{
		this.type1 = type;
	}

	public byte getType() {
		return type1;
	}

	public void setType(byte type) {
		this.type1 = type;
	}
}