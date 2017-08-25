package com.snail.webgame.game.common;

/**
 * 神兵镜像
 * @author zenggang
 */
public class WeaponRecord {

	private int weaponNo;// 神器编号
	private short level;// 神兵等级
	private byte position;// 部位 1-武器、2-盔甲、3-头盔、4-护腕、5-披风、6-鞋子

	public int getWeaponNo() {
		return weaponNo;
	}

	public void setWeaponNo(int weaponNo) {
		this.weaponNo = weaponNo;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}
}
