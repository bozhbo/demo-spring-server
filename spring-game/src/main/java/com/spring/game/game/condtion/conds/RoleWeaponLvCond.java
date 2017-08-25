package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.RoleWeaponInfo;

public class RoleWeaponLvCond extends AbstractConditionCheck {
	private int checkLv;
	private int weaponNum;
	
	public RoleWeaponLvCond() {
		
	}
	
	public RoleWeaponLvCond(int checkLv, int weaponNum) {
		this.checkLv = checkLv;
		this.weaponNum = weaponNum;
	}
	
	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 3) {
			return null;
		}
		int checkLv = Integer.parseInt(agrs[1]);
		int weaponNum = Integer.parseInt(agrs[2]);
		return new RoleWeaponLvCond(checkLv, weaponNum);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (questInProgress == null 
				|| (action != 0 && action != ActionType.action181.getType() && action != ActionType.action359.getType())) {
			return 0;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null || roleLoadInfo.getRoleWeaponInfoMap() == null || roleLoadInfo.getRoleWeaponInfoMap().isEmpty()) {
			return 0;
		}
		
		int curVal = (int) questInProgress.getSpecValue(index);
		// 这个值用于显示 表示当前最大值
		int maxVal = 0;
		
		int flag = 0;
		for (RoleWeaponInfo roleWeaponInfo : roleLoadInfo.getRoleWeaponInfoMap().values()) {
			if (roleWeaponInfo.getLevel() >= checkLv) {
				flag++;
			}
			
			if (roleWeaponInfo.getLevel() > maxVal) {
				maxVal = roleWeaponInfo.getLevel();
			}
		}
		
		if (flag >= weaponNum) {
			updateValue(index, questInProgress, checkLv);
			return 1;
		}
		
		if (maxVal > curVal) {
			updateValue(index, questInProgress, maxVal);
		}
		
		return ErrorCode.ROLE_TASK_ERROR_2;
	}

}
