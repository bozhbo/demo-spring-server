package com.snail.webgame.game.protocal.ride.uplv;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RideUpLvReq extends MessageBody {
	private int rideId;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("rideId", 0);
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}

}
