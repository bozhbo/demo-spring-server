package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;

public class UpStarForHeroCond extends AbstractConditionCheck {

	private int heroNo;
	private int star;
	
	public UpStarForHeroCond() {
	}
	
	public UpStarForHeroCond(int heroNo, int star) {
		this.heroNo = heroNo;
		this.star = star;
	}
	
	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int heroNo = Integer.parseInt(agrs[1]);
		int star = Integer.parseInt(agrs[2]);
		return new UpStarForHeroCond(heroNo, star);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg checkDataChg, int index, Object obj) {
		// 默认2种情况，之前已发生过的和现在发生的
		HeroInfo heroInfo = HeroInfoMap.getHeroInfoByNo(roleInfo.getId(), heroNo);
		if (heroInfo == null) {
			return 0;
		}
		
		if (heroInfo.getStar() >= star) {
			updateValue(index, checkDataChg, star);
			return 1;
		}
		
		updateValue(index, checkDataChg, heroInfo.getStar());
		return 0;
	}

}
