package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class GoldBuyLog extends BaseTO {

	private int roleId;// 用户id
	private String account;// 通行证帐号 (大写)
	private String roleName;// 用户名称
	private Timestamp time;// 日志时间
	private String eventId;// 异动途径ID 对应行为Id
	private int moneyType;// 币种类型 ConditionType
	private int before;// 改动前
	private int after;// 改动后
	private int cost;// 消耗金子
	private int beforeNum;// 改动前购买次数
	private int afterNum;// 改动后购买次数

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

	public int getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
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

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getBeforeNum() {
		return beforeNum;
	}

	public void setBeforeNum(int beforeNum) {
		this.beforeNum = beforeNum;
	}

	public int getAfterNum() {
		return afterNum;
	}

	public void setAfterNum(int afterNum) {
		this.afterNum = afterNum;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
