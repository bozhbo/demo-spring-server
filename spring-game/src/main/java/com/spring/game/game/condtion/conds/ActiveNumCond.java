package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

public class ActiveNumCond extends AbstractConditionCheck {

	private int num;
	
	public ActiveNumCond() {
	}
	
	public ActiveNumCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new ActiveNumCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return 0;
		}
		
		int currVal = 0;
		if (roleLoadInfo.getLastActiveChgTime() != null 
				&& DateUtil.isSameDay(System.currentTimeMillis(), roleLoadInfo.getLastActiveChgTime().getTime())) {
			currVal = roleLoadInfo.getTodayActive();
		}
		
		if (currVal >= num) {
			updateValue(index, checkDataChg, num);
			return 1;
		}
		
		updateValue(index, checkDataChg, currVal);
		return 0;
	}
}
