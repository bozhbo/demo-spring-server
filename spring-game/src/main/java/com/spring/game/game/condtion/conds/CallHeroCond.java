package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;

public class CallHeroCond extends AbstractConditionCheck {
	
	private int heroNo;
	
	public CallHeroCond() {
	}
	
	public CallHeroCond(int heroNo) {
		this.heroNo = heroNo;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int heroNo = Integer.parseInt(agrs[1]);
		return new CallHeroCond(heroNo);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		// 默认2种情况，之前已发生过的和现在发生的
		HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
		if (heroInfo != null) {
			updateValue(index, checkDataChg, 1);
			return 1;
		}
		
		return 0;
	}

}
