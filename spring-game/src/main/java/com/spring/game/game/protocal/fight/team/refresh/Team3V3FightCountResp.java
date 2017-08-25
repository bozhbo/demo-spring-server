 package com.snail.webgame.game.protocal.fight.team.refresh;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * 类介绍:3V3战斗次数刷新
 *
 * @author xiasd
 * @2015年10月13日
 */
public class Team3V3FightCountResp extends MessageBody {
	
	private byte team3V3Count;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("team3V3Count", 0);
	}

	public byte getTeam3V3Count() {
		return team3V3Count;
	}

	public void setTeam3V3Count(byte team3v3Count) {
		team3V3Count = team3v3Count;
	}

}
