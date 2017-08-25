package com.snail.webgame.game.protocal.fightdeploy.change;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DeployChangeReq extends MessageBody {
	
	private byte changeType;//1-上阵  0-下阵
	private byte position;//选择的位置
	private int heroId;//选择的英雄ID

	@Override
	protected void setSequnce(ProtocolSequence ps) {

		ps.add("changeType", 0);
		ps.add("position", 0);
		ps.add("heroId", 0);
		
	}

	public byte getChangeType() {
		return changeType;
	}

	public void setChangeType(byte changeType) {
		this.changeType = changeType;
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
