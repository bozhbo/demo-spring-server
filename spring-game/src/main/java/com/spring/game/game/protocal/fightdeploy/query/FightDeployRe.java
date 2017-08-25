package com.snail.webgame.game.protocal.fightdeploy.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FightDeployRe extends MessageBody {

	private int heroId;// 英雄编号
	private byte deployPos;// 布阵位置

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.add("deployPos", 0);
	}

	public FightDeployRe(int heroId, byte deployPos) {
		this.heroId = heroId;
		this.deployPos = deployPos;
	}

	public int getHeroId() {
		return heroId;
	}

	public FightDeployRe() {
		super();
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public byte getDeployPos() {
		return deployPos;
	}

	public void setDeployPos(byte deployPos) {
		this.deployPos = deployPos;
	}
}
