package com.snail.webgame.game.protocal.stone.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryStoneInfo extends MessageBody
{
	private int stoneNo;
	private byte position;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) 
	{
		ps.add("stoneNo", 0);
		ps.add("position", 0);
	}
	
	public int getStoneNo() {
		return stoneNo;
	}
	public void setStoneNo(int stoneNo) {
		this.stoneNo = stoneNo;
	}
	public byte getPosition() {
		return position;
	}
	public void setPosition(byte position) {
		this.position = position;
	}
	
}
