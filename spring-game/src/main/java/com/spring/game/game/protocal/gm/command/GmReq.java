package com.snail.webgame.game.protocal.gm.command;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GmReq extends MessageBody {

	private String command;

	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("command", "flashCode", 0);
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
