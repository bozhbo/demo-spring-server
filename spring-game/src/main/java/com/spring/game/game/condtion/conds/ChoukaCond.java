package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 检测抽卡次数(只在抽卡后，触发)
 */
public class ChoukaCond extends AbstractConditionCheck {

	private int num;

	public ChoukaCond() {
	}

	public ChoukaCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new ChoukaCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {

		if (questInProgress == null || 
				!(action == ActionType.action66.getType() || action == ActionType.action67.getType() || action == ActionType.action68.getType() || 
				action == ActionType.action69.getType() || action == ActionType.action437.getType() || action == ActionType.action438.getType())) {
			return 0;
		}
		int chouKaNum = 0;

		if (obj != null) {
			chouKaNum = (Integer) obj;
		}
		int value = (int) questInProgress.getSpecValue(index);
		value += chouKaNum;

		if (value >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}

		updateValue(index, questInProgress, value);

		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
