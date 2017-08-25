package com.snail.webgame.game.pvp.competition.end;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.component.room.protocol.processor.BaseProcessor;
import com.snail.webgame.engine.component.room.protocol.util.RoomValue;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.TeamChallengeRecord;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.TeamChallengeRecordSub;
import com.snail.webgame.game.pvp.service.PvpFightEndService;
import com.snail.webgame.game.pvp.service.impl.ChallengeFightEndServiceImpl;
import com.snail.webgame.game.pvp.service.impl.CompetitionFightEndServiceImpl;
import com.snail.webgame.game.pvp.service.impl.MapFightEndServiceImpl;
import com.snail.webgame.game.pvp.service.impl.MutualFightEndServiceImpl;
import com.snail.webgame.game.pvp.service.impl.Team3V3FightEndServiceImpl;
import com.snail.webgame.game.pvp.service.impl.YaBiaoFightEndServiceImpl;
import com.snail.webgame.game.xml.info.TeamChallengeXmlInfo;
import com.snail.webgame.game.xml.load.TeamChallengeXmlLoader;

/**
 * 
 * 类介绍:PVP结束战斗处理Processor
 * 
 * @author zhoubo
 * @2014-11-24
 */
public class ComFightEndProcessor extends BaseProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public ComFightEndProcessor() {
		super();
	}

	public ComFightEndProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		ComFightEndResp comFightEndResp = (ComFightEndResp) message.getiRoomBody();
		PvpFightEndService pvpFightEndService = getService(comFightEndResp.getFightType());
		
		if (logger.isInfoEnabled()) {
			logger.info("ComFightEndProcessor : fightType = " + comFightEndResp.getFightType());
		}

		if (comFightEndResp.getList() != null) {
			if(comFightEndResp.getFightType() == 6){
				// 记录组队副本排名
				recordTeamChallenge(comFightEndResp);
			}
			
			for (ComFightEndRe comFightEndRe : comFightEndResp.getList()) {
				if (comFightEndRe.getServerName().equals( GameConfig.getInstance().getServerName() + GameConfig.getInstance().getGameServerId())) {
					// 当前服务器角色
					RoleInfo roleInfo = RoleInfoMap.getRoleInfo(comFightEndRe.getRoleId());

					if (roleInfo != null) {
						if (roleInfo.getRoleLoadInfo() != null) {
							pvpFightEndService.fightEnd(roleInfo, comFightEndResp, comFightEndRe);
						}
					} else {
						if (logger.isWarnEnabled()) {
							logger.warn("ComFightEndProcessor : roleInfo is null, roleId = " + comFightEndRe.getRoleId());
						}
					}
				} else {
					// 其他服务器角色
				}
			}
		}
	}
	
	private PvpFightEndService getService(byte fightType) {
		switch (fightType) {
		case 1:
			// 竞技场
			return CompetitionFightEndServiceImpl.getInstance();
		case 2:
			// 大地图战斗
			return MapFightEndServiceImpl.getInstance();
		case 3:
			// 对攻战
			return MutualFightEndServiceImpl.getInstance();
		case 5:
			// 对攻战
			return YaBiaoFightEndServiceImpl.getInstance();
		case 6:
			// 组队副本战斗
			return ChallengeFightEndServiceImpl.getInstance();
		case 7:
			// 3V3
			return Team3V3FightEndServiceImpl.getInstance();
		default:
			return null;
		}
	}
	
	// 记录组队副本排名
	private void recordTeamChallenge(ComFightEndResp comFightEndResp){
		try {
			ComFightEndRe fightEndInfo = comFightEndResp.getList().get(0);
			ComFightEndRe fightEndInfo1 = comFightEndResp.getList().get(1);
			ComFightEndRe fightEndInfo2 = comFightEndResp.getList().get(2);

			if(comFightEndResp.getFightType() != 6 || fightEndInfo.getTeamChallengeVo().getIsSameServer() != 1){
				return;
			}

			if(fightEndInfo.getWinner() != 2 && fightEndInfo1.getWinner() != 2 && fightEndInfo2.getWinner() != 2){
				return;
			}
			
			int duplicateId = fightEndInfo.getTeamChallengeVo().getDuplicatId();
			
			synchronized (TeamChallengeRecord.lock) {
				List<TeamChallengeRecordSub> list = new ArrayList<TeamChallengeRecordSub>();
				TeamChallengeRecordSub sub;
				RoleInfo roleInfoSub;
				HeroInfo heroInfoSub;
				long currTime = System.currentTimeMillis();
				long costTime = 0;
				TeamChallengeXmlInfo teamChallengeXmlInfo = TeamChallengeXmlLoader.getTeamChallenge(fightEndInfo.getTeamChallengeVo().getDuplicatId());

				if (teamChallengeXmlInfo == null) {
					return;
				}
				for(ComFightEndRe re : comFightEndResp.getList()){
					roleInfoSub = RoleInfoMap.getRoleInfo(re.getRoleId());
					sub = new TeamChallengeRecordSub();
					sub.setFirstTime(currTime);
					heroInfoSub = HeroInfoMap.getMainHeroInfo(roleInfoSub);
					
					if(heroInfoSub != null)
					{
						sub.setHeroId(heroInfoSub.getHeroNo());
					}
					
					// 超过最大记录等级，则不记录
					if(heroInfoSub.getHeroLevel() > teamChallengeXmlInfo.getMaxLv()){
						return;
					}
					sub.setHeroLevel(heroInfoSub.getHeroLevel());
					sub.setName(roleInfoSub.getRoleName());
					costTime = re.getTeamChallengeVo().getCostTime();
					sub.setQuickTime(costTime);
					sub.setRoleId(roleInfoSub.getId());
					sub.setFightValue(roleInfoSub.getFightValue());
					list.add(sub);
				}
				// 没首杀则记录,并记录速杀
				if(TeamChallengeRecord.getFirstKill() == null || TeamChallengeRecord.getFirstKill().get(duplicateId) == null){
					// 没首杀则记录,并记录速杀
					TeamChallengeRecord.addDuplicateFirstRecord(duplicateId, list, currTime, costTime);
				} else {
					// 仅记录速杀
					if(TeamChallengeRecord.getQuickKill() != null){
						List<TeamChallengeRecordSub> listRecord = TeamChallengeRecord.getQuickKill().get(duplicateId);
						
						if(listRecord != null && listRecord.size() > 1){
							long costTimeRecord = listRecord.get(0).getQuickTime();
							
							if(fightEndInfo.getTeamChallengeVo().getCostTime() < costTimeRecord){
								TeamChallengeRecord.updateDuplicateQuickRecord(duplicateId, list, fightEndInfo.getTeamChallengeVo().getCostTime());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error(e.getMessage());
			}
		}
	}
	
	@Override
	public int getMsgType() {
		return RoomValue.MESSAGE_TYPE_FIGHT_END_FE07;
	}
}
