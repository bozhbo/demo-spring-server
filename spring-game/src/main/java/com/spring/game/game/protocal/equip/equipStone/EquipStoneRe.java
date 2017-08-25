package com.snail.webgame.game.protocal.equip.equipStone;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class EquipStoneRe extends MessageBody {

	private byte seat;
	private int stoneNo;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("seat", 0);
		ps.add("stoneNo", 0);
	}

	public byte getSeat() {
		return seat;
	}

	public void setSeat(byte seat) {
		this.seat = seat;
	}

	public int getStoneNo() {
		return stoneNo;
	}

	public void setStoneNo(int stoneNo) {
		this.stoneNo = stoneNo;
	}
	
}
