package com.snail.webgame.game.protocal.scene.mapMove;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 大地图移动
 * @author hongfm
 *
 */
public class MapMoveReq extends MessageBody {
	
	private float pointx;
	private float pointz;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("pointx", 0);
		ps.add("pointz", 0);
	}

	public float getPointx() {
		return pointx;
	}

	public void setPointx(float pointx) {
		this.pointx = pointx;
	}

	public float getPointz() {
		return pointz;
	}

	public void setPointz(float pointz) {
		this.pointz = pointz;
	}
	
	
}
