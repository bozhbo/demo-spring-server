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
public class KuafuMoneyCond extends AbstractConditionCheck {

	private long needMoney;

	public KuafuMoneyCond() {

	}

	public KuafuMoneyCond(long needMoney) {
		this.needMoney = needMoney;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		long needMondy = Long.valueOf(agrs[1]);
		if (needMondy >= 0) {
			return new KuafuMoneyCond(needMondy);
		}
		return null;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action, CheckDataChg questInProgress, int index, Object obj) {
		if (roleInfo.getRoleLoadInfo() == null || roleInfo.getRoleLoadInfo().getKuafuMoney() < needMoney) {
			return ErrorCode.ROLE_MONEY_ERROR_4;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (needMoney > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.upKuafuMoney = needMoney;
			return sub;
		}
		return null;
	}

}
