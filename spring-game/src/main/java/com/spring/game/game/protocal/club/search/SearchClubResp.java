package com.snail.webgame.game.protocal.club.search;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.club.entity.ClubInfoRe;

public class SearchClubResp extends MessageBody {
	private int result;
	private int count;
	private List<ClubInfoRe> list = new ArrayList<ClubInfoRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.club.entity.ClubInfoRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ClubInfoRe> getList() {
		return list;
	}

	public void setList(List<ClubInfoRe> list) {
		this.list = list;
	}

}
