package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 战斗布阵信息
 * @author zenggang
 */
public class FightDeployInfo extends BaseTO {

	public static final int CREATE_DEPLOY_FIRST_POS = 1;// 创建角色第一个布阵位置

	private int roleId;// 用户编号
	private int heroId;// 英雄编号(英雄的用户编号不同 为帮助英雄)

	private byte playAction;// 玩法
	private byte deployPos;// 第几个布阵位置(1,2,3,4,5)

	public FightDeployInfo() {

	}

	public FightDeployInfo(int roleId, int heroId, byte playAction, byte deployPos) {
		this.roleId = roleId;
		this.heroId = heroId;
		this.playAction = playAction;
		this.deployPos = deployPos;
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

	public byte getPlayAction() {
		return playAction;
	}

	public void setPlayAction(byte playAction) {
		this.playAction = playAction;
	}

	public byte getDeployPos() {
		return deployPos;
	}

	public void setDeployPos(byte deployPos) {
		this.deployPos = deployPos;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}
}
