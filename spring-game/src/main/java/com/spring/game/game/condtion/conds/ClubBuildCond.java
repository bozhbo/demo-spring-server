package com.snail.webgame.game.condtion.conds;

import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleInfo;

public class ClubBuildCond extends AbstractConditionCheck{
	private long need;
	
	public ClubBuildCond() {
	}

	public ClubBuildCond(long need) {
		this.need = need;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		long need = Long.valueOf(agrs[1]);
		if (need >= 0) {
			return new ClubBuildCond(need);
		}
		return null;
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action, CheckDataChg questInProgress, int index, Object obj) {
		RoleClubInfo info = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		if(info == null){
			return ErrorCode.ROLE_CLUB_ERROR_7;
		}
		
		if(info.getBuild() < need){
			return ErrorCode.ROLE_CLUB_ERROR_61;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (need > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.clubBuild = need;
			return sub;
		}
		return null;
	}

}
