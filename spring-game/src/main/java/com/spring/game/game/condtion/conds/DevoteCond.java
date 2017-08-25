package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class DevoteCond extends AbstractConditionCheck {

	private long needDevote;

	public DevoteCond() {

	}

	public DevoteCond(long needDevote) {
		this.needDevote = needDevote;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		long needDevote = Long.valueOf(agrs[1]);
		if (needDevote >= 0) {
			return new DevoteCond(needDevote);
		}
		return null;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
//		if (roleInfo.getRoleLoadInfo() == null && roleInfo.getRoleLoadInfo().getDevotePoint() < needDevote) {
//			return ErrorCode.ROLE_MONEY_ERROR_6;
//		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (needDevote > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.upDevote = needDevote;
			return sub;
		}
		return null;
	}

}
