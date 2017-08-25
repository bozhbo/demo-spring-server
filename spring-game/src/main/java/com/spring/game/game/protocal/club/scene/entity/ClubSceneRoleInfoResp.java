package com.snail.webgame.game.protocal.club.scene.entity;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubSceneRoleInfoResp extends MessageBody{
	private int result;
	private int roleId; // 当FLAG为0时 这个roleId为移除的角色Id, 当FLAG = 1的时候 是自己的roleId
	private int clubId;
	private int count;
	private List<ClubSceneRoleInfo> list;
	private int flag; //0 - 移除角色（离开场景） 1 - 增加角色  2 - 会长形象更改

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("roleId", 0);
		ps.add("clubId", 0);
		ps.add("count", 0);
		
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.club.scene.entity.ClubSceneRoleInfo", "count");
		
		ps.add("flag", 0);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ClubSceneRoleInfo> getList() {
		return list;
	}

	public void setList(List<ClubSceneRoleInfo> list) {
		this.list = list;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
