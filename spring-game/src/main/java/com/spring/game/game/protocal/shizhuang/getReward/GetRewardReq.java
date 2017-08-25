package com.snail.webgame.game.protocal.shizhuang.getReward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetRewardReq extends MessageBody {
	//领取的件数ID  凑齐3套领取就传3  5套就传5
	private int num;
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("num", 0);
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	
}
