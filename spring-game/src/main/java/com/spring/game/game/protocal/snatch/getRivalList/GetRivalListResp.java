package com.snail.webgame.game.protocal.snatch.getRivalList;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetRivalListResp extends MessageBody {
	private int result;
	private int rivalListSize;	//rivalList长度
	private List<RivalListRe> rivalList;	//挑战列表
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("rivalListSize", 0);
		ps.addObjectArray("rivalList", "com.snail.webgame.game.protocal.snatch.getRivalList.RivalListRe", "rivalListSize");
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getRivalListSize() {
		return rivalListSize;
	}
	public void setRivalListSize(int rivalListSize) {
		this.rivalListSize = rivalListSize;
	}
	public List<RivalListRe> getRivalList() {
		return rivalList;
	}
	public void setRivalList(List<RivalListRe> rivalList) {
		this.rivalList = rivalList;
	}
}
