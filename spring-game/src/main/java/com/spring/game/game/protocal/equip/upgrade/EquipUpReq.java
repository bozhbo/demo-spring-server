package com.snail.webgame.game.protocal.equip.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class EquipUpReq extends MessageBody {
	private int heroId; // 英雄编号
	private int equipId; // 装备编号(与数据库主键一致)
	private String resolveEquipIds; // id:id
	private int itemNum;// 装备碎片数量

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.add("equipId", 0);
		ps.addString("resolveEquipIds", "flashCode", 0);
		ps.add("itemNum", 0);
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public String getResolveEquipIds() {
		return resolveEquipIds;
	}

	public void setResolveEquipIds(String resolveEquipIds) {
		this.resolveEquipIds = resolveEquipIds;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

}
