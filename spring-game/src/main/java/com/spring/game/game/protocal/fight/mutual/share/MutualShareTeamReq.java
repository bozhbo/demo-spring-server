package com.snail.webgame.game.protocal.fight.mutual.share;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MutualShareTeamReq extends MessageBody{
	
	private int activityType;// 1-长坂坡 2-组队副本 3-3V3竞技场
	private int duplicateNo;// 副本ID
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("activityType", 0);
		ps.add("duplicateNo", 0);
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public int getDuplicateNo() {
		return duplicateNo;
	}

	public void setDuplicateNo(int duplicateNo) {
		this.duplicateNo = duplicateNo;
	}
	
}
