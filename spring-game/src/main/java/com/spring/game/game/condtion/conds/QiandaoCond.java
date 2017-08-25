package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class QiandaoCond extends AbstractConditionCheck {

	private int needNum;

	public QiandaoCond() {

	}

	public QiandaoCond(int needNum) {
		this.needNum = needNum;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		int needNum = Integer.valueOf(agrs[1]);
		QiandaoCond cond = new QiandaoCond(needNum);
		return cond;
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (questInProgress == null || action != ActionType.action31.getType()) {
			return 0;
		}

		int value = (int) questInProgress.getSpecValue(index);
		value++;
		if (value >= needNum) {
			updateValue(index, questInProgress, needNum);
			return 1;
		}
		updateValue(index, questInProgress, value);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
