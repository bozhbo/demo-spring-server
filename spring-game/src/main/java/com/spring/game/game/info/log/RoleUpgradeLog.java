package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 记录角色升级的日志
 * @author zenggang
 */
public class RoleUpgradeLog extends BaseTO {

	private String serial;// 序列号
	private Timestamp createTime;// 日志时间
	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private int logType;// 升级类型1 等级 2 品质 3技能
	private int before;// 异动前金钱存量
	private int after;// 异动后金钱存量
	private int skillId;// 技能ID

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

	public int getLogType() {
		return logType;
	}

	public void setLogType(int logType) {
		this.logType = logType;
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

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
