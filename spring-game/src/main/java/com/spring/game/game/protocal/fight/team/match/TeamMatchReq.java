package com.snail.webgame.game.protocal.fight.team.match;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class TeamMatchReq extends MessageBody {

	private int activityType;// 活动类型 1-组队副本  2-3V3
	private int matchType; // 匹配类型 1-单人匹配 2-组队匹配
	private int duplicateNo;// 副本ID

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("activityType", 0);
		ps.add("matchType", 0);
		ps.add("duplicateNo", 0);
	}

	public int getMatchType() {
		return matchType;
	}

	public void setMatchType(int matchType) {
		this.matchType = matchType;
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
