package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class TeamChallengeCond extends AbstractConditionCheck {
	
	private int no;
	private int num;

	public TeamChallengeCond() {
	}

	public TeamChallengeCond(int no, int num) {
		this.no = no;
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int no = Integer.parseInt(agrs[1]);
		int num = Integer.parseInt(agrs[2]);
		return new TeamChallengeCond(no, num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		// 任务条件检测
		if (questInProgress == null || action != ActionType.action490.getType() || obj == null) {
			return 0;
		}
		
		int noStr = (Integer) obj;
		
		int value = (int) questInProgress.getSpecValue(index);
		if (no == 0 || no == noStr) {
			value ++;
		}

		if (value >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}
		updateValue(index, questInProgress, value);

		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
