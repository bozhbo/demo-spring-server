package com.snail.webgame.game.protocal.scene.biaocheHelpExec;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 是否护送好友
 * @author hongfm
 *
 */
public class BiaocheHelpExecResp extends MessageBody {

	private int result;
	private int helpRoleId; //同意护送返回好友ID,拒绝不返回
	
	@Override
	protected void setSequnce(ProtocolSequence ps)
	{
		ps.add("result", 0);
		ps.add("helpRoleId", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getHelpRoleId() {
		return helpRoleId;
	}

	public void setHelpRoleId(int helpRoleId) {
		this.helpRoleId = helpRoleId;
	}
	
}
