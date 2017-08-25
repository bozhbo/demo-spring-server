package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class GiveEnergyCond extends AbstractConditionCheck {

	private int num;
	
	public GiveEnergyCond() {
	}

	public GiveEnergyCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new GiveEnergyCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null || action != ActionType.action471.getType()) {
			return 0;
		}
		
		int val = (int) checkDataChg.getSpecValue(index);
		val++;
		
		if (val >= num) {
			updateValue(index, checkDataChg, num);
			return 1;
		}
		updateValue(index, checkDataChg, val);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
