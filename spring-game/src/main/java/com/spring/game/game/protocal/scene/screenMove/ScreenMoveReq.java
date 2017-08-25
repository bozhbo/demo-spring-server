package com.snail.webgame.game.protocal.scene.screenMove;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 设备屏幕移动
 * @author hongfm
 *
 */
public class ScreenMoveReq extends MessageBody {
	
	private int pointx;
	private int pointz;
	private byte disConnectIntoMap;//1-断线重连后重进游戏
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("pointx", 0);
		ps.add("pointz", 0);
		ps.add("disConnectIntoMap", 0);
	}

	public int getPointx() {
		return pointx;
	}

	public void setPointx(int pointx) {
		this.pointx = pointx;
	}

	public int getPointz() {
		return pointz;
	}

	public void setPointz(int pointz) {
		this.pointz = pointz;
	}

	public byte getDisConnectIntoMap() {
		return disConnectIntoMap;
	}

	public void setDisConnectIntoMap(byte disConnectIntoMap) {
		this.disConnectIntoMap = disConnectIntoMap;
	}
}
