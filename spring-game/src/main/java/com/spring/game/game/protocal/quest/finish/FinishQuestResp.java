package com.snail.webgame.game.protocal.quest.finish;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FinishQuestResp extends MessageBody {

	private int result;
	private int questProtoNo;// 任务编号

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("questProtoNo", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getQuestProtoNo() {
		return questProtoNo;
	}

	public void setQuestProtoNo(int questProtoNo) {
		this.questProtoNo = questProtoNo;
	}
}
