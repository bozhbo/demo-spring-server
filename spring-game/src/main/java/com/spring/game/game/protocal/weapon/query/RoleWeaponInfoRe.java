package com.snail.webgame.game.protocal.weapon.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 神兵响应
 * 
 * @author xiasd
 *
 */
public class RoleWeaponInfoRe extends MessageBody {

	private int weaponId;// 神器seqId
	private int exp;// 神兵经验
	private short level;// 神兵等级
	private byte position;// 0-不在阵型 else 部位 1-武器、2-盔甲、3-头盔、4-护腕、5-披风、6-鞋子
	private int weaponNo;// 神兵编号

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("weaponId", 0);
		ps.add("exp", 0);
		ps.add("level", 0);
		ps.add("position", 0);
		ps.add("weaponNo", 0);
	}

	public int getWeaponId() {
		return weaponId;
	}

	public void setWeaponId(int weaponId) {
		this.weaponId = weaponId;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

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
