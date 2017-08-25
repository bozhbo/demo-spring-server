package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 用户行为日志
 * @author zenggang
 */
public class PlayActionLog extends BaseTO {
	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private Timestamp createTime;// 日志时间
	private String actId;// 异动途径ID
	private int type;// 0-开始，1-完成，2-未完成
	private String actValue;// 行为参数

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

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getActValue() {
		return actValue;
	}

	public void setActValue(String actValue) {
		this.actValue = actValue;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
}
