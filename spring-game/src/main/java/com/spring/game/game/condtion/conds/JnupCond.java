package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 技能升级次数
 * 
 * @author tangjq
 * 
 */
public class JnupCond extends AbstractConditionCheck {

	private int isMain;// 1-主角 2-副将
	private int num;

	public JnupCond() {
	}

	public JnupCond(int isMain, int num) {
		this.isMain = isMain;
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int isMain = Integer.parseInt(agrs[1]);
		int num = Integer.parseInt(agrs[2]);
		return new JnupCond(isMain, num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (questInProgress == null || (action != ActionType.action75.getType() && action != ActionType.action90.getType())) {
			return 0;
		}
		
		if ((action == ActionType.action75.getType() && isMain != 2) || (action == ActionType.action90.getType() && isMain != 1)) {
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
