package com.snail.webgame.game.protocal.rolemgt.worldChatLimit;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class WorldChatLimitReq extends MessageBody {
	private int limit; //世界聊天发送消息数量的限制
	private int countFlag; //1 -  把worldChatCount清零 0 - 不处理
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("limit", 0);
		ps.add("countFlag", 0);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getCountFlag() {
		return countFlag;
	}

	public void setCountFlag(int countFlag) {
		this.countFlag = countFlag;
	}
	
}
