package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class WorldBossFightLog extends BaseTO {
	
	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private Timestamp beginTime;//战斗开始时间
	private Timestamp endTime;//战斗结束时间
	private long hurt;//伤害值
	
	
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


	public Timestamp getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}


	public Timestamp getEndTime() {
		return endTime;
	}


	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}


	public long getHurt() {
		return hurt;
	}


	public void setHurt(long hurt) {
		this.hurt = hurt;
	}


	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
