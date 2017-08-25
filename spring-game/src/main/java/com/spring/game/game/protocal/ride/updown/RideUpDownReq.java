package com.snail.webgame.game.protocal.ride.updown;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RideUpDownReq extends MessageBody {
	private int rideId;
	private byte state;// 0-下阵 1-上阵

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("rideId", 0);
		ps.add("state", 0);
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

}
