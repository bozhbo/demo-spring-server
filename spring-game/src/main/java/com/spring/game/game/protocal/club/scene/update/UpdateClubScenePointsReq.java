package com.snail.webgame.game.protocal.club.scene.update;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UpdateClubScenePointsReq extends MessageBody {
	private float pointX;
	private float pointY;
	private float pointZ;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("pointX", 0);
		ps.add("pointY", 0);
		ps.add("pointZ", 0);
	}

	public float getPointX() {
		return pointX;
	}

	public void setPointX(float pointX) {
		this.pointX = pointX;
	}

	public float getPointY() {
		return pointY;
	}

	public void setPointY(float pointY) {
		this.pointY = pointY;
	}

	public float getPointZ() {
		return pointZ;
	}

	public void setPointZ(float pointZ) {
		this.pointZ = pointZ;
	}

}
