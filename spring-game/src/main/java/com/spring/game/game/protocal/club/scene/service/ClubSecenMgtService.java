package com.snail.webgame.game.protocal.club.scene.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snail.webgame.game.cache.ClubSceneInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.club.scene.check.CheckIsInSceneReq;
import com.snail.webgame.game.protocal.club.scene.check.CheckIsInSceneResp;
import com.snail.webgame.game.protocal.club.scene.entity.ClubSceneRoleInfo;
import com.snail.webgame.game.protocal.club.scene.inorout.InOrOutClubSceneReq;
import com.snail.webgame.game.protocal.club.scene.inorout.InOrOutClubSceneResp;
import com.snail.webgame.game.protocal.club.scene.update.UpdateClubScenePointsReq;
import com.snail.webgame.game.protocal.club.service.ClubMgtService;
import com.snail.webgame.game.protocal.club.service.ClubService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.xml.cache.GuildTechXMLInfoMap;
import com.snail.webgame.game.xml.cache.GuildUpgradeXmlInfoMap;
import com.snail.webgame.game.xml.info.GuildTechXMLInfo;
import com.snail.webgame.game.xml.info.GuildUpgradeXmlInfo;

public class ClubSecenMgtService {
	//private static final Logger logger = LoggerFactory.getLogger("logs");
	
	/**
	 * 登录时判断角色上次是否在场景中下线
	 * @param roleId
	 * @param req
	 * @return
	 */
	public CheckIsInSceneResp checkIsInScene(int roleId, CheckIsInSceneReq req) {
		CheckIsInSceneResp resp = new CheckIsInSceneResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
				return resp;
			}
			
			int clubId =  roleInfo.getClubId();
			
			if(clubId <= 0){
				resp.setResult(2);
				return resp;
			}
			
			RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
			if(roleClubInfo == null){
				resp.setResult(2);
				return resp;
			}
			
			RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, roleId);
			
			if(memberInfo == null || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
				resp.setResult(2);
				return resp;
			}
			
			if(memberInfo.getFlag() == 1){
				//之前在场景中下线
				resp.setResult(1);
				return resp;
			}
			
		}
	
		
		return resp;
	}

	/**
	 * 角色进入或者退出场景
	 * @param roleId
	 * @param req
	 * @return
	 */
	public InOrOutClubSceneResp inOrOut(int roleId, InOrOutClubSceneReq req) {
		InOrOutClubSceneResp resp = new InOrOutClubSceneResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
				return resp;
			}
			
			int result = 0;
			
			if((result = ClubMgtService.clubLevelCheck(roleInfo)) != 1){
				resp.setResult(result);
				return resp;
			}
			resp.setFlag(req.getFlag());
			
			if(req.getFlag() == 0){
				// 进入
				inScene(roleInfo, resp);
			}else if(req.getFlag() == 1){
				// 退出
				outScene(roleInfo, req, resp);
			}else{
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_35);
			}
			
		}
		
		
		return resp;
	}

	/**
	 * 退出场景
	 * @param roleInfo
	 * @param resp
	 */
	private void outScene(RoleInfo roleInfo, InOrOutClubSceneReq req, InOrOutClubSceneResp resp) {
		resp.setResult(1);
		
		int clubId = roleInfo.getClubId();
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
		if(roleClubInfo == null){
			return;
		}
		
		RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, roleInfo.getId());

		
		if(memberInfo == null || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			return;
		}
		
//		synchronized(memberInfo){
//			if(RoleClubMemberDao.getInstance().updateClubSceneInfoFlag(roleInfo.getId(), clubId, 0)){
//				//设置为离开场景, 暂时不用
//				memberInfo.setFlag((byte)0);
//			}
//			
//		}
		
		Set<Integer> set = ClubSceneInfoMap.getSceneRoleSet(clubId, memberInfo.getSceneId());
		
		if(set.contains(roleInfo.getId())){
			set.remove(roleInfo.getId());
		}
		
		//通知其他场景内的角色
		ClubSceneService.notifyDelClubSceneRole(roleInfo.getId(), set);
	}

	/**
	 * 进入场景
	 * @param roleInfo
	 * @param resp
	 */
	private void inScene(RoleInfo roleInfo, InOrOutClubSceneResp resp) {
		if(roleInfo.getRolePoint() != null){
			SceneService1.notifyDelAIPoint(roleInfo.getRolePoint());
		}
		
		int clubId = roleInfo.getClubId();
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return;
		}
		
		ClubMgtService.resign(roleClubInfo); // 检查会长是否流失
		
		RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, roleInfo.getId());
		if(memberInfo == null || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_12);
			return;
		}
		
//		synchronized (memberInfo) {
//			if(memberInfo.getFlag() != 1){
//				//暂时不记录是否在场景中
//				if(!RoleClubMemberDao.getInstance().updateClubSceneInfoFlag(roleInfo.getId(), clubId, 1)){
//					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_60);
//					return;
//				}
//				
//				memberInfo.setFlag((byte)1);
//			}
//			
//			if(memberInfo.getPointX() == 0 && memberInfo.getPointY() == 0 && memberInfo.getPointZ() == 0){
//				//TODO设置初始坐标，记得下线后 角色的坐标点入库， 暂时不用
//			}
//		}
		
		ClubSceneInfoMap.addSceneInfo(memberInfo);
		
		Set<Integer> set = ClubSceneInfoMap.getSceneRoleSet(clubId, memberInfo.getSceneId());
		
		if(!set.contains(roleInfo.getId())){
			//防止角色本人丢失
			set.add(roleInfo.getId());
		}
		
		RoleInfo sceneRole = null;
		RoleClubMemberInfo sceneMember = null;
		List<ClubSceneRoleInfo> list = new ArrayList<ClubSceneRoleInfo>();
		for(Integer sceneRoleId : set){
			sceneRole = RoleInfoMap.getRoleInfo(sceneRoleId);
			if(sceneRole == null){
				continue;
			}
			//获取角色本人的基本信息,已经场景内其他角色的信息
			
			if(sceneRole.getClubId() != clubId){
				continue;
			}
			
			//玩家当前处于断开中
			if(sceneRole.getDisconnectPhase() == 1)
			{
				continue;
			}
			
			sceneMember = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, sceneRoleId);
			
			// || sceneMember.getFlag() != 1 剔除
			if(sceneMember == null || sceneMember.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
				continue;
			}
			
			list.add(ClubSceneService.getClubSceneRoleInfo(sceneRole, sceneMember));
			
		}
		
		sceneRole = RoleInfoMap.getRoleInfo(roleClubInfo.getCreateRoleId());
		sceneMember = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, roleClubInfo.getCreateRoleId());
		if(sceneRole != null && sceneMember != null){
			//公会会长每次都要加上去
			list.add(0, ClubSceneService.getClubSceneRoleInfo(sceneRole, sceneMember));
		}
		
		
		resp.setList(list);
		resp.setCount(list.size());
		
		resp.setLevel(roleClubInfo.getLevel());
		resp.setLevelLimit(roleClubInfo.getLevelLimit());
		resp.setBuild(roleClubInfo.getBuild());
		resp.setClubId(roleClubInfo.getId());
		resp.setClubName(roleClubInfo.getClubName());
		resp.setDeclaration(roleClubInfo.getDeclaration());
		resp.setDescription(roleClubInfo.getDescription());
		resp.setImageId(roleClubInfo.getImageId());
		resp.setMemberNum(ClubService.getClubMemberNum(roleClubInfo.getId()));
		GuildUpgradeXmlInfo xmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(roleClubInfo.getLevel());
		
		if(xmlInfo != null){
			resp.setMemberNumLimit(xmlInfo.getMembers());
		}
		
		if(roleClubInfo.getExtendLv() > 0){
			Map<Integer, GuildTechXMLInfo> map = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(2);
			if(map != null){
				GuildTechXMLInfo tXmlInfo = map.get(roleClubInfo.getExtendLv());
				if(tXmlInfo != null){
					resp.setMemberNumLimit(resp.getMemberNumLimit() + tXmlInfo.getAddNum());
				}
			}
		}
		
		resp.setLevel(roleClubInfo.getLevel());
		resp.setLevelLimit(roleClubInfo.getLevelLimit());
		
		resp.setResult(1);
		
		//通知场景内其他角色 增加该角色的进入
		ClubSceneService.notifyAddClubSceneRole(ClubSceneService.getClubSceneRoleInfo(roleInfo, memberInfo), set);
		
	}

	/**
	 * 更新角色坐标
	 * @param roleId
	 * @param req
	 */
	public void updateClubScenePoints(int roleId, UpdateClubScenePointsReq req) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null || roleInfo.getRoleLoadInfo() == null || roleInfo.getLoginStatus() != 1){
			return; 
		}
		
		if(req.getPointX() <= 0 || req.getPointY() <= 0 || req.getPointZ() <= 0){
			return;
		}
		
		if(!ClubService.tryLock(roleInfo)){
			return;
		}
		
		try{
			int clubId = roleInfo.getClubId();
			
			RoleClubInfo clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
			if(clubInfo == null){
				return;
			}
			
			RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, roleId);
			
			if(memberInfo == null){
				return;
			}
			
			synchronized(memberInfo){
				if(memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER || memberInfo.getFlag() != 1){
					return;
				}
				
				memberInfo.setPointX(req.getPointX());
				memberInfo.setPointY(req.getPointY());
				memberInfo.setPointZ(req.getPointZ());
			}
			
		}finally{
			ClubService.unLock(roleInfo);
		}
	}
}
