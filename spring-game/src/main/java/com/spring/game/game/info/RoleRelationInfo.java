package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

public class RoleRelationInfo extends BaseTO {
	private int roleId; //角色Id
	private int friendId; //好友的角色Id
	private int status; // 0 - 不是好友 但是请求过 未处理 请求 1 - 好友 2 - 黑名单

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
