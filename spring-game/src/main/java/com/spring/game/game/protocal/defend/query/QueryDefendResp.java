package com.snail.webgame.game.protocal.defend.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * @author wanglinhui
 *
 */
public class QueryDefendResp extends MessageBody {
	
	private int result;
	private byte remainTime;//剩余次数

	@Override
	protected void setSequnce(ProtocolSequence ps) {

		ps.add("result", 0);
		ps.add("remainTime", 0);
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public byte getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(byte remainTime) {
		this.remainTime = remainTime;
	}

}
