package com.snail.webgame.game.pvp.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.FightMutualRankList;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.GamePvp3Log;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualFightCountResp;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualPrizeResp;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.pvp.competition.end.ComFightEndRe;
import com.snail.webgame.game.pvp.competition.end.ComFightEndResp;
import com.snail.webgame.game.pvp.service.PvpFightEndService;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.SubTeamDuigongXMLInfo;
import com.snail.webgame.game.xml.info.TeamDuigongXMLInfo;

/**
 * 
 * 类介绍:对攻战战斗结束业务类
 *
 * @author zhoubo
 * @2015年6月15日
 */
public class MutualFightEndServiceImpl implements PvpFightEndService {

	private static MutualFightEndServiceImpl service = new MutualFightEndServiceImpl();

	private MutualFightEndServiceImpl() {

	}

	public static PvpFightEndService getInstance() {
		return service;
	}

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void fightEnd(RoleInfo roleInfo, ComFightEndResp comFightEndResp, ComFightEndRe fightEndInfo) {
		MutualPrizeResp resp = null;
		
		GamePvp3Log gamePvp3Log = new GamePvp3Log();
		
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				return;
			}

			logger.info("end mautal fight  , inFight = "+ roleLoadInfo.getInFight() +", role " + roleInfo.getId() + " fight result = " + fightEndInfo.getWinner());

			gamePvp3Log.setAccount(roleInfo.getAccount());
			gamePvp3Log.setRoleId(roleInfo.getId());
			gamePvp3Log.setRoleName(roleInfo.getRoleName());
			gamePvp3Log.setStartTime(new Timestamp(roleLoadInfo.getFightStartTime()));
			gamePvp3Log.setEndTime(new Timestamp(System.currentTimeMillis()));
			gamePvp3Log.setBeforePoint(roleInfo.getScoreValue());
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if(heroInfo != null)
			{
				gamePvp3Log.setHeroNo(heroInfo.getHeroNo());
			}
			
			
			roleLoadInfo.setInFight((byte) 0);
			roleLoadInfo.setFightServer(null);
			roleLoadInfo.setUuid(null);
			roleLoadInfo.setFightStartTime(0);
			
			if (fightEndInfo.getWinner() == 5) {
				return;
			}

			GameLogService.insertPlayActionLog(roleInfo, ActionType.action383.getType(), 0, String.valueOf(fightEndInfo.getWinner()));
			
			// 是否不发奖励
			boolean noAward = false;

			if (roleLoadInfo.getMutualFightLastTime() == 0) {
				// 第一次参加战斗
				roleLoadInfo.setMutualFightLastTime(System.currentTimeMillis());
				roleLoadInfo.setMutualFightCount(1);
			} else {
				if (DateUtil.isSameDay(roleLoadInfo.getMutualFightLastTime(), System.currentTimeMillis())) {
					// 同一天累加次数
					if (roleLoadInfo.getMutualFightCount() < GameValue.MUTUAL_DAILY_FIGHT_COUNTS) {
						// 未满3次
						roleLoadInfo.setMutualFightCount(roleLoadInfo.getMutualFightCount() + 1);
					} else {
						// 同一天超过三次不奖励，不更新库
//						noAward = true;
						return;
					}
				} else {
					// 已经隔天，重新计算
					roleLoadInfo.setMutualFightLastTime(System.currentTimeMillis());
					roleLoadInfo.setMutualFightCount(1);
				}
			}
			
			if (fightEndInfo.getWinner() == 4) {
				// 主动退出没有奖励
				RoleDAO.getInstance().updateRoleMutualCounts(roleLoadInfo);
				
				MutualFightCountResp mutualFightCountResp = new MutualFightCountResp();
				int leftTimes = GameValue.MUTUAL_DAILY_FIGHT_COUNTS - roleLoadInfo.getMutualFightCount();
				mutualFightCountResp.setFightCount(leftTimes);

				// 刷新次数
				SceneService.sendRoleRefreshMsg(mutualFightCountResp, roleInfo.getId(), Command.MUTUAL_FIGHT_COUNTS_RESP);
				return;
			}

			resp = new MutualPrizeResp();
			resp.setFightResult(fightEndInfo.getWinner());

			if (!noAward) {
				// 有奖励
				RoleDAO.getInstance().updateRoleMutualCounts(roleLoadInfo);

				int level = HeroInfoMap.getMainHeroLv(roleInfo.getId());
				SubTeamDuigongXMLInfo subTeamDuigongXMLInfo = TeamDuigongXMLInfo.getSubTeamDuigongXMLInfo(level);

				if (subTeamDuigongXMLInfo != null) {
					String id = null;

					if (fightEndInfo.getWinner() == 3) {
						// 平局
						id = subTeamDuigongXMLInfo.getDrawBag();
					} else if (fightEndInfo.getWinner() == 2) {
						// 胜利
						id = subTeamDuigongXMLInfo.getVictoryBag();
					} else {
						// 失败
						id = subTeamDuigongXMLInfo.getFailureBag();
					}

					// 发送奖励
					List<DropXMLInfo> prizesList = PropBagXMLMap.getPropBagXMLList(id);
					List<BattlePrize> dropList = new ArrayList<BattlePrize>();
					List<BattlePrize> fpPrizeList = new ArrayList<BattlePrize>();
					int check1 = ItemService.addPrizeForPropBag(ActionType.action382.getType(), roleInfo, prizesList, fightEndInfo.getWinner() == 2 ? subTeamDuigongXMLInfo.getCardBag() : null, dropList,
							fpPrizeList, null, null, false);

					if (check1 != 1) {
						logger.error("fightType = 3 , addPrizeForPropBag failed, result = " + check1);
					} else {
						resp.setCount(dropList.size());
						resp.setList(dropList);
						resp.setFpPrizeNum(fpPrizeList.size());
						resp.setFpPrize(fpPrizeList);
					}

					MutualFightCountResp mutualFightCountResp = new MutualFightCountResp();
					int leftTimes = GameValue.MUTUAL_DAILY_FIGHT_COUNTS - roleLoadInfo.getMutualFightCount();
					mutualFightCountResp.setFightCount(leftTimes);

					// 刷新次数
					SceneService.sendRoleRefreshMsg(mutualFightCountResp, roleInfo.getId(), Command.MUTUAL_FIGHT_COUNTS_RESP);
				} else {
					logger.warn("SubTeamDuigongXMLInfo query failed for level = " + level);
				}
			}
			
			if (fightEndInfo.getWinner() == 3) {
				// 平局
				roleInfo.setScoreValue(roleInfo.getScoreValue() + GameValue.MUTUAL_FIGHT_NECK_SCOREVALUE);
			} else if (fightEndInfo.getWinner() == 2) {
				// 胜利
				roleInfo.setScoreValue(roleInfo.getScoreValue() + GameValue.MUTUAL_FIGHT_WON_SCOREVALUE);
			} else {
				// 失败
				if (roleInfo.getScoreValue() > GameValue.MUTUAL_FIGHT_DECREASE_BOTTOM_SCOREVALUE) {
					roleInfo.setScoreValue(roleInfo.getScoreValue() + GameValue.MUTUAL_FIGHT_FAILED_SCOREVALUE);
				}
			}
			
			gamePvp3Log.setResult((byte)fightEndInfo.getWinner());
			gamePvp3Log.setAfterPoint(roleInfo.getScoreValue());
			
			if (roleInfo.getScoreValue() > 10000000) {
				roleInfo.setScoreValue(10000000);
			}
			
			// 更新积分
			if (RoleDAO.getInstance().updateRoleScoreValue(roleInfo)) {
				FightMutualRankList.addSort(roleInfo);
			}
			
			if (roleLoadInfo.getLeaderRoleId() != 0) {
				MutualService.getMutualService().closeMatch(roleLoadInfo, roleInfo.getId(), true, false);
			}
			
			QuestService.checkQuest(roleInfo, ActionType.action383.getType(), 0, true, true);
		}

		// 刷新战斗结果和奖励
		SceneService.sendRoleRefreshMsg(resp, roleInfo.getId(), Command.MUTUAL_FIGHT_END_RESP);

		if (gamePvp3Log.getRoleId() > 0) {
			try{
				GameLogService.insertPVP3Log(gamePvp3Log);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
