package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

public class WeaponUpLog {
	private int roleId;// 用户id
	private String account;// 通行证帐号 (大写)
	private String roleName;// 用户名称
	private int weaponId;// 神兵主键
	private int weaponNo;// 神兵编号
	private Timestamp time;// 日志时间
	private String eventId;// 异动途径ID 对应行为Id
	private int upType;// 变动类型 0-神兵升级
	private int before;// 改动前
	private int after;// 改动后
	
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
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public int getUpType() {
		return upType;
	}
	public void setUpType(int upType) {
		this.upType = upType;
	}
	public int getBefore() {
		return before;
	}
	public void setBefore(int before) {
		this.before = before;
	}
	public int getAfter() {
		return after;
	}
	public void setAfter(int after) {
		this.after = after;
	}
	public int getWeaponId() {
		return weaponId;
	}
	public void setWeaponId(int weaponId) {
		this.weaponId = weaponId;
	}
	public int getWeaponNo() {
		return weaponNo;
	}
	public void setWeaponNo(int weaponNo) {
		this.weaponNo = weaponNo;
	}

}
