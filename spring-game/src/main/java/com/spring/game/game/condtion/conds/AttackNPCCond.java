package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 *攻击NP任务
 * 
 * @author hongfm
 *
 */
public class AttackNPCCond extends AbstractConditionCheck {

	private String GW;
	private int attackTime;
	
	public AttackNPCCond() {
		
	}
	
	public AttackNPCCond(String GW, int attackTime) {
		this.GW = GW;
		this.attackTime = attackTime;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		String GW = agrs[1];
		int attackTime = Integer.valueOf(agrs[2]);
		return new AttackNPCCond(GW, attackTime);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (action != ActionType.action19.getType() || obj == null) {
			return 0;
		}
		
		String noStr = ((String) obj).split(",")[0];
		int val = (int) questInProgress.getSpecValue(index);
		if (GW.equalsIgnoreCase(noStr)) {
			val++;
		}
		
		if (val >= attackTime) {
			updateValue(index, questInProgress, attackTime);
			return 1;
		}
		
		updateValue(index, questInProgress, val);
		return 0;
		
	}

}
