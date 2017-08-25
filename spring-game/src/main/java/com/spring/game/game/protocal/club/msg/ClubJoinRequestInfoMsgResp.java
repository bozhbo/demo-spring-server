package com.snail.webgame.game.protocal.club.msg;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.club.entity.ClubRequestInfoRe;

public class ClubJoinRequestInfoMsgResp extends MessageBody{
	private int result; //1 - 正常 2 - 删掉
	private int requestCount;
	private List<ClubRequestInfoRe> requestList = new ArrayList<ClubRequestInfoRe>();
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("requestCount", 0);
		ps.addObjectArray("requestList", "com.snail.webgame.game.protocal.club.entity.ClubRequestInfoRe", "requestCount");

	}
	public int getRequestCount() {
		return requestCount;
	}
	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}
	public List<ClubRequestInfoRe> getRequestList() {
		return requestList;
	}
	public void setRequestList(List<ClubRequestInfoRe> requestList) {
		this.requestList = requestList;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}

}
