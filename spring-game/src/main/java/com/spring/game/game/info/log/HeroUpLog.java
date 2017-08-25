package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 英雄等级星级 变动日志
 * @author zenggang
 *
 */
public class HeroUpLog extends BaseTO {

	private int roleId;// 用户id
	private String account;// 通行证帐号 (大写)
	private String roleName;// 用户名称
	private int heroNo;// 英雄Id
	private Timestamp time;// 日志时间
	private String eventId;// 异动途径ID 对应行为Id
	private int upType;// 变动类型0-获得 1-等级 2-星级 3-品阶 4-亲密度
	private int before;// 改动前
	private int after;// 改动后

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

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
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

	public int getUpType() {
		return upType;
	}

	public void setUpType(int upType) {
		this.upType = upType;
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

	@Override
	public byte getSaveMode() {
		return 0;
	}
}
