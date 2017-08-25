package com.snail.webgame.game.protocal.scene.updatePoint;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 场景AI1移动
 * @author hongfm
 *
 */
public class UpdatePointReq extends MessageBody {

	private float curPointX;
	private float curPointY;
	private float curPointZ;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("curPointX", 0);
		ps.add("curPointY", 0);
		ps.add("curPointZ", 0);
	}

	public float getCurPointX() {
		return curPointX;
	}

	public void setCurPointX(float curPointX) {
		this.curPointX = curPointX;
	}

	public float getCurPointY() {
		return curPointY;
	}

	public void setCurPointY(float curPointY) {
		this.curPointY = curPointY;
	}

	public float getCurPointZ() {
		return curPointZ;
	}

	public void setCurPointZ(float curPointZ) {
		this.curPointZ = curPointZ;
	}

	
}
