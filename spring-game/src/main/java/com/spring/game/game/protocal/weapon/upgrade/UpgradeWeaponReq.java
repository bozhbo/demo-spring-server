package com.snail.webgame.game.protocal.weapon.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 升级神兵请求
 * 
 * @author xiasd
 *
 */
public class UpgradeWeaponReq extends MessageBody {

	private int weaponId;
	private String weaponIds;// 材料神兵

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("weaponId", 0);
		ps.addString("weaponIds", "flashCode", 0);
	}

	public int getWeaponId() {
		return weaponId;
	}

	public void setWeaponId(int weaponId) {
		this.weaponId = weaponId;
	}

	public String getWeaponIds() {
		return weaponIds;
	}

	public void setWeaponIds(String weaponIds) {
		this.weaponIds = weaponIds;
	}

}
