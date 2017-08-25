package com.snail.webgame.game.protocal.ride.pass;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RidePassReq extends MessageBody {
	private int leftRideId;// 传承者
	private int rightRideId;// 接收者

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("leftRideId", 0);
		ps.add("rightRideId", 0);
	}

	public int getLeftRideId() {
		return leftRideId;
	}

	public void setLeftRideId(int leftRideId) {
		this.leftRideId = leftRideId;
	}

	public int getRightRideId() {
		return rightRideId;
	}

	public void setRightRideId(int rightRideId) {
		this.rightRideId = rightRideId;
	}

}
