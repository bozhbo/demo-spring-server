package com.snail.webgame.game.protocal.relation.recommend;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class RecommendFriend4AddReq extends MessageBody {
	private int action; // 0 - 获取推荐 1 - 刷新

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("action", 0);
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}
