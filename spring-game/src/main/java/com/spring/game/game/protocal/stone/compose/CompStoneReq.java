package com.snail.webgame.game.protocal.stone.compose;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CompStoneReq extends MessageBody {
	
	private int stoneNo1;//初始宝石
	private int stoneNo2;//合成宝石
	private short compCount;//合成数量

	@Override
	protected void setSequnce(ProtocolSequence ps) 
	{
		ps.add("stoneNo1", 0);
		ps.add("stoneNo2", 0);
		ps.add("compCount", 0);
	}

	public int getStoneNo2() {
		return stoneNo2;
	}

	public void setStoneNo2(int stoneNo2) {
		this.stoneNo2 = stoneNo2;
	}

	public short getCompCount() {
		return compCount;
	}

	public void setCompCount(short compCount) {
		this.compCount = compCount;
	}

	public int getStoneNo1() {
		return stoneNo1;
	}

	public void setStoneNo1(int stoneNo1) {
		this.stoneNo1 = stoneNo1;
	}
	
}
