package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class OpActTreasureCond extends AbstractConditionCheck {
	
	private int treasureNum;// 金币10连抽次数

	public OpActTreasureCond() {
	}

	public OpActTreasureCond(int treasureNum) {
		this.treasureNum = treasureNum;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new OpActTreasureCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		if (checkDataChg == null || 
				(action != ActionType.action69.getType() && action != ActionType.action438.getType())) {
			return 0;
		}
		
		int val = (int) checkDataChg.getSpecValue(index);
		if (val >= treasureNum) {
			return 1;
		}
		
		val++;
		if (val >= treasureNum) {
			updateValue(index, checkDataChg, treasureNum);
			return 1;
		}
		
		updateValue(index, checkDataChg, val);
		return 0;
	}

}
