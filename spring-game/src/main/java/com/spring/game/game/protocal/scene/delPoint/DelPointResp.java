package com.snail.webgame.game.protocal.scene.delPoint;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


/**
 * 场景AI消失
 * @author hongfm
 *
 */
public class DelPointResp extends MessageBody {

	private int AIType;//1-角色,2-NPC
	private long changeAIId;//移动AI编号(NPC时NPC编号)
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("AIType", 0);
		ps.add("changeAIId", 0);
	}
	public int getAIType() {
		return AIType;
	}
	public void setAIType(int aIType) {
		AIType = aIType;
	}
	public long getChangeAIId() {
		return changeAIId;
	}

	public void setChangeAIId(long changeAIId) {
		this.changeAIId = changeAIId;
	}
	
}
