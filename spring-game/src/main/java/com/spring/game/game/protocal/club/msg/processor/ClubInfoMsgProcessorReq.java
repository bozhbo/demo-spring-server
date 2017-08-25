package com.snail.webgame.game.protocal.club.msg.processor;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubInfoMsgProcessorReq extends MessageBody {
	private int reqType; // 1 - 事件列表  2 - 请求列表 3 - 成员列表
	private int clubId;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("reqType", 0);
		ps.add("clubId", 0);
	}

	public int getReqType() {
		return reqType;
	}

	public void setReqType(int reqType) {
		this.reqType = reqType;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

}
