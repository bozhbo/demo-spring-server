package com.snail.webgame.game.protocal.fight.fightTime;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FightTimeResp extends MessageBody {

	private int result;
	private int fightTime;//倒计时

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("fightTime", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFightTime() {
		return fightTime;
	}

	public void setFightTime(int fightTime) {
		this.fightTime = fightTime;
	}
	
}
