package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 金子条件
 * 
 * @author tangjq
 * 
 */
public class CoinCond extends AbstractConditionCheck {

	private long needCoin;

	public CoinCond() {

	}

	public CoinCond(long needCoin) {
		this.needCoin = needCoin;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		long needCoin = Long.valueOf(agrs[1]);
		if (needCoin >= 0) {
			return new CoinCond(needCoin);
		}
		return null;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (roleInfo.getCoin() < needCoin) {
			return ErrorCode.ROLE_COIN_ERROR_1;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (needCoin > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.upCoin = needCoin;
			return sub;
		}
		return null;
	}

}
