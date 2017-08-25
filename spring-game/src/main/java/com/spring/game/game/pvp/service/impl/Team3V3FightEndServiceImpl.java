package com.snail.webgame.game.pvp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualPrizeResp;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;
import com.snail.webgame.game.protocal.fight.team.refresh.Team3V3FightCountResp;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.pvp.competition.end.ComFightEndRe;
import com.snail.webgame.game.pvp.competition.end.ComFightEndResp;
import com.snail.webgame.game.pvp.service.PvpFightEndService;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.Team3V3XMLInfo;

/**
 * 
 * 类介绍:竞技场3V3战斗结束业务类
 *
 * @author xiasd
 * @2015年10月10日
 */
public class Team3V3FightEndServiceImpl implements PvpFightEndService {

	private static Team3V3FightEndServiceImpl service = new Team3V3FightEndServiceImpl();

	private Team3V3FightEndServiceImpl() {

	}

	public static PvpFightEndService getInstance() {
		return service;
	}

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void fightEnd(RoleInfo roleInfo, ComFightEndResp comFightEndResp, ComFightEndRe fightEndInfo) {

		MutualPrizeResp resp = null;
		
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				return;
			}

			logger.info("end 3V3 fight , inFight = "+ roleLoadInfo.getInFight() +", role " + roleInfo.getId() + ", fight result = " + fightEndInfo.getWinner());
			
			roleLoadInfo.setInFight((byte) 0);
			roleLoadInfo.setFightServer(null);
			roleLoadInfo.setUuid(null);
			roleLoadInfo.setFightStartTime(0);

			if (fightEndInfo.getWinner() == 5) {
				return;
			}

			// 是否不发奖励
			boolean noAward = false;

			if (roleLoadInfo.getTeam3V3FightLastTime() == 0) {
				// 第一次参加战斗
				roleLoadInfo.setTeam3V3FightLastTime(System.currentTimeMillis());
				roleLoadInfo.setTeam3V3Times((byte) 1);
			} else {
				if (DateUtil.isSameDay(roleLoadInfo.getTeam3V3FightLastTime(), System.currentTimeMillis())) {
					// 同一天累加次数
					if (roleLoadInfo.getTeam3V3Times() < GameValue.TEAM_3V3_COUNT) {
						// 未满3次
						roleLoadInfo.setTeam3V3Times((byte) (roleLoadInfo.getTeam3V3Times() + 1));
					} else {
						// 同一天超奖励次数，不更新库
//						noAward = true;
						return;
					}
				} else {
					// 已经隔天，重新计算
					roleLoadInfo.setTeam3V3FightLastTime(System.currentTimeMillis());
					roleLoadInfo.setTeam3V3Times((byte) 1);
				}
			}
			
			if (fightEndInfo.getWinner() == 4) {
				// 主动退出没有奖励
				RoleDAO.getInstance().updateRoleTeam3V3TimesAndLastTime(roleInfo.getId(), roleLoadInfo.getTeam3V3Times(), roleLoadInfo.getTeam3V3FightLastTime());
				
				Team3V3FightCountResp team3V3FightCountResp = new Team3V3FightCountResp();
				team3V3FightCountResp.setTeam3V3Count(roleLoadInfo.getTeam3V3Times());

				// 刷新次数
				SceneService.sendRoleRefreshMsg(team3V3FightCountResp, roleInfo.getId(), Command.TEAM_3V3_FIGHT_COUNTS_RESP);
				return;
			}

			resp = new MutualPrizeResp();
			resp.setFightResult(fightEndInfo.getWinner());

			if (!noAward) {
				// 有奖励
				RoleDAO.getInstance().updateRoleTeam3V3TimesAndLastTime(roleInfo.getId(), roleLoadInfo.getTeam3V3Times(), roleLoadInfo.getTeam3V3FightLastTime());

				String id = null;

				if (fightEndInfo.getWinner() == 3) {
					// 平局
					id = Team3V3XMLInfo.getDrawBag();
				} else if (fightEndInfo.getWinner() == 2) {
					// 胜利
					id = Team3V3XMLInfo.getVictoryBag();
				} else {
					// 失败
					id = Team3V3XMLInfo.getFailureBag();
				}

				// 发送奖励
				List<DropXMLInfo> prizesList = PropBagXMLMap.getPropBagXMLList(id);
				List<BattlePrize> dropList = new ArrayList<BattlePrize>();
				List<BattlePrize> fpPrizeList = new ArrayList<BattlePrize>();
				int check1 = ItemService.addPrizeForPropBag(ActionType.action488.getType(), roleInfo, prizesList, fightEndInfo.getWinner() == 2 ? Team3V3XMLInfo.getCardBag() : null, dropList,
						fpPrizeList, null, null, false);

				if (check1 != 1) {
					logger.error("fightType = 12 , addPrizeForPropBag failed, result = " + check1);
				} else {
					resp.setCount(dropList.size());
					resp.setList(dropList);
					resp.setFpPrizeNum(fpPrizeList.size());
					resp.setFpPrize(fpPrizeList);
				}

				Team3V3FightCountResp team3V3FightCountResp = new Team3V3FightCountResp();
				team3V3FightCountResp.setTeam3V3Count(roleLoadInfo.getTeam3V3Times());

				// 刷新次数
				SceneService.sendRoleRefreshMsg(team3V3FightCountResp, roleInfo.getId(), Command.TEAM_3V3_FIGHT_COUNTS_RESP);
			}
			
			if(fightEndInfo != null)
			{
				GameLogService.insertPlayActionLog(roleInfo, ActionType.action488.getType(), 0, String.valueOf(fightEndInfo.getWinner()));
			}
			
			if (roleLoadInfo.getLeaderRoleId() != 0) {
				MutualService.getMutualService().closeMatch(roleLoadInfo, roleInfo.getId(), true, false);
			}
			
			QuestService.checkQuest(roleInfo, ActionType.action488.getType(), 0, true, true);
		}

		// 刷新战斗结果和奖励
		SceneService.sendRoleRefreshMsg(resp, roleInfo.getId(), Command.MUTUAL_FIGHT_END_RESP);
	}
}
