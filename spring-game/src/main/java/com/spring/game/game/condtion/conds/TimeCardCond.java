package com.snail.webgame.game.condtion.conds;

import java.util.Date;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class TimeCardCond extends AbstractConditionCheck {
	public int cardType;

	public TimeCardCond() {
	}
	
	public TimeCardCond(int cardType) {
		this.cardType = cardType;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int cardType = Integer.parseInt(agrs[1]);
		return new TimeCardCond(cardType);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		if (roleInfo.getCardEndTime() != null) {
			if (roleInfo.getCardType() >= cardType && roleInfo.getCardEndTime().getTime() > System.currentTimeMillis()) {
				int remainDay = (int) DateUtil.getTwoDateSubNum(new Date(), roleInfo.getCardEndTime());
				updateValue(index, questInProgress, remainDay);
				return 1;
			}
		}
		
		updateValue(index, questInProgress, 0);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
