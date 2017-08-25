package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class OpActPayCond extends AbstractConditionCheck {
	
	private int totalPay;
	
	public OpActPayCond() {
	}

	public OpActPayCond(int totalPay) {
		this.totalPay = totalPay;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int totalPay = Integer.parseInt(agrs[1]);
		return new OpActPayCond(totalPay);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null || action != ActionType.action412.getType() || obj == null) {
			return 0;
		}
		
		int val = (int) checkDataChg.getSpecValue(index);
		if (val >= totalPay) {
			return 1;
		}
		
		int totalCharge = val + (Integer) obj;
		if (totalCharge >= totalPay) {
			updateValue(index, checkDataChg, totalPay);
			return 1;
		}
		
		if (totalCharge > val) {
			updateValue(index, checkDataChg, totalCharge);
		}
		return 0;
	}

}
