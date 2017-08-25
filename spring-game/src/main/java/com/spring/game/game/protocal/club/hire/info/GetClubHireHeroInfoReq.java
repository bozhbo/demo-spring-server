package com.snail.webgame.game.protocal.club.hire.info;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetClubHireHeroInfoReq extends MessageBody{
	private int flag; //fightType 0-查询工会所有的

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	

}
