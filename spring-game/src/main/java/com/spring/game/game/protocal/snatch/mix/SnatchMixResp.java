package com.snail.webgame.game.protocal.snatch.mix;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SnatchMixResp extends MessageBody {
	private int result;
	private int mixNum;// 合成获得数量

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("mixNum", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getMixNum() {
		return mixNum;
	}

	public void setMixNum(int mixNum) {
		this.mixNum = mixNum;
	}
}
