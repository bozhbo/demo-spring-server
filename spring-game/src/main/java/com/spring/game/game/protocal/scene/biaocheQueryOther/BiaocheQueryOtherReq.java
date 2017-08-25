package com.snail.webgame.game.protocal.scene.biaocheQueryOther;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 查看世界地图上的他人镖车
 * @author hongfm
 *
 */
public class BiaocheQueryOtherReq extends MessageBody {

	private int otherRoleId;//他人角色Id
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("otherRoleId", 0);
	}

	public int getOtherRoleId() {
		return otherRoleId;
	}

	public void setOtherRoleId(int otherRoleId) {
		this.otherRoleId = otherRoleId;
	}
	
	
}
