package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class TotalCoinCond extends AbstractConditionCheck {

	private int targetCoin;
	
	public TotalCoinCond() {
	}
	
	public TotalCoinCond(int targetCoin) {
		this.targetCoin = targetCoin;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		int targetCoin = Integer.valueOf(agrs[1]);
		return new TotalCoinCond(targetCoin);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		
		if (roleInfo.getTotalCoin() >= targetCoin) {
			updateValue(index, checkDataChg, targetCoin);
			return 1;
		}
		
		updateValue(index, checkDataChg, (int) roleInfo.getTotalCoin());
		return 0;
	}
}
