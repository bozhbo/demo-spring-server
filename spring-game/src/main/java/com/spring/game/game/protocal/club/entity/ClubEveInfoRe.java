package com.snail.webgame.game.protocal.club.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubEveInfoRe extends MessageBody {
	private int id;//数据库主键ID
	private int roleId;
	private String roleName;
	private int level;
	private int heroNo; // 头像（主武将编号）
	private int eve; // 0 - 加入 1 - 退出 2 - 踢出   3-解散公会 4 - 降为普通成员 5 - 转让会长  6 - 任命为副会长 7 - 官员 
	private long time; // 事件时间
	private int flag; // 0 - 普通成员 1 - 会长 2 - 副会在 3 - 官员

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("id", 0);
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("level", 0);
		ps.add("heroNo", 0);
		ps.add("eve", 0);
		ps.add("time", 0);
		ps.add("flag", 0);
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getEve() {
		return eve;
	}

	public void setEve(int eve) {
		this.eve = eve;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
