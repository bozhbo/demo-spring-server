package com.snail.webgame.game.pvp.service.impl;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.pvp.competition.end.ComFightEndRe;
import com.snail.webgame.game.pvp.competition.end.ComFightEndResp;
import com.snail.webgame.game.pvp.service.PvpFightEndService;

/**
 * 
 * 类介绍:大地图战斗结束业务类
 *
 * @author zhoubo
 * @2015年6月15日
 */
public class MapFightEndServiceImpl implements PvpFightEndService {

	private static MapFightEndServiceImpl service = new MapFightEndServiceImpl();

	private MapFightEndServiceImpl() {

	}

	public static PvpFightEndService getInstance() {
		return service;
	}

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void fightEnd(RoleInfo roleInfo, ComFightEndResp comFightEndResp, ComFightEndRe fightEndInfo) {// 判断是否同一势力
		boolean raceFlag = false;
		boolean broadcastFlag = false;// 分别刷新给战斗人，因为公共方法brocastRolePointStatus过滤掉了
		
		if (comFightEndResp.getList().size() == 2) {
			RoleInfo roleInfo1 = RoleInfoMap.getRoleInfo(comFightEndResp.getList().get(0).getRoleId());
			RoleInfo roleInfo2 = RoleInfoMap.getRoleInfo(comFightEndResp.getList().get(1).getRoleId());
			if (roleInfo1.getRoleRace() != roleInfo2.getRoleRace()) {
				raceFlag = true;
			}
			broadcastFlag = true;
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo != null) {
				roleLoadInfo.setInFight((byte) 0);
				roleLoadInfo.setFightServer(null);
				roleLoadInfo.setUuid(null);
			} else {
				return;
			}

			// 大地图PVP战斗输的玩家直接回城，赢的玩家在原大地图,通过0xCOF更新状态
			if (fightEndInfo.getWinner() == 1) {
				// 输了直接回家
				SceneService1.mapRoleDisappear(roleInfo);
				logger.info("####MapFightEndServiceImpl fight failer,account="+roleInfo.getAccount()+",roleName="+roleInfo.getRoleName()
						+",fightType="+comFightEndResp.getFightType());

				roleInfo.setMapPvpFightTime(new Timestamp(System.currentTimeMillis()));
			} else {
				if (raceFlag && roleLoadInfo.isMapPvpAttack()) {
					// 完成国家任务-消灭敌军任务
					QuestService.checkQuest(roleInfo,ActionType.action14.getType(), 1, true, true);
				}
			}

			roleLoadInfo.setMapPvpAttack(false);
			
			if(comFightEndResp.getTimeoutGameOver() == 1){
				SceneMgtService.mapPVPFightEnd(roleInfo.getId(), 1);
			}
		}

		if(broadcastFlag == true){
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleInfo.getId());
			
			if (pointInfo != null) {
				RoleInfo roleInfo1 = RoleInfoMap.getRoleInfo(comFightEndResp.getList().get(0).getRoleId());
				RoleInfo roleInfo2 = RoleInfoMap.getRoleInfo(comFightEndResp.getList().get(1).getRoleId());
				SceneService1.broadPointStatus(roleInfo1, pointInfo);
				SceneService1.broadPointStatus(roleInfo2, pointInfo);
			}
			broadcastFlag = false;
		}
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action357.getType(), 1, "");
	}
	
}
