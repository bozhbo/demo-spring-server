package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 跨服币
 * 
 * @author hongfm
 * 
 */
public class TeamMoneyCond extends AbstractConditionCheck {

	private long needMoney;

	public TeamMoneyCond() {

	}

	public TeamMoneyCond(long needMoney) {
		this.needMoney = needMoney;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		long needMondy = Long.valueOf(agrs[1]);
		if (needMondy >= 0) {
			return new TeamMoneyCond(needMondy);
		}
		return null;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action, CheckDataChg questInProgress, int index, Object obj) {
		if (roleInfo.getRoleLoadInfo() == null || roleInfo.getRoleLoadInfo().getTeamMoney() < needMoney) {
			return ErrorCode.ROLE_MONEY_ERROR_7;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (needMoney > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.upTeamMoney = needMoney;
			return sub;
		}
		return null;
	}

}
