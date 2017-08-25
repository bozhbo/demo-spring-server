package com.snail.webgame.game.protocal.server.time;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SysTimeResp extends MessageBody {

 
	private int result;
	private long currTime;
	
	protected void setSequnce(ProtocolSequence ps) {
	 
		ps.add("result", 0);
		ps.add("currTime", 0);
		
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public long getCurrTime() {
		return currTime;
	}

	public void setCurrTime(long currTime) {
		this.currTime = currTime;
	}

}
