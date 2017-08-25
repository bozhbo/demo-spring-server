package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.snail.webgame.game.info.ClubEventInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;

public class RoleClubMemberInfoMap {
	//clubId - map<roleId info>  通过公会ID取得所有的公会会员
	private static Map<Integer, Map<Integer, RoleClubMemberInfo>> map = new ConcurrentHashMap<Integer, Map<Integer, RoleClubMemberInfo>>();
	//clubId - list
	private static Map<Integer, List<ClubEventInfo>> eventMap = new ConcurrentHashMap<Integer, List<ClubEventInfo>>();
	private static Map<Integer, List<ClubEventInfo>> roleEventMap = new ConcurrentHashMap<Integer, List<ClubEventInfo>>();
	
	public static void addRoleClubMemberInfo(RoleClubMemberInfo info){
		if(!map.containsKey(info.getClubId())){
			Map<Integer, RoleClubMemberInfo> m = new ConcurrentHashMap<Integer, RoleClubMemberInfo>();
			map.put(info.getClubId(), m);
		}
		
		map.get(info.getClubId()).put(info.getRoleId(), info);
		
	}
	
	public static RoleClubMemberInfo getRoleClubMemberInfo(int clubId, int roleId){
		if(map.get(clubId) != null){
			return map.get(clubId).get(roleId);
		}
		return null;
	}
	
	public static Map<Integer, RoleClubMemberInfo> getRoleClubMemberMap(int clubId){
		return map.get(clubId) == null ? new ConcurrentHashMap<Integer, RoleClubMemberInfo>() : map.get(clubId);
	}
	
	public static void addEvent(ClubEventInfo info){
		if(info == null){
			return;
		}
		if(!eventMap.containsKey(info.getClubId())){
			List<ClubEventInfo> list = new ArrayList<ClubEventInfo>();
			eventMap.put(info.getClubId(), list);
		}
		
		if(!roleEventMap.containsKey(info.getRoleId())){
			List<ClubEventInfo> list = new ArrayList<ClubEventInfo>();
			roleEventMap.put(info.getRoleId(), list);
		}
		
		eventMap.get(info.getClubId()).add(info);
		roleEventMap.get(info.getRoleId()).add(info);
		
	}
	
	public static List<ClubEventInfo> getEventListByClubId(int clubId){
		return eventMap.get(clubId);
	}
	
	public static List<ClubEventInfo> getEventListByRoleId(int roleId){
		return roleEventMap.get(roleId);
	}
	
	
	public static void removeClubMemberMap(int clubId, int roleId){
		if(map.get(clubId) != null){
			map.get(clubId).remove(roleId);
		}
		
	}
	
	public static void removeClubMap(int clubId){
		map.remove(clubId);
	}
	
	/**
	 * 获取角色加入的公会
	 * @param roleId
	 * @return
	 */
	public static int getRoleJoinClubId(int roleId){
		Map<Integer, RoleClubMemberInfo> rMap = null;
		for(Integer clubId : map.keySet()){
			rMap = map.get(clubId);
			if(rMap == null || rMap.size() <= 0){
				continue;
			}
			
			if(rMap.containsKey(roleId) && rMap.get(roleId).getStatus() != RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
				return clubId;
			}
		}
		
		return 0;
	}
	
	public static void removeClubEvent(int clubId){
		eventMap.remove(clubId);
	}
	
	/**
	 * 删除包含角色的公会
	 * @param roleId
	 */
	public static void removeClubRoleByRoleId(int roleId){
		Iterator<Map.Entry<Integer, Map<Integer, RoleClubMemberInfo>>> ite = map.entrySet().iterator();
		Map.Entry<Integer, Map<Integer, RoleClubMemberInfo>> entry = null;
		while(ite.hasNext()){
			entry = ite.next();
			if(entry.getValue() != null && entry.getValue().containsKey(roleId)){
				entry.getValue().remove(roleId);
			}
			
		}
	}
	
	/**
	 * 获取角色公会名
	 * @param roleId
	 * @return
	 */
	public static String getRoleClubName(int roleId){
		if(RoleClubInfoMap.getRoleClubInfoByClubId(getRoleJoinClubId(roleId)) != null){
			return RoleClubInfoMap.getRoleClubInfoByClubId(getRoleJoinClubId(roleId)).getClubName();
			
		}else{
			
			return "";
		}
	}
	
	/**
	 * GM命令用 只删除角色事件的Map 公会的暂时保留 不做删除 重启服务器后数据消失
	 * @param roleId
	 */
	public static void removeClubEventByRoleId(int roleId){
		if(roleEventMap.get(roleId) != null){
			roleEventMap.remove(roleId);
		}
	}
	
	/**
	 * 根据数据库主键删除时间
	 * @param id
	 */
	public static void removeClubEventById(int id){
		Iterator<Map.Entry<Integer, List<ClubEventInfo>>> ite = eventMap.entrySet().iterator();
		Map.Entry<Integer, List<ClubEventInfo>> entry = null;
		List<ClubEventInfo> list = null;
		while(ite.hasNext()){
			entry = ite.next();
			if(entry == null){
				continue;
			}
			 
			list = entry.getValue();
			 
			if(list == null){
				continue;
			}
			 
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).getId() == id){
					list.remove(i);
					i--;
					break;
				}
			}
			 
		}
		
		ite = roleEventMap.entrySet().iterator();
		 
		while(ite.hasNext()){
			entry = ite.next();
			if(entry == null){
				continue;
			}
			 
			list = entry.getValue();
			 
			if(list == null){
				continue;
			}
			 
			for(int i = 0; i < list.size(); i++){
				if(list.get(i).getId() == id){
					list.remove(i);
					i--;
					break;
				}
			}
			 
		}
		 
	}
	
	public static Set<Integer> getRoleRequestClubIdSet(int roleId){
		Set<Integer> set = new HashSet<Integer>();
		Map<Integer, RoleClubMemberInfo> clubMemberMap = null;
		for(Integer clubId : map.keySet()){
			clubMemberMap = RoleClubMemberInfoMap.getRoleClubMemberMap(clubId);
			if(clubMemberMap != null && clubMemberMap.get(roleId) != null
					 && clubMemberMap.get(roleId).getStatus() == RoleClubMemberInfo.CLUB_REQUEST_MEMBER){
				set.add(clubId);
			}
		}
		
		return set;
	}
	
}
