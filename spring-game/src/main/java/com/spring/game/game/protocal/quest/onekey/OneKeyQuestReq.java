package com.snail.webgame.game.protocal.quest.onekey;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class OneKeyQuestReq extends MessageBody {

	private byte isOneKey;// 1-一键秒所有 2-秒单个
	private String questProtoNoStr;// 需要一键秒的任务编号 格式：no,no

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("isOneKey", 0);
		ps.addString("questProtoNoStr", "flashCode", 0);
	}

	public byte getIsOneKey() {
		return isOneKey;
	}

	public void setIsOneKey(byte isOneKey) {
		this.isOneKey = isOneKey;
	}

	public String getQuestProtoNoStr() {
		return questProtoNoStr;
	}

	public void setQuestProtoNoStr(String questProtoNoStr) {
		this.questProtoNoStr = questProtoNoStr;
	}
}
