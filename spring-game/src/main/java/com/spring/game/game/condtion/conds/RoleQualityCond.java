package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;

public class RoleQualityCond extends AbstractConditionCheck {

	private int quality;

	public RoleQualityCond() {
	}

	public RoleQualityCond(int quality) {
		this.quality = quality;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int quality = Integer.parseInt(agrs[1]);
		return new RoleQualityCond(quality);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		// 任务条件检测
		if (action != ActionType.action76.getType()) {
			return 0;
		}

		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (heroInfo == null) {
			return 0;
		}
		if (heroInfo.getQuality() >= quality) {
			updateValue(index, questInProgress, quality);
			return 1;
		}

		updateValue(index, questInProgress, heroInfo.getQuality());
		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
