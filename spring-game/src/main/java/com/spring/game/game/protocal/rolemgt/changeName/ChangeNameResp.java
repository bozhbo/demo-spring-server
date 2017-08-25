package com.snail.webgame.game.protocal.rolemgt.changeName;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChangeNameResp extends MessageBody{

	private int result;
	private String roleName;//改后的名字
	
	protected void setSequnce(ProtocolSequence ps) {	 
		ps.add("result", 0);
		ps.addString("roleName", "flashCode", 0);
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
