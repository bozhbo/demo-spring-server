package com.snail.webgame.game.protocal.rolemgt.syncAdvert;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SyncAdvertResp extends MessageBody {

	private byte isAdvert;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("isAdvert", 0);
	}

	public byte getIsAdvert() {
		return isAdvert;
	}

	public void setIsAdvert(byte isAdvert) {
		this.isAdvert = isAdvert;
	}

}
