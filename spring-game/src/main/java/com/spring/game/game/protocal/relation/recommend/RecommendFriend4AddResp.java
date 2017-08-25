package com.snail.webgame.game.protocal.relation.recommend;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.relation.entity.FriendDetailRe;

public class RecommendFriend4AddResp extends MessageBody{
	private int result;
	private int count;
	private List<FriendDetailRe> list = new ArrayList<FriendDetailRe>();
	private int action;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.relation.entity.FriendDetailRe", "count");
		ps.add("action", 0);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FriendDetailRe> getList() {
		return list;
	}

	public void setList(List<FriendDetailRe> list) {
		this.list = list;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
	
	
}
