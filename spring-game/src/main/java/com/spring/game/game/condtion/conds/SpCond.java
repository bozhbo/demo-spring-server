package com.snail.webgame.game.condtion.conds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;

/**
 * 体力值条件
 * 
 * @author tangjq
 * 
 */
public class SpCond extends AbstractConditionCheck {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private int needSp;

	public SpCond() {

	}

	public SpCond(int needSp) {
		this.needSp = needSp;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		int needSp = Integer.valueOf(agrs[1]);
		if (needSp >= 0) {
			return new SpCond(needSp);
		}

		return null;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		// 因为现在体力不是实时计算的，所以每用到体力的一刻一定需要把玩家之前时间回复的先加上
		if (roleInfo.getSp() < roleInfo.getSpRecoverLimit()) {
			RoleService.timerRecoverSp(roleInfo);
		}
				
		if (roleInfo.getSp() < needSp) {
			logger.warn("sp cond check no enough log, roleId : " + roleInfo.getId() + " , curr role sp : " + roleInfo.getSp() + ", need cost sp : " + needSp);
			return ErrorCode.ROLE_SP_ERROR_1;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (needSp > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.upSp = needSp;
			return sub;
		}
		return null;
	}

}
