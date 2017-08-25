package com.snail.webgame.game.protocal.club.appoint;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubAppointReq extends MessageBody {
	private int clubRoleId; // 任命的公会成员角色ID
	private int flag; // 0 - 降为普通成员 1 - 转让会长  2 - 任命为副会长 3 - 官员 

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("clubRoleId", 0);
		ps.add("flag", 0);
	}

	public int getClubRoleId() {
		return clubRoleId;
	}

	public void setClubRoleId(int clubRoleId) {
		this.clubRoleId = clubRoleId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
