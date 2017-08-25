package com.snail.webgame.game.protocal.fight.mutual.rank;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MutualQueryRankResp extends MessageBody {

	private int myRank;
	private int myValue;
	private int count;
	private List<MutualQueryRankRes> list;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("myRank", 0);
		ps.add("myValue", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fight.mutual.rank.MutualQueryRankRes", "count");
		
	}

	public int getMyRank() {
		return myRank;
	}

	public void setMyRank(int myRank) {
		this.myRank = myRank;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<MutualQueryRankRes> getList() {
		return list;
	}

	public void setList(List<MutualQueryRankRes> list) {
		this.list = list;
	}

	public int getMyValue() {
		return myValue;
	}

	public void setMyValue(int myValue) {
		this.myValue = myValue;
	}
	
	
}
