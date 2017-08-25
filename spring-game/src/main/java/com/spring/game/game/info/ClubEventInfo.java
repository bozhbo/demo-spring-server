package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class ClubEventInfo extends BaseTO{
	private int id;
	private int roleId;
	private int clubId;
	private int event; // -1 - 创建公会 0 - 加入 1 - 退出 2 - 踢出   3-解散公会 4 - 降为普通成员 5 - 转让会长  6 - 任命为副会长 7 - 官员 
	//8 - 初级建设 9 - 中级建设 10 - 高级建设 11 - 公会升级 12 - 更换会长
	private Timestamp time; //事件的时间

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public byte getSaveMode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
