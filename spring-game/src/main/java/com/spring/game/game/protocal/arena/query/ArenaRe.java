package com.snail.webgame.game.protocal.arena.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe;

public class ArenaRe extends MessageBody {

	private int arenaId;// 竞技场Id
	private int roleId;// 0-系统随机 other-用户编号
	private String roleName;// 角色名称

	private int maxPlace;// 历史最高排名
	private int place;// 当前排名

	private int size;
	private List<FightDeployDetailRe> heros = new ArrayList<FightDeployDetailRe>();

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("arenaId", 0);
		ps.add("roleId", 0);
		ps.addString("roleName", "flashCode", 0);

		ps.add("maxPlace", 0);
		ps.add("place", 0);

		ps.add("size", 0);
		ps.addObjectArray("heros", "com.snail.webgame.game.protocal.fightdeploy.view.FightDeployDetailRe", "size");
	}

	public int getArenaId() {
		return arenaId;
	}

	public void setArenaId(int arenaId) {
		this.arenaId = arenaId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<FightDeployDetailRe> getHeros() {
		return heros;
	}

	public void setHeros(List<FightDeployDetailRe> heros) {
		this.heros = heros;
	}
}
