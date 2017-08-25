package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 装备强化次数（只在装备强化行为触发）
 * 
 * @author tangjq
 * 
 */
public class ZbupCond extends AbstractConditionCheck {

	private int num;

	public ZbupCond() {
	}

	public ZbupCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new ZbupCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (questInProgress == null || obj == null || action != ActionType.action77.getType()) {
			return 0;
		}

		int addNum = (Integer) obj;
		int value = (int) questInProgress.getSpecValue(index);
		value += addNum;
		if (value >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}
		updateValue(index, questInProgress, value);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
