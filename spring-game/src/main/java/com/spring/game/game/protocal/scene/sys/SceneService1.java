package com.snail.webgame.game.protocal.scene.sys;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.math.NumberUtils;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.EquipInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.EquipRecord;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.EnergyCond;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.BossInfo;
import com.snail.webgame.game.info.EquipInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.QuestInProgressInfo;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.MapFightLog;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.fight.competition.service.PVPFightService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.rank.service.RankInfo;
import com.snail.webgame.game.protocal.rank.service.RankService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.relation.entity.FriendDetailRe;
import com.snail.webgame.game.protocal.relation.entity.FriendDetailResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.biaocheFightEnd.BiaocheFightEndResp;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.cache.SceneInfoMap;
import com.snail.webgame.game.protocal.scene.cache.ViewChangeInfo;
import com.snail.webgame.game.protocal.scene.delPoint.DelPointResp;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.info.RolePoint;
import com.snail.webgame.game.protocal.scene.info.SuperRInfo;
import com.snail.webgame.game.protocal.scene.joinScene.RoleJoinSceneResp;
import com.snail.webgame.game.protocal.scene.joinScene.SceneRolePointInfo;
import com.snail.webgame.game.protocal.scene.mapMove.AreaDelResp;
import com.snail.webgame.game.protocal.scene.mapMove.MapMoveUpdateResp;
import com.snail.webgame.game.protocal.scene.outCity.DelMapNPCResp;
import com.snail.webgame.game.protocal.scene.outCity.MapNPCPointResp;
import com.snail.webgame.game.protocal.scene.sceneRefre.SceneRefreService;
import com.snail.webgame.game.protocal.scene.updateName.NotifyUpdateEquipResp;
import com.snail.webgame.game.protocal.scene.updateName.NotifyUpdateNameResp;
import com.snail.webgame.game.protocal.scene.updatePoint.OtherUpdatePointResp;
import com.snail.webgame.game.protocal.worship.query.NotifyRefreshSuperResp;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.competition.request.WarriorVo;
import com.snail.webgame.game.xml.cache.GWXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.QuestProtoXmlInfoMap;
import com.snail.webgame.game.xml.cache.SceneXmlInfoMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.GWXMLInfo;
import com.snail.webgame.game.xml.info.QuestProtoXmlInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXMLNPC;

public class SceneService1 {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	//private static GameLogDAO gameLogDAO = GameLogDAO.getInstance();
	/**
	 * 场景中一个物体移动,通知其它玩家更新(除自己外)
	 * @param rolePoint
	 */
	public static void notifyUpdateAIPoint(RolePoint rolePoint)
	{
		Map<Integer,RolePoint> sceneRoleMap = SceneInfoMap.getSceneRoleMap(rolePoint.getNo(),rolePoint.getSceneId());
		if(sceneRoleMap != null)
		{
			for(RolePoint rolePoint1 : sceneRoleMap.values())
			{
				if(rolePoint1.getRoleId() == rolePoint.getRoleId())
				{
					continue;
				}
				
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(rolePoint1.getRoleId());
				if(roleInfo == null)
				{
					continue;
				}
				
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.SCENE_UPDATE_AI_POINT_RESP);
				header.setUserID0((int)rolePoint1.getRoleId());
				message.setHeader(header);
				
				OtherUpdatePointResp resp = new OtherUpdatePointResp();
				resp.setAIType(1);
				resp.setChangeAIId(rolePoint.getRoleId());
				resp.setCurPointX(rolePoint.getPointX());
				resp.setCurPointY(rolePoint.getPointY());
				resp.setCurPointZ(rolePoint.getPointZ());
				
				message.setBody(resp);				
				SceneRefreService.sendRoleRefreshMsg(rolePoint1.getRoleId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
			}
		}
	}
	
	/**
	 * 场景中一个AI下线,通知其它玩家更新(除自己外)
	 * @param rolePoint
	 */
	public static void notifyDelAIPoint(RolePoint rolePoint)
	{
		SceneInfoMap.delRolepoint(rolePoint);
		
		Map<Integer,RolePoint> sceneRoleMap = SceneInfoMap.getSceneRoleMap(rolePoint.getNo(),rolePoint.getSceneId());
		if(sceneRoleMap != null)
		{
			for(RolePoint rolePoint1 : sceneRoleMap.values())
			{
				if(rolePoint1.getRoleId() == rolePoint.getRoleId())
				{
					continue;
				}
				
				RoleInfo roleInfo = RoleInfoMap.getRoleInfo(rolePoint1.getRoleId());
				if(roleInfo == null)
				{
					continue;
				}
				
				//玩家当前处于断开中
				if(roleInfo.getDisconnectPhase() == 1)
				{
					continue;
				}
				
				// 通知其它玩家有人下线
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.SCENE_DEL_AI_POINT_RESP);
				header.setUserID0((int)rolePoint1.getRoleId());
				message.setHeader(header);
				
				DelPointResp resp = new DelPointResp();
				resp.setAIType(1);
				resp.setChangeAIId(rolePoint.getRoleId());
				
				message.setBody(resp);				
				SceneRefreService.sendRoleRefreshMsg(rolePoint1.getRoleId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
			}
		}
	}
	
	
	/**
	 * 场景中新增一个AI,通知其它玩家
	 * @param rolePoint
	 */
	public static void notifyAddAIPoint(RoleInfo roleInfo,byte disConnectIntoScene)
	{
		RolePoint rolePoint = roleInfo.getRolePoint();
		if(rolePoint == null)
		{
			logger.error("111########notifyAddAIPoint error,roleId="+roleInfo.getId());
			return;
		}
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null)
		{
			logger.error("222########notifyAddAIPoint error,roleId="+roleInfo.getId());
			return;
		}
		Map<Integer,RolePoint> sceneRoleMap = SceneInfoMap.getSceneRoleMap(rolePoint.getNo(),rolePoint.getSceneId());
		if(sceneRoleMap != null)
		{
			RoleJoinSceneResp joinSceneResp = new RoleJoinSceneResp();
			joinSceneResp.setRoleId(roleInfo.getId());
			joinSceneResp.setSceneNo(rolePoint.getNo());
			joinSceneResp.setSceneId(rolePoint.getSceneId());
			List<SceneRolePointInfo> rolePointList = new ArrayList<SceneRolePointInfo>();
			
			//通知场景中其它AI更新
			for(RolePoint otherRolePoint : sceneRoleMap.values())
			{
				RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(otherRolePoint.getRoleId());
				HeroInfo otherHeroInfo = HeroInfoMap.getMainHeroInfo(otherRoleInfo);
				if(otherHeroInfo == null){
					continue;
				}
				rolePointList.add(getSceneRolePointInfo(otherRoleInfo, otherRolePoint));						
				//不需要通知自己
				if(otherRolePoint.getRoleId() == rolePoint.getRoleId() /*|| disConnectIntoScene == 1*/)
				{
					continue;
				}
				
				//玩家当前处于断开中
				if(otherRoleInfo.getDisconnectPhase() == 1)
				{
					continue;
				}
				
				//通知其它AI有新AI加入
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.SCENE_ADD_AI_POINT_RESP);
				header.setUserID0((int)otherRoleInfo.getId());
				message.setHeader(header);		
				message.setBody(getSceneRolePointInfo(roleInfo, rolePoint));
				SceneRefreService.sendRoleRefreshMsg(otherRoleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
			}
			
			
			//自己进场景看到的AI
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setMsgType(Command.IN_SCENE_AI_POINT_RESP);
			header.setUserID0((int)roleInfo.getId());
			message.setHeader(header);
			
			joinSceneResp.setRoleNum(rolePointList.size());
			joinSceneResp.setRolePointList(rolePointList);
			
			message.setBody(joinSceneResp);		
			SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
		}
	}
	
	/**
	 * 获取 SceneRolePointInfo
	 * @param roleInfo
	 * @param rolePoint
	 * @return
	 */
	public static SceneRolePointInfo getSceneRolePointInfo(RoleInfo roleInfo,RolePoint rolePoint){
		SceneRolePointInfo resp = new SceneRolePointInfo();		
		resp.setRoleId(roleInfo.getId());
		resp.setRoleName(roleInfo.getRoleName());		
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo != null){
			resp.setHeroNo(heroInfo.getHeroNo());
			resp.setEquipNos(getHeroEquipNoforAvater(heroInfo));
		}				
		resp.setPointX(rolePoint.getPointX());
		resp.setPointY(rolePoint.getPointY());
		resp.setPointZ(rolePoint.getPointZ());	
		if(heroInfo != null){
			resp.setEquipNos(getHeroEquipNoforAvater(heroInfo));
		}	
		resp.setClubName(RoleClubInfoMap.getClubName(roleInfo.getClubId()));				
		resp.setShowPlanId(roleInfo.getIsShowShizhuang());
		if (roleInfo.getRideInfo() != null) {
			resp.setRideNo(roleInfo.getRideInfo().getRideNo());
		}
		
		if(roleInfo.getRoleLoadInfo() != null){
			Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
			
			TitleService.titlesReset(roleInfo, map);//刷新称号
			
			resp.setTitleNo(TitleService.nowTitleCheck(roleInfo, map));
		}
		
		
		return resp;		
	}
	
	
	/**
	 * 检查附近玩家变化
	 * 
	 * @param aiInfo
	 * @param radiiX
	 *            X轴半径
	 * @param radiiZ
	 *            Z轴半径
	 */
	public static ViewChangeInfo checkAreaChange(ConcurrentHashMap<Integer,Integer> oldRoleIds,ConcurrentHashMap<Integer,Integer> curRoleIds) {
		ViewChangeInfo result = new ViewChangeInfo();

		// 附近新出现的玩家
		List<Integer> addRoleIds = new ArrayList<Integer>();
		// 附近消失的玩家
		List<Integer> delRoleIds = new ArrayList<Integer>();
		
		// 新位置原来老玩家
		List<Integer> newPonsiOldRoleIds = new ArrayList<Integer>();
		
		for(int curRoleId:curRoleIds.keySet())
		{
			if(!oldRoleIds.containsKey(curRoleId))
			{
				addRoleIds.add(curRoleId);
			}
		}
		
		for(int oldRoleId:oldRoleIds.keySet())
		{
			if(curRoleIds.contains(oldRoleId))
			{
				newPonsiOldRoleIds.add(oldRoleId);
			}
			else
			{
				delRoleIds.add(oldRoleId);
			}
		}

		result.setAddRoleIds(addRoleIds);
		result.setDelRoleIds(delRoleIds);
		result.setCurPonsiOldRoleIds(newPonsiOldRoleIds);
		
		return result;
	}
	

	/**
	 * 玩家在大地图消失
	 * @param roleInfo
	 */
	public static void mapRoleDisappear(RoleInfo roleInfo)
	{
		//如果在大地图上移除在大地图上的位置
		MapRolePointInfo mapPoint = MapRoleInfoMap.getMapPointInfo((int)roleInfo.getId());
		if(mapPoint != null)
		{
			// 玩家在其它玩家屏幕内,通知其它玩家我消失了。
			ConcurrentHashMap<Integer,Integer> otherRoleIds = mapPoint.getInOtherRoleScreen();
			for(int otherRoleId : otherRoleIds.keySet())
			{
				if(otherRoleId == (int)roleInfo.getId())
				{
					continue;
				}
				
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.MAP_NOTIFY_DEL_POINT_RESP);
				header.setUserID0(otherRoleId);
				message.setHeader(header);
				
				AreaDelResp resp = new AreaDelResp();
				resp.setDelRoleId(roleInfo.getId()+"");
				message.setBody(resp);
				MapRolePointInfo otherRoleMapPoint = MapRoleInfoMap.getMapPointInfo(otherRoleId);
				
				if(otherRoleMapPoint != null && otherRoleMapPoint.getStatus() !=1 && otherRoleMapPoint.getStatus() !=6)
				{
					SceneRefreService.sendRoleRefreshMsg(otherRoleId,SceneRefreService.REFRESH_TYPE_SCENE,message);
				}
				
				
				if(otherRoleMapPoint != null)
				{
					// 移除存在于其它玩家中的数据
					ConcurrentHashMap<Integer,Integer> iter1 = otherRoleMapPoint.getScreenRoleIds();
					iter1.remove(roleInfo.getId());					
					
					ConcurrentHashMap<Integer,Integer> iter = otherRoleMapPoint.getInOtherRoleScreen();
					iter.remove(roleInfo.getId());			
				}
			}
			
			// 押镖的时候下线,清理护镖好友的数据
			if(mapPoint.getStatus() == 4 || mapPoint.getStatus() == 5 || mapPoint.getStatus() == 6)
			{
				if(roleInfo.getRoleLoadInfo().getHubiaoRoleId() > 0)
				{
					RoleInfo helpRoleInfo = RoleInfoMap.getRoleInfo(roleInfo.getRoleLoadInfo().getHubiaoRoleId());
					if (helpRoleInfo != null) 
					{
						RoleLoadInfo helpRoleLoadInfo = helpRoleInfo.getRoleLoadInfo();
						helpRoleLoadInfo.setYabiaoFriendRoleId(0);
					}
				}
			}
			MapRoleInfoMap.removeMapPointInfo(mapPoint.getRoleId());
		}
	}
	
	/**
	 * 创建PVP战斗数据
	 * @param roleInfo
	 * @return
	 */
	public static ComFightRequestReq createPvpFightReq(RoleInfo roleInfo)
	{
		ComFightRequestReq req = new ComFightRequestReq();		
		req.setFightType((byte)2);
		req.setRoleId((int)roleInfo.getId());
		req.setNickName(roleInfo.getRoleName());
		req.setServerName(GameConfig.getInstance().getServerName() + GameConfig.getInstance().getGameServerId());
		req.setShowPlanId(roleInfo.getIsShowShizhuang());
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
		
		List<WarriorVo> warriorVoList = PVPFightService.getFightWarriorVoList(FightType.FIGHT_TYPE_16,roleInfo);
			
		req.setWarriorCount((byte) (warriorVoList != null ? warriorVoList.size() : 0));
		req.setWarriorList(warriorVoList);
		
		return req;
	}
	
	/**
	 * 广播周围玩家某单位坐标及状态修改
	 * @param mapPoint
	 */
	public static void brocastRolePointStatus(MapRolePointInfo mapPoint,RoleInfo roleInfo)
	{
		if (mapPoint == null) {
			return;
		}
		ConcurrentHashMap<Integer,Integer> otherRoleIds = mapPoint.getInOtherRoleScreen();
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		for(int otherRoleId : otherRoleIds.keySet())
		{
			MapRolePointInfo otherRoleMapPoint = MapRoleInfoMap.getMapPointInfo(otherRoleId);
			if(otherRoleMapPoint == null || otherRoleId == mapPoint.getRoleId())
			{
				continue;
			}
			RoleInfo otherRole = RoleInfoMap.getRoleInfo(otherRoleId);
			if(otherRole == null || otherRole.getDisconnectPhase() == 1)
			{
				continue;
			}
			
			if(otherRoleMapPoint.getStatus() !=1 && otherRoleMapPoint.getStatus() !=6 && otherRoleMapPoint.getStatus() !=8)
			{
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.MAP_NOTIFY_UPDATE_POINT_RESP);
				header.setUserID0(otherRoleId);
				message.setHeader(header);
				
				MapMoveUpdateResp resp = new MapMoveUpdateResp();
				resp.setRoleId(mapPoint.getRoleId());
				resp.setPointx(mapPoint.getPointX());
				resp.setPointz(mapPoint.getPointZ());
				resp.setStatus(mapPoint.getStatus());
				resp.setOtherRoleId(mapPoint.getBiaocheOtherRoleId());
				
				if(roleLoadInfo != null){
					resp.setBiaocheType(roleLoadInfo.getBiaocheType());
				}
				
				message.setBody(resp);
				SceneRefreService.sendRoleRefreshMsg(otherRoleId,SceneRefreService.REFRESH_TYPE_SCENE,message);
			}
		}
	}
	
	/**
	 * 广播自己的坐标及状态修改给 自己，和自己战斗的人
	 * 
	 * @param roleInfo
	 * @param pointInfo
	 */
	public static void broadPointStatus(RoleInfo roleInfo, MapRolePointInfo pointInfo){
		if(roleInfo != null && roleInfo.getDisconnectPhase() != 1)
		{
			MapRolePointInfo otherRoleMapPoint = MapRoleInfoMap.getMapPointInfo(roleInfo.getId());
			
			if(otherRoleMapPoint != null) {
				org.epilot.ccf.core.protocol.Message message = new org.epilot.ccf.core.protocol.Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.MAP_NOTIFY_UPDATE_POINT_RESP);
				header.setUserID0(roleInfo.getId());
				message.setHeader(header);
				
				MapMoveUpdateResp resp = new MapMoveUpdateResp();
				resp.setRoleId(pointInfo.getRoleId());
				resp.setPointx(pointInfo.getPointX());
				resp.setPointz(pointInfo.getPointZ());
				resp.setStatus(pointInfo.getStatus());
				resp.setOtherRoleId(pointInfo.getBiaocheOtherRoleId());
				
				RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
				
				if(roleLoadInfo != null){
					resp.setBiaocheType(roleLoadInfo.getBiaocheType());
				}
				message.setBody(resp);
				SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(), SceneRefreService.REFRESH_TYPE_SCENE, message);
			}
		}
	}
	
	/**
	 * 获取玩家状态是否可攻击
	 * @param preFightTime
	 * @return
	 */
	public static byte getMapPointStatus(RoleInfo roleInfo)
	{
		byte status = 0;
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null)
		{
			return status;
		}
		MapRolePointInfo  rolePoint = MapRoleInfoMap.getMapPointInfo((int)roleInfo.getId());
		if(rolePoint == null)
		{
			return status;
		}
		
		status = rolePoint.getStatus();
		
		if(status == 4 || status == 5 || status == 6 || status == 1 || status == 7 || status == 8)
		{
			return status;
		}
		
		//保护状态
		if(System.currentTimeMillis() - roleInfo.getMapPvpFightTime().getTime() < GameValue.PRE_MAP_PVP_FIGHT_TIME)
		{
			status= 3;
		}
		
		//新手状态
		if (heroInfo.getHeroLevel() <= GameValue.MAP_PVP_ROLE_LV) {
			status = 2;
		}
		return status;
	}
	
	/**
	 * 大地图攻击NPC战斗处理
	 */
	public static int mapPVEFightEndHandle(int action,int fightResult, RoleInfo roleInfo, FightInfo fightInfo,List<BattlePrize> prizeList){
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return ErrorCode.MAP_PVP_FIGHT_ERROR_6;
		}
		
		MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo((int) roleInfo.getId());
		if (pointInfo == null) {
			return ErrorCode.MAP_PVP_FIGHT_ERROR_6;
		}
		String[] defendStr = fightInfo.getDefendStr().split(",");
		if (defendStr.length < 1) {
			return ErrorCode.MAP_NPC_FIGHT_ERRROR_2;
		}
		int mapNpcNo = NumberUtils.toInt(defendStr[0]);
		MapCityXMLNPC mapNpc = SceneXmlInfoMap.getMapCityXMLNPC(mapNpcNo);
		if (mapNpc == null) {
			return ErrorCode.MAP_PVP_FIGHT_ERROR_19;
		}

		// 扣除精力
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		conds.add(new EnergyCond(mapNpc.getCostEng()));
		int condCheck = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (condCheck != 1) {
			return condCheck;
		}
		if (RoleService.subRoleResource(ActionType.action29.getType(), roleInfo, conds, null)) {
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFESH_TYPE_ROLE, null);
		}
		int fightArenaRoleId = 0;
		if (fightResult == 1) {
			if(!roleLoadInfo.getPassMapNpcNos().contains(mapNpcNo)){
				List<Integer> pass =new ArrayList<Integer>();
				pass.addAll(roleLoadInfo.getPassMapNpcNos());
				pass.add(mapNpcNo);
				if(RoleDAO.getInstance().updateRolePassMapNpcNos(roleInfo.getId(), pass)){
					roleLoadInfo.setPassMapNpcNos(pass);
				}else{
					return ErrorCode.MAP_PVP_FIGHT_ERROR_20;
				}
			}
			
			String dropBag = "";
			if (fightInfo.getFightType() == FightType.FIGHT_TYPE_7) {
				String gwNo = fightInfo.getStartRespDefendStr().split(",")[0];
				GWXMLInfo xmlInfo = GWXMLInfoMap.getNPCGWXMLInfo(gwNo);
				if (xmlInfo != null) {
					for (String bag : xmlInfo.getDropMap().values()) {
						if (dropBag.length() > 0) {
							dropBag += ",";
						}
						dropBag += bag;
					}
				}

				// 大地图攻击NPC(真实的NPC数据)
				// 探索古墓
				QuestService.checkQuest(roleInfo, ActionType.action19.getType(), fightInfo.getDefendStr(),
						true, true);
			} else {
				dropBag = mapNpc.getPrize();
				fightArenaRoleId = NumberUtils.toInt(fightInfo.getStartRespDefendStr().split(",")[2]);
				// 大地图攻击NPC(玩家镜像)消灭流寇
				// 消灭流寇
				QuestService.checkQuest(roleInfo, ActionType.action19.getType(), fightInfo.getDefendStr(),
						true, true);
			}

			// 大地图NPC掉落
			List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(dropBag);
			
			//处理经验掉落
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (heroInfo == null) {
				return ErrorCode.ADD_ERP_ERROR_1;
			}
			//奖励之前等级
			int level = heroInfo.getHeroLevel();
			int addExp = 0;
			int pzMaxLv = HeroXMLInfoMap.getMaxMainLv();
			if(level < pzMaxLv){
				addExp = HeroInfoMap.getMainHeroLv(roleInfo.getId()) * GameValue.PRE_MAP_NPC_FIGHT_EXP_PRIZE_A
						+ GameValue.PRE_MAP_NPC_FIGHT_EXP_PRIZE_B;
				int addExpResult = ItemService.expAdd(action, roleInfo, addExp, null, null);
				if (addExpResult != 1) 
				{
					return addExpResult;
				}
				prizeList.add(new BattlePrize(ConditionType.TYPE_EXP.getName(), addExp, (byte) 2, (byte)0));
				if(level < heroInfo.getHeroLevel())
				{
					String common = "lvUp";
					RedPointMgtService.check2PopRedPoint(roleInfo.getId(), common, true, RedPointMgtService.LISTENING_HERO_SKILL_UP_TYPES);
				}
			}
			ItemService.addPrizeForPropBag(ActionType.action29.getType(), roleInfo, list,null, 
					prizeList, null, null, null, true);
		} else {
			// 输了直接回家
			SceneService1.mapRoleDisappear(roleInfo);
			logger.info("####mapPVEFightEndHandle fight failer,account="+roleInfo.getAccount()+",roleName="+roleInfo.getRoleName()
					+",action="+action+",fightType="+fightInfo.getFightType()+",defendStr="+fightInfo.getDefendStr()+",fightResult="+fightResult);
		}

		// 计入防守玩法日志
		MapFightLog mapLog = new MapFightLog();
		mapLog.setAccount(roleInfo.getAccount());
		mapLog.setRoleName(roleInfo.getRoleName());
		mapLog.setRoleId(roleInfo.getId());
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (heroInfo != null) {
			mapLog.setMainHeroId(heroInfo.getHeroNo());
		}
		mapLog.setNpcId(mapNpcNo);
		mapLog.setComeRoleId(fightArenaRoleId);
		mapLog.setBeginTime(new Timestamp(fightInfo.getFightTime()));
		mapLog.setEndTime(new Timestamp(System.currentTimeMillis()));
		String pos = "";
		List<StartFightPosInfo> chgposList = fightInfo.getChgPosInfos();
		if (chgposList != null && chgposList.size() > 0) {
			for (StartFightPosInfo info : chgposList) {
				HeroInfo heroInfo1 = HeroInfoMap.getHeroInfo(roleInfo.getId(), info.getHeroId());
				if (heroInfo1 == null || heroInfo1.getDeployStatus() == 1) {
					continue;
				}

				pos = pos + info.getDeployPos() + "," + heroInfo1.getHeroNo() + ";";
			}
		}
		if (pos != null && pos.length() > 0) {
			mapLog.setPos(pos.substring(0, pos.length() - 1));
		}
		mapLog.setDrop(GameLogService.getItem(prizeList, null));
		mapLog.setFightResult(fightResult);
		GameLogService.insertMapFightLog(mapLog);
		return 1;
	}
	
	
	/**
	 * 地图上定时的NPC
	 * @param roleInfo
	 */
	public static void raceTaskMapAddNpc(RoleInfo roleInfo)
	{
		MapNPCPointResp resp = new MapNPCPointResp();
		
		List<QuestInProgressInfo> quests = roleInfo.getQuestInfoMap().getRoleQuest();
		if (quests != null) {
			for (QuestInProgressInfo info : quests) {
				int questProtoNo = info.getQuestProtoNo();
				QuestProtoXmlInfo questProtoXmlInfo = QuestProtoXmlInfoMap.questXml(questProtoNo);
				if (questProtoXmlInfo == null) {
					continue;
				}
				
				//特殊任务，当有任务时，地图出现NPC
				if(questProtoXmlInfo.getShowNpc() != 1)
				{
					continue;
				}
				
				if(info.getStatus() != QuestInProgressInfo.STATUS_REVEIVE)
				{
					continue;
				}
				
				String npcArtStr = "";
				HashMap<Integer,MapCityXMLNPC> npcMap = MapRoleInfoMap.getMapNpc();
				for(int no : npcMap.keySet())
				{
					npcArtStr = npcArtStr+no+",";
				}
				if(npcArtStr.length()<=0){
					continue;
				}
				
				npcArtStr = npcArtStr.substring(0, npcArtStr.length()-1);
				resp.setNpcArtStr(npcArtStr);
				
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.MAP_ADD_NPC_RESP);
				header.setUserID0((int)roleInfo.getId());
				message.setHeader(header);
				
				message.setBody(resp);
				SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
				
				return;
			}
		}
		
	}
	
	/**
	 * 粮仓任务完成,玩家不可见大地图上的粮仓
	 * @param roleInfo
	 */
	public static void raceTaskMapDelNpc(int roleId,MapCityXMLNPC xmlInfo)
	{
		DelMapNPCResp resp = new DelMapNPCResp();
		
		Message message = new Message();
		GameMessageHead header = new GameMessageHead();
		header.setMsgType(Command.MAP_DEL_NPC_REDP);
		header.setUserID0(roleId);
		message.setHeader(header);
		
		String npcArtStr = xmlInfo.getNo()+"";
//		HashMap<Integer,String> npcMap = MapRoleInfoMap.getMapNpc();
//		for(int no : npcMap.keySet())
//		{
//			if(npcMap.get(no).equalsIgnoreCase(gw))
//			{
//				npcArtStr = npcArtStr+no+",";
//			}
//		}
//		
//		npcArtStr = npcArtStr.substring(0, npcArtStr.length()-1);
		resp.setNpcArtStr(npcArtStr);
		
		message.setBody(resp);
		SceneRefreService.sendRoleRefreshMsg(roleId,SceneRefreService.REFRESH_TYPE_SCENE,message);
	}
	
	
	/**
	 * 玩家下线,退出场景
	 * @param roleInfo
	 */
	public static void sceneAILogout(RoleInfo roleInfo) {
		RolePoint rolePoint = roleInfo.getRolePoint();
		if (rolePoint != null) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null)
			{
				return;
			}
			if (!RoleDAO.getInstance().updateSceneAIPoint(rolePoint)) {
				return;
			}
			roleLoadInfo.setPointX(rolePoint.getPointX());
			roleLoadInfo.setPointY(rolePoint.getPointY());
			roleLoadInfo.setPointZ(rolePoint.getPointZ());
			roleLoadInfo.setSceneNo(rolePoint.getNo());
			SceneService1.notifyDelAIPoint(rolePoint);
		}
	}
	
	/**
	 * 玩家下线,场景处理
	 * @param roleInfo
	 */
	public static void userLoginOutExecu(RoleInfo roleInfo)
	{
		// 玩家下线,退出内城场景
		if(roleInfo.getRolePoint() != null)
		{
			Map<Integer,RolePoint> sceneRoleMap = SceneInfoMap.getSceneRoleMap(roleInfo.getRolePoint().getNo(),roleInfo.getRolePoint().getSceneId());
			if(sceneRoleMap != null && sceneRoleMap.containsKey(roleInfo.getId()))
			{
				sceneAILogout(roleInfo);
			}
		}
		
		// 玩家下线,退出大地图
		mapRoleDisappear(roleInfo);
		logger.info("### userLoginOutExecu,romove mapRoleoint,account="+roleInfo.getAccount()+",roleName="+roleInfo.getRoleName());
	}
	
	/**
	 * 角色换名字通知其它人
	 * @param roleInfo
	 */
	public static void roleNameUpdate(RoleInfo roleInfo)
	{
		RolePoint rolePoint = roleInfo.getRolePoint();
		if(rolePoint == null)
		{
			return;
		}
		
		Map<Integer,RolePoint> sceneRoleMap = SceneInfoMap.getSceneRoleMap(rolePoint.getNo(),rolePoint.getSceneId());
		if(sceneRoleMap != null)
		{
			for(RolePoint rolePoint1 : sceneRoleMap.values())
			{
				RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(rolePoint1.getRoleId());
				if(otherRoleInfo == null)
				{
					continue;
				}
				
				//玩家当前处于断开中
				if(otherRoleInfo.getDisconnectPhase() == 1)
				{
					continue;
				}
				
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.ROLE_CHANGE_NAME_NOTIFY);
				header.setUserID0(otherRoleInfo.getId());
				message.setHeader(header);
				
				NotifyUpdateNameResp resp = new NotifyUpdateNameResp();
				resp.setRoleId(roleInfo.getId());
				resp.setRoleName(roleInfo.getRoleName());
				resp.setClubName(RoleClubInfoMap.getClubName(roleInfo.getClubId()));
				
				message.setBody(resp);				
				SceneRefreService.sendRoleRefreshMsg(rolePoint1.getRoleId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
			}
		}
		
		// 也通知一下自己
		Message message = new Message();
		GameMessageHead header = new GameMessageHead();
		header.setMsgType(Command.ROLE_CHANGE_NAME_NOTIFY);
		header.setUserID0(roleInfo.getId());
		message.setHeader(header);
		
		NotifyUpdateNameResp resp = new NotifyUpdateNameResp();
		resp.setRoleId(roleInfo.getId());
		resp.setRoleName(roleInfo.getRoleName());
		resp.setClubName(RoleClubInfoMap.getClubName(roleInfo.getClubId()));
		
		message.setBody(resp);				
		SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
		
		/*
		//通知所有的角色关系
		FriendDetailRe re = new FriendDetailRe();
		
		re.setFightValue(roleInfo.getFightValue());
		re.setRoleId(roleInfo.getId());
		re.setRoleName(roleInfo.getRoleName());
		re.setStatus(roleInfo.getLoginStatus());
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo != null){
			re.setLevel(heroInfo.getHeroLevel());
			re.setHeroNo(heroInfo.getHeroNo());
		}
		
		
		Set<Integer> set = RoleAddRquestMap.getRequestKeySet();
		//好友请求列表
		for(Integer roleId : set){
			Set<Integer> relSet = RoleAddRquestMap.getAddRequestRoleIdSet(roleId);
			if(relSet.contains(roleInfo.getId())){
				sendMessage2ChangeRoleRelation(re, roleId, 8);
			}
		}
		
		
		set = RoleFriendMap.getRoleFriendKeySet();
		//好友列表
		for(Integer roleId : set){
			Set<Integer> relSet = RoleFriendMap.getRoleFriendIdSet(roleId);
			if(relSet.contains(roleInfo.getId())){
				sendMessage2ChangeRoleRelation(re, roleId, 5);
			}
		}
		
		//黑名单列表
		set = RoleBlackMap.getBlackRoleKeySet();
		for(Integer roleId : set){
			Set<Integer> relSet = RoleBlackMap.getBlackRoleIdSet(roleId);
			if(relSet.contains(roleInfo.getId())){
				sendMessage2ChangeRoleRelation(re, roleId, 9);
			}
		}
		
		//好友推荐列表
		set = FriendRecommendMap.getFriendRecommendKeySet();
		for(Integer roleId : set){
			Set<Integer> relSet = FriendRecommendMap.getFriendRecommendSet(roleId);
			if(relSet.contains(roleInfo.getId())){
				sendMessage2ChangeRoleRelation(re, roleId, 6);
			}
		}
		
		//赠送精力
		set = PresentEnergyInfoMap.getPresentEnergyInfoKeySet();
		Map<Integer, PresentEnergyInfo> map = null;
		for(Integer roleId : set){
			map = PresentEnergyInfoMap.getPresentEnergyInfoMap(roleId);
			if(map == null){
				continue;
			}
			
			for(Map.Entry<Integer, PresentEnergyInfo> entry : map.entrySet()){
				if(entry.getValue().getRelRoleId() == roleInfo.getId()){
					sendMessage2ChangeRoleRelation(re, roleId, 7);
				}
			}
			
		}
*/
	}
	
	
	/**
	 * 大R雕像刷新（用于大R装备变化或者排行变化时）
	 * @param roleInfo
	 */
	public static void superRUpdate(RoleInfo roleInfo)
	{
		
		if(roleInfo == null)
		{
			return;
		}
		//如果玩家处于断线状态
		if(roleInfo.getDisconnectPhase() == 1)
		{
			return;
		}
		RolePoint rolePoint = roleInfo.getRolePoint();
		if(rolePoint == null)
		{
			return;
		}
		List<RankInfo> list = RankService.getFightValueRank(1, 3);
		List<SuperRInfo> superList = new ArrayList<SuperRInfo>(); 		
		
		Message message = new Message();
		GameMessageHead header = new GameMessageHead();
		header.setMsgType(Command.NOTIFY_REFRESH_SUPER_RESP);
		header.setUserID0(roleInfo.getId());
		message.setHeader(header);
		
		NotifyRefreshSuperResp resp = new NotifyRefreshSuperResp();
		resp.setSceneNo(rolePoint.getNo());
		
		for(RankInfo rankInfo:list){
			SuperRInfo superRInfo = new SuperRInfo();
			RoleInfo superRoleInfo = RoleInfoMap.getRoleInfo(rankInfo.getRoleId());
			RideInfo rideInfo = superRoleInfo.getRideInfo();
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(superRoleInfo);
			if (mainHero == null) {
				return;
			}
			superRInfo.setRankNo(rankInfo.getRankNum()); 
			superRInfo.setHeroName(rankInfo.getName());
			superRInfo.setEquipNos(getHeroEquipNoforAvater(mainHero));
			superRInfo.setHeroNo(rankInfo.getHeroNo());
			superRInfo.setSuperRoleId(superRoleInfo.getId());
			superRInfo.setShowPlanId(superRoleInfo.getIsShowShizhuang());
			if(rideInfo != null){
				superRInfo.setRideNo(rideInfo.getRideNo());
			}
			superList.add(rankInfo.getRankNum()-1,superRInfo);
			
		}
		resp.setSuperRsCount(superList.size());
		resp.setSuperRs(superList);
		
		message.setBody(resp);				
		SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
		
	}
	
	/**
	 * avater 变化通知其它人
	 * @param roleInfo
	 */
	public static void roleEquipUpdate(RoleInfo roleInfo) {
		RolePoint rolePoint = roleInfo.getRolePoint();
		if (rolePoint == null) {
			return;
		}
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHero == null) {
			return;
		}

		Map<Integer, RolePoint> sceneRoleMap = SceneInfoMap.getSceneRoleMap(rolePoint.getNo(), rolePoint.getSceneId());
		if (sceneRoleMap != null) {
			for (RolePoint rolePoint1 : sceneRoleMap.values()) {
				if(roleInfo.getId() == rolePoint1.getRoleId()){
					continue;
				}
				
				RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(rolePoint1.getRoleId());
				if (otherRoleInfo == null) {
					continue;
				}
				//玩家当前处于断开中
				if(otherRoleInfo.getDisconnectPhase() == 1)
				{
					continue;
				}

				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.ROLE_CHANGE_EQUIP_NOTIFY);
				header.setUserID0(otherRoleInfo.getId());
				message.setHeader(header);

				NotifyUpdateEquipResp resp = new NotifyUpdateEquipResp();
				resp.setRoleId(roleInfo.getId());
				resp.setHeroNo(mainHero.getHeroNo());
				resp.setEquipNos(getHeroEquipNoforAvater(mainHero));
				resp.setShowPlanId(roleInfo.getIsShowShizhuang());
				message.setBody(resp);
				SceneRefreService.sendRoleRefreshMsg(rolePoint1.getRoleId(), SceneRefreService.REFRESH_TYPE_SCENE,
						message);
			}
		}
	}

	/**
	 * 获取武将avater 装备
	 * @param mainHero
	 * @return
	 */
	public static String getHeroEquipNoforAvater(HeroInfo mainHero) {
		StringBuffer sb = new StringBuffer();
		Map<Integer, EquipInfo> equipMap = EquipInfoMap.getHeroEquipMap(mainHero);
		if (equipMap != null) {
			for (EquipInfo equipInfo : equipMap.values()) {
				if (checkHeroEquipNoforAvater(equipInfo)) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(equipInfo.getEquipNo());
				}
			}
		}
		return sb.toString();
	}
	
	public static String getHeroEquipNoforAvater(HeroRecord mainHero) {
		StringBuffer sb = new StringBuffer();
		Map<Integer, EquipRecord> equipMap = mainHero.getEquipMap();
		if (equipMap != null) {
			for (EquipRecord equipInfo : equipMap.values()) {
				if (checkHeroEquipNoforAvater(equipInfo.getEquipType())) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(equipInfo.getEquipNo());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 判断是否是avater 装备
	 * @param mainHero
	 * @return
	 */
	public static boolean checkHeroEquipNoforAvater(EquipInfo equipInfo) {
		return checkHeroEquipNoforAvater(equipInfo.getEquipType());
	}
	
	/**
	 * 判断是否是avater 装备
	 * @param mainHero
	 * @return
	 */
	public static boolean checkHeroEquipNoforAvater(int equipType) {
		switch (equipType) {
		case 1:
		case 2:
		case 8:
		case 9:
		case 10:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * 世界地图上定时刷新世界BOSS
	 * @param roleInfo
	 */
	public static void raceTimeAddBoss(RoleInfo roleInfo)
	{	
		if (roleInfo.getRoleLoadInfo() == null) {
			return;
		}
		if(MapRoleInfoMap.getWorldBoss() == null || MapRoleInfoMap.getWorldBoss().size() <= 0){
			return;
		}
		//世界地图刷新
		sendWorldBossAddMessage(roleInfo);
	}
	
	/**
	 * 世界boss地图显示
	 */
	public static void sendWorldBossAddMessage(RoleInfo roleInfo)
	{
		MapNPCPointResp resp = new MapNPCPointResp();
		
		List<BossInfo> boss = MapRoleInfoMap.getWorldBoss();
		String npcArtStr = "";
		for(BossInfo bossInfo : boss)
		{
			//死亡后玩家不可见
			if(bossInfo == null || bossInfo.getCurrHP() <= 0)
			{
				continue;
			}
			npcArtStr = npcArtStr+bossInfo.getMapNo()+",";
			
		}
		if(npcArtStr.length()<=0){
			return;
		}
		
		npcArtStr = npcArtStr.substring(0, npcArtStr.length()-1);
		resp.setNpcArtStr(npcArtStr);
		
		Message message = new Message();
		GameMessageHead header = new GameMessageHead();
		header.setMsgType(Command.MAP_ADD_NPC_RESP);
		header.setUserID0((int)roleInfo.getId());
		message.setHeader(header);
		
		message.setBody(resp);
		SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);

	}
	
	/**
	 * 世界boss地图删除
	 * @param roleInfo
	 */
	public static void sendWorldBossDelMessage(RoleInfo roleInfo)
	{
		DelMapNPCResp resp = new DelMapNPCResp();
		
		Message message = new Message();
		GameMessageHead header = new GameMessageHead();
		header.setMsgType(Command.MAP_DEL_NPC_REDP);
		header.setUserID0((int)roleInfo.getId());
		message.setHeader(header);
		
		List<BossInfo> boss = MapRoleInfoMap.getWorldBoss();
		String npcArtStr = "";
		for(BossInfo bossInfo : boss){
			npcArtStr = npcArtStr+bossInfo.getMapNo()+",";
		}
		npcArtStr = npcArtStr.substring(0, npcArtStr.length()-1);
		resp.setNpcArtStr(npcArtStr);
		
		message.setBody(resp);
		SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);

	}
	
	/**
	 * 推送消息 5：更新我的好友， 6：更新推荐好友， 7：更新领取精力， 8：更新好友申请， 9：更新黑名单 10 : 好友下线通知
	 * 11 ：黑名单 12 : 好友请求  
	 * @param re
	 * @param roleId
	 * @param type
	 */
	public static void sendMessage2ChangeRoleRelation(FriendDetailRe re, int roleId, int type){
		FriendDetailResp resp = new FriendDetailResp();
		resp.setPushType(type);
		resp.getList().add(re);
		resp.setCount(resp.getList().size());
		resp.setResult(1);
		
		SceneService.sendRoleRefreshMsg(resp, roleId, Command.FRIEND_INFO_CHANGE_RESP);
	}
	
	/**
	 * 
	 * @param roleInfo
	 * @param type 1-你的镖车被***拦截了,损失了***q银子  2-你好友***的镖车被***拦截,你护送奖励损失了***银子, 3-你成功拦截了***镖车获得多少钱**银子(名字取attackRoleName)
	 * @param fightResult 1-战斗失败,2-战斗成功
	 * @param attackName
	 * @param friendName
	 * @param silver
	 */
	public static void yabiaoFightEndNotify(int roleId,byte type,int fightResult,String attackName,String friendName,int silver)
	{
		//提示押镖人或护镖人有人截镖				
		Message message = new Message();
		GameMessageHead header = new GameMessageHead();
		header.setMsgType(Command.YA_BIAO_FIGHT_END_RESP);
		header.setUserID0(roleId);
		message.setHeader(header);
		//你的镖车被拦截了
		BiaocheFightEndResp notifyResp = new BiaocheFightEndResp();
		notifyResp.setResult(1);
		notifyResp.setType(type);
		notifyResp.setFightResult((byte) fightResult);
		notifyResp.setAttackRoleName(attackName);
		notifyResp.setFriendRoleName(friendName);
		notifyResp.setSilverNum(silver);
		message.setBody(notifyResp);
		SceneRefreService.sendRoleRefreshMsg(roleId,SceneRefreService.REFRESH_TYPE_SCENE,message);
	}
	
	/**
	 * 双倍奖励时间
	 * 
	 * @return
	 */
	public static boolean yabiaoDobleTime() {

		try {
			String[] yabiaoTimes = GameValue.BIAO_CHE_TIME.split(",");
			
			for(String yabiaoTimeStr : yabiaoTimes){
				String[] yabiaoTime = yabiaoTimeStr.split("-");
				String startHour = yabiaoTime[0].split(":")[0];
				String endHour = yabiaoTime[1].split(":")[0];
				String startMin = yabiaoTime[0].split(":")[1];
				String endMin = yabiaoTime[1].split(":")[1];
				
				Calendar caStart = Calendar.getInstance();
				caStart.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startHour));
				caStart.set(Calendar.MINUTE, Integer.valueOf(startMin));
				caStart.set(Calendar.SECOND, 0);
				caStart.set(Calendar.MILLISECOND, 0);
				
				Calendar caEnd = Calendar.getInstance();
				caEnd.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endHour));
				caEnd.set(Calendar.MINUTE, Integer.valueOf(endMin));
				caEnd.set(Calendar.SECOND, 0);
				caEnd.set(Calendar.MILLISECOND, 0);
				
				long currentTime = System.currentTimeMillis();
				
				if (currentTime >= caStart.getTimeInMillis()
						&& currentTime <= caEnd.getTimeInMillis()) {
					return true;
				}
			}
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("yabiaoDoubleTime error, exception : " + e.getMessage());
			}
		}
		return false;
	}
	
}
