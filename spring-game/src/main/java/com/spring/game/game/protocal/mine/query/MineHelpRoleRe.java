package com.snail.webgame.game.protocal.mine.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MineHelpRoleRe extends MessageBody {

	private byte helpPos;// 协防位置
	private int roleId; // 占领人
	private String roleName; // 占领人
	private int heroLevel;// 等级
	private int heroNo;// xml编号
	private int fightValue;// 战斗力

	private long createTime;// 矿协防时间

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("helpPos", 0);
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);
		ps.add("heroLevel", 0);
		ps.add("heroNo", 0);
		ps.add("fightValue", 0);

		ps.add("createTime", 0);
	}

	public byte getHelpPos() {
		return helpPos;
	}

	public void setHelpPos(byte helpPos) {
		this.helpPos = helpPos;
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
}
