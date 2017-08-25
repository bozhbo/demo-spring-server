package com.snail.webgame.game.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.fight.mutual.rank.MutualQueryRankRes;

/**
 * 
 * 类介绍:对攻战全局排行
 *
 * @author zhoubo
 * @2015年5月26日
 */
public class FightMutualRankList {

	private static List<Integer> list = new ArrayList<Integer>();
	private static Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	private static Map<Integer, MutualQueryRankRes> respMap = new HashMap<Integer, MutualQueryRankRes>();
	
	/**
	 * 服务器启动初始化后全局角色排名
	 */
	public static void initSort() {
		Set<Entry<Integer, RoleInfo>> set = RoleInfoMap.getMap().entrySet();
		List<RoleInfo> tempList = new ArrayList<RoleInfo>();
		
		for (Entry<Integer, RoleInfo> entry : set) {
			RoleInfo roleInfo = entry.getValue();
			
			if (roleInfo.getScoreValue() == 0) {
				continue;
			}
			
			tempList.add(roleInfo);
		}
		
		if (tempList.size() > 0) {
			Collections.sort(tempList, new Comparator<RoleInfo>() {
				@Override
				public int compare(RoleInfo o1, RoleInfo o2) {
					return o2.getScoreValue() - o1.getScoreValue();
				}
			});
			
			for (RoleInfo roleInfo2 : tempList) {
				if (list.size() >= GameValue.MUTUAL_RANK_MAX_NUM) {
					break;
				}
				
				map.put(roleInfo2.getId(), roleInfo2.getScoreValue());
				list.add(roleInfo2.getId());
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo2);
				respMap.put(roleInfo2.getId(), createMutualQueryRankRes(roleInfo2.getScoreValue(), roleInfo2.getRoleName(), roleInfo2.getId(), heroInfo != null ? heroInfo.getHeroNo() : 0, heroInfo != null ? heroInfo.getHeroLevel() : 0));
			}
		}
	}
	
	/**
	 * 积分变动，重新排序
	 * 
	 * @param roleInfo
	 */
	public static void addSort(RoleInfo roleInfo) {
		synchronized (list) {
			if (list.size() == 0) {
				list.add(roleInfo.getId());
				map.put(roleInfo.getId(), roleInfo.getScoreValue());
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				respMap.put(roleInfo.getId(), createMutualQueryRankRes(roleInfo.getScoreValue(), roleInfo.getRoleName(), roleInfo.getId(), heroInfo != null ? heroInfo.getHeroNo() : 0, heroInfo != null ? heroInfo.getHeroLevel() : 0));
				return;
			}
			
			if (!map.containsKey(roleInfo.getId())) {
				// 第一次进入排名列表，先添加
				if (list.size() >= GameValue.MUTUAL_RANK_MAX_NUM) {
					// 超过上限
					int scoreValue = map.get(list.get(list.size() - 1));
					
					if (scoreValue >= roleInfo.getScoreValue()) {
						return;
					}
					
					// 移除最后一个
					map.remove(list.remove(list.size() - 1));
					respMap.remove(list.remove(list.size() - 1));
				}
			} else {
				// 移除角色排名
				list.remove(Integer.valueOf(roleInfo.getId()));
			}
			
			map.put(roleInfo.getId(), roleInfo.getScoreValue());
			
			HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
			respMap.put(roleInfo.getId(), createMutualQueryRankRes(roleInfo.getScoreValue(), roleInfo.getRoleName(), roleInfo.getId(), heroInfo != null ? heroInfo.getHeroNo() : 0, heroInfo != null ? heroInfo.getHeroLevel() : 0));
			
			boolean flag = false;
			
			for (int i = 0; i < list.size(); i++) {
				if (roleInfo.getScoreValue() >= map.get(list.get(i))) {
					list.add(i, roleInfo.getId());
					flag = true;
					break;
				}
			}
			
			if (!flag) {
				list.add(roleInfo.getId());
			}
		}
	}
	
	/**
	 * 返回全局排名
	 * 
	 * @return	全局排名集合
	 */
	public static List<MutualQueryRankRes> getList() {
		synchronized(list) {
			List<MutualQueryRankRes> respList = new ArrayList<MutualQueryRankRes>();
			
			for (Integer roleId : list) {
				respList.add(respMap.get(roleId));
			}
			
			return respList;
		}
	}
	
	/**
	 * 获取角色排名
	 * 
	 * @param roleInfo	角色信息
	 * @return	int 
	 */
	public static int getRoleIndex(RoleInfo roleInfo) {
		if (roleInfo == null) {
			return -1;
		}
		
		if (roleInfo.getScoreValue() == 0) {
			return -1;
		}
		
		synchronized (list) {
			if (list.size() == 0) {
				return -1;
			}
			
			return list.indexOf(Integer.valueOf(roleInfo.getId()));
		}
	}
	
	private static MutualQueryRankRes createMutualQueryRankRes(int scroeValue, String name, int roleId, int heroId, int heroLevel) {
		MutualQueryRankRes mutualQueryRankRes = new MutualQueryRankRes();
		
		mutualQueryRankRes.setHeroId(heroId);
		mutualQueryRankRes.setHeroLevel(heroLevel);
		mutualQueryRankRes.setName(name);
		mutualQueryRankRes.setRoleId(roleId);
		mutualQueryRankRes.setScroeValue(scroeValue);
		
		return mutualQueryRankRes;
	}
}
