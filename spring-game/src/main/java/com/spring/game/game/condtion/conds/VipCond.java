package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * VIP
 * 
 * @author hongfm
 * 
 */
public class VipCond extends AbstractConditionCheck {

	private int vipLv;

	public VipCond() {

	}

	public VipCond(int vipLv) {
		this.vipLv = vipLv;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		int vipLv = Integer.valueOf(agrs[1]);
		if (vipLv >= 0) {
			return new VipCond(vipLv);
		}
		return null;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (roleInfo.getVipLv() < vipLv) {
			return ErrorCode.ROLE_ACCOUNT_ERROR_13;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}

}
