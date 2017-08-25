package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 大地图某NPC驻足
 * 
 * @author hongfm
 *
 */
public class StayMapNPCCond extends AbstractConditionCheck {

	private String GW;
	
	public StayMapNPCCond() {
		
	}
	
	public StayMapNPCCond(String GW) {
		this.GW = GW;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length < 2) {
			return null;
		}
		String GW =agrs[1];
		return new StayMapNPCCond(GW);
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) 
	{
		if(action != ActionType.action18.getType())
		{
			return 0;
		}
		if(GW.indexOf(String.valueOf(obj))>=0)
		{
			updateValue(index, questInProgress, 1);
			return 1;
		}
		
		return 0;
		
	}

}
