package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class TotalChargeCond extends AbstractConditionCheck {

	private int targetCharge;
	
	public TotalChargeCond() {
	}
	
	public TotalChargeCond(int targetCharge) {
		this.targetCharge = targetCharge;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		int targetCharge = Integer.valueOf(agrs[1]);
		return new TotalChargeCond(targetCharge);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		
		if (roleInfo.getTotalCharge() >= targetCharge) {
			updateValue(index, checkDataChg, targetCharge);
			return 1;
		}
		
		updateValue(index, checkDataChg, (int) roleInfo.getTotalCharge());
		return 0;
	}

}
