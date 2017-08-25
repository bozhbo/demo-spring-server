package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class HireHeroInfo extends BaseTO {
	private int roleId;
	private int heroId; // 同HeroInfo的ID（数据库主键ID）
	private Timestamp time; // 佣兵派出时间
	private int hireMoneySum; // 被雇佣产生的收入总和

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getHireMoneySum() {
		return hireMoneySum;
	}

	public void setHireMoneySum(int hireMoneySum) {
		this.hireMoneySum = hireMoneySum;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
