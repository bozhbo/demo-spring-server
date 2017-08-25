package com.snail.webgame.game.protocal.fightdeploy.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryFightDeployReq extends MessageBody {

	private byte deployType;// 1-普通布阵 -2-竞技场防守阵营

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("deployType", 0);
	}

	public byte getDeployType() {
		return deployType;
	}

	public void setDeployType(byte deployType) {
		this.deployType = deployType;
	}
}
