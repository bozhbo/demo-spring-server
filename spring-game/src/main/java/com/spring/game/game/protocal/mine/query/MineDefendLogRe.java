package com.snail.webgame.game.protocal.mine.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MineDefendLogRe extends MessageBody {

	private int position;// 矿位置编号（唯一）
	private int mineNo; // 矿类型编号
	private int roleId; // 占领人
	private String roleName; // 占领人
	private int roleHeroNo;// xml编号
	private int roleLevel;// 等级
	private int helpRoleId;// helpRoleId 是自己 为协防 0-自己防守

	private int attackRoleId; // 攻击人
	private String attackRoleName; // 攻击人
	private int attackRoleHeroNo;// xml编号
	private int attackRoleLevel;// 等级

	private int getNum;// 抢夺到的收益
	private byte fightResult;// 1-胜 2-败 3-放弃
	private long time; // 时间

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("position", 0);
		ps.add("mineNo", 0);
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("roleHeroNo", 0);
		ps.add("roleLevel", 0);
		ps.add("helpRoleId", 0);

		ps.add("attackRoleId", 0);
		ps.addString("attackRoleName", "flashCode", 0);
		ps.add("attackRoleHeroNo", 0);
		ps.add("attackRoleLevel", 0);

		ps.add("getNum", 0);
		ps.add("fightResult", 0);
		ps.add("time", 0);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getMineNo() {
		return mineNo;
	}

	public void setMineNo(int mineNo) {
		this.mineNo = mineNo;
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

	public int getRoleHeroNo() {
		return roleHeroNo;
	}

	public void setRoleHeroNo(int roleHeroNo) {
		this.roleHeroNo = roleHeroNo;
	}

	public int getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(int roleLevel) {
		this.roleLevel = roleLevel;
	}

	public int getHelpRoleId() {
		return helpRoleId;
	}

	public void setHelpRoleId(int helpRoleId) {
		this.helpRoleId = helpRoleId;
	}

	public int getAttackRoleId() {
		return attackRoleId;
	}

	public void setAttackRoleId(int attackRoleId) {
		this.attackRoleId = attackRoleId;
	}

	public String getAttackRoleName() {
		return attackRoleName;
	}

	public void setAttackRoleName(String attackRoleName) {
		this.attackRoleName = attackRoleName;
	}

	public int getAttackRoleHeroNo() {
		return attackRoleHeroNo;
	}

	public void setAttackRoleHeroNo(int attackRoleHeroNo) {
		this.attackRoleHeroNo = attackRoleHeroNo;
	}

	public int getAttackRoleLevel() {
		return attackRoleLevel;
	}

	public void setAttackRoleLevel(int attackRoleLevel) {
		this.attackRoleLevel = attackRoleLevel;
	}

	public int getGetNum() {
		return getNum;
	}

	public void setGetNum(int getNum) {
		this.getNum = getNum;
	}

	public byte getFightResult() {
		return fightResult;
	}

	public void setFightResult(byte fightResult) {
		this.fightResult = fightResult;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
