package com.snail.webgame.game.protocal.fight.team.service;

import java.util.ArrayList;
import java.util.List;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.fight.competition.service.PVPFightService;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.competition.request.MutualVo;
import com.snail.webgame.game.pvp.competition.request.TeamChallengeVo;
import com.snail.webgame.game.pvp.competition.request.WarriorVo;

public class TeamBaseService {
	
	/**
	 * 封装战斗请求消息
	 * 
	 * @param fightType	战斗类型 1-竞技场 3-组队对攻战 4-单人对攻战 5-二人组队对攻战 6-组队副本 7-3V3 三人  8-单人3V3 9-3V3二人 
	 * @param captain	队长/角色
	 * @param members	队员
	 * @return	ComFightRequestReq	战斗请求消息
	 */
	protected static ComFightRequestReq getFightMatchData(int fightType, RoleInfo captain, List<RoleInfo> members, int duplicateNo) {
		ComFightRequestReq captainReq = null;
		
		synchronized (captain) {
			captainReq = warpFightDataReq(fightType, captain, duplicateNo, (byte) 0);
		}
		
		if (members != null && members.size() > 0) {
			List<ComFightRequestReq> membersData = new ArrayList<ComFightRequestReq>(members.size());
			
			for (RoleInfo member : members) {
				synchronized (member) {
					membersData.add(warpFightDataReq(fightType, member, duplicateNo, (byte) 0));
				}
			}
			if(fightType == 6){
				TeamChallengeVo teamChallengeVo = captainReq.getTeamChallengeVo();
				teamChallengeVo.setCount(membersData.size());
				teamChallengeVo.setList(membersData);
			} else {
				MutualVo mutualVo = captainReq.getMutualVo();
				mutualVo.setCount(membersData.size());
				mutualVo.setList(membersData);
			}
		}
		
		return captainReq;
	}

	/**
	 * 封装角色战斗数据
	 * 
	 * @param fightType	战斗类型 1-竞技场 3-组队对攻战 4-单人对攻战  6-组队副本 7-3V3 三人  8-单人3V3 9-3V3二人 
	 * @param roleInfo	角色信息
	 * @return	ComFightRequestReq 角色战斗数据
	 */
	protected static ComFightRequestReq warpFightDataReq(int fightType, RoleInfo roleInfo, int duplicateNo, byte isSameServer) {
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		FightType eFightType; 
		
		if(fightType == 6){
			eFightType = FightType.FIGHT_TYPE_20;
		} else {
			eFightType = FightType.FIGHT_TYPE_21;
		}
		List<WarriorVo> warriorVoList = PVPFightService.getFightWarriorVoList(eFightType, roleInfo);
		ComFightRequestReq req = new ComFightRequestReq();
		req.setFightType((byte) fightType);
		req.setRoleId((int) roleInfo.getId());
		req.setServerName(GameConfig.getInstance().getServerName() + GameConfig.getInstance().getGameServerId());
		
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
		
		req.setNickName(roleInfo.getRoleName());
		
		if(fightType == 6){
			TeamChallengeVo teamChallengeVo = new TeamChallengeVo();
			teamChallengeVo.setDuplicatId(duplicateNo);
			teamChallengeVo.setIsSameServer(isSameServer);
			req.setTeamChallengeVo(teamChallengeVo);
			
			if(roleInfo.getRoleLoadInfo() != null){
				roleInfo.getRoleLoadInfo().setTeamDuplicateId(duplicateNo);
			}
		} else {
			MutualVo mutualVo = new MutualVo();
			mutualVo.setFightValue(roleInfo.getFightValue());
			mutualVo.setLevel(heroInfo.getHeroLevel());
			req.setMutualVo(mutualVo);
		}
		req.setWarriorCount((byte) (warriorVoList != null ? warriorVoList.size() : 0));
		req.setWarriorList(warriorVoList);
		return req;
	}
}
