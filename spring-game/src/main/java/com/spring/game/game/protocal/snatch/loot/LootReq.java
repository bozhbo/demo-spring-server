package com.snail.webgame.game.protocal.snatch.loot;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class LootReq extends MessageBody {
	private String lootRoleName;//抢夺用户名称
	private byte isNPC;//0-不是  1-是
	private int stoneNo;//石头编号
	private int lootTimes;//抢夺次数
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("lootRoleName", "flashCode", 0);
		ps.add("isNPC", 0);
		ps.add("stoneNo", 0);
		ps.add("lootTimes", 0);
	}
	public String getLootRoleName() {
		return lootRoleName;
	}
	public void setLootRoleName(String lootRoleName) {
		this.lootRoleName = lootRoleName;
	}
	public int getStoneNo() {
		return stoneNo;
	}
	public void setStoneNo(int stoneNo) {
		this.stoneNo = stoneNo;
	}
	public int getLootTimes() {
		return lootTimes;
	}
	public void setLootTimes(int lootTimes) {
		this.lootTimes = lootTimes;
	}
	public byte getIsNPC() {
		return isNPC;
	}
	public void setIsNPC(byte isNPC) {
		this.isNPC = isNPC;
	}
}
