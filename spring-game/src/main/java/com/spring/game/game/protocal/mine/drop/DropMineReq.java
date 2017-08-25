package com.snail.webgame.game.protocal.mine.drop;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DropMineReq extends MessageBody {

	private int mineId;// 矿id
	private int roleId;// 占领人(不是自己是放弃协助)

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mineId", 0);
		ps.add("roleId", 0);
	}

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
