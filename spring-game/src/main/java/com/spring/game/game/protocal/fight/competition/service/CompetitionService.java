package com.snail.webgame.game.protocal.fight.competition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.config.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.config.RoomMessageConfig;
import com.snail.webgame.game.cache.FightCompetitionRankList;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.DiffMail;
import com.snail.webgame.game.common.DiffMailMessage;
import com.snail.webgame.game.common.ETimeMessageType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.InnerSort;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.xml.cache.StageXMLInfoMap;
import com.snail.webgame.game.common.xml.info.StageXMLInfo;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.CompetitionDao;
import com.snail.webgame.game.dao.MailDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.MailInfo.MailAttachment;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.competition.award.GetAwardReq;
import com.snail.webgame.game.protocal.fight.competition.award.GetAwardResp;
import com.snail.webgame.game.protocal.fight.competition.cancel.CancelFightResp;
import com.snail.webgame.game.protocal.fight.competition.history.FightHistoryRes;
import com.snail.webgame.game.protocal.fight.competition.history.FightHistoryResp;
import com.snail.webgame.game.protocal.fight.competition.info.CompetitionAgainstInfo;
import com.snail.webgame.game.protocal.fight.competition.query.QueryInfoResp;
import com.snail.webgame.game.protocal.fight.competition.rank.QueryRankReq;
import com.snail.webgame.game.protocal.fight.competition.rank.QueryRankRes;
import com.snail.webgame.game.protocal.fight.competition.rank.QueryRankResp;
import com.snail.webgame.game.protocal.fight.competition.ready.FightReadyResp;
import com.snail.webgame.game.protocal.fight.competition.submit.SubmitFightResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.pvp.competition.check.CheckFightServerReq;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.competition.request.CompetitionVo;
import com.snail.webgame.game.pvp.competition.request.WarriorVo;
import com.snail.webgame.game.pvp.service.PvpFightService;
import com.snail.webgame.game.thread.ScheduleThread;
import com.snail.webgame.game.thread.SendServerMsgThread;
import com.snail.webgame.game.xml.cache.KuafuPrizeXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.KuafuXMLPrize;

/**
 * 类介绍:竞技场业务Service类
 * @author zhoubo
 * @2014-11-25
 */
public class CompetitionService {
	
	private static Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 申请竞技场战斗
	 * @param roleId 角色Id
	 * @return SubmitFightResp 响应Response
	 */
	public SubmitFightResp submitCompetitionFight(int roleId) {
		SubmitFightResp resp = new SubmitFightResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
			return resp;
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
				return resp;
			}
			
			if(roleLoadInfo.getYabiaoFriendRoleId() > 0)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_38);
				return resp;
			}
			
			//检查是否可以进行PVP
			if(GameValue.PVE_FIGHT_FLAG == 0)
			{
				resp.setResult(ErrorCode.MUTUAL_STATUS_ERROR);
				return resp;
			}
			
			if (MutualService.getMutualService().notMatchTime(GameValue.COMPETITION_START_TIME, GameValue.COMPETITION_END_TIME)) {
				// 非竞技场时间
				resp.setResult(ErrorCode.FIGHT_OUT_OF_TIME_ERROR);
				return resp;
			}
			
			//判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if(checkItem != 1){
				resp.setResult(ErrorCode.QUEST_ERROR_25);
				return resp;
			}
			
			RoleService.timerRecoverEnergy(roleInfo);

			if (roleInfo.getEnergy() < GameValue.COMPETITION_FIGHT_COST_ENERGY_VALUE) {
				// 精力值不购
				resp.setResult(ErrorCode.ROLE_ENERGY_ERROR_1);
				return resp;
			}
			
			// 检查战斗状态
			int result = matchCheckRoleStatus(roleId, roleLoadInfo);
			
			if (result != 1) {
				if (logger.isWarnEnabled()) {
					logger.warn("competition match error : roleId = " + roleId + " match failed, inFight = " + roleLoadInfo.getInFight());
				}
				
				resp.setResult(result);
				return resp;
			}
			
			List<WarriorVo> warriorVoList = PVPFightService.getFightWarriorVoList(FightType.FIGHT_TYPE_9, roleInfo);
			int allHeroFight = roleInfo.getFightValue();
			if (warriorVoList == null || warriorVoList.size() <= 0) {
				resp.setResult(ErrorCode.FIGHT_NO_HEROS_ERROR);
				return resp;
			}

			IoSession session = RoomMessageConfig.serverMap.get(GameConfig.getInstance().getRoomId());

			if (session == null || !session.isConnected()) {
				logger.warn("CompetitionService.submitCompetitionFight : roomManage server is inavaliable");
				resp.setResult(ErrorCode.FIGHT_FIGHT_SERVER_INAVAILABLE_ERROR);
				return resp;
			}

			if (roleLoadInfo.getInFight() == 1) {
				// 报名中
				logger.warn("CompetitionService.submitCompetitionFight : submit fight is repeated");
				resp.setResult(1);
				return resp;
			} else if (roleLoadInfo.getInFight() == 2) {
				// 战斗中
				if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() < 60000) {
					logger.warn("CompetitionService.submitCompetitionFight : submit role is in fighting, roleId = " + roleId);
					resp.setResult(ErrorCode.FIGHT_IN_FIGHT_ERROR);
					return resp;
				}
			}

			ComFightRequestReq req = new ComFightRequestReq();
			req.setFightType((byte) 1);
			req.setRoleId((int) roleInfo.getId());
			req.setServerName(GameConfig.getInstance().getServerName() + GameConfig.getInstance().getGameServerId());
			req.setNickName(roleInfo.getRoleName());
			
			if (roleInfo.getVoiceUid() != null && !"".equals(roleInfo.getVoiceUid())) {
				req.setVoiceUid(roleInfo.getVoiceUid());
			} else {
				if (roleInfo.getUid() != null) {
					String[] strs = roleInfo.getUid().split("\\+");
					
					if (strs.length == 3) {
						roleInfo.setVoiceUid(strs[2]);
						req.setVoiceUid(roleInfo.getVoiceUid());
					}
				}
			}
			
			CompetitionVo competitionVo = new CompetitionVo();
			competitionVo.setFightValue(allHeroFight);
			competitionVo.setLevel(HeroInfoMap.getMainHeroLv(roleId));
			competitionVo.setLoseTimes(roleInfo.getTorrentTimes() < 0 ? roleInfo.getTorrentTimes() * -1 : 0);
			competitionVo.setStage(roleInfo.getCompetitionStage());
			competitionVo.setStageValue(roleInfo.getCompetitionValue());
			competitionVo.setVipLv(roleInfo.getVipLv());
			competitionVo.setMoneyRand((byte)GameValue.GAME_TOOL_KUAFUMONEY_RAND);
			competitionVo.setWinTimes(roleInfo.getTorrentTimes() > 0 ? roleInfo.getTorrentTimes() : 0);
			req.setCompetitionVo(competitionVo);
			req.setWarriorCount((byte) (warriorVoList != null ? warriorVoList.size() : 0));
			req.setWarriorList(warriorVoList);
			result = PvpFightService.sendPvpCompetition(req);

			roleLoadInfo.setFightStartTime(0);

			if (result == 1) {
				// 发送报名成功
				roleLoadInfo.setInFight((byte) 1);

				resp.setResult(1);
				return resp;
			} else {
				// 发送报名失败
				resp.setResult(ErrorCode.FIGHT_FIGHT_SERVER_INAVAILABLE_ERROR);
				return resp;
			}
		}
	}

	/**
	 * 取消申请竞技场战斗
	 * @param roleId 角色Id
	 * @return SubmitFightResp 响应Response
	 */
	public static CancelFightResp cancelCompetitionFight(int roleId) {
		CancelFightResp resp = new CancelFightResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_1);
			return resp;
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
				return resp;
			}

			IoSession session = RoomMessageConfig.serverMap.get(GameConfig.getInstance().getRoomId());

			if (session == null || !session.isConnected()) {
				// 管理服务器不可用
				resp.setResult(1);
				return resp;
			}

			if (roleLoadInfo.getInFight() == 0) {
				// 未报名或已取消
				resp.setResult(1);
				return resp;
			}

			if (roleLoadInfo.getInFight() == 2) {
				// 已经开始战斗
				if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() < GameValue.COMPETITION_FIGHT_MAX_TIME + 60000) {
					// 未达到当前战斗最大时间
					CheckFightServerReq checkFightServerReq = new CheckFightServerReq();
					checkFightServerReq.setFightServer(roleLoadInfo.getFightServer());
					checkFightServerReq.setRoleId(roleId);
					checkFightServerReq.setServerName(GameConfig.getInstance().getServerName()
							+ GameConfig.getInstance().getGameServerId());
					PvpFightService.sendFightServerCheck(checkFightServerReq);

					resp.setResult(1);
					return resp;
				} else {
					// 战斗已超时,取消成功
					roleLoadInfo.setInFight((byte) 0);
					resp.setResult(1);
					return resp;
				}
			}

			if (roleLoadInfo.getInFight() == 1) {
				// 报名匹配中，发送取消
				CompetitionService.cancelCompetition(roleInfo, (byte)1, 0, 0);
				roleLoadInfo.setInFight((byte) 0);
				roleInfo.getRoleLoadInfo().setUuid(null);
				roleLoadInfo.setCancelTime(System.currentTimeMillis());
			}
		}

		resp.setResult(1);
		return resp;
	}

	/**
	 * 发送报名取消消息
	 * @param roleInfo 角色信息
	 * @param fightType // 1-竞技场PVP 2-地图PVP 3-组队对攻战 4-单人对攻战  5-二人组队对攻战 6-组队副本 7-3V3 三人  8-单人3V3 9-3V3二人 
	 * @param captainRoleId , @param duplicateId 这2个参数只有 fightType == 6的时候有用，其他时候传0
	 */
	public static void cancelCompetition(RoleInfo roleInfo, byte fightType, int captainRoleId, int duplicateId) {
		ComFightRequestReq req = new ComFightRequestReq();
		req.setFightType(fightType);
		req.setRoleId(roleInfo.getId());
		req.setServerName(GameConfig.getInstance().getServerName() + GameConfig.getInstance().getGameServerId());
		req.setNickName(roleInfo.getRoleName());
		
		if(fightType == 6){
			req.setRoleId(captainRoleId == 0 ? roleInfo.getId() : captainRoleId);
			req.setUuid("cancel;" + duplicateId);
		} else if (fightType == 1) {
			req.setUuid("cancel;" + HeroInfoMap.getMainHeroLv(roleInfo.getId()) + ";" + roleInfo.getCompetitionStage());
		} else {
			req.setUuid("cancel");
		}
		
		PvpFightService.sendPvpCompetition(req);
	}

	/**
	 * 查询竞技场面板数据
	 * 
	 * @param roleId 角色Id
	 * @return QueryInfoResp 响应Response
	 */
	public QueryInfoResp queryCompetitionInfo(int roleId) {
		QueryInfoResp resp = new QueryInfoResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_11);
			return resp;
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
				return resp;
			}

			resp.setCompetitionStage(roleInfo.getCompetitionStage());
			resp.setCompetitionValue(roleInfo.getCompetitionValue());
			resp.setLoseTimes(roleLoadInfo.getLoseTimes());
			resp.setStageLoseTimes(roleLoadInfo.getStageLoseTimes());
			resp.setStageState(roleLoadInfo.getStageState());
			resp.setStageWinTimes(roleLoadInfo.getStageWinTimes());
			resp.setWinTimes(roleLoadInfo.getWinTimes());
			resp.setRankNum(FightCompetitionRankList.getRoleIndex(roleInfo) + 1);

			Set<Entry<Byte, StageXMLInfo>> set = StageXMLInfoMap.getStageXMLInfoSet();
			byte stageAward = 0;

			for (Entry<Byte, StageXMLInfo> entry : set) {
				if ((entry.getValue().getStageBit() & roleInfo.getStageAward()) == entry.getValue().getStageBit()) {
					if (stageAward < entry.getKey()) {
						stageAward = entry.getKey();
					}
				}
			}

			resp.setStageAward(stageAward);

			StringBuffer buffer = new StringBuffer();

			for (Entry<Byte, StageXMLInfo> entry : set) {
				if (entry.getKey() <= stageAward) {
					if ((entry.getValue().getStageBit() & roleLoadInfo.getCompetitionAward()) == 0) {
						buffer.append(entry.getKey()).append(",");
					}
				}
			}

			if (buffer.length() > 0) {
				resp.setCompetitionAward(buffer.toString().substring(0, buffer.toString().length() - 1));
			}
			
			long serverStartTime = ScheduleThread.SERVER_START_TIME;
			
			if (serverStartTime > 0) {
				long passedTimes = System.currentTimeMillis() - serverStartTime;
				long oneDay = 1000 * 3600 * 24;
				long cleanTime = GameValue.COMPETION_FIGHT_CLEAN_DAYS * oneDay;
				
				long periodPassedTime = passedTimes % cleanTime;// 此阶段过去的时间
				
				resp.setRemainCleanDays((byte)(GameValue.COMPETION_FIGHT_CLEAN_DAYS - (periodPassedTime / oneDay)));
				
				if (resp.getRemainCleanDays() <= 0) {
					resp.setRemainCleanDays((byte)GameValue.COMPETION_FIGHT_CLEAN_DAYS);
				}
			}
		}

		resp.setResult(1);
		return resp;
	}

	/**
	 * 查询全局排行
	 * 
	 * @param req 请求Request
	 * @param roleId 角色Id
	 * @return QueryRankResp 响应Response
	 */
	public QueryRankResp queryRank(QueryRankReq req, int roleId) {
		QueryRankResp resp = new QueryRankResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			return resp;
		}

		int startIndex = req.getStartIndex();

		if (startIndex < 0) {
			return resp;
		}

		synchronized (roleInfo) {
			List<InnerSort> list = FightCompetitionRankList.getRank(startIndex * GameValue.COMPETITION_RANK_PAGE_NUM,
					GameValue.COMPETITION_RANK_PAGE_NUM);

			if (list == null || list.size() == 0) {
				return resp;
			} else {
				resp.setList(new ArrayList<QueryRankRes>());

				for (int i = 0; i < list.size(); i++) {
					InnerSort innerSort = list.get(i);

					QueryRankRes queryRankRes = new QueryRankRes();
					RoleInfo tempRoleInfo = RoleInfoMap.getRoleInfo(innerSort.getId());

					if (tempRoleInfo != null) {
						queryRankRes.setLevel(HeroInfoMap.getMainHeroLv(tempRoleInfo.getId()));
						queryRankRes.setName(tempRoleInfo.getRoleName());
						queryRankRes.setRoleId(tempRoleInfo.getId());

						HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(tempRoleInfo);
						queryRankRes.setHeroId(heroInfo != null ? heroInfo.getHeroNo() : 0);
						queryRankRes.setMilitaryRank(innerSort.getStage());
						queryRankRes.setRankNum(startIndex * GameValue.COMPETITION_RANK_PAGE_NUM + i + 1);
						resp.getList().add(queryRankRes);
					}
				}

				resp.setCount(resp.getList().size());
			}
		}

		return resp;
	}

	/**
	 * 查询历史战斗记录
	 * 
	 * @param roleId 角色Id
	 * @return FightHistoryResp 响应Response
	 */
	public FightHistoryResp queryRecord(int roleId) {
		FightHistoryResp resp = new FightHistoryResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			return resp;
		}

		synchronized (roleInfo) {
			List<CompetitionAgainstInfo> list = roleInfo.getCompetitionAgainstInfoList();

			if (list == null || list.size() == 0) {
				return resp;
			} else {
				resp.setList(new ArrayList<FightHistoryRes>());

				for (int i = 0; i < list.size(); i++) {
					CompetitionAgainstInfo competitionAgainstInfo = list.get(i);

					FightHistoryRes fightHistoryRes = new FightHistoryRes();
					fightHistoryRes.setIsWin(competitionAgainstInfo.getIsWin());
					fightHistoryRes.setLevel(competitionAgainstInfo.getLevel());
					fightHistoryRes.setMilitaryRank(competitionAgainstInfo.getMilitaryRank());
					fightHistoryRes.setRoleName(competitionAgainstInfo.getRoleName());
					resp.getList().add(fightHistoryRes);
				}

				resp.setCount(resp.getList().size());
			}
		}

		return resp;
	}

	/**
	 * 领取竞技场奖励
	 * 
	 * @param req 请求Request
	 * @param roleId 角色Id
	 * @return GetAwardResp 响应Response
	 */
	public GetAwardResp getAward(GetAwardReq req, int roleId) {
		GetAwardResp resp = new GetAwardResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			return resp;
		}

		byte stage = req.getStage();

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
				return resp;
			}
			
			//判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if(checkItem != 1){
				resp.setResult(ErrorCode.QUEST_ERROR_25);
				return resp;
			}

			if (roleInfo.getStageAward() == 0) {
				resp.setResult(ErrorCode.FIGHT_AWARD_IS_EMPTY__ERROR);
				return resp;
			}

			StageXMLInfo stageXMLInfo = StageXMLInfoMap.getStageXMLInfo(stage);

			if (stageXMLInfo == null) {
				resp.setResult(ErrorCode.FIGHT_AWARD_IS_EMPTY__ERROR);
				return resp;
			}

			if ((roleInfo.getStageAward() & stageXMLInfo.getStageBit()) == 0) {
				// 未达到过此段位
				resp.setResult(ErrorCode.FIGHT_AWARD_IS_EMPTY__ERROR);
				return resp;
			}

			if ((roleLoadInfo.getCompetitionAward() & stageXMLInfo.getStageBit()) == stageXMLInfo.getStageBit()) {
				// 此段位奖励已领取
				resp.setResult(ErrorCode.FIGHT_AWARD_IS_EMPTY__ERROR);
				return resp;
			}
			
			roleLoadInfo.setCompetitionAward(roleLoadInfo.getCompetitionAward() | stageXMLInfo.getStageBit());
			CompetitionDao.getInstance().updateRolesCompetitionAward(roleInfo);

			Map<Integer, Integer> awardMap = stageXMLInfo.getAwardMap();
			Set<Entry<Integer, Integer>> set = awardMap.entrySet();
			List<DropInfo> addList = new ArrayList<DropInfo>();

			for (Entry<Integer, Integer> entry : set) {
				addList.add(new DropInfo(entry.getKey() + "", entry.getValue()));
			}

			if (stageXMLInfo.getGold() > 0) {
				addList.add(new DropInfo(ConditionType.TYPE_COIN.getName(), stageXMLInfo.getGold()));
			}

			if (stageXMLInfo.getMoney() > 0) {
				addList.add(new DropInfo(ConditionType.TYPE_MONEY.getName(), stageXMLInfo.getMoney()));
			}

			if (addList.size() > 0) {
				List<BattlePrize> prizeList = new ArrayList<BattlePrize>();

				int result = ItemService.addPrize(ActionType.action351.getType(), roleInfo, addList, null, null,
						null, null, null, null,prizeList, null, null, true);

				if (result != 1) {
					resp.setResult(result);
					return resp;
				}
			}
		}

		SceneService.sendRoleRefreshMsg(roleId, SceneService.REFESH_TYPE_ROLE, "");
		
		//红点判断
		RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_ARENA_FIGHT_END);

		GameLogService.insertPlayActionLog(roleInfo, ActionType.action351.getType(), req.getStage()+"");
		resp.setStage(stage);
		resp.setResult(1);
		return resp;
	}

	/**
	 * 取得当前战斗服务器信息
	 * 
	 * @param roleId 角色Id
	 * @return FightReadyResp 响应Response
	 */
	public FightReadyResp getFightInfo(int roleId) {
		FightReadyResp resp = new FightReadyResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			return resp;
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
				return resp;
			}

			// 断线重连后,玩家在世界地图并且不在战斗中
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if(pointInfo != null && roleLoadInfo.getInFight() != 4)
			{
				// 客户端在世界地图正常状态，客户端断线重连后清除老数据，拿新数据
				resp.setResult(2);
				return resp;
			}
			
			if (roleLoadInfo.getInFight() != 2 && roleLoadInfo.getInFight() != 4 && roleLoadInfo.getInFight() != 7 && roleLoadInfo.getInFight() != 10 && roleLoadInfo.getInFight() != 12) {
				// 非战斗状态
				resp.setResult(ErrorCode.FIGHT_REUSE_END_ERROR);
				return resp;
			}

			resp.setServer(roleLoadInfo.getFightServer());
			resp.setUuid(roleLoadInfo.getUuid());

			if (roleLoadInfo.getInFight() == 2) {
				resp.setFightType((byte) 1);
			} else if (roleLoadInfo.getInFight() == 4) {
				resp.setFightType((byte) 2);
			} else if (roleLoadInfo.getInFight() == 7) {
				resp.setFightType((byte) 3);
			} else if (roleLoadInfo.getInFight() == 10) {
				resp.setFightType((byte) 6);
			} else if (roleLoadInfo.getInFight() == 12) {
				resp.setFightType((byte) 7);
			}
		}

		resp.setResult(1);
		return resp;
	}

	/**
	 * 查询角色排行
	 * 
	 * @param roleId 角色Id
	 * @return QueryRankResp 响应Response
	 */
	public QueryRankResp queryUserRank(int roleId) {
		QueryRankResp resp = new QueryRankResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			return resp;
		}

		synchronized (roleInfo) {
			int index = FightCompetitionRankList.getRoleIndex(roleInfo);

			if (index >= 0) {
				int startIndex = 0;

				if (GameValue.COMPETITION_RANK_PAGE_NUM > 1) {
					while ((startIndex + 1) * GameValue.COMPETITION_RANK_PAGE_NUM - 1 < index) {
						startIndex += 1;
					}
				} else {
					// TODO
				}

				List<InnerSort> list = FightCompetitionRankList.getRank(startIndex
						* GameValue.COMPETITION_RANK_PAGE_NUM, GameValue.COMPETITION_RANK_PAGE_NUM);

				if (list == null || list.size() == 0) {
					return resp;
				} else {
					resp.setList(new ArrayList<QueryRankRes>());

					for (int i = 0; i < list.size(); i++) {
						InnerSort innerSort = list.get(i);

						QueryRankRes queryRankRes = new QueryRankRes();
						RoleInfo tempRoleInfo = RoleInfoMap.getRoleInfo(innerSort.getId());

						if (tempRoleInfo != null) {
							queryRankRes.setLevel(HeroInfoMap.getMainHeroLv(roleId));
							queryRankRes.setName(tempRoleInfo.getRoleName());
							queryRankRes.setRoleId(tempRoleInfo.getId());

							HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(tempRoleInfo);
							queryRankRes.setHeroId(heroInfo != null ? heroInfo.getHeroNo() : 0);
							queryRankRes.setMilitaryRank(innerSort.getStage());
							queryRankRes.setRankNum(startIndex * GameValue.COMPETITION_RANK_PAGE_NUM + i + 1);
							resp.getList().add(queryRankRes);
						}
					}

					resp.setCount(resp.getList().size());
				}
			}
		}

		return resp;
	}
	
	/**
	 * 检查角色包名状态
	 * 
	 * @param roleId		角色Id
	 * @param roleLoadInfo	角色在线信息
	 * @return	int 1-成功 otherwise-不成功 
	 */
	private int matchCheckRoleStatus(int roleId, RoleLoadInfo roleLoadInfo) {
		if (roleLoadInfo.getInFight() == 0) {
			// 状态正常
			if ((System.currentTimeMillis() - roleLoadInfo.getCancelTime()) < 5000) {
				// 过于频繁报名，取消报名和下次报名需要间隔2秒
				return ErrorCode.FIGHT_MATCH_TOO_BUSY_ERROR;
			}
		} else if (roleLoadInfo.getInFight() == 1) {
			// 已报名
			return ErrorCode.FIGHT_IN_FIGHT_ERROR;
		} else if (roleLoadInfo.getInFight() == 2) {
			// 竞技场战斗中
			if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() < 60000) {
				logger.warn("MutualService.match : role is in fighting, roleId = " + roleId + " inFight = 2");
				return ErrorCode.FIGHT_IN_FIGHT_ERROR;
			}
		} else if (roleLoadInfo.getInFight() == 7) {
			// 对攻战战斗中
			roleLoadInfo.setUuid(null);
			roleLoadInfo.setInFight((byte)0);
			roleLoadInfo.setFightStartTime(0);
		} else if (roleLoadInfo.getInFight() == 4) {
			// 地图战斗中
			if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() < 60000) {
				logger.warn("MutualService.match : role is in fighting, roleId = " + roleId + " inFight = 4");
				return ErrorCode.FIGHT_IN_FIGHT_ERROR;
			}
		} else if (roleLoadInfo.getInFight() == 10) {
			// 组队副本战斗中
			roleLoadInfo.setUuid(null);
			roleLoadInfo.setInFight((byte)0);
			roleLoadInfo.setFightStartTime(0);
		} else if (roleLoadInfo.getInFight() == 12) {
			// 3V3战斗中
			roleLoadInfo.setUuid(null);
			roleLoadInfo.setInFight((byte)0);
			roleLoadInfo.setFightStartTime(0);
		} else {
			// 不正常状态
			if (logger.isWarnEnabled()) {
				logger.warn("mutual match error : roleId = " + roleId + " inFight = " + roleLoadInfo.getInFight());
			}

			return ErrorCode.MUTUAL_STATUS_ERROR;
		}

		return 1;
	}
	
	/**
	 * 每天 23：00 发放竞技场排名奖励
	 * @return
	 */
	public static void sendPlaceRewardMailInfo() {
		List<DiffMail> diffMailList = FightCompetitionRankList.getDiffMailList();
		DiffMailMessage diffMailMessage = new DiffMailMessage(ETimeMessageType.SEND_BATCH_DIFF_MAIL, diffMailList);
		SendServerMsgThread.addMessage(diffMailMessage);
	}

	/**
	 * 获取竞技场排名奖励邮件信息
	 * @param roleInfo
	 * @param arenaInfo
	 * @return
	 */
	public static DiffMail getKuafuPlaceRewardMailInfo(RoleInfo roleInfo,int place) {
		KuafuXMLPrize prize = KuafuPrizeXMLInfoMap.getKuafuXMLbyPlace(place);
		if (prize == null) {
			logger.info("getKuafuPlaceReward error palce=" + place + "roleId=" + roleInfo.getId());
			return null;
		}
		List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(prize.getPlaceDropNoStr());
		// 奖励
		List<DropInfo> addList = new ArrayList<DropInfo>();

		ItemService.getDropXMLInfo(roleInfo, list, addList);

		List<MailAttachment> attachments = new ArrayList<MailAttachment>();
		MailAttachment att = null;
		for (DropInfo drop : addList) {
			if (drop.getItemNo().equals(ConditionType.TYPE_KUAFU_MONEY.getName())) {
				drop.setItemNum(drop.getItemNum() * (byte) GameValue.GAME_TOOL_KUAFUMONEY_RAND);
			}
			att = new MailAttachment(drop.getItemNo(), drop.getItemNum(), NumberUtils.toInt(drop.getParam()), 0);
			attachments.add(att);
		}
		String attachment = MailDAO.encoderAttachment(attachments);
		if (attachment.length() <= 0) {
			logger.info("getPlaceReward error palce=" + place + "roleId=" + roleInfo.getId());
			return null;
		}

		String title = Resource.getMessage("game", "MULTI_PVP_STREAK_TITLE");
		String content = Resource.getMessage("game", "MULTI_PVP_STREAK_CONTENT");
		String reserve =  place +"";

		DiffMail diffmail = new DiffMail(roleInfo.getId(), attachment, content, title, reserve);

		return diffmail;
	}
}
