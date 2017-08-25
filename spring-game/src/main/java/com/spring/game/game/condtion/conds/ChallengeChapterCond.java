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
public class ChallengeChapterCond extends AbstractConditionCheck {

	private int challengeBattleId;// 通关战场id

	public ChallengeChapterCond() {

	}

	public ChallengeChapterCond(int challengeBattleId) {
		this.challengeBattleId = challengeBattleId;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}
		int challengeBattleId = Integer.valueOf(agrs[1]);

		ChallengeChapterCond cond = new ChallengeChapterCond(challengeBattleId);
		return cond;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		if (roleLoadInfo != null) {
			if (roleLoadInfo.getChallengeOpen() != 1) {
				if (roleLoadInfo.getBattleList() == null || !roleLoadInfo.getBattleList().contains(challengeBattleId)) {
					return ErrorCode.ROLE_FB_ERROR_1;
				}
			}
		} else {
			return ErrorCode.ROLE_FB_ERROR_1;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
}
