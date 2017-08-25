package com.snail.webgame.game.protocal.fight.team.rank;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

/**
 * 查询组队副本首杀速杀
 * 
 * @author xiasd
 *
 */
public class TeamQueryRankReq extends MessageBody{

	private int duplicateId;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("duplicateId", 0);
	}

	public int getDuplicateId() {
		return duplicateId;
	}

	public void setDuplicateId(int duplicateId) {
		this.duplicateId = duplicateId;
	}

}
