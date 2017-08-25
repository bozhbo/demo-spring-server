package com.snail.webgame.game.protocal.relation.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.client.Client;
import org.epilot.ccf.core.protocol.Message;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.FriendRecommendMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleAddRquestMap;
import com.snail.webgame.game.cache.RoleBlackMap;
import com.snail.webgame.game.cache.RoleFriendMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.PresentEnergyDao;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.dao.RoleRelationDao;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.PresentEnergyInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.relation.addOrDel.AddOrDelFriendReq;
import com.snail.webgame.game.protocal.relation.addOrDel.AddOrDelFriendResp;
import com.snail.webgame.game.protocal.relation.dispose.DisposeAddRequestReq;
import com.snail.webgame.game.protocal.relation.dispose.DisposeAddRequestResp;
import com.snail.webgame.game.protocal.relation.entity.FriendDetailRe;
import com.snail.webgame.game.protocal.relation.get.GetEnergy2RoleSelfReq;
import com.snail.webgame.game.protocal.relation.get.GetEnergy2RoleSelfResp;
import com.snail.webgame.game.protocal.relation.getEnergy.GetPresentEnergyListReq;
import com.snail.webgame.game.protocal.relation.getEnergy.GetPresentEnergyListResp;
import com.snail.webgame.game.protocal.relation.getRequest.GetAddRequestReq;
import com.snail.webgame.game.protocal.relation.getRequest.GetAddRequestResp;
import com.snail.webgame.game.protocal.relation.onekey.energy.OneKeyEnergyReq;
import com.snail.webgame.game.protocal.relation.onekey.energy.OneKeyEnergyResp;
import com.snail.webgame.game.protocal.relation.onekey.op.OneKeyOperationReq;
import com.snail.webgame.game.protocal.relation.onekey.op.OneKeyOperationResp;
import com.snail.webgame.game.protocal.relation.present.PresentEnergyReq;
import com.snail.webgame.game.protocal.relation.present.PresentEnergyResp;
import com.snail.webgame.game.protocal.relation.query.GetFriendListReq;
import com.snail.webgame.game.protocal.relation.query.GetFriendListResp;
import com.snail.webgame.game.protocal.relation.recommend.RecommendFriend4AddReq;
import com.snail.webgame.game.protocal.relation.recommend.RecommendFriend4AddResp;
import com.snail.webgame.game.protocal.relation.search.SearchRoleInfoReq;
import com.snail.webgame.game.protocal.relation.search.SearchRoleInfoResp;
import com.snail.webgame.game.protocal.relation.set.SetCanFlagReq;
import com.snail.webgame.game.protocal.relation.set.SetCanFlagResp;
import com.snail.webgame.game.protocal.relation.syn.SysBlackListReq;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.cache.SceneInfoMap;
import com.snail.webgame.game.protocal.scene.info.RolePoint;

public class RoleRelationMgtService {

	/**
	 * 获取角色的好友和黑名单列表
	 * @return
	 */

	public GetFriendListResp getRoleFriendList(int roleId, GetFriendListReq req) {
		GetFriendListResp resp = new GetFriendListResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
				return resp;
			}
			
			List<FriendDetailRe> list = getFriendDetailReList(roleInfo, RoleFriendMap.getRoleFriendIdSet(roleId));
			
			Collections.sort(list, new Comparator<FriendDetailRe>(){ //按照等级从高到低排序
				@Override
				public int compare(FriendDetailRe o1, FriendDetailRe o2) {
					if(o1.getLevel() > o2.getLevel()){
						return 1;
					}else if(o1.getLevel() == o2.getLevel()){
						return 0;
					}else{
						return -1;
					}
				}
			});
			
			if(roleLoadInfo.getRecordPresentTimeMap().size() > 0){
				long time = System.currentTimeMillis();
				for(Map.Entry<Integer, Long> entry : roleLoadInfo.getRecordPresentTimeMap().entrySet()){
					for(FriendDetailRe re : list){
						//获取今日已经赠送过的玩家Id
						if(re.getRoleId() == entry.getKey() && DateUtil.isSameDay(entry.getValue(), time)){
							re.setCanGive(1);
						}
					}
				}
				
			}
			
			List<FriendDetailRe> blackList = getFriendDetailReList(roleInfo, RoleBlackMap.getBlackRoleIdSet(roleId));
			
			resp.setList(list);
			resp.setCount(list.size());
			
			resp.setBlackList(blackList);
			resp.setBlackListCount(blackList.size());
			resp.setIsCanAdd(roleInfo.getIsCanAddFlag());
			resp.setResult(1);
			
			
		}
		
		
		return resp;
	}

	/**
	 * 删除或者添加好友
	 * @param roleId
	 * @param req
	 * @return
	 */
	public AddOrDelFriendResp requestOperation(int roleId, AddOrDelFriendReq req) {
		AddOrDelFriendResp resp = new AddOrDelFriendResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			if(roleId == req.getRelRoleId()){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_14);
				return resp;
			}
			
			if(req.getFlag() == 0){ //添加好友
				
				resp.setResult(addFriend(roleInfo, req));
				
			}else if(req.getFlag() == 1){ //删除好友
				
				resp.setResult(delFriend(roleInfo, req));
			} else {
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_31);
				return resp;
			}
		}
		resp.setRelRoleId(req.getRelRoleId());
		resp.setFlag(req.getFlag());
		return resp;
	}
	
	
	/**
	 * 删除好友
	 * @param roleInfo
	 * @param req
	 * @return
	 */
	private int delFriend(RoleInfo roleInfo, AddOrDelFriendReq req){
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return ErrorCode.ROLE_RELATION_ERROR_2;
		}
		
		Set<Integer> set = RoleFriendMap.getRoleFriendIdSet(roleInfo.getId());
		if(!set.contains(req.getRelRoleId())){
			return ErrorCode.ROLE_RELATION_ERROR_3;
		}
		
		if(!RoleRelationDao.getInstance().delRoleFriend(roleInfo.getId(), req.getRelRoleId())){
			return ErrorCode.ROLE_RELATION_ERROR_5;
		}
		
		set.remove(req.getRelRoleId());
		
		RoleFriendMap.removeRoleFriendId(roleInfo.getId(), req.getRelRoleId());
		
		//对方也一起解除关系
		if(RoleRelationDao.getInstance().delRoleFriend(req.getRelRoleId(), roleInfo.getId())){
			RoleInfo relRoleInfo = RoleInfoMap.getRoleInfo(req.getRelRoleId());
			
			if(relRoleInfo != null && relRoleInfo.getLoginStatus() == 1){
				String[] str = {String.valueOf(roleInfo.getId()), "3"};
				
				SceneService.sendRoleRefreshMsg(req.getRelRoleId(), SceneService.REFRESH_TYPE_REMOVE_FRIEND, str);
				
			}
			
			RoleFriendMap.removeRoleFriendId(req.getRelRoleId(), roleInfo.getId());
			RoleAddRquestMap.removeRequestRoleId(req.getRelRoleId(), roleInfo.getId()); //确保数据干净
			
		}
		
		sendToMail4ChangeRoleRelation(roleInfo.getId(), req.getRelRoleId(), 1, 1); //通知邮件服务器移除好友
		
		
		GameLogService.insertRoleRelationLog(roleInfo, req.getRelRoleId(), 4, System.currentTimeMillis());
		
		return 1;
	}
	
	/**
	 * 添加好友请求
	 * @param roleInfo
	 * @param req
	 * @return
	 */
	private int addFriend(RoleInfo roleInfo, AddOrDelFriendReq req){
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return ErrorCode.ROLE_RELATION_ERROR_2;
		}
		
		Set<Integer> set = RoleFriendMap.getRoleFriendIdSet(roleInfo.getId());
		
		int relRoleId = req.getRelRoleId();
		
		if(RoleBlackMap.getBlackRoleIdSet(relRoleId).contains(roleInfo.getId())){
			return ErrorCode.ROLE_RELATION_ERROR_18;
		}
		
		if(RoleBlackMap.getBlackRoleIdSet(roleInfo.getId()).contains(relRoleId)){
			return ErrorCode.ROLE_RELATION_ERROR_19;
		}
		
		if(set.contains(req.getRelRoleId())){
			return ErrorCode.ROLE_RELATION_ERROR_4;
		}
		
		if(set.size() >= GameValue.FRIEND_LIST_LIMIT){
			return ErrorCode.ROLE_RELATION_ERROR_13;
		}
		
		if(RoleFriendMap.getRoleFriendIdSet(req.getRelRoleId()).size() >= GameValue.FRIEND_LIST_LIMIT){
			return ErrorCode.ROLE_RELATION_ERROR_32;
		}
		
		RoleInfo relRoleInfo = RoleInfoMap.getRoleInfo(relRoleId);
		if(relRoleInfo == null){
			return ErrorCode.ROLE_RELATION_ERROR_6;
		}
		
		if(relRoleInfo.getIsCanAddFlag() == 1){
			return ErrorCode.ROLE_RELATION_ERROR_36;
		}
		
		set = RoleFriendMap.getRoleFriendIdSet(relRoleId);
		
		if(set.size() >= GameValue.FRIEND_LIST_LIMIT){
			return ErrorCode.ROLE_RELATION_ERROR_32;
		}
		
		if(HeroInfoMap.getMainHeroLv(relRoleId) < GameValue.ROLE_RELATION_LIMIT){
			return ErrorCode.ROLE_RELATION_ERROR_30;
		}
		
		Set<Integer> requestSet = RoleAddRquestMap.getAddRequestRoleIdSet(relRoleId);
		if(requestSet.size() > GameValue.ADD_FRIEND_REQUEST_LIMIT){
			return ErrorCode.ROLE_RELATION_ERROR_33;
		}
		
		if(!requestSet.contains(roleInfo.getId())){
			//之前没有请求过
			if(!RoleRelationDao.getInstance().insertRoleFriend(relRoleId, roleInfo.getId(), 0)){
				return ErrorCode.ROLE_RELATION_ERROR_7;
			}
			
			RoleAddRquestMap.addRequestRoleId(relRoleId, roleInfo.getId());
		}
		
			
		if(relRoleInfo.getLoginStatus() == 1){
			
			//在线 推送给玩家
			SceneService.sendRoleRefreshMsg(relRoleId, SceneService.REFRESH_TYPE_ADD_FRIEND_REQUEST, String.valueOf(roleInfo.getId()));
			//红点推送
			RedPointMgtService.check2PopRedPoint(relRoleId, null, true, RedPointMgtService.LISTENING_FRIENDS_TYPE);
		}
			
		//推荐好友的添加 则从推荐列表中移除并且添加新的
		Set<Integer> recommendSet = FriendRecommendMap.getFriendRecommendSet(roleInfo.getId());
		if(recommendSet.contains(req.getRelRoleId())){
			recommendSet.remove(req.getRelRoleId());
		}
		
		GameLogService.insertRoleRelationLog(roleInfo, req.getRelRoleId(), 0, System.currentTimeMillis());
		

		return 1;
	}

	/**
	 * 获取添加好友请求列表
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetAddRequestResp getAddFriendRequestList(int roleId, GetAddRequestReq req) {
		GetAddRequestResp resp = new GetAddRequestResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
				return resp;
			}
			
			List<FriendDetailRe> list = getFriendDetailReList(roleInfo, RoleAddRquestMap.getAddRequestRoleIdSet(roleInfo.getId()));
			
			if(roleLoadInfo.getRecordPresentTimeMap().size() > 0){
				long time = System.currentTimeMillis();
				for(Map.Entry<Integer, Long> entry : roleLoadInfo.getRecordPresentTimeMap().entrySet()){
					for(FriendDetailRe re : list){
						//获取今日已经赠送过的玩家Id
						if(re.getRoleId() == entry.getKey() && DateUtil.isSameDay(entry.getValue(), time)){
							re.setCanGive(1);
						}
					}
				}
				
			}
			
			
			
			resp.setResult(1);
			resp.setList(list);
			resp.setCount(list.size());
			
		}
		
		return resp;
	}

	/**
	 * 处理添加好友请求
	 * @param roleId
	 * @param req
	 * @return
	 */
	public DisposeAddRequestResp disposeAddRequest(int roleId, DisposeAddRequestReq req) {
		DisposeAddRequestResp resp = new DisposeAddRequestResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
				return resp;
			}
			
			int action = req.getAction();
			int relRoleId = req.getRelRoleId();

			RoleInfo relRoleInfo = RoleInfoMap.getRoleInfo(relRoleId);
			if(relRoleInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_8);
				return resp;
			}
			
			if(action == 0){
				//同意添加
				
				if(!RoleAddRquestMap.getAddRequestRoleIdSet(roleInfo.getId()).contains(relRoleId)){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_10);
					return resp;
				}
				
				if(RoleFriendMap.getRoleFriendIdSet(roleId).contains(relRoleId)){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_28);
					return resp;
				}
				
				if(roleId == relRoleId){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_14);
					return resp;
				}
				
				if(RoleBlackMap.getBlackRoleIdSet(relRoleId).contains(roleInfo.getId())){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_18);
					return resp;
				}
				
				if(RoleFriendMap.getRoleFriendIdSet(relRoleId).size() >= GameValue.FRIEND_LIST_LIMIT){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_32);
					return resp;
				}
				
				if(RoleFriendMap.getRoleFriendIdSet(roleId).size() >= GameValue.FRIEND_LIST_LIMIT){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_13);
					return resp;
				}
				
				if(!RoleRelationDao.getInstance().updateRoleStatus(roleId, relRoleId, 1)){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_11);
					return resp;
				}
				//更新缓存
				RoleAddRquestMap.removeRequestRoleId(roleId, relRoleId);
				RoleFriendMap.addRoleFriendId(roleId, relRoleId);
				
				//对面也要添加
				//判断role是否也发送过添加好友请求给relRole
				if(RoleAddRquestMap.getAddRequestRoleIdSet(relRoleId).contains(roleId)){
					if(RoleRelationDao.getInstance().updateRoleStatus(relRoleId, roleId, 1)){
						if(relRoleInfo.getLoginStatus() == 1){
							// 推送消息
							SceneService.sendRoleRefreshMsg(relRoleId, SceneService.REFRESH_TYPE_FRIEND_LIST, String.valueOf(roleInfo.getId()));
						}
							
						RoleAddRquestMap.removeRequestRoleId(relRoleId, roleId);
						RoleFriendMap.addRoleFriendId(relRoleId, roleId);
					}
					
				}else{
					// 直接添加好友
					if(RoleRelationDao.getInstance().insertRoleFriend(relRoleId, roleId, 1)){
						if(relRoleInfo.getRoleLoadInfo() != null){ //在线更新缓存
								
								RoleFriendMap.addRoleFriendId(relRoleId, roleId);
							
							if(relRoleInfo.getLoginStatus() == 1){
								// 推送消息
								SceneService.sendRoleRefreshMsg(relRoleId, SceneService.REFRESH_TYPE_FRIEND_LIST, String.valueOf(roleInfo.getId()));
							}
						}
						RoleFriendMap.addRoleFriendId(relRoleId, roleId);
					}
				}
				
				sendToMail4ChangeRoleRelation(roleInfo.getId(), req.getRelRoleId(), 0, 1); //通知邮件服务器添加好友
				GameLogService.insertRoleRelationLog(roleInfo, req.getRelRoleId(), 2, System.currentTimeMillis());
				
			}else if(action == 1){
				//拒绝添加
				if(!RoleAddRquestMap.getAddRequestRoleIdSet(roleInfo.getId()).contains(relRoleId)){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_10);
					return resp;
				}
				
				if(!RoleRelationDao.getInstance().delRoleFriend(roleId, relRoleId)){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_12);
					return resp;
				}
				
				RoleAddRquestMap.removeRequestRoleId(roleId, relRoleId);
				
				
				GameLogService.insertRoleRelationLog(roleInfo, req.getRelRoleId(), 1, System.currentTimeMillis());
				
			}else if(action == 2){
				//黑名单
				if(RoleBlackMap.getBlackRoleIdSet(roleId).contains(relRoleId)){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_15);
					return resp;
				}
				
				if(RoleFriendMap.getRoleFriendIdSet(roleId).contains(relRoleId)){
					//好友中直接拉黑
					if(!RoleRelationDao.getInstance().updateRoleStatus(roleId, relRoleId, 2)){
						resp.setResult(ErrorCode.ROLE_RELATION_ERROR_17);
						return resp;
					}
					
					RoleFriendMap.removeRoleFriendId(roleId, relRoleId);
					
					//删除对方好友列表 一方拉黑 两方关系均解除
					
					//对方也一起解除关系
					if(RoleFriendMap.getRoleFriendIdSet(relRoleId).contains(roleId)){
						if(RoleRelationDao.getInstance().delRoleFriend(req.getRelRoleId(), roleInfo.getId())){
							if(relRoleInfo.getLoginStatus() == 1){
								String[] str = {String.valueOf(roleInfo.getId()), "3"};
								
								SceneService.sendRoleRefreshMsg(req.getRelRoleId(), SceneService.REFRESH_TYPE_REMOVE_FRIEND, str);
							}
								
							
							RoleAddRquestMap.removeRequestRoleId(req.getRelRoleId(), roleId); //确保数据干净
							RoleFriendMap.removeRoleFriendId(relRoleId, roleId);
						}
					}
					
					
				}else if(RoleAddRquestMap.getAddRequestRoleIdSet(roleId).contains(relRoleId)){
					//发送请求的 直接拉黑
					if(!RoleRelationDao.getInstance().updateRoleStatus(roleId, relRoleId, 2)){
						resp.setResult(ErrorCode.ROLE_RELATION_ERROR_17);
						return resp;
					}
					RoleAddRquestMap.removeRequestRoleId(roleId, relRoleId);
					
					//判断对方请求中是否有好友请求 有则删除
					if(RoleAddRquestMap.getAddRequestRoleIdSet(relRoleId).contains(roleId)){
						if(RoleRelationDao.getInstance().delRoleFriend(relRoleId, roleId)){
							RoleAddRquestMap.removeRequestRoleId(relRoleId, roleId);
							
							if(relRoleInfo.getLoginStatus() == 1){
								String[] str = {String.valueOf(roleInfo.getId()), "4"};
								
								SceneService.sendRoleRefreshMsg(req.getRelRoleId(), SceneService.REFRESH_TYPE_REMOVE_FRIEND, str);
							}
						}
					}
					
				}else{
					//非好友拉黑
					if(!RoleRelationDao.getInstance().insertRoleFriend(roleId, relRoleId, 2)){
						resp.setResult(ErrorCode.ROLE_RELATION_ERROR_17);
						return resp;
					}
					
				}
				
				RoleBlackMap.addBlackRoleId(roleId, relRoleId);
				
				sendToMail4ChangeRoleRelation(roleInfo.getId(), req.getRelRoleId(), 0, 0); //通知邮件服务器加入黑名单
				
				
				GameLogService.insertRoleRelationLog(roleInfo, req.getRelRoleId(), 3, System.currentTimeMillis());
				
				
			}else if(action == 3){
				//移除黑名单
				if(!RoleBlackMap.getBlackRoleIdSet(roleId).contains(relRoleId)){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_16);
					return resp;
				}
				
				
				if(!RoleRelationDao.getInstance().delRoleFriend(roleId, relRoleId)){
					resp.setResult(ErrorCode.ROLE_RELATION_ERROR_17);
					return resp;
				}
				
				RoleBlackMap.removeBlackRoleId(roleId, relRoleId);
				
				
				sendToMail4ChangeRoleRelation(roleInfo.getId(), req.getRelRoleId(), 1, 0); //通知邮件服务器移除黑名单
		
				GameLogService.insertRoleRelationLog(roleInfo, req.getRelRoleId(), 5, System.currentTimeMillis());
				
			
			} else {
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_31);
				return resp;
			}
			
			
			resp.setAction(req.getAction());
			resp.setRelRoleId(relRoleId);
			resp.setResult(1);
		}
		RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true,
				RedPointMgtService.LISTENING_FRIENDS_TYPE);
		
		return resp;
	}

	/**
	 * 搜索玩家
	 * @param roleId
	 * @param req
	 * @return
	 */
	public SearchRoleInfoResp searchRoleInfo4Add(int roleId, SearchRoleInfoReq req) {
		SearchRoleInfoResp resp = new SearchRoleInfoResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
				return resp;
			}
			
			RoleInfo relRoleInfo = RoleInfoMap.getRoleInfoByName(req.getRoleName());
			if(relRoleInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_27);
				return resp;
			}
			
			FriendDetailRe re = new FriendDetailRe();
			
			re.setRoleId(relRoleInfo.getId());
			re.setRoleName(relRoleInfo.getRoleName());
			re.setFightValue(relRoleInfo.getFightValue());
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(relRoleInfo);
			if(heroInfo != null){
				re.setLevel(heroInfo.getHeroLevel());
				re.setHeroNo(heroInfo.getHeroNo());
			}
			
			re.setStatus(relRoleInfo.getLoginStatus());
			resp.setResult(1);
			
			resp.getList().add(re);
			resp.setCount(resp.getList().size());
			
			if(resp.getCount() <= 0){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_29);
			}
		}
		
		return resp;
	}

	/**
	 * 好友推荐
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RecommendFriend4AddResp recommendFriend4Add(int roleId, RecommendFriend4AddReq req) {
		RecommendFriend4AddResp resp = new RecommendFriend4AddResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
				return resp;
			}
			
			
			if(req.getAction() == 0){
				//获取原本的推荐
				if(FriendRecommendMap.getFriendRecommendSet(roleId).size() <= 0){
					//之前没有推荐过 或者服务器重启 缓存数据消失
					
					HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
					if(heroInfo != null){
						
						Set<Integer> recommendSet = getFriendRecommendSet(roleInfo, heroInfo.getHeroLevel(), null);

						roleInfo.setFriendRecommendTime(System.currentTimeMillis()); //设置获得刷新时间
						
						FriendRecommendMap.addFriendRecommendSet(roleId, recommendSet); //添加缓存
						
						
						List<FriendDetailRe> list = getFriendDetailReList(roleInfo, recommendSet);
						
						resp.setList(list);
						resp.setCount(list.size());
					}

				}else{
					//之前有推荐过
					if((System.currentTimeMillis() - roleInfo.getFriendRecommendTime()) > (12 * 60 * 60 * 1000)){
						//间隔大于12个小时就刷新
						//刷新列表
						Set<Integer> cacheSet = FriendRecommendMap.getFriendRecommendSet(roleId);
						
						HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
						if(heroInfo != null){
							
							Set<Integer> recommendSet = getFriendRecommendSet(roleInfo, heroInfo.getHeroLevel(), cacheSet);
							
							roleInfo.setFriendRecommendTime(System.currentTimeMillis()); //设置获得刷新时间
							
							FriendRecommendMap.addFriendRecommendSet(roleId, recommendSet);//添加缓存
							
							List<FriendDetailRe> list = getFriendDetailReList(roleInfo, recommendSet);
							
							resp.setList(list);
							resp.setCount(list.size());
						}
						
					}else{
						//获取之前的列表
						Set<Integer> cacheSet = FriendRecommendMap.getFriendRecommendSet(roleId);
						
						
						List<FriendDetailRe> list = getFriendDetailReList(roleInfo, cacheSet);
						
						resp.setList(list);
						resp.setCount(list.size());
						
					}
				}
				
			}else if(req.getAction() == 1){
				//刷新推荐
				Set<Integer> cacheSet = FriendRecommendMap.getFriendRecommendSet(roleId);
				
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if(heroInfo != null){
					
					Set<Integer> recommendSet = getFriendRecommendSet(roleInfo, heroInfo.getHeroLevel(), cacheSet);
					
					roleInfo.setFriendRecommendTime(System.currentTimeMillis()); //设置获得刷新时间
					
					FriendRecommendMap.addFriendRecommendSet(roleId, recommendSet); //添加缓存
					
					List<FriendDetailRe> list = getFriendDetailReList(roleInfo, recommendSet);
					
					resp.setList(list);
					resp.setCount(list.size());
				}				
				
			} 
			else
			{
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_31);
				return resp;
			}
			
			resp.setAction(req.getAction());
			resp.setResult(1);
		}
		
		
		return resp;
	}
	
	/**
	 * 获取好友推荐
	 * @param roleLv 玩家等级
	 * @param cacheSet 之前推荐的好友缓存
	 * @return
	 */
	private Set<Integer> getFriendRecommendSet(RoleInfo roleInfo, int roleLv, Set<Integer> cacheSet){
		Set<Integer> recommendSet = new HashSet<Integer>();
			
		if(cacheSet == null){
			cacheSet = new HashSet<Integer>();
		}

		RoleInfo reRoleInfo = null;
		
		
		for(Map.Entry<Integer, RoleInfo> entry : RoleInfoMap.getRoleInfoEntrySet()){
			if(entry.getKey() == roleInfo.getId()){
				continue;
			}
			
			reRoleInfo = RoleInfoMap.getRoleInfo(entry.getKey());
			if(reRoleInfo == null){
				continue;
			}
			
			if(reRoleInfo.getLoginStatus() == 0){
				continue;
			}
			
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(reRoleInfo);
			if(heroInfo == null){
				continue;
			}
			
			int min = roleLv - GameValue.RECOMMEND_FRIEND_RANGE <= GameValue.ROLE_RELATION_LIMIT ? GameValue.ROLE_RELATION_LIMIT : roleLv - GameValue.RECOMMEND_FRIEND_RANGE;
			int max = roleLv + GameValue.RECOMMEND_FRIEND_RANGE;
			
			
			if(heroInfo.getHeroLevel() >= min && heroInfo.getHeroLevel() <= max){
				if(!cacheSet.contains(entry.getKey()) && !RoleFriendMap.getRoleFriendIdSet(roleInfo.getId()).contains(entry.getKey())){
					//缓存列表中不含有 获取跟之前不一样的玩家 && 去除好友
					recommendSet.add(entry.getKey());
				}
			}
			
			if(recommendSet.size() >= GameValue.RECOMMEND_FRIEND_NUM){
				break;
			}

		}
		
		if(recommendSet.size() < GameValue.RECOMMEND_FRIEND_NUM && RoleInfoMap.getRoleInfoEntrySet().size() > GameValue.RECOMMEND_FRIEND_NUM){
			//小于推荐的数量则随机补足
//			int i = 0;
			
			RolePoint rolePoint = roleInfo.getRolePoint();
			if(rolePoint == null){
				return recommendSet;
			}
			
			Map<Integer, RolePoint> map = SceneInfoMap.getSceneRoleMap(rolePoint.getNo(), rolePoint.getSceneId());
			
			if(map == null){
				//只有自己在
				return recommendSet;
			}
			
			if(map.size() <= 1 && rolePoint.getSceneId() > 1){
				//如果场景内只有自己,隔壁场景去取
				map = SceneInfoMap.getSceneRoleMap(rolePoint.getNo(), rolePoint.getSceneId() - 1);
			}
			
			if(map == null || map.size() <= 0){
				//其他场景也没有人
				return recommendSet;
			}
			
			for(Integer roleId : map.keySet()){
				reRoleInfo = RoleInfoMap.getRoleInfo(roleId);
				
				if(reRoleInfo == null 
						|| reRoleInfo.getLoginStatus() == 0
						|| roleId == roleInfo.getId()
						|| RoleFriendMap.getRoleFriendIdSet(roleInfo.getId()).contains(roleId)
						|| cacheSet.contains(roleId)){
					//过滤掉自己，未在线的，上批推荐过的，已经是好友的, 上次推荐过的
					continue;
				}
				
				
				recommendSet.add(roleId);
				
				if(recommendSet.size() >= GameValue.RECOMMEND_FRIEND_NUM){
					break;
				}
			}
			
			
//			while(recommendSet.size() < GameValue.RECOMMEND_FRIEND_NUM){
//				if(i > 20){ //避免死循环
//					break;
//				}
//				
//				i++;
				
//				int randamNum = RandomUtil.getRandom(0, RoleInfoMap.getRoleInfoEntrySet().size() - 1);
//				int randamNum = RandomUtil.getRandom(1, RoleInfoMap.getRoleInfoEntrySet().size());
				//randamNum 就是角色Id
//				if(recommendSet.contains(randamNum)
//					|| RoleFriendMap.getRoleFriendIdSet(roleInfo.getId()).contains(randamNum)
//					|| randamNum == roleInfo.getId()
//					|| RoleInfoMap.getRoleInfo(randamNum) == null){
//					continue;
//				}

				
				
//				recommendSet.add(randamNum);
				
//			}
			
		}
		
		return recommendSet;
	}
	
	/**
	 * 获取详细信息
	 * @param roleInfo
	 * @param set roleId
	 * @return
	 */
	private static List<FriendDetailRe> getFriendDetailReList(RoleInfo roleInfo, Set<Integer> set){
		List<FriendDetailRe> list = new ArrayList<FriendDetailRe>();
		
		if(set == null || set.size() <= 0){
			return list;
		}
		
		FriendDetailRe re = null;
		RoleInfo relRoleInfo = null;
		HeroInfo heroInfo = null;
		
		for(Integer id : set){
			relRoleInfo = RoleInfoMap.getRoleInfo(id);
			if(relRoleInfo == null){
				continue;
			}
			
			re = new FriendDetailRe();
			re.setFightValue(relRoleInfo.getFightValue());
			heroInfo =  HeroInfoMap.getMainHeroInfo(relRoleInfo);
			if(heroInfo != null){
				re.setLevel(heroInfo.getHeroLevel());
				re.setHeroNo(heroInfo.getHeroNo());
			}
			
			
			re.setRoleId(id);
			re.setRoleName(relRoleInfo.getRoleName());
			re.setStatus(relRoleInfo.getLoginStatus());
			
			list.add(re);
		}
		
		return list;
	}
	
	/**
	 * 同步到邮件服务器
	 * @param roleId 角色ID
	 * @param relRoleId 改变关系的角色ID
	 * @param flag // 0-加入 1-移除 
	 * @param relation // 0-黑名单 1-好友
	 */
	public static void sendToMail4ChangeRoleRelation(int roleId, int relRoleId, int flag, int relation) {

		IoSession session = Client.getInstance().getSession(ServerName.MAIL_SERVER_NAME);
		if (session != null && session.isConnected()) {
			SysBlackListReq req = new SysBlackListReq();
			
			req.setRoleId(roleId);
			req.setRelRoleId(relRoleId + "");
			req.setFlag(flag);
			req.setRelation(relation);
			
			Message message = new Message();
			GameMessageHead header = new GameMessageHead();
			header.setUserID0(roleId);
			header.setMsgType(Command.SYN_ROLE_RELATION);
			message.setHeader(header);
			message.setBody(req);
			session.write(message);
		}
	}

	/**
	 * 获取好友赠送的精力
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetPresentEnergyListResp getPresentEnergyInfo(int roleId, GetPresentEnergyListReq req) {
		GetPresentEnergyListResp resp = new GetPresentEnergyListResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
				return resp;
			}
			
			long now = System.currentTimeMillis();
			long time = roleLoadInfo.getQueryEnergyInfoTime() == null ? 0 : roleLoadInfo.getQueryEnergyInfoTime().getTime();
			
			// 重置可领取的好友赠送精力次数
			if (!DateUtil.isSameDay(now, time)){
				//重置次数和时间
				if(RoleDAO.getInstance().updateGetPresentEnergyTimes(roleInfo.getId(), now)){
					roleLoadInfo.setGetPresentEnergyTimes((byte)0);
					roleLoadInfo.setQueryEnergyInfoTime(new Timestamp(now));
				}
			}
			
			Map<Integer, PresentEnergyInfo> map = roleLoadInfo.getPresentEnergyMap();
			List<FriendDetailRe> list = new ArrayList<FriendDetailRe>();
			
			if(map != null && map.size() > 0){
				FriendDetailRe re = null;
				RoleInfo relRoleInfo = null;
				PresentEnergyInfo info = null;
				HeroInfo heroInfo = null;
				for(Map.Entry<Integer, PresentEnergyInfo> entry : map.entrySet()){
					info = entry.getValue();
					if(info == null){
						continue;
					}
					
					relRoleInfo = RoleInfoMap.getRoleInfo(info.getRelRoleId());
					if(relRoleInfo == null){
						continue;
					}
					
					re = new FriendDetailRe();
					re.setId(info.getId());
					
					re.setFightValue(relRoleInfo.getFightValue());
					re.setRoleId(info.getRelRoleId());
					re.setRoleName(relRoleInfo.getRoleName());
					re.setStatus(relRoleInfo.getLoginStatus());
					
					heroInfo = HeroInfoMap.getMainHeroInfo(relRoleInfo);
					if(heroInfo != null){
						re.setHeroNo(heroInfo.getHeroNo());
						re.setLevel(heroInfo.getHeroLevel());
					}
					
					re.setApplyTime(info.getPresentDate().getTime());
					
					list.add(re);
				}
				
			}
			
			//红点推送
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_FRIENDS_TYPE);
			
			resp.setGetTimes(roleLoadInfo.getGetPresentEnergyTimes());
			resp.setResult(1);
			resp.setList(list);
			resp.setCount(list.size());
			
		}
		
		return resp;
	}

	/**
	 * 赠送精力
	 * @param roleId
	 * @param req
	 * @return
	 */
	public PresentEnergyResp presentEnergy2Role(int roleId, PresentEnergyReq req) {
		PresentEnergyResp resp = new PresentEnergyResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
			return resp;
		}
		
		int relRoleId = req.getRelRoleId();
		
		RoleInfo relRoleInfo = RoleInfoMap.getRoleInfo(relRoleId);
		if(relRoleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_22);
			return resp;
		}
		
		if(!RoleFriendMap.getRoleFriendIdSet(roleInfo.getId()).contains(relRoleId)){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_23);
			return resp;
		}
		
		long presentTime = System.currentTimeMillis();

		Map<Integer, Long> map = roleLoadInfo.getRecordPresentTimeMap();
		if(map.containsKey(relRoleId)){
			long time = map.get(relRoleId);
			if(DateUtil.isSameDay(time, presentTime)){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_20);
				return resp;
			}
		}
		
		PresentEnergyInfo info = new PresentEnergyInfo();
		info.setRoleId(relRoleId);
		info.setRelRoleId(roleId);
		info.setPresentDate(new Timestamp(presentTime));
		
		if(!PresentEnergyDao.getInstence().insertPresentEnegerInfo(info)){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_21);
			return resp;
		}
		
		map.put(relRoleId, presentTime); //赠送缓存
		
//		PresentEnergyInfoMap.addPresentEnergyInfo(relRoleId, info);
		
		if(relRoleInfo.getRoleLoadInfo() != null){
			//在线 推送消息 更新缓存
			synchronized(relRoleInfo){
				relRoleInfo.getRoleLoadInfo().getPresentEnergyMap().put(info.getId(), info);
				//红点推送
				RedPointMgtService.check2PopRedPoint(relRoleInfo.getId(), null, true, RedPointMgtService.LISTENING_FRIENDS_TYPE);
			}
			
			if(relRoleInfo.getLoginStatus() == 1){
				String[] str = {roleId + "", info.getId() + ""};
				SceneService.sendRoleRefreshMsg(relRoleId, SceneService.REFRESH_TYPE_PRESENT_ENERGY, str);
			}
		}
		
		QuestService.checkQuest(roleInfo, ActionType.action471.getType(), null, true, true);
		
		resp.setRoleId(relRoleId);
		resp.setResult(1);
		
		GameLogService.insertRoleRelationLog(roleInfo, req.getRelRoleId(), 6, System.currentTimeMillis());
		
		return resp;
	}

	/**
	 * 获取好友赠送的精力
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetEnergy2RoleSelfResp getPresentEnergy2RoleSelf(int roleId, GetEnergy2RoleSelfReq req) {
		GetEnergy2RoleSelfResp resp = new GetEnergy2RoleSelfResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		synchronized(roleInfo){
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if(roleLoadInfo == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
				return resp;
			}
			
			long now = System.currentTimeMillis();
			long time = roleLoadInfo.getQueryEnergyInfoTime() == null ? 0 : roleLoadInfo.getQueryEnergyInfoTime().getTime();
			
			// 重置可领取的好友赠送精力次数
			if (!DateUtil.isSameDay(now, time)){
				//重置次数和时间
				if(RoleDAO.getInstance().updateGetPresentEnergyTimes(roleInfo.getId(), now)){
					roleLoadInfo.setGetPresentEnergyTimes((byte)0);
					roleLoadInfo.setQueryEnergyInfoTime(new Timestamp(now));
				}
			}
			
			PresentEnergyInfo info = roleLoadInfo.getPresentEnergyMap().get(req.getId());
			if(info == null){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_24);
				return resp;
			}
			
			if(roleLoadInfo.getGetPresentEnergyTimes() >= GameValue.GET_ENERGY_MAX_NUM_PER_DAY){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_25);
				return resp;
			}
			
			int times = roleLoadInfo.getGetPresentEnergyTimes() + 1;
			
			if(!PresentEnergyDao.getInstence().deletePresentEnergyById(req.getId())){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_26);
				return resp;
			}
			
			roleLoadInfo.getPresentEnergyMap().remove(req.getId());
			
			
			if(!RoleDAO.getInstance().updateGetPresentEnergyTimesById(roleId, times)){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_26);
				return resp;
			}
			
			roleLoadInfo.setGetPresentEnergyTimes((byte) times);
			
			RoleService.addRoleRoleResource(ActionType.action446.getType(), roleInfo, ConditionType.TYPE_ENERGY, GameValue.PRESENT_ENERGY_NUM,null);
			
			//红点推送
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_FRIENDS_TYPE);
			
			GameLogService.insertRoleRelationLog(roleInfo, info.getRelRoleId(), 7, System.currentTimeMillis());
			
			
			resp.setResult(1);
			resp.setId(req.getId());
			resp.setRoleId(info.getRoleId());
			
		}
		return resp;
	}

	/**
	 * 一键赠送或者领取精力
	 * @param roleId
	 * @param req
	 * @return
	 */
	public OneKeyEnergyResp oneKeyEnergy(int roleId, OneKeyEnergyReq req) {
		OneKeyEnergyResp resp = new OneKeyEnergyResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
			return resp;
		}
		
		if(req.getFlag() == 0){
			resp.setResult(oneKeyGetEnergy(roleInfo, resp));
			
		}else if(req.getFlag() == 1){
			resp.setResult(oneKeyPresentEnergy(roleInfo));
			
		}else{
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_31);
		}
		
		resp.setFlag(req.getFlag());
		return resp;
	}

	/**
	 * 一键赠送精力
	 * @param roleInfo
	 * @return
	 */
	private int oneKeyPresentEnergy(RoleInfo roleInfo) {
		List<RoleInfo> list = new ArrayList<RoleInfo>(); //可赠送的角色
		Map<Integer, PresentEnergyInfo> presentMap = new HashMap<Integer, PresentEnergyInfo>();
		
		synchronized(roleInfo){
			Set<Integer> set = RoleFriendMap.getRoleFriendIdSet(roleInfo.getId());
			long presentTime = System.currentTimeMillis();

			Map<Integer, Long> map = roleInfo.getRoleLoadInfo().getRecordPresentTimeMap();
			List<PresentEnergyInfo> infoList = new ArrayList<PresentEnergyInfo>();
			RoleInfo relRoleInfo = null;
			PresentEnergyInfo info = null;
			long lastPresentTime = 0;
			
			for(Integer relRoleId : set){
				relRoleInfo = RoleInfoMap.getRoleInfo(relRoleId);
				if(relRoleInfo == null){
					continue;
				}
				
				if(map.containsKey(relRoleId)){
					lastPresentTime = map.get(relRoleId);
					if(DateUtil.isSameDay(lastPresentTime, presentTime)){
						continue;
					}
				}
				
				list.add(relRoleInfo); //可以赠送的角色
				
				info = new PresentEnergyInfo();
				info.setRoleId(relRoleId);
				info.setRelRoleId(roleInfo.getId());
				info.setPresentDate(new Timestamp(presentTime));
				infoList.add(info);
			}
			
			
			if(!PresentEnergyDao.getInstence().batchInsertPresentEnegerInfo(infoList)){
				return ErrorCode.ROLE_RELATION_ERROR_21;
			}
			
			for(PresentEnergyInfo pInfo : infoList){
				//放入Map中 用于直接用角色Id获取Info
				presentMap.put(pInfo.getRoleId(), pInfo);
			}
			
			infoList.clear();
			
			if (list.size() > 0) {
				for(RoleInfo rRoleInfo : list){
					//记录赠送时间
					map.put(rRoleInfo.getId(), presentTime);
				}
				
				// 检测赠送精力任务
				QuestService.checkQuest(roleInfo, ActionType.action471.getType(), null, true, true);
			}
		}
		
		if(list.size() > 0){
			PresentEnergyInfo info = null;
			for(RoleInfo relRoleInfo : list){
				if(relRoleInfo.getRoleLoadInfo() != null){
					info = presentMap.get(relRoleInfo.getId());
					if(info == null){
						continue;
					}
					
					//在线 推送消息 更新缓存
					synchronized(relRoleInfo){
						relRoleInfo.getRoleLoadInfo().getPresentEnergyMap().put(info.getId(), info);
						//红点推送
						RedPointMgtService.check2PopRedPoint(relRoleInfo.getId(), null, true, RedPointMgtService.LISTENING_FRIENDS_TYPE);
					}
					
					if(relRoleInfo.getLoginStatus() == 1){
						String[] str = {roleInfo.getId() + "", info.getId() + ""};
						SceneService.sendRoleRefreshMsg(relRoleInfo.getId(), SceneService.REFRESH_TYPE_PRESENT_ENERGY, str);
					}
				}
			}
		}
		
		GameLogService.insertRoleRelationLog(roleInfo, 0, 8, System.currentTimeMillis());
		
		return 1;
	}

	/**
	 *  一键领取精力
	 * @param roleInfo
	 * @return
	 */
	private int oneKeyGetEnergy(RoleInfo roleInfo, OneKeyEnergyResp resp) {
		synchronized(roleInfo){
			long now = System.currentTimeMillis();
			long time = roleInfo.getRoleLoadInfo().getQueryEnergyInfoTime() == null ? 0 :  roleInfo.getRoleLoadInfo().getQueryEnergyInfoTime().getTime();
			
			// 重置可领取的好友赠送精力次数
			if (!DateUtil.isSameDay(now, time)){
				//重置次数和时间
				if(RoleDAO.getInstance().updateGetPresentEnergyTimes(roleInfo.getId(), now)){
					roleInfo.getRoleLoadInfo().setGetPresentEnergyTimes((byte)0);
					roleInfo.getRoleLoadInfo().setQueryEnergyInfoTime(new Timestamp(now));
				}
			}
			
			if(roleInfo.getRoleLoadInfo().getGetPresentEnergyTimes() >= GameValue.GET_ENERGY_MAX_NUM_PER_DAY){
				return ErrorCode.ROLE_RELATION_ERROR_25;
			}
			
			Map<Integer, PresentEnergyInfo> map = roleInfo.getRoleLoadInfo().getPresentEnergyMap();
			
			List<PresentEnergyInfo> list = new ArrayList<PresentEnergyInfo>();
			
			list.addAll(map.values());
			
			Collections.sort(list, new Comparator<PresentEnergyInfo>() {
				@Override
				public int compare(PresentEnergyInfo o1, PresentEnergyInfo o2) {
					long time1 = o1.getPresentDate() == null ? 0 : o1.getPresentDate().getTime();
					long time2 = o2.getPresentDate() == null ? 0 : o2.getPresentDate().getTime();
					if(time1 > time2){
						return 1;
					}else if(time1 < time2){
						return -1;
					}else{
						return 0;
					}
				}
			});
			
			if(list.size() > 0){
				Set<Integer> canPrsentIdSet = new HashSet<Integer>(); //删除用
				int count = 0; //实际领取的次数
				
				if(GameValue.GET_ENERGY_MAX_NUM_PER_DAY - roleInfo.getRoleLoadInfo().getGetPresentEnergyTimes() > list.size()){
					count = list.size();
				}else{
					count = GameValue.GET_ENERGY_MAX_NUM_PER_DAY - roleInfo.getRoleLoadInfo().getGetPresentEnergyTimes();
				}
				
				PresentEnergyInfo info = null;
				for(int i = 0; i <= count - 1; i++){
					info = list.get(i);
					if(info == null){
						continue;
					}
					
					canPrsentIdSet.add(info.getId());
					
				}
				
				if(!PresentEnergyDao.getInstence().batchDeletePresentEnergyById(canPrsentIdSet)){
					return ErrorCode.ROLE_RELATION_ERROR_26;
				}
				
				StringBuffer sb = new StringBuffer();
				
				for(Integer id : canPrsentIdSet){
					map.remove(id);
					sb.append(id).append(":");
				}
				
				
				int times = roleInfo.getRoleLoadInfo().getGetPresentEnergyTimes() + count;
				
				if(!RoleDAO.getInstance().updateGetPresentEnergyTimesById(roleInfo.getId(), times)){
					return ErrorCode.ROLE_RELATION_ERROR_26;
				}
				
				roleInfo.getRoleLoadInfo().setGetPresentEnergyTimes((byte)times);
				
				RoleService.addRoleRoleResource(ActionType.action477.getType(), roleInfo, ConditionType.TYPE_ENERGY, GameValue.PRESENT_ENERGY_NUM * count,null);
				
				//红点推送
				RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_FRIENDS_TYPE);

				
				if(sb.toString().endsWith(":")){
					resp.setIds(sb.substring(0, sb.length() - 1).toString());
				}
			}
			
			GameLogService.insertRoleRelationLog(roleInfo, 0, 9, System.currentTimeMillis());
			
		}
		
		
		return 1;
	}

	/**
	 * 一键添加或者拒绝
	 * @param roleId
	 * @param req
	 * @return
	 */
	public OneKeyOperationResp oneKeyOperation(int roleId, OneKeyOperationReq req) {
		OneKeyOperationResp resp = new OneKeyOperationResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
			return resp;
		}
		
		if(req.getFlag() == 0){
			resp.setResult(oneKeyAddRoles(roleInfo, resp));
			
		}else if(req.getFlag() == 1){
			resp.setResult(oneKeyRefuseRoles(roleInfo));
			
		}else{
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_31);
		}
		
		resp.setFlag(req.getFlag());
		
		return resp;
	}

	/**
	 * 一键拒绝添加
	 * @param roleInfo
	 * @return
	 */
	private int oneKeyRefuseRoles(RoleInfo roleInfo) {
		synchronized(roleInfo){
			Set<Integer> set = RoleAddRquestMap.getAddRequestRoleIdSet(roleInfo.getId());
			if(set.size() <= 0){
				return 1;
			}
			
			if(!RoleRelationDao.getInstance().delRoleFriendByStatus(roleInfo.getId(), 0)){
				return ErrorCode.ROLE_RELATION_ERROR_34;
			}
			
			RoleAddRquestMap.removeAddRequestRoleIdSet(roleInfo.getId());

		}
		
		//红点推送
		RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_FRIENDS_TYPE);
		
		GameLogService.insertRoleRelationLog(roleInfo, 0, 10, System.currentTimeMillis());
		return 1;
	}

	/**
	 * 一键添加好友
	 * @param roleInfo
	 * @param resp
	 * @return
	 */
	private int oneKeyAddRoles(RoleInfo roleInfo, OneKeyOperationResp resp) {
		synchronized(roleInfo){
			Set<Integer> friendSet = RoleFriendMap.getRoleFriendIdSet(roleInfo.getId());
			
			Set<Integer> requestSet = RoleAddRquestMap.getAddRequestRoleIdSet(roleInfo.getId());
			
			if(requestSet.size() <= 0){
				return 1;
			}
			
			if(friendSet.size() >= GameValue.FRIEND_LIST_LIMIT){
				//达到了上限
				return ErrorCode.ROLE_RELATION_ERROR_13;
			}

			int canAddNum = 0;

			if(requestSet.size() > GameValue.FRIEND_LIST_LIMIT - friendSet.size()){
				//请求列表的数量大于可添加的数量, 只能添加可添加的
				canAddNum = GameValue.FRIEND_LIST_LIMIT - friendSet.size();
				
			}else{
				//可以全部添加进去
				canAddNum = requestSet.size();
			}
			
			RoleInfo relRoleInfo = null;
			
			List<RoleInfo> list = new ArrayList<RoleInfo>();
			
			for(Integer roleId : requestSet){
				relRoleInfo = RoleInfoMap.getRoleInfo(roleId);
				if(relRoleInfo == null){
					continue;
				}
				
				if(friendSet.contains(roleId)){
					//已经是好友了
					continue;
				}
				
				if(RoleBlackMap.getBlackRoleIdSet(roleId).contains(roleInfo.getId())){
					continue;
				}
				
				list.add(relRoleInfo);
				
			}
			
			Collections.sort(list, new Comparator<RoleInfo>() {

				@Override
				public int compare(RoleInfo o1, RoleInfo o2) {
					int h1 = HeroInfoMap.getMainHeroLv(o1.getId());
					int h2 = HeroInfoMap.getMainHeroLv(o2.getId());
					if(h1 < h2){
						return 1;
					}else if(h1 > h2){
						return -1;
					}
					
					return 0;
				}
			});
			
			
			Set<Integer> agreeSet = new HashSet<Integer>();
			
			if(canAddNum > list.size()){
				//避免数组越界异常
				canAddNum = list.size();
			}
			
			for(int i = 0; i <= canAddNum - 1; i++){
				relRoleInfo = list.get(i);
				if(relRoleInfo == null){
					continue;
				}
				
				agreeSet.add(relRoleInfo.getId());
			}
			
			StringBuffer sb = new StringBuffer();
			for(Integer relRoleId : agreeSet){
				sb.append(relRoleId).append(":");
				
				if(RoleRelationDao.getInstance().updateRoleStatus(roleInfo.getId(), relRoleId, 1)){
					//更新缓存
					RoleAddRquestMap.removeRequestRoleId(roleInfo.getId(), relRoleId);
					RoleFriendMap.addRoleFriendId(roleInfo.getId(), relRoleId);
					
					//对面也要添加
					//判断role是否也发送过添加好友请求给relRole
					if(RoleAddRquestMap.getAddRequestRoleIdSet(relRoleId).contains(roleInfo.getId())){
						if(RoleRelationDao.getInstance().updateRoleStatus(relRoleId, roleInfo.getId(), 1)){
							if(relRoleInfo.getLoginStatus() == 1){
								// 推送消息
								SceneService.sendRoleRefreshMsg(relRoleId, SceneService.REFRESH_TYPE_FRIEND_LIST, String.valueOf(roleInfo.getId()));
							}
							
							RoleAddRquestMap.removeRequestRoleId(relRoleId, roleInfo.getId());
							RoleFriendMap.addRoleFriendId(relRoleId, roleInfo.getId());
						}
						
					}else{
						// 直接添加好友
						if(RoleRelationDao.getInstance().insertRoleFriend(relRoleId, roleInfo.getId(), 1)){
							if(relRoleInfo.getRoleLoadInfo() != null){ //在线更新缓存
								
								RoleFriendMap.addRoleFriendId(relRoleId, roleInfo.getId());
								
								if(relRoleInfo.getLoginStatus() == 1){
									// 推送消息
									SceneService.sendRoleRefreshMsg(relRoleId, SceneService.REFRESH_TYPE_FRIEND_LIST, String.valueOf(roleInfo.getId()));
								}
							}
							RoleFriendMap.addRoleFriendId(relRoleId, roleInfo.getId());
						}
					}
					
				}
			}
			
			resp.setRoleIds(sb.substring(0, sb.length() - 1).toString());
			
		}
		
		GameLogService.insertRoleRelationLog(roleInfo, 0, 11, System.currentTimeMillis());
		
		return 1;
	}

	/**
	 * 设置角色是否允许被添加
	 * @param roleId
	 * @param req
	 * @return
	 */
	public SetCanFlagResp setCanAddFlag(int roleId, SetCanFlagReq req) {
		SetCanFlagResp resp = new SetCanFlagResp();
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		
		if(roleInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_1);
			return resp;
		}
		
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			resp.setResult(ErrorCode.ROLE_RELATION_ERROR_2);
			return resp;
		}
		
		synchronized(roleInfo){
			int flag = roleInfo.getIsCanAddFlag() == 0 ? 1 : 0;
			
			if(!RoleDAO.getInstance().updateRoleIsCanAddFlag(roleId, flag)){
				resp.setResult(ErrorCode.ROLE_RELATION_ERROR_35);
				return resp;
			}
			
			roleInfo.setIsCanAddFlag(flag);
		}
		
		resp.setResult(1);
		return resp;
	}
	
}
