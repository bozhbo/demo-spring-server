package com.snail.webgame.game.protocal.mine.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MineRoleRe extends MessageBody {

	private int minePointId; // 矿点编号
	private int roleId; // 占领人
	private String roleName; // 占领人
	private int heroLevel;// 等级
	private int heroNo;// xml编号
	private int fightValue;// 战斗力

	private long createTime;// 矿占领时间
	private long endTime;// 矿结束采集时间

	// 协防人
	private int count;
	private List<MineHelpRoleRe> helpRoles = new ArrayList<MineHelpRoleRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("minePointId", 0);
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("heroLevel", 0);
		ps.add("heroNo", 0);
		ps.add("fightValue", 0);

		ps.add("createTime", 0);
		ps.add("endTime", 0);

		ps.add("count", 0);
		ps.addObjectArray("helpRoles", "com.snail.webgame.game.protocal.mine.query.MineHelpRoleRe", "count");
	}

	public int getMinePointId() {
		return minePointId;
	}

	public void setMinePointId(int minePointId) {
		this.minePointId = minePointId;
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

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<MineHelpRoleRe> getHelpRoles() {
		return helpRoles;
	}

	public void setHelpRoles(List<MineHelpRoleRe> helpRoles) {
		this.helpRoles = helpRoles;
	}
}
