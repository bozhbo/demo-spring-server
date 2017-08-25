package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class OpActConsumeCond extends AbstractConditionCheck {

	private int totalConsume;
	
	public OpActConsumeCond() {
	}

	public OpActConsumeCond(int totalConsume) {
		this.totalConsume = totalConsume;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int totalConsume = Integer.parseInt(agrs[1]);
		return new OpActConsumeCond(totalConsume);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null || action != ActionType.action411.getType() || obj == null) {
			return 0;
		}
		
		int val = (int) checkDataChg.getSpecValue(index);
		if (val >= totalConsume) {
			return 1;
		}
		
		int curCost = val + ((Long) obj).intValue();
		if (curCost >= totalConsume) {
			updateValue(index, checkDataChg, totalConsume);
			return 1;
		}
		
		if (curCost > val) {
			updateValue(index, checkDataChg, curCost);
		}
		
		return 0;
	}
}
