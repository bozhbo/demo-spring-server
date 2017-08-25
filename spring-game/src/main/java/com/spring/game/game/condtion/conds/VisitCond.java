package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class VisitCond extends AbstractConditionCheck {

	public VisitCond() {
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		return new VisitCond();
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null) {
			return 0;
		}
		
		updateValue(index, checkDataChg, 1);
		return 1;
	}

}
