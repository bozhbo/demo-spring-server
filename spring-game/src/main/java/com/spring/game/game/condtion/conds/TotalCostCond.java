package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class TotalCostCond extends AbstractConditionCheck {

	private int targetCost;
	
	public TotalCostCond() {
	}
	
	public TotalCostCond(int targetCost) {
		this.targetCost = targetCost;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		int targetCost = Integer.valueOf(agrs[1]);
		return new TotalCostCond(targetCost);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		
		int totalCost = (int) (roleInfo.getTotalCoin() - roleInfo.getCoin());
		if (totalCost >= targetCost) {
			updateValue(index, checkDataChg, targetCost);
			return 1;
		}
		
		updateValue(index, checkDataChg, totalCost);
		return 0;
	}
}
