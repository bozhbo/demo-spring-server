package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 副将升级
 * 
 * @author tangjq
 * 
 */
public class YxupCond extends AbstractConditionCheck {

	private int num;

	public YxupCond() {
	}

	public YxupCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new YxupCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (questInProgress == null || action != ActionType.action72.getType()) {
			return 0;
		}
		int value = (int) questInProgress.getSpecValue(index);
		value++;
		if (value >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}
		updateValue(index, questInProgress, value);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
