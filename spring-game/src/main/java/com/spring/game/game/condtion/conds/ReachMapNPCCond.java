package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 到达大地图某NPC完成任务
 * 
 * @author hongfm
 *
 */
public class ReachMapNPCCond extends AbstractConditionCheck {

	private String GW;
	private int reachTime;
	
	public ReachMapNPCCond() {
		
	}
	
	public ReachMapNPCCond(String GW, int reachTime) {
		this.GW = GW;
		this.reachTime = reachTime;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		String GW =agrs[1];
		int reachTime = Integer.valueOf(agrs[2]);
		return new ReachMapNPCCond(GW, reachTime);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (action != ActionType.action17.getType()) {
			return 0;
		}
		
		int val = (int) questInProgress.getSpecValue(index);
		if (GW.equalsIgnoreCase(String.valueOf(obj))) {
			val++;
		}
		
		if (val >= reachTime) {
			updateValue(index, questInProgress, reachTime);
			return 1;
		}
		
		updateValue(index, questInProgress, val);
		return 0;
	}

}
