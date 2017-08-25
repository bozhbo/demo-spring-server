package com.snail.webgame.game.protocal.gm.command;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GmResp extends MessageBody {

	private int result;
	private String command;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.addString("command", "flashCode", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
