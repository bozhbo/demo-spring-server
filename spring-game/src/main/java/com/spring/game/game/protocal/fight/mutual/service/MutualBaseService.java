package com.snail.webgame.game.protocal.fight.mutual.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.fight.competition.service.PVPFightService;
import com.snail.webgame.game.protocal.fight.mutual.refresh.MutualRefershResp;
import com.snail.webgame.game.protocal.mail.chat.ChatResp;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.competition.request.MutualVo;
import com.snail.webgame.game.pvp.competition.request.WarriorVo;

public class MutualBaseService {
	
	private static Logger logger = LoggerFactory.getLogger("logs");

	public static MutualRefershResp createMutualRefershResp(RoleInfo roleInfo) {
		MutualRefershResp resp = new MutualRefershResp();
		
		if (roleInfo == null) {
			if (logger.isErrorEnabled()) {
				logger.error("createMutualRefershResp error, roleInfo is null");
			}
			
			return resp;
		}
		
		try {
			resp.setRoleId(roleInfo.getId());
			resp.setName(roleInfo.getRoleName());
			resp.setLevel(HeroInfoMap.getMainHeroLv(roleInfo.getId()));
			resp.setHeroId(HeroInfoMap.getMainHeroInfo(roleInfo).getHeroNo());
			
			Map<Integer, HeroInfo> map = HeroInfoMap.getHeroByRoleId(roleInfo.getId());
			StringBuffer buffer = new StringBuffer();
			
			if (map != null) {
				Set<Entry<Integer, HeroInfo>> set = map.entrySet();
				List<HeroInfo> list = new ArrayList<HeroInfo>();
				
				for (Entry<Integer, HeroInfo> entry : set) {
					if (entry.getValue().getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM 
							&& entry.getValue().getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT
							&& entry.getValue().getDeployStatus() != 1) {
						list.add(entry.getValue());
						
					}
				}
				
				Collections.sort(list, new Comparator<HeroInfo>() {
					@Override
					public int compare(HeroInfo o1, HeroInfo o2) {
						return o1.getDeployStatus() - o2.getDeployStatus();
					}
				});
				
				for (HeroInfo heroInfo : list) {
					buffer.append(heroInfo.getHeroNo()).append(":").append(heroInfo.getQuality()).append(",");
				}
			}
			
			resp.setTempHeroId(buffer.length() > 1 ? buffer.toString().substring(0, buffer.length() - 1) : "");
			resp.setFightValue(roleInfo.getFightValue());
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("createMutualRefershResp error", e);
			}
		}
		
		return resp;
	}
	
	/**
	 * 封装战斗请求消息
	 * 
	 * @param fightType	战斗类型 1-竞技场 3-组队对攻战 4-单人对攻战 5-二人组队对攻战 6-组队副本 7-3V3 三人  8-单人3V3 9-3V3二人 
	 * @param captain	队长/角色
	 * @param members	队员
	 * @return	ComFightRequestReq	战斗请求消息
	 */
	public static ComFightRequestReq getFightMatchData(int fightType, RoleInfo captain, List<RoleInfo> members) {
		ComFightRequestReq captainReq = null;
		
		synchronized (captain) {
			captainReq = warpFightDataReq(fightType, captain);
		}
		
		if (members != null && members.size() > 0) {
			List<ComFightRequestReq> membersData = new ArrayList<ComFightRequestReq>(members.size());
			
			for (RoleInfo member : members) {
				synchronized (member) {
					membersData.add(warpFightDataReq(fightType, member));
				}
			}
			
			MutualVo mutualVo = captainReq.getMutualVo();
			mutualVo.setCount(membersData.size());
			mutualVo.setList(membersData);
		}
		
		return captainReq;
	}

	/**
	 * 封装角色战斗数据
	 * 
	 * @param fightType	战斗类型 1-竞技场 2-地图战斗 3-组队对攻战 4-单人对攻战  5-二人组队对攻战 6-组队副本 7-3V3
	 * @param roleInfo	角色信息
	 * @return	ComFightRequestReq 角色战斗数据
	 */
	private static ComFightRequestReq warpFightDataReq(int fightType, RoleInfo roleInfo) {
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		List<WarriorVo> warriorVoList = PVPFightService.getFightWarriorVoList(FightType.FIGHT_TYPE_14, roleInfo);
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
		MutualVo mutualVo = new MutualVo();
		mutualVo.setFightValue(roleInfo.getFightValue());
		mutualVo.setLevel(heroInfo.getHeroLevel());
		req.setMutualVo(mutualVo);
		req.setWarriorCount((byte) (warriorVoList != null ? warriorVoList.size() : 0));
		req.setWarriorList(warriorVoList);
		return req;
	}
	
	/**
	 * 发送组队广播
	 * @param roleId
	 * @param roleName
	 * @param teamLv
	 * @param duplicateId 
	 * @param activityType 
	 */
	public static void broadcast(RoleInfo roleInfo,int teamLv, int activityType, int duplicateId) {
		Message message = new Message();
		GameMessageHead head = new GameMessageHead();
		head.setUserID1(Command.CHAT_RESP);
		head.setMsgType(Command.USER_MSG_SEND_RESP);
		message.setHeader(head);
		ChatResp resp = new ChatResp();
		resp.setResult(1);
		
		int msgType = 0;
		String content = "";
		
		if(activityType == 1){
			msgType = Command.MSG_CAHNNEL_TEAM_MUTUAL;
			content = String.valueOf(teamLv);
		} else if(activityType == 2){
			msgType = Command.MSG_CAHNNEL_TEAM_DUPLICATE;
			content = teamLv + "," + duplicateId;
		} else if(activityType == 3){
			msgType = Command.MSG_CAHNNEL_TEAM_3V3;
			content = String.valueOf(teamLv);
		}
		
		resp.setMsgType(msgType);
		resp.setSendRoleId(roleInfo.getId());
		resp.setSendRace(roleInfo.getRoleRace());
		resp.setSendRoleName(roleInfo.getRoleName());
		resp.setMsgContent(content);
		resp.setSendTime(System.currentTimeMillis());
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (heroInfo == null) {
			return;
		}
		resp.setSendMainHeroNo(heroInfo.getHeroNo());
		resp.setVipLevel((byte) roleInfo.getVipLv());
		message.setBody(resp);
		
		IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
		
		if (session != null && session.isConnected()) {
			session.write(message);
		}
	}
}
