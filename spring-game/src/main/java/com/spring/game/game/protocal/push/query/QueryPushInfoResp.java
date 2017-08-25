package com.snail.webgame.game.protocal.push.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryPushInfoResp extends MessageBody {
	private int result;
	private String rolePushStr; // 玩家当前设置的推送信息 格式:no,no

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("rolePushStr", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRolePushStr() {
		return rolePushStr;
	}

	public void setRolePushStr(String rolePushStr) {
		this.rolePushStr = rolePushStr;
	}

}
