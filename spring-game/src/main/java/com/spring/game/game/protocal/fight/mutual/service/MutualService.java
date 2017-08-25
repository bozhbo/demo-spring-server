package com.snail.webgame.game.protocal.fight.mutual.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.FightInfoMap;
import com.snail.webgame.game.cache.FightMutualRankList;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.competition.service.CompetitionService;
import com.snail.webgame.game.protocal.fight.mutual.cancel.MutualCancelResp;
import com.snail.webgame.game.protocal.fight.mutual.close.CloseMatchResp;
import com.snail.webgame.game.protocal.fight.mutual.invite.MutualInviteReq;
import com.snail.webgame.game.protocal.fight.mutual.invite.MutualInviteResp;
import com.snail.webgame.game.protocal.fight.mutual.invite.MutualInviteResultResp;
import com.snail.webgame.game.protocal.fight.mutual.join.MutualJoinTeamReq;
import com.snail.webgame.game.protocal.fight.mutual.join.MutualJoinTeamResp;
import com.snail.webgame.game.protocal.fight.mutual.leave.MakeLeaveReq;
import com.snail.webgame.game.protocal.fight.mutual.leave.MakeLeaveResp;
import com.snail.webgame.game.protocal.fight.mutual.match.MutualMatchReq;
import com.snail.webgame.game.protocal.fight.mutual.match.MutualMatchResp;
import com.snail.webgame.game.protocal.fight.mutual.open.OpenMatchResp;
import com.snail.webgame.game.protocal.fight.mutual.rank.MutualQueryRankRes;
import com.snail.webgame.game.protocal.fight.mutual.rank.MutualQueryRankResp;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualLeftRefreshResp;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualRefershResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.service.PvpFightService;
import com.snail.webgame.game.xml.info.TeamChallengeXmlInfo;
import com.snail.webgame.game.xml.load.TeamChallengeXmlLoader;

/**
 * 
 * 类介绍:PVP对攻战单人和组队面板操作业务类
 * 对于组队修改和读取多人数据保持同步，不使用synchronized全局锁，改用AtomicBoolean值的检查保持弱锁定，
 * 可以被嵌套在其他synchronized关键字中使用,但是有可能出现修改时被锁定，则修改失败
 *
 * @author zhoubo
 * @2015年6月5日
 */
public class MutualService {

	private static MutualService mutualService = new MutualService();

	private MutualService() {

	}

	public synchronized static MutualService getMutualService() {
		return mutualService;
	}

	private static Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 查询全局排名
	 * 
	 * @param roleId 角色Id
	 * @return 响应Response
	 */
	public MutualQueryRankResp queryRank(int roleId) {
		MutualQueryRankResp resp = new MutualQueryRankResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			return resp;
		}

		synchronized (roleInfo) {
			List<MutualQueryRankRes> list = FightMutualRankList.getList();
			if (list == null || list.size() == 0) {
				return resp;
			}
			
			List<MutualQueryRankRes> tempList = new ArrayList<MutualQueryRankRes>();
			for (MutualQueryRankRes mutualQueryRankRes : list) {
				RoleInfo tempRoleInfo = RoleInfoMap.getRoleInfo(mutualQueryRankRes.getRoleId());
				
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(tempRoleInfo);
				
				mutualQueryRankRes.setName(tempRoleInfo.getRoleName());
				
				if (heroInfo != null) {
					mutualQueryRankRes.setHeroId(heroInfo.getHeroNo());
					mutualQueryRankRes.setHeroLevel(heroInfo.getHeroLevel());
				}
				
				tempList.add(mutualQueryRankRes);
			}
			
			resp.setMyRank(FightMutualRankList.getRoleIndex(roleInfo));
			resp.setMyValue(roleInfo.getScoreValue());
			
			if (tempList.size() > 0) {
				resp.setCount(tempList.size());
				resp.setList(tempList);
			}
		}

		return resp;
	}

	/**
	 * 打开多人匹配界面
	 * 
	 * @param roleId 角色Id
	 * @param activityType 1-长坂坡 2-组队副本 3-3V3竞技场 
	 * @param duplicateId 
	 * @return 响应Response
	 */
	public OpenMatchResp openMatch(int roleId, int activityType, int duplicateId) {
		OpenMatchResp resp = new OpenMatchResp();

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
			
			//检查是否可以进行PVP
			if(GameValue.PVE_FIGHT_FLAG == 0)
			{
				resp.setResult(ErrorCode.MUTUAL_STATUS_ERROR);
				return resp;
			}
			
			int result = checkCanAttend(activityType, roleInfo, roleLoadInfo, duplicateId);
			
			if (result != 1) {
				if (logger.isWarnEnabled()) {
					logger.warn("openMatch error : not match time, roleId = " + roleId );
				}
				
				resp.setResult(result);
				return resp;
			}
			
			result = matchCheckRoleStatus(roleId, roleLoadInfo, true);
			
			if (result != 1) {
				// 非正常状态不能建立队伍
				if (logger.isWarnEnabled()) {
					logger.warn("openMatch error : role was in fight, roleId = " + roleId + " InFight = " + roleLoadInfo.getInFight());
				}
				
				resp.setResult(result);
				return resp;
			}
			
			if (!tryLock(roleLoadInfo)) {
				// 队伍信息正被修改中
				resp.setResult(ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR);
				return resp;
			}

			try {
				if (roleLoadInfo.getLeaderRoleId() != 0) {
					if (logger.isWarnEnabled()) {
						logger.warn("openMatch error : role was in team, roleId = " + roleId + " leaderRole = " + roleLoadInfo.getLeaderRoleId());
					}

					if (roleLoadInfo.getLeaderRoleId() == roleId) {
						// 原来是队长，解散队伍
						breakTeam(roleLoadInfo, roleId, true);
					} else {
						// 原来是队员,离开原队伍
						leaveTeam(roleLoadInfo, roleId, true);
					}
				}

				// 设置队长为自己
				roleLoadInfo.setLeaderRoleId(roleId);
				roleLoadInfo.setMember1RoleId(0);
				roleLoadInfo.setMember2RoleId(0);

				MutualRefershResp mutualRefershResp = MutualBaseService.createMutualRefershResp(roleInfo);
				resp.setFightValue(mutualRefershResp.getFightValue());
				resp.setHeroId(mutualRefershResp.getHeroId());
				resp.setLevel(mutualRefershResp.getLevel());
				resp.setName(mutualRefershResp.getName());
				resp.setResult(1);
				resp.setRoleId(roleId);
				resp.setTempHeroId(mutualRefershResp.getTempHeroId());
				
				if (logger.isInfoEnabled()) {
					logger.info("openMatch info : roleId = " + roleId + " openMatch successed");
				}
			} finally {
				unLock(roleLoadInfo);
			}
		}

		resp.setResult(1);
		return resp;
	}

	/**
	 * 检查是否能进入
	 * 
	 * @param activityType 1-长坂坡 2-组队副本 3-3V3竞技场
	 * @param roleInfo
	 * @param roleLoadInfo
	 * @param duplicateId
	 * @return
	 */
	public int checkCanAttend(int activityType, RoleInfo roleInfo, RoleLoadInfo roleLoadInfo, int duplicateId) {
		int result = 1;
		
		if (activityType == 1){
			if(notMatchTime(GameValue.MUTUAL_FIGHT_START_TIME_1, GameValue.MUTUAL_FIGHT_END_TIME_1) 
					&& notMatchTime(GameValue.MUTUAL_FIGHT_START_TIME_2, GameValue.MUTUAL_FIGHT_END_TIME_2)) {
				result = ErrorCode.MUTUAL_MATCH_TIME_ERROR;
				return result;
			}
			
			if(roleLoadInfo.getMutualFightCount() >= GameValue.MUTUAL_DAILY_FIGHT_COUNTS){
				result = ErrorCode.MUTUAL_COUNT_ERROR;
				return result;
			}

			RoleService.timerRecoverEnergy(roleInfo);
			
			if (roleInfo.getEnergy() < GameValue.MUTUAL_FIGHT_COST_ENERGY_VALUE) {
				// 精力值不够
				result = ErrorCode.ROLE_ENERGY_ERROR_1;
				return result;
			}
		}
		
		if (activityType == 2) {
			// 组队副本参加等级不够
			result = checkCanAttendTeamChallenge(roleInfo, duplicateId);
			
			if(result != 1){
				return result;
			}
		}
		
		if (activityType == 3) {
			if(notMatchTime(GameValue.TEAM_FIGHT_START_TIME_1, GameValue.TEAM_FIGHT_END_TIME_1) && 
					notMatchTime(GameValue.TEAM_FIGHT_START_TIME_2, GameValue.TEAM_FIGHT_END_TIME_2)){
				result = ErrorCode.MUTUAL_MATCH_TIME_ERROR;
				return result;
			}

			if(roleLoadInfo.getTeam3V3Times() >= GameValue.TEAM_3V3_COUNT){
				result = ErrorCode.TEAM__COUNT_ERROR;
				return result;
			}
		}
		
		return result;
	}

	public int checkCanAttendTeamChallenge(RoleInfo roleInfo, int duplicateId){
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		
		if(heroInfo == null){
			return ErrorCode.HERO_UP_ERROR_8;
		}
		TeamChallengeXmlInfo info = TeamChallengeXmlLoader.getTeamChallenge(duplicateId);

		if(info == null){
			return ErrorCode.TEAM_CHALLENGE_NOT_EXISIT;
		}
		if(heroInfo.getHeroLevel() < info.getLv()){
			return ErrorCode.TEAM_CHALLENGE_LV_ERROR;
		}
		int getPrizecount = roleInfo.getRoleLoadInfo().getTeamChallengeTimesByDupId(duplicateId);// 已经获得的奖励次数
		
		if(getPrizecount >= info.getTimes() || roleInfo.getRoleLoadInfo().getTeamChallengeAllTimes() >= GameValue.TEAM_CHALLENGE_COUNT){
			return ErrorCode.TEAM__COUNT_ERROR;
		}
		
		return 1;
	}
	
	/**
	 * 关闭组队匹配面板,支持队长关闭和队员关闭
	 * 
	 * @param roleId 角色Id
	 * @return CloseMatchResp 响应Response
	 */
	public CloseMatchResp closeMatch(int roleId) {
		CloseMatchResp resp = new CloseMatchResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
			return resp;
		}
		
		int memberRoleId1 = 0;
		int memberRoleId2 = 0;
		int leaderRoleId = 0;

		synchronized (roleInfo) {
			if (roleLoadInfo.getInFight() == 7 || roleLoadInfo.getInFight() == 10 || roleLoadInfo.getInFight() == 12) {
				if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() < 5000) {
					// 战斗中不能关闭面板
					resp.setResult(ErrorCode.MUTUAL_IS_IN_FIGHT_ERROR);
					return resp;
				}
			}
			
			if (roleLoadInfo.getLeaderRoleId() == roleId) {
				memberRoleId1 = roleLoadInfo.getMember1RoleId();
				memberRoleId2 = roleLoadInfo.getMember2RoleId();
				leaderRoleId = roleId;
			} else {
				leaderRoleId = roleLoadInfo.getLeaderRoleId();
				memberRoleId1 = roleLoadInfo.getLeaderRoleId();
				
				RoleInfo leaderRoleInfo = RoleInfoMap.getRoleInfo(leaderRoleId);
				
				if (leaderRoleInfo != null) {
					if (leaderRoleInfo.getRoleLoadInfo() != null && leaderRoleInfo.getRoleLoadInfo().getLeaderRoleId() == leaderRoleId) {
						memberRoleId2 = leaderRoleInfo.getRoleLoadInfo().getMember2RoleId() == roleId ? leaderRoleInfo.getRoleLoadInfo().getMember1RoleId() : leaderRoleInfo.getRoleLoadInfo().getMember2RoleId();					}
				}
			}
			
			if (roleLoadInfo.getInFight() == 8) {
				// 三人匹配中,发送取消匹配
				CompetitionService.cancelCompetition(RoleInfoMap.getRoleInfo(roleId), (byte) 3, 0, 0);
				roleLoadInfo.setInFight((byte) 0);
				roleLoadInfo.setUuid(null);
				roleLoadInfo.setCancelTime(System.currentTimeMillis());
			} else if (roleLoadInfo.getInFight() == 9) {
				// 二人匹配中,发送取消匹配
				CompetitionService.cancelCompetition(RoleInfoMap.getRoleInfo(roleId), (byte) 5, 0, 0);
				roleLoadInfo.setInFight((byte) 0);
				roleLoadInfo.setUuid(null);
				roleLoadInfo.setCancelTime(System.currentTimeMillis());
			} else if (roleLoadInfo.getInFight() == 11) {
				CompetitionService.cancelCompetition(RoleInfoMap.getRoleInfo(roleId), (byte) 6, leaderRoleId, roleLoadInfo.getTeamDuplicateId());
				roleLoadInfo.setInFight((byte) 0);
				roleLoadInfo.setUuid(null);
				roleLoadInfo.setCancelTime(System.currentTimeMillis());
			} else if (roleLoadInfo.getInFight() == 13) {
				CompetitionService.cancelCompetition(RoleInfoMap.getRoleInfo(roleId), (byte) 8, 0, 0);
				roleLoadInfo.setInFight((byte) 0);
				roleLoadInfo.setUuid(null);
				roleLoadInfo.setCancelTime(System.currentTimeMillis());
			} else if (roleLoadInfo.getInFight() == 14) {
				CompetitionService.cancelCompetition(RoleInfoMap.getRoleInfo(roleId), (byte) 9, 0, 0);
				roleLoadInfo.setInFight((byte) 0);
				roleLoadInfo.setUuid(null);
				roleLoadInfo.setCancelTime(System.currentTimeMillis());
			} else if (roleLoadInfo.getInFight() == 15) {
				CompetitionService.cancelCompetition(RoleInfoMap.getRoleInfo(roleId), (byte) 7, 0, 0);
				roleLoadInfo.setInFight((byte) 0);
				roleLoadInfo.setUuid(null);
				roleLoadInfo.setCancelTime(System.currentTimeMillis());
			}
		}
		
		// 取消其他匹配中的角色
		beCancelMatch(leaderRoleId, RoleInfoMap.getRoleInfo(memberRoleId1), RoleInfoMap.getRoleInfo(memberRoleId2));
		
		synchronized (roleInfo) {
			int result = closeMatch(roleLoadInfo, roleId, false, true);

			if (logger.isInfoEnabled()) {
				logger.info("closeMatch info : roleId = " + roleId + " closeMatch result = " + result);
			}

			resp.setResult(result);
		}
		
		return resp;
	}

	/**
	 * 申请加入队伍
	 * 
	 * @param req 请求Request
	 * @param roleId 角色Id
	 * @return MutualJoinTeamResp 响应Response
	 */
	public MutualJoinTeamResp joinTeam(MutualJoinTeamReq req, int roleId) {
		MutualJoinTeamResp resp = new MutualJoinTeamResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
			return resp;
		}

		RoleInfo leaderRoleInfo = RoleInfoMap.getRoleInfo(req.getLeaderRoleId());

		if (leaderRoleInfo == null) {
			resp.setResult(ErrorCode.MUTUAL_LEADER_NOT_EXIST_ERROR);
			return resp;
		}

		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
			return resp;
		}

		RoleLoadInfo leaderRoleLoadInfo = leaderRoleInfo.getRoleLoadInfo();

		if (leaderRoleLoadInfo == null) {
			resp.setResult(ErrorCode.MUTUAL_LEADER_NOT_EXIST_ERROR);
			return resp;
		}
		
		if (matchCheckRoleStatus(leaderRoleInfo.getId(), leaderRoleLoadInfo, false) != 1) {
			resp.setResult(ErrorCode.MUTUAL_MEMBER_FIGHT_STATUS_ERROR);
			return resp;
		}

		if (roleId == req.getLeaderRoleId()) {
			// 不能加入自己的队伍
			if (logger.isWarnEnabled()) {
				logger.warn("joinTeam error : roleId = " + roleId + " is join mine team");
			}

			resp.setResult(ErrorCode.MUTUAL_IS_JOIN_MINE_TEAM_ERROR);
			return resp;
		}

		int result = 1;

		synchronized (roleInfo) {
			if (matchCheckRoleStatus(roleId, roleLoadInfo, true) != 1) {
				// 非正常状态不能加入队伍
				if (logger.isWarnEnabled()) {
					logger.warn("joinTeam error : role was in fight, roleId = " + roleId + " InFight = " + roleLoadInfo.getInFight());
				}
				
				resp.setResult(ErrorCode.MUTUAL_JOIN_TEAM_IN_FIGHT_ERROR);
				return resp;
			}

			boolean flag1 = tryLock(roleLoadInfo);
			boolean flag2 = tryLock(leaderRoleLoadInfo);

			try {
				if (flag1 && flag2) {
					int leaderRoleId = roleLoadInfo.getLeaderRoleId();

					if (leaderRoleId == req.getLeaderRoleId()) {
						// 已加入此队伍
						if (logger.isWarnEnabled()) {
							logger.warn("joinTeam error : roleId = " + roleId + " is joined team leaderId = " + leaderRoleId);
						}

						resp.setResult(ErrorCode.MUTUAL_IS_IN_TEAM_ERROR);
						return resp;
					}

					if (leaderRoleId != 0) {
						if (logger.isWarnEnabled()) {
							logger.warn("joinTeam error : roleId = " + roleId + " leaderRole = " + leaderRoleId);
						}

						if (leaderRoleId != roleId) {
							// 原来是队员
							leaveTeam(roleLoadInfo, roleId, true);
						} else if (leaderRoleId == roleId) {
							// 原来是队长
							breakTeam(roleLoadInfo, roleId, true);
						}
					}

					result = joinTeam(leaderRoleLoadInfo, req.getLeaderRoleId(), roleLoadInfo, roleId);

					if (result != 1) {
						if (logger.isWarnEnabled()) {
							logger.warn("joinTeam error : roleId = " + roleId + " leaderRole = " + req.getLeaderRoleId() + " join failed, result = " + result);
						}

						resp.setResult(result);
						return resp;
					}

					resp.setList(new ArrayList<MutualRefershResp>());
					resp.getList().add(MutualBaseService.createMutualRefershResp(leaderRoleInfo));

					int memberRoleId1 = leaderRoleLoadInfo.getMember1RoleId();
					int memberRoleId2 = leaderRoleLoadInfo.getMember2RoleId();

					if (memberRoleId1 != 0 && memberRoleId1 != roleId) {
						// 其他队员
						if (RoleInfoMap.getRoleInfo(memberRoleId1) != null) {
							resp.getList().add(MutualBaseService.createMutualRefershResp(RoleInfoMap.getRoleInfo(memberRoleId1)));
						}
					}

					if (memberRoleId2 != 0 && memberRoleId2 != roleId) {
						// 其他队员
						if (RoleInfoMap.getRoleInfo(memberRoleId2) != null) {
							resp.getList().add(MutualBaseService.createMutualRefershResp(RoleInfoMap.getRoleInfo(memberRoleId2)));
						}
					}

					resp.getList().add(MutualBaseService.createMutualRefershResp(roleInfo));
					resp.setCount(resp.getList().size());
				} else {
					// 队伍信息正被修改中
					resp.setResult(ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR);
					return resp;
				}
			} finally {
				if (flag1) {
					unLock(roleLoadInfo);
				}

				if (flag2) {
					unLock(leaderRoleLoadInfo);
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("joinTeam info : roleId = " + roleId + " is joined team leaderId = " + req.getLeaderRoleId() + " successed, result = " + result);
			}
		}

		resp.setResult(result);
		return resp;
	}

	/**
	 * 踢出队伍
	 * 
	 * @param req 请求Request
	 * @param roleId 角色ID
	 * @return MakeLeaveResp 响应Response
	 */
	public MakeLeaveResp makeLeave(MakeLeaveReq req, int roleId) {
		MakeLeaveResp resp = new MakeLeaveResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
			return resp;
		}

		RoleInfo memberRoleInfo = RoleInfoMap.getRoleInfo(req.getMemberRoleId());
		RoleLoadInfo memberRoleLoadInfo = null;

		if (memberRoleInfo != null) {
			memberRoleLoadInfo = memberRoleInfo.getRoleLoadInfo();
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
				return resp;
			}
			
			if (roleLoadInfo.getInFight() == 8 || roleLoadInfo.getInFight() == 9 
					|| roleLoadInfo.getInFight() == 11 || roleLoadInfo.getInFight() == 14
					|| roleLoadInfo.getInFight() == 15) {
				// 处于匹配中不能踢出退伍
				resp.setResult(ErrorCode.MUTUAL_MAKE_OUT_IN_FIGHT_ERROR);
				return resp;
			}

			int result = makeLeave(roleLoadInfo, roleId, memberRoleLoadInfo, req.getMemberRoleId());

			if (logger.isInfoEnabled()) {
				logger.info("makeLeave info : leaderId = " + roleId + " make memberId = " + req.getMemberRoleId() + " leave, result = " + result);
			}

			resp.setResult(result);
			resp.setRoleId(req.getMemberRoleId());
		}

		return resp;
	}

	/**
	 * 开始匹配,支持单人匹配和组队匹配
	 * 
	 * @param req 请求Request
	 * @param roleId 角色Id
	 * @return MutualMatchResp 响应Response
	 */
	public MutualMatchResp match(MutualMatchReq req, int roleId) {
		MutualMatchResp resp = new MutualMatchResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = null;

		synchronized (roleInfo) {
			roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
				return resp;
			}

			int result = checkCanAttend(1, roleInfo, roleLoadInfo, 0);

			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			
			// 检查角色状态
			result = matchCheckRoleStatus(roleId, roleLoadInfo, true);

			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			
			if (req.getMatchType() != 1 && req.getMatchType() != 2) {
				resp.setResult(ErrorCode.MUTUAL_STATUS_ERROR);
				return resp;
			}
			
			//检查是否可以进行PVP
			if(GameValue.PVE_FIGHT_FLAG == 0)
			{
				resp.setResult(ErrorCode.MUTUAL_STATUS_ERROR);
				return resp;
			}

			if (req.getMatchType() == 1) {
				// 单人匹配
				if (roleLoadInfo.getLeaderRoleId() != 0 && roleLoadInfo.getLeaderRoleId() != roleId) {
					// 处于其他队伍中(自己是队员)
					
					if (logger.isWarnEnabled()) {
						logger.warn("mutual match error : roleId = " + roleId + " signal match leaderId = " + roleLoadInfo.getLeaderRoleId());
					}
					
					if (!tryLock(roleLoadInfo)) {
						// 修改中
						resp.setResult(ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR);
						return resp;
					}

					try {
						leaveTeam(roleLoadInfo, roleId, true);
					} finally {
						unLock(roleLoadInfo);
					}
				} else if (roleLoadInfo.getLeaderRoleId() != 0 && roleLoadInfo.getLeaderRoleId() == roleId) {
					// 处于其他队伍中(自己是队长)
					
					if (logger.isWarnEnabled()) {
						logger.warn("mutual match error : roleId = " + roleId + " signal match leaderId = " + roleLoadInfo.getLeaderRoleId());
					}

					if (!tryLock(roleLoadInfo)) {
						// 修改中
						resp.setResult(ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR);
						return resp;
					}
					
					try {
						breakTeam(roleLoadInfo, roleId, true);
					} finally {
						unLock(roleLoadInfo);
					}
				}

				ComFightRequestReq comFightRequestReq = MutualBaseService.getFightMatchData(4, roleInfo, null);
				result = PvpFightService.sendPvpCompetition(comFightRequestReq);

				if (result == 1) {
					roleLoadInfo.setInFight((byte) 6);
				} else {
					resp.setResult(result);
				}
			}
		}
		
		int memberRoleId1 = roleLoadInfo.getMember1RoleId();
		int memberRoleId2 = roleLoadInfo.getMember2RoleId();
		
		RoleInfo memberRoleInfo1 = RoleInfoMap.getRoleInfo(memberRoleId1);
		RoleInfo memberRoleInfo2 = RoleInfoMap.getRoleInfo(memberRoleId2);
		
		if (req.getMatchType() == 2) {
			// 组队匹配
			if (memberRoleInfo1 != null && memberRoleInfo2 != null) {
				// 三人匹配
				synchronized (memberRoleInfo1) {
					RoleService.timerRecoverEnergy(memberRoleInfo1);
				}
				
				if (memberRoleInfo1.getEnergy() < GameValue.MUTUAL_FIGHT_COST_ENERGY_VALUE) {
					// 精力值不够
					resp.setResult(ErrorCode.MUTUAL_ENERGY_VALUE_ERROR);
					return resp;
				}
				
				synchronized (memberRoleInfo2) {
					RoleService.timerRecoverEnergy(memberRoleInfo2);
				}
				
				if (memberRoleInfo2.getEnergy() < GameValue.MUTUAL_FIGHT_COST_ENERGY_VALUE) {
					// 精力值不够
					resp.setResult(ErrorCode.MUTUAL_ENERGY_VALUE_ERROR);
					return resp;
				}

				RoleLoadInfo memberRoleLoadInfo1 = memberRoleInfo1.getRoleLoadInfo();
				RoleLoadInfo memberRoleLoadInfo2 = memberRoleInfo2.getRoleLoadInfo();

				if (memberRoleLoadInfo1 == null || memberRoleLoadInfo2 == null) {
					resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
					return resp;
				}

				boolean flag1 = tryLock(roleLoadInfo);
				boolean flag2 = tryLock(memberRoleLoadInfo1);
				boolean flag3 = tryLock(memberRoleLoadInfo2);

				try {
					if (flag1 && flag2 && flag3) {
						if (roleLoadInfo.getLeaderRoleId() != roleId) {
							// 队伍已解散
							resp.setResult(ErrorCode.MUTUAL_LEADER_NOT_EXIST_ERROR);
							return resp;
						}

						if (roleLoadInfo.getMember1RoleId() != memberRoleId1 || roleLoadInfo.getMember2RoleId() != memberRoleId2) {
							// 队员已离开
							resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
							return resp;
						}

						if (memberRoleLoadInfo1.getLeaderRoleId() != roleId || memberRoleLoadInfo2.getLeaderRoleId() != roleId) {
							// 队员已离开
							resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
							return resp;
						}
						
						synchronized (memberRoleInfo1) {
							int result = matchCheckRoleStatus(memberRoleId1, memberRoleLoadInfo1, false);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId1 + " match failed, inFight = " + memberRoleLoadInfo1.getInFight());
								}
								
								resp.setResult(result);
								return resp;
							}
							
							result = checkCanAttend(1, memberRoleInfo1, memberRoleLoadInfo1, 0);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId1 + " match failed,result = " + result);
								}
								
								resp.setResult(result);
								return resp;
							}
							memberRoleLoadInfo1.setInFight((byte)8);
						}
						
						synchronized (memberRoleInfo2) {
							int result = matchCheckRoleStatus(memberRoleId2, memberRoleLoadInfo2, false);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId2 + " match failed, inFight = " + memberRoleLoadInfo2.getInFight());
								}
								
								// 回滚
								memberRoleLoadInfo1.setInFight((byte)0);
								
								resp.setResult(result);
								return resp;
							}
							
							result = checkCanAttend(1, memberRoleInfo2, memberRoleLoadInfo2, 0);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId2 + " match failed,result = " + result);
								}
								// 回滚
								memberRoleLoadInfo1.setInFight((byte)0);
								
								resp.setResult(result);
								return resp;
							}
							
							memberRoleLoadInfo2.setInFight((byte)8);
						}
						
						synchronized (roleInfo) {
							int result = matchCheckRoleStatus(roleId, roleLoadInfo, false);

							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + roleId + " match failed, inFight = " + roleLoadInfo.getInFight());
								}
								
								// 回滚
								memberRoleLoadInfo1.setInFight((byte)0);
								memberRoleLoadInfo2.setInFight((byte)0);
								
								resp.setResult(result);
								return resp;
							}
							
							roleLoadInfo.setInFight((byte) 8);
						}
						
						List<RoleInfo> members = new ArrayList<RoleInfo>();
						members.add(memberRoleInfo1);
						members.add(memberRoleInfo2);
						ComFightRequestReq comFightRequestReq = MutualBaseService.getFightMatchData(3, roleInfo, members);
						int result = PvpFightService.sendPvpCompetition(comFightRequestReq);

						if (result == 1) {
							SceneService.sendRoleRefreshMsg(new MutualLeftRefreshResp((byte) 4, roleId, memberRoleId1), memberRoleId1, Command.MUTUAL_LEAVE_MEMBER_RESP);
							SceneService.sendRoleRefreshMsg(new MutualLeftRefreshResp((byte) 4, roleId, memberRoleId2), memberRoleId2, Command.MUTUAL_LEAVE_MEMBER_RESP);
						} else {
							// 回滚
							memberRoleLoadInfo1.setInFight((byte)0);
							memberRoleLoadInfo2.setInFight((byte)0);
							roleLoadInfo.setInFight((byte) 0);
							
							resp.setResult(result);
							return resp;
						}
					} else {
						resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
						return resp;
					}
				} finally {
					if (flag1) {
						unLock(roleLoadInfo);
					}

					if (flag2) {
						unLock(memberRoleLoadInfo1);
					}

					if (flag3) {
						unLock(memberRoleLoadInfo2);
					}
				}
			} else if (memberRoleInfo1 != null || memberRoleInfo2 != null) {
				// 二人匹配
				memberRoleInfo1 = memberRoleInfo1 != null ? memberRoleInfo1 : memberRoleInfo2;
				memberRoleId1 = memberRoleInfo1.getId();
				
				synchronized (memberRoleInfo1) {
					RoleService.timerRecoverEnergy(memberRoleInfo1);
				}
				
				if (memberRoleInfo1.getEnergy() < GameValue.MUTUAL_FIGHT_COST_ENERGY_VALUE) {
					// 精力值不够
					resp.setResult(ErrorCode.MUTUAL_ENERGY_VALUE_ERROR);
					return resp;
				}

				RoleLoadInfo memberRoleLoadInfo1 = memberRoleInfo1.getRoleLoadInfo();

				if (memberRoleLoadInfo1 == null) {
					resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
					return resp;
				}

				boolean flag1 = tryLock(roleLoadInfo);
				boolean flag2 = tryLock(memberRoleLoadInfo1);

				try {
					if (flag1 && flag2) {
						if (roleLoadInfo.getLeaderRoleId() != roleId) {
							// 队伍已解散
							resp.setResult(ErrorCode.MUTUAL_LEADER_NOT_EXIST_ERROR);
							return resp;
						}

						if (roleLoadInfo.getMember1RoleId() != memberRoleInfo1.getId() && roleLoadInfo.getMember2RoleId() != memberRoleInfo1.getId()) {
							// 队员已离开
							resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
							return resp;
						}

						if (memberRoleLoadInfo1.getLeaderRoleId() != roleId) {
							// 队员已离开
							resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
							return resp;
						}
						
						synchronized (memberRoleInfo1) {
							int result = matchCheckRoleStatus(memberRoleId1, memberRoleLoadInfo1, false);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId1 + " match failed, inFight = " + memberRoleLoadInfo1.getInFight());
								}
								
								resp.setResult(result);
								return resp;
							}
							result = checkCanAttend(1, memberRoleInfo1, memberRoleLoadInfo1, 0);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId1 + " match failed,result = " + result);
								}
								
								resp.setResult(result);
								return resp;
							}
							
							memberRoleLoadInfo1.setInFight((byte)9);
						}
						
						synchronized (roleInfo) {
							int result = matchCheckRoleStatus(roleId, roleLoadInfo, false);

							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + roleId + " match failed, inFight = " + roleLoadInfo.getInFight());
								}
								
								// 回滚
								memberRoleLoadInfo1.setInFight((byte)0);
								
								resp.setResult(result);
								return resp;
							}
							
							roleLoadInfo.setInFight((byte) 9);
						}
						
						List<RoleInfo> members = new ArrayList<RoleInfo>();
						members.add(memberRoleInfo1);
						ComFightRequestReq comFightRequestReq = MutualBaseService.getFightMatchData(5, roleInfo, members);
						int result = PvpFightService.sendPvpCompetition(comFightRequestReq);

						if (result == 1) {
							SceneService.sendRoleRefreshMsg(new MutualLeftRefreshResp((byte) 4, roleId, memberRoleId1), memberRoleId1, Command.MUTUAL_LEAVE_MEMBER_RESP);
						} else {
							// 回滚
							memberRoleLoadInfo1.setInFight((byte)0);
							roleLoadInfo.setInFight((byte) 0);
							
							resp.setResult(result);
							return resp;
						}
					} else {
						resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
						return resp;
					}
				} finally {
					if (flag1) {
						unLock(roleLoadInfo);
					}

					if (flag2) {
						unLock(memberRoleLoadInfo1);
					}
				}
			} else {
				resp.setResult(ErrorCode.MUTUAL_MEMBER_IS_INAVALIABE_ERROR);
				return resp;
			}
		}

		resp.setResult(1);
		return resp;
	}

	/**
	 * 取消匹配
	 * 
	 * @param roleId 角色Id
	 * @return MutualCancelResp 响应Response
	 */
	public MutualCancelResp cancelMatch(int roleId) {
		MutualCancelResp resp = new MutualCancelResp();

		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
			return resp;
		}

		int otherRoleId1 = 0;
		int otherRoleId2 = 0;
		
		int leaderRoleId = 0;
		
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.ROLE_NOT_EXIST_ERROR_22);
				return resp;
			}
			
			if (!tryLock(roleLoadInfo)) {
				// 修改中
				resp.setResult(ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR);
				return resp;
			}
			
			try {
				if (roleLoadInfo.getInFight() == 0) {
					// 已取消
					resp.setResult(1);
					return resp;
				} else if (roleLoadInfo.getInFight() == 6) {
					roleLoadInfo.setInFight((byte) 0);
					roleLoadInfo.setCancelTime(System.currentTimeMillis());
					
					CompetitionService.cancelCompetition(roleInfo, (byte) 4, 0, 0);
				} else if (roleLoadInfo.getInFight() == 7 || roleLoadInfo.getInFight() == 10 || roleLoadInfo.getInFight() == 12) {
					// 已经开始战斗
					if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() < 5000) {
						// 已经开始战斗不能取消
						resp.setResult(1);
						return resp;
					} else {
						if (roleLoadInfo.getLeaderRoleId() != 0) {
							// 组队中
							closeMatch(roleLoadInfo, roleLoadInfo.getLeaderRoleId(), true, false);
						}
						
						roleLoadInfo.setInFight((byte) 0);
						roleLoadInfo.setFightServer(null);
						roleLoadInfo.setUuid(null);
						roleLoadInfo.setFightStartTime(0);
						roleLoadInfo.setCancelTime(System.currentTimeMillis());
					}
				} else if (roleLoadInfo.getInFight() == 11 || roleLoadInfo.getInFight() == 13 || roleLoadInfo.getInFight() == 14 || roleLoadInfo.getInFight() == 15
						|| roleLoadInfo.getInFight() == 8 || roleLoadInfo.getInFight() == 9) {
					// 取消匹配
					int fightType = 0;
					
					if(roleLoadInfo.getInFight() == 8){
						fightType = 3;
					} else if(roleLoadInfo.getInFight() == 9){
						fightType = 5;
					} else if(roleLoadInfo.getInFight() == 11){
						fightType = 6;
					} else if(roleLoadInfo.getInFight() == 13){
						fightType = 8;
					} else if(roleLoadInfo.getInFight() == 14){
						fightType = 9;
					} else if(roleLoadInfo.getInFight() == 15){
						fightType = 7;
					}
					CompetitionService.cancelCompetition(roleInfo, (byte) fightType, roleLoadInfo.getLeaderRoleId(), roleLoadInfo.getTeamDuplicateId());
					
					roleLoadInfo.setInFight((byte) 0);
					roleLoadInfo.setCancelTime(System.currentTimeMillis());
					
					if (roleLoadInfo.getLeaderRoleId() == roleId) {
						// 队长
						otherRoleId1 = roleLoadInfo.getMember1RoleId();
						otherRoleId2 = roleLoadInfo.getMember2RoleId();
						leaderRoleId = roleId;
					} else if (roleLoadInfo.getLeaderRoleId() != roleId) {
						// 队员
						otherRoleId1 = roleLoadInfo.getLeaderRoleId();
						leaderRoleId = otherRoleId1;
						RoleInfo leaderRoleInfo = RoleInfoMap.getRoleInfo(leaderRoleId);
						
						if (leaderRoleInfo != null) {
							if (leaderRoleInfo.getRoleLoadInfo() != null) {
								otherRoleId2 = leaderRoleInfo.getRoleLoadInfo().getMember1RoleId() == roleId ? leaderRoleInfo.getRoleLoadInfo().getMember2RoleId() : leaderRoleInfo.getRoleLoadInfo()
										.getMember1RoleId();
							}
						}
					}
				}
			} finally {
				unLock(roleLoadInfo);
			}
		}
		
		// 取消队友的匹配状态
		beCancelMatch(leaderRoleId, RoleInfoMap.getRoleInfo(otherRoleId1), RoleInfoMap.getRoleInfo(otherRoleId2));

		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 发送邀请加入队伍请求
	 * 
	 * @param req 请求Request
	 * @param roleId 角色Id
	 */
	public MutualInviteResultResp inviteSend(MutualInviteReq req, int roleId) {
		MutualInviteResultResp resp = new MutualInviteResultResp();
		
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

			if (!tryLock(roleLoadInfo)) {
				resp.setResult(ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR);
				return resp;
			}
			
			try {
				int leaderRoleId = roleLoadInfo.getLeaderRoleId();
				String roleName = null;
				
				if (roleLoadInfo.getLeaderRoleId() == roleId) {
					leaderRoleId = roleId;
					roleName = roleInfo.getRoleName();
					
					if (roleLoadInfo.getMember1RoleId() != 0 && roleLoadInfo.getMember2RoleId() != 0) {
						// 队伍已满
						if (logger.isWarnEnabled()) {
							logger.warn("inviteSend error : members is full");
						}

						resp.setResult(ErrorCode.MUTUAL_TEAM_FULL_ERROR);
						return resp;
					}
				} else {
					leaderRoleId = roleLoadInfo.getLeaderRoleId();
					RoleInfo leaderRoleInfo = RoleInfoMap.getRoleInfo(leaderRoleId);
					
					if (leaderRoleInfo != null) {
						roleName = leaderRoleInfo.getRoleName();
					}
				}

				RoleInfo memberRoleInfo = RoleInfoMap.getRoleInfo(req.getMemberRoleId());
				
				if (memberRoleInfo != null) {
					if (MapRoleInfoMap.getMapPointInfo(memberRoleInfo.getId()) != null) {
						resp.setResult(ErrorCode.MUTUAL_INVITE_OUT_OF_TOWN_ERROR);
						return resp;
					}
					
					RoleLoadInfo memberRoleLoadInfo = memberRoleInfo.getRoleLoadInfo();

					if (memberRoleLoadInfo != null) {
						if (memberRoleLoadInfo.getLeaderRoleId() != 0 || memberRoleLoadInfo.getMember1RoleId() != 0 || memberRoleLoadInfo.getMember2RoleId() != 0) {
							// 在其他队伍中
							resp.setResult(ErrorCode.MUTUAL_INVITE_IN_OTHER_TEAM_ERROR);
							return resp;
						}
						
						int result = checkCanAttend(req.getActivityType(), memberRoleInfo, memberRoleLoadInfo, req.getDuplicateId());
						
						if(result != 1){
							resp.setResult(result);
							return resp;
						}
						
						if (memberRoleLoadInfo.getInFight() != 0 || FightInfoMap.getFightInfoByRoleId(req.getMemberRoleId()) != null) {
							// 在战斗中
							if (logger.isWarnEnabled()) {
								logger.warn("inviteSend error : members is in fight, inFight = " + memberRoleLoadInfo.getInFight());
							}
							
							resp.setResult(ErrorCode.MUTUAL_INVITE_IN_FIGHT_ERROR);
							return resp;
						}
						
						if (matchCheckRoleStatus(memberRoleInfo.getId(), memberRoleLoadInfo, false) == 1 && FightInfoMap.getFightInfoByRoleId(memberRoleInfo.getId()) == null) {
							// 对方不在战斗中
							if (memberRoleLoadInfo.getLeaderRoleId() == 0 && leaderRoleId != 0 && roleName != null) {
								// 对方不在其他队伍中
								
								MutualInviteResp mutualInviteResp = new MutualInviteResp();
								mutualInviteResp.setLeaderRoleId(leaderRoleId);
								mutualInviteResp.setLeaderRoleName(roleName);
								mutualInviteResp.setActivityType(req.getActivityType());
								
								// 刷新到客户端添加队伍通知
								SceneService.sendRoleRefreshMsg(mutualInviteResp, req.getMemberRoleId(), Command.MUTUAL_INVITE_TEAM_RESP);
							} else {
								if (logger.isWarnEnabled()) {
									logger.warn("inviteSend error : members is in team, leaderRoleId = " + memberRoleLoadInfo.getLeaderRoleId());
								}
								
								resp.setResult(ErrorCode.MUTUAL_INVITE_IN_OTHER_TEAM_ERROR);
								return resp;
							}
						} else {
							if (logger.isWarnEnabled()) {
								logger.warn("inviteSend error : members is in fight, inFight = " + memberRoleLoadInfo.getInFight());
							}
							
							resp.setResult(ErrorCode.MUTUAL_INVITE_IN_FIGHT_ERROR);
							return resp;
						}
					} else {
						if (logger.isWarnEnabled()) {
							logger.warn("inviteSend error : members is not online");
						}
						
						resp.setResult(ErrorCode.BIAO_CHE_ERROR_31);
						return resp;
					}
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn("inviteSend error : members RoleInfo is null");
					}
				}
			} finally {
				unLock(roleLoadInfo);
			}
		}
		
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 世界频道分享加入队伍
	 * 
	 * @param roleId	角色Id
	 * @param activityType 活动类型 1-长坂坡 2-组队副本 3-3V3竞技场
	 * @param duplicateId 
	 */
	public void shareTeam(int roleId, int activityType, int duplicateId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			return;
		}
		
		if(activityType != 1 && activityType != 2 && activityType != 3){
		    return;
		}

		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				return;
			}
			
			if (System.currentTimeMillis() - roleLoadInfo.getSendMessageTime() < 10000) {
				return;
			}

			int result = checkCanAttend(activityType, roleInfo, roleLoadInfo, duplicateId);
			
			if(result != 1){
				return ;
			}
			
			if (!tryLock(roleLoadInfo)) {
				return;
			}

			try {
				RoleInfo leaderRoleInfo = null;
				int leaderRoleId = roleLoadInfo.getLeaderRoleId();
				
				if (roleLoadInfo.getLeaderRoleId() == roleId) {
					leaderRoleInfo = roleInfo;
				} else {
					leaderRoleId = roleLoadInfo.getLeaderRoleId();
					leaderRoleInfo = RoleInfoMap.getRoleInfo(leaderRoleId);
				}

				if (leaderRoleInfo != null) {
					MutualBaseService.broadcast(leaderRoleInfo, HeroInfoMap.getMainHeroLv(leaderRoleId), activityType, duplicateId);
				}
				
				roleLoadInfo.setSendMessageTime(System.currentTimeMillis());
			} finally {
				unLock(roleLoadInfo);
			}
		}
	}
	
	/**
	 * 角色修改副将结束，准备刷新到其他队员
	 * 
	 * @param roleId	角色Id
	 */
	public void changeHero(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			return;
		}
		
		RoleLoadInfo roleLoadInfo = null;
		MutualRefershResp resp = null;
		
		synchronized (roleInfo) {
			roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				return;
			}
			
			if (roleLoadInfo.getLeaderRoleId() == 0) {
				return;
			}
			
			resp = MutualBaseService.createMutualRefershResp(roleInfo);
		}
		
		int refreshRoleId1 = 0;
		int refreshRoleId2 = 0;
		
		if (roleLoadInfo.getLeaderRoleId() == roleId) {
			refreshRoleId1 = roleLoadInfo.getMember1RoleId();
			refreshRoleId2 = roleLoadInfo.getMember2RoleId();
		} else {
			refreshRoleId1 = roleLoadInfo.getLeaderRoleId();
			RoleInfo leaderRoleInfo = RoleInfoMap.getRoleInfo(roleLoadInfo.getLeaderRoleId());
			
			if (leaderRoleInfo != null) {
				if (leaderRoleInfo.getRoleLoadInfo() != null) {
					refreshRoleId2 = leaderRoleInfo.getRoleLoadInfo().getMember1RoleId() == roleId ? leaderRoleInfo.getRoleLoadInfo().getMember2RoleId() : leaderRoleInfo.getRoleLoadInfo().getMember1RoleId();
				}
			}
		}
		
		SceneService.sendRoleRefreshMsg(resp, roleId, Command.MUTUAL_CHANGE_HERO_RESP);
		
		if (refreshRoleId1 != 0) {
			SceneService.sendRoleRefreshMsg(resp, refreshRoleId1, Command.MUTUAL_CHANGE_HERO_RESP);
		}
		
		if (refreshRoleId2 != 0) {
			SceneService.sendRoleRefreshMsg(resp, refreshRoleId2, Command.MUTUAL_CHANGE_HERO_RESP);
		}
	}
	
	/**
	 * 检查角色当前战斗状态
	 * 
	 * @param roleId		角色Id
	 * @param roleLoadInfo	角色信息
	 * @return	int 1-正常 otherwise-不正常
	 */
	public int matchCheckRoleStatus(int roleId, RoleLoadInfo roleLoadInfo, boolean isInitiative) {
		if (roleLoadInfo.getInFight() == 0) {
			// 状态正常
			if ((System.currentTimeMillis() - roleLoadInfo.getCancelTime()) < 5000) {
				// 过于频繁报名，取消报名和下次报名需要间隔2秒
				return ErrorCode.FIGHT_MATCH_TOO_BUSY_ERROR;
			}
		} else if (roleLoadInfo.getInFight() == 6 || roleLoadInfo.getInFight() == 8 || roleLoadInfo.getInFight() == 9
				|| roleLoadInfo.getInFight() == 11 || roleLoadInfo.getInFight() == 13 
				|| roleLoadInfo.getInFight() == 14 || roleLoadInfo.getInFight() == 15) {
			// 已报名
			return ErrorCode.MUTUAL_MEMBER_MATCH_STATUS_ERROR;
		} else if (roleLoadInfo.getInFight() == 2) {
			// 竞技场战斗中
			if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() < 60000) {
				logger.warn("MutualService.match : role is in fighting, roleId = " + roleId + " inFight = 2");
				return ErrorCode.FIGHT_IN_FIGHT_ERROR;
			}
		} else if (roleLoadInfo.getInFight() == 7 || roleLoadInfo.getInFight() == 10 || roleLoadInfo.getInFight() == 12) {
			// 对攻战战斗中
			if (isInitiative) {
				if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() > 5000) {
					roleLoadInfo.setUuid(null);
					roleLoadInfo.setInFight((byte)0);
					roleLoadInfo.setFightStartTime(0);
				} else {
					return ErrorCode.FIGHT_IN_FIGHT_ERROR;
				}
			} else {
				return ErrorCode.MUTUAL_MEMBER_FIGHT_STATUS_ERROR;
			}
		} else if (roleLoadInfo.getInFight() == 4) {
			// 地图战斗中
			if (System.currentTimeMillis() - roleLoadInfo.getFightStartTime() < 60000) {
				logger.warn("MutualService.match : role is in fighting, roleId = " + roleId + " inFight = 4");
				return ErrorCode.FIGHT_IN_FIGHT_ERROR;
			}
		} else {
			// 不正常状态
			if (logger.isWarnEnabled()) {
				logger.warn("mutual match error : roleId = " + roleId + " inFight = " + roleLoadInfo.getInFight());
			}

			return ErrorCode.MUTUAL_STATUS_ERROR;
		}

		return 1;
	}

	public int joinTeam(RoleLoadInfo leaderRoleLoadInfo, int leaderRoleId, RoleLoadInfo roleLoadInfo, int roleId) {
		if (leaderRoleLoadInfo.getLeaderRoleId() != leaderRoleId) {
			// 队伍已解散
			return ErrorCode.MUTUAL_LEADER_NOT_EXIST_ERROR;
		}

		if (roleLoadInfo.getLeaderRoleId() == leaderRoleId) {
			// 已加入此队伍
			return ErrorCode.MUTUAL_IS_IN_TEAM_ERROR;
		}

		if (leaderRoleLoadInfo.getMember1RoleId() != 0 && leaderRoleLoadInfo.getMember2RoleId() != 0 && leaderRoleLoadInfo.getMember1RoleId() != roleId
				&& leaderRoleLoadInfo.getMember2RoleId() != roleId) {
			// 已经满员
			return ErrorCode.MUTUAL_TEAM_FULL_ERROR;
		}

		int memberRoleId = 0;

		if (leaderRoleLoadInfo.getMember1RoleId() != 0 && leaderRoleLoadInfo.getMember1RoleId() != roleId) {
			memberRoleId = leaderRoleLoadInfo.getMember1RoleId();
		}

		if (leaderRoleLoadInfo.getMember2RoleId() != 0 && leaderRoleLoadInfo.getMember2RoleId() != roleId) {
			memberRoleId = leaderRoleLoadInfo.getMember2RoleId();
		}

		if (leaderRoleLoadInfo.getMember1RoleId() == 0) {
			leaderRoleLoadInfo.setMember1RoleId(roleId);
		} else if (leaderRoleLoadInfo.getMember2RoleId() == 0) {
			leaderRoleLoadInfo.setMember2RoleId(roleId);
		}

		MutualRefershResp resp = MutualBaseService.createMutualRefershResp(RoleInfoMap.getRoleInfo(roleId));

		// 刷新给队长增加队员
		SceneService.sendRoleRefreshMsg(resp, leaderRoleId, Command.MUTUAL_ADD_MEMBER_RESP);

		if (memberRoleId > 0) {
			// 存在其他队员
			RoleInfo memberRoleInfo = RoleInfoMap.getRoleInfo(memberRoleId);

			if (memberRoleInfo != null) {
				// 刷新给队员增加队员
				SceneService.sendRoleRefreshMsg(resp, memberRoleId, Command.MUTUAL_ADD_MEMBER_RESP);
			}
		}

		roleLoadInfo.setLeaderRoleId(leaderRoleId);
		roleLoadInfo.setMember1RoleId(0);
		roleLoadInfo.setMember2RoleId(0);

		return 1;
	}

	private int makeLeave(RoleLoadInfo roleLoadInfo, int roleId, RoleLoadInfo memberRoleLoadInfo, int memberRoleId) {
		if (!tryLock(roleLoadInfo)) {
			// 修改中
			return ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR;
		}
		
		try {
			if (roleLoadInfo.getLeaderRoleId() != roleId) {
				// 队伍已解散
				if (logger.isWarnEnabled()) {
					logger.warn("makeLeave error : roleId = " + roleId + " leaderId = " + roleLoadInfo.getLeaderRoleId() + " is not match");
				}

				return ErrorCode.MUTUAL_LEADER_NOT_EXIST_ERROR;
			}

			if (roleLoadInfo.getMember1RoleId() == memberRoleId) {
				roleLoadInfo.setMember1RoleId(0);
			} else if (roleLoadInfo.getMember2RoleId() == memberRoleId) {
				roleLoadInfo.setMember2RoleId(0);
			} else {
				// 此队员已离开
				if (logger.isWarnEnabled()) {
					logger.warn("makeLeave error : roleId = " + roleId + " is leave team");
				}
			}

			int otherMemberRoleId = roleLoadInfo.getMember1RoleId() > 0 ? roleLoadInfo.getMember1RoleId() : roleLoadInfo.getMember2RoleId();

			if (otherMemberRoleId > 0) {
				// 存在其他队员,通知队员离开
				if (RoleInfoMap.getRoleInfo(otherMemberRoleId) != null) {
					SceneService.sendRoleRefreshMsg(new MutualLeftRefreshResp((byte) 2, roleId, memberRoleId), otherMemberRoleId, Command.MUTUAL_LEAVE_MEMBER_RESP);
				}
			}

			if (memberRoleLoadInfo != null) {
				SceneService.sendRoleRefreshMsg(new MutualLeftRefreshResp((byte) 3, roleId, memberRoleId), memberRoleId, Command.MUTUAL_LEAVE_MEMBER_RESP);

				if (!tryLock(memberRoleLoadInfo)) {
					// 修改中
					if (logger.isWarnEnabled()) {
						logger.warn("makeLeave error : memberRoleId = " + memberRoleId + " is in use");
					}
					
					return ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR;
				}
				
				try {
					if (memberRoleLoadInfo.getLeaderRoleId() != roleId) {
						// 已离开队伍
						if (logger.isWarnEnabled()) {
							logger.warn("makeLeave error : memberRoleId = " + memberRoleId + " is left");
						}

						return 1;
					} else {
						memberRoleLoadInfo.setLeaderRoleId(0);
						memberRoleLoadInfo.setMember1RoleId(0);
						memberRoleLoadInfo.setMember2RoleId(0);
					}
				} finally {
					unLock(memberRoleLoadInfo);
				}
			}
		} finally {
			unLock(roleLoadInfo);
		}

		return 1;
	}

	public int leaveTeam(RoleLoadInfo roleLoadInfo, int roleId, boolean needNotify) {
		RoleInfo oldLeaderRoleInfo = RoleInfoMap.getRoleInfo(roleLoadInfo.getLeaderRoleId());

		MutualLeftRefreshResp resp = new MutualLeftRefreshResp((byte) 2, roleLoadInfo.getLeaderRoleId(), roleId);

		try {
			if (oldLeaderRoleInfo == null) {
				return 1;
			}

			RoleLoadInfo leaderRoleLoadInfo = oldLeaderRoleInfo.getRoleLoadInfo();

			if (leaderRoleLoadInfo == null) {
				return 1;
			}
			
			if (!tryLock(leaderRoleLoadInfo)) {
				// 修改中
				return ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR;
			}

			try {
				if (leaderRoleLoadInfo.getLeaderRoleId() != oldLeaderRoleInfo.getId()) {
					// 队伍已解散
					return 1;
				}

				if (leaderRoleLoadInfo.getLeaderRoleId() != roleLoadInfo.getLeaderRoleId()) {
					// 已离开此队
					return 1;
				}

				if (leaderRoleLoadInfo.getMember1RoleId() == roleId) {
					leaderRoleLoadInfo.setMember1RoleId(0);
				} else if (leaderRoleLoadInfo.getMember2RoleId() == roleId) {
					leaderRoleLoadInfo.setMember2RoleId(0);
				} else {
					return 1;
				}

				if (needNotify) {
					// 通知队长，队员离开离开
					SceneService.sendRoleRefreshMsg(resp, oldLeaderRoleInfo.getId(), Command.MUTUAL_LEAVE_MEMBER_RESP);
				}

				int memberRoleId = leaderRoleLoadInfo.getMember1RoleId() > 0 ? leaderRoleLoadInfo.getMember1RoleId() : leaderRoleLoadInfo.getMember2RoleId();

				if (memberRoleId > 0 && needNotify) {
					// 存在其他队员,通知队员离开
					SceneService.sendRoleRefreshMsg(resp, memberRoleId, Command.MUTUAL_LEAVE_MEMBER_RESP);
				}
			} finally {
				unLock(leaderRoleLoadInfo);
			}
		} finally {
			roleLoadInfo.setLeaderRoleId(0);
			roleLoadInfo.setMember1RoleId(0);
			roleLoadInfo.setMember2RoleId(0);
		}

		return 1;
	}

	/**
	 * 解散队伍
	 * 
	 * @param leaderRoleLoadInfo 队长信息
	 * @return 处理结果 1-成功 otherwise-失败
	 */
	public int breakTeam(RoleLoadInfo leaderRoleLoadInfo, int roleId, boolean needNotify) {
		try {
			int[] memberRoleIds = new int[2];

			memberRoleIds[0] = leaderRoleLoadInfo.getMember1RoleId();
			memberRoleIds[1] = leaderRoleLoadInfo.getMember2RoleId();

			MutualLeftRefreshResp resp = new MutualLeftRefreshResp((byte) 1, roleId, roleId);

			for (int memberRoleId : memberRoleIds) {
				if (memberRoleId > 0) {
					RoleInfo memberRoleInfo = RoleInfoMap.getRoleInfo(memberRoleId);

					if (memberRoleInfo != null) {
						RoleLoadInfo memberRoleLoadInfo = memberRoleInfo.getRoleLoadInfo();

						if (memberRoleLoadInfo != null) {
							if (!tryLock(memberRoleLoadInfo)) {
								// 修改中
								continue;
							}
							
							try {
								if (memberRoleLoadInfo.getLeaderRoleId() == roleId) {
									memberRoleLoadInfo.setLeaderRoleId(0);

									if (needNotify) {
										// 刷新解散队伍
										SceneService.sendRoleRefreshMsg(resp, memberRoleId, Command.MUTUAL_LEAVE_MEMBER_RESP);
									}
								}
							} finally {
								unLock(memberRoleLoadInfo);
							}
						}
					}
				}
			}
		} finally {
			leaderRoleLoadInfo.setMember1RoleId(0);
			leaderRoleLoadInfo.setMember2RoleId(0);
			leaderRoleLoadInfo.setLeaderRoleId(0);
		}

		return 1;
	}

	/**
	 * 关闭组队面板
	 * 
	 * @param roleLoadInfo	角色信息
	 * @param roleId		角色Id
	 * @param force			是否检查修改锁，如果是正常关闭需要检查锁，如果是直接下线操作强制修改不检查锁
	 * @param needNotify	是否需要通知
	 * @return	int 1-成功 otherwise-失败
	 */
	public int closeMatch(RoleLoadInfo roleLoadInfo, int roleId, boolean force, boolean needNotify) {
		if (!force) {
			if (!tryLock(roleLoadInfo)) {
				// 队伍信息正被修改中
				if (logger.isWarnEnabled()) {
					logger.warn("closeMatch error : roleId = " + roleId + " is in use");
				}
				
				return ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR;
			}
		}
		
		try {
			try {
				if (roleLoadInfo.getLeaderRoleId() != 0) {
					if (roleLoadInfo.getLeaderRoleId() == roleId) {
						// 原来是队长，解散队伍
						breakTeam(roleLoadInfo, roleId, needNotify);
					} else {
						// 原来是队员，离开原队伍
						leaveTeam(roleLoadInfo, roleId, needNotify);
					}
				}
			} finally {
				roleLoadInfo.setLeaderRoleId(0);
				roleLoadInfo.setMember1RoleId(0);
				roleLoadInfo.setMember2RoleId(0);
			}
		} finally {
			if (!force) {
				unLock(roleLoadInfo);
			}
		}

		return 1;
	}
	
	/**
	 * 修改队友的匹配状态
	 * 
	 * @param roleId	取消匹配的角色Id
	 * @param roleInfos	其他受影响角色集合
	 */
	private void beCancelMatch(int leaderRoleId, RoleInfo ... roleInfos) {
		for (RoleInfo roleInfo : roleInfos) {
			if (roleInfo != null) {
				synchronized (roleInfo) {
					if (roleInfo.getRoleLoadInfo() != null) {
						if ((roleInfo.getRoleLoadInfo().getInFight() == 8 || roleInfo.getRoleLoadInfo().getInFight() == 9
								|| roleInfo.getRoleLoadInfo().getInFight() == 11 || roleInfo.getRoleLoadInfo().getInFight() == 14 
								|| roleInfo.getRoleLoadInfo().getInFight() == 15) 
								&& (roleInfo.getRoleLoadInfo().getLeaderRoleId() == leaderRoleId || roleInfo.getRoleLoadInfo().getLeaderRoleId() == 0)) {
							roleInfo.getRoleLoadInfo().setInFight((byte) 0);
							roleInfo.getRoleLoadInfo().setUuid(null);
							roleInfo.getRoleLoadInfo().setCancelTime(System.currentTimeMillis());
							
							if (roleInfo.getRoleLoadInfo().getLeaderRoleId() == roleInfo.getId()) {
								SceneService.sendRoleRefreshMsg(new MutualLeftRefreshResp((byte) 5, leaderRoleId, roleInfo.getId()), roleInfo.getId(), Command.MUTUAL_LEAVE_MEMBER_RESP);
							} else {
								SceneService.sendRoleRefreshMsg(new MutualLeftRefreshResp((byte) 5, leaderRoleId, roleInfo.getId()), roleInfo.getId(), Command.MUTUAL_LEAVE_MEMBER_RESP);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 角色下线处理
	 * 
	 * @param roleInfo	下线角色信息
	 */
	public void roleLogout(RoleInfo roleInfo) {
		int otherRoleId1 = 0;
		int otherRoleId2 = 0;
		int leaderRoleId = 0;

		synchronized (roleInfo) {
			if (roleInfo.getRoleLoadInfo() != null && roleInfo.getRoleLoadInfo().getLeaderRoleId() != 0) {
				if (roleInfo.getRoleLoadInfo().getLeaderRoleId() == roleInfo.getId()) {
					// 队长
					otherRoleId1 = roleInfo.getRoleLoadInfo().getMember1RoleId();
					otherRoleId2 = roleInfo.getRoleLoadInfo().getMember2RoleId();
					leaderRoleId = roleInfo.getId();
				} else if (roleInfo.getRoleLoadInfo().getLeaderRoleId() != roleInfo.getId()) {
					// 队员
					otherRoleId1 = roleInfo.getRoleLoadInfo().getLeaderRoleId();
					leaderRoleId = otherRoleId1;
					RoleInfo leaderRoleInfo = RoleInfoMap.getRoleInfo(otherRoleId1);
					
					if (leaderRoleInfo != null) {
						if (leaderRoleInfo.getRoleLoadInfo() != null) {
							otherRoleId2 = leaderRoleInfo.getRoleLoadInfo().getMember1RoleId() == roleInfo.getId() ? leaderRoleInfo.getRoleLoadInfo().getMember2RoleId() : leaderRoleInfo.getRoleLoadInfo()
									.getMember1RoleId();
						}
					}
				}
			}
		}
		
		// 清除队友的报名状态
		if (leaderRoleId > 0) {
			beCancelMatch(leaderRoleId, RoleInfoMap.getRoleInfo(otherRoleId1), RoleInfoMap.getRoleInfo(otherRoleId2));
		}
		
		synchronized (roleInfo) {
			// 离开队伍并清除自己的报名状态
			if (roleInfo.getRoleLoadInfo() != null) {
				// 强制关闭组队界面,离开队伍
				closeMatch(roleInfo.getRoleLoadInfo(), roleInfo.getId(), true, true);
				
				if (roleInfo.getRoleLoadInfo().getInFight() == 1 || roleInfo.getRoleLoadInfo().getInFight() == 6 || roleInfo.getRoleLoadInfo().getInFight() == 8 || roleInfo.getRoleLoadInfo().getInFight() == 9
						|| roleInfo.getRoleLoadInfo().getInFight() == 11 || roleInfo.getRoleLoadInfo().getInFight() == 13
						|| roleInfo.getRoleLoadInfo().getInFight() == 14 || roleInfo.getRoleLoadInfo().getInFight() == 15) {
					// 匹配中
					if (roleInfo.getRoleLoadInfo().getInFight() == 1) {
						CompetitionService.cancelCompetition(roleInfo, (byte) 1, 0, 0);
					} else if (roleInfo.getRoleLoadInfo().getInFight() == 6) {
						CompetitionService.cancelCompetition(roleInfo, (byte) 4, 0, 0);
					} else if (roleInfo.getRoleLoadInfo().getInFight() == 8) {
						CompetitionService.cancelCompetition(roleInfo, (byte) 3, 0, 0);
					} else if (roleInfo.getRoleLoadInfo().getInFight() == 9) {
						CompetitionService.cancelCompetition(roleInfo, (byte) 5, 0, 0);
					} else if (roleInfo.getRoleLoadInfo().getInFight() == 11) {
						CompetitionService.cancelCompetition(roleInfo, (byte) 6, leaderRoleId, roleInfo.getRoleLoadInfo().getTeamDuplicateId());
					} else if (roleInfo.getRoleLoadInfo().getInFight() == 13) {
						CompetitionService.cancelCompetition(roleInfo, (byte) 8, 0, 0);
					} else if (roleInfo.getRoleLoadInfo().getInFight() == 14) {
						CompetitionService.cancelCompetition(roleInfo, (byte) 9, 0, 0);
					} else if (roleInfo.getRoleLoadInfo().getInFight() == 15) {
						CompetitionService.cancelCompetition(roleInfo, (byte) 7, 0, 0);
					}
					
					roleInfo.getRoleLoadInfo().setInFight((byte) 0);
					roleInfo.getRoleLoadInfo().setUuid(null);
					roleInfo.getRoleLoadInfo().setCancelTime(System.currentTimeMillis());
				}
			}
		}
	}
	
	/**
	 * 检查对攻战时间
	 * 
	 * @return	boolean false-符合时间区间 true-不符合时间区间
	 */
	public boolean notMatchTime(Date startTime, Date endTime) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2000);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		Date curDate = c.getTime();
		if (endTime.before(startTime)) {
			// 跨越24点
			if (curDate.before(startTime) && curDate.after(endTime)) {
				return true;
			}
		} else {
			if (curDate.before(startTime) || curDate.after(endTime)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean tryLock(RoleLoadInfo roleLoadInfo) {
		return roleLoadInfo.getLock().compareAndSet(false, true);
	}

	public void unLock(RoleLoadInfo roleLoadInfo) {
		roleLoadInfo.getLock().compareAndSet(true, false);
	}
}
