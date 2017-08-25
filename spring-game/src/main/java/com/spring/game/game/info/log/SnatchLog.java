package com.snail.webgame.game.info.log;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

public class SnatchLog extends BaseTO {

	private String account;// 通行证帐号 (大写)
	private String roleName;// 角色名
	private int roleId;// 角色id

	private String defendRoleName;// 对手名字
	private int defendRoleId;// 对手ID 0-npc

	private int stoneNo;// 石头编号
	private byte lootSuccess; // 抢夺状态(1成功，2失败)
	private int lootTimes;// 抢夺次数

	private String getItem;// 对战获得
	private int useEnergy;// 挑战消耗精力

	private Timestamp createTime;// 日志时间（战斗结束时间）
	private String comment;// 备注

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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getDefendRoleName() {
		return defendRoleName;
	}

	public void setDefendRoleName(String defendRoleName) {
		this.defendRoleName = defendRoleName;
	}

	public int getDefendRoleId() {
		return defendRoleId;
	}

	public void setDefendRoleId(int defendRoleId) {
		this.defendRoleId = defendRoleId;
	}

	public int getStoneNo() {
		return stoneNo;
	}

	public void setStoneNo(int stoneNo) {
		this.stoneNo = stoneNo;
	}

	public byte getLootSuccess() {
		return lootSuccess;
	}

	public void setLootSuccess(byte lootSuccess) {
		this.lootSuccess = lootSuccess;
	}

	public int getLootTimes() {
		return lootTimes;
	}

	public void setLootTimes(int lootTimes) {
		this.lootTimes = lootTimes;
	}

	public String getGetItem() {
		return getItem;
	}

	public void setGetItem(String getItem) {
		this.getItem = getItem;
	}

	public int getUseEnergy() {
		return useEnergy;
	}

	public void setUseEnergy(int useEnergy) {
		this.useEnergy = useEnergy;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

}
