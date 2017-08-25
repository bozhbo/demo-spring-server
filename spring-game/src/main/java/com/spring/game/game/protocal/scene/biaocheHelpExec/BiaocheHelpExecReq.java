package com.snail.webgame.game.protocal.scene.biaocheHelpExec;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 接受或拒绝好友护送压镖请求
 * @author hongfm
 *
 */
public class BiaocheHelpExecReq extends MessageBody {

	private int beHelpRoleId;
	private byte helpType;//1-接受 2-拒绝
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("beHelpRoleId", 0);
		ps.add("helpType", 0);
	}

	public int getBeHelpRoleId() {
		return beHelpRoleId;
	}

	public void setBeHelpRoleId(int beHelpRoleId) {
		this.beHelpRoleId = beHelpRoleId;
	}

	public byte getHelpType() {
		return helpType;
	}

	public void setHelpType(byte helpType) {
		this.helpType = helpType;
	}
	
	
}
