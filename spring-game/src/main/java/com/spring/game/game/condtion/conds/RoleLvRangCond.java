package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

public class RoleLvRangCond extends AbstractConditionCheck {
	
	private int prevLv;
	private int nextLv;
	
	public RoleLvRangCond() {
	}
	
	public RoleLvRangCond(int prevLv, int nextLv) {
		this.prevLv = prevLv;
		this.nextLv = nextLv;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int prevLv = Integer.parseInt(agrs[1]);
		int nextLv = Integer.parseInt(agrs[2]);
		return new RoleLvRangCond(prevLv, nextLv);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		int roleLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
		if (prevLv <= roleLv && roleLv <= nextLv) {
			updateValue(index, checkDataChg, roleLv);
			return 1;
		}
		
		updateValue(index, checkDataChg, roleLv);
		return ErrorCode.ROLE_LEVEL_ERROR_2;
	}

}
