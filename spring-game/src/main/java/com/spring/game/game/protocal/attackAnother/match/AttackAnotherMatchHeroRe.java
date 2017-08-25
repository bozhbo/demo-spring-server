package com.snail.webgame.game.protocal.attackAnother.match;

import com.snail.webgame.engine.component.charge.connect.processor.protocol.MessageBody;
import com.snail.webgame.engine.component.charge.connect.processor.protocol.ProtocolSequence;

public class AttackAnotherMatchHeroRe extends MessageBody {

	private int heroNo;
	private int heroLevel;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {

		ps.add("heroNo", 0);
		ps.add("heroLevel", 0);

	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

}
