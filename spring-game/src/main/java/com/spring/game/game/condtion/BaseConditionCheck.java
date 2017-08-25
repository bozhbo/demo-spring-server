package com.snail.webgame.game.condtion;

import com.snail.webgame.game.info.RoleInfo;

/**
 * 条件检测
 * 
 * @author nijp
 *
 */
public interface BaseConditionCheck {
	
	/**
	 * 解析String
	 * 
	 * @return
	 */
	public AbstractConditionCheck generate(String[] agrs);

	/**
	 * 检测条件是否满足，并处理中间状态。
	 * 
	 * @param roleInfo
	 * @param action 触发行为
	 * @param checkDataChg 记录检测中间状态的对象
	 * @param index 检测索引
	 * @param obj
	 * @return 1-成功 其他 ErrorCode
	 */
	public int checkCond(RoleInfo roleInfo, int action, CheckDataChg checkDataChg, int index, Object obj);
	
}
