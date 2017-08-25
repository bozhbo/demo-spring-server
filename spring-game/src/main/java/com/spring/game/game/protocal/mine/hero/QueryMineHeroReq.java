package com.snail.webgame.game.protocal.mine.hero;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryMineHeroReq extends MessageBody {

	private int mineId;// 矿id
	private int roleId;// 占领人
	private int helpRoleId;// 协助人 0-占领人

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mineId", 0);
		ps.add("roleId", 0);
		ps.add("helpRoleId", 0);
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

	public int getHelpRoleId() {
		return helpRoleId;
	}

	public void setHelpRoleId(int helpRoleId) {
		this.helpRoleId = helpRoleId;
	}
}
