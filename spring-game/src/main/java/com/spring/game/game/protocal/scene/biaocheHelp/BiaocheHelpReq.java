package com.snail.webgame.game.protocal.scene.biaocheHelp;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 镖车刷新
 * @author hongfm
 *
 */
public class BiaocheHelpReq extends MessageBody {

	private int helpRoleId;
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("helpRoleId", 0);
	}

	public int getHelpRoleId() {
		return helpRoleId;
	}

	public void setHelpRoleId(int helpRoleId) {
		this.helpRoleId = helpRoleId;
	}
	
}
