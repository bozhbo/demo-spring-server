package com.snail.webgame.game.protocal.guide.update;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 更新引导信息
 * @author wanglinhui
 *
 */
public class UpdateStepReq extends MessageBody {

	private String rate; // 进度

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("rate", "flashCode", 0);
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}


}
