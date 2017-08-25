package com.snail.webgame.game.protocal.club.hire.operation;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HireHeroOperationReq extends MessageBody {
	private int flag; // 0 - 召回 ，1 - 雇佣  2 - 派出
	private int heroId;
	private int roleId; // 被雇佣英雄的角色ID
	private String heroIds; //heroId:heroId 派出多个佣兵

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);
		ps.add("heroId", 0);
		ps.add("roleId", 0);
		ps.addString("heroIds", "flashCode", 0);
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getHeroIds() {
		return heroIds;
	}

	public void setHeroIds(String heroIds) {
		this.heroIds = heroIds;
	}

}
