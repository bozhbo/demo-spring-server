package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class Pvp3MoneyCond extends AbstractConditionCheck{
	private long pvp3Money;
	
	public Pvp3MoneyCond() {
	}

	public Pvp3MoneyCond(long pvp3Money) {
		this.pvp3Money = pvp3Money;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		long pvp3Money = Long.valueOf(agrs[1]);
		if (pvp3Money >= 0) {
			return new Pvp3MoneyCond(pvp3Money);
		}
		return null;
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action, CheckDataChg questInProgress, int index, Object obj) {
		if (roleInfo.getRoleLoadInfo() == null || roleInfo.getRoleLoadInfo().getPvp3Money() < pvp3Money) {
			return ErrorCode.ROLE_PVP3_MOENY_ERROR_1;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (pvp3Money > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.pvp3Money = pvp3Money;
			return sub;
		}
		return null;
	}

}
