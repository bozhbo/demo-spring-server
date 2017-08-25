package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class ShowFuliCardCond extends AbstractConditionCheck {

	private int flag;

	public ShowFuliCardCond() {
	}
	
	public ShowFuliCardCond(int flag) {
		this.flag = flag;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int flag = Integer.parseInt(agrs[1]);
		return new ShowFuliCardCond(flag);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		long now = System.currentTimeMillis();
		if (flag == 0 && roleInfo.getFuliCardEndTime() == null) {
			updateValue(index, questInProgress, 1);
			return 1;
		}
		
		if (roleInfo.getFuliCardEndTime() != null) {
			if (flag == 0 && roleInfo.getFuliCardEndTime().getTime() <= now) {
				updateValue(index, questInProgress, 1);
				return 1;
			}
			
			if (flag == 1 && now < roleInfo.getFuliCardEndTime().getTime()) {
				updateValue(index, questInProgress, 1);
				return 1;
			}
		}
		
		updateValue(index, questInProgress, 0);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
