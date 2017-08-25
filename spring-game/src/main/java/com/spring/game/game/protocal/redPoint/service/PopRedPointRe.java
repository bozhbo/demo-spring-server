package com.snail.webgame.game.protocal.redPoint.service;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PopRedPointRe extends MessageBody {
	private byte remindType;//提示类型
	private byte light;//(0灭 1亮)
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("remindType", 0);
		ps.add("light", 0);
	}
	public byte getRemindType() {
		return remindType;
	}
	public void setRemindType(byte remindType) {
		this.remindType = remindType;
	}
	public byte getLight() {
		return light;
	}
	public void setLight(byte light) {
		this.light = light;
	}
}
