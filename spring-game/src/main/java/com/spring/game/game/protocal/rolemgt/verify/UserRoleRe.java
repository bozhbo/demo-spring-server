package com.snail.webgame.game.protocal.rolemgt.verify;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UserRoleRe extends MessageBody {

	private int roleId;// 角色Id
	private String roleName;// 角色名称
	private int roleRace;// 国家势力
	private int roleLevel;// 角色等级
	private int heroNo;// 英雄编号

	private int roleStatus;// 角色状态 0-正常 1- 角色被冻结（时间限制） 2-角色永久冻结
	private long roleStatusTime; // 异常状态结束时间

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("roleRace", 0);
		ps.add("roleLevel", 0);
		ps.add("heroNo", 0);

		ps.add("roleStatus", 0);
		ps.add("roleStatusTime", 0);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getRoleRace() {
		return roleRace;
	}

	public void setRoleRace(int roleRace) {
		this.roleRace = roleRace;
	}

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(int roleStatus) {
		this.roleStatus = roleStatus;
	}

	public long getRoleStatusTime() {
		return roleStatusTime;
	}

	public void setRoleStatusTime(long roleStatusTime) {
		this.roleStatusTime = roleStatusTime;
	}
}
