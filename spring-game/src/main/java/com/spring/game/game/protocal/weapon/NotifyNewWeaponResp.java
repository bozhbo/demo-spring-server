package com.snail.webgame.game.protocal.weapon;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.weapon.query.RoleWeaponInfoRe;

/**
 * 获得新神兵刷新
 * 
 * @author xiasd
 *
 */
public class NotifyNewWeaponResp extends MessageBody{

	private int count;
	private List<RoleWeaponInfoRe> list;// 神兵信息

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.weapon.query.RoleWeaponInfoRe", "count");
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<RoleWeaponInfoRe> getList() {
		return list;
	}

	public void setList(List<RoleWeaponInfoRe> list) {
		this.list = list;
	}
	
	
}
