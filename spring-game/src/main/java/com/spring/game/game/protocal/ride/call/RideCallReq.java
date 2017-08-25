package com.snail.webgame.game.protocal.ride.call;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RideCallReq extends MessageBody {
	private int rideNo;// 召唤的坐骑编号

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("rideNo", 0);
	}

	public int getRideNo() {
		return rideNo;
	}

	public void setRideNo(int rideNo) {
		this.rideNo = rideNo;
	}

}
