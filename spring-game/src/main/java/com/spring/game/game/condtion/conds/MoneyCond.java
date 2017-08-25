package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 银子条件
 * 
 * @author tangjq
 * 
 */
public class MoneyCond extends AbstractConditionCheck {

	private long needMoney;

	public MoneyCond() {

	}

	public MoneyCond(long needMoney) {
		this.needMoney = needMoney;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		long needMondy = Long.valueOf(agrs[1]);
		if (needMondy >= 0) {
			return new MoneyCond(needMondy);
		}
		return null;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action, CheckDataChg questInProgress, int index, Object obj) {
		if (roleInfo.getMoney() < needMoney) {
			return ErrorCode.ROLE_MONEY_ERROR_1;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (needMoney > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.upMoney = needMoney;
			return sub;
		}
		return null;
	}

}
