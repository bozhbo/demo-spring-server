package com.snail.webgame.game.protocal.fight.team.refresh;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 
 * 类介绍:组队副本战斗次数刷新
 *
 * @author xiasd
 * @2015年10月13日
 */
public class TeamFightCountResp extends MessageBody {
	
	private String teamChallengeCount;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("teamChallengeCount", "flashCode", 0);
	}

	public String getTeamChallengeCount() {
		return teamChallengeCount;
	}

	public void setTeamChallengeCount(String teamChallengeCount) {
		this.teamChallengeCount = teamChallengeCount;
	}

}
