package com.snail.webgame.game.common;

public class RoleLoginQueueInfo {
	private String account;// 玩家计费账号
	private GameMessageHead head;

	public RoleLoginQueueInfo(String account, GameMessageHead head) {
		this.account = account;
		this.head = head;
	}

	public String getAccount() {
		return account;
	}

	public GameMessageHead getHead() {
		return head;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RoleLoginQueueInfo){
			RoleLoginQueueInfo info = (RoleLoginQueueInfo)obj;
			return info.getAccount().equals(this.getAccount());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.account.hashCode();
	}
}
