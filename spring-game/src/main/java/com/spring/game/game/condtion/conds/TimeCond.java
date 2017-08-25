package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class TimeCond extends AbstractConditionCheck {

	private String beginTime;
	private String endTime;

	public TimeCond() {

	}

	public TimeCond(String beginTime, String endTime) {
		this.beginTime = beginTime;
		this.endTime = endTime;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {

		if (agrs.length < 3) {
			return null;
		}

		String beginTime = agrs[1];// mm:ss
		String endTime = agrs[2];// mm:ss
		if (DateUtil.verifyHMTime(beginTime) && DateUtil.verifyHMTime(endTime)) {
			return new TimeCond(beginTime, endTime);
		}
		return null;
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		long begin = DateUtil.getTodayHMTime(beginTime);
		long end = DateUtil.getTodayHMTime(endTime);
		if (begin != 0 && end != 0) {
			long now = System.currentTimeMillis();
			if (begin <= now && now <= end) {
				
				updateValue(index, questInProgress, 1);
				return 1;
			}
		}
		return 0;
	}
	
}
