package com.snail.webgame.game.protocal.scene.mapPvpFight;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 大地图攻击
 * 
 * @author hongfm
 * 
 */
public class MapPvpFightReq extends MessageBody {

	private int beRoleId;
	private byte fightType;// 1-截标战斗 2-普通战斗

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("beRoleId", 0);
		ps.add("fightType", 0);
	}

	public int getBeRoleId() {
		return beRoleId;
	}

	public void setBeRoleId(int beRoleId) {
		this.beRoleId = beRoleId;
	}

	public byte getFightType() {
		return fightType;
	}

	public void setFightType(byte fightType) {
		this.fightType = fightType;
	}

}
