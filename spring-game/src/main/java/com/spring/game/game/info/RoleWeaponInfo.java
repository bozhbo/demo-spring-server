package com.snail.webgame.game.info;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 角色神器信息
 * 
 * @author xiasd
 */
public class RoleWeaponInfo extends BaseTO {

	private int roleId;
	private int weaponNo;// 神器编号
	private int exp;// 神器经验
	private short level;// 神兵等级
	private byte position;// 0-不在阵型 else 部位 1-武器、2-盔甲、3-头盔、4-护腕、5-披风、6-鞋子

	public RoleWeaponInfo() {

	}

	public RoleWeaponInfo(int roleId, int weaponNo) {
		this.roleId = roleId;
		this.weaponNo = weaponNo;
		this.level = 1;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getWeaponNo() {
		return weaponNo;
	}

	public void setWeaponNo(int weaponNo) {
		this.weaponNo = weaponNo;
	}

	@Override
	public byte getSaveMode() {
		return ONLINE;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}

}
