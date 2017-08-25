package com.snail.webgame.game.condtion.conds;

import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;

public class HeroLvCond extends AbstractConditionCheck {

	private int lv;
	private int num;

	public HeroLvCond() {
	}

	public HeroLvCond(int lv, int num) {
		this.lv = lv;
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int lv = Integer.parseInt(agrs[1]);
		int num = Integer.parseInt(agrs[2]);
		return new HeroLvCond(lv, num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		// 任务条件检测
		if (action != 0 && action != ActionType.action181.getType() 
			&& action != ActionType.action72.getType() && action != ActionType.action74.getType()
			&& action != ActionType.action66.getType() && action != ActionType.action67.getType()
			&& action != ActionType.action68.getType() && action != ActionType.action69.getType()) {
			return 0;
		}
		
		Map<Integer, HeroInfo> heroInfoMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
		if (heroInfoMap == null || heroInfoMap.isEmpty()) {
			return 0;
		}
		
		int curVal = (int) questInProgress.getSpecValue(index);
		// 这个值用于显示 表示当前最大值
		int maxVal = 0;
		
		int flag = 0;
		for(HeroInfo heroInfo : heroInfoMap.values()) {
			if (heroInfo.getDeployStatus() == 1) {
				continue;
			}
			
			if (heroInfo.getHeroLevel() >= lv) {
				flag++;
			}
			
			if (heroInfo.getHeroLevel() > maxVal) {
				maxVal = heroInfo.getHeroLevel();
			}
		}
		
		if (flag >= num) {
			updateValue(index, questInProgress, lv);
			return 1;
		}

		if (maxVal > curVal) {
			updateValue(index, questInProgress, maxVal);
		}

		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
