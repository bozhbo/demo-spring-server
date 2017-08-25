package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 副本日志
 * @author zhangyq
 */
public class ChallengeLog extends BaseTO {
	private int roleId;
	private String roleName;
	private String account;
	private String challengeNO;
	private int action;
	private Timestamp time;
	private int star;
	private Timestamp startTime;//副本开始时间


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

	public String getChallengeNO() {
		return challengeNO;
	}

	public void setChallengeNO(String challengeNO) {
		this.challengeNO = challengeNO;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
