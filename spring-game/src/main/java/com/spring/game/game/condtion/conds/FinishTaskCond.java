package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class FinishTaskCond extends AbstractConditionCheck {

	private int no;

	public FinishTaskCond() {
	}

	public FinishTaskCond(int no) {
		this.no = no;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {

		if (agrs.length < 2) {
			return null;
		}
		int no = Integer.parseInt(agrs[1]);
		return new FinishTaskCond(no);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		// 初始化一刻检测及完成任务时检测
		if (action != ActionType.action181.getType() || obj == null) {
			return 0;
		}
		
		int finishQuestNo = (Integer) obj;
		if (finishQuestNo == no) {
			updateValue(index, questInProgress, no);
			return 1;
		}
		
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}

