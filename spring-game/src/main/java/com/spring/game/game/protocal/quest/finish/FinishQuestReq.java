package com.snail.webgame.game.protocal.quest.finish;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FinishQuestReq extends MessageBody {

	private int questProtoNo;// 任务编号

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("questProtoNo", 0);
	}

	public int getQuestProtoNo() {
		return questProtoNo;
	}

	public void setQuestProtoNo(int questProtoNo) {
		this.questProtoNo = questProtoNo;
	}
}
