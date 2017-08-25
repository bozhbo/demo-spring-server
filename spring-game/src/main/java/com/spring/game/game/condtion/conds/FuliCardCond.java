package com.snail.webgame.game.condtion.conds;

import java.util.Date;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class FuliCardCond extends AbstractConditionCheck {
	public int flag;

	public FuliCardCond() {
	}
	
	public FuliCardCond(int flag) {
		this.flag = flag;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int flag = Integer.parseInt(agrs[1]);
		return new FuliCardCond(flag);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		long now = System.currentTimeMillis();
		if (roleInfo.getFuliCardEndTime() != null) {
			if (now < roleInfo.getFuliCardEndTime().getTime()) {
				int remainDay = (int) DateUtil.getTwoDateSubNum(new Date(), roleInfo.getFuliCardEndTime());
				updateValue(index, questInProgress, remainDay);
				return 1;
			}
		}
		
		updateValue(index, questInProgress, 0);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
