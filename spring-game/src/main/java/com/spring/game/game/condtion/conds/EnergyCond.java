package com.snail.webgame.game.condtion.conds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.CheckDataChg;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;

/**
 * 精力值条件
 * 
 * @author tangjq
 * 
 */
public class EnergyCond extends AbstractConditionCheck {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private int needEnergy;

	public EnergyCond() {

	}

	public EnergyCond(int needEnergy) {
		this.needEnergy = needEnergy;
	}

	@Override
	public AbstractConditionCheck generate(String[] agrs) {
		if (agrs.length > 2) {
			return null;
		}

		int needEnergy = Integer.valueOf(agrs[1]);
		if (needEnergy >= 0) {
			return new EnergyCond(needEnergy);
		}

		return null;
	}
	
	@Override
	public int checkCond(RoleInfo roleInfo, int action,
			CheckDataChg questInProgress, int index, Object obj) {
		
		// 因为现在精力不是实时计算的，所以每用到精力的一刻一定需要把玩家之前时间回复的先加上
		if (roleInfo.getEnergy() < roleInfo.getEnergyRecoverLimit()) {
			RoleService.timerRecoverEnergy(roleInfo);
		}
				
		if (roleInfo.getEnergy() < needEnergy) {
			logger.warn("sp cond check no enough log, roleId : " + roleInfo.getId() + " , curr role energy : " + roleInfo.getEnergy() + ", need cost sp : " + needEnergy);
			return ErrorCode.ROLE_ENERGY_ERROR_1;
		}
		
		updateValue(index, questInProgress, 1);
		return 1;
	}
	
	@Override
	public BaseSubResource sub() {
		if (needEnergy > 0) {
			BaseSubResource sub = new BaseSubResource();
			sub.upEnergy = needEnergy;
			return sub;
		}
		return null;
	}

}
