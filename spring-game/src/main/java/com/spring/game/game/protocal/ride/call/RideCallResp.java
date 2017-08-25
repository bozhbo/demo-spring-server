package com.snail.webgame.game.protocal.ride.call;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.ride.query.RideDetailRe;

public class RideCallResp extends MessageBody {
	private int result;
	private RideDetailRe re;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addObject("re");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public RideDetailRe getRe() {
		return re;
	}

	public void setRe(RideDetailRe re) {
		this.re = re;
	}

}
