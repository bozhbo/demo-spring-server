package com.snail.webgame.game.protocal.snatch.safeMode;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SafeModeReq extends MessageBody {
	private byte operatingMode;	//操作类型(1开启 2关闭)
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("operatingMode", 0);
	}
	public byte getOperatingMode() {
		return operatingMode;
	}
	public void setOperatingMode(byte operatingMode) {
		this.operatingMode = operatingMode;
	}
}
