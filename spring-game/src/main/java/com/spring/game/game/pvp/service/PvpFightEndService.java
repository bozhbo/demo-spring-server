package com.snail.webgame.game.pvp.service;

import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.pvp.competition.end.ComFightEndRe;
import com.snail.webgame.game.pvp.competition.end.ComFightEndResp;

/**
 * 
 * 类介绍:战斗结束接口
 *
 * @author zhoubo
 * @2015年6月15日
 */
public interface PvpFightEndService {

	public void fightEnd(RoleInfo roleInfo, ComFightEndResp comFightEndResp, ComFightEndRe fightEndInfo);
}
