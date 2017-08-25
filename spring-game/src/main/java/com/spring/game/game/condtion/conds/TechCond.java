package com.snail.webgame.game.condtion.conds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;

public class TechCond extends AbstractConditionCheck {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	private int need;

	public TechCond() {

	}

	public TechCond(int need) {
		this.need = need;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		int need = Integer.valueOf(agrs[1]);
		if (need >= 0) {
			return new TechCond(need);
		}

		return null;
	}

	@Override
	public int checkCond(RoleInfo roleInfo, int action, CheckDataChg questInProgress, int index, Object obj) {

		// 因为现在体力不是实时计算的，所以每用到体力的一刻一定需要把玩家之前时间回复的先加上
		RoleService.timerRecoverTech(roleInfo);

		if (roleInfo.getTech() < need) {
			logger.warn("tech cond check no enough log, roleId : " + roleInfo.getId() + " , curr role tech : " + roleInfo.getTech() + ", need cost tech : " + need);
			return ErrorCode.ROLE_TECH_ERROR_1;
		}

		updateValue(index, questInProgress, 1);
		return 1;
	}

	@Override
	public BaseSubResource sub() {
		if (need > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.upTech = need;
			return sub;
		}
		return null;
	}

}
