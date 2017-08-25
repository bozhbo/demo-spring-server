package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 宝物活动战死残血和上阵的英雄信息
 * @author zenggang
 */
public class FightCampaignHero extends BaseTO {

	public static final byte HERO_STATUS_0 = 0;
	public static final byte HERO_STATUS_1 = 1;

	private int roleId;// 用户编号
	private int heroId;// 武将编号id
	private byte deployPos;// 布阵位置 0-普通 1-5上阵
	private byte heroStatus;// 0-战死 1-残血
	private int cutHp;// 减少的血量

	public byte getDeployPos() {
		return deployPos;
	}

	public void setDeployPos(byte deployPos) {
		this.deployPos = deployPos;
	}

	public byte getHeroStatus() {
		return heroStatus;
	}

	public void setHeroStatus(byte heroStatus) {
		this.heroStatus = heroStatus;
	}

	public int getCutHp() {
		return cutHp;
	}

	public void setCutHp(int cutHp) {
		this.cutHp = cutHp;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

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

}
