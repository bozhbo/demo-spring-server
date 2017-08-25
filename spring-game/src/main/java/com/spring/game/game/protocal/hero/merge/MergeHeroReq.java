package com.snail.webgame.game.protocal.hero.merge;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MergeHeroReq extends MessageBody {

	private int heroNo;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroNo", 0);
	}

	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}
}
