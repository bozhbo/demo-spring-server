package com.snail.webgame.game.protocal.club.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.ClubSceneInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.WordListMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameFlag;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.common.util.EmojiFilterUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.condtion.conds.MoneyCond;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.core.VoiceGameService;
import com.snail.webgame.game.dao.RoleClubEventDao;
import com.snail.webgame.game.dao.RoleClubInfoDao;
import com.snail.webgame.game.dao.RoleClubMemberDao;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.ClubEventInfo;
import com.snail.webgame.game.info.ClubSceneInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.club.appoint.ClubAppointReq;
import com.snail.webgame.game.protocal.club.appoint.ClubAppointResp;
import com.snail.webgame.game.protocal.club.build.ClubBuildReq;
import com.snail.webgame.game.protocal.club.build.ClubBuildResp;
import com.snail.webgame.game.protocal.club.create.CreateClubReq;
import com.snail.webgame.game.protocal.club.create.CreateClubResp;
import com.snail.webgame.game.protocal.club.entity.ClubInfoRe;
import com.snail.webgame.game.protocal.club.entity.ClubRequestInfoRe;
import com.snail.webgame.game.protocal.club.entity.RoomIdMsgResp;
import com.snail.webgame.game.protocal.club.info.GetClubInfoReq;
import com.snail.webgame.game.protocal.club.info.GetClubInfoResp;
import com.snail.webgame.game.protocal.club.join.JoinClubReq;
import com.snail.webgame.game.protocal.club.join.JoinClubResp;
import com.snail.webgame.game.protocal.club.msg.ClubEveInfoMsgResp;
import com.snail.webgame.game.protocal.club.msg.ClubJoinRequestInfoMsgResp;
import com.snail.webgame.game.protocal.club.msg.ClubRoleMemberInfoMsgResp;
import com.snail.webgame.game.protocal.club.operation.OperationClubReq;
import com.snail.webgame.game.protocal.club.operation.OperationClubResp;
import com.snail.webgame.game.protocal.club.scene.entity.ClubSceneRoleInfo;
import com.snail.webgame.game.protocal.club.scene.entity.ClubSceneRoleInfoResp;
import com.snail.webgame.game.protocal.club.scene.entity.GetOutClubSceneResp;
import com.snail.webgame.game.protocal.club.scene.service.ClubSceneService;
import com.snail.webgame.game.protocal.club.search.SearchClubReq;
import com.snail.webgame.game.protocal.club.search.SearchClubResp;
import com.snail.webgame.game.protocal.club.tech.info.GetClubTechInfoReq;
import com.snail.webgame.game.protocal.club.tech.info.GetClubTechInfoResp;
import com.snail.webgame.game.protocal.club.tech.upgrade.ClubTechUpgradeReq;
import com.snail.webgame.game.protocal.club.tech.upgrade.ClubTechUpgradeResp;
import com.snail.webgame.game.protocal.club.update.UpdateClubInfoReq;
import com.snail.webgame.game.protocal.club.update.UpdateClubInfoResp;
import com.snail.webgame.game.protocal.club.upgrade.ClubUpgradeReq;
import com.snail.webgame.game.protocal.club.upgrade.ClubUpgradeResp;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.xml.cache.GuildConstructionXmlInfoMap;
import com.snail.webgame.game.xml.cache.GuildTechXMLInfoMap;
import com.snail.webgame.game.xml.cache.GuildUpgradeXmlInfoMap;
import com.snail.webgame.game.xml.info.GuildConstructionXmlInfo;
import com.snail.webgame.game.xml.info.GuildTechXMLInfo;
import com.snail.webgame.game.xml.info.GuildUpgradeXmlInfo;

public class ClubMgtService {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 创建公会
	 * @param roleId
	 * @param req
	 * @return
	 */
	public CreateClubResp createClub(int roleId, CreateClubReq req) {
		CreateClubResp resp = new CreateClubResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		int result = 0;
		
		if((result = clubLevelCheck(roleInfo)) != 1){
			resp.setResult(result);
			return resp;
		}
		
		List<AbstractConditionCheck> conds = new ArrayList <AbstractConditionCheck>();
		conds.add(new CoinCond(GameValue.CREATE_CLUB_COST_GOLD));

		int check = AbstractConditionCheck.checkCondition(roleInfo, conds);

		if (check != 1)
		{
			resp.setResult(check);
			return resp;
		}
		
		if (!ClubService.tryLock(roleInfo)) {
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_6);
			return resp;
		}
		
		try{
			
			RoleClubInfo info = null;
			
			synchronized(GameFlag.ROLE_CLUB){
				
				String clubName = req.getClubName();
				int imageId = req.getImageId();
				
				if(clubName != null){
					clubName = clubName.trim();
				}
				
				if(!RoleService.checkName(clubName)){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_3);
					return resp;
				}
				
				
				if(RoleClubInfoMap.getClubNameSet().contains(clubName.toUpperCase())){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_34);
					return resp;
				}
				
				info = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
	
				if(info != null && info.getCreateRoleId() == roleId){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_5);
					return resp;
				}
				
				if(roleInfo.getClubId() > 0){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_33);
					return resp;
				}
				
				List<ClubEventInfo> eventList = RoleClubMemberInfoMap.getEventListByRoleId(roleId);
				if(eventList != null && eventList.size() > 0){
					long dayTime = 1000 * 60 * 60 * 24;
					for(ClubEventInfo event : eventList){
						if(event == null){
							continue;
						}
						
						if(event.getEvent() == 3 && System.currentTimeMillis() - event.getTime().getTime() < dayTime){
							//解散公会
							resp.setResult(2);
							resp.setTime(dayTime - (System.currentTimeMillis() - event.getTime().getTime()));
							return resp;
						}
					}
				}
				
				info = new RoleClubInfo();
				info.setClubName(clubName);
				info.setImageId(imageId);
				info.setCreateRoleId(roleId);
				info.setLevel(1);
				info.setFlag(1);
				info.setLevelLimit(GameValue.CREATE_CLUB_LEVEL_LIMIT);
				info.setCreateTime(new Timestamp(System.currentTimeMillis()));
				
				if(!RoleClubInfoDao.getInstance().insertRoleClubInfo(info)){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_6);
					return resp;
				}
				
				RoleClubMemberDao.getInstance().deleteAllRoleMemberInfoByRoleId(roleId); //删除角色所有的其他申请
				
				
				RoleClubMemberInfo to = new RoleClubMemberInfo();
				to.setRoleId(roleId);
				to.setClubId(info.getId());
				to.setJoinTime(new Timestamp(System.currentTimeMillis()));
				to.setStatus(RoleClubMemberInfo.CLUB_BOSS);
				//TODO insert增加 坐标 和FLAG数据
				if(!RoleClubMemberDao.getInstance().insertRoleClubMemberInfo(to)){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_6);
					return resp;
				}
				roleInfo.setClubId(info.getId());
				
				ClubEventInfo clubEventInfo = new ClubEventInfo();
				clubEventInfo.setClubId(info.getId());
				clubEventInfo.setRoleId(roleId);
				clubEventInfo.setTime(to.getJoinTime());
				clubEventInfo.setEvent(-1);
				
				RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
				
	
				RoleClubMemberInfoMap.removeClubRoleByRoleId(roleId);
				RoleClubInfoMap.addRoleClubInfo(info);
				RoleClubMemberInfoMap.addRoleClubMemberInfo(to);
				RoleClubMemberInfoMap.addEvent(clubEventInfo);
				
			}
			
			RoleClubInfo rClubInfo = null;
			RoleInfo leaderRoleInfo = null;
			for(Integer clubId : roleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet()){
				//推送给客户端去掉其他公会的显示
				rClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
				if(rClubInfo == null){
					continue;
				}
				leaderRoleInfo = RoleInfoMap.getRoleInfo(rClubInfo.getCreateRoleId());
				
				if(leaderRoleInfo == null){
					continue;
				}
				
				if(leaderRoleInfo.getLoginStatus() == 1){
					//会在处于登录状态 推送消息
					ClubJoinRequestInfoMsgResp requestResp = new ClubJoinRequestInfoMsgResp();
					ClubRequestInfoRe requestRe = new ClubRequestInfoRe();;
					
					requestRe.setRoleId(roleInfo.getId());
					
					requestResp.getRequestList().add(requestRe);
					requestResp.setRequestCount(requestResp.getRequestList().size());
					requestResp.setResult(2);
					
					RedPointMgtService.check2PopRedPoint(rClubInfo.getCreateRoleId(), null, true, RedPointMgtService.LISTENING_CLUB_TYPE);

					SceneService.sendRoleRefreshMsg(requestResp, leaderRoleInfo.getId(), Command.CLUB_REQUEST_LIST_RESP);
				
				}
				
			}
			
			synchronized(roleInfo){
				if (RoleService.subRoleResource(ActionType.action384.getType(), roleInfo, conds , null)){
					String updateSourceStr = RoleService.returnResourceChange(conds);
					if (updateSourceStr != null)
					{
						String[] sourceStr = updateSourceStr.split(",");
						if (sourceStr != null && sourceStr.length > 1)
						{
							resp.setSourceType(Byte.valueOf(sourceStr[0]));
							resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
						}
					}
				}
				
				roleLoadInfo.getRoleClubMemberInfoSet().clear();
			}
			
			
			//通知邮件服务器
			ClubService.send2MailServerSynClub(info.getId(), roleId, 0);
			
			resp.setId(info.getId());
			resp.setResult(1);

			
		}finally{
			ClubService.unLock(roleInfo);
		}
		
		
		// 公会名改变通知其他人
		SceneService1.roleNameUpdate(roleInfo);
		
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), -1);
		
		// 创建公会聊天房间
		try {
			if (VoiceGameService.getVoiceService() != null) {
				VoiceGameService.getVoiceService().sendMessageCreateRoom(GameConfig.getInstance().getVoiceAccount(), GameConfig.getInstance().getVoicePass(), GameConfig.getInstance().getVoiceKey(), "2,"+roleInfo.getClubId(), "clubRoom:" + roleInfo.getClubId() + ":" + System.currentTimeMillis(), 1);	
			}
		} catch (Exception e) {
			logger.error("create voice room error", e);
		}
		
		
		return resp;
	}

	/**
	 * 获取全部的公会信息
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetClubInfoResp getClubInfo(int roleId, GetClubInfoReq req) {
		GetClubInfoResp resp = new GetClubInfoResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		int result = 0;
		
		if((result = clubLevelCheck(roleInfo)) != 1){
			resp.setResult(result);
			return resp;
		}
		
//			List<RoleClubMemberInfo> memberList = new ArrayList<RoleClubMemberInfo>();
		boolean isMember = false;
		RoleClubInfo roleClubInfo = null;
		RoleClubMemberInfo roleClubMemberInfo = null;
		
//			Map<Integer, RoleClubMemberInfo> memberMap = null;
//			for(Integer clubId : memberMap.keySet()){
//				roleClubMemberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, roleId);
//				if(roleClubMemberInfo!= null && roleClubMemberInfo.getStatus() != 99){
//					isMember = true;
//					memberList.add(roleClubMemberInfo);
//				}
//				
//			}
		
		if(roleInfo.getClubId() > 0 && (roleClubMemberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleInfo.getClubId(), roleId)) != null){
			isMember = true;
		}
		
		if(!isMember){
			//没有加入的公会 通知客户端 跳到公会列表界面 约定result = 0;
			return resp;
		}
		
//		if(memberList.size() > 2){
//			//有多条公会信息存在
//			Collections.sort(memberList, new JoinTimeOrderByEarlyComparator());
//			
//			List<Integer> idList = new ArrayList<Integer>();
//			List<Integer> clubList = new ArrayList<Integer>();
//			for(int i = memberList.size() - 1; i > 0; i--){
//				//保留第一条数据
//				idList.add(memberList.get(i).getId());
//				clubList.add(memberList.get(i).getClubId());
//			}
//			
//			RoleClubMemberDao.getInstance().batchDeleteRoleMemberInfo(idList);
//			
//			synchronized(GameFlag.ROLE_CLUB){
//				for(Integer clubId : clubList){
//					RoleClubMemberInfoMap.removeClubMemberMap(clubId, roleId);
//				}
//			}
//			
//			for(Integer clubId : clubList){
//				roleLoadInfo.getRoleClubMemberInfoMap().remove(clubId);
//			}
//			
//		}
		
		roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleClubMemberInfo.getClubId());
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		resign(roleClubInfo); // 检查会长是否流失
		
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
		
		resp.setResult(1);
		resp.setLevel(roleClubInfo.getLevel());
		resp.setLevelLimit(roleClubInfo.getLevelLimit());
		
		
//		memberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(roleClubInfo.getId());
//		
//		if(memberMap != null && memberMap.size() > GameValue.CLUB_MEMBER_LIMIT){
//			List<RoleClubMemberInfo> overList = new ArrayList<RoleClubMemberInfo>(); //用于判断是否有超出的会员
//			//超出限定大小
//			for(Map.Entry<Integer, RoleClubMemberInfo> entry : memberMap.entrySet()){
//				if(entry.getValue() != null && entry.getValue().getStatus() == 0){
//					overList.add(entry.getValue());
//				}
//			}
//			
//			if(overList.size() > GameValue.CLUB_MEMBER_LIMIT - 1){
//				//去除会长，并且超出，按照时间排序 将最晚入会的踢出公会
//				Collections.sort(overList, new JoinTimeOrderByEarlyComparator());
//				
//				List<Integer> dataBaseIdList = new ArrayList<Integer>(); //数据库主键ID
//				List<Integer> clubRoleIdList = new ArrayList<Integer>(); //角色ID
//				for(int i = GameValue.CLUB_MEMBER_LIMIT - 1; i < overList.size(); i++){
//					dataBaseIdList.add(overList.get(i).getId());
//					clubRoleIdList.add(overList.get(i).getRoleId());
//				}
//				
//				if(RoleClubMemberDao.getInstance().batchDeleteRoleMemberInfo(dataBaseIdList)){
//					synchronized(GameFlag.ROLE_CLUB){
//						for(Integer rId : clubRoleIdList){
//							memberMap.remove(rId);
//						}
//					}
//					
//					ClubService.sendMail2Role(new String[]{"ROLE_CLUB_KICK_TITLE", "ROLE_CLUB_KICK_CONTENT"}, roleInfo.getRoleName() + "," + roleClubInfo.getClubName(), clubRoleIdList, 0);
//				}
//			}
//			
//		}
		
		
		resp.setRequestList(ClubService.getJoinRequestList(roleInfo, roleClubInfo, resp));
		resp.setRequestCount(resp.getRequestList().size());
		
		
		resp.setList(ClubService.getRoleClubMemberList(roleInfo, roleClubInfo.getId()));
		resp.setCount(resp.getList().size());
		
		
		resp.setEveList(ClubService.getClubEveInfoList(roleClubInfo));
		resp.setEveListCount(resp.getEveList().size());
		

		return resp;
	}

	/**
	 * 加入公会
	 * @param roleId
	 * @param req
	 * @return
	 */
	public JoinClubResp joinClub(int roleId, JoinClubReq req) {
		JoinClubResp resp = new JoinClubResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		if (!ClubService.tryLock(roleInfo)) {
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_9);
			return resp;
		}
		
		try{
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
				return resp;
			}
			
			int result = 0;
			
			if((result = clubLevelCheck(roleInfo)) != 1){
				resp.setResult(result);
				return resp;
			}
			
			RoleClubInfo info = RoleClubInfoMap.getRoleClubInfoByClubId(req.getClubId());
			if(info == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
				return resp; 
			}
			
			
			if(req.getAction() == 0){
				//加入公会
				resp.setResult(joinClub(resp, roleInfo, info));
				
			}else if(req.getAction() == 1){
				//退出公会
				resp.setResult(quitClub(roleInfo, info));
			} else {
				//非法操作
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_35);
				return resp; 
			}
			
			resp.setAction(req.getAction());
			
			
		}finally{
			ClubService.unLock(roleInfo);
		}
		
		
		return resp;
	}

	/**
	 * 操作公会及公会成员
	 * @param roleId
	 * @param req
	 * @return
	 */
	public OperationClubResp operationClub(int roleId, OperationClubReq req) {
		OperationClubResp resp = new OperationClubResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
			
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		int result = 0;
		
		if((result = clubLevelCheck(roleInfo)) != 1){
			resp.setResult(result);
			return resp;
		}
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(req.getClubId());
		
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		RoleClubMemberInfo leader = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
		
		if(leader == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_16);
			return resp;
		}
		
		if(roleId == req.getRoleId()){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_23);
			return resp;
		}
		
		RoleInfo memberRole = RoleInfoMap.getRoleInfo(req.getRoleId());
		if(memberRole == null && req.getAction() != 3){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_17);
			return resp;
		}
		
		if(req.getAction() == 0){
			//通过申请
			if(leader.getStatus() != RoleClubMemberInfo.CLUB_BOSS
					&& leader.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT
					&& leader.getStatus() != RoleClubMemberInfo.CLUB_LEADER){
				
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_16);
				return resp;
			}
			resp.setResult(pass(roleInfo, roleClubInfo, memberRole));
			
		}else if(req.getAction() == 1){
			//拒绝申请
			if(leader.getStatus() != RoleClubMemberInfo.CLUB_BOSS
					&& leader.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT
					&& leader.getStatus() != RoleClubMemberInfo.CLUB_LEADER){
				
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_16);
				return resp;
			}
			
			resp.setResult(refuse(roleInfo, roleClubInfo, memberRole));
			
		}else if(req.getAction() == 2){
			//踢人
			if(leader.getStatus() != RoleClubMemberInfo.CLUB_BOSS
					&& leader.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT
					&& leader.getStatus() != RoleClubMemberInfo.CLUB_LEADER){
				
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_16);
				return resp;
			}
			
			resp.setResult(kick(roleInfo, roleClubInfo, memberRole, leader));
			
		}else if(req.getAction() == 3){
			//解散
			if(leader.getStatus() != RoleClubMemberInfo.CLUB_BOSS)
			{
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_16);
				return resp;
			}
			resp.setResult(dismiss(roleInfo, roleClubInfo));
			
			// 退出公会房间
			if (roleClubInfo.getVoiceInfo() != null) {
				try {
					if (VoiceGameService.getVoiceService() != null) {
						VoiceGameService.getVoiceService().sendMessageRemoveRoom(GameConfig.getInstance().getVoiceAccount(), GameConfig.getInstance().getVoicePass(), GameConfig.getInstance().getVoiceKey(), roleClubInfo.getVoiceInfo().getId() + "", roleClubInfo.getVoiceInfo().getRoomId());	
					}
				} catch (Exception e) {
					logger.error("delete voice room error", e);
				}
			}
		} else {
			
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_35);
			return resp;
		}
		
		if(resp.getResult() != 1){
			return resp;
		}
		
		resp.setRoleId(req.getRoleId());
		resp.setClubId(req.getClubId());
		resp.setAction(req.getAction());
		
		return resp;
	}

	/**
	 * 解散公会
	 * @param roleInfo
	 * @param roleClubInfo
	 * @return
	 */
	private int dismiss(RoleInfo roleInfo, RoleClubInfo roleClubInfo) {
		Map<Integer, RoleClubMemberInfo> map = RoleClubMemberInfoMap.getRoleClubMemberMap(roleClubInfo.getId());
		Set<Integer> set = map.keySet();
		
		if(!RoleClubMemberDao.getInstance().deleteAllRoleMemberInfoByClubId(roleClubInfo.getId())){
			return ErrorCode.ROLE_CLUB_ERROR_25;
		}
		
		if(!RoleClubInfoDao.getInstance().deleteRoleClubInfo(roleClubInfo.getId())){
			//如果公会失败 则回滚之前删除的所以玩家
			RoleClubMemberInfo roleClubMemberInfo = null;
			for(Integer roleId : set){
				roleClubMemberInfo = map.get(roleId);
				if(roleClubMemberInfo == null){
					continue;
				}
				
				RoleClubMemberDao.getInstance().insertRoleClubMemberInfo(roleClubMemberInfo);
				
			}
			return ErrorCode.ROLE_CLUB_ERROR_25;
		}
		
		RoleClubEventDao.getInstance().deleteClubEventByClubId(roleClubInfo.getId());
		
		synchronized(GameFlag.ROLE_CLUB){
			RoleClubMemberInfoMap.removeClubMap(roleClubInfo.getId());
			RoleClubInfoMap.removeRoleIdMapByClubId(roleClubInfo.getId());
			RoleClubInfoMap.removeClubName(roleClubInfo.getClubName());
			RoleClubMemberInfoMap.removeClubEvent(roleClubInfo.getId());
			
		}
		
		List<Integer> list = new ArrayList<Integer>(set);
		
		RoleInfo memberRoleInfo = null;
		for(Integer roleId : list){
			memberRoleInfo = RoleInfoMap.getRoleInfo(roleId);
			if(memberRoleInfo == null){
				continue;
			}
			
			synchronized(memberRoleInfo){
				memberRoleInfo.setClubId(0);
				
				
				if(memberRoleInfo.getRoleLoadInfo() != null){
					memberRoleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet().clear();
				}
				
				ClubService.hireBalance(memberRoleInfo);
				
				// 公会名改变通知场景里的人
				SceneService1.roleNameUpdate(memberRoleInfo);
				
				if(memberRoleInfo.getLoginStatus() == 1){
					SceneService.sendRoleRefreshMsg(new RoomIdMsgResp(0) , memberRoleInfo.getId(), Command.CLUB_ROOM_ID_RESP);
				}
				
			}
			
			//在场景中 切退出场景
			SceneService.sendRoleRefreshMsg(new GetOutClubSceneResp(1) , memberRoleInfo.getId(), Command.GET_OUT_CLUB_SCENE_RESP);
			
		}
		
		ClubSceneInfoMap.cleanUpScene(roleClubInfo.getId());
		
		//记录时间 24小时内不允许创建
		ClubEventInfo event = new ClubEventInfo();
		event.setClubId(roleClubInfo.getId());
		event.setRoleId(roleInfo.getId());
		event.setEvent(3);
		event.setTime(new Timestamp(System.currentTimeMillis()));
		
		if(RoleClubEventDao.getInstance().insertRoleClubEventInfo(event)){
			synchronized (GameFlag.ROLE_CLUB) {
				RoleClubMemberInfoMap.addEvent(event);
			}
		}
		
		
		ClubService.sendMail2Role(new String[]{"ROLE_CLUB_DISMISS_TITLE", "ROLE_CLUB_DISMISS_CONTENT"}, roleInfo.getRoleName() + "," + roleClubInfo.getClubName(), list, 0);
		
		//通知邮件服务器
		ClubService.send2MailServerSynClub(roleClubInfo.getId(), 0, 2);
		
		RedPointMgtService.check2PopRedPoint(roleClubInfo.getCreateRoleId(), null, true, RedPointMgtService.LISTENING_CLUB_TYPE);
		
		
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 3);

		return 1;
	}

	/**
	 * 会长踢人
	 * @param roleInfo
	 * @param clubId
	 * @param roleId 被踢的角色
	 * @return
	 */
	private int kick(RoleInfo roleInfo, RoleClubInfo roleClubInfo, RoleInfo memberRole, RoleClubMemberInfo leader) {
		RoleClubMemberInfo info = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), memberRole.getId());
		if(info == null || 
				(info.getStatus() != RoleClubMemberInfo.CLUB_MEMBER && info.getStatus() != RoleClubMemberInfo.CLUB_BOSS
				&& info.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT && info.getStatus() != RoleClubMemberInfo.CLUB_LEADER )){
			return ErrorCode.ROLE_CLUB_ERROR_22;
		}
		
		if(info.getStatus() != RoleClubMemberInfo.CLUB_MEMBER && info.getStatus() < leader.getStatus()){
			return ErrorCode.ROLE_CLUB_ERROR_58;
		}
		
		if(!ClubService.tryLock(memberRole)){
			return ErrorCode.ROLE_CLUB_ERROR_21;
		}
		
		try{
			
			if(!RoleClubMemberDao.getInstance().deleteAllRoleMemberInfo4Refuse(info.getRoleId(), info.getClubId())){
				return ErrorCode.ROLE_CLUB_ERROR_24;
			}
			
			//加入退出事件
			ClubEventInfo clubEventInfo = new ClubEventInfo();
			clubEventInfo.setClubId(roleClubInfo.getId());
			clubEventInfo.setEvent(2);
			clubEventInfo.setRoleId(memberRole.getId());
			clubEventInfo.setTime(new Timestamp(System.currentTimeMillis()));
			
			RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
			
			memberRole.setClubId(0);
			if(memberRole.getRoleLoadInfo() != null){
				memberRole.getRoleLoadInfo().getRoleClubMemberInfoSet().clear();
			}
			
			
			synchronized(GameFlag.ROLE_CLUB){
				RoleClubMemberInfoMap.removeClubMemberMap(info.getClubId(), info.getRoleId());
				RoleClubMemberInfoMap.addEvent(clubEventInfo);
			}
			
			synchronized (roleClubInfo) {
				if(roleClubInfo.getAdminSet().contains(memberRole.getId())){
					ClubService.removeAdmin(roleClubInfo, memberRole.getId());
				}
			}
			
			//佣兵结算
			if(memberRole.getHireHeroMap().size() > 0){
				synchronized(memberRole){
					ClubService.hireBalance(memberRole);
				}
			}
			
			
		}finally{
			ClubService.unLock(memberRole);
		}
		
		
		//在场景中 则移除
		Set<Integer> set = ClubSceneInfoMap.getSceneRoleSet(roleClubInfo.getId(), info.getSceneId());
		if(set.contains(info.getRoleId())){
			set.remove(info.getRoleId());
			ClubSceneService.notifyDelClubSceneRole(info.getRoleId(), set);
		}
		
		//在场景中 切退出场景
		SceneService.sendRoleRefreshMsg(new GetOutClubSceneResp(1) , memberRole.getId(), Command.GET_OUT_CLUB_SCENE_RESP);

		
		ClubService.sendMail2Role(new String[]{"ROLE_CLUB_KICK_TITLE", "ROLE_CLUB_KICK_CONTENT"}, roleInfo.getRoleName() + "," + roleClubInfo.getClubName(), null, memberRole.getId());
		
		//通知邮件服务器
		ClubService.send2MailServerSynClub(roleClubInfo.getId(), memberRole.getId(), 1);
		
		
		GameLogService.insertRoleClubLog(memberRole, memberRole.getClubId(), 2);
		
		// 公会名改变通知场景里的人
		SceneService1.roleNameUpdate(roleInfo);
		
		//发送公会的ROOMID
		if(memberRole.getLoginStatus() == 1){
			SceneService.sendRoleRefreshMsg(new RoomIdMsgResp(0) , memberRole.getId(), Command.CLUB_ROOM_ID_RESP);
			
		}
		
		return 1;
	}

	/**
	 * 拒绝加入
	 * @param roleInfo
	 * @param clubId
	 * @param roleId 被拒绝的角色
	 * @return
	 */
	private int refuse(RoleInfo roleInfo, RoleClubInfo roleClubInfo, RoleInfo memberRole) {
		RoleClubMemberInfo info = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), memberRole.getId());
		if(info == null || info.getStatus() != RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			return ErrorCode.ROLE_CLUB_ERROR_18;
		}
		
		if(!ClubService.tryLock(memberRole)){
			return ErrorCode.ROLE_CLUB_ERROR_21;
		}
		
		try{
			
			if(!RoleClubMemberDao.getInstance().deleteAllRoleMemberInfo4Refuse(memberRole.getId(), roleClubInfo.getId())){
				return ErrorCode.ROLE_CLUB_ERROR_21;
			}
			
			synchronized(GameFlag.ROLE_CLUB){
				RoleClubMemberInfoMap.removeClubMemberMap(roleClubInfo.getId(), memberRole.getId());
			}
			
			if(memberRole.getRoleLoadInfo() != null){
				synchronized(memberRole){
					memberRole.getRoleLoadInfo().getRoleClubMemberInfoSet().remove(roleClubInfo.getId());
				}
			}
			
		}finally{
			ClubService.unLock(memberRole);
		}
		
		ClubService.sendMail2Role(new String[]{"ROLE_CLUB_REFUSE_TITLE", "ROLE_CLUB_REFUSE_CONTENT"}, roleInfo.getRoleName() + "," + roleClubInfo.getClubName(), null, memberRole.getId());
		
		RedPointMgtService.check2PopRedPoint(roleClubInfo.getCreateRoleId(), null, true, RedPointMgtService.LISTENING_CLUB_TYPE);
		
		GameLogService.insertRoleClubLog(memberRole, memberRole.getClubId(), 12);
		
		return 1;
	}

	/**
	 * 通过公会请求
	 * @param roleInfo
	 * @param clubId
	 * @param roleId
	 * @return
	 */
	private int pass(RoleInfo roleInfo, RoleClubInfo roleClubInfo, RoleInfo memberRole) {
		RoleClubMemberInfo info = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), memberRole.getId());
		if(info == null || info.getStatus() != RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			return ErrorCode.ROLE_CLUB_ERROR_18;
		}
		
		GuildUpgradeXmlInfo xmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(roleClubInfo.getLevel());
		
		if(xmlInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_49;
		}
		
		
		int limit = xmlInfo.getMembers();
		
		if(roleClubInfo.getExtendLv() > 0){
			Map<Integer, GuildTechXMLInfo> map = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(2);
			if(map != null){
				GuildTechXMLInfo tXmlInfo = map.get(roleClubInfo.getExtendLv());
				if(tXmlInfo != null){
					limit += tXmlInfo.getAddNum();
				}
			}
		}
		
		if(ClubService.getClubMemberNum(info.getClubId()) >= limit){
			//把多余的请求去掉
//			if(RoleClubMemberDao.getInstance().deleteAllRoleMemberInfoByStatus(info.getClubId())){
//				synchronized(GameFlag.ROLE_CLUB){
//					Map<Integer, RoleClubMemberInfo> clubMemberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(info.getClubId());
//					Iterator<Map.Entry<Integer, RoleClubMemberInfo>> ite  = clubMemberMap.entrySet().iterator();
//					while(ite.hasNext()){
//						if(ite.next().getValue().getStatus() == 99){
//							ite.remove();
//						}
//					}
//				}
//			}
			
			return ErrorCode.ROLE_CLUB_ERROR_19;
		}
		
		if(!ClubService.tryLock(memberRole)){
			return ErrorCode.ROLE_CLUB_ERROR_21;
		}
		
		try{
			
			// 删除之前的所以的数据,之前所有的请求
			if(!RoleClubMemberDao.getInstance().deleteAllRoleMemberInfoByRoleId(memberRole.getId())){
				return ErrorCode.ROLE_CLUB_ERROR_9;
			}
			
			info = new RoleClubMemberInfo();
			info.setClubId(roleClubInfo.getId());
			info.setRoleId(memberRole.getId());
			info.setJoinTime(new Timestamp(System.currentTimeMillis()));
			info.setStatus(RoleClubMemberInfo.CLUB_MEMBER);
			//TODO insert增加 坐标 和FLAG数据
			if(!RoleClubMemberDao.getInstance().insertRoleClubMemberInfo(info)){
				return ErrorCode.ROLE_CLUB_ERROR_21;
			}
			
			ClubEventInfo clubEventInfo = new ClubEventInfo();
			clubEventInfo.setClubId(roleClubInfo.getId());
			clubEventInfo.setRoleId(memberRole.getId());
			clubEventInfo.setTime(info.getJoinTime());
			
			RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
			
			memberRole.setClubId(info.getClubId());
			
			if(memberRole.getRoleLoadInfo() != null){
				memberRole.getRoleLoadInfo().getRoleClubMemberInfoSet().clear();
				
			}
				
			synchronized(GameFlag.ROLE_CLUB){
				RoleClubMemberInfoMap.removeClubRoleByRoleId(memberRole.getId());
				RoleClubMemberInfoMap.addRoleClubMemberInfo(info);
				RoleClubMemberInfoMap.addEvent(clubEventInfo);
			}
			
			RoleClubInfo clubInfo = null;
			RoleInfo leaderRoleInfo = null;
			Set<Integer> requestClubId  = null;
			if(memberRole.getRoleLoadInfo() != null){
				requestClubId = memberRole.getRoleLoadInfo().getRoleClubMemberInfoSet();
			
			}else{
				requestClubId = RoleClubMemberInfoMap.getRoleRequestClubIdSet(memberRole.getId());
			}
			
			//所以角色之前申请过的公会Id
			if(requestClubId != null && requestClubId.size() > 0){
				requestClubId.remove(roleClubInfo.getId());//去除当前公会Id
				for(Integer clubId : requestClubId){
					//推送给客户端去掉其他公会的显示
					clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
					if(clubInfo == null){
						continue;
					}
					
					leaderRoleInfo = RoleInfoMap.getRoleInfo(clubInfo.getCreateRoleId());
					
					if(leaderRoleInfo == null){
						continue;
					}
					
					
					if(leaderRoleInfo.getLoginStatus() == 1){
						//会在处于登录状态 推送消息
						ClubJoinRequestInfoMsgResp requestResp = new ClubJoinRequestInfoMsgResp();
						ClubRequestInfoRe requestRe = new ClubRequestInfoRe();;
						
						requestRe.setRoleId(memberRole.getId());
						
						
						requestResp.getRequestList().add(requestRe);
						requestResp.setRequestCount(requestResp.getRequestList().size());
						requestResp.setResult(2);
						
						RedPointMgtService.check2PopRedPoint(clubInfo.getCreateRoleId(), null, true, RedPointMgtService.LISTENING_CLUB_TYPE);
						
						SceneService.sendRoleRefreshMsg(requestResp, leaderRoleInfo.getId(), Command.CLUB_REQUEST_LIST_RESP);
						
					}
					
				}
				
				
			}
			
		}finally{
			ClubService.unLock(memberRole);
		}
		
		
		ClubService.sendMail2Role(new String[]{"ROLE_CLUB_JOIN_TITLE", "ROLE_CLUB_JOIN_CONTENT"}, roleInfo.getRoleName() + "," + roleClubInfo.getClubName(), null, memberRole.getId());
		
		
		//通知邮件服务器
		ClubService.send2MailServerSynClub(roleClubInfo.getId(), memberRole.getId(), 0);
		
		RedPointMgtService.check2PopRedPoint(roleClubInfo.getCreateRoleId(), null, true, RedPointMgtService.LISTENING_CLUB_TYPE);
		
		
		GameLogService.insertRoleClubLog(memberRole, memberRole.getClubId(), 13);
		// 公会名改变通知场景里的人
		SceneService1.roleNameUpdate(memberRole);
		// 推送公会ROOMID
		if(memberRole.getLoginStatus() == 1 && roleClubInfo.getVoiceInfo() != null){
			SceneService.sendRoleRefreshMsg(new RoomIdMsgResp(roleClubInfo.getVoiceInfo().getRoomId()) , memberRole.getId(), Command.CLUB_ROOM_ID_RESP);
			
		}
		return 1;
	}

	/**
	 * 查找公会
	 * @param roleId
	 * @param req
	 * @return
	 */
	public SearchClubResp searchClub(int roleId, SearchClubReq req) {
		SearchClubResp resp = new SearchClubResp();
		
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
			
			if((result = clubLevelCheck(roleInfo)) != 1){
				resp.setResult(result);
				return resp;
			}
			
			
			if(req.getFlag() == 0){
				getRoleClubInfoPage(roleLoadInfo, resp);
				
			}else if(req.getFlag() == 1){
				if(NumberUtils.isNumber(req.getName())){
					//按照ID查询
					searchClubById(roleLoadInfo, resp, Integer.parseInt(req.getName()));
					
				}else{
					//按照名字查询
					searchClubByName(roleLoadInfo, resp, req.getName());
					
				}
			}
			
		}
		
		return resp;
	}

	/**
	 * 更新公会信息
	 * @param roleId
	 * @param req
	 * @return
	 */
	public UpdateClubInfoResp updateClubInfo(int roleId, UpdateClubInfoReq req) {
		UpdateClubInfoResp resp = new UpdateClubInfoResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
				return resp;
			}
			
			int result = 0;
			
			if((result = clubLevelCheck(roleInfo)) != 1){
				resp.setResult(result);
				return resp;
			}
			
			RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(req.getClubId());
			if(roleClubInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
				return resp;
			}
			synchronized(roleClubInfo){
			
			RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
			if(memberInfo == null || (memberInfo.getStatus() != RoleClubMemberInfo.CLUB_BOSS
					&& memberInfo.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT)){
				
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_16);
				return resp;
			}
			
			if(!"".equals(req.getDeclaration()) && WordListMap.isExistWord(req.getDeclaration(), GameConfig.getInstance().getWorldType())
					|| (!"".equals(req.getDeclaration()) && EmojiFilterUtil.containsEmoji(req.getDeclaration()))){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_28);
				return resp;
			}
			
			if((!"".equals(req.getDescription()) && WordListMap.isExistWord(req.getDescription(), GameConfig.getInstance().getWorldType()))
					|| (!"".equals(req.getDescription()) && EmojiFilterUtil.containsEmoji(req.getDescription()))){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_28);
				return resp;
			}
			
			if(req.getLevelLimit() > GameValue.CLUB_MAX_LIMIT){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_29);
				return resp;
			}
			
			RoleClubInfo to = new RoleClubInfo();
			to.setId(req.getClubId());
			to.setFlag(req.getFlag());
			to.setImageId(req.getImageId());
			to.setDeclaration(req.getDeclaration());
			to.setDescription(req.getDescription());
			to.setLevelLimit(req.getLevelLimit() < GameValue.CREATE_CLUB_LEVEL_LIMIT ? GameValue.CREATE_CLUB_LEVEL_LIMIT : req.getLevelLimit());
			
			if(!RoleClubInfoDao.getInstance().updateRoleClubInfo(to)){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_26);
				return resp;
			}
			
			
			roleClubInfo.setFlag(req.getFlag());
			roleClubInfo.setImageId(req.getImageId());
			roleClubInfo.setDeclaration(req.getDeclaration());
			roleClubInfo.setDescription(req.getDescription());
			roleClubInfo.setLevelLimit(req.getLevelLimit());
			
			resp.setClubId(req.getClubId());
			resp.setDescription(req.getDescription());
			resp.setDeclaration(req.getDeclaration());
			resp.setDescription(req.getDescription());
			resp.setFlag(req.getFlag());
			resp.setImageId(req.getImageId());
			resp.setLevelLimit(req.getLevelLimit());

//			RoleInfo memberRoleInfo = null;
//			Map<Integer, RoleClubMemberInfo> map = RoleClubMemberInfoMap.getRoleClubMemberMap(req.getClubId());
//			
//			
//			for(RoleClubMemberInfo roleClubMemberInfo : map.values()){
//				if(roleClubMemberInfo == null || roleClubMemberInfo.getRoleId() == roleId){
//					continue;
//				}
//				
//				memberRoleInfo = RoleInfoMap.getRoleInfo(roleClubMemberInfo.getRoleId());
//				
//				if(memberRoleInfo == null || memberRoleInfo.getLoginStatus() != 1 || roleClubMemberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
//					continue;
//				}
//				
//				//玩家暂时断线
//				if(memberRoleInfo.getDisconnectPhase() == 1)
//				{
//					continue;
//				}
//				
//				//推送所以的在线玩家 公会的更改
//				SceneService.sendRoleRefreshMsg(resp, memberRoleInfo.getId(), Command.UPDATE_CLUB_INFO_RESP);
//			}
			
			resp.setResult(1);
		}
		
			GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 14);
		
			
		return resp;
	}
	
	/**
	 * 公会等级判断
	 * @param roleInfo
	 * @return
	 */
	public static int clubLevelCheck(RoleInfo roleInfo){
		if(HeroInfoMap.getMainHeroInfo(roleInfo) != null && HeroInfoMap.getMainHeroInfo(roleInfo).getHeroLevel() < GameValue.CREATE_CLUB_LEVEL_LIMIT){
			return ErrorCode.ROLE_CLUB_ERROR_8;
		}
		
		return 1;
	}
	
	
	/**
	 * 加入公会
	 * @param roleInfo
	 * @return
	 */
	private int joinClub(JoinClubResp resp, RoleInfo roleInfo, RoleClubInfo info){
		HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
		if(heroInfo != null){
			if(heroInfo.getHeroLevel() < info.getLevelLimit()){
				return ErrorCode.ROLE_CLUB_ERROR_8;
			}
		}
		
		GuildUpgradeXmlInfo xmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(info.getLevel());
		
		if(xmlInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_49;
		}
		
		int limit = xmlInfo.getMembers();
		
		if(info.getExtendLv() > 0){
			Map<Integer, GuildTechXMLInfo> map = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(2);
			if(map != null){
				GuildTechXMLInfo tXmlInfo = map.get(info.getExtendLv());
				if(tXmlInfo != null){
					limit += tXmlInfo.getAddNum();
				}
			}
		}
		
		if(ClubService.getClubMemberNum(info.getId()) >= limit){
			//把多余的请求去掉
//			if(RoleClubMemberDao.getInstance().deleteAllRoleMemberInfoByStatus(info.getId())){
//				synchronized(GameFlag.ROLE_CLUB){
//					Map<Integer, RoleClubMemberInfo> clubMemberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(info.getId());
//					Iterator<Map.Entry<Integer, RoleClubMemberInfo>> ite  = clubMemberMap.entrySet().iterator();
//					while(ite.hasNext()){
//						if(ite.next().getValue().getStatus() == 99){
//							ite.remove();
//						}
//					}
//				}
//			}
			
			return ErrorCode.ROLE_CLUB_ERROR_19;
		}
		
		RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(info.getId(), roleInfo.getId());
		if(memberInfo != null && memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			if(!roleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet().contains(info.getId())){
				synchronized(roleInfo){
					roleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet().add(info.getId());
				}
			}
			
			return ErrorCode.ROLE_CLUB_ERROR_10;
		}else if(memberInfo != null && 
				(memberInfo.getStatus() == RoleClubMemberInfo.CLUB_MEMBER || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_BOSS
						|| memberInfo.getStatus() == RoleClubMemberInfo.CLUB_ASSISTANT || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_LEADER
				)){
			
			return ErrorCode.ROLE_CLUB_ERROR_11;
		}
		
		
		if(roleInfo.getClubId() > 0 || RoleClubMemberInfoMap.getRoleClubMemberInfo(info.getId(), roleInfo.getId()) != null){
			//已经存在公会了
			return ErrorCode.ROLE_CLUB_ERROR_13;
		}
		
		
		List<ClubEventInfo> eventList = RoleClubMemberInfoMap.getEventListByRoleId(roleInfo.getId());
		if(eventList != null && eventList.size() > 0){
			long dayTime = 1000 * 60 * 60 * 24;
			for(ClubEventInfo event : eventList){
				if(event == null){
					continue;
				}
				
				//1 - 退出 2 - 踢出 
				if(event.getClubId() == info.getId() && (event.getEvent() == 1  || event.getEvent() == 2) && System.currentTimeMillis() - event.getTime().getTime() < dayTime){
					//解散公会
					
					resp.setTime(dayTime - (System.currentTimeMillis() - event.getTime().getTime()));
					return 2;
				}
			}
		}
		
		
		RoleInfo createRoleInfo = RoleInfoMap.getRoleInfo(info.getCreateRoleId());
		String createRoleName = "";
		if(createRoleInfo != null){
			createRoleName = createRoleInfo.getRoleName();
		}
		
		memberInfo = new RoleClubMemberInfo();
		
		memberInfo.setRoleId(roleInfo.getId());
		memberInfo.setJoinTime(new Timestamp(System.currentTimeMillis()));
		memberInfo.setClubId(info.getId());
		
		
		if(info.getFlag() == 1){
			//需要验证
			memberInfo.setStatus(RoleClubMemberInfo.CLUB_REQUEST_MEMBER);
			resp.setFlag(1); //需要申请
			resp.setClubId(info.getId());
			
			if(!RoleClubMemberDao.getInstance().insertRoleClubMemberInfo(memberInfo)){
				return ErrorCode.ROLE_CLUB_ERROR_9;
			}
		
			synchronized(roleInfo){
				roleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet().add(memberInfo.getClubId());
			}
			
			
		}else{
			// 删除之前的所以的数据
			if(!RoleClubMemberDao.getInstance().deleteAllRoleMemberInfoByRoleId(roleInfo.getId())){
				return ErrorCode.ROLE_CLUB_ERROR_9;
			}
			
			synchronized(GameFlag.ROLE_CLUB){
				for(Integer clubId : roleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet()){
					RoleClubMemberInfoMap.removeClubMemberMap(clubId, roleInfo.getId());
				}
			}
			
			//TODO insert增加 坐标 和FLAG数据
			if(!RoleClubMemberDao.getInstance().insertRoleClubMemberInfo(memberInfo)){
				return ErrorCode.ROLE_CLUB_ERROR_9;
			}
			
			RoleClubInfo roleClubInfo = null;
			RoleInfo leaderRoleInfo = null;
			for(Integer clubId : roleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet()){
				//推送给客户端去掉其他公会的显示
				roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
				if(roleClubInfo == null){
					continue;
				}
				leaderRoleInfo = RoleInfoMap.getRoleInfo(roleClubInfo.getCreateRoleId());
				
				if(leaderRoleInfo == null){
					continue;
				}
				
				if(leaderRoleInfo.getLoginStatus() == 1){
					//会在处于登录状态 推送消息
					ClubJoinRequestInfoMsgResp requestResp = new ClubJoinRequestInfoMsgResp();
					ClubRequestInfoRe requestRe = new ClubRequestInfoRe();;
					
					requestRe.setRoleId(roleInfo.getId());
					
					
					requestResp.getRequestList().add(requestRe);
					requestResp.setRequestCount(requestResp.getRequestList().size());
					requestResp.setResult(2);
					
					RedPointMgtService.check2PopRedPoint(roleClubInfo.getCreateRoleId(), null, true, RedPointMgtService.LISTENING_CLUB_TYPE);

					SceneService.sendRoleRefreshMsg(requestResp, leaderRoleInfo.getId(), Command.CLUB_REQUEST_LIST_RESP);
				
				}
				
			}
			
			synchronized(roleInfo){
				roleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet().clear();
				roleInfo.setClubId(memberInfo.getClubId());
			}
			
		}
		
		
		// 直接加入才要事件缓存
		ClubEventInfo clubEventInfo = null;
		if(info.getFlag() == 0){
			clubEventInfo = new ClubEventInfo();
			clubEventInfo.setClubId(info.getId());
			clubEventInfo.setRoleId(roleInfo.getId());
			clubEventInfo.setTime(memberInfo.getJoinTime());
			
			RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
			
			
			ClubService.sendMail2Role(new String[]{"ROLE_CLUB_JOIN_TITLE", "ROLE_CLUB_JOIN_CONTENT"}, createRoleName + "," + info.getClubName(), null, roleInfo.getId());
			//通知邮件服务器
			ClubService.send2MailServerSynClub(info.getId(), memberInfo.getRoleId(), 0);
			
			//日志
			GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 0);
			
		}else{
			//日志
			GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 11);
		}
			
		synchronized(GameFlag.ROLE_CLUB){
			RoleClubMemberInfoMap.addRoleClubMemberInfo(memberInfo);
			if(clubEventInfo != null){
				RoleClubMemberInfoMap.addEvent(clubEventInfo);
				
			}
		}
		
		if(info.getFlag() == 1){
			//发送加入请求 推送消息给会长 请求处理
			ClubJoinRequestInfoMsgResp requestResp = new ClubJoinRequestInfoMsgResp();
			ClubRequestInfoRe requestRe = new ClubRequestInfoRe();
			
			requestRe.setFightValue(roleInfo.getFightValue());
			requestRe.setRoleId(roleInfo.getId());
			requestRe.setRoleName(roleInfo.getRoleName());
			
			heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			if(heroInfo != null){
				requestRe.setHeroNo(heroInfo.getHeroNo());
				requestRe.setLevel(heroInfo.getHeroLevel());
			}
			
			requestResp.getRequestList().add(requestRe);
			requestResp.setRequestCount(requestResp.getRequestList().size());
			requestResp.setResult(1);
						
			SceneService.sendRoleRefreshMsg(requestResp, info.getCreateRoleId(), Command.CLUB_REQUEST_LIST_RESP);
			
		} else if(info.getFlag() == 0){
			// 公会名改变通知场景里的人
			SceneService1.roleNameUpdate(roleInfo);
			
			//发送公会的ROOMID
			if(info.getVoiceInfo() != null){
				SceneService.sendRoleRefreshMsg(new RoomIdMsgResp(info.getVoiceInfo().getRoomId()) , roleInfo.getId(), Command.CLUB_ROOM_ID_RESP);
				
			}
			
		}
		//红点推送，会长，副会长，官员
		RedPointMgtService.check2PopRedPoint(info.getCreateRoleId(), null, true, RedPointMgtService.LISTENING_CLUB_TYPE);
		RoleClubMemberInfo mInfo = null;
		for(Integer rId : info.getAdminSet()){
			mInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(info.getId(), rId);
			if(mInfo == null){
				continue;
			}
			if(mInfo.getStatus() == RoleClubMemberInfo.CLUB_ASSISTANT){
				RedPointMgtService.check2PopRedPoint(rId, null, true, RedPointMgtService.LISTENING_CLUB_TYPE);
			}
			if(mInfo.getStatus() == RoleClubMemberInfo.CLUB_LEADER){
				RedPointMgtService.check2PopRedPoint(rId, null, true, RedPointMgtService.LISTENING_CLUB_TYPE);
			}
		}

		
		
		return 1;
	}
	
	/**
	 * 退出公会
	 * @param roleInfo
	 * @return
	 */
	private int quitClub(RoleInfo roleInfo, RoleClubInfo info){
		RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(info.getId(), roleInfo.getId());
		if(memberInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_12;
		}
		
		if(memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			return ErrorCode.ROLE_CLUB_ERROR_12;
		}
		
		if(memberInfo.getStatus() == RoleClubMemberInfo.CLUB_BOSS){
			return ErrorCode.ROLE_CLUB_ERROR_14;
		}
		
		
		if(!RoleClubMemberDao.getInstance().deleteAllRoleMemberInfoByRoleId(roleInfo.getId())){
			return ErrorCode.ROLE_CLUB_ERROR_15;
		}
		
		
		//加入退出事件
		ClubEventInfo clubEventInfo = new ClubEventInfo();
		clubEventInfo.setClubId(info.getId());
		clubEventInfo.setEvent(1);
		clubEventInfo.setRoleId(roleInfo.getId());
		clubEventInfo.setTime(new Timestamp(System.currentTimeMillis()));
		
		RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
		
		synchronized(GameFlag.ROLE_CLUB){
			RoleClubMemberInfoMap.removeClubMemberMap(info.getId(), roleInfo.getId());
			RoleClubMemberInfoMap.addEvent(clubEventInfo);
			
		}
		
		synchronized(roleInfo){
			roleInfo.getRoleLoadInfo().getRoleClubMemberInfoSet().clear();
			roleInfo.setClubId(0);
			
			ClubService.hireBalance(roleInfo);
			
		}
		
		synchronized (info) {
			if(info.getAdminSet().contains(roleInfo.getId())){
				ClubService.removeAdmin(info, roleInfo.getId());
			}
		}
		
		Set<Integer> set = ClubSceneInfoMap.getSceneRoleSet(memberInfo.getClubId(), memberInfo.getSceneId());
		//移除场景
		if(set.contains(memberInfo.getRoleId())){
			set.remove(memberInfo.getRoleId());
			ClubSceneService.notifyDelClubSceneRole(roleInfo.getId(), set);
		}
		
		
		//在场景中 切退出场景
		SceneService.sendRoleRefreshMsg(new GetOutClubSceneResp(1) , roleInfo.getId(), Command.GET_OUT_CLUB_SCENE_RESP);

		//通知邮件服务器
		ClubService.send2MailServerSynClub(info.getId(), roleInfo.getId(), 1);
		
		
		//日志
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 1);
		
		// 公会名改变通知场景里的人
		SceneService1.roleNameUpdate(roleInfo);
		
		//发送公会的ROOMID
		SceneService.sendRoleRefreshMsg(new RoomIdMsgResp(0) , roleInfo.getId(), Command.CLUB_ROOM_ID_RESP);
		
		return 1;
	}
	
	
	/**
	 * 获取公会事件
	 * @param roleId
	 * @param clubId
	 * @return
	 */
	public ClubEveInfoMsgResp getEveList(int roleId, int clubId) {
		ClubEveInfoMsgResp resp = new ClubEveInfoMsgResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		int result = 0;
		
		if((result = clubLevelCheck(roleInfo)) != 1){
			resp.setResult(result);
			return resp;
		}
		
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		
		resp.setEveList(ClubService.getClubEveInfoList(roleClubInfo));
		resp.setEveListCount(resp.getEveList().size());
		
		resp.setResult(1);
		return resp;
	}

	/**
	 * 获取公会请求列表（有权限者）
	 * @param roleId
	 * @param clubId
	 * @return
	 */
	public ClubJoinRequestInfoMsgResp getJoinRequstList(int roleId, int clubId) {
		ClubJoinRequestInfoMsgResp resp = new ClubJoinRequestInfoMsgResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		int result = 0;
		
		if((result = clubLevelCheck(roleInfo)) != 1){
			resp.setResult(result);
			return resp;
		}
		
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		resp.setRequestList(ClubService.getJoinRequestList(roleInfo, roleClubInfo, null));
		resp.setRequestCount(resp.getRequestList().size());
		
		
		resp.setResult(1);
		return resp;
	}

	/**
	 * 获取公会成员列表
	 * @param roleId
	 * @param clubId
	 * @return
	 */
	public ClubRoleMemberInfoMsgResp getClubMemberInfoList(int roleId, int clubId) {
		ClubRoleMemberInfoMsgResp resp = new ClubRoleMemberInfoMsgResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		int result = 0;
		
		if((result = clubLevelCheck(roleInfo)) != 1){
			resp.setResult(result);
			return resp;
		}
		
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		resp.setList(ClubService.getRoleClubMemberList(roleInfo, clubId));
		resp.setCount(resp.getList().size());
		
		resp.setResult(1);
		return resp;
		
	}
	
	/**
	 * 分页显示公会信息
	 * @param roleLoadInfo
	 * @param resp
	 */
	private void getRoleClubInfoPage(RoleLoadInfo roleLoadInfo, SearchClubResp resp){
		Set<Integer> idSet = new HashSet<Integer>(RoleClubInfoMap.getRoleClubInfoClubIdKeySet());
		
		RoleClubInfo info = null;
		ClubInfoRe re = null;
		int reqCount = 0; //申请过的计数器
		
		Set<Integer> displaySet = new HashSet<Integer>();
		
		for(Integer clubId : idSet){
			info = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
			if(info == null){
				continue;
			}
			
			if(roleLoadInfo.getClubDisplaySet().contains(clubId)){
				//上批显示过的
				continue;
			}
			
			re = new ClubInfoRe();
			
			if(roleLoadInfo.getRoleClubMemberInfoSet().contains(info.getId())){
				if(reqCount >= GameValue.CLUB_INFO_REQ_DISPALY_NUM){
					//到达申请过的公会显示数量上限
					continue;
				}
				
				//已经申请加入
				re.setIsReq(1);
				reqCount++;
			}
			
			re.setClubId(info.getId());
			re.setClubName(info.getClubName());
			re.setDeclaration(info.getDeclaration());
			re.setLevel(info.getLevel());
			re.setImageId(info.getImageId());
			re.setIsApprove(info.getFlag());
			
			GuildUpgradeXmlInfo xmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(info.getLevel());
			
			if(xmlInfo != null){
				re.setMemberNumLimit(xmlInfo.getMembers());
			}
			
			if(info.getExtendLv() > 0){
				Map<Integer, GuildTechXMLInfo> map = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(2);
				if(map != null){
					GuildTechXMLInfo tXmlInfo = map.get(info.getExtendLv());
					if(tXmlInfo != null){
						re.setMemberNumLimit(re.getMemberNumLimit() + tXmlInfo.getAddNum());
					}
				}
			}
			
			re.setMemberNum(ClubService.getClubMemberNum(info.getId()));
			re.setCreateTime(info.getCreateTime().getTime());
			re.setBuild(info.getBuild());
			re.setLevelLimit(info.getLevelLimit());
			
			resp.getList().add(re);
			
			displaySet.add(clubId);
			
			if(resp.getList().size() >= GameValue.CLUB_INFO_DISPALY_NUM){
				break;
			}
		}
		
//		new ClubComparator(resp.getList()); //特定排序 该比较器相当耗CPU
		if(resp.getList().size() < GameValue.CLUB_INFO_DISPALY_NUM){
			//小于20条补足 说明
			idSet.clear();
			idSet.addAll(roleLoadInfo.getClubDisplaySet());
			
			for(Integer clubId : idSet){
				info = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
				if(info == null){
					continue;
				}
				
				resp.getList().add(getClubInfoRe(roleLoadInfo, info));
				
				
				displaySet.add(clubId);
				
				if(resp.getList().size() >= GameValue.CLUB_INFO_DISPALY_NUM){
					break;
				}
			}
		}
		
		roleLoadInfo.getClubDisplaySet().clear();
		
		roleLoadInfo.setClubDisplaySet(displaySet);
		
		
		resp.setCount(resp.getList().size());
		resp.setResult(1);
	}
	
	/**
	 * 根据名字查找公会
	 * @param roleLoadInfo
	 * @param resp
	 * @param clubName
	 */
	private void searchClubByName(RoleLoadInfo roleLoadInfo, SearchClubResp resp, String clubName){
		Set<Integer> clubIdSet = RoleClubInfoMap.getRoleClubInfoClubIdKeySet();
		
		if("".equals(clubName) || clubName == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return;
		}
		
		RoleClubInfo info = null;
		for(Integer clubId : clubIdSet){
			info = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
			if(info == null){
				continue;
			}
			
			if(info.getClubName().indexOf(clubName) != -1){
				
				resp.getList().add(getClubInfoRe(roleLoadInfo, info));
				
			}
		}
		
		resp.setCount(resp.getList().size());
		resp.setResult(2);
		
	}
	
	/**
	 * 根据ClubId查找公会
	 * @param roleLoadInfo
	 * @param resp
	 * @param clubId
	 */
	private void searchClubById(RoleLoadInfo roleLoadInfo, SearchClubResp resp, int clubId){
		RoleClubInfo info = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
		if(info == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return;
		}
		
		
		resp.getList().add(getClubInfoRe(roleLoadInfo, info));
		resp.setCount(resp.getList().size());
		resp.setResult(2);
		
	}
	
	private ClubInfoRe getClubInfoRe(RoleLoadInfo roleLoadInfo, RoleClubInfo info){
		ClubInfoRe re = new ClubInfoRe();
		
		re.setClubId(info.getId());
		re.setClubName(info.getClubName());
		re.setDeclaration(info.getDeclaration());
		re.setLevel(info.getLevel());
		re.setImageId(info.getImageId());
		
		GuildUpgradeXmlInfo xmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(info.getLevel());
		
		if(xmlInfo != null){
			re.setMemberNumLimit(xmlInfo.getMembers());
		}
		
		if(info.getExtendLv() > 0){
			Map<Integer, GuildTechXMLInfo> map = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(2);
			if(map != null){
				GuildTechXMLInfo tXmlInfo = map.get(info.getExtendLv());
				if(tXmlInfo != null){
					re.setMemberNumLimit(re.getMemberNumLimit() + tXmlInfo.getAddNum());
				}
			}
		}
		
		re.setMemberNum(ClubService.getClubMemberNum(info.getId()));
		re.setCreateTime(info.getCreateTime().getTime());
		re.setBuild(info.getBuild());
		re.setLevelLimit(info.getLevelLimit());
		re.setIsApprove(info.getFlag());
		
		if(roleLoadInfo.getRoleClubMemberInfoSet().contains(info.getId())){
			//已经申请加入
			re.setIsReq(1);
		}
		
		return re;
	}

	/**
	 * 任命公会成员
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ClubAppointResp appointClubMember(int roleId, ClubAppointReq req) {
		ClubAppointResp resp = new ClubAppointResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		synchronized(roleClubInfo){
			RoleInfo clubRoleInfo = RoleInfoMap.getRoleInfo(req.getClubRoleId());
			
			if(clubRoleInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_36);
				return resp; 
			}
			
			if(!ClubService.tryLock(clubRoleInfo)){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_37);
				return resp;
			}
			
			try {
				RoleClubMemberInfo member = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), clubRoleInfo.getId());
				if(member == null || member.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_38);
					return resp;
				}
				
				RoleClubMemberInfo memberLeader = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleInfo.getId());
				if(memberLeader == null){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_12);
					return resp;
				}
				
				int result = 0;
				
				if(req.getFlag() == 0){
					//降为普通成员
					result = demotionMember(roleInfo, roleClubInfo, memberLeader, member);
					
				}else if(req.getFlag() == 1){
					//转让公会
					result = transferClub(roleInfo, roleClubInfo, memberLeader, member, clubRoleInfo);
					
				}else if(req.getFlag() == 2){
					//任命副会长
					result = appointAssistant(roleInfo, roleClubInfo, memberLeader, member);
					
				}else if(req.getFlag() == 3){
					//任命官员
					result = appointLeader(roleInfo, roleClubInfo, memberLeader, member);
					
				}else {
					
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_35);
					return resp;
				}
				
				if(result != 1){
					resp.setResult(result);
					return resp;
				}
				
			}finally{
				ClubService.unLock(clubRoleInfo);
			}
			
		}
		
		ClubEventInfo clubEventInfo = new ClubEventInfo();
		clubEventInfo.setClubId(roleClubInfo.getId());
		clubEventInfo.setRoleId(req.getClubRoleId());
		clubEventInfo.setTime(new Timestamp(System.currentTimeMillis()));
		clubEventInfo.setEvent(4 + req.getFlag());
		
		RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
		
		synchronized(GameFlag.ROLE_CLUB){
			RoleClubMemberInfoMap.addEvent(clubEventInfo);
		}
		
		resp.setResult(1);
		resp.setFlag(req.getFlag());
		resp.setClubRoleId(req.getClubRoleId());
		
		return resp;
	}

	/**
	 * 公会建设
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ClubBuildResp buildClub(int roleId, ClubBuildReq req) {
		ClubBuildResp resp = new ClubBuildResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		synchronized(roleClubInfo){
			RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
			if(memberInfo == null || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_47);
				return resp;
			}
			
			long buildingTime = System.currentTimeMillis();
			long beforeBuildTime = roleInfo.getClubBuildTime().getTime();
			
			if(DateUtil.isSameDay(beforeBuildTime, buildingTime)){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_48);
				return resp;
			}
			
			if(req.getFlag() == 0 || req.getFlag() == 1 || req.getFlag() == 2){
				// 0 - 低级建设 1-中级建设 2-高级建设
				resp.setResult(building(roleInfo, roleClubInfo, memberInfo, req.getFlag(), resp, buildingTime));
			}else{
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_35);
				return resp;
			}
			
			if(resp.getResult() != 1){
				return resp;
			}
			
		}
		
		ClubEventInfo clubEventInfo = new ClubEventInfo();
		clubEventInfo.setClubId(roleClubInfo.getId());
		clubEventInfo.setRoleId(roleId);
		clubEventInfo.setTime(roleInfo.getClubBuildTime());
		clubEventInfo.setEvent(8 + req.getFlag());
		
		RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
		
		synchronized(GameFlag.ROLE_CLUB){
			RoleClubMemberInfoMap.addEvent(clubEventInfo);
		}

		return resp;
	}

	/**
	 * 公会升级
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ClubUpgradeResp clubUpgrade(int roleId, ClubUpgradeReq req) {
		ClubUpgradeResp resp = new ClubUpgradeResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		if(roleInfo.getRoleLoadInfo() == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
		
		if(roleClubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		synchronized(roleClubInfo){
			RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
			
			if(memberInfo == null ||
					(memberInfo.getStatus() != RoleClubMemberInfo.CLUB_BOSS 
							&& memberInfo.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT)
			){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_16);
				return resp;
				
			}
			
			int upLevel = roleClubInfo.getLevel() + 1;
			
			GuildUpgradeXmlInfo upXmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(upLevel);
			
			if(upXmlInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_51);
				return resp;
			}
			
			upXmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(roleClubInfo.getLevel());
			if(upXmlInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_49);
				return resp;
			}
			
			int surplusBuild = roleClubInfo.getBuild() - upXmlInfo.getConstructionPoint();
			
			if(surplusBuild < 0){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_52);
				return resp;
			}
			
			if(!RoleClubInfoDao.getInstance().updateRoleClubInfoBuildAndLevel(roleClubInfo.getId(), surplusBuild, upLevel)){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_53);
				return resp;
			}
			
			roleClubInfo.setLevel(upLevel);
			roleClubInfo.setBuild(surplusBuild);
			
		}
		
		ClubEventInfo clubEventInfo = new ClubEventInfo();
		clubEventInfo.setClubId(roleClubInfo.getId());
		clubEventInfo.setRoleId(roleId);
		clubEventInfo.setTime(new Timestamp(System.currentTimeMillis()));
		clubEventInfo.setEvent(11);
		
		RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
		
		synchronized(GameFlag.ROLE_CLUB){
			RoleClubMemberInfoMap.addEvent(clubEventInfo);
		}
		
		resp.setResult(1);
		
		return resp;
	}
	
	/**
	 * 任命副会长
	 * @param roleClubInfo
	 * @param memberLeader 管理者
	 * @param member 被任命的成员
	 * @return
	 */
	private int appointAssistant(RoleInfo roleInfo, RoleClubInfo roleClubInfo, RoleClubMemberInfo memberLeader, RoleClubMemberInfo member){
		if(memberLeader.getStatus() != RoleClubMemberInfo.CLUB_BOSS){
			return ErrorCode.ROLE_CLUB_ERROR_40;
		}
		
		if(member.getStatus() == RoleClubMemberInfo.CLUB_ASSISTANT){
			return ErrorCode.ROLE_CLUB_ERROR_41;
		}
		
		GuildUpgradeXmlInfo xmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(roleClubInfo.getLevel());
		
		if(xmlInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_49;
		}
		
		RoleClubMemberInfo mInfo = null;
		int i = 0;
		for(Integer rId : roleClubInfo.getAdminSet()){
			mInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), rId);
			
			if(mInfo == null){
				ClubService.removeAdmin(roleClubInfo, rId);
				continue;
			}
			
			if(mInfo.getStatus() == RoleClubMemberInfo.CLUB_ASSISTANT){
				i++;
			}
			
			if(i == xmlInfo.getVicePresident()){
				return ErrorCode.ROLE_CLUB_ERROR_42;
			}
			
		}
		
		if(!RoleClubMemberDao.getInstance().updateClubMemberInfoStatus(member.getRoleId(), member.getClubId(), RoleClubMemberInfo.CLUB_ASSISTANT)){
			return ErrorCode.ROLE_CLUB_ERROR_37;
		}
		
		member.setStatus(RoleClubMemberInfo.CLUB_ASSISTANT); //副会长
		
		ClubService.addAdmin(roleClubInfo, member.getRoleId());
		
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 6);
		
		return 1;
	}
	
	/**
	 * 转让公会
	 * @param roleInfo 自己
	 * @param roleClubInfo
	 * @param memberLeader
	 * @param member
	 * @param clubRoleInfo 转给的玩家
	 * @return
	 */
	private int transferClub(RoleInfo roleInfo, RoleClubInfo roleClubInfo, RoleClubMemberInfo memberLeader, RoleClubMemberInfo member, RoleInfo clubRoleInfo){
		if(memberLeader.getStatus() != RoleClubMemberInfo.CLUB_BOSS){
			return ErrorCode.ROLE_CLUB_ERROR_39;
		}
		
		if(clubRoleInfo.getLoginStatus() == 0 && DateUtil.compareDayBalance(clubRoleInfo.getLogoutTime().getTime(), System.currentTimeMillis()) > 7){
			return ErrorCode.ROLE_CLUB_ERROR_70;
		}
		
		if(RoleClubInfoDao.getInstance().updateRoleClubInfoCreateRoleId(member.getClubId(), member.getRoleId())){
			roleClubInfo.setCreateRoleId(member.getRoleId());
		}
		
		
		if(!RoleClubMemberDao.getInstance().updateClubMemberInfoStatus4Transaction(member.getRoleId(), RoleClubMemberInfo.CLUB_BOSS, memberLeader.getRoleId(), RoleClubMemberInfo.CLUB_MEMBER, roleClubInfo.getId())){
			return ErrorCode.ROLE_CLUB_ERROR_37;
		}
		
		member.setStatus(RoleClubMemberInfo.CLUB_BOSS);
		memberLeader.setStatus(RoleClubMemberInfo.CLUB_MEMBER);
		ClubService.removeAdmin(roleClubInfo, member.getRoleId());
		
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 5);
		
		// 通知场景的所有人 将原会长形象变更为新会长
		ClubSceneInfo clubSceneInfo = ClubSceneInfoMap.getClubSceneInfoByClubId(roleClubInfo.getId());
		if(clubSceneInfo != null){
			ClubSceneRoleInfoResp clubSceneRoleInfoResp = new ClubSceneRoleInfoResp();
			clubSceneRoleInfoResp.setResult(1);
			clubSceneRoleInfoResp.setFlag(2);
			
			List<ClubSceneRoleInfo> list = new ArrayList<ClubSceneRoleInfo>();
			list.add(ClubSceneService.getClubSceneRoleInfo(clubRoleInfo, member));
			clubSceneRoleInfoResp.setList(list);
			clubSceneRoleInfoResp.setCount(list.size());
			
			Set<Integer> sceneRoleIdSet = null;
			for(Integer sceneId : clubSceneInfo.getClubSceneMap().keySet()){
				sceneRoleIdSet = clubSceneInfo.getClubSceneMap().get(sceneId);
				if(sceneRoleIdSet == null || sceneRoleIdSet.size() <= 0){
					continue;
				}
				
				for(Integer sceneRoleId : sceneRoleIdSet){
					SceneService.sendRoleRefreshMsg(clubSceneRoleInfoResp, sceneRoleId, Command.CLUB_SCENE_ROLE_CHANGE_RESP);
				}
				
			}
		}
		
		
		return 1;
	
	}
	
	/**
	 * 降职
	 * @param roleClubInfo
	 * @param memberLeader
	 * @param member
	 * @return
	 */
	private int demotionMember(RoleInfo roleInfo, RoleClubInfo roleClubInfo, RoleClubMemberInfo memberLeader, RoleClubMemberInfo member){
		if(memberLeader.getStatus() != RoleClubMemberInfo.CLUB_BOSS && memberLeader.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT){
			return ErrorCode.ROLE_CLUB_ERROR_45;
		}
		
		
		if(member.getStatus() == RoleClubMemberInfo.CLUB_MEMBER){
			return ErrorCode.ROLE_CLUB_ERROR_57;
		}
		
		if(!RoleClubMemberDao.getInstance().updateClubMemberInfoStatus(member.getRoleId(), member.getClubId(), RoleClubMemberInfo.CLUB_MEMBER)){
			return ErrorCode.ROLE_CLUB_ERROR_37;
		}
		
		member.setStatus(RoleClubMemberInfo.CLUB_MEMBER);
		
		ClubService.removeAdmin(roleClubInfo, member.getRoleId());
		
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 4);
		
		return 1;
	}
	
	/**
	 * 任命公会官员
	 * @param roleClubInfo
	 * @param memberLeader
	 * @param member
	 * @return
	 */
	private int appointLeader(RoleInfo roleInfo, RoleClubInfo roleClubInfo, RoleClubMemberInfo memberLeader, RoleClubMemberInfo member){
		if(memberLeader.getStatus() != RoleClubMemberInfo.CLUB_BOSS && memberLeader.getStatus() != RoleClubMemberInfo.CLUB_ASSISTANT){
			return ErrorCode.ROLE_CLUB_ERROR_43;
		}
		
		if(member.getStatus() == RoleClubMemberInfo.CLUB_LEADER){
			return ErrorCode.ROLE_CLUB_ERROR_56;
		}
		
		GuildUpgradeXmlInfo xmlInfo = GuildUpgradeXmlInfoMap.getGuildUpgradeXmlInfo(roleClubInfo.getLevel());
		
		if(xmlInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_49;
		}
		
		RoleClubMemberInfo mInfo = null;
		int i = 0;
		for(Integer rId : roleClubInfo.getAdminSet()){
			mInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), rId);
			
			if(mInfo == null){
				ClubService.removeAdmin(roleClubInfo, rId);
				continue;
			}
			
			if(mInfo.getStatus() == RoleClubMemberInfo.CLUB_LEADER){
				i++;

			}
			
			if(i == xmlInfo.getOffical()){
				return ErrorCode.ROLE_CLUB_ERROR_44;
			}
			
		}
		
		if(!RoleClubMemberDao.getInstance().updateClubMemberInfoStatus(member.getRoleId(), member.getClubId(), RoleClubMemberInfo.CLUB_LEADER)){
			return ErrorCode.ROLE_CLUB_ERROR_37;
		}
		
		member.setStatus(RoleClubMemberInfo.CLUB_LEADER); //官员
		
		ClubService.addAdmin(roleClubInfo, member.getRoleId());
		
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 7);

		return 1;
	}
	
	/**
	 * 公会建设
	 * @param roleInfo
	 * @param roleClubInfo
	 * @param memberInfo
	 * @param type 0 - 初级建设 1 - 中级建设  2 - 高级建设
	 * @param buildingTime 公会建设时间
	 * @return
	 */
	private int building(RoleInfo roleInfo, RoleClubInfo roleClubInfo, RoleClubMemberInfo memberInfo, int type, ClubBuildResp resp, long buildingTime){
		GuildConstructionXmlInfo xmlInfo = GuildConstructionXmlInfoMap.getGuildConstructionXmlInfo(type + 1);
		if(xmlInfo == null){
			return ErrorCode.ROLE_CLUB_ERROR_49;
		}
		
		if(xmlInfo.getVipLv() > roleInfo.getVipLv()){
			return ErrorCode.ROLE_CLUB_ERROR_46;
		}
		
		List<AbstractConditionCheck> conds = new ArrayList <AbstractConditionCheck>();
		
		if(xmlInfo.getCostType() == 1){
			conds.add(new MoneyCond(xmlInfo.getCostNum()));
			
		}else if(xmlInfo.getCostType() == 2){
			conds.add(new CoinCond(xmlInfo.getCostNum()));
				
		}

		int check = AbstractConditionCheck.checkCondition(roleInfo, conds);

		if (check != 1)
		{
			return check;
		}
		
		int totalBuild = roleClubInfo.getBuild() + xmlInfo.getConstructionPoint();
		
		if(!RoleClubInfoDao.getInstance().updateRoleClubInfoBuildAndLevel(roleClubInfo.getId(), totalBuild, roleClubInfo.getLevel())){
			return ErrorCode.ROLE_CLUB_ERROR_50;
		}
		
		resp.setBuild(xmlInfo.getConstructionPoint());
		roleClubInfo.setBuild(totalBuild);
		
		synchronized(roleInfo){
			if(RoleService.subRoleResource(ActionType.action447.getType(), roleInfo, conds , null)){
				//扣钱成功增加贡献值
				RoleService.addRoleRoleResource(ActionType.action447.getType(), roleInfo, ConditionType.TYPE_CLUB_CONTRIBUTION, xmlInfo.getClubContribution(),null);
				resp.setClubContribution(xmlInfo.getClubContribution());
			}
			
			long roleTotalBuild = roleInfo.getTotalBuild() +  xmlInfo.getConstructionPoint();
			
			if(RoleDAO.getInstance().updateRoleClubBuildTime(roleInfo.getId(), buildingTime, roleTotalBuild)){
				roleInfo.setClubBuildTime(new Timestamp(buildingTime));
				roleInfo.setTotalBuild(roleTotalBuild);
			}
			
			String updateSourceStr = RoleService.returnResourceChange(conds);
			if (updateSourceStr != null)
			{
				String[] sourceStr = updateSourceStr.split(",");
				if (sourceStr != null && sourceStr.length > 1)
				{
					resp.setSourceType(Byte.valueOf(sourceStr[0]));
					resp.setSourceChange(-Integer.valueOf(sourceStr[1]));
				}
			}
			
		}
		
		GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 8);
		
		return 1;
	}
	
	/**
	 * 检查会长是否流失
	 * @param roleClubInfo
	 */
	public static void resign(RoleClubInfo roleClubInfo){
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleClubInfo.getCreateRoleId());
		if(roleInfo == null || (roleInfo.getLoginStatus() == 0 && DateUtil.compareDayBalance(roleInfo.getLogoutTime().getTime(), System.currentTimeMillis()) > 7)){
			//会长换人
			int srcRoleId = roleClubInfo.getCreateRoleId();
			int replceRoleId = -1;
			boolean flag = false;
			synchronized(roleClubInfo){
				RoleClubMemberInfo memberInfo = null;
				for(Integer roleId : roleClubInfo.getAdminSet()){
					roleInfo = RoleInfoMap.getRoleInfo(roleId);
					if(roleInfo == null){
						ClubService.removeAdmin(roleClubInfo, roleId);
						continue;
					}
					
					if(roleInfo.getLoginStatus() == 0 && DateUtil.compareDayBalance(roleInfo.getLogoutTime().getTime(), System.currentTimeMillis()) > 7){
						continue;
					}
					
					memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
					if(memberInfo == null){
						ClubService.removeAdmin(roleClubInfo, roleId);
						continue;
					}
					
					if(memberInfo.getStatus() == RoleClubMemberInfo.CLUB_ASSISTANT){
						flag = true;
						replceRoleId = memberInfo.getRoleId();
						break;
					}
					
				}
				
				if(!flag){
					//没找到, 继续找
					long max = 0;
					for(Integer roleId : roleClubInfo.getAdminSet()){
						roleInfo = RoleInfoMap.getRoleInfo(roleId);
						if(roleInfo == null){
							ClubService.removeAdmin(roleClubInfo, roleId);
							continue;
						}
						
						if(roleInfo.getLoginStatus() == 0 && DateUtil.compareDayBalance(roleInfo.getLogoutTime().getTime(), System.currentTimeMillis()) > 7){
							continue;
						}
						
						memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
						if(memberInfo == null){
							ClubService.removeAdmin(roleClubInfo, roleId);
							continue;
						}
						
						if(memberInfo.getStatus() == RoleClubMemberInfo.CLUB_LEADER){
							if(roleInfo.getClubContributionSum() > max){
								//获得贡献值最多的官员
								max = roleInfo.getClubContributionSum();
								replceRoleId = memberInfo.getRoleId();
							}
						}
					}
				}
				
				
				if(replceRoleId < 0){
					//还没找到, 从所有会员中找贡献值累积最多的
					long max = 0;
					Map<Integer, RoleClubMemberInfo> map = RoleClubMemberInfoMap.getRoleClubMemberMap(roleClubInfo.getId());
					
					if(map != null && map.size() > 0){
						for(Integer roleId : map.keySet()){
							if(roleId == roleClubInfo.getCreateRoleId()){
								continue;
							}
							
							roleInfo = RoleInfoMap.getRoleInfo(roleId);
							if(roleInfo == null){
								continue;
							}
							
							if(roleInfo.getLoginStatus() == 0 && DateUtil.compareDayBalance(roleInfo.getLogoutTime().getTime(), System.currentTimeMillis()) > 7){
								continue;
							}
							
							memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleId);
							if(memberInfo == null){
								continue;
							}
							
							if(roleInfo.getClubContributionSum() > max){
								max = roleInfo.getClubContributionSum();
								replceRoleId = roleId;
							}
							
						}
					}
				}
				
				if(replceRoleId > -1 && RoleInfoMap.getRoleInfo(replceRoleId) != null){
					//有人 替换掉
					if(!RoleClubInfoDao.getInstance().updateRoleClubInfoCreateRoleId(roleClubInfo.getId(), replceRoleId)){
						return;
					}
					
					roleClubInfo.setCreateRoleId(replceRoleId);
					
				}else{
					return;
				}
				
			}
			
			if(RoleClubMemberDao.getInstance().updateClubMemberInfoStatus4Transaction(roleClubInfo.getCreateRoleId(), RoleClubMemberInfo.CLUB_BOSS, srcRoleId, RoleClubMemberInfo.CLUB_MEMBER, roleClubInfo.getId())){
				synchronized(GameFlag.ROLE_CLUB){
					RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), srcRoleId);
					if(memberInfo != null){
						memberInfo.setStatus(RoleClubMemberInfo.CLUB_MEMBER);
					}
					
					memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(roleClubInfo.getId(), roleClubInfo.getCreateRoleId());
					if(memberInfo != null){
						memberInfo.setStatus(RoleClubMemberInfo.CLUB_BOSS);
					}
				}
			}
			
			ClubEventInfo clubEventInfo = new ClubEventInfo();
			clubEventInfo.setClubId(roleClubInfo.getId());
			clubEventInfo.setRoleId(replceRoleId);
			clubEventInfo.setTime(new Timestamp(System.currentTimeMillis()));
			clubEventInfo.setEvent(12);
			
			RoleClubEventDao.getInstance().insertRoleClubEventInfo(clubEventInfo);
			
			synchronized(GameFlag.ROLE_CLUB){
				RoleClubMemberInfoMap.addEvent(clubEventInfo);
			}
			
			GameLogService.insertRoleClubLog(roleInfo, roleInfo.getClubId(), 10);
		}
		
		
	}

	/**
	 * 获取公会科技信息
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetClubTechInfoResp getClubTechInfo(int roleId, GetClubTechInfoReq req) {
		GetClubTechInfoResp resp = new GetClubTechInfoResp();
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
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_12);
				return resp;
			}
			
			RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
			if(roleClubInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
				return resp;
			}
			
			RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, roleId);
			
			if(memberInfo == null || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_12);
				return resp;
			}
			
			StringBuffer sb = new StringBuffer();
			
			int xmlNo = GuildTechXMLInfoMap.getXmkNoByBuildTypeAndLevel(1, roleClubInfo.getTech());
			if(xmlNo > 0){
				sb.append(xmlNo).append(":");
			}
			
			xmlNo = GuildTechXMLInfoMap.getXmkNoByBuildTypeAndLevel(2, roleClubInfo.getExtendLv());
			if(xmlNo > 0){
				sb.append(xmlNo).append(":");
			}
			
			
			if(roleInfo.getClubTechPlusInfo() == null){
				//设置初始等级
				if(RoleDAO.getInstance().updateRoleClubTechPlusInfo(roleInfo.getId(), GameValue.INIT_ROLE_CLUB_TECH_PLUS)){
					roleInfo.setClubTechPlusInfo(GameValue.INIT_ROLE_CLUB_TECH_PLUS);
				}
			}
			
			if(roleInfo.getClubTechPlusInfo() != null && !"".equals(roleInfo.getClubTechPlusInfo())){
				try{
					int buildType = 0;
					int lv = 0;
					xmlNo = 0;
					String[] strs = roleInfo.getClubTechPlusInfo().split(";");
					for(String str : strs){
						String[] subStrs = str.split(":");
						if(subStrs.length != 2){
							continue;
						}
						
						buildType = Integer.parseInt(subStrs[0]);
						lv = Integer.parseInt(subStrs[1]);
						
						xmlNo = GuildTechXMLInfoMap.getXmkNoByBuildTypeAndLevel(buildType, lv);
						
						if(xmlNo > 0){
							sb.append(xmlNo).append(":");
						}
						
					}
					
				}catch (Exception e) {
					if(logger.isErrorEnabled()){
						logger.error("tech role plus parse error", e);
					}
				}
				
			}
			
			
			if(sb.toString().endsWith(":")){
				resp.setXmlNo(sb.substring(0, sb.length() - 1));
			}else{
				resp.setXmlNo(sb.toString());
			}
			
			resp.setGold(roleClubInfo.getGold());
			
			resp.setResult(1);
			
		}
		
		return resp;
	}

	/**
	 * 公会科技升级
	 * @param roleId
	 * @param req
	 * @return
	 */
	public ClubTechUpgradeResp clubTechUpgrade(int roleId, ClubTechUpgradeReq req) {
		ClubTechUpgradeResp resp = new ClubTechUpgradeResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_2);
			return resp;
		}
		
		int clubId = roleInfo.getClubId();
		
		RoleClubInfo clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(clubId);
		
		if(clubInfo == null){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_7);
			return resp;
		}
		
		RoleClubMemberInfo memberInfo = RoleClubMemberInfoMap.getRoleClubMemberInfo(clubId, roleInfo.getId());
		if(memberInfo == null || memberInfo.getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
			resp.setResult(ErrorCode.ROLE_CLUB_ERROR_12);
			return resp;
		}
		
		
		if(req.getBuildType() == 1){
			//科技学院升级
			techUp(roleInfo, resp, req.getBuildType(), memberInfo, clubInfo);
		}else if(req.getBuildType() == 2){
			//公会扩容
			clubExtend(roleInfo, resp, req, memberInfo, clubInfo);
		}else if(req.getBuildType() > 10){
			//角色相关属性加成
			clubTechPlus(roleInfo, resp, req.getBuildType(), memberInfo, clubInfo);
		}
		
		resp.setBuildType(req.getBuildType());
		
		return resp;
	}

	/**
	 * 科技升级
	 * @param roleInfo
	 * @param resp
	 */
	private void techUp(RoleInfo roleInfo, ClubTechUpgradeResp resp, int buildType, RoleClubMemberInfo memberInfo, RoleClubInfo clubInfo) {
		synchronized(clubInfo){
			Map<Integer, GuildTechXMLInfo> map = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(buildType);
			
			if(map == null || map.size() <= 0){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_49);
				return;
			}
			
			int tech = clubInfo.getTech();
			
			if(clubInfo.getTech() > 0 && map.get(tech + 1) == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_62);
				return;
			}
			
			GuildTechXMLInfo xmlInfo =  map.get(tech);
			if(xmlInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_49);
				return;
			}
			
			int check = techUpSecurityCheck(memberInfo, xmlInfo);
			
			if(check != 1){
				resp.setResult(check);
				return;
			}
			
			synchronized(roleInfo){
				check = techUpCheck(clubInfo, roleInfo, xmlInfo);
				if(check != 1){
					resp.setResult(check);
					return;
				}
				
				check = AbstractConditionCheck.checkCondition(roleInfo, xmlInfo.getConditions());
				
				if (check != 1){
					resp.setResult(check);
					return;
				}
				
				if(!RoleService.subRoleResource(ActionType.action474.getType(), roleInfo, xmlInfo.getConditions() , null)){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_64);
					return;
				}
				
				resp.setSourceChange(ClubService.resourceChange(xmlInfo.getConditions()));
				
				if(RoleClubInfoDao.getInstance().updateRoleClubInfoTech(clubInfo.getId(), tech + 1)){
					clubInfo.setTech(tech + 1);
				}
				
				xmlInfo =  map.get(tech + 1);
				if(xmlInfo != null){
					resp.setExtendInfo(xmlInfo.getNo() + "");
				}
				
			}
		}
		
		resp.setResult(1);
		
	}

	/**
	 * 公会扩容
	 * @param roleInfo
	 * @param resp
	 */
	private void clubExtend(RoleInfo roleInfo, ClubTechUpgradeResp resp, ClubTechUpgradeReq req,  RoleClubMemberInfo memberInfo, RoleClubInfo clubInfo) {
		synchronized(clubInfo){
			Map<Integer, GuildTechXMLInfo> map = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(req.getBuildType());
			
			if(map == null || map.size() <= 0){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_49);
				return;
			}
			
			int srcLv = clubInfo.getExtendLv();
			
			int lv = clubInfo.getExtendLv() + 1;
			
			GuildTechXMLInfo xmlInfo = map.get(lv);
			if(xmlInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_66);
				return;
			}
			
			xmlInfo =  map.get(srcLv);
			if(xmlInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_49);
				return;
			}
			

			int check = techUpSecurityCheck(memberInfo, xmlInfo);
			
			if(check != 1){
				resp.setResult(check);
				return;
			}
			
			synchronized(roleInfo){
				check = techUpCheck(clubInfo, roleInfo, xmlInfo);
				if(check != 1){
					resp.setResult(check);
					return;
				}
				
				int donate = req.getDonate();
				
				if(donate % GameValue.CLUB_EXTEND_MIN_GOLD != 0){
					//最小单位为10
					donate = donate - donate % GameValue.CLUB_EXTEND_MIN_GOLD;
				}
				
				List<AbstractConditionCheck> conds = new ArrayList <AbstractConditionCheck>();
				
				conds.add(new CoinCond(donate));
				
				check = AbstractConditionCheck.checkCondition(roleInfo, conds);
				
				if (check != 1){
					resp.setResult(check);
					return;
				}
				
				int gold = clubInfo.getGold() + req.getDonate();
				
				if(gold >= xmlInfo.getCost()){
					//公会扩容升级
					gold -= xmlInfo.getCost();
					
				}else{
					//保留原来等级
					lv = clubInfo.getExtendLv();
				}
				
				if(!RoleClubInfoDao.getInstance().updateRoleClubInfoGoldAndExtendLv(clubInfo.getId(), gold, lv)){
					resp.setResult(ErrorCode.ROLE_CLUB_ERROR_67);
				}
				
				clubInfo.setGold(gold);
				clubInfo.setExtendLv(lv);
				
				resp.setExtendInfo(map.get(lv).getNo() + ":" + clubInfo.getGold());
				
				if (RoleService.subRoleResource(ActionType.action475.getType(), roleInfo, conds , null)){
					resp.setSourceChange(ClubService.resourceChange(conds));
				}
				
				//回赠玩家公会币
				donate *= GameValue.CLUB_EXTEND_UP_RETURN;
				
				RoleService.addRoleRoleResource(ActionType.action475.getType(), roleInfo, ConditionType.TYPE_CLUB_CONTRIBUTION, donate,null);
				
				resp.setSourceChange(resp.getSourceChange() + ";" + ConditionType.TYPE_CLUB_CONTRIBUTION.getType() + ":" + donate);
			}
			
		}
		
		resp.setResult(1);
	}

	/**
	 * 自身属性升级
	 * @param roleInfo
	 * @param resp
	 */
	private void clubTechPlus(RoleInfo roleInfo, ClubTechUpgradeResp resp, int buildType,  RoleClubMemberInfo memberInfo, RoleClubInfo clubInfo) {
		
		synchronized(roleInfo){
			Map<Integer, Integer> upMap = ClubService.clubTechPlusStr2Map(roleInfo);
			
			Integer lv = upMap.get(buildType);
			
			if(lv == null){
				lv = 1;
			}
			
			Map<Integer, GuildTechXMLInfo> map = GuildTechXMLInfoMap.getGuildTechXMLInfobuildTypeMap(buildType);
			
			if(map == null || map.size() <= 0){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_49);
				return;
			}
			
			GuildTechXMLInfo xmlInfo = map.get(lv + 1);
			if(xmlInfo == null){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_68);
				return;
			}
			
			xmlInfo = map.get(lv);
			
			int check = techUpCheck(clubInfo, roleInfo, xmlInfo);
			if(check != 1){
				resp.setResult(check);
				return;
			}
			
			check = techUpSecurityCheck(memberInfo, xmlInfo);
			
			if(check != 1){
				resp.setResult(check);
				return;
			}
			check = AbstractConditionCheck.checkCondition(roleInfo, xmlInfo.getConditions());
			
			if (check != 1){
				resp.setResult(check);
				return;
			}
			
			upMap.put(buildType, lv + 1);
			
			//转换成String
			String clubTechPlusInfo = ClubService.clubTechPlusMap2Str(upMap);
			
			if(!RoleDAO.getInstance().updateRoleClubTechPlusInfo(roleInfo.getId(), clubTechPlusInfo)){
				resp.setResult(ErrorCode.ROLE_CLUB_ERROR_69);
				return;
			}
			
			roleInfo.setClubTechPlusInfo(clubTechPlusInfo);
			
			
			if (RoleService.subRoleResource(ActionType.action475.getType(), roleInfo, xmlInfo.getConditions() , null)){
				resp.setSourceChange(ClubService.resourceChange(xmlInfo.getConditions()));
			}
			
			if(buildType > 10){
				//buildType 大于10 增加的是个人属性
				//公会科技升级 各种属性增加 战斗力变化
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if(heroInfo != null){
					HeroService.refeshHeroProperty(roleInfo, heroInfo);
					resp.setFightValue(heroInfo.getFightValue());
					
				}
			}
			
			resp.setResult(1);
			
		}
	}
	
	/**
	 * 检查相关属性
	 * @param clubInfo
	 * @param roleInfo
	 * @param xmlInfo
	 * @return
	 */
	private int techUpCheck(RoleClubInfo clubInfo, RoleInfo roleInfo, GuildTechXMLInfo xmlInfo){
		int level = 0;
		if(xmlInfo.getNeedType() == 0){
			//公会等级
			level = clubInfo.getLevel();
			
		}else if(xmlInfo.getNeedType() == 1){
			// 公会科技
			level = clubInfo.getTech();
			
		}else if(xmlInfo.getNeedType() == 2){
			
			
		}else if(xmlInfo.getNeedType() == 3){
			
			
		}else if(xmlInfo.getNeedType() > 10){
			//关联个人属性
			Integer lv = ClubService.clubTechPlusStr2Map(roleInfo).get(xmlInfo.getNeedType());

			if(lv != null){
				level = lv;
			}
		}
		
		if(level < xmlInfo.getNeedLv()){
			return ErrorCode.ROLE_CLUB_ERROR_65;
		}
		
		return 1;
	}
	
	/**
	 * 
	 * @param memberInfo
	 * @param xmlInfo
	 * @return
	 */
	private int techUpSecurityCheck(RoleClubMemberInfo memberInfo, GuildTechXMLInfo xmlInfo){
		if(xmlInfo.getOffical() > 0 && memberInfo.getStatus() > xmlInfo.getOffical()){
			return ErrorCode.ROLE_CLUB_ERROR_63;
		}
		
		return 1;
	}
	
}
