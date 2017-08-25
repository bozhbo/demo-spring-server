package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

/**
 * 副本条件
 * 
 * @author tangjq
 * 
 */
public class FinishChallengeCond extends AbstractConditionCheck {

	private int battleNo;// 战场编号(0表示任意)

	public FinishChallengeCond() {
	}

	public FinishChallengeCond(int battleNo) {
		this.battleNo = battleNo;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 5) {
			return null;
		}
		int battleNo = Integer.parseInt(agrs[3]);
		return new FinishChallengeCond(battleNo);
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		if (roleLoadInfo != null) {
			if (roleLoadInfo.getBattleList() == null || !roleLoadInfo.getBattleList().contains(battleNo)) {
				return ErrorCode.ROLE_FB_ERROR_1;
			}
		} else {
			return ErrorCode.ROLE_FB_ERROR_1;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
}
