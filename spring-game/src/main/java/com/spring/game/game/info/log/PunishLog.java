package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 角色被惩罚相关的日志
 * @author zenggang
 */
public class PunishLog extends BaseTO {

	private String serial;// 序列号
	private Timestamp createTime;// 日志时间
	private String operator;// 后台操作人通行证
	private String account;// 通行证帐号 (大写)
	private String roleName;// 用户名称
	private int minutes; // 被禁言或冻结的分钟数
	private int punishType; // 惩罚类别0-解禁 1-禁言 2-冻结

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getPunishType() {
		return punishType;
	}

	public void setPunishType(int punishType) {
		this.punishType = punishType;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
