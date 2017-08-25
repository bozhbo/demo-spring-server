package com.snail.webgame.game.protocal.appellation.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 查询称号面板主界面返回消息
 * 
 * @author SnailGame
 * 
 */
public class QueryAppellationResp extends MessageBody {
	private int result; // 玩家所有称号
	private String allAppellation;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("allAppellation", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getAllAppellation() {
		return allAppellation;
	}

	public void setAllAppellation(String allAppellation) {
		this.allAppellation = allAppellation;
	}

}
