package com.snail.webgame.game.protocal.fight.team.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.TeamChallengeRecord;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.TeamChallengeRecordSub;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualLeftRefreshResp;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;
import com.snail.webgame.game.protocal.fight.team.match.TeamMatchReq;
import com.snail.webgame.game.protocal.fight.team.match.TeamMatchResp;
import com.snail.webgame.game.protocal.fight.team.rank.TeamQueryRankResp;
import com.snail.webgame.game.protocal.fight.team.rank.TeamRoleRankRes;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.service.PvpFightService;

/**
 * 
 * 类介绍:PVP对攻战单人和组队面板操作业务类
 * 对于组队修改和读取多人数据保持同步，不使用synchronized全局锁，改用AtomicBoolean值的检查保持弱锁定，
 * 可以被嵌套在其他synchronized关键字中使用,但是有可能出现修改时被锁定，则修改失败
 *
 * @author zhoubo
 * @2015年6月5日
 */
public class TeamService {

	private static TeamService teamService = new TeamService();

	private TeamService() {

	}

	public synchronized static TeamService getTeamService() {
		return teamService;
	}

	private static Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 开始匹配,支持单人匹配和组队匹配
	 * 
	 * @param req 请求Request
	 * @param roleId 角色Id
	 * @return MutualMatchResp 响应Response
	 */
	public TeamMatchResp match(TeamMatchReq req, int roleId) {
		TeamMatchResp resp = new TeamMatchResp();

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
			
			//检查能否进行PVP战斗
			if(GameValue.PVE_FIGHT_FLAG == 0)
			{
				resp.setResult(ErrorCode.MUTUAL_STATUS_ERROR);
				return resp;
			}
			
			int result = MutualService.getMutualService().checkCanAttend(req.getActivityType() == 1 ? 2 : 3, roleInfo, roleLoadInfo, req.getDuplicateNo());
			
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			
			// 检查角色状态
			result = MutualService.getMutualService().matchCheckRoleStatus(roleId, roleLoadInfo, true);

			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			
			if (req.getActivityType() != 1 && req.getActivityType() != 2 && req.getMatchType() != 1 && req.getMatchType() != 2) {
				resp.setResult(ErrorCode.MUTUAL_PARAMETER_ERROR);
				return resp;
			}
			
			if (req.getMatchType() == 1) {
				// 单人匹配
				if (roleLoadInfo.getLeaderRoleId() != 0 && roleLoadInfo.getLeaderRoleId() != roleId) {
					// 处于其他队伍中(自己是队员)
					
					if (logger.isWarnEnabled()) {
						logger.warn("team match error : roleId = " + roleId + " signal match leaderId = " + roleLoadInfo.getLeaderRoleId() + "avtivityType=" + req.getActivityType());
					}
					
					if (!tryLock(roleLoadInfo)) {
						// 修改中
						resp.setResult(ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR);
						return resp;
					}

					try {
						MutualService.getMutualService().leaveTeam(roleLoadInfo, roleId, true);
					} finally {
						unLock(roleLoadInfo);
					}
				} else if (roleLoadInfo.getLeaderRoleId() != 0 && roleLoadInfo.getLeaderRoleId() == roleId) {
					// 处于其他队伍中(自己是队长)
					
					if (logger.isWarnEnabled()) {
						logger.warn("mutual match error : roleId = " + roleId + " signal match leaderId = " + roleLoadInfo.getLeaderRoleId() + "avtivityType=" + req.getActivityType());
					}

					if (!tryLock(roleLoadInfo)) {
						// 修改中
						resp.setResult(ErrorCode.MUTUAL_IS_IN_OPERATION_ERROR);
						return resp;
					}
					
					try {
						MutualService.getMutualService().breakTeam(roleLoadInfo, roleId, true);
					} finally {
						unLock(roleLoadInfo);
					}
				}
				
				int fightType = 0;
				
				if (req.getActivityType() == 1) {
					fightType = 6;
				} else if(req.getActivityType() == 2){
					fightType = 8;
				}
				
				ComFightRequestReq comFightRequestReq = TeamBaseService.getFightMatchData(fightType, roleInfo, null, req.getDuplicateNo());
				result = PvpFightService.sendPvpCompetition(comFightRequestReq);

				if (result == 1) {
					if(fightType == 6){
						roleLoadInfo.setInFight((byte) 11);
					} else if(fightType == 8){
						roleLoadInfo.setInFight((byte) 13);
					}
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
							int result = MutualService.getMutualService().matchCheckRoleStatus(memberRoleId1, memberRoleLoadInfo1, false);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId1 + " match failed, inFight = " + memberRoleLoadInfo1.getInFight() + "avtivityType=" + req.getActivityType());
								}
								
								resp.setResult(result);
								return resp;
							}
							result = MutualService.getMutualService().checkCanAttend(req.getActivityType() == 1 ? 2 : 3, memberRoleInfo1, memberRoleLoadInfo1, req.getDuplicateNo());
							
							if (result != 1) {
								resp.setResult(result);
								return resp;
							}
							
							if (req.getActivityType() == 1) {
								memberRoleLoadInfo1.setInFight((byte) 11);
							} else if(req.getActivityType() == 2){
								memberRoleLoadInfo1.setInFight((byte) 15);
							}
						}
						
						synchronized (memberRoleInfo2) {
							int result = MutualService.getMutualService().matchCheckRoleStatus(memberRoleId2, memberRoleLoadInfo2, false);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId2 + " match failed, inFight = " + memberRoleLoadInfo2.getInFight() + "avtivityType=" + req.getActivityType());
								}
								
								// 回滚
								memberRoleLoadInfo1.setInFight((byte)0);
								
								resp.setResult(result);
								return resp;
							}
							result = MutualService.getMutualService().checkCanAttend(req.getActivityType() == 1 ? 2 : 3, memberRoleInfo2, memberRoleLoadInfo2, req.getDuplicateNo());
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId2 + " match failed, inFight = " + memberRoleLoadInfo2.getInFight() + "avtivityType=" + req.getActivityType() + " result = " + result);
								}
								
								// 回滚
								memberRoleLoadInfo1.setInFight((byte)0);
								
								resp.setResult(result);
								return resp;
							}
							
							if (req.getActivityType() == 1) {
								memberRoleLoadInfo2.setInFight((byte) 11);
							} else if(req.getActivityType() == 2){
								memberRoleLoadInfo2.setInFight((byte) 15);
							}
						}
						
						synchronized (roleInfo) {
							int result = MutualService.getMutualService().matchCheckRoleStatus(roleId, roleLoadInfo, false);

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
							
							if (req.getActivityType() == 1) {
								roleLoadInfo.setInFight((byte) 11);
							} else if(req.getActivityType() == 2){
								roleLoadInfo.setInFight((byte) 15);
							}
						}
						
						if(req.getActivityType() == 1) {
							// 组队副本3人，直接连战斗服务器

							List<ComFightRequestReq> list = new ArrayList<ComFightRequestReq>();
							list.add(TeamBaseService.warpFightDataReq(6, roleInfo, req.getDuplicateNo(), (byte) 1));
							list.add(TeamBaseService.warpFightDataReq(6, memberRoleInfo1, req.getDuplicateNo(), (byte) 1));
							list.add(TeamBaseService.warpFightDataReq(6, memberRoleInfo2, req.getDuplicateNo(), (byte) 1));
							
							int result = PvpFightService.sendPvpMap(list, (byte) 6);
							
							if (result != 1) {
								// 回滚
								memberRoleLoadInfo1.setInFight((byte)0);
								memberRoleLoadInfo2.setInFight((byte)0);
								roleLoadInfo.setInFight((byte) 0);
								
								resp.setResult(ErrorCode.FIGHT_SERVER_INAVAILABLE);
								return resp;
							}
						} else if(req.getActivityType() == 2) {
							// 竞技场3V3还是需要匹配的
							List<RoleInfo> members = new ArrayList<RoleInfo>();
							members.add(memberRoleInfo1);
							members.add(memberRoleInfo2);
							ComFightRequestReq comFightRequestReq = TeamBaseService.getFightMatchData(7, roleInfo, members, req.getDuplicateNo());
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
							int result = MutualService.getMutualService().matchCheckRoleStatus(memberRoleId1, memberRoleLoadInfo1, false);
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId1 + " match failed, inFight = " + memberRoleLoadInfo1.getInFight() + "avtivityType=" + req.getActivityType());
								}
								
								resp.setResult(result);
								return resp;
							}
							
							result = MutualService.getMutualService().checkCanAttend(req.getActivityType() == 1 ? 2 : 3, memberRoleInfo1, memberRoleLoadInfo1, req.getDuplicateNo());
							
							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + memberRoleId1 + " match failed, inFight = " + memberRoleLoadInfo1.getInFight() + "avtivityType=" + req.getActivityType() + " result = " + result);
								}
								
								resp.setResult(result);
								return resp;
							}
							
							if (req.getActivityType() == 1) {
								memberRoleLoadInfo1.setInFight((byte) 11);
							} else if(req.getActivityType() == 2){
								memberRoleLoadInfo1.setInFight((byte) 14);
							}
						}
						
						synchronized (roleInfo) {
							int result = MutualService.getMutualService().matchCheckRoleStatus(roleId, roleLoadInfo, false);

							if (result != 1) {
								if (logger.isWarnEnabled()) {
									logger.warn("mutual match error : roleId = " + roleId + " match failed, inFight = " + roleLoadInfo.getInFight());
								}
								
								// 回滚
								memberRoleLoadInfo1.setInFight((byte)0);
								
								resp.setResult(result);
								return resp;
							}

							if (req.getActivityType() == 1) {
								roleLoadInfo.setInFight((byte) 11);
							} else if(req.getActivityType() == 2){
								roleLoadInfo.setInFight((byte) 14);
							}
						}
						
						int fightType = 0;
						
						if (req.getActivityType() == 1) {
							fightType = 6;
						} else if(req.getActivityType() == 2){
							fightType = 9;
						}
						
						List<RoleInfo> members = new ArrayList<RoleInfo>();
						members.add(memberRoleInfo1);
						ComFightRequestReq comFightRequestReq = TeamBaseService.getFightMatchData(fightType, roleInfo, members, req.getDuplicateNo());
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
	 * 查询组队副本首杀速杀
	 * 
	 * @param duplicateId
	 * @return
	 */
	public TeamQueryRankResp queryRank(int duplicateId) {
		TeamQueryRankResp resp = new TeamQueryRankResp();
		
		if(TeamChallengeRecord.getFirstKill() == null){
			return resp;
		}
		List<TeamChallengeRecordSub> firstKill = TeamChallengeRecord.getFirstKill().get(duplicateId);
		
		if(firstKill != null){
			List<TeamRoleRankRes> firstList = new ArrayList<TeamRoleRankRes>();
			
			for(TeamChallengeRecordSub sub : firstKill){
				TeamRoleRankRes res = new TeamRoleRankRes();
				res.setFirstTime(sub.getFirstTime());
				res.setHeroId(sub.getHeroId());
				res.setHeroLevel(sub.getHeroLevel());
				res.setName(sub.getName());
				res.setQuickTime(sub.getQuickTime());
				res.setRoleId(sub.getRoleId());
				res.setFightValue(sub.getFightValue());
				
				firstList.add(res);
			}
			
			resp.setFirstCount(firstKill.size());
			resp.setFirstList(firstList);
		}
		List<TeamChallengeRecordSub> quickKill = TeamChallengeRecord.getQuickKill().get(duplicateId);
		
		if(quickKill != null){
			List<TeamRoleRankRes> quickList = new ArrayList<TeamRoleRankRes>();
			
			for(TeamChallengeRecordSub sub : quickKill){
				TeamRoleRankRes res = new TeamRoleRankRes();
				res.setFirstTime(sub.getFirstTime());
				res.setHeroId(sub.getHeroId());
				res.setHeroLevel(sub.getHeroLevel());
				res.setName(sub.getName());
				res.setQuickTime(sub.getQuickTime());
				res.setRoleId(sub.getRoleId());
				res.setFightValue(sub.getFightValue());
				quickList.add(res);
			}
			
			resp.setQuickCount(quickList.size());
			resp.setQuickList(quickList);
		}
		return resp;
	}

	public boolean tryLock(RoleLoadInfo roleLoadInfo) {
		return roleLoadInfo.getLock().compareAndSet(false, true);
	}

	public void unLock(RoleLoadInfo roleLoadInfo) {
		roleLoadInfo.getLock().compareAndSet(true, false);
	}

}
