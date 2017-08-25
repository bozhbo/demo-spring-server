package com.snail.webgame.game.pvp.service.impl;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.sys.SceneMgtService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.pvp.competition.end.ComFightEndRe;
import com.snail.webgame.game.pvp.competition.end.ComFightEndResp;
import com.snail.webgame.game.pvp.service.PvpFightEndService;
import com.snail.webgame.game.xml.cache.YabiaoPrizeXMLMap;
import com.snail.webgame.game.xml.info.YabiaoPrizeXMLInfo;

/**
 * 
 * 类介绍:押镖战斗结束业务类
 *
 * @author zhoubo
 * @2015年6月15日
 */
public class YaBiaoFightEndServiceImpl implements PvpFightEndService {

	private static YaBiaoFightEndServiceImpl service = new YaBiaoFightEndServiceImpl();

	private YaBiaoFightEndServiceImpl() {

	}

	public static PvpFightEndService getInstance() {
		return service;
	}

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void fightEnd(RoleInfo roleInfo, ComFightEndResp comFightEndResp, ComFightEndRe fightEndInfo) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo != null) 
		{
			synchronized (roleInfo) 
			{
				roleLoadInfo.setInFight((byte) 0);
				roleLoadInfo.setFightServer(null);
				roleLoadInfo.setUuid(null);
			}
		} 
		else 
		{
			return;
		}
		
		if(roleLoadInfo.getYabiaoFriendRoleId() != 0)
		{
			//此人为护镖人
			RoleInfo yaBiaoRole = RoleInfoMap.getRoleInfo(roleLoadInfo.getYabiaoFriendRoleId());
			if(yaBiaoRole == null)
			{
				return;
			}
			RoleInfo jieBiaoRole = RoleInfoMap.getRoleInfo(yaBiaoRole.getRoleLoadInfo().getJiebiaoRoleId());
			if(jieBiaoRole == null)
			{
				return;
			}
			calYabiao(yaBiaoRole,roleInfo,jieBiaoRole,fightEndInfo.getWinner());
		}
		else if(roleLoadInfo.getJiebiaoRoleId() != 0)
		{
			//此人为押镖人
			RoleInfo jieBiaoRole = RoleInfoMap.getRoleInfo(roleLoadInfo.getJiebiaoRoleId());
			if(jieBiaoRole == null)
			{
				return;
			}
			calYabiao(roleInfo,null,jieBiaoRole,fightEndInfo.getWinner());
		}
		else
		{
			//此人为劫镖人
			// 大地图PVP战斗输的玩家直接回城，赢的玩家在原大地图,通过0xCOF更新状态
			if (fightEndInfo.getWinner() == 1) {
				// 输了直接回家
				SceneService1.mapRoleDisappear(roleInfo);
				logger.info("####YaBiaoFightEndServiceImpl fight failer,account="+roleInfo.getAccount()+",roleName="+roleInfo.getRoleName()
						+",fightType="+comFightEndResp.getFightType()+",winer="+fightEndInfo.getWinner());

				synchronized(roleInfo)
				{
					roleInfo.setMapPvpFightTime(new Timestamp(System.currentTimeMillis()));
				}
			}
		}

		synchronized(roleInfo)
		{
			roleLoadInfo.setMapPvpAttack(false);
			
			if(comFightEndResp.getTimeoutGameOver() == 1){
				SceneMgtService.mapPVPFightEnd(roleInfo.getId(), 1);
			}
		}
		
		MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleInfo.getId());
		
		if (pointInfo != null) {
			RoleInfo roleInfo1 = RoleInfoMap.getRoleInfo(comFightEndResp.getList().get(0).getRoleId());
			RoleInfo roleInfo2 = RoleInfoMap.getRoleInfo(comFightEndResp.getList().get(1).getRoleId());
			SceneService1.broadPointStatus(roleInfo1, pointInfo);
			SceneService1.broadPointStatus(roleInfo2, pointInfo);
		}
		
		if(roleInfo.isFightEndClearBiaoche()){
			RoleMgtService.clearBiaocheRelation(roleInfo);
		}
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action389.getType(), 0, "");
	}
	
	/**
	 * 劫镖战斗结束处理
	 * @param yaBiaoRole
	 * @param huBiaoRole
	 * @param jieBiaoRole
	 * @param battleResult
	 */
	public static void calYabiao(RoleInfo yaBiaoRole,RoleInfo huBiaoRole,RoleInfo jieBiaoRole,int battleResult)
	{
		MapRolePointInfo yabiaoPoint = MapRoleInfoMap.getMapPointInfo(yaBiaoRole.getId());
		MapRolePointInfo jiebiaoPoint= MapRoleInfoMap.getMapPointInfo(jieBiaoRole.getId());
		
		// 劫镖次数加1
		synchronized (jieBiaoRole) 
		{
			if(!RoleDAO.getInstance().updateTodayJiebiaoNum(jieBiaoRole.getId(), (byte) (jieBiaoRole.getRoleLoadInfo().getTodayJiebiaoNum() + 1))){
				logger.error("1---updateTodayJiebiaoNum error.....jiebiaoRoleId=" + jieBiaoRole.getId());
				return;
			}
			jieBiaoRole.getRoleLoadInfo().setTodayJiebiaoNum((byte) (jieBiaoRole.getRoleLoadInfo().getTodayJiebiaoNum() + 1));
		}
		
		// 于好友战斗时，之前给押镖人头上的战斗状态（status = 5    ===>   status =4）重置
		if(huBiaoRole != null)
		{
			if(yabiaoPoint != null){
				yabiaoPoint.setStatus((byte) 4);
				SceneService1.brocastRolePointStatus(yabiaoPoint,yaBiaoRole);
			}

			MapRolePointInfo hubiaoPoint= MapRoleInfoMap.getMapPointInfo(huBiaoRole.getId());
			
			if(hubiaoPoint != null){
				hubiaoPoint.setStatus((byte) 7);
				SceneService1.brocastRolePointStatus(hubiaoPoint,huBiaoRole);
			}
		}
		
		if(battleResult == 1)
		{
			// 劫镖成功时,押镖人被劫镖次数加1
			synchronized(yaBiaoRole)
			{
				/*if(yabiaoPoint != null){
					yabiaoPoint.setStatus((byte)4);
					SceneService1.brocastRolePointStatus(yabiaoPoint,yaBiaoRole);
				}*/
				if(!RoleDAO.getInstance().updateThisBiaocheJiebiaoNum(yaBiaoRole.getId(), (byte) (yaBiaoRole.getRoleLoadInfo().getThisbiaocheTypeJieBiaoNum() + 1)))
				{
					return;
				}
				yaBiaoRole.getRoleLoadInfo().setThisbiaocheTypeJieBiaoNum((byte) (yaBiaoRole.getRoleLoadInfo().getThisbiaocheTypeJieBiaoNum() + 1));
			}
		}
		
		YabiaoPrizeXMLInfo biaochePrize = YabiaoPrizeXMLMap.getPlayXMLInfo(yaBiaoRole.getRoleLoadInfo().getBiaocheType());
		int prize = 0;
		if (biaochePrize != null) 
		{
			HeroInfo yabaioHeroInfo = HeroInfoMap.getMainHeroInfo(yaBiaoRole);
			if(yabaioHeroInfo != null) 
			{
				// 押镖奖励
				prize = biaochePrize.getBaseMoney() + (yabaioHeroInfo.getHeroLevel() - 1) * biaochePrize.getAddMoney();
				if (SceneService1.yabiaoDobleTime())
				{
					prize *= GameValue.BIAO_CHE_TIME_PRIZE_RATE;
				}
			}
		}
		// 护镖奖励
		int friendPrize = (int) (prize * GameValue.BIAO_CHE_HU_SONG_PRIZE_RATE);
		
		if(battleResult == 1)
		{
			//劫镖成功
			if (jieBiaoRole.getRoleLoadInfo().getTodayJiebiaoNum() <= GameValue.TODAY_JIE_BIAO_NUM)
			{
				if(huBiaoRole != null)
				{
					if(yabiaoPoint != null)
					{
						SceneService1.yabiaoFightEndNotify(yaBiaoRole.getId(), (byte) 1, 1, jieBiaoRole.getRoleName(), huBiaoRole.getRoleName(),
								(int) (prize * GameValue.BIAO_CHE_LAN_JIE_LOST_RATE));
					}
					
					
					SceneService1.yabiaoFightEndNotify(huBiaoRole.getId(), (byte) 2, 1, jieBiaoRole.getRoleName(), yaBiaoRole.getRoleName(),
							(int) (friendPrize * (1 - GameValue.BIAO_CHE_HU_SONG_PRIZE_LOST_RATE * yaBiaoRole.getRoleLoadInfo().getThisbiaocheTypeJieBiaoNum())));
				}
				else
				{
					if(yabiaoPoint != null)
					{
						SceneService1.yabiaoFightEndNotify(yaBiaoRole.getId(), (byte) 1, 1, jieBiaoRole.getRoleName(), "",
								(int) (prize * GameValue.BIAO_CHE_LAN_JIE_LOST_RATE));
					}
				}
				
				synchronized (jieBiaoRole) 
				{
					RoleService.addRoleRoleResource(ActionType.action381.getType(), jieBiaoRole, ConditionType.TYPE_MONEY, (int) (prize * GameValue.BIAO_CHE_LAN_JIE_LOST_RATE),null);
					
					if(jiebiaoPoint != null)
					{
						SceneService1.yabiaoFightEndNotify(jieBiaoRole.getId(), (byte) 3, 2, yaBiaoRole.getRoleName(), "", (int) (prize * GameValue.BIAO_CHE_LAN_JIE_LOST_RATE));
					}
				}

				GameLogService.insertBiaocheLog(jieBiaoRole, ActionType.action389.getType(), jieBiaoRole.getRoleLoadInfo().getTodayJiebiaoNum(), (int) (prize * GameValue.BIAO_CHE_LAN_JIE_LOST_RATE),0,0,yaBiaoRole.getRoleLoadInfo().getBiaocheType(),1);
			}
			else
			{
				if(huBiaoRole != null)
				{
					if(yabiaoPoint != null)
					{
						SceneService1.yabiaoFightEndNotify(yaBiaoRole.getRoleLoadInfo().getId(), (byte) 1, 1, jieBiaoRole.getRoleName(), huBiaoRole.getRoleName(), 0);
					}
					
					SceneService1.yabiaoFightEndNotify(huBiaoRole.getId(), (byte) 2, 1, jieBiaoRole.getRoleName(), yaBiaoRole.getRoleName(), 0);
				}
				else
				{
					if(yabiaoPoint != null)
					{
						SceneService1.yabiaoFightEndNotify(yaBiaoRole.getRoleLoadInfo().getId(), (byte) 1, 1, jieBiaoRole.getRoleName(), "", 0);
					}
				}

				if(jiebiaoPoint != null)
				{
					SceneService1.yabiaoFightEndNotify(jieBiaoRole.getId(), (byte) 3, 2, yaBiaoRole.getRoleName(), "", 0);
				}
				
				GameLogService.insertBiaocheLog(jieBiaoRole, ActionType.action389.getType(), jieBiaoRole.getRoleLoadInfo().getTodayJiebiaoNum(), 0,0,0,yaBiaoRole.getRoleLoadInfo().getBiaocheType(),1);
			}
		}
		else
		{
			// 劫镖失败
			if(huBiaoRole != null)
			{
				if(yabiaoPoint != null)
				{
					SceneService1.yabiaoFightEndNotify(yaBiaoRole.getId(), (byte) 1, 2, jieBiaoRole.getRoleName(), huBiaoRole.getRoleName(), 0);
				}
				
				SceneService1.yabiaoFightEndNotify(huBiaoRole.getId(), (byte) 2, 2, jieBiaoRole.getRoleName(), huBiaoRole.getRoleName(), 0);
			}
			else
			{
				if(yabiaoPoint != null)
				{
					SceneService1.yabiaoFightEndNotify(yaBiaoRole.getId(), (byte) 1, 2, jieBiaoRole.getRoleName(), "", 0);
				}
			}
			

			SceneService1.yabiaoFightEndNotify(jieBiaoRole.getId(), (byte) 3, 1, yaBiaoRole.getRoleName(), "", 0);

			//红点检测
			RedPointMgtService.check2PopRedPoint(jieBiaoRole.getId(), null, true, RedPointMgtService.LISTENING_GOLD_BUY);
			GameLogService.insertBiaocheLog(jieBiaoRole, ActionType.action389.getType(), jieBiaoRole.getRoleLoadInfo().getTodayJiebiaoNum(), 0,0,0,yaBiaoRole.getRoleLoadInfo().getBiaocheType(),0);
		
		}
		
		synchronized(yaBiaoRole)
		{
			yaBiaoRole.getRoleLoadInfo().setJiebiaoRoleId(0);
		}
	}
}
