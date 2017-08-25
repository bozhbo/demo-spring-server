package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class ReachCityCond extends AbstractConditionCheck {
	
	private String cityNo;
	private int time;// 次数
	
	public ReachCityCond() {
	}
	
	public ReachCityCond(String cityNo, int time) {
		this.cityNo = cityNo;
		this.time = time;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		String cityNo = agrs[1];
		int time = Integer.parseInt(agrs[2]);
		return new ReachCityCond(cityNo, time);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		if (action != ActionType.action30.getType()) {
			return 0;
		}
		
		int val = (int) questInProgress.getSpecValue(index);
		if (cityNo.equalsIgnoreCase(String.valueOf(obj))) {
			val++;
		}
		
		if (val >= time) {
			updateValue(index, questInProgress, time);
			return 1;
		}
		
		updateValue(index, questInProgress, val);
		return 0;
	}
}
