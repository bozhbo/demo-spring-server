package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 玩家等级,只在玩家升级后触发
 */
public class RoleLvCond extends AbstractConditionCheck {

	private int level;

	public RoleLvCond() {
	}

	public RoleLvCond(int level) {
		this.level = level;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int level = Integer.parseInt(agrs[1]);
		return new RoleLvCond(level);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		int roleLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
		if (roleLv >= level) {
			updateValue(index, questInProgress, level);
			return 1;
		}
		
		updateValue(index, questInProgress, roleLv);
		return ErrorCode.ROLE_LEVEL_ERROR_2;
	}

	public int getLevel() {
		return level;
	}
}
