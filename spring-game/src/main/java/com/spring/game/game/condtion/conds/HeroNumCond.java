package com.snail.webgame.game.condtion.conds;

import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;

public class HeroNumCond extends AbstractConditionCheck {

	private int num;

	public HeroNumCond() {
	}

	public HeroNumCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.parseInt(agrs[1]);
		return new HeroNumCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		// 任务条件检测
		if (action != 0 && action != ActionType.action181.getType()
				&& action != ActionType.action74.getType()
				&& action != ActionType.action66.getType() && action != ActionType.action67.getType()
				&& action != ActionType.action68.getType()&& action != ActionType.action69.getType()) {
			return 0;
		}

		int currHeroNum = 0;
		Map<Integer, HeroInfo> allHeroMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
		if (allHeroMap != null && allHeroMap.size() > 0) {
			// 副将个数
			currHeroNum = allHeroMap.size() - 1;
		}
		
		if (currHeroNum >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}

		updateValue(index, questInProgress, currHeroNum);
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
