package com.snail.webgame.game.protocal.scene.sys;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.epilot.ccf.config.Resource;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.ClubSceneInfoMap;
import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.FightInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleFriendMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameFlag;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.CommonUtil;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.common.xml.info.VipXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.condtion.conds.EnergyCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.arena.service.ArenaService;
import com.snail.webgame.game.protocal.club.scene.service.ClubSceneService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.service.FightMgtService;
import com.snail.webgame.game.protocal.gmcc.service.GmccMgtService;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.biaocheEnd.BiaocheEndResp;
import com.snail.webgame.game.protocal.scene.biaocheHelp.BiaocheHelpNotifyResp;
import com.snail.webgame.game.protocal.scene.biaocheHelp.BiaocheHelpReq;
import com.snail.webgame.game.protocal.scene.biaocheHelp.BiaocheHelpResp;
import com.snail.webgame.game.protocal.scene.biaocheHelpExec.BiaocheHelpExecReq;
import com.snail.webgame.game.protocal.scene.biaocheHelpExec.BiaocheHelpExecResp;
import com.snail.webgame.game.protocal.scene.biaocheLanjie.BiaocheLanjieNotiyyResp;
import com.snail.webgame.game.protocal.scene.biaocheQuery.BiaocheQueryResp;
import com.snail.webgame.game.protocal.scene.biaocheQueryOther.BiaocheQueryOtherReq;
import com.snail.webgame.game.protocal.scene.biaocheQueryOther.BiaocheQueryOtherResp;
import com.snail.webgame.game.protocal.scene.biaocheRef.BiaocheRefResp;
import com.snail.webgame.game.protocal.scene.biaocheVipBuy.BiaocheVipBuyResp;
import com.snail.webgame.game.protocal.scene.biaochestart.BiaocheStartResp;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.cache.SceneInfoMap;
import com.snail.webgame.game.protocal.scene.cache.ViewChangeInfo;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.info.RolePoint;
import com.snail.webgame.game.protocal.scene.joinScene.RoleJoinSceneReq;
import com.snail.webgame.game.protocal.scene.mapMove.MapMoveReq;
import com.snail.webgame.game.protocal.scene.mapMove.MapMoveUpdateResp;
import com.snail.webgame.game.protocal.scene.mapPvpFight.MapPvpFightNofityResp;
import com.snail.webgame.game.protocal.scene.mapPvpFight.MapPvpFightReq;
import com.snail.webgame.game.protocal.scene.mapPvpFight.MapPvpFightResp;
import com.snail.webgame.game.protocal.scene.mapPvpIntoFight.MapPvpIntoFightReq;
import com.snail.webgame.game.protocal.scene.mapReachNPC.MapReachNPCReq;
import com.snail.webgame.game.protocal.scene.outCity.MapRolePointRe;
import com.snail.webgame.game.protocal.scene.outCity.QueryMePointResp;
import com.snail.webgame.game.protocal.scene.queryOtherAI.OtherHeroInfo;
import com.snail.webgame.game.protocal.scene.queryOtherAI.QueryOtherAIReq;
import com.snail.webgame.game.protocal.scene.queryOtherAI.QueryOtherAIResp;
import com.snail.webgame.game.protocal.scene.sceneRefre.SceneRefreService;
import com.snail.webgame.game.protocal.scene.screenMove.ScreenMoveReq;
import com.snail.webgame.game.protocal.scene.screenMove.ScreenMoveResp;
import com.snail.webgame.game.protocal.scene.stayMapNpc.StayMapNpcReq;
import com.snail.webgame.game.protocal.scene.stayMapNpc.StayMapNpcResp;
import com.snail.webgame.game.protocal.scene.sweepMapNpc.SweepMapNpcReq;
import com.snail.webgame.game.protocal.scene.sweepMapNpc.SweepMapNpcResp;
import com.snail.webgame.game.protocal.scene.updatePoint.UpdatePointReq;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.competition.request.MapVo;
import com.snail.webgame.game.pvp.service.PvpFightService;
import com.snail.webgame.game.xml.cache.GWXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.cache.SceneXmlInfoMap;
import com.snail.webgame.game.xml.cache.YabiaoPrizeXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.GWXMLInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXML;
import com.snail.webgame.game.xml.info.SceneXMLInfo.MapCityXMLNPC;
import com.snail.webgame.game.xml.info.YabiaoPrizeXMLInfo;

public class SceneMgtService {

	private static final Logger logger = LoggerFactory.getLogger("logs");
	private RoleDAO roleDAO = RoleDAO.getInstance();
	
	/**
	 * 场景中物体移动
	 */
	public void updatePoint(int roleId, UpdatePointReq req) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		RolePoint rolePoint1 = roleInfo.getRolePoint();
		if (rolePoint1 == null) {
			return;
		}

		synchronized (roleInfo) {
			RolePoint rolePoint = SceneInfoMap.getRolePoint(roleId, rolePoint1.getNo(), rolePoint1.getSceneId());
			if (rolePoint == null) {
				return;
			}

			rolePoint.setPointX(req.getCurPointX());
			rolePoint.setPointY(req.getCurPointY());
			rolePoint.setPointZ(req.getCurPointZ());

			// 移动时不广播其它玩家，客户端模拟其它玩家随机移动，只在上下线时通知其它玩家
			//SceneService1.notifyUpdateAIPoint(rolePoint);
		}
	}

	/**
	 * 玩家回城或进入游戏内城
	 * @return
	 */
	public void changeScene(int roleId, RoleJoinSceneReq req) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

		if (roleInfo == null) {
			logger.error("111########changeScene error,roleId="+roleId);
			return;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null)
		{
			logger.error("222########changeScene error,roleId="+roleId);
			return;
		}
		
		RolePoint rolePoint = null;
		synchronized(roleInfo)
		{
			if(roleInfo.getClubId() > 0){
				//判断公会场景内是否有角色 有则删除,避免出现强关客户端后没有清公会场景缓存
				RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
				if(roleClubInfo != null){
					RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleInfo.getClubId(), roleInfo.getId());
					
					
					if(memberInfo != null && memberInfo.getStatus() != RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
						Set<Integer> set = ClubSceneInfoMap.getSceneRoleSet(roleInfo.getClubId(), memberInfo.getSceneId());
						
						if(set.contains(roleInfo.getId())){
							set.remove(roleInfo.getId());
							
							//通知其他场景内的角色
							ClubSceneService.notifyDelClubSceneRole(roleInfo.getId(), set);
						}
					
					}
				}
				
			}
			
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			
			if(pointInfo != null && pointInfo.getBiaocheOtherRoleId() != 0){
				logger.error("777########changeScene error,roleId="+roleId);
				return;
			}
			if (req.getSceneNo() != 0) {
				// 场景切换,进入新场景
				SceneXMLInfo sceneXMlInfo = SceneXmlInfoMap.getSceneXml(req.getSceneNo());
				if (sceneXMlInfo == null) {
					logger.error("333########changeScene error,sceneNo="+req.getSceneNo());
					
					//错误的城市
					sceneXMlInfo = SceneXmlInfoMap.getSceneXml(GameValue.COMMON_SCENE_NO);
					if (sceneXMlInfo == null) {
						logger.error("666########changeScene error,sceneNo="+GameValue.COMMON_SCENE_NO);
						return;
					}
				}

				rolePoint = roleInfo.getRolePoint();
				if (rolePoint == null) {
					rolePoint = new RolePoint();
				}
				
				rolePoint.setRoleId(roleId);
				rolePoint.setNo(sceneXMlInfo.getNo());
				rolePoint.setPointX(sceneXMlInfo.getJinchengPointX());
				rolePoint.setPointY(sceneXMlInfo.getJinchengPointY());
				rolePoint.setPointZ(sceneXMlInfo.getJinchengPointZ());

				roleInfo.setRolePoint(rolePoint);
			}

			rolePoint = roleInfo.getRolePoint();
			if (rolePoint == null) {
				// 进入默认场景
				rolePoint = new RolePoint();
				SceneXMLInfo sceneXMlInfo = SceneXmlInfoMap.getSceneXml(GameValue.COMMON_SCENE_NO);
				if (sceneXMlInfo == null) {
					logger.error("444########changeScene error,sceneNo="+GameValue.COMMON_SCENE_NO);
					return;
				}
				rolePoint.setRoleId(roleInfo.getId());
				rolePoint.setNo(sceneXMlInfo.getNo());
				rolePoint.setPointX(sceneXMlInfo.getBornPointX());
				rolePoint.setPointY(sceneXMlInfo.getBornPointY());
				rolePoint.setPointZ(sceneXMlInfo.getBornPointZ());
				roleInfo.setRolePoint(rolePoint);
			}
			// 如果在大地图上移除在大地图上的位置
			SceneService1.mapRoleDisappear(roleInfo);
			//推送大R消息
			SceneService1.superRUpdate(roleInfo);
			logger.info("### changeScene,romove mapRoleoint,account="+roleInfo.getAccount()+",roleName="+roleInfo.getRoleName());
			
			//删除原有的战斗结算缓存
			FightInfoMap.removeFightResultByRoleId(roleId);
		}
		
		if(rolePoint.getNo() == 0)
		{
			// 数据库里没有存sceneNo,所以生成一个坐标,游戏坐标容错
			SceneXMLInfo sceneXMlInfo = SceneXmlInfoMap.getSceneXml(GameValue.COMMON_SCENE_NO);
			if (sceneXMlInfo == null) {
				logger.error("555########changeScene error,sceneNo="+GameValue.COMMON_SCENE_NO);
				return;
			}
			rolePoint.setRoleId(roleInfo.getId());
			rolePoint.setNo(sceneXMlInfo.getNo());
			rolePoint.setPointX(sceneXMlInfo.getBornPointX());
			rolePoint.setPointY(sceneXMlInfo.getBornPointY());
			rolePoint.setPointZ(sceneXMlInfo.getBornPointZ());
		}
			

		if(rolePoint != null)
		{
			SceneInfoMap.addScene(rolePoint);
		}
		SceneService1.notifyAddAIPoint(roleInfo,req.getDisConnectIntoScene());

	}

	/**
	 * 角色在场景中相应功能时,从场景中删除
	 */
	public static void AIDisapperForMoment(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo != null && roleInfo.getRolePoint() != null) {
			
			if(roleInfo.getRolePoint() != null)
			{
				Map<Integer,RolePoint> sceneRoleMap = SceneInfoMap.getSceneRoleMap(roleInfo.getRolePoint().getNo(),roleInfo.getRolePoint().getSceneId());
				if(sceneRoleMap != null && sceneRoleMap.containsKey(roleInfo.getId()))
				{
					SceneService1.notifyDelAIPoint(roleInfo.getRolePoint());
				}
			}
			
		}
	}
 
	/**
	 * 查看场景其它玩家
	 * @param req
	 * @return
	 */
	public QueryOtherAIResp queryOtherAI(QueryOtherAIReq req) {
		QueryOtherAIResp resp = new QueryOtherAIResp();
		resp.setResult(1);
		resp.setOtherRoleId(req.getOtherRoleId());
		//计算战斗力
		int fightValue = 0;
		
		List<OtherHeroInfo> heroList = new ArrayList<OtherHeroInfo>();
		
		if(req.getOtherRoleId() > 0)
		{
			// 查看场景玩家或竞技场中的玩家
			RoleInfo otherRole = RoleInfoMap.getRoleInfo(req.getOtherRoleId());
			if (otherRole == null) {
				resp.setResult(ErrorCode.SCENE_ERROR_1);
				return resp;
			}			
			HeroInfo otherHero = HeroInfoMap.getMainHeroInfo(otherRole);
			if (otherHero == null) {
				resp.setResult(ErrorCode.SCENE_ERROR_1);
				return resp;
			}
			HeroXMLInfo xmlInfo = HeroXMLInfoMap.getHeroXMLInfo(otherHero.getHeroNo());
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.SCENE_ERROR_1);
				return resp;
			}

			resp.setOtherRolaName(otherRole.getRoleName());
			resp.setHeroInfo(HeroService.getHeroDetailRe(otherHero));
			if(req.getIsWorship()!=0){
				resp.setWorShipTimes(otherRole.getBeWorshipNum());
			}
			
			//战斗力计算
			fightValue = otherRole.getFightValue();
			
			
			Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(otherRole.getId());
			if(heroMap != null && heroMap.size() > 0)
			{
				for(int heroId : heroMap.keySet())
				{
					HeroInfo heroInfo = heroMap.get(heroId);
					if(heroInfo == null || heroInfo.getDeployStatus() <= 1 || heroInfo.getDeployStatus() >= 6)
					{
						continue;
					}
					OtherHeroInfo otherHero1 = new OtherHeroInfo();
					otherHero1.setHeroNo(heroInfo.getHeroNo());
					otherHero1.setStar((byte) heroInfo.getStar());
					otherHero1.setQuality((byte) heroInfo.getQuality());
					otherHero1.setPosition(heroInfo.getDeployStatus());
					
					
					heroList.add(otherHero1);
				}
			}
			if(otherRole.getClubId() > 0 ){
				RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(otherRole.getClubId());
				if(roleClubInfo != null){
					resp.setClubName(roleClubInfo.getClubName());
				}
			}
		}
		else
		{
			// 查看异步竞技场NPC
			FightArenaInfo info = FightArenaInfoMap.getFightArenaInfobyArenaId(req.getArenaId());
			if(info == null)
			{
				resp.setResult(ErrorCode.SCENE_ERROR_2);
				return resp;
			}
			resp.setOtherRolaName(info.getRoleName());
			Map<Byte, HeroRecord> fightDeployMap = info.getFightDeployMap();
			if (fightDeployMap != null && fightDeployMap.size() > 0) {
				int recordFightValue = 0;
				for (HeroRecord record : fightDeployMap.values()) {
					recordFightValue = ArenaService.getFightValue(null, fightDeployMap, record, info,
								GameValue.ARENA_NPC_EQUIP_RATE);	
					fightValue += recordFightValue;
					if (record.getDeployStatus() == 1) {
						RoleInfo roleInfo = RoleInfoMap.getRoleInfo(info.getRoleId());
						resp.setHeroInfo(HeroRecordService.getHeroDetailRe(roleInfo, fightDeployMap, record));
						resp.getHeroInfo().getHeroInfo().setFightValue(recordFightValue);
					}
					else
					{
						OtherHeroInfo otherHero1 = new OtherHeroInfo();
						otherHero1.setHeroNo(record.getHeroNo());
						otherHero1.setStar((byte) record.getStar());
						otherHero1.setQuality((byte) record.getQuality());
						otherHero1.setPosition(record.getDeployStatus());
						
						heroList.add(otherHero1);
					}
				}
			}
		}
		
		resp.setHeroCount(heroList.size());
		resp.setHeroList(heroList);
		resp.setFightNum(fightValue);		
		return resp;
	}

	/**
	 * 玩家出城
	 * @param roleId
	 * @param type --1:护镖人瞬间移动  else-正常出城
	 * @param yabiaoRoleId --护镖人出城时，表示押镖人ID
	 */
	public QueryMePointResp outCity(int roleId, int type, int yabiaoRoleId)
	{
		QueryMePointResp resp = new QueryMePointResp();
		
		synchronized(GameFlag.INTO_MAP)
		{
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			if (roleInfo == null) 
			{
				resp.setResult(ErrorCode.OUT_CITY_ERROR_1);
				return resp;
			}
			synchronized(roleInfo)
			{
				RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
				
				if(roleLoadInfo == null){
					resp.setResult(ErrorCode.OUT_CITY_ERROR_2);
					return resp;
				}
				
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if(heroInfo == null)
				{
					resp.setResult(ErrorCode.OUT_CITY_ERROR_4);
					return resp;
				}
				
				RolePoint rolePoint1 = roleInfo.getRolePoint();
				if(rolePoint1 == null && type != 1)
				{
					resp.setResult(ErrorCode.OUT_CITY_ERROR_5);
					return resp;
				}
				
				RolePoint rolePoint = SceneInfoMap.getRolePoint(roleId,rolePoint1.getNo(),rolePoint1.getSceneId());
				if(rolePoint == null && type != 1)
				{
					resp.setResult(ErrorCode.OUT_CITY_ERROR_6);
					return resp;
				}
				
				SceneXMLInfo sceneXmlInfo = SceneXmlInfoMap.getSceneXml(rolePoint1.getNo());
				if(sceneXmlInfo == null)
				{
					sceneXmlInfo = SceneXmlInfoMap.getSceneXml(GameValue.COMMON_SCENE_NO);
					if(sceneXmlInfo == null)
					{
						resp.setResult(ErrorCode.OUT_CITY_ERROR_7);
						return resp;
					}
				}
				
				//如果玩家在主城中，清除在主城中的位置 出城后，重新回城，玩家处于回城点
				rolePoint1.setPointX(sceneXmlInfo.getDisaperPointX());
				rolePoint1.setPointY(sceneXmlInfo.getDisaperPointY());
				rolePoint1.setPointZ(sceneXmlInfo.getDisaperPointZ());
				
				if(rolePoint != null){
					SceneService1.notifyDelAIPoint(rolePoint);
				}
				
				MapCityXML mapCityXml = SceneXmlInfoMap.getMapCityXml(rolePoint1.getNo());
				if(mapCityXml == null)
				{
					mapCityXml = SceneXmlInfoMap.getMapCityXml(GameValue.COMMON_SCENE_NO);
					if(mapCityXml == null)
					{
						resp.setResult(ErrorCode.OUT_CITY_ERROR_8);
						return resp;
					}
				}
				
				// 护镖人直接飞的，不是正常的出城
				if(type == 1){
					MapRolePointInfo mapRolePointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
					
					if(mapRolePointInfo == null){
						mapRolePointInfo = new MapRolePointInfo();
						mapRolePointInfo.setPointX(mapCityXml.getPointX());
						mapRolePointInfo.setPointZ(mapCityXml.getPointZ());
						mapRolePointInfo.setRoleId((int)roleId);
						mapRolePointInfo.setRoleName(roleInfo.getRoleName());				
						mapRolePointInfo.setRolePic(heroInfo.getHeroNo());
						mapRolePointInfo.setRace(roleInfo.getRoleRace());
						mapRolePointInfo.setScreenPointX((int) mapRolePointInfo.getPointX());
						mapRolePointInfo.setScreenPointZ((int) mapRolePointInfo.getPointZ());
						
						MapRoleInfoMap.addMapPoint(mapRolePointInfo);
					}
					mapRolePointInfo.setBiaocheOtherRoleId(yabiaoRoleId);
					
					MapRolePointInfo yabiaoPointInfo = MapRoleInfoMap.getMapPointInfo(yabiaoRoleId);
					
					if (yabiaoPointInfo != null) {
						mapRolePointInfo.setPointX(yabiaoPointInfo.getPointX());
						mapRolePointInfo.setPointZ(yabiaoPointInfo.getPointZ());
						mapRolePointInfo.setStatus((byte) 7);
					}

					resp.setResult(1);
					resp.setPointRe(getMapRolePointRe(roleInfo, mapRolePointInfo));
				} else {
					MapRolePointInfo mapPoint = new MapRolePointInfo();
					mapPoint.setPointX(mapCityXml.getPointX());
					mapPoint.setPointZ(mapCityXml.getPointZ());
					mapPoint.setRoleId((int)roleId);
					mapPoint.setRoleName(roleInfo.getRoleName());				
					mapPoint.setRolePic(heroInfo.getHeroNo());
					mapPoint.setRace(roleInfo.getRoleRace());
					mapPoint.setScreenPointX((int) mapPoint.getPointX());
					mapPoint.setScreenPointZ((int) mapPoint.getPointZ());
					
					MapRoleInfoMap.addMapPoint(mapPoint);
					mapPoint.setStatus(SceneService1.getMapPointStatus(roleInfo));
					
					resp.setResult(1);
					resp.setPointRe(getMapRolePointRe(roleInfo, mapPoint));
				}
				
				//粮仓任务，大地图可见NPC
				SceneService1.raceTaskMapAddNpc(roleInfo);
				SceneService1.raceTimeAddBoss(roleInfo);
				SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.QUERY_TYPE_MINE,null);
				
				int mapRoleCount = MapRoleInfoMap.getAllRole();
				if (logger.isInfoEnabled()) {
					logger.info("cur time map have role num ="+mapRoleCount);
				}
				return resp;
			}
		}
	}

	/**
	 * 大地图移动
	 * @param req
	 */
	public void mapMove(int roleId, MapMoveReq req) 
	{
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) 
		{
			return;
		}
		MapRolePointInfo helpPointInfo = null;// 护镖人的
		MapRolePointInfo pointInfo;
		
		synchronized(roleInfo)
		{
			pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if (pointInfo == null) 
			{
				return;
			}

			pointInfo.setPointX(req.getPointx());
			pointInfo.setPointZ(req.getPointz());
			pointInfo.setWaitingHelp(false);
			pointInfo.getHelpList().clear();
			
			if(pointInfo.getStatus() == 4 && pointInfo.getBiaocheOtherRoleId() != 0){
				helpPointInfo = MapRoleInfoMap.getMapPointInfo(pointInfo.getBiaocheOtherRoleId());
			}
			
			ConcurrentHashMap<Integer,Integer> inOtherRoleScreen = pointInfo.getInOtherRoleScreen();
			for(int otherRoleId : inOtherRoleScreen.keySet())
			{
				MapRolePointInfo otherRolePointInfo = MapRoleInfoMap.getMapPointInfo(otherRoleId);
				RoleInfo otherRole = RoleInfoMap.getRoleInfo(otherRoleId);
				if (otherRolePointInfo == null) 
				{
					return;
				}
				if(otherRoleId == roleId)
				{
					continue;
				}
				if(otherRolePointInfo.getStatus() ==1 || otherRolePointInfo.getStatus() ==6)
				{
					continue;
				}
				
				if(otherRole == null || otherRole.getDisconnectPhase() == 1)
				{
					continue;
				}
				
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.MAP_NOTIFY_UPDATE_POINT_RESP);
				header.setUserID0(otherRoleId);
				message.setHeader(header);
				
				MapMoveUpdateResp resp = new MapMoveUpdateResp();
				resp.setRoleId(pointInfo.getRoleId());
				resp.setPointx(pointInfo.getPointX());
				resp.setPointz(pointInfo.getPointZ());
				resp.setStatus(SceneService1.getMapPointStatus(roleInfo));
				resp.setOtherRoleId(pointInfo.getBiaocheOtherRoleId());
				
				RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
				
				if(roleLoadInfo != null){
					resp.setBiaocheType(roleLoadInfo.getBiaocheType());
				}
				
				message.setBody(resp);
				SceneRefreService.sendRoleRefreshMsg(otherRoleId,SceneRefreService.REFRESH_TYPE_SCENE,message);
				
			}
			
		}
		
		if(helpPointInfo != null){
			mapMove(pointInfo.getBiaocheOtherRoleId(), req);
		}
	}
	
	
	/**
	 * 刷新大地图
	 * @param roleId
	 */
	public void screenFresh(int roleId)
	{
		synchronized(GameFlag.REFRESH_ACTION_OBJECT)
		{
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			if(roleInfo == null)
			{
				return;
			}
			
			MapRolePointInfo  pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if(pointInfo == null)
			{
				return;
			}
			
			//刷新后能看到的玩家
			ConcurrentHashMap<Integer,Integer> curRoleIds = MapRoleInfoMap.getScreenRole((int)pointInfo.getPointX(),(int)pointInfo.getPointZ(),roleId);
			
			//附近改变的玩家
			ViewChangeInfo viewChangeInfo = SceneService1.checkAreaChange(pointInfo.getScreenRoleIds(),curRoleIds);
			
			//1.屏幕内新出现的玩家
			List<MapRolePointRe> addRoleList = new ArrayList<MapRolePointRe>();
			List<Integer> addRoleIds = viewChangeInfo.getAddRoleIds();
			for(int addRoleId:addRoleIds)
			{
				// 刷新屏幕,遇见自己也不增加
				if(addRoleId == roleId)
				{
					continue;
				}
				
				MapRolePointInfo addRolePointInfo = MapRoleInfoMap.getMapPointInfo(addRoleId);
				if(addRolePointInfo == null)
				{
					continue;
				}
				RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(addRoleId);
				if(otherRoleInfo == null)
				{
					continue;
				}
				
				// 通知自己新加玩家
				addRoleList.add(getMapRolePointRe(otherRoleInfo, addRolePointInfo));
				
				// 记录在其它玩家屏幕内出现
				if(!addRolePointInfo.getInOtherRoleScreen().containsKey(roleId))
				{
					addRolePointInfo.getInOtherRoleScreen().put(roleId,roleId);
				}
			}
			
			//2.移除从屏幕消失的玩家
			List<Integer> delRoleIds = viewChangeInfo.getDelRoleIds();
			StringBuffer delRoleStr=new StringBuffer();
			for(int delRoleId:delRoleIds)
			{
				// 自己不能从屏幕消失，否则在大地图行军时报错
				if(delRoleId == roleId)
				{
					continue;
				}
				
				MapRolePointInfo delRolePointInfo = MapRoleInfoMap.getMapPointInfo(delRoleId);
				if(delRolePointInfo == null)
				{
					continue;
				}
				
				ConcurrentHashMap<Integer,Integer> iter = delRolePointInfo.getInOtherRoleScreen();
				iter.remove(roleId);				
				
				delRoleStr.append(delRoleId);
				delRoleStr.append(",");
				
			}
			
			pointInfo.setScreenRoleIds(curRoleIds);
			ScreenMoveResp resp = new ScreenMoveResp();
			resp.setResult(1);
			resp.setAddRoleCount(addRoleList.size());
			resp.setAddRoleList(addRoleList);
			resp.setDelRoleId(delRoleStr.toString());
			
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setMsgType(Command.SCREEN_MOVE_RESP);
			header.setUserID0(roleId);
			message.setHeader(header);
			message.setBody(resp);
			SceneRefreService.sendRoleRefreshMsg(roleId,SceneRefreService.REFRESH_TYPE_SCENE,message);
		}
	}
	
	/**
	 * 大地图PVP点击攻击其它人
	 * @param roleId
	 * @param req
	 * @return
	 */
	public MapPvpFightResp mapPvpFightStart(int roleId,MapPvpFightReq req)
	{
		MapPvpFightResp resp = new MapPvpFightResp();
		resp.setResult(1);
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		RoleInfo beRoleInfo = RoleInfoMap.getRoleInfo(req.getBeRoleId());
		
		if(roleInfo == null || beRoleInfo == null)
		{
			resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_7);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		RoleLoadInfo beRoleLoadInfo = beRoleInfo.getRoleLoadInfo();
		
		if(roleLoadInfo == null || beRoleLoadInfo == null)
		{
			resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_7);
			return resp;
		}
		if(roleId == req.getBeRoleId())
		{
			resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_15);
			return resp;
		}
		
		if(GameValue.PVE_FIGHT_FLAG == 0)
		{
			resp.setResult(ErrorCode.MUTUAL_STATUS_ERROR);
			return resp;
		}
		
		MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
		MapRolePointInfo bePointInfo = MapRoleInfoMap.getMapPointInfo(beRoleInfo.getId());
		if(pointInfo == null || bePointInfo == null || beRoleInfo.getDisconnectPhase() == 1)
		{
			resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_1);
			return resp;
		}
		
		if(bePointInfo.getStatus() == 7 || bePointInfo.getStatus() == 8){
			resp.setResult(ErrorCode.HU_BIAO_CHE_FIGHT_ERROR);
			return resp;
		}
		synchronized(GameFlag.MAP_PVP_START_FIGHT)
		{
			// 判断当前是否正在拦截中,拦截成功,提示2秒后正式进入战斗
			ConcurrentHashMap<Integer,Long> mapPvPFightRoleMap = MapRoleInfoMap.getMapPVPFightMap();
			if((mapPvPFightRoleMap.containsKey(roleInfo.getId()) && (System.currentTimeMillis() - mapPvPFightRoleMap.get(roleInfo.getId()) <= 3000)) 
					|| (mapPvPFightRoleMap.containsKey(beRoleInfo.getId()) && (System.currentTimeMillis() - mapPvPFightRoleMap.get(beRoleInfo.getId()) <= 3000)))
			{
				resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_10);
				return resp;
			}
			
			if(pointInfo.getStatus() != 0 && pointInfo.getStatus() != 3)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_39);
				return resp;
			}
			
			if(bePointInfo.getStatus() == 4)
			{
				if(beRoleLoadInfo.getInFight() != 0)
				{
					if(mapPvPFightRoleMap.containsKey(beRoleInfo.getId()) && (System.currentTimeMillis() - mapPvPFightRoleMap.get(beRoleInfo.getId()) > 30000))
					{
						mapPvPFightRoleMap.remove(beRoleInfo.getId());
					}
					resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_17);
				}
				// 截镖等级限制
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				
				if(heroInfo == null){
					resp.setResult(ErrorCode.BIAO_CHE_ERROR_29);
					return resp;
				}
				
				if(heroInfo.getHeroLevel() < GameValue.BIAO_CHE_ATTEND_LEVEL){
					resp.setResult(ErrorCode.LANJIE_BIAO_CHE_LEVEL_ERROR);
					return resp;
				}
				// 截镖
				if(req.getFightType() != 1){
					resp.setResult(ErrorCode.BIAO_CHE_FIGHT_ERROR1);
					return resp;
				}
				if(beRoleLoadInfo.getThisbiaocheTypeJieBiaoNum() >= GameValue.PER_BIAO_CHE_LAN_JIE_NUM)
				{
					resp.setResult(ErrorCode.BIAO_CHE_ERROR_19);
					return resp;
				}
				
				if(roleLoadInfo.getTodayJiebiaoNum() >= GameValue.TODAY_JIE_BIAO_NUM)
				{
					resp.setResult(ErrorCode.BIAO_CHE_ERROR_20);
					return resp;
				}
				
				//提示押镖人或护镖人有人劫镖				
				Message message1 = new Message();
				GameMessageHead header1 = new GameMessageHead();
				header1.setMsgType(Command.BIAO_CHE_BE_LAN_JIE_RESP);
				header1.setUserID0((int)beRoleInfo.getId());
				message1.setHeader(header1);
				//你的镖车被拦截了
				BiaocheLanjieNotiyyResp notifyResp1 = new BiaocheLanjieNotiyyResp();
				notifyResp1.setType1((byte)1);
				notifyResp1.setAttackRoleName(roleInfo.getRoleName());
				message1.setBody(notifyResp1);
				SceneRefreService.sendRoleRefreshMsg(beRoleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message1);
				
				// 护镖人
				if(beRoleLoadInfo.getHubiaoRoleId() > 0)
				{
					Message message2 = new Message();
					GameMessageHead header2 = new GameMessageHead();
					header2.setMsgType(Command.BIAO_CHE_BE_LAN_JIE_RESP);
					header2.setUserID0(beRoleLoadInfo.getHubiaoRoleId());
					message2.setHeader(header2);
					// 你好友的镖车被拦截了,请准备好
					BiaocheLanjieNotiyyResp notifyResp2 = new BiaocheLanjieNotiyyResp();
					notifyResp2.setType1((byte)2);
					notifyResp2.setAttackRoleName(roleInfo.getRoleName());
					notifyResp2.setFriendRoleName(beRoleInfo.getRoleName());
					message2.setBody(notifyResp2);
					SceneRefreService.sendRoleRefreshMsg(beRoleLoadInfo.getHubiaoRoleId(),SceneRefreService.REFRESH_TYPE_SCENE,message2);
				}
				
				
				Message message3 = new Message();
				GameMessageHead header3 = new GameMessageHead();
				header3.setMsgType(Command.BIAO_CHE_BE_LAN_JIE_RESP);
				header3.setUserID0(roleInfo.getId());
				message3.setHeader(header3);
				// 你拦截的谁的镖车
				BiaocheLanjieNotiyyResp notifyResp3 = new BiaocheLanjieNotiyyResp();
				notifyResp3.setType1((byte)3);
				notifyResp3.setAttackRoleName(beRoleInfo.getRoleName());
				message3.setBody(notifyResp3);
				SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message3);
			}
			else
			{
				if(beRoleLoadInfo.getInFight() != 0)
				{
					if(mapPvPFightRoleMap.containsKey(beRoleInfo.getId()) && (System.currentTimeMillis() - mapPvPFightRoleMap.get(beRoleInfo.getId()) > 30000))
					{
						mapPvPFightRoleMap.remove(beRoleInfo.getId());
					}
					
					resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_16);
				}
				// 正常拦截
				if(req.getFightType() != 2){
					resp.setResult(ErrorCode.BIAO_CHE_FIGHT_ERROR2);
					return resp;
				}
				//判断状态
				if(bePointInfo.getStatus() != 0)
				{
					resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_2);
					return resp;
				}
				
				if (roleInfo.getRoleLoadInfo().getInFight() == 4) {
					if (System.currentTimeMillis() - roleInfo.getRoleLoadInfo().getFightStartTime() < GameValue.COMPETITION_FIGHT_MAX_TIME) {
						// 战斗中，并且未到战斗结束时间
						resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_12);
						return resp;
					} else {
						roleInfo.getRoleLoadInfo().setInFight((byte)0);
					}
				}
				
				if (beRoleInfo.getRoleLoadInfo().getInFight() == 4) {
					if (System.currentTimeMillis() - beRoleInfo.getRoleLoadInfo().getFightStartTime() < GameValue.COMPETITION_FIGHT_MAX_TIME) {
						// 战斗中，并且未到战斗结束时间
						resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_12);
						return resp;
					} else {
						beRoleInfo.getRoleLoadInfo().setInFight((byte)0);
					}
				}
				
				// 判断上次间隔时间
				if(System.currentTimeMillis() - beRoleInfo.getMapPvpFightTime().getTime() < GameValue.PRE_MAP_PVP_FIGHT_TIME)
				{
					resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_3);
					return resp;
				}
				
				//开战前，提示双方玩家战斗信息(你攻击了***,***攻击了你)
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.MAP_PVP_FIGHT_NOFIFY_RESP);
				header.setUserID0((int)roleInfo.getId());
				message.setHeader(header);
				//你攻击了***
				MapPvpFightNofityResp notifyResp = new MapPvpFightNofityResp();
				notifyResp.setType1((byte)1);
				notifyResp.setRoleName(beRoleInfo.getRoleName());
				message.setBody(notifyResp);
				SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
				
				
				Message message1 = new Message();
				GameMessageHead header1 = new GameMessageHead();
				header1.setMsgType(Command.MAP_PVP_FIGHT_NOFIFY_RESP);
				header1.setUserID0((int)beRoleInfo.getId());
				message1.setHeader(header1);
				//你被***攻击了
				MapPvpFightNofityResp notifyResp1 = new MapPvpFightNofityResp();
				notifyResp1.setType1((byte)2);
				notifyResp1.setRoleName(roleInfo.getRoleName());
				message1.setBody(notifyResp1);
				SceneRefreService.sendRoleRefreshMsg(beRoleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message1);
			}
			
			mapPvPFightRoleMap.put(roleInfo.getId(), System.currentTimeMillis());
			mapPvPFightRoleMap.put(beRoleInfo.getId(), System.currentTimeMillis());
		}
		
		
		return resp;
	}
	
	/**
	 * 大地图PVP提示2秒后开始战斗
	 * @param roleId
	 * @param req
	 * @return
	 */
	public MapPvpFightResp mapPvpIntoFight(int roleId,MapPvpIntoFightReq req)
	{
		MapPvpFightResp resp = new MapPvpFightResp();
		resp.setResult(1);
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		RoleInfo beRoleInfo = RoleInfoMap.getRoleInfoByName(req.getBeRoleName());
		if(roleInfo == null || beRoleInfo == null || roleInfo.getRoleLoadInfo() == null || beRoleInfo.getRoleLoadInfo() == null)
		{
			resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_7);
			return resp;
		}
		
		synchronized(GameFlag.MAP_PVP_INTO_FIGHT)
		{
			if(roleInfo.getRoleLoadInfo().getInFight() == 4 || roleInfo.getRoleLoadInfo().getInFight() == 5)
			{
				resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_13);
				return resp;
			}
			
			if(beRoleInfo.getRoleLoadInfo().getInFight() == 4 || beRoleInfo.getRoleLoadInfo().getInFight() == 5)
			{
				resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_14);
				return resp;
			}
			
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			MapRolePointInfo bePointInfo = MapRoleInfoMap.getMapPointInfo(beRoleInfo.getId());
			if(pointInfo == null || bePointInfo == null)
			{
				resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_1);
				return resp;
			}
			
			if(bePointInfo.getStatus() != 4 || bePointInfo.getStatus() !=7){
				 if(beRoleInfo.getDisconnectPhase() == 1){
					 resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_1);
					 return resp;
				 }
			}
		
			// 判断当前是否有拦截行为
			ConcurrentHashMap<Integer,Long> mapPvPFightRoleMap = MapRoleInfoMap.getMapPVPFightMap();		
			if(!(mapPvPFightRoleMap.containsKey(roleInfo.getId()) && mapPvPFightRoleMap.containsKey(beRoleInfo.getId())))
			{
				resp.setResult(ErrorCode.MAP_PVP_FIGHT_ERROR_11);
				return resp;
			}
			
			List<ComFightRequestReq> list = new ArrayList<ComFightRequestReq>();
			ComFightRequestReq comFightRequestReq1 = null;
			ComFightRequestReq comFightRequestReq2 = null;
			
			comFightRequestReq1 = SceneService1.createPvpFightReq(roleInfo);
			list.add(comFightRequestReq1);
			
			if(bePointInfo.getStatus() == 4 && beRoleInfo.getRoleLoadInfo().getHubiaoRoleId() > 0)
			{
				// 押镖状态有好友护送
				RoleInfo helpRoleInfo = RoleInfoMap.getRoleInfo(beRoleInfo.getRoleLoadInfo().getHubiaoRoleId());
				comFightRequestReq2 = SceneService1.createPvpFightReq(helpRoleInfo);
				list.add(comFightRequestReq2);
			}
			else
			{
				comFightRequestReq2 = SceneService1.createPvpFightReq(beRoleInfo);
				list.add(comFightRequestReq2);
			}
			
			//发送双方数据
			byte fightType = 2;
			if(bePointInfo.getStatus() == 4)
			{
				comFightRequestReq1.setMapVo(new MapVo());
				comFightRequestReq2.setMapVo(new MapVo());
				fightType = 5;
			}
			int result = PvpFightService.sendPvpMap(list,fightType);
			
			if (result == 0) {
				resp.setResult(ErrorCode.FIGHT_SERVER_INAVAILABLE);
				return resp;
			}
			roleInfo.setMapPvpFightTime(new Timestamp(0));
			roleInfo.getRoleLoadInfo().setMapPvpAttack(true);
			roleInfo.getRoleLoadInfo().setInFight((byte)5);
			if (beRoleInfo.getRoleLoadInfo().getHubiaoRoleId() == 0)
			{
				beRoleInfo.getRoleLoadInfo().setInFight((byte)5);
			}
			
			beRoleInfo.getRoleLoadInfo().setJiebiaoRoleId(roleInfo.getId());
			
			mapPvPFightRoleMap.remove(roleInfo.getId());
			mapPvPFightRoleMap.remove(beRoleInfo.getId());
		}
		
		return resp;
	}
	
	/**
	 * 世界地图PVP,PVE战斗结束,胜利一方点击结算界面之后，重新在世界场景中
	 * @param roleInfo
	 * @param type 1-进入战斗超时调用
	 */
	public static QueryMePointResp mapPVPFightEnd(int roleId, int type)
	{
		QueryMePointResp resp = new QueryMePointResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null)
		{
			resp.setResult(ErrorCode.OUT_CITY_ERROR_9);
			return resp;
		}
		synchronized(roleInfo)
		{
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo((int)roleInfo.getId());
			
			if(pointInfo == null && type != 1)
			{
				mapFightEndGenMapPoint(roleInfo);
				logger.error("######mapPVPFightEnd error,point remove otherPlace,regen...account="+roleInfo.getAccount()
						+",roleName="+roleInfo.getRoleName()+",type="+type);
				
				pointInfo = MapRoleInfoMap.getMapPointInfo((int)roleInfo.getId());
			}

			if(pointInfo == null)
			{
				// 如果不再地图上，回城
				ScreenMoveResp screenMoveResp = new ScreenMoveResp();
				screenMoveResp.setResult(ErrorCode.SCREEN_MOVE_ERROR_2);
				
				Message message1 = new Message();
				GameMessageHead header1 = new GameMessageHead();
				header1.setMsgType(Command.SCREEN_MOVE_RESP);
				header1.setUserID0(roleId);
				message1.setHeader(header1);
				message1.setBody(screenMoveResp);
				SceneRefreService.sendRoleRefreshMsg(roleId,SceneRefreService.REFRESH_TYPE_SCENE,message1);
				
				logger.error("######mapPVPFightEnd error too,system error....");
				resp.setResult(ErrorCode.MAP_NPC_FIGHT_ERROR_1);
				return resp;
			}
			
			if(pointInfo.getStatus() == 1)
			{
				if(System.currentTimeMillis() - roleInfo.getMapPvpFightTime().getTime() < GameValue.PRE_MAP_PVP_FIGHT_TIME){
					pointInfo.setStatus((byte) 3);
				} else {
					pointInfo.setStatus((byte) 0);
				}
			}
			else if (pointInfo.getStatus() == 6)
			{
				pointInfo.setStatus((byte) 4);
			} else if(pointInfo.getStatus() == 8){
				pointInfo.setStatus((byte) 7);
			}
			
			SceneService1.brocastRolePointStatus(pointInfo,roleInfo);
			
			if(type != 1){
				// 修改屏幕里看到的玩家的缓存
				ConcurrentHashMap<Integer,Integer> roleIds = pointInfo.getScreenRoleIds();
				
				for(int otherRoleId:roleIds.keySet())
				{
					MapRolePointInfo otherRointInfo = MapRoleInfoMap.getMapPointInfo(otherRoleId);
					if(otherRointInfo == null)
					{
						continue;
					}
					ConcurrentHashMap<Integer,Integer> iter = otherRointInfo.getInOtherRoleScreen();
					iter.remove(roleId);
				}
				
				// 战斗胜利后，从战斗场景到世界地图，清除战斗前自己能看到的其它玩家数据(因战斗时世界地图数据客户端不能缓存)
//				pointInfo.getInOtherRoleScreen().clear();
				pointInfo.getScreenRoleIds().clear();
				
				resp.setResult(1);
				resp.setPointRe(getMapRolePointRe(roleInfo, pointInfo));
				
				//粮仓任务，大地图可见NPC
				SceneService1.raceTaskMapAddNpc(roleInfo);
				//世界地图刷新
				SceneService1.raceTimeAddBoss(roleInfo);
				
				SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.QUERY_TYPE_MINE,null);
			}
		}
		return resp;
	}
	
	/**
	 * 世界地图PVP,PVE战斗结束,胜利一方点击结算界面之后,重新在世界场景中,此时发现自己地图对像清除了,
	 * 默认回到主城外面,防止客户端战斗结束后卡死
	 */
	public static void mapFightEndGenMapPoint(RoleInfo roleInfo)
	{
		MapCityXML mapCityXml = SceneXmlInfoMap.getMapCityXml(roleInfo.getRolePoint().getNo());
		if(mapCityXml == null)
		{
			logger.error("### mapFightEndGenMapPoint error,account="+roleInfo.getAccount()+",roleName="+roleInfo.getRoleName()
					+",cityNo="+roleInfo.getRolePoint().getNo());
			
			mapCityXml = SceneXmlInfoMap.getMapCityXml(GameValue.COMMON_SCENE_NO);
			if(mapCityXml == null)
			{
				logger.error("### mapFightEndGenMapPoint xml error");
				return;
			}
		}
		
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null)
		{
			logger.error("### mapFightEndGenMapPoint hero is null");
			return;
		}
		
		MapRolePointInfo mapPoint = new MapRolePointInfo();
		mapPoint.setPointX(mapCityXml.getPointX());
		mapPoint.setPointZ(mapCityXml.getPointZ());
		mapPoint.setRoleId(roleInfo.getId());
		mapPoint.setRoleName(roleInfo.getRoleName());				
		mapPoint.setRolePic(heroInfo.getHeroNo());
		mapPoint.setRace(roleInfo.getRoleRace());
		mapPoint.setScreenPointX((int) mapPoint.getPointX());
		mapPoint.setScreenPointZ((int) mapPoint.getPointZ());
		
		MapRoleInfoMap.addMapPoint(mapPoint);
		mapPoint.setStatus(SceneService1.getMapPointStatus(roleInfo));
	}
	
	/**
	 * 到达大地图某个NPC(散步流言,刺探军情,烧毁物资任务)
	 * @param roleId
	 * @param req
	 * @return
	 */
	public void mapReachNPC(int roleId,MapReachNPCReq req) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		
		int no = 0;
		if (req.getReachType() == 1) {
			MapCityXMLNPC mapCityNPCXml = SceneXmlInfoMap.getMapCityXMLNPC(req.getMapCityNPCNo());
			if (mapCityNPCXml != null) {
				no = mapCityNPCXml.getNo();
			}
		} else if (req.getReachType() == 2) {
			MapCityXML mapCityXml = SceneXmlInfoMap.getMapCityXml(req.getMapCityNPCNo());
			if (mapCityXml != null) {
				no = mapCityXml.getNo();
			}
		}
		
		if (no <= 0) {
			return;
		}
		
		synchronized(roleInfo) {
			// 任务 拜访贤者
			int gameAction = ActionType.action17.getType();
			if (req.getReachType() == 2) {
				gameAction = ActionType.action30.getType();
			}
			QuestService.checkQuest(roleInfo, gameAction, no, true, true);
			
			GameLogService.insertPlayActionLog(roleInfo, gameAction, 1, "");
		}
	}
	
	/**
	 * 玩家在大地图上某个NPC点驻足
	 * @param roleId
	 * @param req
	 */
	public void mapStayMap(int roleId,StayMapNpcReq req)
	{
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null)
		{
			return;
		}
		MapRolePointInfo  otherRointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
		if(otherRointInfo == null)
		{
			return;
		}
		
		HashMap<Integer,Long> stayNpcMap = otherRointInfo.getStayNpcMap();
		if(req.getType1() == 1)
		{
			//开始点火
			stayNpcMap.put(req.getNo(), System.currentTimeMillis());
		}
		else if(req.getType1() == 2)
		{
			//点火取消
			if(stayNpcMap.containsKey(req.getNo()))
			{
				stayNpcMap.remove(req.getNo());
			}
		}
		else
		{
			//点火结束,任务完成
			long stayTime = stayNpcMap.get(req.getNo());
			MapCityXMLNPC mapCityNPCXml = SceneXmlInfoMap.getMapCityXMLNPC(req.getNo());
			if(stayNpcMap.containsKey(req.getNo()) && mapCityNPCXml != null && System.currentTimeMillis() - stayTime >= mapCityNPCXml.getTimeDown()*1000l)
			{
				// 任务
				QuestService.checkQuest(roleInfo, ActionType.action18.getType(), mapCityNPCXml.getNo(), true, true);
				
				//提示大地图粮仓掠夺
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.MAP_STAY_NPC_RESP);
				header.setUserID0((int)roleInfo.getId());
				message.setHeader(header);
				StayMapNpcResp resp = new StayMapNpcResp();
				resp.setNo(req.getNo());
				message.setBody(resp);
				SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);

				stayNpcMap.remove(req.getNo());
				
				SceneService1.raceTaskMapDelNpc(roleId,mapCityNPCXml);
			}
		}
			
	}
	
	/**
	 * 玩家设备屏幕移到哪,就能看到哪的人
	 * 
	 * @param roleId
	 * @param req
	 * @param isAllRefresh 护镖人的观众，都要刷一下
	 * @return
	 */
	public ScreenMoveResp screenMove(int roleId, ScreenMoveReq req) 
	{
		ScreenMoveResp resp = new ScreenMoveResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) 
		{
			resp.setResult(ErrorCode.SCREEN_MOVE_ERROR_1);
			return resp;
		}
		synchronized(roleInfo)
		{
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if (pointInfo == null) 
			{
				resp.setResult(ErrorCode.SCREEN_MOVE_ERROR_2);
				return resp;
			}
			pointInfo.setScreenPointX(req.getPointx());
			pointInfo.setScreenPointZ(req.getPointz());
			
			ConcurrentHashMap<Integer,Integer> screenRoleList = MapRoleInfoMap.getScreenRole((int)req.getPointx(), (int)req.getPointz(),roleId);
			
			if(pointInfo.getBiaocheOtherRoleId() != 0){
				screenRoleList.put(pointInfo.getBiaocheOtherRoleId(), pointInfo.getBiaocheOtherRoleId());
			}
			//附近改变的玩家
			ViewChangeInfo viewChangeInfo = SceneService1.checkAreaChange(pointInfo.getScreenRoleIds(),screenRoleList);
			
			StringBuffer delRoleStr=new StringBuffer();
			List<MapRolePointRe> addRoleList = new ArrayList<MapRolePointRe>();
			
			if(req.getDisConnectIntoMap() == 1)
			{
				// 玩家断线重连后的屏幕玩家
				for(int addRoleId:screenRoleList.keySet())
				{
					MapRolePointInfo addRolePointInfo = MapRoleInfoMap.getMapPointInfo(addRoleId);
					if(addRolePointInfo == null)
					{
						continue;
					}
					RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(addRoleId);
					if(otherRoleInfo == null)
					{
						continue;
					}
					
					addRoleList.add(getMapRolePointRe(otherRoleInfo, addRolePointInfo));
					
					//对其它玩家的影响
					//1.屏幕内新出现的玩家
					List<Integer> addRoleIds = viewChangeInfo.getAddRoleIds();
					for(int otherRoleId:addRoleIds)
					{
						MapRolePointInfo otherRolePointInfo = MapRoleInfoMap.getMapPointInfo(otherRoleId);
						if(otherRolePointInfo == null)
						{
							continue;
						}
						RoleInfo otherRoleInfo1 = RoleInfoMap.getRoleInfo(otherRoleId);
						if(otherRoleInfo1 == null)
						{
							continue;
						}
						
						// 玩家出城,不管是否在屏幕内,都发给客户端,后期屏幕移动时,遇见自己也不增加
						if(pointInfo.isOutCity() && otherRoleId == roleId)
						{
							continue;
						}
						
						// 记录在其它玩家屏幕内出现
						addRolePointInfo.getInOtherRoleScreen().put(roleId,roleId);
					}
					
					//2.移除从屏幕消失的玩家
					List<Integer> delRoleIds = viewChangeInfo.getDelRoleIds();
					
					for(int delRoleId:delRoleIds)
					{
						// 自己不能从屏幕消失，否则在大地图行军时报错
						if(delRoleId == roleId)
						{
							continue;
						}
						
						MapRolePointInfo delRolePointInfo = MapRoleInfoMap.getMapPointInfo(delRoleId);
						if(delRolePointInfo == null)
						{
							continue;
						}
						ConcurrentHashMap<Integer,Integer> iter = delRolePointInfo.getInOtherRoleScreen();
						iter.remove(roleId);
						
						delRoleStr.append(delRoleId);
						delRoleStr.append(",");
						
					}
				}
				SceneRefreService.sendRoleRefreshMsg(roleId, SceneRefreService.QUERY_TYPE_MINE, null);
			}
			else
			{
				// 正常移动屏幕
				
				//1.屏幕内新出现的玩家
				List<Integer> addRoleIds = viewChangeInfo.getAddRoleIds();
				for(int addRoleId:addRoleIds)
				{
					MapRolePointInfo addRolePointInfo = MapRoleInfoMap.getMapPointInfo(addRoleId);
					if(addRolePointInfo == null)
					{
						continue;
					}
					RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(addRoleId);
					if(otherRoleInfo == null)
					{
						continue;
					}
					
					// 玩家出城,不管是否在屏幕内,都发给客户端,后期屏幕移动时,遇见自己也不增加
					if(/*pointInfo.isOutCity() &&*/ addRoleId == roleId)
					{
						continue;
					}
					
					// 通知自己新加玩家
					addRoleList.add(getMapRolePointRe(otherRoleInfo, addRolePointInfo));
					
					// 记录在其它玩家屏幕内出现
					addRolePointInfo.getInOtherRoleScreen().put(roleId,roleId);
				}
				
				//2.移除从屏幕消失的玩家
				List<Integer> delRoleIds = viewChangeInfo.getDelRoleIds();
				
				for(int delRoleId:delRoleIds)
				{
					// 自己不能从屏幕消失，否则在大地图行军时报错
					if(delRoleId == roleId)
					{
						continue;
					}
					
					MapRolePointInfo delRolePointInfo = MapRoleInfoMap.getMapPointInfo(delRoleId);
					if(delRolePointInfo == null)
					{
						continue;
					}
					
					ConcurrentHashMap<Integer,Integer> iter = delRolePointInfo.getInOtherRoleScreen();
					iter.remove(roleId);
					
					delRoleStr.append(delRoleId);
					delRoleStr.append(",");
					
				}
			}
			
			resp.setResult(1);
			resp.setAddRoleCount(addRoleList.size());
			resp.setAddRoleList(addRoleList);
			resp.setDelRoleId(delRoleStr.toString());
			
			pointInfo.setScreenRoleIds(screenRoleList);
			pointInfo.setOutCity(true);
		}
		
		return resp;
	}
	
	/**
	 * 刷新镖车
	 * @param roleId
	 * @return
	 */
	public BiaocheRefResp biaocheRef(int roleId)
	{
		BiaocheRefResp resp = new BiaocheRefResp();
		resp.setResult(1);
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) {
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		if (roleLoadInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_1);
			return resp;
		}
		synchronized(roleInfo)
		{
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if (pointInfo == null) 
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_2);
				return resp;
			}
			
			if(roleLoadInfo.getBiaocheType() == 5)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_41);
				return resp;
			}
			
			int refBiaocheNum = roleLoadInfo.getRefBiaoCheNum() + 1 - GameValue.BIAO_CHE_REF_FREE_NUM;
			float needCoin = 0;
			
			if(refBiaocheNum > 0)
			{
				needCoin = GameValue.REF_BIAO_CHE_BASE_COIN * refBiaocheNum;
				
				List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
				conds.add(new CoinCond((long) needCoin));
				int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}
				
				if (RoleService.subRoleResource(ActionType.action379.getType(), roleInfo, conds , null)) {
					String updateSourceStr = RoleService.returnResourceChange(conds);
					if (updateSourceStr != null) {
						String[] sourceStr = updateSourceStr.split(",");
						if (sourceStr != null && sourceStr.length > 1) {
							resp.setSourceType(Byte.valueOf(sourceStr[0]));
							resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
						}
					}
				} else {
					resp.setResult(ErrorCode.BIAO_CHE_ERROR_3);
					return resp;
				}
			}
			
			int biaocheType = YabiaoPrizeXMLMap.getBiaocheType(roleLoadInfo.getBiaocheType());
			
			if(!roleDAO.updateRefBiaocheNum_biaocheType_ThisBiaocheTypeJiebiaoNum(roleId, (byte) (roleLoadInfo.getRefBiaoCheNum()+1), (byte)biaocheType, (byte) 0)){
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_3);
				return resp;
			}
			
			roleLoadInfo.setRefBiaoCheNum((byte) (roleLoadInfo.getRefBiaoCheNum()+1));
			roleLoadInfo.setBiaocheType((byte)biaocheType);
			roleLoadInfo.setNotice(false);
			roleLoadInfo.setThisbiaocheTypeJieBiaoNum((byte) 0);
			
			resp.setBiaocheType((byte)biaocheType);
			resp.setBiaocheFreeRefNum((byte) ((GameValue.BIAO_CHE_REF_FREE_NUM - roleLoadInfo.getRefBiaoCheNum()) >=0 ? (GameValue.BIAO_CHE_REF_FREE_NUM - roleLoadInfo.getRefBiaoCheNum()):0));
			resp.setBiaocheRefNum((byte) ((roleLoadInfo.getRefBiaoCheNum()-GameValue.BIAO_CHE_REF_FREE_NUM) > 0 ? (roleLoadInfo.getRefBiaoCheNum()-GameValue.BIAO_CHE_REF_FREE_NUM) : 0));
			
			GameLogService.insertBiaocheLog(roleInfo, ActionType.action379.getType(), (int)roleLoadInfo.getRefBiaoCheNum(), 0,0,(int)needCoin,(byte)biaocheType,1);
			
		}
		return resp;
	}
	
	/**
	 * 开始压镖
	 * @param roleId
	 * @return
	 */
	public BiaocheStartResp biaocheStart(int roleId)
	{
		BiaocheStartResp resp = new BiaocheStartResp();
		resp.setResult(1);
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if (roleInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_11);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_11);
			return resp;
		}
		synchronized(roleInfo)
		{
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if (pointInfo == null) 
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_12);
				return resp;
			}

			if(roleInfo.getRoleLoadInfo().getInFight() != 0 || RoleService.checkRoleInFight(roleInfo)
					|| (pointInfo.getStatus() != 0 && pointInfo.getStatus() != 2 && pointInfo.getStatus() != 3)){
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_12);
				return resp;
			}
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			
			if(heroInfo == null){
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_12);
				return resp;
			}
			
			if(heroInfo.getHeroLevel() < GameValue.BIAO_CHE_ATTEND_LEVEL){
				resp.setResult(ErrorCode.BIAO_CHE_LEVEL_ERROR);
				return resp;
			}
			
			if(roleLoadInfo.getTodayYabiaoNum() >= getTodayFreeYaBiaoNum(roleInfo))
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_40);
				return resp;
			}
			pointInfo.setStatus((byte)4);
			pointInfo.setWaitingHelp(false);
			pointInfo.getHelpList().clear();
			resp.setBiaocheType(roleLoadInfo.getBiaocheType());
			
			if(roleLoadInfo.getBiaocheType() == 5 && !roleLoadInfo.isNotice()){
				String content = Resource.getMessage("game", "BIAO_CHE_VIP_BUY") + "," + roleInfo.getRoleName();
				GmccMgtService.sendChatMessage(content);
				roleLoadInfo.setNotice(true);
			}
			
			if(roleLoadInfo.getBiaocheType() == 4 && !roleLoadInfo.isNotice()){
				String content = Resource.getMessage("game", "BIAO_CHE_VIP_BUY2") + "," + roleInfo.getRoleName();
				GmccMgtService.sendChatMessage(content);
				roleLoadInfo.setNotice(true);
			}
			
			SceneService1.brocastRolePointStatus(pointInfo,roleInfo);
			
			if(pointInfo.getBiaocheOtherRoleId() != 0){
				RoleInfo hubiaoRoleInfo = RoleInfoMap.getRoleInfo(pointInfo.getBiaocheOtherRoleId());
				
				if(hubiaoRoleInfo != null){
					// 之前是否在地图上
					boolean isBeforeHasPoint = MapRoleInfoMap.getMapPointInfo(hubiaoRoleInfo.getId()) == null ? false : true;
					QueryMePointResp queryMePointResp = outCity(hubiaoRoleInfo.getId(), 1, roleId);
					
					// 不在地图上，才需要刷
					if (!isBeforeHasPoint) {
						Message message = new Message();
						GameMessageHead header = new GameMessageHead();
						header.setMsgType(Command.SCREEN_ME_POSITION_RESP);
						header.setUserID0((int)hubiaoRoleInfo.getId());
						message.setHeader(header);
						message.setBody(queryMePointResp);
						SceneRefreService.sendRoleRefreshMsg((int)hubiaoRoleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
					}
					
					MapRolePointInfo hubiaoPointInfo = MapRoleInfoMap.getMapPointInfo(hubiaoRoleInfo.getId());
					
					if (hubiaoPointInfo != null) {
						SceneService1.brocastRolePointStatus(hubiaoPointInfo,hubiaoRoleInfo);
					}
					
					BiaocheStartResp resp1 = new BiaocheStartResp();
					resp1.setBiaocheType(roleLoadInfo.getBiaocheType());
					resp1.setResult(1);
					resp1.setIsHubiaoRole((byte) 1);
					resp1.setOtherRoleId(roleId);
					resp.setOtherRoleId(hubiaoRoleInfo.getId());
					//提示好友接受护送押镖
					Message message = new Message();
					GameMessageHead header = new GameMessageHead();
					header.setMsgType(Command.BIAO_CHE_START_RESP);
					header.setUserID0(hubiaoRoleInfo.getId());
					message.setHeader(header);
					message.setBody(resp1);
					SceneRefreService.sendRoleRefreshMsg(hubiaoRoleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE, message);

					//附近改变的玩家
					if(hubiaoPointInfo != null){
						ViewChangeInfo viewChangeInfo = SceneService1.checkAreaChange(hubiaoPointInfo.getInOtherRoleScreen(), pointInfo.getInOtherRoleScreen());
						// 以前的观众中消失了的那些人，刷一下
						if(viewChangeInfo.getDelRoleIds() != null){
							for(int inOtherRoleId : viewChangeInfo.getDelRoleIds()){
								MapRolePointInfo audienceInfo = MapRoleInfoMap.getMapPointInfo(inOtherRoleId);
								
								if (audienceInfo != null) {
									ScreenMoveReq screenMoveReq = new ScreenMoveReq();
									screenMoveReq.setPointx(audienceInfo.getScreenPointX());
									screenMoveReq.setPointz(audienceInfo.getScreenPointZ());
									ScreenMoveResp screenMoveResp = screenMove(inOtherRoleId, screenMoveReq);
									
									Message message1 = new Message();
									GameMessageHead header1 = new GameMessageHead();
									header1.setMsgType(Command.SCREEN_MOVE_RESP);
									header1.setUserID0(inOtherRoleId);
									message1.setHeader(header1);
									message1.setBody(screenMoveResp);
									SceneRefreService.sendRoleRefreshMsg(inOtherRoleId,SceneRefreService.REFRESH_TYPE_SCENE,message1);
								}
							}
						}
					}
				}
				
				// 让押镖人的观众刷一下屏幕
				for(int inOtherRoleId : pointInfo.getInOtherRoleScreen().keySet()){
					if(pointInfo.getBiaocheOtherRoleId() == inOtherRoleId){
						continue;
					}
					MapRolePointInfo audienceInfo = MapRoleInfoMap.getMapPointInfo(inOtherRoleId);
					
					if (audienceInfo != null) {
						ScreenMoveReq screenMoveReq = new ScreenMoveReq();
						screenMoveReq.setPointx((int) audienceInfo.getScreenPointX());
						screenMoveReq.setPointz((int) audienceInfo.getScreenPointZ());
						ScreenMoveResp screenMoveResp = screenMove(inOtherRoleId, screenMoveReq);
						
						Message message = new Message();
						GameMessageHead header = new GameMessageHead();
						header.setMsgType(Command.SCREEN_MOVE_RESP);
						header.setUserID0(inOtherRoleId);
						message.setHeader(header);
						message.setBody(screenMoveResp);
						SceneRefreService.sendRoleRefreshMsg(inOtherRoleId,SceneRefreService.REFRESH_TYPE_SCENE,message);
					}
				}
				
			}
		}
		
		return resp;
	}
	
	/**
	 * 请求好友护送押镖
	 * @param roleId
	 * @param req
	 */
	public BiaocheHelpResp biaocheHelp(int roleId,BiaocheHelpReq req)
	{
		BiaocheHelpResp resp = new BiaocheHelpResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_29);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_29);
			return resp;
		}
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null)
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_29);
			return resp;
		}
		
		synchronized(GameFlag.OBJ_YABIAO_HELP)
		{
			if(roleLoadInfo.getTodayYabiaoNum() >= getTodayFreeYaBiaoNum(roleInfo))
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_36);
				return resp;
			}
			
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if (pointInfo == null) 
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_29);
				return resp;
			}
			
			RoleInfo otherRole = RoleInfoMap.getRoleInfo(req.getHelpRoleId());
			if(otherRole == null)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_30);
				return resp;
			}
			
			MapRolePointInfo helpPointInfo = MapRoleInfoMap.getMapPointInfo(req.getHelpRoleId());
			if (helpPointInfo != null && (helpPointInfo.getStatus() != 0 && helpPointInfo.getStatus() != 2 && helpPointInfo.getStatus() != 3)) 
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_34);
				return resp;
			}
			
			RoleLoadInfo otherRoleLoadInfo = otherRole.getRoleLoadInfo();
			if(otherRoleLoadInfo == null)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_30);
				return resp;
			}

			// 将台，3v3,长坂坡，在队伍中的人，不能被拉镖的人通过好友邀请出城，
			if(otherRoleLoadInfo.getLeaderRoleId() != 0 || otherRoleLoadInfo.getMember1RoleId() != 0 || otherRoleLoadInfo.getMember2RoleId() != 0){
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_34);
				return resp;
			}
			
			// 距离上次该好友拒绝CD
			HashMap<Integer, Long> rejectMap = otherRoleLoadInfo.getRejectHelpMap(true);
			if(rejectMap != null && rejectMap.containsKey(roleId) && 
					(System.currentTimeMillis() - rejectMap.get(roleId)) < GameValue.PER_HELP_HU_BIAO_TIME*1000)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_35);
				return resp;
			}
			
			
			HeroInfo otherHeroInfo = HeroInfoMap.getMainHeroInfo(otherRole);
			if(otherHeroInfo == null)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_30);
				return resp;
			}
			
			// 防止好友未出新手引导
			if(otherHeroInfo.getHeroLevel() < 5)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_30);
				return resp;
			}
			
			// 好友是否空闲
			if((otherRole.getRoleLoadInfo() != null && otherRole.getRoleLoadInfo().getInFight() != 0) 
					|| RoleService.checkRoleInFight(otherRole))
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_34);
				return resp;
			}
			
			// 是否是好友
			Set<Integer> friendSet = RoleFriendMap.getRoleFriendIdSet(roleId);
			if(friendSet == null || !friendSet.contains(req.getHelpRoleId()))
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_33);
				return resp;
			}
			
			// 好友是否在线
			if(otherRole.getLoginStatus() == 0)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_31);
				return resp;
			}
			
			YabiaoPrizeXMLInfo biaochePrize = YabiaoPrizeXMLMap.getPlayXMLInfo(roleLoadInfo.getBiaocheType());
			if(biaochePrize == null)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_32);
				return resp;
			}
			
			// 押镖奖励
			int prize = biaochePrize.getBaseMoney() + (heroInfo.getHeroLevel() -1)*biaochePrize.getAddMoney();
			if(SceneService1.yabiaoDobleTime())
			{
				prize *= GameValue.BIAO_CHE_TIME_PRIZE_RATE;
			}
			// 护镖奖励
			int friendPrize = (int)(prize * GameValue.BIAO_CHE_HU_SONG_PRIZE_RATE);
			
			
			BiaocheHelpNotifyResp resp1 = new BiaocheHelpNotifyResp();
			resp1.setResult(1);
			resp1.setFriendRoleId(roleId);
			resp1.setFriendRoleName(roleInfo.getRoleName());
			if(otherRoleLoadInfo.getHubiaoNum() < GameValue.BIAO_CHE_HELP_PRIZE_NUM)
			{
				// 护镖奖励
				if(roleLoadInfo.getThisbiaocheTypeJieBiaoNum() >= 0)
				{
					friendPrize =(int)(friendPrize * (1-roleLoadInfo.getThisbiaocheTypeJieBiaoNum() * GameValue.BIAO_CHE_HU_SONG_PRIZE_LOST_RATE));
				}
				
				resp1.setGetSilver(friendPrize);
			}
			
			pointInfo.getHelpList().add(req.getHelpRoleId());
			pointInfo.setWaitingHelp(true);
			
			//提示好友接受护送押镖
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setMsgType(Command.YA_BIAO_HELP_NOFITY_RESP);
			header.setUserID0(otherRole.getId());
			message.setHeader(header);
			message.setBody(resp1);
			SceneRefreService.sendRoleRefreshMsg(otherRole.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
			
		}
		resp.setResult(1);
		return resp;
	}
	
	/**
	 * 处理好友压镖护送请求
	 * @param roleId
	 * @param req
	 */
	public BiaocheHelpExecResp BiaocheHelpExecResp(int roleId,BiaocheHelpExecReq req)
	{
		BiaocheHelpExecResp resp = new BiaocheHelpExecResp();
		resp.setResult(1);
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_4);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_4);
			return resp;
		}
		synchronized(GameFlag.OBJ_YABIAO_HELP)
		{
			RoleInfo beHelpRole = RoleInfoMap.getRoleInfo(req.getBeHelpRoleId());
			if(beHelpRole == null)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_5);
				return resp;
			}
			
			BiaocheHelpResp resp1 = new BiaocheHelpResp();
			
			MapRolePointInfo beHelpPointInfo = MapRoleInfoMap.getMapPointInfo(beHelpRole.getId());
			if (beHelpPointInfo == null) 
			{
				resp.setResult(ErrorCode.MAP_NPC_FIGHT_ERROR_1);
				return resp;
			}
			
			if(!beHelpPointInfo.isWaitingHelp()){
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_43);
				return resp;
			}
			
			HeroInfo otherHeroInfo = HeroInfoMap.getMainHeroInfo(beHelpRole);
			if(otherHeroInfo == null)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_5);
				return resp;
			}
			
			if(beHelpPointInfo.getHelpList() == null || !beHelpPointInfo.getHelpList().contains(roleInfo.getId()))
			{
				// 未向本人发送请求
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_7);
				return resp;
			}
			
			// 已经在护送好友
			if(roleLoadInfo.getYabiaoFriendRoleId() > 0)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_6);
				return resp;
			}
			
			if(req.getHelpType() == 1)
			{
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if(heroInfo == null)
				{
					resp.setResult(ErrorCode.BIAO_CHE_ERROR_4);
					return resp;
				}
				
				if(heroInfo.getHeroLevel() < GameValue.BIAO_CHE_ATTEND_LEVEL){
					resp.setResult(ErrorCode.HU_BIAO_CHE_LEVEL_ERROR);
					return resp;
				}
				
				if(beHelpRole.getRoleLoadInfo().getHubiaoRoleId() != 0)
				{
					// 好友有人护送了
					resp.setResult(ErrorCode.BIAO_CHE_ERROR_14);
					return resp;
				}
				
				if(beHelpPointInfo.getStatus() != 0 && beHelpPointInfo.getStatus() != 2 && beHelpPointInfo.getStatus() != 3){
					resp.setResult(ErrorCode.BIAO_CHE_FIGHT_ERROR1);
					return resp;
				}
				
				MapRolePointInfo helpPointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
				if (helpPointInfo != null) 
				{
					if(helpPointInfo.getStatus() != 0 && helpPointInfo.getStatus() != 2 && helpPointInfo.getStatus() != 3){
						resp.setResult(ErrorCode.BIAO_CHE_FIGHT_ERROR1);
						return resp;
					}
				}
				
				// 护送好友押镖
				beHelpRole.getRoleLoadInfo().setHubiaoRoleId(roleId);
				beHelpPointInfo.setBiaocheOtherRoleId(roleId);
				roleLoadInfo.setYabiaoFriendRoleId(beHelpRole.getId());
				
				RoleLoadInfo beRoleLoadInfo = beHelpRole.getRoleLoadInfo();
				
				YabiaoPrizeXMLInfo biaochePrize = YabiaoPrizeXMLMap.getPlayXMLInfo(beRoleLoadInfo.getBiaocheType());
				if(biaochePrize == null)
				{
					resp.setResult(ErrorCode.BIAO_CHE_ERROR_32);
					return resp;
				}
				
				// 押镖奖励
				int prize = biaochePrize.getBaseMoney() + (otherHeroInfo.getHeroLevel() -1)*biaochePrize.getAddMoney();
				if(SceneService1.yabiaoDobleTime())
				{
					prize *= GameValue.BIAO_CHE_TIME_PRIZE_RATE;
				}
				// 护镖奖励
				int friendPrize = (int)(prize * GameValue.BIAO_CHE_HU_SONG_PRIZE_RATE);
				
				if(roleLoadInfo.getHubiaoNum() < GameValue.BIAO_CHE_HELP_PRIZE_NUM)
				{
					// 护镖奖励
					if(roleLoadInfo.getThisbiaocheTypeJieBiaoNum() >= 0)
					{
						friendPrize =(int)(friendPrize * (1-beRoleLoadInfo.getThisbiaocheTypeJieBiaoNum() * GameValue.BIAO_CHE_HU_SONG_PRIZE_LOST_RATE));
					}
					resp1.setGetSilver(friendPrize);
				}
				
				resp.setHelpRoleId(req.getBeHelpRoleId());
			}
			else
			{
				// 拒绝护送好友押镖
				beHelpPointInfo.getHelpList().remove(Integer.valueOf(roleId));
				roleLoadInfo.getRejectHelpMap(false).put(beHelpRole.getId(), System.currentTimeMillis());
			}
			
			resp1.setResult(1);
			resp1.setFriendRoleId(roleId);
			resp1.setFriendRoleName(roleInfo.getRoleName());
			resp1.setHelpType(req.getHelpType());
			
			//提示好友是否护送压镖
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setMsgType(Command.BIAO_CHE_HELP_RESP);
			header.setUserID0((int)beHelpRole.getId());
			message.setHeader(header);
			message.setBody(resp1);
			SceneRefreService.sendRoleRefreshMsg(beHelpRole.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
		}
		
		
		return resp;
	}
	
	/**
	 * 压镖结束
	 * @param roleId
	 * @return
	 */
	public BiaocheEndResp biaocheEnd(int roleId)
	{
		BiaocheEndResp resp = new BiaocheEndResp();
		resp.setResult(1);
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_9);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_9);
			return resp;
		}
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null)
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_9);
			return resp;
		}
		MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
		if (pointInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_10);
			return resp;
		}
		
		YabiaoPrizeXMLInfo biaochePrize = YabiaoPrizeXMLMap.getPlayXMLInfo(roleLoadInfo.getBiaocheType());
		if(biaochePrize == null)
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_17);
			return resp;
		}
		
		// 押镖奖励
		int prize = biaochePrize.getBaseMoney() + (heroInfo.getHeroLevel() -1)*biaochePrize.getAddMoney();
		int lostPrize=0;
		if(SceneService1.yabiaoDobleTime())
		{
			prize *= GameValue.BIAO_CHE_TIME_PRIZE_RATE;
		}
		// 护镖奖励
		int friendPrize = (int)(prize * GameValue.BIAO_CHE_HU_SONG_PRIZE_RATE);
		
		if(roleLoadInfo.getThisbiaocheTypeJieBiaoNum() >= 0)
		{
			prize = (int)(prize * (1-roleLoadInfo.getThisbiaocheTypeJieBiaoNum() * GameValue.BIAO_CHE_LAN_JIE_LOST_RATE));
			lostPrize = (int)(prize * roleLoadInfo.getThisbiaocheTypeJieBiaoNum() * GameValue.BIAO_CHE_LAN_JIE_LOST_RATE);
			friendPrize =(int)(friendPrize * (1-roleLoadInfo.getThisbiaocheTypeJieBiaoNum() * GameValue.BIAO_CHE_HU_SONG_PRIZE_LOST_RATE));
		}
		
		int hubiaoRoleId = 0;
		synchronized(roleInfo)
		{
			if (RoleService.addRoleRoleResource(ActionType.action380.getType(), roleInfo,
					ConditionType.TYPE_MONEY, prize,null)) {
				resp.setSilverNum(prize);
				resp.setRoleName(roleInfo.getRoleName());
				
				boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action380.getType(), null, true, true);
				boolean isRedPoint = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, RedPointMgtService.LISTENING_GOLD_BUY);
				//红点推送
				if(isRedPointQuest || isRedPoint){
					RedPointMgtService.pop(roleInfo.getId());
				}
				
				GameLogService.insertBiaocheLog(roleInfo, ActionType.action380.getType(), roleLoadInfo.getTodayYabiaoNum(), prize,lostPrize,0,(byte)roleLoadInfo.getBiaocheType(),1);
				
			} else {
				logger.error("----yabiao end add resource error,roleId="+roleId+",prize="+prize);
			}
			hubiaoRoleId = roleLoadInfo.getHubiaoRoleId();
			
			roleInfo.getMapPvpFightTime().setTime(0);
			pointInfo.setStatus((byte)0);
			
			if(!roleDAO.updateYabiaoNum_biaocheType_ThisBiaocheTypeJiebiaoNum(roleId, (byte)1, (byte) 0, (byte) (roleLoadInfo.getTodayYabiaoNum()+1))){
				logger.error("----update yabiao num error,roleId="+roleId+",todayYabiaoNum="+(roleLoadInfo.getTodayYabiaoNum()+1));
			}
			roleLoadInfo.setTodayYabiaoNum((byte) (roleLoadInfo.getTodayYabiaoNum()+1));
			roleLoadInfo.setBiaocheType((byte)1);
			roleLoadInfo.setNotice(false);
			roleLoadInfo.setThisbiaocheTypeJieBiaoNum((byte) 0);
		}
		// 有好友护送
		if(hubiaoRoleId > 0)
		{
			while(true)
			{
				RoleInfo helpRoleInfo = RoleInfoMap.getRoleInfo(hubiaoRoleId);
				if (helpRoleInfo == null) 
				{
					logger.error("11----hubiaoRoleId error,hubiaoRoleId="+hubiaoRoleId);
					break;
				}
				RoleLoadInfo helpRoleLoadInfo = helpRoleInfo.getRoleLoadInfo();
				
				if (helpRoleLoadInfo == null) 
				{
					logger.error("22----hubiaoRoleId error,hubiaoRoleId="+hubiaoRoleId);
					break;
				}
				BiaocheEndResp resp1 = new BiaocheEndResp();
				
				// 有护送奖励
				synchronized(helpRoleInfo)
				{
					//结束护送
					helpRoleLoadInfo.setYabiaoFriendRoleId(0);
					
					if(!roleDAO.updateHubiaoNum(roleId, (byte) (helpRoleLoadInfo.getHubiaoNum()+1))){
						logger.error("33----update hubiaoRole hubiaoNum error,hubiaoRoleId="+hubiaoRoleId+",num="+(helpRoleLoadInfo.getHubiaoNum()+1));
						break;
					}
					helpRoleLoadInfo.setHubiaoNum((byte) (helpRoleLoadInfo.getHubiaoNum()+1));
					
					if(friendPrize > 0 && helpRoleLoadInfo.getHubiaoNum() <= GameValue.BIAO_CHE_HELP_PRIZE_NUM)
					{
						if (RoleService.addRoleRoleResource(ActionType.action381.getType(), helpRoleInfo,
								ConditionType.TYPE_MONEY, friendPrize,null)) {
							
							resp1.setSilverNum(friendPrize);
							GameLogService.insertBiaocheLog(helpRoleInfo, ActionType.action381.getType(), helpRoleLoadInfo.getHubiaoNum(), friendPrize,0,0,(byte)roleLoadInfo.getBiaocheType(),1);
							
						} else {
							logger.error("44----add hubiao role resource error,hubiaoRoleId="+hubiaoRoleId+",friendPrize="+friendPrize+",biaoche Type="+roleLoadInfo.getBiaocheType());
							break;
						}
					}
					resp1.setRoleName(roleInfo.getRoleName());
					
					pointInfo.setBiaocheOtherRoleId(0);
					MapRolePointInfo helpPointInfo = MapRoleInfoMap.getMapPointInfo(hubiaoRoleId);
					
					if (helpPointInfo != null) {
						helpPointInfo.setBiaocheOtherRoleId(0);
						helpPointInfo.setStatus((byte) 0);
					}
					roleLoadInfo.setHubiaoRoleId(0);
					SceneService1.brocastRolePointStatus(pointInfo,roleInfo);
				}
				resp1.setResult(1);
				
				//提示护送好友,护镖结束
				Message message = new Message();
				GameMessageHead header = new GameMessageHead();
				header.setMsgType(Command.BIAO_CHE_END_RESP);
				header.setUserID0(helpRoleInfo.getId());
				message.setHeader(header);
				message.setBody(resp1);
				SceneRefreService.sendRoleRefreshMsg(helpRoleInfo.getId(),SceneRefreService.REFRESH_TYPE_SCENE,message);
				
				//红点检测
				boolean isRedPointQuest = QuestService.checkQuest(helpRoleInfo, ActionType.action381.getType(), null, true, false);
				boolean isRedPoint = RedPointMgtService.check2PopRedPoint(helpRoleInfo.getId(), null, false, RedPointMgtService.LISTENING_GOLD_BUY);
				//红点推送
				if(isRedPointQuest || isRedPoint){
					RedPointMgtService.pop(roleInfo.getId());
				}
				
				break;
			}
		}
		
		SceneService1.brocastRolePointStatus(pointInfo,roleInfo);
		
		return resp;
	}
	
	/**
	 * 查询镖车NPC
	 * @param roleId
	 * @return
	 */
	public BiaocheQueryResp queryBiaocheNPC(int roleId)
	{
		BiaocheQueryResp resp = new BiaocheQueryResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_15);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_15);
			return resp;
		}
		synchronized(roleInfo)
		{
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if (pointInfo == null) 
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_16);
				return resp;
			}
			
			//是否是新的一天,将押镖数据重置
			if(roleLoadInfo.getBiaocheQueryTime() == null || !DateUtil.isSameDay(System.currentTimeMillis(), roleLoadInfo.getBiaocheQueryTime().getTime()))
			{
				if(!RoleDAO.getInstance().resetAllBiaocheNum(roleInfo.getId()))
				{
					resp.setResult(ErrorCode.BIAO_CHE_ERROR_44);
					return resp;
				}
				roleLoadInfo.setRefBiaoCheNum((byte) 0);
				roleLoadInfo.setTodayJiebiaoNum((byte) 0);
				roleLoadInfo.setTodayYabiaoNum((byte) 0);
				roleLoadInfo.setHubiaoNum((byte)0);
				roleLoadInfo.setBiaocheQueryTime(new Timestamp(System.currentTimeMillis()));
			}
			
			resp.setResult(1);
			resp.setBiaocheRefNum((byte) ((roleLoadInfo.getRefBiaoCheNum()-GameValue.BIAO_CHE_REF_FREE_NUM) > 0 ? (roleLoadInfo.getRefBiaoCheNum()-GameValue.BIAO_CHE_REF_FREE_NUM) : 0));
			resp.setBiaocheFreeRefNum((byte) ((GameValue.BIAO_CHE_REF_FREE_NUM - roleLoadInfo.getRefBiaoCheNum()) >=0 ? (GameValue.BIAO_CHE_REF_FREE_NUM - roleLoadInfo.getRefBiaoCheNum()):0));
			resp.setBiaocheType(roleLoadInfo.getBiaocheType());
			resp.setLeftJiebiaoNum((byte)(GameValue.TODAY_JIE_BIAO_NUM - roleLoadInfo.getTodayJiebiaoNum()));
			resp.setLeftFreeYabiaoNum((byte) ((getTodayFreeYaBiaoNum(roleInfo) - roleLoadInfo.getTodayYabiaoNum()) >=0 ? (getTodayFreeYaBiaoNum(roleInfo) - roleLoadInfo.getTodayYabiaoNum()):0));
		}
		return resp;
	}
	
	/**
	 * 查询地图他人镖车
	 * @param roleId
	 * @param req
	 * @return
	 */
	public BiaocheQueryOtherResp queryOtherBiaoche(int roleId,BiaocheQueryOtherReq req)
	{
		BiaocheQueryOtherResp resp = new BiaocheQueryOtherResp();
		resp.setResult(1);
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_21);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

		if (roleLoadInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_21);
			return resp;
		}
		MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
		if (pointInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_22);
			return resp;
		}
		
		RoleInfo otherRoleInfo = RoleInfoMap.getRoleInfo(req.getOtherRoleId());
		if(otherRoleInfo == null)
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_23);
			return resp;
		}
		
		RoleLoadInfo otherRoleLoadInfo = otherRoleInfo.getRoleLoadInfo();

		if (otherRoleLoadInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_21);
			return resp;
		}
		
		MapRolePointInfo otherPointInfo = MapRoleInfoMap.getMapPointInfo(otherRoleInfo.getId());
		if (otherPointInfo == null) 
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_24);
			return resp;
		}
		
		YabiaoPrizeXMLInfo biaochePrize = YabiaoPrizeXMLMap.getPlayXMLInfo(otherRoleLoadInfo.getBiaocheType());
		if(biaochePrize == null)
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_27);
			return resp;
		}
		
		HeroInfo otherHeroInfo = HeroInfoMap.getMainHeroInfo(otherRoleInfo);
		if(otherHeroInfo == null)
		{
			resp.setResult(ErrorCode.BIAO_CHE_ERROR_28);
			return resp;
		}
		
		// 有护送好友
		if(otherRoleLoadInfo.getHubiaoRoleId() > 0)
		{
			RoleInfo helpRoleInfo = RoleInfoMap.getRoleInfo(otherRoleLoadInfo.getHubiaoRoleId());
			if(helpRoleInfo == null)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_25);
				return resp;
			}
			
			HeroInfo helpHeroInfo = HeroInfoMap.getMainHeroInfo(helpRoleInfo);
			if(helpHeroInfo == null)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_25);
				return resp;
			}
			
			resp.setHelpRoleId(helpRoleInfo.getId());
			resp.setHelpRoleName(helpRoleInfo.getRoleName());
			resp.setHelpRoleFightValue(helpRoleInfo.getFightValue());
			resp.setHelpRoleLv((short) helpHeroInfo.getHeroLevel());
		}
		
		resp.setOtherRoleId(otherRoleInfo.getId());
		resp.setOtherRoleName(otherRoleInfo.getRoleName());
		resp.setOtherRoleBiaocheType(otherRoleLoadInfo.getBiaocheType());
		resp.setOtherRoleFightValue(otherRoleInfo.getFightValue());
		resp.setOtherRoleLeftJiebiaoNum((byte) ((GameValue.PER_BIAO_CHE_LAN_JIE_NUM - otherRoleLoadInfo.getThisbiaocheTypeJieBiaoNum()) > 0 ? (GameValue.PER_BIAO_CHE_LAN_JIE_NUM - otherRoleLoadInfo.getThisbiaocheTypeJieBiaoNum()) : 0));
		resp.setOtherHeroLv((short) otherHeroInfo.getHeroLevel());
		resp.setOtherRoleHeroNo(otherHeroInfo.getHeroNo());
		int prize = biaochePrize.getBaseMoney() + (otherHeroInfo.getHeroLevel() -1)*biaochePrize.getAddMoney();
		if(SceneService1.yabiaoDobleTime())
		{
			prize *= GameValue.BIAO_CHE_TIME_PRIZE_RATE;
		}
		resp.setGetSilver((int) (prize*GameValue.BIAO_CHE_LAN_JIE_LOST_RATE));
		
		return resp;
	}

	/**
	 * 获取 MapRolePointRe
	 * @param roleInfo
	 * @param pointInfo
	 * @return
	 */
	private static MapRolePointRe getMapRolePointRe(RoleInfo roleInfo,MapRolePointInfo  pointInfo){
		MapRolePointRe pointRe = new MapRolePointRe();
		pointRe.setRoleId(pointInfo.getRoleId());
		pointRe.setRoleName(pointInfo.getRoleName());
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo != null){
			pointRe.setRoleLv(heroInfo.getHeroLevel());
			pointRe.setMainHeroNo(heroInfo.getHeroNo());
		}
		
		pointRe.setRace((byte)pointInfo.getRace());
		pointRe.setPointX(pointInfo.getPointX());
		pointRe.setPointZ(pointInfo.getPointZ());
		pointRe.setStatus(pointInfo.getStatus());
		pointRe.setBiaocheOtherRoleId(pointInfo.getBiaocheOtherRoleId());
		pointRe.setFightValue(roleInfo.getFightValue());
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo != null) {
			pointRe.setBiaoCheType(roleLoadInfo.getBiaocheType());
		}
		
		pointRe.setClubName(RoleClubInfoMap.getClubName(roleInfo.getClubId()));
		pointRe.setShowPlanId(roleInfo.getIsShowShizhuang());
		pointRe.setEquipNos(SceneService1.getHeroEquipNoforAvater(heroInfo));
		if (roleInfo.getRideInfo() != null) {
			pointRe.setRideNo(roleInfo.getRideInfo().getRideNo());
		}
		
		if(roleInfo.getRoleLoadInfo() != null){
			Map<Integer, Long> map = CommonUtil.String2MapByValueLong(roleInfo.getRoleLoadInfo().getAllAppellation());
			
			TitleService.titlesReset(roleInfo, map);//刷新称号
			
			pointRe.setTitleNo(TitleService.nowTitleCheck(roleInfo, map));
		}
		
		return pointRe;
	}

	/**
	 * VIP直接购买橙色镖车
	 * 
	 * @param roleId
	 * @return
	 */
	public BiaocheVipBuyResp biaocheVipBuy(int roleId) {
		BiaocheVipBuyResp resp = new BiaocheVipBuyResp();
		resp.setResult(1);
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.BIAO_CHE_BUY_ERROR);
			return resp;
		}
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.BIAO_CHE_BUY_ERROR);
			return resp;
		}
		
		VipXMLInfo vipXMLInfo = VipXMLInfoMap.getVipXMLInfo(roleInfo.getVipLv());
		
		if (vipXMLInfo == null || vipXMLInfo.getVipMap().get(VipType.BIAOCHE) == null || vipXMLInfo.getVipMap().get(VipType.BIAOCHE) != 1) {
			resp.setResult(ErrorCode.BIAO_CHE_BUY_VIP_LV_ERROR);
			return resp;
		}
		
		synchronized(roleInfo)
		{
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if (pointInfo == null) 
			{
				resp.setResult(ErrorCode.BIAO_CHE_BUY_NOT_IN_MAP_ERROR);
				return resp;
			}
			
			if(roleLoadInfo.getBiaocheType() == 5)
			{
				resp.setResult(ErrorCode.BIAO_CHE_ERROR_41);
				return resp;
			}
			
			int needCoin = GameValue.BIAO_CHE_BUY_COST_COIN;
			
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new CoinCond((long) needCoin));
			int check = AbstractConditionCheck.checkCondition(roleInfo,conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}
			
			if (RoleService.subRoleResource(ActionType.action390.getType(), roleInfo, conds , null)) {
				String updateSourceStr = RoleService.returnResourceChange(conds);
				if (updateSourceStr != null) {
					String[] sourceStr = updateSourceStr.split(",");
					if (sourceStr != null && sourceStr.length > 1) {
						resp.setSourceType(Byte.valueOf(sourceStr[0]));
						resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
					}
				}
			} else {
				resp.setResult(ErrorCode.BIAO_CHE_BUY_ERROR);
				return resp;
			}
			
			if(!roleDAO.updateRefBiaocheNum_biaocheType_ThisBiaocheTypeJiebiaoNum(roleId, roleLoadInfo.getRefBiaoCheNum(), (byte)5, (byte) 0)){
				resp.setResult(ErrorCode.BIAO_CHE_BUY_ERROR);
				return resp;
			}
			
			roleLoadInfo.setBiaocheType((byte) 5);
			roleLoadInfo.setNotice(false);
			roleLoadInfo.setThisbiaocheTypeJieBiaoNum((byte) 0);
			
			GameLogService.insertBiaocheLog(roleInfo, ActionType.action390.getType(), 0,0,0,needCoin,(byte)5,1);
		}
		return resp;
	}
	
	/**
	 * 获取免费押镖次数
	 * 
	 * @return
	 */
	public static int getTodayFreeYaBiaoNum(RoleInfo roleInfo){
		int freeNum = GameValue.TODAY_FREE_LA_BIAO_NUM;
		
		VipXMLInfo vipXMLInfo = VipXMLInfoMap.getVipXMLInfo(roleInfo.getVipLv());
		
		if (vipXMLInfo != null && vipXMLInfo.getVipMap().get(VipType.YUNBIAO_NUM) != null) {
			freeNum += vipXMLInfo.getVipMap().get(VipType.YUNBIAO_NUM);
		}
		return freeNum;
	}
	
	/**
	 * 扫荡流寇古墓 扫到BOSS直接进入战斗
	 * @param roleId
	 * @param req
	 */
	public SweepMapNpcResp sweepMapNpcNo(int roleId, SweepMapNpcReq req) {
		SweepMapNpcResp resp = new SweepMapNpcResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null || roleLoadInfo.getYabiaoFriendRoleId() > 0) {
				resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_2);
				return resp;
			}
			MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
			if (pointInfo == null) {
				resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_3);
				return resp;
			}
			int mapNpcNo = req.getMapNpcNo();
			MapCityXMLNPC mapNpc = SceneXmlInfoMap.getMapCityXMLNPC(mapNpcNo);
			if (mapNpc == null) {
				resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_4);
				return resp;
			}			
			if (mapNpc.getBattleType() != 9 && mapNpc.getBattleType() != 10) {
				resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_5);
				return resp;
			}
			if (mapNpc.getLevel() > HeroInfoMap.getMainHeroLv(roleInfo.getId())) {
				resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_6);
				return resp;
			}
			if(!roleLoadInfo.getPassMapNpcNos().contains(mapNpcNo)){
				resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_7);
				return resp;
			}
			String gwNo = mapNpc.getGw();
			if (gwNo == null || gwNo.length() <= 0 || GWXMLInfoMap.getNPCGWXMLInfo(gwNo) == null) {
				resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_8);
				return resp;
			}
			// 扣除精力
			List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
			conds.add(new EnergyCond(mapNpc.getCostEng()));
			int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}		
			List<BattlePrize> prize = new ArrayList<BattlePrize>();
			if (!GameValue.BOSS_MAP_NPC_NOS.contains(gwNo)) {
				if (!RoleService.subRoleResource(ActionType.action473.getType(), roleInfo, conds, null)) {
					resp.setResult(ErrorCode.SWEEP_MAP_NPC_ERROR_9);
					return resp;
				}
				String dropBag = "";
				GWXMLInfo xmlInfo = GWXMLInfoMap.getNPCGWXMLInfo(gwNo);
				if (xmlInfo != null) {
					for (String bag : xmlInfo.getDropMap().values()) {
						if (dropBag.length() > 0) {
							dropBag += ",";
						}
						dropBag += bag;
					}
				}
				// 大地图NPC掉落
				List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(dropBag);
				List<DropInfo> mustDrops = new ArrayList<DropInfo>();
				int addExp = HeroInfoMap.getMainHeroLv(roleInfo.getId()) * GameValue.PRE_MAP_NPC_FIGHT_EXP_PRIZE_A
						+ GameValue.PRE_MAP_NPC_FIGHT_EXP_PRIZE_B;
				mustDrops.add(new DropInfo(ConditionType.TYPE_EXP.getName(), addExp));

				check = ItemService.addPrizeForPropBag(ActionType.action473.getType(), roleInfo, list, null, prize,
						null, mustDrops, null, true);
				if (check != 1) {
					resp.setResult(check);
					return resp;
				}
			} else {
				// 扫到BOSS直接进入战斗
				FightInfo fightInfo = FightInfoMap.getFightInfoByRoleId(roleId);
				if(fightInfo != null){
					resp.setResult(ErrorCode.FIGHT_ERROR_6);
					return resp;
				}
				//背包判断
				int itemCheck = ItemService.addItemAndEquipCheck(roleInfo);
				if(itemCheck != 1){
					resp.setResult(ErrorCode.FIGHT_ERROR_4);
					return resp;
				}
				String defendStr = mapNpcNo + "," + mapNpc.getBattleType();
				fightInfo = new FightInfo(FightType.FIGHT_TYPE_7, roleId);
				fightInfo.setDefendStr(defendStr);
				fightInfo.setEndRespDefendStr(defendStr);
				fightInfo.setStartRespDefendStr(gwNo + "," + mapNpc.getBattleType());
				
				pointInfo.setStatus((byte) 1);
				
				int checkFightResult = FightMgtService.sendIntoFight(roleInfo, fightInfo);
				if (checkFightResult != 1) {
					resp.setResult(checkFightResult);
					return resp;
				}
								
				// 大地图上战斗，广播给可见自己的其它人
				SceneService1.brocastRolePointStatus(pointInfo, roleInfo);
				// 进入战斗null
				return null;
			}

			resp.setResult(1);
			resp.setPrize(prize);
			resp.setPrizeNum(prize.size());
			return resp;
		}
	}
}
