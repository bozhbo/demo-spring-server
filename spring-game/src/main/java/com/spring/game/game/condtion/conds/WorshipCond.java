package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class WorshipCond extends AbstractConditionCheck {
	private int num;
	
	public WorshipCond() {
	}
	
	public WorshipCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new WorshipCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null || action != ActionType.action470.getType()) {
			return 0;
		}
		
		int value = (int) checkDataChg.getSpecValue(index);
		value ++;
		
		if (value >= num) {
			updateValue(index, checkDataChg, num);
			return 1;
		}

		updateValue(index, checkDataChg, value);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
