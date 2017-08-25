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

public class HeroSkillLvCond extends AbstractConditionCheck {
	private int skillLv;
	private int heroNum;
	
	public HeroSkillLvCond() {
		
	}
	
	public HeroSkillLvCond(int skillLv, int heroNum) {
		this.skillLv = skillLv;
		this.heroNum = heroNum;
	}
	
	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int skillLv = Integer.parseInt(agrs[1]);
		int heroNum = Integer.parseInt(agrs[2]);
		return new HeroSkillLvCond(skillLv, heroNum);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		// 任务条件检测
		if ((action != 0 && action != ActionType.action181.getType() && action !=ActionType.action75.getType())
				|| questInProgress == null) {
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
		int tempHeroMaxVal = 0;// 单个英雄当前检测条件最大值
		for (HeroInfo heroInfo : heroInfoMap.values()) {
			if (heroInfo.getDeployStatus() == 1) {
				continue;
			}
			
			Map<Integer, Integer> skillMap = HeroService.getSkillMap(heroInfo);
			if (skillMap == null || skillMap.isEmpty()) {
				continue;
			}
			
			for(int heroSkillLv : skillMap.values()) {
				
				if (heroSkillLv > tempHeroMaxVal) {
					tempHeroMaxVal = heroSkillLv;
				}
				
			}
			
			if (tempHeroMaxVal > maxVal) {
				maxVal = tempHeroMaxVal;
			}
			
			if (tempHeroMaxVal >= skillLv) {
				flag++;
			}
			
		}
		
		if (flag >= heroNum) {
			updateValue(index, questInProgress, skillLv);
			return 1;
		}
		
		if (maxVal > curVal) {
			updateValue(index, questInProgress, maxVal);
		}
		
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
