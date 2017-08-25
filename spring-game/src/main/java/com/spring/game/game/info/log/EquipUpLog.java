package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class EquipUpLog extends BaseTO {
	
	private long roleId;// 用户id
	private String account;// 通行证帐号 (大写)
	private String roleName;// 用户名称
	private long heroId;// 英雄Id
	private long itemId;// 装备Id
	private int equipNo;// 装备no
	private Timestamp time;// 日志时间
	private String eventId;// 异动途径ID 对应 GameAction行为Id
	private int before;// 改动前
	private int after;// 改动后


	@Override
	public byte getSaveMode() {
		// TODO Auto-generated method stub
		return 0;
	}



	public long getRoleId() {
		return roleId;
	}


	public void setRoleId(long roleId) {
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


	public long getHeroId() {
		return heroId;
	}


	public void setHeroId(long heroId) {
		this.heroId = heroId;
	}


	public long getItemId() {
		return itemId;
	}


	public void setItemId(long itemId) {
		this.itemId = itemId;
	}


	public int getEquipNo() {
		return equipNo;
	}


	public void setEquipNo(int equipNo) {
		this.equipNo = equipNo;
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

}
