package com.snail.webgame.game.protocal.mine.deploy;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;

public class MineDeployReq extends MessageBody {

	private int mineId;// 矿id
	private int roleId;// 占领人

	// 布阵位置
	private int size;
	private List<StartFightPosInfo> chgPosInfos;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mineId", 0);
		ps.add("roleId", 0);

		ps.add("size", 0);
		ps.addObjectArray("chgPosInfos", "com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo", "size");
	}

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<StartFightPosInfo> getChgPosInfos() {
		return chgPosInfos;
	}

	public void setChgPosInfos(List<StartFightPosInfo> chgPosInfos) {
		this.chgPosInfos = chgPosInfos;
	}
}

