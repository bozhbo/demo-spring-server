package com.snail.webgame.game.protocal.server.active;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ServerActiveReq extends MessageBody {

 
	private String serverName;
	private int flag;
	private String reserve;
	
	protected void setSequnce(ProtocolSequence ps) {
	 
		ps.addString("serverName", "flashCode", 0);
		ps.add("flag", 0);
		ps.addString("reserve", "flashCode", 0);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

 

}
