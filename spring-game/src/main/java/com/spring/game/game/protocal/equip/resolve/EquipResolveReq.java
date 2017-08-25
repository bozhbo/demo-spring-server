package com.snail.webgame.game.protocal.equip.resolve;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class EquipResolveReq extends MessageBody {
	private String equipIds; // 装备ID 最多八个 id:id 与服务端装备表数据库主键一致 type
	private int resolveType; // 0 - 普通 1 - 重铸 2 - 分解星石
	private String star; //id:num;id:num resolveType 为 2 分解星石时候使用

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("equipIds", "flashCode", 0);
		ps.add("resolveType", 0);
		ps.addString("star", "flashCode", 0);
	}

	public String getEquipIds() {
		return equipIds;
	}

	public void setEquipIds(String equipIds) {
		this.equipIds = equipIds;
	}

	public int getResolveType() {
		return resolveType;
	}

	public void setResolveType(int resolveType) {
		this.resolveType = resolveType;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

}
