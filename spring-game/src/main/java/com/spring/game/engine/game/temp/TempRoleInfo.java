package com.snail.webgame.engine.game.temp;

import com.snail.webgame.engine.game.base.actor.ActorInfo;

public class TempRoleInfo implements ActorInfo {

	private int roleId;
	private String roleName;
	
	private int gold;
	private int point;
	
	private int roleActivity;
	
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
	public int getRoleActivity() {
		return roleActivity;
	}
	public void setRoleActivity(int roleActivity) {
		this.roleActivity = roleActivity;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	@Override
	public Integer getId() {
		return roleId;
	}
	
	
}
