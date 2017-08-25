package com.snail.webgame.game.protocal.weapon.equip;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 装备神兵请求
 * 
 * @author xiasd
 *
 */
public class EquipWeaponReq extends MessageBody {
	private int weaponId;// 神兵主键
	private int equipPosition;// 镶嵌的位置  // 0-卸载  else 部位 1-武器、2-盔甲、3-头盔、4-护腕、5-披风、6-鞋子

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("weaponId", 0);
		ps.add("equipPosition", 0);
	}

	public int getWeaponId() {
		return weaponId;
	}

	public void setWeaponId(int weaponId) {
		this.weaponId = weaponId;
	}

	public int getEquipPosition() {
		return equipPosition;
	}

	public void setEquipPosition(int equipPosition) {
		this.equipPosition = equipPosition;
	}

}
