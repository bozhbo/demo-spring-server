package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 对攻战日志记录
 * @author nijy
 *
 */
public class GamePVELog extends BaseTO {

	private long roleId;// 用户id
	private String account;// 通行证帐号 (大写)
	private String roleName;// 用户名称
	private int fightResult;//战斗结果  1-胜 2-败
	private long defenseId;// 防守方ID
	private String defenseName;// 防守方名字
	private String reward;// 获得奖励
	private Timestamp startTime;//开始时间
	private Timestamp endTime;// 结束时间


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


	public long getDefenseId() {
		return defenseId;
	}


	public void setDefenseId(long defenseId) {
		this.defenseId = defenseId;
	}


	public String getDefenseName() {
		return defenseName;
	}


	public void setDefenseName(String defenseName) {
		this.defenseName = defenseName;
	}


	public String getReward() {
		return reward;
	}


	public void setReward(String reward) {
		this.reward = reward;
	}




	public Timestamp getStartTime() {
		return startTime;
	}


	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}


	public Timestamp getEndTime() {
		return endTime;
	}


	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}


	public int getFightResult() {
		return fightResult;
	}


	public void setFightResult(int fightResult) {
		this.fightResult = fightResult;
	}


	@Override
	public byte getSaveMode() {
		return 0;
	}
}
