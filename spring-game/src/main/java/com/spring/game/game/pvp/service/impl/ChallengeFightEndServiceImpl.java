package com.snail.webgame.game.pvp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualPrizeResp;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;
import com.snail.webgame.game.protocal.fight.team.refresh.TeamFightCountResp;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.pvp.competition.end.ComFightEndRe;
import com.snail.webgame.game.pvp.competition.end.ComFightEndResp;
import com.snail.webgame.game.pvp.competition.request.TeamChallengeVo;
import com.snail.webgame.game.pvp.service.PvpFightEndService;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.TeamChallengeXmlInfo;
import com.snail.webgame.game.xml.load.TeamChallengeXmlLoader;

/**
 * 
 * 类介绍:组队副本战斗结束业务类
 *
 * @author xiasd
 * @2015年10月10日
 */
public class ChallengeFightEndServiceImpl implements PvpFightEndService {

	private static ChallengeFightEndServiceImpl service = new ChallengeFightEndServiceImpl();

	private ChallengeFightEndServiceImpl() {

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

			roleLoadInfo.setInFight((byte) 0);
			roleLoadInfo.setFightServer(null);
			roleLoadInfo.setUuid(null);
			roleLoadInfo.setFightStartTime(0);

			if (fightEndInfo.getWinner() == 5) {
				return;
			}

			resp = new MutualPrizeResp();
			resp.setFightResult(fightEndInfo.getWinner());
			TeamChallengeVo teamChallengeVo = fightEndInfo.getTeamChallengeVo();
			
			if(teamChallengeVo == null){
				return;
			}
			
			logger.info("end teamChallenge fight , inFight = "+ roleLoadInfo.getInFight() +", role " + roleInfo.getId() + " fight result = " + fightEndInfo.getWinner()
					+",challengeNo="+teamChallengeVo.getDuplicatId());
			
			TeamChallengeXmlInfo teamChallengeXmlInfo = TeamChallengeXmlLoader.getTeamChallenge(fightEndInfo.getTeamChallengeVo().getDuplicatId());

			if (teamChallengeXmlInfo != null) {
				long currTime =  System.currentTimeMillis();

				if (fightEndInfo.getWinner() == 2 || fightEndInfo.getWinner() == 4) {
					// 胜利和主动退出扣次数
					int getPrizecount = roleLoadInfo.getTeamChallengeTimesByDupId(fightEndInfo.getTeamChallengeVo().getDuplicatId());// 已经获得的奖励次数
					
					if(getPrizecount < teamChallengeXmlInfo.getTimes() && roleLoadInfo.getTeamChallengeAllTimes() < GameValue.TEAM_CHALLENGE_COUNT){
						// 次数不超过指定奖励次数才能奖励
						// 先扣次数，再发奖励
						if(roleLoadInfo.getTeamChallengeTimes() == null){
							roleLoadInfo.setTeamChallengeTimes(new HashMap<Integer, Integer>());
						}
						roleLoadInfo.getTeamChallengeTimes().put(fightEndInfo.getTeamChallengeVo().getDuplicatId(), getPrizecount + 1);
						long lastTime = roleLoadInfo.getTeamChallengeFightLastTime();
						roleLoadInfo.setTeamChallengeFightLastTime(currTime);
						
						//新通关的组队副本记录,默认成功
						if(roleLoadInfo.getTeamChallengeStr() == null){
							roleLoadInfo.setTeamChallengeStr(String.valueOf(fightEndInfo.getTeamChallengeVo().getDuplicatId()));
						}else if(roleLoadInfo.getTeamChallengeStr().indexOf(String.valueOf(fightEndInfo.getTeamChallengeVo().getDuplicatId())) == -1 && fightEndInfo.getWinner() == 2)
						{
							roleLoadInfo.setTeamChallengeStr(roleLoadInfo.getTeamChallengeStr()+","+fightEndInfo.getTeamChallengeVo().getDuplicatId());
						}
						
						if(!RoleDAO.getInstance().updateRoleTeamChallengeTimesAndLastTime(roleInfo.getId(), roleLoadInfo.getTeamChallengeTimes(), currTime,roleLoadInfo.getTeamChallengeStr())){
							// 数据库不成功，rollback
							roleLoadInfo.getTeamChallengeTimes().put(fightEndInfo.getTeamChallengeVo().getDuplicatId(), getPrizecount);
							roleLoadInfo.setTeamChallengeFightLastTime(lastTime);
						}
						else
						{
							TeamFightCountResp teamFightCountResp = new TeamFightCountResp();
							teamFightCountResp.setTeamChallengeCount(roleLoadInfo.getTeamChallengeAllTimesStr());
							// 刷新次数
							SceneService.sendRoleRefreshMsg(teamFightCountResp, roleInfo.getId(), Command.TEAM_FIGHT_COUNTS_RESP);
							
							if(fightEndInfo.getWinner() == 2){
								// 胜利发奖励
								String id = teamChallengeXmlInfo.getBag();
								// 发送奖励
								List<DropXMLInfo> prizesList = PropBagXMLMap.getPropBagXMLList(id);
								List<BattlePrize> dropList = new ArrayList<BattlePrize>();
								int check1 = ItemService.addPrizeForPropBag(ActionType.action490.getType(), roleInfo, prizesList, null, dropList,
										null, null, null, false);
								
								if (check1 != 1) {
									logger.error("fightType = 6 , addPrizeForPropBag failed, result = " + check1);
								} else {
									resp.setCount(dropList.size());
									resp.setList(dropList);
								}
							}
						}
					}
				} else if(fightEndInfo.getWinner() == 1){
					// 输了，更新时间
					
					if(RoleDAO.getInstance().updateRoleTeamChallengeTimesAndLastTime(roleInfo.getId(), roleLoadInfo.getTeamChallengeTimes(), currTime,roleLoadInfo.getTeamChallengeStr())){
						// 数据库不成功，rollback
						roleLoadInfo.setTeamChallengeFightLastTime(currTime);
					}
				}
			} else {
				logger.warn("teamChallengeXmlInfo query failed for duplicateId = " + fightEndInfo.getTeamChallengeVo().getDuplicatId());
			}
			
			String str = teamChallengeVo.getDuplicatId()+":"+fightEndInfo.getWinner();
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action490.getType(), 0, str);
			
			if (roleLoadInfo.getLeaderRoleId() != 0) {
				MutualService.getMutualService().closeMatch(roleLoadInfo, roleInfo.getId(), true, false);
			}
			
			if (fightEndInfo.getWinner() == 2) {
				//任务刷新
				QuestService.checkQuest(roleInfo, ActionType.action490.getType(), teamChallengeXmlInfo.getNo(), true, true);
			}
		}

		// 刷新战斗结果和奖励
		if(fightEndInfo.getWinner() != 4){
			SceneService.sendRoleRefreshMsg(resp, roleInfo.getId(), Command.MUTUAL_FIGHT_END_RESP);
		}
	}
}
