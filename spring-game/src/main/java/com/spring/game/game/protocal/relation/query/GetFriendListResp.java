package com.snail.webgame.game.protocal.relation.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.relation.entity.FriendDetailRe;

public class GetFriendListResp extends MessageBody {
	private int result;
	private int count;
	private List<FriendDetailRe> list = new ArrayList<FriendDetailRe>();
	private int blackListCount;
	private List<FriendDetailRe> blackList = new ArrayList<FriendDetailRe>();
	private int isCanAdd;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.relation.entity.FriendDetailRe", "count");
		ps.add("blackListCount", 0);
		ps.addObjectArray("blackList", "com.snail.webgame.game.protocal.relation.entity.FriendDetailRe", "blackListCount");
		ps.add("isCanAdd", 0);
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

	public int getBlackListCount() {
		return blackListCount;
	}

	public void setBlackListCount(int blackListCount) {
		this.blackListCount = blackListCount;
	}

	public List<FriendDetailRe> getBlackList() {
		return blackList;
	}

	public void setBlackList(List<FriendDetailRe> blackList) {
		this.blackList = blackList;
	}

	public int getIsCanAdd() {
		return isCanAdd;
	}

	public void setIsCanAdd(int isCanAdd) {
		this.isCanAdd = isCanAdd;
	}
}
