package com.snail.webgame.game.protocal.club.msg;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.club.entity.ClubRoleInfoRe;

public class ClubRoleMemberInfoMsgResp extends MessageBody{
	private int result;
	private int count;
	private List<ClubRoleInfoRe> list = new ArrayList<ClubRoleInfoRe>();
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.club.entity.ClubRoleInfoRe", "count");
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ClubRoleInfoRe> getList() {
		return list;
	}

	public void setList(List<ClubRoleInfoRe> list) {
		this.list = list;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	
}
