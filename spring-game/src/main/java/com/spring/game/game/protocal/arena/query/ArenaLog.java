package com.snail.webgame.game.protocal.arena.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe;

public class ArenaLog extends MessageBody {

	private int fightRoleId;// 战斗方
	private String fightRoleName;// 战斗方角色名称

	private int beforePlace;// 战斗前排名
	private int afterPlace;// 战斗后排名
	private int fightResult;// 1-胜 2-败

	private long beginTime;// 战斗开始时间
	// 战斗方上阵信息
	private int count;
	private List<FightDeployDetailRe> list = new ArrayList<FightDeployDetailRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("fightRoleId", 0);
		ps.addString("fightRoleName", "flashCode", 0);

		ps.add("beforePlace", 0);
		ps.add("afterPlace", 0);
		ps.add("fightResult", 0);

		ps.add("beginTime", 0);

		ps.add("count", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe", "count");
	}

	public int getFightRoleId() {
		return fightRoleId;
	}

	public void setFightRoleId(int fightRoleId) {
		this.fightRoleId = fightRoleId;
	}

	public String getFightRoleName() {
		return fightRoleName;
	}

	public void setFightRoleName(String fightRoleName) {
		this.fightRoleName = fightRoleName;
	}

	public int getBeforePlace() {
		return beforePlace;
	}

	public void setBeforePlace(int beforePlace) {
		this.beforePlace = beforePlace;
	}

	public int getAfterPlace() {
		return afterPlace;
	}

	public void setAfterPlace(int afterPlace) {
		this.afterPlace = afterPlace;
	}

	public int getFightResult() {
		return fightResult;
	}

	public void setFightResult(int fightResult) {
		this.fightResult = fightResult;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<FightDeployDetailRe> getList() {
		return list;
	}

	public void setList(List<FightDeployDetailRe> list) {
		this.list = list;
	}
}
