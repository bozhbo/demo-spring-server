package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class ShowTimeCardCond extends AbstractConditionCheck {
	public int cardType;

	public ShowTimeCardCond() {
	}
	
	public ShowTimeCardCond(int cardType) {
		this.cardType = cardType;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int cardType = Integer.parseInt(agrs[1]);
		return new ShowTimeCardCond(cardType);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		if (roleInfo.getCardType() == cardType && cardType == 0) {
			updateValue(index, questInProgress, 1);
			return 1;
		}
		
		if (roleInfo.getCardEndTime() != null) {
			if (roleInfo.getCardType() == cardType && roleInfo.getCardEndTime().getTime() > System.currentTimeMillis()) {
				updateValue(index, questInProgress, 1);
				return 1;
			}
			
			// 卡过期了
			if (roleInfo.getCardEndTime().getTime() <= System.currentTimeMillis() && cardType == 0) {
				updateValue(index, questInProgress, 1);
				return 1;
			}
		}
		
		updateValue(index, questInProgress, 0);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
