package com.snail.webgame.game.protocal.fight.competition.submit;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * 类介绍:提交竞技场战斗申请Response
 *
 * @author zhoubo
 * @2014-11-25
 */
public class SubmitFightResp extends MessageBody {

	private int result;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
}
