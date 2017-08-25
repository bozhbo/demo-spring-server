package com.snail.webgame.game.protocal.stone.inlay;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class InlayStoneReq extends MessageBody 
{
	private int heroId;//在武将身上传武将Id,不在传0
	private int equipId;//装备Id
	private byte flag;// 0-镶嵌 1-摘除
	private int stoneNo;
	private byte seat;//位置

	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("heroId", 0);
		ps.add("equipId", 0);
		ps.add("flag", 0);
		ps.add("stoneNo", 0);
		ps.add("seat", 0);
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

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public int getStoneNo() {
		return stoneNo;
	}

	public void setStoneNo(int stoneNo) {
		this.stoneNo = stoneNo;
	}

	public byte getSeat() {
		return seat;
	}

	public void setSeat(byte seat) {
		this.seat = seat;
	}
	
}
