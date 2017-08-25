package com.snail.webgame.game.protocal.scene.mapMove;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 附近原玩家更新我的坐标
 * @author hongfm
 *
 */
public class MapMoveUpdateResp extends MessageBody {
	
	private int roleId;
	private float pointx;
	private float pointz;
	private byte status;
	private byte biaocheType;
	private int otherRoleId;// 有护镖人的时候
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("roleId", 0);
		ps.add("pointx", 0);
		ps.add("pointz", 0);
		ps.add("status", 0);
		ps.add("biaocheType", 0);
		ps.add("otherRoleId", 0);
	}

	public float getPointx() {
		return pointx;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setPointz(float pointz) {
		this.pointz = pointz;
	}

	public void setPointx(float pointx) {
		this.pointx = pointx;
	}

	public float getPointz() {
		return pointz;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getBiaocheType() {
		return biaocheType;
	}

	public void setBiaocheType(byte biaocheType) {
		this.biaocheType = biaocheType;
	}

	public int getOtherRoleId() {
		return otherRoleId;
	}

	public void setOtherRoleId(int otherRoleId) {
		this.otherRoleId = otherRoleId;
	}
	
}
