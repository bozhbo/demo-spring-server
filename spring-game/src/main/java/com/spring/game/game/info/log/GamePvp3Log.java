package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

/**
 * 
 * 类介绍:对攻战日志存储对象
 *
 * @author zhoubo
 * @2015年7月16日
 */
public class GamePvp3Log {

	private int roleId;
	private String account;
	private String roleName;
	private byte result;
	private int point;
	private int beforePoint;
	private int afterPoint;
	private Timestamp startTime;
	private Timestamp endTime;
	private int heroNo;//主武将编号

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

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getBeforePoint() {
		return beforePoint;
	}

	public void setBeforePoint(int beforePoint) {
		this.beforePoint = beforePoint;
	}

	public int getAfterPoint() {
		return afterPoint;
	}

	public void setAfterPoint(int afterPoint) {
		this.afterPoint = afterPoint;
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

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

}
