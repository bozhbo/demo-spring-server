package com.snail.webgame.game.protocal.club.syn;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SysKingdomChatReq extends MessageBody {

	private long roleId;// 角色id
	private long kingdomId;// 公会id
	private int flag;// 进出公会标示 // 0-加入公会 1-退出公会

	protected void setSequnce(ProtocolSequence ps) {

		ps.add("roleId", 0);
		ps.add("kingdomId", 0);
		ps.add("flag", 0);
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getKingdomId() {
		return kingdomId;
	}

	public void setKingdomId(long kingdomId) {
		this.kingdomId = kingdomId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
