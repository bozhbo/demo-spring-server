package com.snail.webgame.game.protocal.relation.entity;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FriendDetailRe extends MessageBody {
	private int roleId; // 角色Id
	private String roleName; // 角色名称
	private int level; // 角色等级
	private int fightValue; // 角色战斗力
	private int status; // 角色状态。0:下线 1:在线
	private long applyTime; // 馈赠的精力时常，单位：毫秒
	private int heroNo;// xml编号
	private int id; // 好友赠送精炼的数据库主键Id 用于定位好友赠送精力
	private int canGive; //0 - 可以赠送  1 - 不可
	

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("level", 0);
		ps.add("fightValue", 0);
		ps.add("status", 0);
		ps.add("applyTime", 0);
		ps.add("heroNo", 0);
		ps.add("id", 0);
		ps.add("canGive", 0);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(long applyTime) {
		this.applyTime = applyTime;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCanGive() {
		return canGive;
	}

	public void setCanGive(int canGive) {
		this.canGive = canGive;
	}

}
