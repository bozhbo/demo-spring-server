package com.snail.webgame.game.protocal.weapon.query;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 查询神兵信息响应
 * 
 * @author xiasd
 * 
 */
public class QueryRoleWeaponInfoResp extends MessageBody {

	private int result;
	private int count;
	private List<RoleWeaponInfoRe> list;// 神兵信息

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.weapon.query.RoleWeaponInfoRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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
