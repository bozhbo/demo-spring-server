package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 竞技场，完成挑战次数(竞技场战斗结束时，触发检测)
 * 
 * @author nijp
 * 
 */
public class ArenaCond extends AbstractConditionCheck {

	private int num;

	public ArenaCond() {
	}

	public ArenaCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new ArenaCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {

		// 任务条件检测
		if (action != ActionType.action142.getType() || questInProgress == null) {
			return 0;
		}
		
		// ----------------------------------
		// 不与任务关联
//		int value = 0;
//		
//		FightArenaInfo arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleInfo.getId());
//		if (arenaInfo != null) {
//			value = arenaInfo.getFightNum();
//		}
		// ----------------------------------
		
		int value = (int) questInProgress.getSpecValue(index);
		value ++;

		if (value >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}

		updateValue(index, questInProgress, value);
		
		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
