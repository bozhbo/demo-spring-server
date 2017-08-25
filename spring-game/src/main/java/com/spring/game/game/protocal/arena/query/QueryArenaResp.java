package com.snail.webgame.game.protocal.arena.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QueryArenaResp extends MessageBody {

	private int result;
	private int roleId;// 用户编号
	private int maxPlace;// 历史最高排名
	private int place;// 当前排名

	private long resetTime;// 下次重置时间 0-不需重置

	// 匹配的战斗用户
	private int count;
	private List<ArenaRe> list = new ArrayList<ArenaRe>();

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("roleId", 0);
		ps.add("maxPlace", 0);
		ps.add("place", 0);

		ps.add("resetTime", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.arena.query.ArenaRe", "count");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getMaxPlace() {
		return maxPlace;
	}

	public void setMaxPlace(int maxPlace) {
		this.maxPlace = maxPlace;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public long getResetTime() {
		return resetTime;
	}

	public void setResetTime(long resetTime) {
		this.resetTime = resetTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ArenaRe> getList() {
		return list;
	}

	public void setList(List<ArenaRe> list) {
		this.list = list;
	}
}
