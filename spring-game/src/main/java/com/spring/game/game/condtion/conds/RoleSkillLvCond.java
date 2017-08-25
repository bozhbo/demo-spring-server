package com.snail.webgame.game.condtion.conds;

import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.hero.service.HeroService;

public class RoleSkillLvCond extends AbstractConditionCheck {
	private int checkLv;
	
	public RoleSkillLvCond() {
		
	}
	
	public RoleSkillLvCond(int checkLv) {
		this.checkLv = checkLv;
	}
	
	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int checkLv = Integer.parseInt(agrs[1]);
		return new RoleSkillLvCond(checkLv);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		// 任务条件检测
		if ((action != 0 && action != ActionType.action181.getType() && action != ActionType.action90.getType()) 
				|| questInProgress == null) {
			return 0;
		}
		
		int curVal = (int) questInProgress.getSpecValue(index);
		int maxVal = 0;

		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null)
		{
			return 0;
		}
		
		Map<Integer, Integer> skillMap = HeroService.getSkillMap(heroInfo);
		if (skillMap != null && !skillMap.isEmpty()) {
			for(int skillLv : skillMap.values()) {
				
				if (skillLv > maxVal) {
					maxVal = skillLv;
				}
				
			}
		}
		
		if (maxVal >= checkLv) {
			updateValue(index, questInProgress, checkLv);
			return 1;
		}
		
		if (maxVal > curVal) {
			updateValue(index, questInProgress, maxVal);
		}
		
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
