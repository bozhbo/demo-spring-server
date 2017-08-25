package com.snail.webgame.game.protocal.relation.syn;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SysBlackListReq extends MessageBody {

	private long roleId;
	private String relRoleId;
	private int flag;// 0-加入 1-移除
	private int relation; // 0-黑名单 1-好友

	protected void setSequnce(ProtocolSequence ps) {

		ps.add("roleId", 0);
		ps.addString("relRoleId", "flashCode", 0);
		ps.add("flag", 0);
		ps.add("relation", 0);
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getRelRoleId() {
		return relRoleId;
	}

	public void setRelRoleId(String relRoleId) {
		this.relRoleId = relRoleId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}
}
