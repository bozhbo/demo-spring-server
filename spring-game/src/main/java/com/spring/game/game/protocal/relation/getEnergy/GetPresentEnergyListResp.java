package com.snail.webgame.game.protocal.relation.getEnergy;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.relation.entity.FriendDetailRe;

public class GetPresentEnergyListResp extends MessageBody {
	private int result;
	private int count;
	private List<FriendDetailRe> list = new ArrayList<FriendDetailRe>();
	private int getTimes; //每日获取精力的次数

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.relation.entity.FriendDetailRe", "count");
		ps.add("getTimes", 0);
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

	public List<FriendDetailRe> getList() {
		return list;
	}

	public void setList(List<FriendDetailRe> list) {
		this.list = list;
	}

	public int getGetTimes() {
		return getTimes;
	}

	public void setGetTimes(int getTimes) {
		this.getTimes = getTimes;
	}

}
