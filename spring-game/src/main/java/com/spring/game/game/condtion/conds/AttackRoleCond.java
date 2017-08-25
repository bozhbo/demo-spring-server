package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 攻击玩家次数
 * 
 * @author hongfm
 *
 */
public class AttackRoleCond extends AbstractConditionCheck {

	private int num;
	
	public AttackRoleCond() {
		
	}
	
	public AttackRoleCond(int num) {
		this.num = num;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		int num = Integer.valueOf(agrs[1]);
		return new AttackRoleCond(num);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		if (action != ActionType.action14.getType()) {
			return 0;
		}
		
		int attackNum = (Integer) obj;
		int val = (int) questInProgress.getSpecValue(index);
		val += attackNum;
		
		if (val >= num) {
			updateValue(index, questInProgress, num);
			return 1;
		}
		
		updateValue(index, questInProgress, val);
		return 0;
		
	}

}
