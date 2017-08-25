package com.snail.webgame.game.protocal.mine.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MineHeroRe extends MessageBody {

	private byte position;
	private int heroId;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("position", 0);
		ps.add("heroId", 0);
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}
}
