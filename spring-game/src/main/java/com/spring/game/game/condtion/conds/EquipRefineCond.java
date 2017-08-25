package com.snail.webgame.game.condtion.conds;

import java.util.Map;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;

public class EquipRefineCond extends AbstractConditionCheck {
	private int checkLv;
	private int equipNum;
	
	public EquipRefineCond() {
		
	}
	
	public EquipRefineCond(int checkLv, int equipNum) {
		this.checkLv = checkLv;
		this.equipNum = equipNum;
	}
	
	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int checkLv = Integer.parseInt(agrs[1]);
		int equipNum = Integer.parseInt(agrs[2]);
		return new EquipRefineCond(checkLv, equipNum);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (questInProgress == null 
				|| (action != 0 && action != ActionType.action181.getType() && action != ActionType.action87.getType())) {
			return 0;
		}

		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null) {
			return 0;
		}
		
		int curVal = (int) questInProgress.getSpecValue(index);
		// 这个值用于显示 表示当前最大值
		int maxVal = 0;
		
		int flag = 0;
		// 背包中的装备
		if (roleLoadInfo.getBagEquipMap() != null && !roleLoadInfo.getBagEquipMap().isEmpty()) {
			for (EquipInfo equipInfo : roleLoadInfo.getBagEquipMap().values()) {
				if (equipInfo.getRefineLv() >= checkLv) {
					flag++;
				}
				
				if (equipInfo.getRefineLv() > maxVal) {
					maxVal = equipInfo.getRefineLv();
				}
			}
		}
		
		// 装备在英雄身上的装备
		Map<Integer, HeroInfo> heroInfoMap = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
		if (heroInfoMap != null && !heroInfoMap.isEmpty()) {
			for (HeroInfo heroInfo : heroInfoMap.values()) {
				for (EquipInfo equipInfo : heroInfo.getEquipMap().values()) {
					if (equipInfo.getRefineLv() >= checkLv) {
						flag++;
					}
					
					if (equipInfo.getRefineLv() > maxVal) {
						maxVal = equipInfo.getRefineLv();
					}
				}
			}
		}
		
		if (flag >= equipNum) {
			updateValue(index, questInProgress, checkLv);
			return 1;
		}
		
		if (maxVal > curVal) {
			updateValue(index, questInProgress, maxVal);
		}
		
		return ErrorCode.ROLE_TASK_ERROR_2;
	}
}
