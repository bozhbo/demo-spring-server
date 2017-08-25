package com.snail.webgame.game.protocal.opactivity.weixin.getaward;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class WeiXinGetAwardReq extends MessageBody {

	private byte awardType;// 奖励类型 1-首次 2-每日

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("awardType", 0);
	}

	public byte getAwardType() {
		return awardType;
	}

	public void setAwardType(byte awardType) {
		this.awardType = awardType;
	}

}
