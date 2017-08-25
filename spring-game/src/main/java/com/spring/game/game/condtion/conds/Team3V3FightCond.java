package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class Team3V3FightCond extends AbstractConditionCheck {
	
	private int num;

	public Team3V3FightCond() {
	}

	public Team3V3FightCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new Team3V3FightCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		// 任务条件检测
		if (questInProgress == null || action != ActionType.action488.getType()) {
			return 0;
		}

		int value = (int) questInProgress.getSpecValue(index);
		value ++;

		if (value >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}

		updateValue(index, questInProgress, value);

		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
