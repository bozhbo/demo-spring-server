package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class MineNumCond extends AbstractConditionCheck {
	
	private int num;
	
	public MineNumCond() {
	}

	public MineNumCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new MineNumCond(num);
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		// 任务条件检测
		if (checkDataChg == null || 
				(action != ActionType.action452.getType() && action != ActionType.action454.getType())) {
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
