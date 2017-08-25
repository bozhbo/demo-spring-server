package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class StarMoneyCond extends AbstractConditionCheck{
	private long need;
	
	public StarMoneyCond() {
	}

	public StarMoneyCond(long need) {
		this.need = need;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		long need = Long.valueOf(agrs[1]);
		if (need >= 0) {
			return new StarMoneyCond(need);
		}
		return null;
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action, CheckDataChg questInProgress, int index, Object obj) {
		if (roleInfo.getRoleLoadInfo() == null || roleInfo.getRoleLoadInfo().getStarMoney() < need) {
			return ErrorCode.STAR_MONEY_ERROR1;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (need > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.starMoney = need;
			return sub;
		}
		return null;
	}

}
