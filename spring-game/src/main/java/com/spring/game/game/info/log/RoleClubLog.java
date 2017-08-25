package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class RoleClubLog extends BaseTO {
	private int roleId;
	private String account;
	private String roleName;
	private int clubId;
	private int type; // -1 - 创建公会 0 - 加入 1 - 退出 2 - 踢出   3-解散公会 4 - 降为普通成员 5 - 转让会长  6 - 任命为副会长 7 - 官员 
					//8 - 公会建设 9 - 公会升级  10 - 更换会长 11 - 申请入会 12 - 拒绝加入公会 13 - 通过加入 14 - 更新公会信息
	private Timestamp time;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
