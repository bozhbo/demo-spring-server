package com.snail.webgame.game.protocal.club.scene.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.snail.webgame.game.cache.ClubSceneInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.club.scene.entity.ClubSceneRoleInfo;
import com.snail.webgame.game.protocal.club.scene.entity.ClubSceneRoleInfoResp;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;

public class ClubSceneService {
	
	/**
	 * 通知移除场景中的角色
	 * @param removeRoleId
	 * @param roleIdSet
	 */
	public static void notifyDelClubSceneRole(int removeRoleId, Set<Integer> roleIdSet){
		if(roleIdSet == null || roleIdSet.size() <= 0){
			return;
		}
		
		RoleInfo roleInfo = null;
		RoleClubInfo clubInfo = null;
		RoleClubMemberInfo memberInfo = null;
		ClubSceneRoleInfoResp resp = null;
		
		for(Integer roleId : roleIdSet){
			roleInfo = RoleInfoMap.getRoleInfo(roleId);
			if(roleInfo == null){
				continue;
			}
			
			//玩家当前处于断开中
			if(roleInfo.getDisconnectPhase() == 1)
			{
				continue;
			}
			
			clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
			if(clubInfo == null){
				continue;
			}
			
			memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleInfo.getClubId(), roleId);
			if(memberInfo == null){
				continue;
			}
			
			resp = new ClubSceneRoleInfoResp();
			resp.setResult(1);
			resp.setClubId(clubInfo.getId());
			resp.setRoleId(removeRoleId);
			resp.setFlag(0);
			resp.setList(new ArrayList<ClubSceneRoleInfo>());
			resp.setCount(0);
			
			SceneService.sendRoleRefreshMsg(resp, roleId, Command.CLUB_SCENE_ROLE_CHANGE_RESP);
			
		}
		
	}
	
	/**
	 * 获取场景对象
	 * @param roleInfo
	 * @param memberInfo
	 * @return
	 */
	public static ClubSceneRoleInfo getClubSceneRoleInfo(RoleInfo roleInfo, RoleClubMemberInfo memberInfo){
		ClubSceneRoleInfo info = new ClubSceneRoleInfo();
		
		info.setPointX(memberInfo.getPointX());
		info.setPointY(memberInfo.getPointY());
		info.setPointZ(memberInfo.getPointZ());
		info.setRoleId(roleInfo.getId());
		info.setRoleName(roleInfo.getRoleName());
		info.setPosition(memberInfo.getStatus());
		info.setShowPlanId(roleInfo.getIsShowShizhuang());
		info.setTitleNo(TitleService.getNowTitle(roleInfo));
		
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo == null){
			return info;
		}
		info.setHeroNo(heroInfo.getHeroNo());
		
		info.setEquipNos(SceneService1.getHeroEquipNoforAvater(heroInfo));		
		return info;
	}
	
	/**
	 * 增加场景内角色
	 * @param info
	 * @param roleIdSet
	 */
	public static void notifyAddClubSceneRole(ClubSceneRoleInfo info, Set<Integer> roleIdSet){
		if(info == null){
			return;
		}

		RoleInfo roleInfo = null;
		RoleClubInfo clubInfo = null;
		RoleClubMemberInfo memberInfo = null;
		ClubSceneRoleInfoResp resp = null;
		
		List<ClubSceneRoleInfo> list = new ArrayList<ClubSceneRoleInfo>();
		list.add(info);
		
		for(Integer roleId : roleIdSet){
			if(roleId == info.getRoleId()){
				continue;
			}
			
			roleInfo = RoleInfoMap.getRoleInfo(roleId);
			if(roleInfo == null){
				continue;
			}
			
			//玩家当前处于断开中
			if(roleInfo.getDisconnectPhase() == 1)
			{
				continue;
			}
			
			clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
			if(clubInfo == null){
				continue;
			}
			
			memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleInfo.getClubId(), roleId);
			if(memberInfo == null){
				continue;
			}
			
			resp = new ClubSceneRoleInfoResp();
			resp.setResult(1);
			resp.setClubId(clubInfo.getId());
			resp.setRoleId(roleId);
			resp.setFlag(1);
			resp.setList(list);
			resp.setCount(list.size());
			
			SceneService.sendRoleRefreshMsg(resp, roleId, Command.CLUB_SCENE_ROLE_CHANGE_RESP);
			
		}
		
	}
	
	/**
	 * 角色下线后 保存在公会场景中的坐标点
	 * @param roleInfo
	 */
	public static void savePoints(RoleInfo roleInfo){
		if(roleInfo == null){
			return;
		}
		
		RoleClubInfo clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		
		if(clubInfo == null){
			return;
		}
		
		RoleClubMemberInfo info = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleInfo.getClubId(), roleInfo.getId());
		
		if(info == null || info.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			return;
		}
		
//		synchronized(info){
//			RoleClubMemberDao.getInstance().updateClubMemberInfoPoints(info);
//		}
		
		
		Set<Integer> set = ClubSceneInfoMap.getSceneRoleSet(roleInfo.getId(), info.getSceneId());
		
		if(set.contains(roleInfo.getId())){
			//还在场景中，移除
			set.remove(roleInfo.getId());
			notifyDelClubSceneRole(roleInfo.getId(), set);
		}
		
		
	}
}
