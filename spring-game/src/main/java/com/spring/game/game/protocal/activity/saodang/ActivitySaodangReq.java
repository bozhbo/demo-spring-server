package com.snail.webgame.game.protocal.activity.saodang;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 扫荡练兵场
 * 
 * @author nijy
 *
 */
public class ActivitySaodangReq extends MessageBody {

	private int saodangType;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("saodangType", 0);
	}

	public int getSaodangType() {
		return saodangType;
	}

	public void setSaodangType(int saodangType) {
		this.saodangType = saodangType;
	}

	

}
