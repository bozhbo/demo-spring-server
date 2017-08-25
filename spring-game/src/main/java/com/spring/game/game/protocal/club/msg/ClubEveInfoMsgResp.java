package com.snail.webgame.game.protocal.club.msg;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.club.entity.ClubEveInfoRe;

public class ClubEveInfoMsgResp extends MessageBody{
	private int result;
	private int eveListCount;
	private List<ClubEveInfoRe> eveList = new ArrayList<ClubEveInfoRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("eveListCount", 0);
		ps.addObjectArray("eveList", "com.snail.webgame.game.protocal.club.entity.ClubEveInfoRe", "eveListCount");

	}

	public int getEveListCount() {
		return eveListCount;
	}

	public void setEveListCount(int eveListCount) {
		this.eveListCount = eveListCount;
	}

	public List<ClubEveInfoRe> getEveList() {
		return eveList;
	}

	public void setEveList(List<ClubEveInfoRe> eveList) {
		this.eveList = eveList;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}


}
