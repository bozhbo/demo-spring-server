package com.snail.webgame.game.protocal.hero.starUp;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HeroStarUpReq extends MessageBody {

	private int heroId;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}
}

