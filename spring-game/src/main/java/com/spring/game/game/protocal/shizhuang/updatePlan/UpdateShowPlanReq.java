package com.snail.webgame.game.protocal.shizhuang.updatePlan;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UpdateShowPlanReq extends MessageBody {
	
	private int planId;// 0-显示套装  1-显示时装
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("planId", 0);
	}
	public int getPlanId() {
		return planId;
	}
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	
}
