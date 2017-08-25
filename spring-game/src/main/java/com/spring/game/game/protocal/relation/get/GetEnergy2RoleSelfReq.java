package com.snail.webgame.game.protocal.relation.get;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetEnergy2RoleSelfReq extends MessageBody {
	private int id; //精力赠送数据库的主键Id

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("id", 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
