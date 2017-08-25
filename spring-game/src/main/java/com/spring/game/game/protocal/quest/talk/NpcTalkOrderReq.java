package com.snail.webgame.game.protocal.quest.talk;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 记录当前任务的对话顺序
 * 
 * @author nijp
 *
 */
public class NpcTalkOrderReq extends MessageBody {

	private int questProtoNo;// 任务模板no
	private byte talkOrder;// 当前任务的对话顺序

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("questProtoNo",  0);
		ps.add("talkOrder",  0);
	}

	public int getQuestProtoNo() {
		return questProtoNo;
	}

	public void setQuestProtoNo(int questProtoNo) {
		this.questProtoNo = questProtoNo;
	}

	public byte getTalkOrder() {
		return talkOrder;
	}

	public void setTalkOrder(byte talkOrder) {
		this.talkOrder = talkOrder;
	}

}
