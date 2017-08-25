package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class RoleRelationLog extends BaseTO {
	private String account;// 帐号
	private String roleName;// 角色名称
	private int roleId;
	private int relRoleId;
	private int relation; // 0 - 发送请求 1 - 拒绝添加 2 - 同意好友请求  3 - 黑名单 4 — 删除好友 5 - 移除黑名单 6 - 赠送精力 7 - 领取精力
	                      // 8 - 一键赠送精力 9 - 一键领取精力 10 - 一键拒绝添加好友 11 - 一键同意添加好友
	private Timestamp time; // 操作时间

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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRelRoleId() {
		return relRoleId;
	}

	public void setRelRoleId(int relRoleId) {
		this.relRoleId = relRoleId;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
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
