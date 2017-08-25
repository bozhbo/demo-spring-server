package com.snail.webgame.game.protocal.club.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubRoleInfoRe extends MessageBody {
	private int roleId;
	private int heroNo; // 头像（主武将编号）
	private String roleName; // 角色名字
	private int level; // 等级
	private long contribution; // 贡献值(原来)，现取的是累积的公会建设度
	private int flag; // 1 - 会长 2 - 副会在 3 - 官员
	private int loginStatus; //0 - 不在线 1 - 在线
	private long time; //如果成员不在线 则告诉客户端成员下线时间

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.add("heroNo", 0);
		ps.addString("roleName", "flashCode", 0);
		
		ps.add("level", 0);
		ps.add("contribution", 0);
		ps.add("flag", 0);
		ps.add("loginStatus", 0);
		ps.add("time", 0);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getContribution() {
		return contribution;
	}

	public void setContribution(long contribution) {
		this.contribution = contribution;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
