package com.snail.webgame.game.protocal.hero.upgrade;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class HeroUpgradeReq extends MessageBody {

	private int heroId;
	private int num;// 升几级默认1

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.add("num", 0);
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
